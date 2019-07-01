package com.linjingc.loginservertoken.controller;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户登录Controller
 *
 * @author cxc
 * @date 2019年6月27日10:04:20
 */
@Controller
@ServletComponentScan
public class LoginController {


    /**
     * 欢迎页面
     *
     * @return
     */
    @RequestMapping(value="/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "进入首页";
    }

    /**
     * 登录授权页
     *
     * @return
     */
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 退出后跳转的页面
     *
     * @return
     */
    @RequestMapping("byeBye")
    @ResponseBody
    public String byeBye() {
        return "byeBye";
    }


    /**
     *
     */
    @RequestMapping("doPay")
    @ResponseBody
    public String doPay(HttpServletRequest request, HttpServletResponse response) {
        UserDetails principal =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpSession session = request.getSession();
        Object jessionid = session.getAttribute("JESSIONID");

        return "session有效 进入首页";
    }

}
