package com.pratilipi.MainServer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Client {

    public static void main(String[] srgs){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/spring-rest/foos";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
    }
}
