package com.concessionaria.backend.exception;

public class CpfVendedorJaCadastradoException extends RuntimeException {

    public CpfVendedorJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}