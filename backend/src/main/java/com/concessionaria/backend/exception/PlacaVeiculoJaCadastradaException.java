package com.concessionaria.backend.exception;

public class PlacaVeiculoJaCadastradaException extends RuntimeException {

    public PlacaVeiculoJaCadastradaException(String mensagem) {
        super(mensagem);
    }
}