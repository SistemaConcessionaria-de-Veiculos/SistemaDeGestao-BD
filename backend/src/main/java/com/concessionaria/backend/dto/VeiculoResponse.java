package com.concessionaria.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.concessionaria.backend.model.StatusVeiculo;

public record VeiculoResponse(
        String chassi,
        Long numeroNota,
        String marca,
        String modelo,
        String cor,
        LocalDate dataFabricacao,
        StatusVeiculo statusDisponibilidade,
        BigDecimal valorVeiculo,
        String tipo,
        String placa,
        Integer quilometragem
) {
}