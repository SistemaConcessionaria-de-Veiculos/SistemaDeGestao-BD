package com.concessionaria.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.concessionaria.backend.dto.VendaAtualizacaoRequest;
import com.concessionaria.backend.dto.VendaCadastroRequest;
import com.concessionaria.backend.dto.VendaResponse;
import com.concessionaria.backend.model.Venda;
import com.concessionaria.backend.repository.ClienteRepository;
import com.concessionaria.backend.repository.VendaRepository;
import com.concessionaria.backend.repository.VendedorRepository;
import com.concessionaria.backend.exception.VeiculoJaVinculadoVendaException;
import com.concessionaria.backend.model.Veiculo;
import com.concessionaria.backend.repository.VeiculoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final VeiculoRepository veiculoRepository;

    public VendaService(
        VendaRepository vendaRepository,
        ClienteRepository clienteRepository,
        VendedorRepository vendedorRepository,
        VeiculoRepository veiculoRepository) {
    this.vendaRepository = vendaRepository;
    this.clienteRepository = clienteRepository;
    this.vendedorRepository = vendedorRepository;
    this.veiculoRepository = veiculoRepository;
}

    @Transactional
    public VendaResponse cadastrar(VendaCadastroRequest request) {
        validarCliente(request.idCliente());
        validarVendedor(request.matriculaVendedor());

        Venda venda = new Venda(
                request.idCliente(),
                request.matriculaVendedor(),
                request.valorTotalVenda(),
                request.dataDaVenda()
        );

        Venda salva = vendaRepository.save(venda);

        vincularVeiculos(
            salva.getNumeroNota(),
            request.chassis()
    );

return montarResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<VendaResponse> listar() {
        return vendaRepository.findAll()
                .stream()
                .map(this::montarResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VendaResponse buscarPorNumeroNota(Long numeroNota) {
        return montarResponse(buscarVenda(numeroNota));
    }

    @Transactional
    public VendaResponse atualizar(
            Long numeroNota,
            VendaAtualizacaoRequest request) {

        Venda venda = buscarVenda(numeroNota);

        validarCliente(request.idCliente());
        validarVendedor(request.matriculaVendedor());

        venda.setIdCliente(request.idCliente());
        venda.setMatriculaVendedor(request.matriculaVendedor());
        venda.setValorTotalVenda(request.valorTotalVenda());
        venda.setDataDaVenda(request.dataDaVenda());

        Venda salva = vendaRepository.save(venda);

        atualizarVinculosVeiculos(
            salva.getNumeroNota(),
            request.chassis()
        );

        return montarResponse(salva);
    }

    @Transactional
    public void excluir(Long numeroNota) {
        Venda venda = buscarVenda(numeroNota);
        vendaRepository.delete(venda);
    }

    private Venda buscarVenda(Long numeroNota) {
        return vendaRepository.findById(numeroNota)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Venda não encontrada: " + numeroNota
                        )
                );
    }

    private void validarCliente(Integer idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new EntityNotFoundException(
                    "Cliente não encontrado: " + idCliente
            );
        }
    }

    private void validarVendedor(Integer matriculaVendedor) {
        if (!vendedorRepository.existsById(matriculaVendedor)) {
            throw new EntityNotFoundException(
                    "Vendedor não encontrado: " + matriculaVendedor
            );
        }
    }

    private void vincularVeiculos(
        Long numeroNota,
        List<String> chassis) {

    if (chassis == null || chassis.isEmpty()) {
        return;
    }

    for (String chassi : chassis) {

        Veiculo veiculo = veiculoRepository.findById(chassi)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Veículo não encontrado: " + chassi
                        )
                );

        if (veiculo.getNumeroNota() != null
                && !veiculo.getNumeroNota().equals(numeroNota)) {

            throw new VeiculoJaVinculadoVendaException(
                    "Veículo já vinculado a outra venda: " + chassi
            );
        }

        veiculo.setNumeroNota(numeroNota);
    }
}

    private void atualizarVinculosVeiculos(
        Long numeroNota,
        List<String> chassis) {


    if (chassis == null) {
        return;
    }

    List<Veiculo> atualmenteVinculados =
            veiculoRepository.findByNumeroNota(numeroNota);

    for (Veiculo veiculo : atualmenteVinculados) {
        veiculo.setNumeroNota(null);
    }

    vincularVeiculos(numeroNota, chassis);
}

    private VendaResponse montarResponse(Venda venda) {

    List<String> chassis = veiculoRepository
            .findByNumeroNota(venda.getNumeroNota())
            .stream()
            .map(Veiculo::getChassi)
            .toList();

    return new VendaResponse(
            venda.getNumeroNota(),
            venda.getIdCliente(),
            venda.getMatriculaVendedor(),
            venda.getValorTotalVenda(),
            venda.getDataDaVenda(),
            chassis
    );
}
}