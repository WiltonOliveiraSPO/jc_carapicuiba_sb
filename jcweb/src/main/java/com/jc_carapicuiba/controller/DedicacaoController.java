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
        model.addAttribute("dedicacao", new Dedicacao());
        model.addAttribute("lista", repository.findAll());
        return "dedicacao";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Dedicacao dedicacao) {
        repository.save(dedicacao);
        return "redirect:/dedicacao";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Dedicacao d = repository.findById(id).orElseThrow();
        model.addAttribute("dedicacao", d);
        model.addAttribute("lista", repository.findAll());
        return "dedicacao";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        repository.deleteById(id);
        return "redirect:/dedicacao";
    }
}