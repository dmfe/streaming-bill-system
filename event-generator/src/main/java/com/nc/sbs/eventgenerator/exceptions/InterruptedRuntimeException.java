package com.nc.sbs.eventgenerator.exceptions;

public class InterruptedRuntimeException extends RuntimeException {

    public InterruptedRuntimeException(String message) {
        super(message);
    }

    public InterruptedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
