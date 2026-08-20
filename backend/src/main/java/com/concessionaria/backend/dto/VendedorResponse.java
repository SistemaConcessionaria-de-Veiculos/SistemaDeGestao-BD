package com.concessionaria.backend.dto;

public record VendedorResponse(
        Integer matricula,
        String nome,
        String cpf
) {
}