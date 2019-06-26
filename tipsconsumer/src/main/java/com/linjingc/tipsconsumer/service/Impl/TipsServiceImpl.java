package com.linjingc.tipsconsumer.service.Impl;

import com.linjingc.tipsconsumer.service.TipsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TipsServiceImpl implements TipsService {
    @Resource
    private TipsService tipsService;

    @Override
    public String sendTip(String tip) {
        return tipsService.sendTip(tip);
    }

    @Override
    public String index() {
        return tipsService.index();
    }
}
