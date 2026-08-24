package com.concessionaria.backend.controller;

import com.concessionaria.backend.dto.relatorio.ClientesComprasResponse;
import com.concessionaria.backend.dto.relatorio.ResumoVendasResponse;
import com.concessionaria.backend.dto.relatorio.VeiculosCustomizacoesResponse;
import com.concessionaria.backend.service.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RelatorioControllerTest {

    @Mock
    private RelatorioService relatorioService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        RelatorioController controller =
                new RelatorioController(relatorioService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void deveListarResumoVendasERetornar200() throws Exception {
        ResumoVendasResponse resumo = new ResumoVendasResponse(
                81L,
                LocalDate.of(2026, 8, 18),
                12,
                "Mariana Albuquerque",
                7,
                "Rafael Montenegro",
                2L,
                new BigDecimal("245900.00")
        );

        when(relatorioService.listarResumoVendas())
                .thenReturn(List.of(resumo));

        mockMvc.perform(get("/api/relatorios/resumo-vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroNota").value(81))
                .andExpect(jsonPath("$[0].dataDaVenda").exists())
                .andExpect(jsonPath("$[0].nomeCliente").value("Mariana Albuquerque"))
                .andExpect(jsonPath("$[0].nomeVendedor").value("Rafael Montenegro"))
                .andExpect(jsonPath("$[0].quantidadeVeiculos").value(2))
                .andExpect(jsonPath("$[0].valorTotalVenda").value(245900.00));

        verify(relatorioService).listarResumoVendas();
    }

    @Test
    void deveListarClientesComprasERetornar200() throws Exception {
        ClientesComprasResponse cliente = new ClientesComprasResponse(
                12,
                "Mariana Albuquerque",
                3L,
                4L,
                new BigDecimal("438700.00")
        );

        when(relatorioService.listarClientesCompras())
                .thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/relatorios/clientes-compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCliente").value(12))
                .andExpect(jsonPath("$[0].nomeCliente").value("Mariana Albuquerque"))
                .andExpect(jsonPath("$[0].quantidadeCompras").value(3))
                .andExpect(jsonPath("$[0].quantidadeVeiculos").value(4))
                .andExpect(jsonPath("$[0].valorTotalCompras").value(438700.00));

        verify(relatorioService).listarClientesCompras();
    }

    @Test
    void deveListarVeiculosCustomizacoesERetornar200() throws Exception {
        VeiculosCustomizacoesResponse veiculo =
                new VeiculosCustomizacoesResponse(
                        "9BWZZZ377VT004251",
                        "Volkswagen",
                        "Nivus",
                        "reservado",
                        "USADO",
                        "BRA2E26",
                        28500,
                        new BigDecimal("119900.00"),
                        1L,
                        "Central Multimidia",
                        new BigDecimal("3200.00"),
                        new BigDecimal("123100.00")
                );

        when(relatorioService.listarVeiculosCustomizacoes())
                .thenReturn(List.of(veiculo));

        mockMvc.perform(get("/api/relatorios/veiculos-customizacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].chassi").value("9BWZZZ377VT004251"))
                .andExpect(jsonPath("$[0].marca").value("Volkswagen"))
                .andExpect(jsonPath("$[0].modelo").value("Nivus"))
                .andExpect(jsonPath("$[0].tipoVeiculo").value("USADO"))
                .andExpect(jsonPath("$[0].placa").value("BRA2E26"))
                .andExpect(jsonPath("$[0].quilometragem").value(28500))
                .andExpect(jsonPath("$[0].valorTotalComCustomizacoes").value(123100.00));

        verify(relatorioService).listarVeiculosCustomizacoes();
    }
}
