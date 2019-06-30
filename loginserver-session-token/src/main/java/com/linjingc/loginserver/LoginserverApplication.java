package com.linjingc.loginserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession(redisNamespace="heihei")

public class LoginserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginserverApplication.class, args);
    }

}
