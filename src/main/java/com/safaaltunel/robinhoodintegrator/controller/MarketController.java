package com.safaaltunel.robinhoodintegrator.controller;

import com.safaaltunel.robinhoodintegrator.entity.Market;
import com.safaaltunel.robinhoodintegrator.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MarketController {

    private final MarketService marketService;


    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping("/sync/markets")
    public ResponseEntity<List<Market>> syncMarkets() {

        List<Market> response = marketService.syncMarkets();

        return ResponseEntity
                .ok(response);
    }
}
