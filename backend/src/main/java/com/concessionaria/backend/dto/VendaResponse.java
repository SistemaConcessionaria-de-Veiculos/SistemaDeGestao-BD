package com.concessionaria.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VendaResponse(
        Long numeroNota,
        Integer idCliente,
        Integer matriculaVendedor,
        BigDecimal valorTotalVenda,
        LocalDate dataDaVenda,
        List<String> chassis
) {

    public VendaResponse(
            Long numeroNota,
            Integer idCliente,
            Integer matriculaVendedor,
            BigDecimal valorTotalVenda,
            LocalDate dataDaVenda
    ) {
        this(
                numeroNota,
                idCliente,
                matriculaVendedor,
                valorTotalVenda,
                dataDaVenda,
                List.of()
        );
    }
}