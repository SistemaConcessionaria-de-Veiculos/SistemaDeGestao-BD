package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Integer> {

    Optional<PessoaJuridica> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
