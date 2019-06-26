package com.linjingc.tipsproducer.controller;

import com.linjingc.tipsproducer.service.TipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TipsController {
    @Autowired
    private TipsService tipsServiceImpl;

    @RequestMapping(value = "sendTip/{tip}", method = RequestMethod.GET)
    public String sendTip(@PathVariable String tip) {
        System.out.println("获取到请求sentTIP");
        return tipsServiceImpl.sendTip(tip);
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        System.out.println("获取到请求index");
        return tipsServiceImpl.index();
    }

}
