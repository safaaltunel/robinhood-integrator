package com.safaaltunel.robinhoodintegrator.proxy;

import com.safaaltunel.robinhoodintegrator.model.response.Markets;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "market", url = "${robinhood.url}")
public interface MarketProxy {

    @GetMapping("/markets")
    Markets getMarkets();

}
