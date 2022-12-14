package com.safaaltunel.robinhoodintegrator.service;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import com.safaaltunel.robinhoodintegrator.entity.Market;
import com.safaaltunel.robinhoodintegrator.model.InstrumentResponse;
import com.safaaltunel.robinhoodintegrator.model.response.Instruments;
import com.safaaltunel.robinhoodintegrator.proxy.InstrumentProxy;
import com.safaaltunel.robinhoodintegrator.repository.MarketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AsyncServiceTest {

    @Autowired
    private AsyncService asyncService;

    @MockBean
    private InstrumentProxy instrumentProxy;

    @MockBean
    private MarketRepository marketRepository;

    private Instrument instrument;

    @BeforeEach
    void setUp() {
        instrument = Instrument.builder()
                .id(1L)
                .symbol("AAPL")
                .name("Apple Inc. Common Stock")
                .customName("Apple")
                .marketId(6L)
                .build();
    }

    @Test
    @DisplayName("Test get instrument asynchronously when happy flow occurs")
    void getInstrumentAsync_whenHappyFlow() throws ExecutionException, InterruptedException {

        InstrumentResponse instrumentResponse = InstrumentResponse.builder()
                .name("Apple Inc. Common Stock")
                .simple_name("Apple")
                .market("https://api.robinhood.com/markets/XNAS/")
                .build();

        Instruments instruments = new Instruments();
        instruments.setResults(List.of(instrumentResponse));

        Mockito.when(instrumentProxy.getInstrument(instrument.getSymbol()))
                .thenReturn(instruments);

        Instrument result = asyncService.getInstrumentAsync(instrument).get();

        assertEquals(instrument.getSymbol(),result.getSymbol());
        assertEquals(instrument.getName(),result.getName());
        assertEquals(instrument.getCustomName(),result.getCustomName());
        assertEquals(instrument.getMarketId(),result.getMarketId());
    }

    @Test
    @DisplayName("Test get instrument asynchronously when no response returned from RobinHood API")
    void getInstrumentAsync_whenNoResponse() throws ExecutionException, InterruptedException {
        Instruments instruments = new Instruments();
        instruments.setResults(List.of());

        Mockito.when(instrumentProxy.getInstrument(instrument.getSymbol()))
                .thenReturn(instruments);

        Instrument result = asyncService.getInstrumentAsync(instrument).get();

        assertEquals(-1,result.getMarketId());
    }

    @Test
    @DisplayName("Test set instrument fields based on response from RobinHood API but no market record exists in market db")
    void setInstrumentFields_whenMarketNotExists() {

        Instrument emptyInstrument = Instrument.builder().build();

        InstrumentResponse instrumentResponse = InstrumentResponse.builder()
                .name("Apple Inc. Common Stock")
                .simple_name("Apple")
                .market("https://api.robinhood.com/markets/XNAS/")
                .build();

        Mockito.when(marketRepository.findByCode("invalid_market"))
                .thenReturn(Optional.empty());

        asyncService.setInstrumentFields(instrumentResponse, emptyInstrument);

        assertEquals(instrument.getName(), emptyInstrument.getName());
        assertEquals(instrument.getCustomName(), emptyInstrument.getCustomName());
        assertEquals(-1, emptyInstrument.getMarketId());
    }

    @Test
    @DisplayName("Test set instrument fields based on response from RobinHood API in happy flow")
    void setInstrumentFields_whenMarketExists() {

        Instrument emptyInstrument = Instrument.builder().build();

        InstrumentResponse instrumentResponse = InstrumentResponse.builder()
                .name("Apple Inc. Common Stock")
                .simple_name("Apple")
                .market("https://api.robinhood.com/markets/XNAS/")
                .build();

        Market market = Market.builder()
                        .id(instrument.getMarketId())
                        .build();

        Mockito.when(marketRepository.findByCode("XNAS"))
                .thenReturn(Optional.of(market));

        asyncService.setInstrumentFields(instrumentResponse, emptyInstrument);

        assertEquals(instrument.getName(), emptyInstrument.getName());
        assertEquals(instrument.getCustomName(), emptyInstrument.getCustomName());
        assertEquals(instrument.getMarketId(), emptyInstrument.getMarketId());
    }
}