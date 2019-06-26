package com.linjingc.tipsconsumer.service.fallback;

import com.linjingc.tipsconsumer.service.TipsService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TipsServerFallBackFactory implements FallbackFactory<TipsService> {
    private static final Logger logger = LoggerFactory.getLogger(TipsServerFallBackFactory.class);

    @Override
    public TipsService create(Throwable throwable) {
        logger.info("fallback reason was: {} ", throwable.getMessage());
        return new TipsService() {
            @Override
            public String sendTip(String tip) {
                return "sendTip请求失败";
            }

            @Override
            public String index() {
                return "index请求失败";
            }
        };

    }
}
