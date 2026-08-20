package com.concessionaria.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.concessionaria.backend.model.StatusVeiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record VeiculoAtualizacaoRequest(

        Long numeroNota,

        @NotBlank(message = "A marca é obrigatória")
        @Size(max = 50, message = "A marca deve ter no máximo 50 caracteres")
        String marca,

        @NotBlank(message = "O modelo é obrigatório")
        @Size(max = 80, message = "O modelo deve ter no máximo 80 caracteres")
        String modelo,

        @NotBlank(message = "A cor é obrigatória")
        @Size(max = 30, message = "A cor deve ter no máximo 30 caracteres")
        String cor,

        @NotNull(message = "A data de fabricação é obrigatória")
        LocalDate dataFabricacao,

        @NotNull(message = "O status de disponibilidade é obrigatório")
        StatusVeiculo statusDisponibilidade,

        @NotNull(message = "O valor do veículo é obrigatório")
        @Positive(message = "O valor do veículo deve ser maior que zero")
        BigDecimal valorVeiculo,

        @NotBlank(message = "O tipo do veículo é obrigatório")
        @Pattern(
            regexp = "NOVO|USADO",
            message = "O tipo deve ser NOVO ou USADO"
        )
        String tipo,

        @Size(min = 7, max = 7, message = "A placa deve conter 7 caracteres")
        String placa,

        @PositiveOrZero(message = "A quilometragem não pode ser negativa")
        Integer quilometragem
) {
}