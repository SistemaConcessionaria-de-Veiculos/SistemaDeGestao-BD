package com.concessionaria.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "veiculos_usados")
public class VeiculoUsado {

    @Id
    @Column(name = "chassi", length = 17)
    private String chassi;

    @OneToOne
    @MapsId
    @JoinColumn(name = "chassi")
    private Veiculo veiculo;

    @Column(nullable = false, unique = true, length = 7)
    private String placa;

    @Column(nullable = false)
    private Integer quilometragem;

    public VeiculoUsado() {
    }

    public VeiculoUsado(
            Veiculo veiculo,
            String placa,
            Integer quilometragem) {
        this.veiculo = veiculo;
        this.placa = placa;
        this.quilometragem = quilometragem;
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Integer quilometragem) {
        this.quilometragem = quilometragem;
    }
}