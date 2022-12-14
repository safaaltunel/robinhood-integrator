package com.safaaltunel.robinhoodintegrator.aspect;

import com.safaaltunel.robinhoodintegrator.model.response.Instruments;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.logging.Logger;

@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around(value = "@annotation(ToLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Instruments returnedValue = (Instruments) joinPoint.proceed();
        if(returnedValue.getResults().size() == 0){
            String symbol = (String) joinPoint.getArgs()[0];
            logger.warning(symbol + " instrument record is not available at RobinHood API");
        }

        return returnedValue;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
