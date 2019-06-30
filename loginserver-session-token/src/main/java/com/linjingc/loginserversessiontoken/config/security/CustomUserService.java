package com.linjingc.loginserversessiontoken.config.security;

import com.linjingc.loginserversessiontoken.entity.BasicUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 自定义查询用户    Security接口
 */
public class CustomUserService implements UserDetailsService { //实现 这个用户验证接口
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {   //这里重构了原来的的方法
        //User user = userRepository.findByUsername(s); //jpa 查询数据库
        BasicUser user = new BasicUser();
        //模拟查询
        user.setUsername("ad");
        user.setPassword("123456");

        //权限
        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        //这里添加权限 可以添加多个权限
        auths.add(new SimpleGrantedAuthority("add"));
        auths.add(new SimpleGrantedAuthority("update"));
        auths.add(new SimpleGrantedAuthority("delete"));
        auths.add(new SimpleGrantedAuthority("USER"));
        //任何以ROLE_开头的权限都被视为角色
        auths.add(new SimpleGrantedAuthority("ROLE_AAA"));


        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return new User(user.getUsername(), user.getPassword(), auths);
    }
}