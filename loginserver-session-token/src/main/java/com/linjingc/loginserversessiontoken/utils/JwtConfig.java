package com.linjingc.loginserversessiontoken.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtConfig {
    @Value("${basic.cookie.maxAge}")
    private Integer cookieMaxAge;
}
