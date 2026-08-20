package com.concessionaria.backend.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusVeiculoConverter
        implements AttributeConverter<StatusVeiculo, String> {

    @Override
    public String convertToDatabaseColumn(StatusVeiculo status) {
        return status == null ? null : status.getValorBanco();
    }

    @Override
    public StatusVeiculo convertToEntityAttribute(String valorBanco) {
        return StatusVeiculo.fromValor(valorBanco);
    }
}