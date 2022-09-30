FROM openjdk:8-jdk-alpine
RUN apk --no-cache add curl
WORKDIR /tmp/workdir
COPY target/demo-*.jar demo.jar
COPY ./opentelemetry-javaagent.jar opentelemetry-javaagent.jar
ENTRYPOINT ["java", "-javaagent:/tmp/workdir/opentelemetry-javaagent.jar","-Dotel.exporter.otlp.traces.endpoint=https://tempo-us-central1.grafana.net:443","-Dotel.exporter.otlp.traces.headers=Authorization=Bearer 160639:eyJrIjoiYjkzMDA1NDE0ODRhYWRjYzgwNjg0YTJlNzcyZmY0MThmMDNiOTRjMiIsIm4iOiJ0ZXN0IiwiaWQiOjYwMTM4MH0=","-Dotel.service.name=welcome-cloud","-Dotel.traces.exporter=otlp","-jar", "demo.jar", "-Dexternal.param=${EXTERNAL_PARAM}"]
EXPOSE 80
