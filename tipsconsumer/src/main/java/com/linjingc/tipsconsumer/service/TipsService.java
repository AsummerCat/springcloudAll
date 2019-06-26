package com.linjingc.tipsconsumer.service;

import com.linjingc.tipsconsumer.service.fallback.TipsServerFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@FeignClient(value = "Tip-Producer-Server", fallbackFactory = TipsServerFallBackFactory.class)
public interface TipsService {

    @RequestMapping(value = "sendTip/{tip}")
    String sendTip(@PathVariable("tip") String tip);

    @RequestMapping(value = "index")
    String index();
}
