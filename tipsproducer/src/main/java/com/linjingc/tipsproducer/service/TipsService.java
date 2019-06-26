package com.linjingc.tipsproducer.service;

import org.springframework.stereotype.Service;

@Service
public interface TipsService {

    String sendTip(String tip);

    String index();
}
