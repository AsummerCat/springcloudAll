package com.linjingc.configclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {

    @Value("${test.name}")
    private String test;

    @RequestMapping("/")
    public String getTest() {
        return test;
    }
}
