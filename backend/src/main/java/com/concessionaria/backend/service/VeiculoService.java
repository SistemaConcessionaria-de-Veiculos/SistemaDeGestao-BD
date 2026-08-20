package com.concessionaria.backend.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.concessionaria.backend.dto.VeiculoAtualizacaoRequest;
import com.concessionaria.backend.dto.VeiculoCadastroRequest;
import com.concessionaria.backend.dto.VeiculoListagemResponse;
import com.concessionaria.backend.dto.VeiculoResponse;
import com.concessionaria.backend.exception.ChassiVeiculoJaCadastradoException;
import com.concessionaria.backend.exception.PlacaVeiculoJaCadastradaException;
import com.concessionaria.backend.exception.VeiculoNaoEncontradoException;
import com.concessionaria.backend.model.Veiculo;
import com.concessionaria.backend.model.VeiculoNovo;
import com.concessionaria.backend.model.VeiculoUsado;
import com.concessionaria.backend.repository.VeiculoNovoRepository;
import com.concessionaria.backend.repository.VeiculoRepository;
import com.concessionaria.backend.repository.VeiculoUsadoRepository;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final VeiculoNovoRepository veiculoNovoRepository;
    private final VeiculoUsadoRepository veiculoUsadoRepository;

    public VeiculoService(
            VeiculoRepository veiculoRepository,
            VeiculoNovoRepository veiculoNovoRepository,
            VeiculoUsadoRepository veiculoUsadoRepository) {

        this.veiculoRepository = veiculoRepository;
        this.veiculoNovoRepository = veiculoNovoRepository;
        this.veiculoUsadoRepository = veiculoUsadoRepository;
    }

    @Transactional
    public VeiculoResponse cadastrar(VeiculoCadastroRequest request) {
        String chassi = request.chassi().trim().toUpperCase(Locale.ROOT);
        String tipo = normalizarTipo(request.tipo());

        if (veiculoRepository.existsById(chassi)) {
            throw new ChassiVeiculoJaCadastradoException(
                    "Chassi já cadastrado: " + chassi
            );
        }

        validarEspecializacao(
                tipo,
                request.placa(),
                request.quilometragem(),
                null
        );

        Veiculo veiculo = new Veiculo(
                chassi,
                request.numeroNota(),
                request.marca(),
                request.modelo(),
                request.cor(),
                request.dataFabricacao(),
                request.statusDisponibilidade(),
                request.valorVeiculo()
        );

        veiculo = veiculoRepository.save(veiculo);

        if ("NOVO".equals(tipo)) {
            veiculoNovoRepository.save(new VeiculoNovo(veiculo));
        } else {
            veiculoUsadoRepository.save(
                    new VeiculoUsado(
                            veiculo,
                            normalizarPlaca(request.placa()),
                            request.quilometragem()
                    )
            );
        }

        return montarResponse(veiculo);
    }

    @Transactional(readOnly = true)
    public List<VeiculoListagemResponse> listarVeiculos() {
        return veiculoRepository.findAll()
                .stream()
                .map(this::montarListagemResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VeiculoResponse buscarPorChassi(String chassi) {
        return montarResponse(buscarVeiculo(chassi));
    }

    @Transactional
    public VeiculoResponse atualizar(
            String chassi,
            VeiculoAtualizacaoRequest request) {

        Veiculo veiculo = buscarVeiculo(chassi);
        String tipo = normalizarTipo(request.tipo());

        validarEspecializacao(
                tipo,
                request.placa(),
                request.quilometragem(),
                veiculo.getChassi()
        );

        veiculo.setNumeroNota(request.numeroNota());
        veiculo.setMarca(request.marca());
        veiculo.setModelo(request.modelo());
        veiculo.setCor(request.cor());
        veiculo.setDataFabricacao(request.dataFabricacao());
        veiculo.setStatusDisponibilidade(request.statusDisponibilidade());
        veiculo.setValorVeiculo(request.valorVeiculo());

        boolean eraNovo =
                veiculoNovoRepository.existsById(veiculo.getChassi());

        boolean eraUsado =
                veiculoUsadoRepository.existsById(veiculo.getChassi());

        if ("NOVO".equals(tipo)) {
            if (eraUsado) {
                veiculoUsadoRepository.deleteById(veiculo.getChassi());
            }

            if (!eraNovo) {
                veiculoNovoRepository.save(new VeiculoNovo(veiculo));
            }
        } else {
            if (eraNovo) {
                veiculoNovoRepository.deleteById(veiculo.getChassi());
            }

            VeiculoUsado usado =
                    veiculoUsadoRepository
                            .findById(veiculo.getChassi())
                            .orElseGet(() -> new VeiculoUsado(
                                    veiculo,
                                    normalizarPlaca(request.placa()),
                                    request.quilometragem()
                            ));

            usado.setPlaca(normalizarPlaca(request.placa()));
            usado.setQuilometragem(request.quilometragem());

            veiculoUsadoRepository.save(usado);
        }

        veiculoRepository.save(veiculo);

        return montarResponse(veiculo);
    }

    @Transactional
    public void excluir(String chassi) {
        Veiculo veiculo = buscarVeiculo(chassi);

        if (veiculoNovoRepository.existsById(veiculo.getChassi())) {
            veiculoNovoRepository.deleteById(veiculo.getChassi());
        }

        if (veiculoUsadoRepository.existsById(veiculo.getChassi())) {
            veiculoUsadoRepository.deleteById(veiculo.getChassi());
        }

        veiculoRepository.delete(veiculo);
    }

    private Veiculo buscarVeiculo(String chassi) {
        String chassiNormalizado =
                chassi.trim().toUpperCase(Locale.ROOT);

        return veiculoRepository.findById(chassiNormalizado)
                .orElseThrow(() ->
                        new VeiculoNaoEncontradoException(
                                "Veículo não encontrado: "
                                        + chassiNormalizado
                        )
                );
    }

    private void validarEspecializacao(
            String tipo,
            String placa,
            Integer quilometragem,
            String chassiAtual) {

        if ("NOVO".equals(tipo)) {
            if (placa != null && !placa.isBlank()) {
                throw new IllegalArgumentException(
                        "Placa não deve ser informada para veículo novo"
                );
            }

            if (quilometragem != null) {
                throw new IllegalArgumentException(
                        "Quilometragem não deve ser informada para veículo novo"
                );
            }

            return;
        }

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "Placa é obrigatória para veículo usado"
            );
        }

        if (quilometragem == null) {
            throw new IllegalArgumentException(
                    "Quilometragem é obrigatória para veículo usado"
            );
        }

        String placaNormalizada = normalizarPlaca(placa);

        veiculoUsadoRepository.findByPlaca(placaNormalizada)
                .filter(usado ->
                        chassiAtual == null
                                || !usado.getChassi().equals(chassiAtual))
                .ifPresent(usado -> {
                    throw new PlacaVeiculoJaCadastradaException(
                            "Placa já cadastrada: " + placaNormalizada
                    );
                });
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo do veículo é obrigatório"
            );
        }

        String tipoNormalizado =
                tipo.trim().toUpperCase(Locale.ROOT);

        if (!"NOVO".equals(tipoNormalizado)
                && !"USADO".equals(tipoNormalizado)) {
            throw new IllegalArgumentException(
                    "O tipo deve ser NOVO ou USADO"
            );
        }

        return tipoNormalizado;
    }

    private String normalizarPlaca(String placa) {
        return placa == null
                ? null
                : placa.trim().toUpperCase(Locale.ROOT);
    }

    private VeiculoResponse montarResponse(Veiculo veiculo) {
        VeiculoUsado usado =
                veiculoUsadoRepository
                        .findById(veiculo.getChassi())
                        .orElse(null);

        if (usado != null) {
            return new VeiculoResponse(
                    veiculo.getChassi(),
                    veiculo.getNumeroNota(),
                    veiculo.getMarca(),
                    veiculo.getModelo(),
                    veiculo.getCor(),
                    veiculo.getDataFabricacao(),
                    veiculo.getStatusDisponibilidade(),
                    veiculo.getValorVeiculo(),
                    "USADO",
                    usado.getPlaca(),
                    usado.getQuilometragem()
            );
        }

        if (veiculoNovoRepository.existsById(veiculo.getChassi())) {
            return new VeiculoResponse(
                    veiculo.getChassi(),
                    veiculo.getNumeroNota(),
                    veiculo.getMarca(),
                    veiculo.getModelo(),
                    veiculo.getCor(),
                    veiculo.getDataFabricacao(),
                    veiculo.getStatusDisponibilidade(),
                    veiculo.getValorVeiculo(),
                    "NOVO",
                    null,
                    null
            );
        }

        throw new IllegalStateException(
                "Veículo sem especialização NOVO ou USADO: "
                        + veiculo.getChassi()
        );
    }

    private VeiculoListagemResponse montarListagemResponse(
            Veiculo veiculo) {

        VeiculoResponse response = montarResponse(veiculo);

        return new VeiculoListagemResponse(
                response.chassi(),
                response.marca(),
                response.modelo(),
                response.cor(),
                response.dataFabricacao(),
                response.valorVeiculo(),
                response.statusDisponibilidade(),
                response.tipo(),
                response.placa(),
                response.quilometragem()
        );
    }
}