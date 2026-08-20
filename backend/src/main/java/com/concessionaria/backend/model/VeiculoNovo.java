package com.concessionaria.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "veiculos_novos")
public class VeiculoNovo {

    @Id
    @Column(name = "chassi", length = 17)
    private String chassi;

    @OneToOne
    @MapsId
    @JoinColumn(name = "chassi")
    private Veiculo veiculo;

    public VeiculoNovo() {
    }

    public VeiculoNovo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public String getChassi() {
        return chassi;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
}