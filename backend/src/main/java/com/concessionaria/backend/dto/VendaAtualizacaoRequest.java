package com.concessionaria.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VendaAtualizacaoRequest(

        @NotNull(message = "O cliente é obrigatório")
        @Positive(message = "O id do cliente deve ser maior que zero")
        Integer idCliente,

        @NotNull(message = "O vendedor é obrigatório")
        @Positive(message = "A matrícula do vendedor deve ser maior que zero")
        Integer matriculaVendedor,

        @NotNull(message = "O valor total da venda é obrigatório")
        @Positive(message = "O valor total da venda deve ser maior que zero")
        BigDecimal valorTotalVenda,

        @NotNull(message = "A data da venda é obrigatória")
        LocalDate dataDaVenda,

        List<String> chassis
) {
        public VendaAtualizacaoRequest(
        Integer idCliente,
        Integer matriculaVendedor,
        BigDecimal valorTotalVenda,
        LocalDate dataDaVenda
) {
    this(
            idCliente,
            matriculaVendedor,
            valorTotalVenda,
            dataDaVenda,
            List.of()
    );
}
}