package com.concessionaria.backend.controller;

import com.concessionaria.backend.dto.VendaResponse;
import com.concessionaria.backend.exception.GlobalExceptionHandler;
import com.concessionaria.backend.service.VendaService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VendaControllerTest {

    @Mock
    private VendaService vendaService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        VendaController controller =
                new VendaController(vendaService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveCadastrarVendaERetornar201() throws Exception {
        VendaResponse resposta = new VendaResponse(
                51L,
                1,
                1,
                new BigDecimal("118500.00"),
                LocalDate.of(2026, 8, 20)
        );

        when(vendaService.cadastrar(any()))
                .thenReturn(resposta);

        mockMvc.perform(post("/api/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idCliente": 1,
                                  "matriculaVendedor": 1,
                                  "valorTotalVenda": 118500.00,
                                  "dataDaVenda": "2026-08-20"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroNota").value(51))
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.matriculaVendedor").value(1));
    }

    @Test
    void deveRetornar400QuandoCadastroForInvalido()
            throws Exception {

        mockMvc.perform(post("/api/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idCliente": 0,
                                  "matriculaVendedor": 0,
                                  "valorTotalVenda": 0,
                                  "dataDaVenda": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.idCliente").exists())
                .andExpect(jsonPath("$.matriculaVendedor").exists())
                .andExpect(jsonPath("$.valorTotalVenda").exists())
                .andExpect(jsonPath("$.dataDaVenda").exists());

        verify(vendaService, never()).cadastrar(any());
    }

    @Test
    void deveListarVendasERetornar200() throws Exception {
        VendaResponse venda = new VendaResponse(
                10L,
                1,
                1,
                new BigDecimal("100000.00"),
                LocalDate.of(2026, 8, 20)
        );

        when(vendaService.listar())
                .thenReturn(List.of(venda));

        mockMvc.perform(get("/api/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroNota").value(10))
                .andExpect(jsonPath("$[0].idCliente").value(1));
    }

    @Test
    void deveBuscarVendaPorNumeroNota() throws Exception {
        VendaResponse venda = new VendaResponse(
                20L,
                1,
                1,
                new BigDecimal("100000.00"),
                LocalDate.of(2026, 8, 20)
        );

        when(vendaService.buscarPorNumeroNota(20L))
                .thenReturn(venda);

        mockMvc.perform(get("/api/vendas/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroNota").value(20));
    }

    @Test
    void deveAtualizarVendaERetornar200() throws Exception {
        VendaResponse venda = new VendaResponse(
                30L,
                2,
                2,
                new BigDecimal("130000.00"),
                LocalDate.of(2026, 8, 21)
        );

        when(vendaService.atualizar(eq(30L), any()))
                .thenReturn(venda);

        mockMvc.perform(put("/api/vendas/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idCliente": 2,
                                  "matriculaVendedor": 2,
                                  "valorTotalVenda": 130000.00,
                                  "dataDaVenda": "2026-08-21"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroNota").value(30))
                .andExpect(jsonPath("$.idCliente").value(2));
    }

    @Test
    void deveExcluirVendaERetornar204() throws Exception {
        mockMvc.perform(delete("/api/vendas/40"))
                .andExpect(status().isNoContent());

        verify(vendaService).excluir(40L);
    }

    @Test
    void deveRetornar200EListaVazia() throws Exception {
        when(vendaService.listar())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/vendas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}