package com.pratilipi.MainServer.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloGradleController {

    @GetMapping("/hello")
    public String helloGradle() {
        return "Hello 15Gradle!";
    }

}