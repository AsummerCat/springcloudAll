package com.linjingc.loginserversessiontoken.config.security;

import com.linjingc.loginserversessiontoken.utils.JwtConfig;
import com.linjingc.loginserversessiontoken.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT 整合Spring Security
 * 访问认证类
 *
 * @author cxc
 * @date 2019年6月27日15:09:19
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtConfig jwtConfig;
    private JwtUtils jwtUtils;

    JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, JwtUtils jwtUtils) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(jwtUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (StringUtils.isEmpty(tokenHeader) || !tokenHeader.startsWith(jwtUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     * @param tokenHeader
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(jwtUtils.TOKEN_PREFIX, "");
        String username = jwtUtils.getUsername(token);
        if (StringUtils.isNotEmpty(username)) {
            return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        }
        return null;
    }
}
