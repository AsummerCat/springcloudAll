package com.linjingc.loginserversessiontoken.config.jwt.aop;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Component
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtLoginAop {
}
