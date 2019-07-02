package com.linjingc.loginserversessiontoken.config.security;

import com.linjingc.loginserversessiontoken.config.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 自定义token会话身份验证策略
 *
 * @author cxc
 * @date 2019年7月1日14:26:51
 */
@Slf4j
@Component
public class RegisterTokenAuthenticationStrategy implements SessionAuthenticationStrategy {
    private JwtUtils jwtUtils;

    public RegisterTokenAuthenticationStrategy(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        log.info("--------------------------SecurityConfig自定义token会话认证加载----------------------------");
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.createJWT(UUID.randomUUID().toString(), principal.getUsername() + "登录成功", principal.getUsername());
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        response.setHeader(jwtUtils.tokenHeader, jwtUtils.tokenPrefix + token);
    }
}
