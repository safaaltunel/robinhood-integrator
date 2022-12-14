package com.safaaltunel.robinhoodintegrator.proxy;

import com.safaaltunel.robinhoodintegrator.aspect.ToLog;
import com.safaaltunel.robinhoodintegrator.model.response.Instruments;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "instrument", url = "${robinhood.url}")
public interface InstrumentProxy {

    @GetMapping("/instruments/")
    @ToLog
    Instruments getInstrument(@RequestParam String symbol);
}
