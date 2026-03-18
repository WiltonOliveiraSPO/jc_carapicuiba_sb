package com.jc_carapicuiba.repository;

import com.jc_carapicuiba.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findAllByOrderByDtPedidosAsc();

    List<Pedido> findByDtPedidosBetweenOrderByDtPedidosAsc(LocalDate inicio, LocalDate fim);

    Optional<Pedido> findFirstByOrderByCodPedidosAsc();

    Optional<Pedido> findFirstByOrderByCodPedidosDesc();

    Optional<Pedido> findFirstByCodPedidosLessThanOrderByCodPedidosDesc(Integer id);

    Optional<Pedido> findFirstByCodPedidosGreaterThanOrderByCodPedidosAsc(Integer id);
}
