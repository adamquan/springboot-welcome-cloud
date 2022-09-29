package com.serverless.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @Value("${external.param}")
    private String externalParam;

    @Value("${project.version}")
    private String appVer;

    @Value("${cloud.url}")
    private String cloudUrl;

    @GetMapping(value = "/")
    private ResponseEntity<Object> defaultHealthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/welcome", produces = "application/json")
    private ResponseEntity<Object> welcome() {

        // Make a remote call, to demo invoking another Fargate task
        URL url;
        try {
            url = new URL(cloudUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
		    System.out.println("Cloud GET Response Code :: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return a simple json response
        Map<String, String> respMap = new HashMap<>();
        respMap.put("app", "Fargate Demo App");
        respMap.put("version", appVer);
        respMap.put("message", "Welcome!");
        respMap.put("externalParam", externalParam);

        return new ResponseEntity<>(respMap, HttpStatus.OK);

    }

    @GetMapping(value = "/cloud", produces = "application/json")
    private ResponseEntity<Object> cloud() {
        Map<String, String> respMap = new HashMap<>();
        respMap.put("app", "Fargate Demo App");
        respMap.put("version", appVer);
        respMap.put("message", "Cloud!");
        respMap.put("externalParam", externalParam);

        return new ResponseEntity<>(respMap, HttpStatus.OK);

    }
}
