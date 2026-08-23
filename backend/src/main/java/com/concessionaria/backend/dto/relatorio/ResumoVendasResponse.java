package com.concessionaria.backend.dto.relatorio;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumoVendasResponse(
        Long numeroNota,
        LocalDate dataDaVenda,
        Integer idCliente,
        String nomeCliente,
        Integer matriculaVendedor,
        String nomeVendedor,
        Long quantidadeVeiculos,
        BigDecimal valorTotalVenda
) {
}