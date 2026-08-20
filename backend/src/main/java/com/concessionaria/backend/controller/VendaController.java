package com.concessionaria.backend.controller;

import java.util.List;

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

import com.concessionaria.backend.dto.VendaAtualizacaoRequest;
import com.concessionaria.backend.dto.VendaCadastroRequest;
import com.concessionaria.backend.dto.VendaResponse;
import com.concessionaria.backend.service.VendaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponse> cadastrar(
            @Valid @RequestBody VendaCadastroRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vendaService.cadastrar(request));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponse>> listar() {
        return ResponseEntity.ok(vendaService.listar());
    }

    @GetMapping("/{numeroNota}")
    public ResponseEntity<VendaResponse> buscarPorNumeroNota(
            @PathVariable Long numeroNota) {

        return ResponseEntity.ok(
                vendaService.buscarPorNumeroNota(numeroNota)
        );
    }

    @PutMapping("/{numeroNota}")
    public ResponseEntity<VendaResponse> atualizar(
            @PathVariable Long numeroNota,
            @Valid @RequestBody VendaAtualizacaoRequest request) {

        return ResponseEntity.ok(
                vendaService.atualizar(numeroNota, request)
        );
    }

    @DeleteMapping("/{numeroNota}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long numeroNota) {

        vendaService.excluir(numeroNota);

        return ResponseEntity.noContent().build();
    }
}