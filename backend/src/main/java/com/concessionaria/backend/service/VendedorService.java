package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.VendedorAtualizacaoRequest;
import com.concessionaria.backend.dto.VendedorCadastroRequest;
import com.concessionaria.backend.dto.VendedorResponse;
import com.concessionaria.backend.model.Vendedor;
import com.concessionaria.backend.repository.VendedorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.concessionaria.backend.exception.CpfVendedorJaCadastradoException;

import java.util.List;

@Service
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @Transactional
    public VendedorResponse cadastrar(VendedorCadastroRequest request) {
        validarCpfDuplicado(request.cpf(), null);

        Vendedor vendedor = new Vendedor(
                request.nome(),
                request.cpf()
        );

        vendedor = vendedorRepository.save(vendedor);

        return montarResponse(vendedor);
    }

    @Transactional(readOnly = true)
    public List<VendedorResponse> listar() {
        return vendedorRepository.findAll()
                .stream()
                .map(this::montarResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VendedorResponse buscarPorMatricula(Integer matricula) {
        return montarResponse(buscarVendedor(matricula));
    }

    @Transactional
    public VendedorResponse atualizar(
            Integer matricula,
            VendedorAtualizacaoRequest request) {

        Vendedor vendedor = buscarVendedor(matricula);

        validarCpfDuplicado(request.cpf(), matricula);

        vendedor.setNome(request.nome());
        vendedor.setCpf(request.cpf());

        vendedor = vendedorRepository.save(vendedor);

        return montarResponse(vendedor);
    }

    @Transactional
    public void excluir(Integer matricula) {
        Vendedor vendedor = buscarVendedor(matricula);
        vendedorRepository.delete(vendedor);
    }

    private Vendedor buscarVendedor(Integer matricula) {
        return vendedorRepository.findById(matricula)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vendedor não encontrado: " + matricula
                        )
                );
    }

    private void validarCpfDuplicado(
            String cpf,
            Integer matriculaAtual) {

        vendedorRepository.findByCpf(cpf)
                .filter(vendedor ->
                        !vendedor.getMatricula()
                                .equals(matriculaAtual))
                .ifPresent(vendedor -> {
                    throw new CpfVendedorJaCadastradoException(
        "CPF de vendedor já cadastrado"
);
                });
    }

    private VendedorResponse montarResponse(Vendedor vendedor) {
        return new VendedorResponse(
                vendedor.getMatricula(),
                vendedor.getNome(),
                vendedor.getCpf()
        );
    }
}