package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Integer> {

    Optional<PessoaFisica> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
