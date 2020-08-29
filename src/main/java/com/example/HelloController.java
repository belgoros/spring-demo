package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${spring.application.name}")
    String applicationName;

    @GetMapping("/hello")
    public String sayHello(@RequestParam String greeting) {
        return applicationName + ": Welcome, " + greeting + "!";
    }
}
