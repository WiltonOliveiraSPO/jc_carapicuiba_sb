package com.jc_carapicuiba.repository;

import com.jc_carapicuiba.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembroRepository extends JpaRepository<Membro, Integer> {

    List<Membro> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    List<Membro> findAllByOrderByNomeAsc();

    Optional<Membro> findFirstByOrderByCodMembroAsc();

    Optional<Membro> findFirstByOrderByCodMembroDesc();

    Optional<Membro> findFirstByCodMembroLessThanOrderByCodMembroDesc(Integer id);

    Optional<Membro> findFirstByCodMembroGreaterThanOrderByCodMembroAsc(Integer id);
}
