package com.concessionaria.backend.exception;

public class DocumentoClienteJaCadastradoException extends RuntimeException {

    public DocumentoClienteJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}