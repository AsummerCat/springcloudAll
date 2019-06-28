package com.linjingc.loginserver.config.security;

import com.alibaba.fastjson.JSON;
import com.linjingc.loginserver.entity.BasicUser;
import com.linjingc.loginserver.utils.JwtConfig;
import com.linjingc.loginserver.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtConfig jwtConfig;
    private JwtUtils jwtUtils;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        //限制只有post请求才能进入登录操作
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("认证方法请使用POST请求: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            try {
                BasicUser loginUser = new BasicUser();
                loginUser.setUsername(username);
                loginUser.setPassword(password);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>());
                this.setDetails(request, authRequest);
                return authenticationManager.authenticate(authRequest);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

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


    /**
     * 这是验证失败时候调用的方法
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
