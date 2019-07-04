package com.linjingc.loginserversessiontoken.config.jwt.aop;

import com.linjingc.loginserversessiontoken.config.jwt.JwtUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Configuration
public class JwtLoginAopConfiguration {
    @Autowired
    private JwtUtils jwtUtils;

    @Pointcut("execution(* com.linjingc.loginserversessiontoken.controller.*.*(..))&& @annotation(JwtLoginAop)")
    private void executeService() {
    }

    /**
     * 前置
     *
     * @param joinPoint
     */
    @Before("executeService()")
    public void before(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal.getUsername());
        jwtUtils.createToken(response);
    }
}
