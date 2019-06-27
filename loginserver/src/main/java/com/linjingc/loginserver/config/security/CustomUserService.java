package com.linjingc.loginserver.config.security;

import com.linjingc.loginserver.entity.BasicUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义查询用户    Security接口
 */
public class CustomUserService implements UserDetailsService { //实现 这个用户验证接口
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {   //这里重构了原来的的方法
        //User user = userRepository.findByUsername(s); //jpa 查询数据库
        BasicUser user = new BasicUser();
        user.setUsername("ad");    //模拟查询
        user.setPassword("123456");


        List<SimpleGrantedAuthority> auths = new ArrayList<>();  //权限
        auths.add(new SimpleGrantedAuthority("add"));//这里添加权限 可以添加多个权限
        auths.add(new SimpleGrantedAuthority("update"));
        auths.add(new SimpleGrantedAuthority("delete"));
        auths.add(new SimpleGrantedAuthority("USER"));
        auths.add(new SimpleGrantedAuthority("ROLE_AAA"));  //任何以ROLE_开头的权限都被视为角色


        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        System.out.println("userName:" + userName);
        System.out.println("username:" + user.getUsername() + ";password:" + user.getPassword());
        return new User(user.getUsername(),
                user.getPassword(), auths);
    }
}