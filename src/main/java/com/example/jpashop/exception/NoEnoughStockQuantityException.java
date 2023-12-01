package com.example.jpashop.exception;

public class NoEnoughStockQuantityException extends RuntimeException {
    public NoEnoughStockQuantityException(String s) {

    }

    public NoEnoughStockQuantityException() {
        super();
    }

    public NoEnoughStockQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEnoughStockQuantityException(Throwable cause) {
        super(cause);
    }

    protected NoEnoughStockQuantityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
