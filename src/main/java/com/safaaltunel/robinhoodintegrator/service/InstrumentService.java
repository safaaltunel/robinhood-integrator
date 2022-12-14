package com.safaaltunel.robinhoodintegrator.service;

import com.safaaltunel.robinhoodintegrator.entity.Instrument;
import com.safaaltunel.robinhoodintegrator.exception.InstrumentNotFoundException;
import com.safaaltunel.robinhoodintegrator.exception.InstrumentsNotSyncedException;
import com.safaaltunel.robinhoodintegrator.repository.InstrumentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;


@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final AsyncService asyncService;

    private boolean isAltered;


    public InstrumentService(InstrumentRepository instrumentRepository, AsyncService asyncService) {
        this.instrumentRepository = instrumentRepository;
        this.asyncService = asyncService;
        this.isAltered = instrumentRepository.columnCount() != 2;
    }


    @CacheEvict(value = "symbols", allEntries = true)
    public List<Instrument> syncInstruments() {
        if(!isAltered) {
            isAltered = true;
            instrumentRepository.addColumns();
        }
        List<Instrument> instrumentsInRepo = (List<Instrument>) instrumentRepository.findAll();
        List<Instrument> instrumentsResponse = IntStream.range(0, instrumentsInRepo.size())
                .parallel()
                .mapToObj(i -> {
                    Instrument inst = instrumentsInRepo.get(i);
                    return asyncService.getInstrumentAsync(inst);
                })
                .map(CompletableFuture::join)
                .toList();

        return (List<Instrument>) instrumentRepository.saveAll(instrumentsResponse);
    }


    public List<Instrument> getAllInstruments() {
        if(!isAltered) {
            throw new InstrumentsNotSyncedException();
        }
        return (List<Instrument>) instrumentRepository.findAll();
    }

    @Cacheable(value = "symbols", key = "#symbol")
    public Instrument getInstrumentBySymbol(String symbol) {
        if(!isAltered) {
            throw new InstrumentsNotSyncedException();
        }

        Instrument instrument = instrumentRepository.findBySymbol(symbol);
        if(instrument == null) {
            throw new InstrumentNotFoundException(symbol);
        }
        return instrument;
    }
}
