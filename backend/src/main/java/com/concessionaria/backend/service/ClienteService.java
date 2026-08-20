package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.ClienteAtualizacaoRequest;
import com.concessionaria.backend.dto.ClienteCadastroRequest;
import com.concessionaria.backend.dto.ClienteResponse;
import com.concessionaria.backend.model.Cliente;
import com.concessionaria.backend.model.PessoaFisica;
import com.concessionaria.backend.model.PessoaJuridica;
import com.concessionaria.backend.repository.ClienteRepository;
import com.concessionaria.backend.repository.PessoaFisicaRepository;
import com.concessionaria.backend.repository.PessoaJuridicaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.concessionaria.backend.exception.DocumentoClienteJaCadastradoException;

import java.util.List;
import java.util.Locale;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final PessoaJuridicaRepository pessoaJuridicaRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            PessoaFisicaRepository pessoaFisicaRepository,
            PessoaJuridicaRepository pessoaJuridicaRepository) {
        this.clienteRepository = clienteRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
    }

    @Transactional
    public ClienteResponse cadastrar(ClienteCadastroRequest request) {
        String tipo = normalizarTipo(request.tipo());

        validarDocumentos(tipo, request.cpf(), request.cnpj());
        validarDocumentoDuplicado(tipo, request.cpf(), request.cnpj(), null);

        Cliente cliente = new Cliente(
                request.nome(),
                request.email(),
                request.telefone(),
                request.rua(),
                request.numero(),
                request.cep()
        );

        cliente = clienteRepository.save(cliente);

        if ("FISICA".equals(tipo)) {
            pessoaFisicaRepository.save(
                    new PessoaFisica(cliente, request.cpf())
            );
        } else {
            pessoaJuridicaRepository.save(
                    new PessoaJuridica(cliente, request.cnpj())
            );
        }

        return montarResponse(cliente, tipo, request.cpf(), request.cnpj());
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(this::montarResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Integer idCliente) {
        Cliente cliente = buscarCliente(idCliente);
        return montarResponse(cliente);
    }

    @Transactional
    public ClienteResponse atualizar(
            Integer idCliente,
            ClienteAtualizacaoRequest request) {

        Cliente cliente = buscarCliente(idCliente);
        String tipo = normalizarTipo(request.tipo());

        validarDocumentos(tipo, request.cpf(), request.cnpj());
        validarDocumentoDuplicado(
                tipo,
                request.cpf(),
                request.cnpj(),
                idCliente
        );

        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        cliente.setRua(request.rua());
        cliente.setNumero(request.numero());
        cliente.setCep(request.cep());

        PessoaFisica pessoaFisica =
                pessoaFisicaRepository.findById(idCliente).orElse(null);

        PessoaJuridica pessoaJuridica =
                pessoaJuridicaRepository.findById(idCliente).orElse(null);

        if ("FISICA".equals(tipo)) {
            if (pessoaJuridica != null) {
                pessoaJuridicaRepository.delete(pessoaJuridica);
            }

            if (pessoaFisica == null) {
                pessoaFisicaRepository.save(
                        new PessoaFisica(cliente, request.cpf())
                );
            } else {
                pessoaFisica.setCpf(request.cpf());
            }
        } else {
            if (pessoaFisica != null) {
                pessoaFisicaRepository.delete(pessoaFisica);
            }

            if (pessoaJuridica == null) {
                pessoaJuridicaRepository.save(
                        new PessoaJuridica(cliente, request.cnpj())
                );
            } else {
                pessoaJuridica.setCnpj(request.cnpj());
            }
        }

        clienteRepository.save(cliente);

        return montarResponse(
                cliente,
                tipo,
                request.cpf(),
                request.cnpj()
        );
    }

    @Transactional
    public void excluir(Integer idCliente) {
        Cliente cliente = buscarCliente(idCliente);

        pessoaFisicaRepository.findById(idCliente)
                .ifPresent(pessoaFisicaRepository::delete);

        pessoaJuridicaRepository.findById(idCliente)
                .ifPresent(pessoaJuridicaRepository::delete);

        clienteRepository.delete(cliente);
    }

    private Cliente buscarCliente(Integer idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente não encontrado: " + idCliente
                        )
                );
    }

    private ClienteResponse montarResponse(Cliente cliente) {
        PessoaFisica pessoaFisica =
                pessoaFisicaRepository
                        .findById(cliente.getIdCliente())
                        .orElse(null);

        if (pessoaFisica != null) {
            return montarResponse(
                    cliente,
                    "FISICA",
                    pessoaFisica.getCpf(),
                    null
            );
        }

        PessoaJuridica pessoaJuridica =
                pessoaJuridicaRepository
                        .findById(cliente.getIdCliente())
                        .orElse(null);

        if (pessoaJuridica != null) {
            return montarResponse(
                    cliente,
                    "JURIDICA",
                    null,
                    pessoaJuridica.getCnpj()
            );
        }

        throw new IllegalStateException(
                "Cliente sem especialização PF ou PJ: "
                        + cliente.getIdCliente()
        );
    }

    private ClienteResponse montarResponse(
            Cliente cliente,
            String tipo,
            String cpf,
            String cnpj) {

        return new ClienteResponse(
                cliente.getIdCliente(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getRua(),
                cliente.getNumero(),
                cliente.getCep(),
                tipo,
                cpf,
                cnpj
        );
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo de cliente é obrigatório"
            );
        }

        String tipoNormalizado =
                tipo.trim().toUpperCase(Locale.ROOT);

        if (!"FISICA".equals(tipoNormalizado)
                && !"JURIDICA".equals(tipoNormalizado)) {
            throw new IllegalArgumentException(
                    "O tipo deve ser FISICA ou JURIDICA"
            );
        }

        return tipoNormalizado;
    }

    private void validarDocumentos(
            String tipo,
            String cpf,
            String cnpj) {

        if ("FISICA".equals(tipo)) {
            if (cpf == null || cpf.isBlank()) {
                throw new IllegalArgumentException(
                        "CPF é obrigatório para pessoa física"
                );
            }

            if (cnpj != null && !cnpj.isBlank()) {
                throw new IllegalArgumentException(
                        "CNPJ não deve ser informado para pessoa física"
                );
            }
        } else {
            if (cnpj == null || cnpj.isBlank()) {
                throw new IllegalArgumentException(
                        "CNPJ é obrigatório para pessoa jurídica"
                );
            }

            if (cpf != null && !cpf.isBlank()) {
                throw new IllegalArgumentException(
                        "CPF não deve ser informado para pessoa jurídica"
                );
            }
        }
    }

    private void validarDocumentoDuplicado(
            String tipo,
            String cpf,
            String cnpj,
            Integer idClienteAtual) {

        if ("FISICA".equals(tipo)) {
            pessoaFisicaRepository.findByCpf(cpf)
                    .filter(pessoa ->
                            !pessoa.getIdCliente()
                                    .equals(idClienteAtual))
                    .ifPresent(pessoa -> {
                        throw new DocumentoClienteJaCadastradoException(
                        "CPF já cadastrado"
);
                    });
        } else {
            pessoaJuridicaRepository.findByCnpj(cnpj)
                    .filter(pessoa ->
                            !pessoa.getIdCliente()
                                    .equals(idClienteAtual))
                    .ifPresent(pessoa -> {
                        throw new DocumentoClienteJaCadastradoException(
                        "CNPJ já cadastrado"
);
                    });
        }
    }
}