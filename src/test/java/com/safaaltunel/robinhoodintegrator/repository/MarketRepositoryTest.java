package com.safaaltunel.robinhoodintegrator.repository;

import com.safaaltunel.robinhoodintegrator.entity.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MarketRepositoryTest {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Market market = Market.builder()
                .code("TEST")
                .symbol("TEST")
                .name("IEX Market")
                .country("US - United States of America")
                .website("www.iextrading.com")
                .build();

        entityManager.persist(market);
    }

    @Test
    void whenFindByCode_thenReturnMarket() {

        Optional<Market> market = marketRepository.findByCode("TEST");

        assertEquals("TEST", market.get().getSymbol());

    }
}