package com.linjingc.loginzuuloauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
@EnableZuulProxy
public class LoginzuuloauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginzuuloauthApplication.class, args);
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
