package com.concessionaria.backend.dto.relatorio;

import java.math.BigDecimal;

public record ClientesComprasResponse(
        Integer idCliente,
        String nomeCliente,
        Long quantidadeCompras,
        Long quantidadeVeiculos,
        BigDecimal valorTotalCompras
) {
}