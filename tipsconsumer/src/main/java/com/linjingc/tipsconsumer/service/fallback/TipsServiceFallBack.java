package com.linjingc.tipsconsumer.service.fallback;

import com.linjingc.tipsconsumer.service.TipsService;
import org.springframework.stereotype.Component;

@Component
public class TipsServiceFallBack implements TipsService {

    @Override
    public String sendTip(String tip) {
        return "链接失败";
    }

    @Override
    public String index() {
        return "查询首页失败";
    }
}
