package com.concessionaria.backend.controller;

import com.concessionaria.backend.dto.ClienteAtualizacaoRequest;
import com.concessionaria.backend.dto.ClienteCadastroRequest;
import com.concessionaria.backend.dto.ClienteResponse;
import com.concessionaria.backend.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(
            @Valid @RequestBody ClienteCadastroRequest request) {

        ClienteResponse cliente = clienteService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponse> buscarPorId(
            @PathVariable Integer idCliente) {

        return ResponseEntity.ok(
                clienteService.buscarPorId(idCliente)
        );
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteResponse> atualizar(
            @PathVariable Integer idCliente,
            @Valid @RequestBody ClienteAtualizacaoRequest request) {

        return ResponseEntity.ok(
                clienteService.atualizar(idCliente, request)
        );
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer idCliente) {

        clienteService.excluir(idCliente);

        return ResponseEntity.noContent().build();
    }
}