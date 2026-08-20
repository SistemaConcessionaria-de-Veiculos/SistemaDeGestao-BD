package com.concessionaria.backend.controller;

import com.concessionaria.backend.dto.VendedorAtualizacaoRequest;
import com.concessionaria.backend.dto.VendedorCadastroRequest;
import com.concessionaria.backend.dto.VendedorResponse;
import com.concessionaria.backend.service.VendedorService;
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
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final VendedorService vendedorService;

    public VendedorController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @PostMapping
    public ResponseEntity<VendedorResponse> cadastrar(
            @Valid @RequestBody VendedorCadastroRequest request) {

        VendedorResponse vendedor = vendedorService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vendedor);
    }

    @GetMapping
    public ResponseEntity<List<VendedorResponse>> listar() {
        return ResponseEntity.ok(vendedorService.listar());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<VendedorResponse> buscarPorMatricula(
            @PathVariable Integer matricula) {

        return ResponseEntity.ok(
                vendedorService.buscarPorMatricula(matricula)
        );
    }

    @PutMapping("/{matricula}")
    public ResponseEntity<VendedorResponse> atualizar(
            @PathVariable Integer matricula,
            @Valid @RequestBody VendedorAtualizacaoRequest request) {

        return ResponseEntity.ok(
                vendedorService.atualizar(matricula, request)
        );
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> excluir(
            @PathVariable Integer matricula) {

        vendedorService.excluir(matricula);

        return ResponseEntity.noContent().build();
    }
}