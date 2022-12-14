package com.safaaltunel.robinhoodintegrator.controller;

import com.safaaltunel.robinhoodintegrator.entity.Market;
import com.safaaltunel.robinhoodintegrator.exception.MarketsNotFoundException;
import com.safaaltunel.robinhoodintegrator.service.MarketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarketController.class)
class MarketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarketService marketService;

    @Test
    @DisplayName("Test sync markets when no market is returned by RobinHood API")
    void syncMarkets_whenNoResponse() throws Exception {
        Mockito.when(marketService.syncMarkets())
                .thenThrow(new MarketsNotFoundException());

        mockMvc.perform(post("/sync/markets"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test sync markets when happy flow is occured")
    void syncMarkets_whenHappyFlow() throws Exception {
        Market market = Market.builder()
                .id(1L)
                .code("IEXG")
                .symbol("IEX")
                .name("IEX Market")
                .country("US - United States of America")
                .website("www.iextrading.com")
                .build();
        List<Market> marketList = List.of(market);
        Mockito.when(marketService.syncMarkets())
                .thenReturn(marketList);

        mockMvc.perform(post("/sync/markets"))
                .andExpect(status().isOk())
                .andExpect(content().string("[" +
                        "{\"id\":1," +
                        "\"code\":\"IEXG\"," +
                        "\"symbol\":\"IEX\"," +
                        "\"name\":\"IEX Market\"," +
                        "\"country\":\"US - United States of America\"," +
                        "\"website\":\"www.iextrading.com\"}" +
                        "]"));
    }
}