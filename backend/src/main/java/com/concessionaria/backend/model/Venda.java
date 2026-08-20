package com.concessionaria.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_nota")
    private Long numeroNota;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Column(name = "matricula_vendedor", nullable = false)
    private Integer matriculaVendedor;

    @Column(name = "valor_total_venda", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotalVenda;

    @Column(name = "data_da_venda", nullable = false)
    private LocalDate dataDaVenda;

    public Venda() {
    }

    public Venda(
            Integer idCliente,
            Integer matriculaVendedor,
            BigDecimal valorTotalVenda,
            LocalDate dataDaVenda) {
        this.idCliente = idCliente;
        this.matriculaVendedor = matriculaVendedor;
        this.valorTotalVenda = valorTotalVenda;
        this.dataDaVenda = dataDaVenda;
    }

    public Long getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Long numeroNota) {
        this.numeroNota = numeroNota;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getMatriculaVendedor() {
        return matriculaVendedor;
    }

    public void setMatriculaVendedor(Integer matriculaVendedor) {
        this.matriculaVendedor = matriculaVendedor;
    }

    public BigDecimal getValorTotalVenda() {
        return valorTotalVenda;
    }

    public void setValorTotalVenda(BigDecimal valorTotalVenda) {
        this.valorTotalVenda = valorTotalVenda;
    }

    public LocalDate getDataDaVenda() {
        return dataDaVenda;
    }

    public void setDataDaVenda(LocalDate dataDaVenda) {
        this.dataDaVenda = dataDaVenda;
    }
}