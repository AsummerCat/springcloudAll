package com.linjingc.tipsconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//开启Feign
@EnableFeignClients
@SpringCloudApplication
public class TipsConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TipsConsumerApplication.class, args);
    }

}
