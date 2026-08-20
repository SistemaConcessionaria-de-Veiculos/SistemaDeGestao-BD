package com.concessionaria.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.concessionaria.backend.model.StatusVeiculo;

public record VeiculoListagemResponse(
        String chassi,
        String marca,
        String modelo,
        String cor,
        LocalDate dataFabricacao,
        BigDecimal valorVeiculo,
        StatusVeiculo statusDisponibilidade,
        String tipo,
        String placa,
        Integer quilometragem
) {
}