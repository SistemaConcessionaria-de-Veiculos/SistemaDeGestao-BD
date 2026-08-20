package com.concessionaria.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteCadastroRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail deve ser válido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone,

        @NotBlank(message = "A rua é obrigatória")
        @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres")
        String rua,

        @NotBlank(message = "O número é obrigatório")
        @Size(max = 10, message = "O número deve ter no máximo 10 caracteres")
        String numero,

        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos")
        String cep,

        @NotBlank(message = "O tipo de cliente é obrigatório")
        @Pattern(
            regexp = "FISICA|JURIDICA",
            message = "O tipo deve ser FISICA ou JURIDICA"
        )
        String tipo,

        @Pattern(
            regexp = "^$|\\d{11}",
            message = "O CPF deve conter 11 dígitos"
        )
        String cpf,

        @Pattern(
            regexp = "^$|\\d{14}",
            message = "O CNPJ deve conter 14 dígitos"
        )
        String cnpj
) {
}
