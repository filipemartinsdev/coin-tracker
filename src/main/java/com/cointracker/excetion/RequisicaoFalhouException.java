package com.cointracker.excetion;

public class RequisicaoFalhouException extends RuntimeException {
    public RequisicaoFalhouException(String message) {
        super(message);
    }

    public RequisicaoFalhouException(String message, Throwable cause) {
        super(message, cause);
    }
}