package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.VeiculoNovo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoNovoRepository
        extends JpaRepository<VeiculoNovo, String> {
}