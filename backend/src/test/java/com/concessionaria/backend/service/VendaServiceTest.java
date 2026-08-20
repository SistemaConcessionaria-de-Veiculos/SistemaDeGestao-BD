package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.VendaAtualizacaoRequest;
import com.concessionaria.backend.dto.VendaCadastroRequest;
import com.concessionaria.backend.dto.VendaResponse;
import com.concessionaria.backend.model.Venda;
import com.concessionaria.backend.exception.VeiculoJaVinculadoVendaException;
import com.concessionaria.backend.model.StatusVeiculo;
import com.concessionaria.backend.model.Veiculo;
import com.concessionaria.backend.repository.ClienteRepository;
import com.concessionaria.backend.repository.VendaRepository;
import com.concessionaria.backend.repository.VendedorRepository;
import com.concessionaria.backend.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VendedorRepository vendedorRepository;
    @Mock
    private VeiculoRepository veiculoRepository;
    private VendaService vendaService;

    @BeforeEach
    void configurar() {
        vendaService = new VendaService(
            vendaRepository,
            clienteRepository,
            vendedorRepository,
            veiculoRepository
    );
}

    @Test
    void deveCadastrarVendaValida() {
        VendaCadastroRequest request = new VendaCadastroRequest(
                1,
                1,
                new BigDecimal("118500.00"),
                LocalDate.of(2026, 8, 20)
        );

        when(clienteRepository.existsById(1)).thenReturn(true);
        when(vendedorRepository.existsById(1)).thenReturn(true);

        when(vendaRepository.save(any(Venda.class)))
                .thenAnswer(invocacao -> {
                    Venda venda = invocacao.getArgument(0);
                    venda.setNumeroNota(51L);
                    return venda;
                });

        VendaResponse resposta = vendaService.cadastrar(request);

        assertThat(resposta.numeroNota()).isEqualTo(51L);
        assertThat(resposta.idCliente()).isEqualTo(1);
        assertThat(resposta.matriculaVendedor()).isEqualTo(1);
        assertThat(resposta.valorTotalVenda())
                .isEqualByComparingTo("118500.00");

        verify(vendaRepository).save(any(Venda.class));
    }

    @Test
    void deveRejeitarClienteInexistente() {
        VendaCadastroRequest request = new VendaCadastroRequest(
                999999,
                1,
                new BigDecimal("90000.00"),
                LocalDate.of(2026, 8, 20)
        );

        when(clienteRepository.existsById(999999)).thenReturn(false);

        assertThatThrownBy(() -> vendaService.cadastrar(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Cliente não encontrado: 999999");
    }

    @Test
    void deveRejeitarVendedorInexistente() {
        VendaCadastroRequest request = new VendaCadastroRequest(
                1,
                999999,
                new BigDecimal("90000.00"),
                LocalDate.of(2026, 8, 20)
        );

        when(clienteRepository.existsById(1)).thenReturn(true);
        when(vendedorRepository.existsById(999999)).thenReturn(false);

        assertThatThrownBy(() -> vendaService.cadastrar(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vendedor não encontrado: 999999");
    }

    @Test
    void deveListarVendas() {
        Venda venda = criarVenda(10L);

        when(vendaRepository.findAll())
                .thenReturn(List.of(venda));

        List<VendaResponse> resposta = vendaService.listar();

        assertThat(resposta).hasSize(1);
        assertThat(resposta.get(0).numeroNota()).isEqualTo(10L);
    }

    @Test
    void deveBuscarVendaPorNumeroNota() {
        Venda venda = criarVenda(20L);

        when(vendaRepository.findById(20L))
                .thenReturn(Optional.of(venda));

        VendaResponse resposta =
                vendaService.buscarPorNumeroNota(20L);

        assertThat(resposta.numeroNota()).isEqualTo(20L);
    }

    @Test
    void deveAtualizarVenda() {
        Venda venda = criarVenda(30L);

        when(vendaRepository.findById(30L))
                .thenReturn(Optional.of(venda));

        when(clienteRepository.existsById(2))
                .thenReturn(true);

        when(vendedorRepository.existsById(2))
                .thenReturn(true);

        when(vendaRepository.save(any(Venda.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        VendaAtualizacaoRequest request =
                new VendaAtualizacaoRequest(
                        2,
                        2,
                        new BigDecimal("130000.00"),
                        LocalDate.of(2026, 8, 21)
                );

        VendaResponse resposta =
                vendaService.atualizar(30L, request);

        assertThat(resposta.numeroNota()).isEqualTo(30L);
        assertThat(resposta.idCliente()).isEqualTo(2);
        assertThat(resposta.matriculaVendedor()).isEqualTo(2);
        assertThat(resposta.valorTotalVenda())
                .isEqualByComparingTo("130000.00");
    }

    @Test
    void deveExcluirVenda() {
        Venda venda = criarVenda(40L);

        when(vendaRepository.findById(40L))
                .thenReturn(Optional.of(venda));

        vendaService.excluir(40L);

        verify(vendaRepository).delete(venda);
    }

    @Test
    void deveCadastrarVendaEVincularVeiculo() {
    String chassi = "9BWZZZ377VT004251";

    VendaCadastroRequest request = new VendaCadastroRequest(
            1,
            1,
            new BigDecimal("145000.00"),
            LocalDate.of(2026, 8, 20),
            List.of(chassi)
    );

    Veiculo veiculo = new Veiculo(
            chassi,
            null,
            "Toyota",
            "Corolla",
            "Prata",
            LocalDate.of(2025, 1, 10),
            StatusVeiculo.DISPONIVEL,
            new BigDecimal("145000.00")
    );

    when(clienteRepository.existsById(1))
            .thenReturn(true);

    when(vendedorRepository.existsById(1))
            .thenReturn(true);

    when(vendaRepository.save(any(Venda.class)))
            .thenAnswer(invocacao -> {
                Venda venda = invocacao.getArgument(0);
                venda.setNumeroNota(60L);
                return venda;
            });

    when(veiculoRepository.findById(chassi))
            .thenReturn(Optional.of(veiculo));

    when(veiculoRepository.findByNumeroNota(60L))
            .thenReturn(List.of(veiculo));

    VendaResponse resposta = vendaService.cadastrar(request);

    assertThat(veiculo.getNumeroNota())
            .isEqualTo(60L);

    assertThat(resposta.chassis())
            .containsExactly(chassi);
}
    @Test
    void deveRejeitarVeiculoVinculadoAOutraVenda() {
    String chassi = "9BG116GW04C400001";

    VendaCadastroRequest request = new VendaCadastroRequest(
            1,
            1,
            new BigDecimal("95000.00"),
            LocalDate.of(2026, 8, 20),
            List.of(chassi)
    );

    Veiculo veiculo = new Veiculo(
            chassi,
            99L,
            "Honda",
            "Civic",
            "Preto",
            LocalDate.of(2023, 5, 15),
            StatusVeiculo.DISPONIVEL,
            new BigDecimal("95000.00")
    );

    when(clienteRepository.existsById(1))
            .thenReturn(true);

    when(vendedorRepository.existsById(1))
            .thenReturn(true);

    when(vendaRepository.save(any(Venda.class)))
            .thenAnswer(invocacao -> {
                Venda venda = invocacao.getArgument(0);
                venda.setNumeroNota(61L);
                return venda;
            });

    when(veiculoRepository.findById(chassi))
            .thenReturn(Optional.of(veiculo));

    assertThatThrownBy(() -> vendaService.cadastrar(request))
            .isInstanceOf(VeiculoJaVinculadoVendaException.class)
            .hasMessage(
                    "Veículo já vinculado a outra venda: " + chassi
            );
}
    @Test
    void deveAtualizarVinculosDosVeiculosDaVenda() {
    String chassiAntigo = "9BWZZZ377VT004251";
    String chassiNovo = "9BG116GW04C400001";

    Venda venda = criarVenda(30L);

    Veiculo veiculoAntigo = new Veiculo(
            chassiAntigo,
            30L,
            "Toyota",
            "Corolla",
            "Prata",
            LocalDate.of(2024, 1, 10),
            StatusVeiculo.DISPONIVEL,
            new BigDecimal("120000.00")
    );

    Veiculo veiculoNovo = new Veiculo(
            chassiNovo,
            null,
            "Honda",
            "Civic",
            "Preto",
            LocalDate.of(2024, 2, 15),
            StatusVeiculo.DISPONIVEL,
            new BigDecimal("130000.00")
    );

    when(vendaRepository.findById(30L))
            .thenReturn(Optional.of(venda));

    when(clienteRepository.existsById(2))
            .thenReturn(true);

    when(vendedorRepository.existsById(2))
            .thenReturn(true);

    when(vendaRepository.save(any(Venda.class)))
            .thenAnswer(invocacao -> invocacao.getArgument(0));

    when(veiculoRepository.findByNumeroNota(30L))
            .thenReturn(
                    List.of(veiculoAntigo),
                    List.of(veiculoNovo)
            );

    when(veiculoRepository.findById(chassiNovo))
            .thenReturn(Optional.of(veiculoNovo));

    VendaAtualizacaoRequest request =
            new VendaAtualizacaoRequest(
                    2,
                    2,
                    new BigDecimal("130000.00"),
                    LocalDate.of(2026, 8, 21),
                    List.of(chassiNovo)
            );

    VendaResponse resposta =
            vendaService.atualizar(30L, request);

    assertThat(veiculoAntigo.getNumeroNota())
            .isNull();

    assertThat(veiculoNovo.getNumeroNota())
            .isEqualTo(30L);

    assertThat(resposta.chassis())
            .containsExactly(chassiNovo);
}

    private Venda criarVenda(Long numeroNota) {
        Venda venda = new Venda(
                1,
                1,
                new BigDecimal("100000.00"),
                LocalDate.of(2026, 8, 20)
        );

        venda.setNumeroNota(numeroNota);

        return venda;
    }
}