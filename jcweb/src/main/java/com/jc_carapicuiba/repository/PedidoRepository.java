package com.jc_carapicuiba.repository;

import com.jc_carapicuiba.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findAllByOrderByDtPedidosAsc();

}