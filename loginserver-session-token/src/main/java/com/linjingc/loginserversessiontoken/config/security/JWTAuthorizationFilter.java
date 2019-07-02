package com.linjingc.loginserversessiontoken.config.security;

import com.linjingc.loginserversessiontoken.config.jwt.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * JWT 整合Spring Security
 * 访问认证类
 *
 * @author cxc
 * @date 2019年6月27日15:09:19
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtUtils jwtUtils;

    JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(jwtUtils.tokenHeader);
        // 如果请求头中没有Authorization信息则直接放行了
        if (StringUtils.isEmpty(tokenHeader) || !tokenHeader.startsWith(jwtUtils.tokenPrefix)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     *
     * @param tokenHeader
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(jwtUtils.tokenPrefix, "");
        try {
            UserDetails user = jwtUtils.getUser(token);
            if (Objects.nonNull(user) && StringUtils.isNotEmpty(user.getUsername())) {
                return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("无法获取用户信息");
        }
        return null;
    }
}
