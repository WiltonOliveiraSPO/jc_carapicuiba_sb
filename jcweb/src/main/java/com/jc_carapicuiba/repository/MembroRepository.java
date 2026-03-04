package com.jc_carapicuiba.repository;

import com.jc_carapicuiba.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembroRepository extends JpaRepository<Membro, Integer> {

    List<Membro> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    List<Membro> findAllByOrderByNomeAsc();
}