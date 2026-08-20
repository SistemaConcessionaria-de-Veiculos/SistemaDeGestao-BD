package com.concessionaria.backend.repository;

import java.util.List;

import com.concessionaria.backend.model.Veiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, String> {

    List<Veiculo> findByNumeroNota(Long numeroNota);
}