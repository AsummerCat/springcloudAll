package com.linjingc.loginserversessiontoken.config.security;

import com.linjingc.loginserversessiontoken.entity.BasicUser;
import com.linjingc.loginserversessiontoken.utils.JwtConfig;
import com.linjingc.loginserversessiontoken.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
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
 * JWT 整合Spring Security 登录密码校验类
 * 这里开启后 SecurityConfig中 需要移除  .successHandler(myAuthenticationSuccessHandler)
 * 这里开启后 SecurityConfig中 需要添加   http.addFilterBefore(new MyAuthenticationFilter(authenticationManager(), jwtConfig, jwtUtils), UsernamePasswordAuthenticationFilter.class);
 *
 * @author cxc
 * @date 2019年6月27日13:08:02
 */
@Slf4j
public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private JwtConfig jwtConfig;
    private JwtUtils jwtUtils;

    public MyAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, JwtUtils jwtUtils) {

        super(new AntPathRequestMatcher("/login", "POST"));
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
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
            // setSessionAuthenticationStrategy(new RegisterTokenAuthenticationStrategy(jwtUtils));
            setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        }
        return authentication;
    }

//    @Override
////    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
////        log.info("--------------------------SecurityConfig自定义token会话认证加载----------------------------");
////        //UserDetails principal = (UserDetails) authentication.getPrincipal();
////        UserDetails principal =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////
////        String token = jwtUtils.createJWT(UUID.randomUUID().toString(), principal.getUsername() + "登录成功", principal.getUsername());
////        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
////        response.setHeader(jwtUtils.TOKEN_HEADER, jwtUtils.TOKEN_PREFIX + token);
////
////        super.successfulAuthentication(request, response, chain, authResult);
////    }
}
