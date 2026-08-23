package com.concessionaria.backend.controller;

import com.concessionaria.backend.dto.relatorio.ClientesComprasResponse;
import com.concessionaria.backend.dto.relatorio.ResumoVendasResponse;
import com.concessionaria.backend.dto.relatorio.VeiculosCustomizacoesResponse;
import com.concessionaria.backend.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/resumo-vendas")
    public ResponseEntity<List<ResumoVendasResponse>>
    listarResumoVendas() {

        return ResponseEntity.ok(
                relatorioService.listarResumoVendas()
        );
    }

    @GetMapping("/clientes-compras")
    public ResponseEntity<List<ClientesComprasResponse>>
    listarClientesCompras() {

        return ResponseEntity.ok(
                relatorioService.listarClientesCompras()
        );
    }

    @GetMapping("/veiculos-customizacoes")
    public ResponseEntity<List<VeiculosCustomizacoesResponse>>
    listarVeiculosCustomizacoes() {

        return ResponseEntity.ok(
                relatorioService.listarVeiculosCustomizacoes()
        );
    }
}