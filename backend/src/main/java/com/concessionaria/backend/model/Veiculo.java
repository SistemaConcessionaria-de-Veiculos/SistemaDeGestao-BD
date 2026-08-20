package com.concessionaria.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @Column(name = "chassi", nullable = false, length = 17)
    private String chassi;

    @Column(name = "numero_nota")
    private Long numeroNota;

    @Column(name = "marca", nullable = false, length = 50)
    private String marca;

    @Column(name = "modelo", nullable = false, length = 80)
    private String modelo;

    @Column(name = "cor", nullable = false, length = 30)
    private String cor;

    @Column(name = "data_fabricacao", nullable = false)
    private LocalDate dataFabricacao;

    @Convert(converter = StatusVeiculoConverter.class)
    @Column(name = "status_disponibilidade", nullable = false, length = 20)
    private StatusVeiculo statusDisponibilidade;

    @Column(name = "valor_veiculo", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorVeiculo;

    public Veiculo() {
    }

    public Veiculo(
            String chassi,
            Long numeroNota,
            String marca,
            String modelo,
            String cor,
            LocalDate dataFabricacao,
            StatusVeiculo statusDisponibilidade,
            BigDecimal valorVeiculo) {

        this.chassi = chassi;
        this.numeroNota = numeroNota;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.dataFabricacao = dataFabricacao;
        this.statusDisponibilidade = statusDisponibilidade;
        this.valorVeiculo = valorVeiculo;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public Long getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Long numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public LocalDate getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDate dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public StatusVeiculo getStatusDisponibilidade() {
        return statusDisponibilidade;
    }

    public void setStatusDisponibilidade(
            StatusVeiculo statusDisponibilidade) {
        this.statusDisponibilidade = statusDisponibilidade;
    }

    public BigDecimal getValorVeiculo() {
        return valorVeiculo;
    }

    public void setValorVeiculo(BigDecimal valorVeiculo) {
        this.valorVeiculo = valorVeiculo;
    }
}