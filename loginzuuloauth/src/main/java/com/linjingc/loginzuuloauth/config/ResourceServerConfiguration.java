package com.linjingc.loginzuuloauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


/**
 * 配置资源服务器
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private static final String DEMO_RESOURCE_ID = "order";

    //内部关联了ResourceServerSecurityConfigurer和HttpSecurity。前者与资源安全配置相关，后者与http安全配置相关
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //resourceId 用于分配给可授予的clientId
        //stateless  标记以指示在这些资源上仅允许基于令牌的身份验证
        //tokenStore token的存储方式（上一章节提到）
        resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
//        /authenticationEntryPoint  认证异常流程处理返回
//               //tokenExtractor            token获取方式,默认BearerTokenExtractor
//               //                         从header获取token为空则从request.getParameter("access_token")
//             .authenticationEntryPoint(authenticationEntryPoint).tokenExtractor(unicomTokenExtractor);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.requestMatchers().anyRequest()
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                //配置order访问控制，必须认证过后才可以访问
                .antMatchers("/order/**").authenticated();
    }
}
