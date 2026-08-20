package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {

    Optional<Vendedor> findByCpf(String cpf);
}