package com.safaaltunel.robinhoodintegrator.repository;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrument, Long> {
    @Modifying
    @Transactional
    @Query(
            value = "alter table instrument add column name varchar, add column custom_name varchar, add column market_id bigserial",
            nativeQuery = true
    )
    void addColumns();

    @Query(
            value = "SELECT count(*) FROM information_schema.columns WHERE table_name = 'instrument'",
            nativeQuery = true
    )
    int columnCount();


    Instrument findBySymbol(String symbol);
}
