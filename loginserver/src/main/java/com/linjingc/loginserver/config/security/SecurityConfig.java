package com.linjingc.loginserver.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * @author cxc
 * @date 2018/10/16 15:36
 * Security配置类
 */
@Slf4j
@Configuration
@EnableWebSecurity //注解开启Security
@EnableGlobalMethodSecurity(prePostEnabled = true)   //开启Security注解  然后在controller中就可以使用方法注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    UserDetailsService customUserService() {
        return new CustomUserService();
    }

    /**
     * 这里可以设置忽略的路径或者文件
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略css.jq.img等文件
        log.info("--------------------------SecurityConfig忽略文件及路径----------------------------");
        web.ignoring().antMatchers("/**.html", "/**.css", "/img/**", "/**.js", "/third-party/**");
    }


    /**
     * 这里是权限控制配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("--------------------------SecurityConfig加载成功----------------------------");

        // 去掉 CSRF
        http.csrf().disable()
                .authorizeRequests()
                //不需要权限访问
                .antMatchers("/home", "/**.html", "/**.html", "/**.css", "/img/**", "/**.js", "/third-party/**").permitAll()
                //该路径需要验证通过
                .antMatchers("/", "/index/", "/index/**").authenticated()
                //该路径需要角色  or 权限XXX
                .antMatchers("/lo").access("hasRole('AAA') or hasAuthority('add')")
                //还有一种方法 就是在方法名称上写注解    @PreAuthorize("hasAnyAuthority('USER','delete')")   //注解拦截权限
                //任何以ROLE_开头的权限都被视为角色
                .anyRequest().authenticated() //都要权限  放在最后
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .and()
                .formLogin()
                //自定义登录页
                  .loginPage("/login")
//                //登录成功页面
                .defaultSuccessUrl("/hello")
                .permitAll()
                .and()
                //添加jwt验证
                .addFilter((new JWTAuthenticationFilter(authenticationManager())))
                .addFilter((new JWTAuthorizationFilter(authenticationManager())))
                .logout()
                //退出登录后的默认url是"/home"
                .logoutSuccessUrl("/byeBye")
                .permitAll()
                // 不需要session
                .invalidateHttpSession(true);

    }


    /**
     * 这里是验证登录并且赋予权限
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("--------------------------Security自定义验证登录赋予权限方法加载成功----------------------------");

        /**
         * 方式一 写在内存中的角色
         */
        //在此处应用自定义PasswordEncoder
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                //写在内存中的角色
                .withUser("user").password("password").roles("USER")
                .and() //这个是指可以写多个
                .withUser("admin").password("$2a$10$rqOD.4PCiTJUm3BrNDjxfO287rWocQCjT7p/TE3YwTi6LhSXSX0Ba").authorities("ROLE_USER", "ROLE_ADMIN");

        /**
         * 方式二 数据库查询用户信息
         */
        //     auth.userDetailsService(customUserService()).passwordEncoder(bCryptPasswordEncoder());//添加自定义的userDetailsService认证  //现在已经要加密.passwordEncoder(new MyPasswordEncoder())
        //    auth.eraseCredentials(false);   //这里是清除还是不清除登录的密码  SecurityContextHolder中
    }

    /**
     * 不加密 官方已经不推荐了
     * 自定义密码加密器
     */
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
    /**
     * BCryptPasswordEncoder 使用BCrypt的强散列哈希加密实现，并可以由客户端指定加密的强度strength，强度越高安全性自然就越高，默认为10.
     * 自定义密码加密器
     * BCryptPasswordEncoder(int strength, SecureRandom random)
     * SecureRandom secureRandom3 = SecureRandom.getInstance("SHA1PRNG");
     */
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 防止注解使用不了
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
