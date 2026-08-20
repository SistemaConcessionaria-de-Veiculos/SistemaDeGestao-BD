package com.concessionaria.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VendaResponse(
        Long numeroNota,
        Integer idCliente,
        Integer matriculaVendedor,
        BigDecimal valorTotalVenda,
        LocalDate dataDaVenda
) {
}