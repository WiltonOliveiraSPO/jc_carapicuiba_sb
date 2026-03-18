package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Pedido;
import com.jc_carapicuiba.repository.PedidoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repository;

    public PedidoController(PedidoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        return carregar(model, new Pedido(), null);
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pedido pedido) {
        repository.save(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Pedido p = repository.findById(id).orElseThrow();
        return carregar(model, p, null);
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        repository.deleteById(id);
        return "redirect:/pedidos";
    }

    @GetMapping("/primeiro")
    public String primeiro(Model model) {
        Pedido p = repository.findFirstByOrderByCodPedidosAsc().orElse(new Pedido());
        return carregar(model, p, null);
    }

    @GetMapping("/ultimo")
    public String ultimo(Model model) {
        Pedido p = repository.findFirstByOrderByCodPedidosDesc().orElse(new Pedido());
        return carregar(model, p, null);
    }

    @GetMapping("/anterior")
    public String anterior(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            return primeiro(model);
        }

        Pedido atual = repository.findById(id).orElse(new Pedido());
        return repository.findFirstByCodPedidosLessThanOrderByCodPedidosDesc(id)
                .map(prev -> carregar(model, prev, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no primeiro registro."));
    }

    @GetMapping("/proximo")
    public String proximo(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            return ultimo(model);
        }

        Pedido atual = repository.findById(id).orElse(new Pedido());
        return repository.findFirstByCodPedidosGreaterThanOrderByCodPedidosAsc(id)
                .map(next -> carregar(model, next, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no último registro."));
    }

    private String carregar(Model model, Pedido pedido, String aviso) {
        model.addAttribute("pedido", pedido);
        if (aviso != null) {
            model.addAttribute("aviso", aviso);
        }
        return "pedidos";
    }
}
