package com.jc_carapicuiba.repository;

import com.jc_carapicuiba.model.PrimeiraVez;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrimeiraVezRepository extends JpaRepository<PrimeiraVez, Integer> {

    List<PrimeiraVez> findAllByOrderByDataPrimVezDesc();

    List<PrimeiraVez> findByNomeContainingIgnoreCaseOrderByDataPrimVezDesc(String nome);

    Optional<PrimeiraVez> findFirstByOrderByCodPrimVezAsc();

    Optional<PrimeiraVez> findFirstByOrderByCodPrimVezDesc();

    Optional<PrimeiraVez> findFirstByCodPrimVezLessThanOrderByCodPrimVezDesc(Integer id);

    Optional<PrimeiraVez> findFirstByCodPrimVezGreaterThanOrderByCodPrimVezAsc(Integer id);
}
