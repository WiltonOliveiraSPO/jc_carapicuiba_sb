package com.jc_carapicuiba.repository;

import com.jc_carapicuiba.model.Gratidao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GratidaoRepository extends JpaRepository<Gratidao, Integer> {

    List<Gratidao> findAllByOrderByDtGratidaoDesc();

    List<Gratidao> findByTipoGratidaoOrderByDtGratidaoDesc(String tipoGratidao);

    List<Gratidao> findByDtGratidaoBetweenOrderByDtGratidaoAsc(LocalDateTime inicio, LocalDateTime fim);

    @Query("select g from Gratidao g left join fetch g.membro where g.dtGratidao between :inicio and :fim order by g.dtGratidao asc")
    List<Gratidao> findByDtGratidaoBetweenFetchMembro(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("select g from Gratidao g left join fetch g.membro where g.tipoGratidao = :tipo order by g.dtGratidao desc")
    List<Gratidao> findByTipoGratidaoFetchMembro(@Param("tipo") String tipo);

    Optional<Gratidao> findFirstByOrderByCodGratidaoAsc();

    Optional<Gratidao> findFirstByOrderByCodGratidaoDesc();

    Optional<Gratidao> findFirstByCodGratidaoLessThanOrderByCodGratidaoDesc(Integer id);

    Optional<Gratidao> findFirstByCodGratidaoGreaterThanOrderByCodGratidaoAsc(Integer id);
}
