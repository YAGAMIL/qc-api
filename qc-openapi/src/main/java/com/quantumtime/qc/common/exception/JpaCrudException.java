package com.quantumtime.qc.common.exception;


public class JpaCrudException extends RuntimeException {

    public JpaCrudException(String message) {
        super(message);
    }

    public JpaCrudException(String message, Throwable e) {
        super(message, e);
    }
}
