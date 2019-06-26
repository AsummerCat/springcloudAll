package com.linjingc.tipsproducer.service.Impl;

import com.linjingc.tipsproducer.service.TipsService;
import org.springframework.stereotype.Service;

@Service
public class TipsServiceImpl implements TipsService {
    @Override
    public String sendTip(String tip) {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return tip;
    }

    @Override
    public String index() {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "返回首页";
    }
}
