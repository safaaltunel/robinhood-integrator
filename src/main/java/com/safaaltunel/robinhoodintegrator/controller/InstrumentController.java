package com.safaaltunel.robinhoodintegrator.controller;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import com.safaaltunel.robinhoodintegrator.service.InstrumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @PostMapping("/sync/instruments")
    public ResponseEntity<List<Instrument>> syncInstruments() {
        List<Instrument> response = instrumentService.syncInstruments();
        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/instruments")
    public ResponseEntity<List<Instrument>> getAllInstruments() {
        return ResponseEntity
                .ok(instrumentService.getAllInstruments());
    }

    @GetMapping(value = "/instruments", params = "symbol")
    public ResponseEntity<Instrument> getInstrumentBySymbol(@RequestParam String symbol) {
        return ResponseEntity
                .ok(instrumentService.getInstrumentBySymbol(symbol));
    }
}
