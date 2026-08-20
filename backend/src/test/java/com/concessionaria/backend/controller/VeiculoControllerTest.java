package com.concessionaria.backend.controller;

import com.concessionaria.backend.dto.VeiculoListagemResponse;
import com.concessionaria.backend.dto.VeiculoResponse;
import com.concessionaria.backend.exception.GlobalExceptionHandler;
import com.concessionaria.backend.model.StatusVeiculo;
import com.concessionaria.backend.service.VeiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VeiculoControllerTest {

    @Mock
    private VeiculoService veiculoService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        VeiculoController controller =
                new VeiculoController(veiculoService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveCadastrarVeiculoERetornar201() throws Exception {
        VeiculoResponse resposta = new VeiculoResponse(
                "9BWZZZ377VT004251",
                null,
                "Toyota",
                "Corolla",
                "Prata",
                LocalDate.of(2024, 1, 10),
                StatusVeiculo.DISPONIVEL,
                new BigDecimal("150000.00"),
                "NOVO",
                null,
                null
        );

        when(veiculoService.cadastrar(any()))
                .thenReturn(resposta);

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "chassi": "9BWZZZ377VT004251",
                                  "numeroNota": null,
                                  "marca": "Toyota",
                                  "modelo": "Corolla",
                                  "cor": "Prata",
                                  "dataFabricacao": "2024-01-10",
                                  "statusDisponibilidade": "disponivel",
                                  "valorVeiculo": 150000.00,
                                  "tipo": "NOVO",
                                  "placa": null,
                                  "quilometragem": null
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.chassi")
                                .value("9BWZZZ377VT004251")
                )
                .andExpect(
                        jsonPath("$.marca")
                                .value("Toyota")
                )
                .andExpect(
                        jsonPath("$.statusDisponibilidade")
                                .value("disponivel")
                )
                .andExpect(
                        jsonPath("$.tipo")
                                .value("NOVO")
                );
    }

    @Test
    void deveRetornar400QuandoCadastroForInvalido()
            throws Exception {

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "chassi": "",
                                  "marca": "",
                                  "modelo": "",
                                  "cor": "",
                                  "dataFabricacao": null,
                                  "statusDisponibilidade": null,
                                  "valorVeiculo": 0,
                                  "tipo": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.chassi")
                                .exists()
                )
                .andExpect(
                        jsonPath("$.marca")
                                .value("A marca é obrigatória")
                )
                .andExpect(
                        jsonPath("$.modelo")
                                .value("O modelo é obrigatório")
                )
                .andExpect(
                        jsonPath("$.cor")
                                .value("A cor é obrigatória")
                )
                .andExpect(
                        jsonPath("$.dataFabricacao")
                                .value("A data de fabricação é obrigatória")
                )
                .andExpect(
                        jsonPath("$.statusDisponibilidade")
                                .value("O status de disponibilidade é obrigatório")
                )
                .andExpect(
                        jsonPath("$.valorVeiculo")
                                .value("O valor do veículo deve ser maior que zero")
                )
                .andExpect(
                        jsonPath("$.tipo")
                                .exists()
                );

        verify(veiculoService, never())
                .cadastrar(any());
    }

    @Test
    void deveListarVeiculosERetornar200() throws Exception {
        VeiculoListagemResponse veiculo =
                new VeiculoListagemResponse(
                        "9BG116GW04C400001",
                        "Honda",
                        "Civic",
                        "Prata",
                        LocalDate.of(2024, 1, 10),
                        new BigDecimal("120000.00"),
                        StatusVeiculo.DISPONIVEL,
                        "NOVO",
                        null,
                        null
                );

        when(veiculoService.listarVeiculos())
                .thenReturn(List.of(veiculo));

        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].chassi")
                                .value("9BG116GW04C400001")
                )
                .andExpect(
                        jsonPath("$[0].marca")
                                .value("Honda")
                )
                .andExpect(
                        jsonPath("$[0].modelo")
                                .value("Civic")
                )
                .andExpect(
                        jsonPath("$[0].tipo")
                                .value("NOVO")
                );
    }

    @Test
    void deveRetornar200EListaVazia() throws Exception {
        when(veiculoService.listarVeiculos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/veiculos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}