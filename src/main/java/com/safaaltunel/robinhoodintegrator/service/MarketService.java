package com.safaaltunel.robinhoodintegrator.service;

import com.safaaltunel.robinhoodintegrator.entity.Market;
import com.safaaltunel.robinhoodintegrator.exception.MarketsNotFoundException;
import com.safaaltunel.robinhoodintegrator.model.MarketResponse;
import com.safaaltunel.robinhoodintegrator.model.response.Markets;
import com.safaaltunel.robinhoodintegrator.proxy.MarketProxy;
import com.safaaltunel.robinhoodintegrator.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final MarketProxy marketProxy;

    public MarketService(MarketRepository marketRepository, MarketProxy marketProxy) {
        this.marketRepository = marketRepository;
        this.marketProxy = marketProxy;
    }

    public List<Market> syncMarkets() {
        Markets markets = marketProxy.getMarkets();

        if(markets.getResults().size() == 0) {
            throw new MarketsNotFoundException();
        }

        List<Market> marketsList = new ArrayList<>();
        for(MarketResponse marketResponse : markets.getResults()) {
            Optional<Market> marketInRepo = marketRepository.findByCode(marketResponse.getMic());
            Market market;
            if(marketInRepo.isEmpty()) {
                market = new Market();
            }
            else {
                market = marketInRepo.get();
            }
            market.setCode(marketResponse.getMic());
            market.setSymbol(marketResponse.getAcronym());
            market.setName(marketResponse.getName());
            market.setWebsite(marketResponse.getWebsite());
            market.setCountry(marketResponse.getCountry());

            marketsList.add(market);
        }

        return (List<Market>) marketRepository.saveAll(marketsList);
    }

}
