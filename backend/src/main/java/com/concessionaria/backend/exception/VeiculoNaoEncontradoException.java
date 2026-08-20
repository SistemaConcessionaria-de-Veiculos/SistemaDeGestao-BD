package com.concessionaria.backend.exception;

public class VeiculoNaoEncontradoException extends RuntimeException {

    public VeiculoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}