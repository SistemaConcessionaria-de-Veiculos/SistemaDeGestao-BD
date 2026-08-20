package com.concessionaria.backend.dto;

public record ClienteResponse(
        Integer idCliente,
        String nome,
        String email,
        String telefone,
        String rua,
        String numero,
        String cep,
        String tipo,
        String cpf,
        String cnpj
) {
}
