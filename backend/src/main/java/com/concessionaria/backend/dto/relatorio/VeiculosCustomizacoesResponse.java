package com.concessionaria.backend.dto.relatorio;

import java.math.BigDecimal;

public record VeiculosCustomizacoesResponse(
        String chassi,
        String marca,
        String modelo,
        String statusDisponibilidade,
        String tipoVeiculo,
        String placa,
        Integer quilometragem,
        BigDecimal valorVeiculo,
        Long quantidadeCustomizacoes,
        String customizacoes,
        BigDecimal custoCustomizacoes,
        BigDecimal valorTotalComCustomizacoes
) {
}