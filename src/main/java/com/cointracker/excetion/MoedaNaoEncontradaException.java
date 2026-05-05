package com.cointracker.excetion;

public class MoedaNaoEncontradaException extends RuntimeException {
    public MoedaNaoEncontradaException(String message) {
        super(message);
    }
}
