package com.concessionaria.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pessoas_fisicas")
public class PessoaFisica {

    @Id
    @Column(name = "id_cliente")
    private Integer idCliente;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    public PessoaFisica() {
    }

    public PessoaFisica(Cliente cliente, String cpf) {
        this.cliente = cliente;
        this.cpf = cpf;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}