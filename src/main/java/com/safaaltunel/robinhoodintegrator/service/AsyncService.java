package com.safaaltunel.robinhoodintegrator.service;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import com.safaaltunel.robinhoodintegrator.entity.Market;
import com.safaaltunel.robinhoodintegrator.model.InstrumentResponse;
import com.safaaltunel.robinhoodintegrator.model.response.Instruments;
import com.safaaltunel.robinhoodintegrator.proxy.InstrumentProxy;
import com.safaaltunel.robinhoodintegrator.repository.MarketRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    private final InstrumentProxy instrumentProxy;
    private final MarketRepository marketRepository;



    public AsyncService(InstrumentProxy instrumentProxy, MarketRepository marketRepository) {
        this.instrumentProxy = instrumentProxy;
        this.marketRepository = marketRepository;
    }

    @Async
    public CompletableFuture<Instrument> getInstrumentAsync(Instrument instrument) {
        String symbol = instrument.getSymbol();
        Instruments instruments = instrumentProxy.getInstrument(symbol);

        List<InstrumentResponse> responses = instruments.getResults();
        if(responses.size() == 0) {
            instrument.setMarketId(-1L);
            return CompletableFuture.completedFuture(instrument);
        }
        InstrumentResponse instrumentResponse = responses.get(0);
        setInstrumentFields(instrumentResponse, instrument);

        return CompletableFuture.completedFuture(instrument);
    }

    public void setInstrumentFields(InstrumentResponse instrumentResponse, Instrument instrument) {
        instrument.setName(instrumentResponse.getName());
        instrument.setCustomName(instrumentResponse.getSimple_name());

        String marketUrl = instrumentResponse.getMarket();
        String[] tokens = marketUrl.split("/");
        String marketName = tokens[tokens.length - 1];
        Optional<Market> market = marketRepository.findByCode(marketName);
        if(market.isPresent()) {
            Long marketId = market.get().getId();
            instrument.setMarketId(marketId);
        } else {
            instrument.setMarketId(-1L);
        }
    }
}
