package com.safaaltunel.robinhoodintegrator.service;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import com.safaaltunel.robinhoodintegrator.exception.InstrumentNotFoundException;
import com.safaaltunel.robinhoodintegrator.repository.InstrumentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InstrumentServiceTest {

    @Autowired
    private InstrumentService instrumentService;

    @MockBean
    private AsyncService asyncService;

    @MockBean
    private InstrumentRepository instrumentRepository;

    private static Instrument instrument;


    @BeforeAll
    static void setUp() {
        instrument = Instrument.builder()
                .id(1L)
                .symbol("AAPL")
                .name("Apple Inc. Common Stock")
                .customName("Apple")
                .marketId(6L)
                .build();

    }

    @Test
    void syncInstruments() {
        Instrument emptyInstrument = Instrument.builder().build();
        List<Instrument> emptyInstrumentList = List.of(emptyInstrument);
        List<Instrument> instrumentList = List.of(instrument);

        Mockito.when(instrumentRepository.findAll())
                .thenReturn(emptyInstrumentList);

        Mockito.when(asyncService.getInstrumentAsync(emptyInstrument))
                .thenReturn(CompletableFuture.completedFuture(instrument));

        Mockito.when(instrumentRepository.saveAll(instrumentList))
                .thenReturn(instrumentList);

        List<Instrument> result = instrumentService.syncInstruments();

        assertEquals(instrument.getSymbol(), result.get(0).getSymbol());
        assertEquals(instrument.getName(), result.get(0).getName());
        assertEquals(instrument.getCustomName(), result.get(0).getCustomName());
        assertEquals(instrument.getMarketId(), result.get(0).getMarketId());

    }

    @Test
    void getAllInstruments() {
        List<Instrument> instrumentList = List.of(instrument);
        Mockito.when(instrumentRepository.findAll())
                .thenReturn(instrumentList);

        List<Instrument> result = instrumentService.getAllInstruments();

        assertEquals(instrument.getSymbol(), result.get(0).getSymbol());
        assertEquals(instrument.getName(), result.get(0).getName());
        assertEquals(instrument.getCustomName(), result.get(0).getCustomName());
        assertEquals(instrument.getMarketId(), result.get(0).getMarketId());
    }

    @Test
    @DisplayName("Test get instrument by symbol when provided symbol is found on the database")
    void getInstrumentBySymbol_whenSymbolValid() {
        Mockito.when(instrumentRepository.findBySymbol(instrument.getSymbol()))
                .thenReturn(instrument);

        Instrument result = instrumentService.getInstrumentBySymbol(instrument.getSymbol());

        assertEquals(instrument.getSymbol(), result.getSymbol());
        assertEquals(instrument.getName(), result.getName());
        assertEquals(instrument.getCustomName(), result.getCustomName());
        assertEquals(instrument.getMarketId(), result.getMarketId());
    }

    @Test
    @DisplayName("Test get instrument by symbol when provided symbol is not found on the database")
    void getInstrumentBySymbol_whenSymbolInvalid() {
        String invalidSymbol= "invalid_symbol";
        Mockito.when(instrumentRepository.findBySymbol(invalidSymbol))
                .thenReturn(null);

        assertThrows(InstrumentNotFoundException.class, () -> instrumentService.getInstrumentBySymbol(invalidSymbol));

    }
}