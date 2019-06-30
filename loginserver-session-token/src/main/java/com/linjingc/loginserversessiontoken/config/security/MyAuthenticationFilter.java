package com.linjingc.loginserversessiontoken.config.security;

import com.alibaba.fastjson.JSON;
import com.linjingc.loginserversessiontoken.entity.BasicUser;
import com.linjingc.loginserversessiontoken.utils.JwtConfig;
import com.linjingc.loginserversessiontoken.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * JWT 整合Spring Security 密码校验类
 *
 * @author cxc
 * @date 2019年6月27日13:08:02
 */
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
        //if (authentication != null) {
        //    super.setContinueChainBeforeSuccessfulAuthentication(true);
        //}
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        BasicUser jwtUser = new BasicUser();
        jwtUser.setUsername(userDetails.getUsername());

        //签发token
        String token = jwtUtils.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(jwtUser), userDetails.getUsername());
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        response.setHeader(jwtUtils.TOKEN_HEADER, jwtUtils.TOKEN_PREFIX + token);
        //登录成功后转发到首页
        request.getRequestDispatcher("/").forward(request, response);
    }
}
