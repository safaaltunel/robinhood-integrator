package com.safaaltunel.robinhoodintegrator.repository;

import com.safaaltunel.robinhoodintegrator.entity.Market;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MarketRepository extends CrudRepository<Market,Long> {

    Optional<Market> findByCode(String code);
}
