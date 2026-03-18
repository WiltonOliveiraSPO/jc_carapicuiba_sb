package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Dedicacao;
import com.jc_carapicuiba.repository.DedicacaoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dedicacao")
public class DedicacaoController {

    private final DedicacaoRepository repository;

    public DedicacaoController(DedicacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        return carregar(model, new Dedicacao(), null);
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Dedicacao dedicacao) {
        repository.save(dedicacao);
        return "redirect:/dedicacao";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Dedicacao d = repository.findById(id).orElseThrow();
        return carregar(model, d, null);
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        repository.deleteById(id);
        return "redirect:/dedicacao";
    }

    @GetMapping("/primeiro")
    public String primeiro(Model model) {
        Dedicacao d = repository.findFirstByOrderByCodDedicacaoAsc().orElse(new Dedicacao());
        return carregar(model, d, null);
    }

    @GetMapping("/ultimo")
    public String ultimo(Model model) {
        Dedicacao d = repository.findFirstByOrderByCodDedicacaoDesc().orElse(new Dedicacao());
        return carregar(model, d, null);
    }

    @GetMapping("/anterior")
    public String anterior(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            return primeiro(model);
        }

        Dedicacao atual = repository.findById(id).orElse(new Dedicacao());
        return repository.findFirstByCodDedicacaoLessThanOrderByCodDedicacaoDesc(id)
                .map(prev -> carregar(model, prev, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no primeiro registro."));
    }

    @GetMapping("/proximo")
    public String proximo(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            return ultimo(model);
        }

        Dedicacao atual = repository.findById(id).orElse(new Dedicacao());
        return repository.findFirstByCodDedicacaoGreaterThanOrderByCodDedicacaoAsc(id)
                .map(next -> carregar(model, next, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no último registro."));
    }

    private String carregar(Model model, Dedicacao dedicacao, String aviso) {
        model.addAttribute("dedicacao", dedicacao);
        if (aviso != null) {
            model.addAttribute("aviso", aviso);
        }
        return "dedicacao";
    }
}
