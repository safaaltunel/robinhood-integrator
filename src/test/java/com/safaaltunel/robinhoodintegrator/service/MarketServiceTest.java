package com.safaaltunel.robinhoodintegrator.service;

import com.safaaltunel.robinhoodintegrator.entity.Market;
import com.safaaltunel.robinhoodintegrator.exception.MarketsNotFoundException;
import com.safaaltunel.robinhoodintegrator.model.MarketResponse;
import com.safaaltunel.robinhoodintegrator.model.response.Markets;
import com.safaaltunel.robinhoodintegrator.proxy.MarketProxy;
import com.safaaltunel.robinhoodintegrator.repository.MarketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketServiceTest {

    @Autowired
    private MarketService marketService;

    @MockBean
    private MarketRepository marketRepository;

    @MockBean
    private MarketProxy marketProxy;

    @Test
    @DisplayName("Test sync markets when no market is returned by RobinHood API")
    void syncMarketsWhenNoMarketsFound() {
        Markets emptyMarkets = new Markets();
        emptyMarkets.setResults(List.of());
        Mockito.when(marketProxy.getMarkets())
                .thenReturn(emptyMarkets);

        assertThrows(MarketsNotFoundException.class, () -> marketService.syncMarkets());
    }

    @Test
    @DisplayName("Test sync markets when happy flow occurs")
    void syncMarketsWhenHappyFlow() {
        MarketResponse marketResponse = MarketResponse.builder()
                .mic("IEXG")
                .acronym("IEX")
                .name("IEX Market")
                .country("US - United States of America")
                .website("www.iextrading.com")
                .build();
        List<MarketResponse> marketResponseList = List.of(marketResponse);
        Markets markets = new Markets();
        markets.setResults(marketResponseList);

        Market market = Market.builder()
                .id(1L)
                .code("IEXG")
                .symbol("IEX")
                .name("IEX Market")
                .country("US - United States of America")
                .website("www.iextrading.com")
                .build();

        Market emptyMarket = Market.builder().build();

        List<Market> marketList = List.of(emptyMarket);

        Mockito.when(marketProxy.getMarkets())
                .thenReturn(markets);

        Mockito.when(marketRepository.findByCode(market.getCode()))
                        .thenReturn(Optional.of(emptyMarket));

        Mockito.when(marketRepository.saveAll(marketList))
                        .thenReturn(marketList);

        List<Market> result = marketService.syncMarkets();
        assertEquals(market.getCode(), result.get(0).getCode());
        assertEquals(market.getSymbol(), result.get(0).getSymbol());
        assertEquals(market.getName(), result.get(0).getName());
        assertEquals(market.getWebsite(), result.get(0).getWebsite());
        assertEquals(market.getCountry(), result.get(0).getCountry());
    }
}