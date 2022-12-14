package com.safaaltunel.robinhoodintegrator.controller.advice;

import com.safaaltunel.robinhoodintegrator.exception.InstrumentNotFoundException;
import com.safaaltunel.robinhoodintegrator.exception.InstrumentsNotSyncedException;
import com.safaaltunel.robinhoodintegrator.exception.MarketsNotFoundException;
import com.safaaltunel.robinhoodintegrator.model.ErrorDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MarketsNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionMarketsNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Markets information is not available");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(InstrumentNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionInstrumentNotFoundHandler(InstrumentNotFoundException e) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Instrument with symbol: '" + e.getMessage() + "' is not found");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(InstrumentsNotSyncedException.class)
    public ResponseEntity<ErrorDetails> exceptionInstrumentsNotSyncedHandler() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Instruments are not synced!");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}

