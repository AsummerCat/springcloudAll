package com.linjingc.loginserversessiontoken;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServerSessionTokenApplicationTests {
    @Value("${jwt.secret}")
    private String secret;


    @Test
    public void contextLoads() {
        System.out.println(">>>>>>>>>>>>>>>>.."+secret);
    }

}
