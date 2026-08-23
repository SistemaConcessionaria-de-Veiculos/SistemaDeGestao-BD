package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.relatorio.ClientesComprasResponse;
import com.concessionaria.backend.dto.relatorio.ResumoVendasResponse;
import com.concessionaria.backend.dto.relatorio.VeiculosCustomizacoesResponse;
import com.concessionaria.backend.repository.RelatorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;

    public RelatorioService(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    @Transactional(readOnly = true)
    public List<ResumoVendasResponse> listarResumoVendas() {
        return relatorioRepository.buscarResumoVendas();
    }

    @Transactional(readOnly = true)
    public List<ClientesComprasResponse> listarClientesCompras() {
        return relatorioRepository.buscarClientesCompras();
    }

    @Transactional(readOnly = true)
    public List<VeiculosCustomizacoesResponse>
    listarVeiculosCustomizacoes() {

        return relatorioRepository.buscarVeiculosCustomizacoes();
    }
}