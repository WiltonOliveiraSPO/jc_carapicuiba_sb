package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Pedido;
import com.jc_carapicuiba.repository.PedidoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repository;

    public PedidoController(PedidoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {

        List<Pedido> lista = repository.findAllByOrderByDtPedidosAsc();

        model.addAttribute("pedido", new Pedido());
        model.addAttribute("lista", lista);

        return "pedidos";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pedido pedido) {
        repository.save(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {

        Pedido p = repository.findById(id).orElseThrow();

        model.addAttribute("pedido", p);
        model.addAttribute("lista", repository.findAllByOrderByDtPedidosAsc());

        return "pedidos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {

        repository.deleteById(id);
        return "redirect:/pedidos";
    }
}