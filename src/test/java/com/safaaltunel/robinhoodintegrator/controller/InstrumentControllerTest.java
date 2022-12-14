package com.safaaltunel.robinhoodintegrator.controller;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import com.safaaltunel.robinhoodintegrator.exception.InstrumentNotFoundException;
import com.safaaltunel.robinhoodintegrator.exception.InstrumentsNotSyncedException;
import com.safaaltunel.robinhoodintegrator.service.InstrumentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstrumentController.class)
class InstrumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static List<Instrument> instrumentList;

    @MockBean
    private InstrumentService  instrumentService;

    @BeforeAll
    static void setUp() {
        Instrument instrument = Instrument.builder()
                .id(1L)
                .symbol("AAPL")
                .name("Apple Inc. Common Stock")
                .customName("Apple")
                .marketId(6L)
                .build();

        instrumentList = List.of(instrument);
    }

    @Test
    void syncInstruments() throws Exception {

        Mockito.when(instrumentService.syncInstruments())
                .thenReturn(instrumentList);

        mockMvc.perform(post("/sync/instruments"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1,\"symbol\":\"AAPL\",\"name\":\"Apple Inc. Common Stock\",\"customName\":\"Apple\",\"marketId\":6}]"));

    }

    @Test
    @DisplayName("Test get all instruments when instruments are not synced with RobinHood API")
    void getAllInstruments_whenInstrumentsNotSynced() throws Exception {

        Mockito.when(instrumentService.getAllInstruments())
                .thenThrow(new InstrumentsNotSyncedException());

        mockMvc.perform(get("/instruments"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test get all instruments when happy flow occurs")
    void getAllInstruments_whenHappyFlow() throws Exception {

        Mockito.when(instrumentService.getAllInstruments())
                .thenReturn(instrumentList);

        mockMvc.perform(get("/instruments"))
                .andExpect(status().isOk())
                .andExpect(content().string("[" +
                        "{\"id\":1," +
                        "\"symbol\":\"AAPL\"," +
                        "\"name\":\"Apple Inc. Common Stock\"," +
                        "\"customName\":\"Apple\"," +
                        "\"marketId\":6}" +
                        "]"));
    }

    @Test
    @DisplayName("Test get instrument by symbol when instruments are not synced with RobinHood API")
    void getInstrumentBySymbol_whenInstrumentsNotSynced() throws Exception {
        Instrument instrument = instrumentList.get(0);
        String symbol = instrument.getSymbol();
        Mockito.when(instrumentService.getInstrumentBySymbol(symbol))
                .thenThrow(new InstrumentsNotSyncedException());

        mockMvc.perform(get("/instruments?symbol=" + symbol))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test get instrument by symbol when instrument symbol is not found")
    void getInstrumentBySymbol_whenInstrumentNotFound() throws Exception {
        Instrument instrument = instrumentList.get(0);
        String symbol = instrument.getSymbol();
        Mockito.when(instrumentService.getInstrumentBySymbol(symbol))
                .thenThrow(new InstrumentNotFoundException(symbol));

        mockMvc.perform(get("/instruments?symbol=" + symbol))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test get instrument by symbol when happy flow occurs")
    void getInstrumentBySymbol_whenHappyFlow() throws Exception {
        Instrument instrument = instrumentList.get(0);
        String symbol = instrument.getSymbol();
        Mockito.when(instrumentService.getInstrumentBySymbol(symbol))
                .thenThrow(new InstrumentNotFoundException(symbol));

        mockMvc.perform(get("/instruments?symbol=" + symbol))
                .andExpect(status().isBadRequest());
    }
}