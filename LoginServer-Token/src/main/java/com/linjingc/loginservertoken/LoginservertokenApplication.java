package com.linjingc.loginservertoken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LoginservertokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginservertokenApplication.class, args);
	}

}
