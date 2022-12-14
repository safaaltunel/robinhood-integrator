package com.safaaltunel.robinhoodintegrator.repository;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InstrumentRepositoryTest {

    @Autowired
    private InstrumentRepository instrumentRepository;


    @Test
    void whenFindBySymbol_thenReturnInstrument() {
        Instrument instrument = instrumentRepository.findBySymbol("AAPL");

        assertEquals("AAPL", instrument.getSymbol());
        assertEquals("Apple Inc. Common Stock", instrument.getName());
        assertEquals("Apple", instrument.getCustomName());
        assertEquals(6L, instrument.getMarketId());
    }
}