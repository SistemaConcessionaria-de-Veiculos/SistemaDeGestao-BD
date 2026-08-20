package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.VeiculoUsado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeiculoUsadoRepository
        extends JpaRepository<VeiculoUsado, String> {

    Optional<VeiculoUsado> findByPlaca(String placa);
}