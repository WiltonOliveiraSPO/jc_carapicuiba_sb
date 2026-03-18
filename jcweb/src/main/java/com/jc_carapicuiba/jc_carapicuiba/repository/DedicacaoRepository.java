package com.jc_carapicuiba.repository;

import com.jc_carapicuiba.model.Dedicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DedicacaoRepository extends JpaRepository<Dedicacao, Integer> {

    List<Dedicacao> findByDtDedicacaoBetweenOrderByDtDedicacaoAsc(LocalDate inicio, LocalDate fim);

    Optional<Dedicacao> findFirstByOrderByCodDedicacaoAsc();

    Optional<Dedicacao> findFirstByOrderByCodDedicacaoDesc();

    Optional<Dedicacao> findFirstByCodDedicacaoLessThanOrderByCodDedicacaoDesc(Integer id);

    Optional<Dedicacao> findFirstByCodDedicacaoGreaterThanOrderByCodDedicacaoAsc(Integer id);
}
