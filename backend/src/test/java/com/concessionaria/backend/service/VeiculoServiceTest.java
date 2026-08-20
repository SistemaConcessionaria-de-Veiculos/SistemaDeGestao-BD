package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.VeiculoCadastroRequest;
import com.concessionaria.backend.dto.VeiculoListagemResponse;
import com.concessionaria.backend.dto.VeiculoResponse;
import com.concessionaria.backend.model.StatusVeiculo;
import com.concessionaria.backend.model.Veiculo;
import com.concessionaria.backend.repository.VeiculoNovoRepository;
import com.concessionaria.backend.repository.VeiculoRepository;
import com.concessionaria.backend.repository.VeiculoUsadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private VeiculoNovoRepository veiculoNovoRepository;

    @Mock
    private VeiculoUsadoRepository veiculoUsadoRepository;

    private VeiculoService veiculoService;

    @BeforeEach
    void configurar() {
        veiculoService = new VeiculoService(
                veiculoRepository,
                veiculoNovoRepository,
                veiculoUsadoRepository
        );
    }

    @Test
    void deveCadastrarVeiculoNovoValido() {
        String chassi = "9BWZZZ377VT004251";

        VeiculoCadastroRequest request = new VeiculoCadastroRequest(
                chassi,
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

        when(veiculoRepository.existsById(chassi))
                .thenReturn(false);

        when(veiculoRepository.save(any(Veiculo.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        when(veiculoUsadoRepository.findById(chassi))
                .thenReturn(Optional.empty());

        when(veiculoNovoRepository.existsById(chassi))
                .thenReturn(true);

        VeiculoResponse resposta = veiculoService.cadastrar(request);

        ArgumentCaptor<Veiculo> captor =
                ArgumentCaptor.forClass(Veiculo.class);

        verify(veiculoRepository).save(captor.capture());

        Veiculo veiculoSalvo = captor.getValue();

        assertThat(veiculoSalvo.getChassi())
                .isEqualTo(chassi);

        assertThat(veiculoSalvo.getMarca())
                .isEqualTo("Toyota");

        assertThat(veiculoSalvo.getModelo())
                .isEqualTo("Corolla");

        assertThat(veiculoSalvo.getStatusDisponibilidade())
                .isEqualTo(StatusVeiculo.DISPONIVEL);

        assertThat(resposta.chassi())
                .isEqualTo(chassi);

        assertThat(resposta.valorVeiculo())
                .isEqualByComparingTo("150000.00");

        assertThat(resposta.tipo())
                .isEqualTo("NOVO");
    }

    @Test
    void deveListarVeiculosCadastrados() {
        String chassi = "9BG116GW04C400001";

        Veiculo veiculo = criarVeiculo(
                chassi,
                "Honda",
                "Civic"
        );

        when(veiculoRepository.findAll())
                .thenReturn(List.of(veiculo));

        when(veiculoUsadoRepository.findById(chassi))
                .thenReturn(Optional.empty());

        when(veiculoNovoRepository.existsById(chassi))
                .thenReturn(true);

        List<VeiculoListagemResponse> resposta =
                veiculoService.listarVeiculos();

        assertThat(resposta).hasSize(1);

        assertThat(resposta.get(0).chassi())
                .isEqualTo(chassi);

        assertThat(resposta.get(0).marca())
                .isEqualTo("Honda");

        assertThat(resposta.get(0).modelo())
                .isEqualTo("Civic");

        assertThat(resposta.get(0).tipo())
                .isEqualTo("NOVO");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremVeiculos() {
        when(veiculoRepository.findAll())
                .thenReturn(List.of());

        List<VeiculoListagemResponse> resposta =
                veiculoService.listarVeiculos();

        assertThat(resposta).isEmpty();
    }

    private Veiculo criarVeiculo(
            String chassi,
            String marca,
            String modelo) {

        return new Veiculo(
                chassi,
                null,
                marca,
                modelo,
                "Prata",
                LocalDate.of(2024, 1, 10),
                StatusVeiculo.DISPONIVEL,
                new BigDecimal("120000.00")
        );
    }
}