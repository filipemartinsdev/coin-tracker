package com.cointracker.exception;

public class MoedaNaoEncontradaException extends RuntimeException {
    public MoedaNaoEncontradaException(String message) {
        super(message);
    }
}
