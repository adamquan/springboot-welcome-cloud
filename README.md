# springboot-welcome-cloud
A simple SpringBoot application deployed as two Fargate tasks, demoing task communication.

Welcome --> Cloud

## Build instructions. 
All commands assume that this folder is your working directory.

```
mvn clean install

docker build . -t adamquan/springboot-welcome-cloud

docker push adamquan/springboot-welcome-cloud
```

If you are building the image from an ARM machine for AMD target, run

```
docker build . -t adamquan/springboot-welcome-cloud --platform amd64
```

## Run locally
All commands assume that this folder is your working directory. The `server.port` option configures the port the service is listening on. Default is `8080`.

Open two terminal windows, one to run the welcome service and one to run the cloud service.

Welcome: Start the service on port 80

To run the service without OTEL instrumentation:
```
./mvnw spring-boot:run
```

To run the service with OTEL instrumentation:
```
export JAVA_TOOL_OPTIONS="-javaagent:./opentelemetry-javaagent.jar"
export OTEL_TRACES_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=https://tempo-us-central1.grafana.net:443
export OTEL_SERVICE_NAME=springboot-welcome
export OTEL_EXPORTER_OTLP_TRACES_HEADERS="Authorization=Bearer 160639:eyJrIjoiYjkzMDA1NDE0ODRhYWRjYzgwNjg0YTJlNzcyZmY0MThmMDNiOTRjMiIsIm4iOiJ0ZXN0IiwiaWQiOjYwMTM4MH0="
./mvnw spring-boot:run -Dspring-boot.run.arguments="--cloud.url=http://localhost:9090"
```

Cloud: Start the service on port 9090

To run the service without OTEL instrumentation:
```
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

To run the service with OTEL instrumentation:
```
export JAVA_TOOL_OPTIONS="-javaagent:./opentelemetry-javaagent.jar"
export OTEL_TRACES_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=https://tempo-us-central1.grafana.net:443
export OTEL_SERVICE_NAME=springboot-cloud
export OTEL_EXPORTER_OTLP_TRACES_HEADERS="Authorization=Bearer 160639:eyJrIjoiYjkzMDA1NDE0ODRhYWRjYzgwNjg0YTJlNzcyZmY0MThmMDNiOTRjMiIsIm4iOiJ0ZXN0IiwiaWQiOjYwMTM4MH0="
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```
Once both services are started, you can access the service from [http://locahost/welcome](http://localhost/welcome)


## Run as Fargate tasks

When running as Fargate tasks, the `cloud.url` will be configured as container environment variable via teh Parameter Store.

See this [blog](https://betterprogramming.pub/how-to-host-a-serverless-springboot-web-application-using-aws-fargate-a3669d9eebd5) for details of creating Fargate tasks, and this [blog](https://medium.com/@giritech7/aws-set-environment-variable-for-ecs-tasks-a7ea3bf1d56f) or this [doc](https://docs.google.com/document/d/1iEwC_d6L80SzGQ4a8hakOTii9n2EeceR8OV5W40wTCY/edit?usp=sharing) for details on setting environment variables. 