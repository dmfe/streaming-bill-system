package com.nc.sbs.eventgenerator.utils;

import com.nc.sbs.eventgenerator.exceptions.ExecutionRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class ExceptionHandlers {
    private ExceptionHandlers() {}

    public static void handleInterruptedException(InterruptedException ex) {
        log.error("Thread " + Thread.currentThread().getName() + " was interrupted: " +
                ex.getLocalizedMessage(), ex);
        Thread.currentThread().interrupt();
    }

    public static void hadleExecutionException(ExecutionException ex) {
        log.error("Execution error occurred: " + ex.getLocalizedMessage(), ex);
        throw new ExecutionRuntimeException("Execution error occurred: " + ex.getLocalizedMessage(), ex);
    }
}
