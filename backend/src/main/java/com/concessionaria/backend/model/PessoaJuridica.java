package com.concessionaria.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pessoas_juridicas")
public class PessoaJuridica {

    @Id
    @Column(name = "id_cliente")
    private Integer idCliente;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    public PessoaJuridica() {
    }

    public PessoaJuridica(Cliente cliente, String cnpj) {
        this.cliente = cliente;
        this.cnpj = cnpj;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}