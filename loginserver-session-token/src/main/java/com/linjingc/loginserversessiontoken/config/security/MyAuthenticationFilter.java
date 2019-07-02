package com.linjingc.loginserversessiontoken.config.security;

import com.linjingc.loginserversessiontoken.config.jwt.JwtUtils;
import com.linjingc.loginserversessiontoken.entity.BasicUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * JWT 整合Spring Security 密码校验类
 *
 * @author cxc
 * @date 2019年6月27日13:08:02
 */
public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public MyAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {

        super(new AntPathRequestMatcher("/login", "POST"));
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        BasicUser loginUser = new BasicUser();
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>());
        Authentication authentication = this.authenticationManager.authenticate(authRequest);
        if (authentication != null) {
            super.setContinueChainBeforeSuccessfulAuthentication(true);
            //因为是使用seesion管理的话 这边使用自定义注册token
            setSessionAuthenticationStrategy(new RegisterTokenAuthenticationStrategy(jwtUtils));
        }
        return authentication;
    }
}
