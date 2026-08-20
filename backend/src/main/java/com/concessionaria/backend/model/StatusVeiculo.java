package com.concessionaria.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusVeiculo {

    DISPONIVEL("disponivel"),
    RESERVADO("reservado"),
    VENDIDO("vendido");

    private final String valorBanco;

    StatusVeiculo(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    @JsonValue
    public String getValorBanco() {
        return valorBanco;
    }

    @JsonCreator
    public static StatusVeiculo fromValor(String valor) {
        if (valor == null) {
            return null;
        }

        for (StatusVeiculo status : values()) {
            if (status.valorBanco.equalsIgnoreCase(valor)
                    || status.name().equalsIgnoreCase(valor)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Status de veículo inválido: " + valor
        );
    }
}