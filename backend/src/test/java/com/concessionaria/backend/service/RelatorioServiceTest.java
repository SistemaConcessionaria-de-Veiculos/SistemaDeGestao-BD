package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.relatorio.ClientesComprasResponse;
import com.concessionaria.backend.dto.relatorio.ResumoVendasResponse;
import com.concessionaria.backend.dto.relatorio.VeiculosCustomizacoesResponse;
import com.concessionaria.backend.repository.RelatorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private RelatorioRepository relatorioRepository;

    private RelatorioService relatorioService;

    @BeforeEach
    void configurar() {
        relatorioService = new RelatorioService(relatorioRepository);
    }

    @Test
    void deveListarResumoVendas() {
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

        when(relatorioRepository.buscarResumoVendas())
                .thenReturn(List.of(resumo));

        List<ResumoVendasResponse> resposta =
                relatorioService.listarResumoVendas();

        assertThat(resposta).containsExactly(resumo);
        verify(relatorioRepository).buscarResumoVendas();
    }

    @Test
    void deveListarClientesCompras() {
        ClientesComprasResponse cliente = new ClientesComprasResponse(
                12,
                "Mariana Albuquerque",
                3L,
                4L,
                new BigDecimal("438700.00")
        );

        when(relatorioRepository.buscarClientesCompras())
                .thenReturn(List.of(cliente));

        List<ClientesComprasResponse> resposta =
                relatorioService.listarClientesCompras();

        assertThat(resposta).containsExactly(cliente);
        verify(relatorioRepository).buscarClientesCompras();
    }

    @Test
    void deveListarVeiculosCustomizacoes() {
        VeiculosCustomizacoesResponse veiculo =
                new VeiculosCustomizacoesResponse(
                        "9BD358A1NNY123456",
                        "Fiat",
                        "Pulse",
                        "disponivel",
                        "NOVO",
                        null,
                        null,
                        new BigDecimal("128900.00"),
                        2L,
                        "Central Multimidia, Bancos em Couro",
                        new BigDecimal("8000.00"),
                        new BigDecimal("136900.00")
                );

        when(relatorioRepository.buscarVeiculosCustomizacoes())
                .thenReturn(List.of(veiculo));

        List<VeiculosCustomizacoesResponse> resposta =
                relatorioService.listarVeiculosCustomizacoes();

        assertThat(resposta).containsExactly(veiculo);
        verify(relatorioRepository).buscarVeiculosCustomizacoes();
    }

    @Test
    void deveRetornarListasVaziasQuandoNaoExistiremDados() {
        when(relatorioRepository.buscarResumoVendas())
                .thenReturn(List.of());
        when(relatorioRepository.buscarClientesCompras())
                .thenReturn(List.of());
        when(relatorioRepository.buscarVeiculosCustomizacoes())
                .thenReturn(List.of());

        assertThat(relatorioService.listarResumoVendas()).isEmpty();
        assertThat(relatorioService.listarClientesCompras()).isEmpty();
        assertThat(relatorioService.listarVeiculosCustomizacoes()).isEmpty();

        verify(relatorioRepository).buscarResumoVendas();
        verify(relatorioRepository).buscarClientesCompras();
        verify(relatorioRepository).buscarVeiculosCustomizacoes();
    }
}
