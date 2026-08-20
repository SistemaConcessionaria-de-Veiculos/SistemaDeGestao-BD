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

import jakarta.persistence.EntityNotFoundException;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            VendedorRepository vendedorRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.vendedorRepository = vendedorRepository;
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

        return montarResponse(vendaRepository.save(venda));
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

    private VendaResponse montarResponse(Venda venda) {
        return new VendaResponse(
                venda.getNumeroNota(),
                venda.getIdCliente(),
                venda.getMatriculaVendedor(),
                venda.getValorTotalVenda(),
                venda.getDataDaVenda()
        );
    }
}