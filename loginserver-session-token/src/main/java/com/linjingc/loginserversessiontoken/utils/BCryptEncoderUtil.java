package com.linjingc.loginserversessiontoken.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 在Security中使用BCryptPasswordEncoder做凭证校验器 如何创建注册密码
 *
 * @author cxc
 * @date 2018/10/16 17:27
 */
@Slf4j
public class BCryptEncoderUtil {
    public static void main(String[] args) {
        String pwd = encoder("heihei");
        matches("$2a$10$rqOD.4PCiTJUm3BrNDjxfO287rWocQCjT7p/TE3YwTi6LhSXSX0Ba", "heihei");
    }

    /**
     * 加密
     */
    public static String encoder(String password) {
        //进行加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String nowPassord = encoder.encode(password.trim());
        log.info("加密后的密码:{}", nowPassord);
        return nowPassord;
    }

    /**
     * 验证加密密码是否相等
     *
     * @oldPassword 加密后的密码
     * @password 原密码
     */
    public static boolean matches(String oldPassword, String password) {
        //进行加密
        BCryptPasswordEncoder matches = new BCryptPasswordEncoder();
        boolean flag = matches.matches(password, oldPassword);
//        log.info("密码校验:{}", flag ? "成功" : "失败");
        return flag;
    }
}