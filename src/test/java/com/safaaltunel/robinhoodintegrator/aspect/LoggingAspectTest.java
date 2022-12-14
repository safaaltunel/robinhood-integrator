package com.safaaltunel.robinhoodintegrator.aspect;

import com.safaaltunel.robinhoodintegrator.proxy.InstrumentProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
class LoggingAspectTest {

    private Logger loggingAspectLogger;

    @Autowired
    private LoggingAspect loggingAspect;

    @Autowired
    private InstrumentProxy instrumentProxy;

    @BeforeEach
    public void before() {
        this.loggingAspectLogger = mock(Logger.class);
        loggingAspect.setLogger(loggingAspectLogger);
    }

    @Test
    @DisplayName("Test that logging aspect intercepts the execution" +
            " of the InstrumentProxy.getInstrument() method.")
    void testAspectInterceptsGetInstrumentMethod() {
        instrumentProxy.getInstrument("TWTR");

        verify(loggingAspectLogger).warning("TWTR instrument record is not available at RobinHood API");
    }
}