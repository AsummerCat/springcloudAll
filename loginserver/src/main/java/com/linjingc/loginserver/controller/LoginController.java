package com.linjingc.loginserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录
 *
 * @author cxc
 * @date 2019年6月27日10:04:20
 */
@Controller
public class LoginController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
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


}
