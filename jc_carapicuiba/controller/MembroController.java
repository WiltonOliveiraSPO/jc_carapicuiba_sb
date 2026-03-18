package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Membro;
import com.jc_carapicuiba.repository.MembroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/membros")
public class MembroController {

    private final MembroRepository repository;

    public MembroController(MembroRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        return carregar(model, new Membro(), null);
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Membro membro) {
        repository.save(membro);
        return "redirect:/membros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Membro m = repository.findById(id).orElseThrow();
        return carregar(model, m, null);
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        repository.deleteById(id);
        return "redirect:/membros";
    }

    @GetMapping("/primeiro")
    public String primeiro(Model model) {
        Membro m = repository.findFirstByOrderByCodMembroAsc().orElse(new Membro());
        return carregar(model, m, null);
    }

    @GetMapping("/ultimo")
    public String ultimo(Model model) {
        Membro m = repository.findFirstByOrderByCodMembroDesc().orElse(new Membro());
        return carregar(model, m, null);
    }

    @GetMapping("/anterior")
    public String anterior(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            return primeiro(model);
        }

        Membro atual = repository.findById(id).orElse(new Membro());
        return repository.findFirstByCodMembroLessThanOrderByCodMembroDesc(id)
                .map(prev -> carregar(model, prev, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no primeiro registro."));
    }

    @GetMapping("/proximo")
    public String proximo(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            return ultimo(model);
        }

        Membro atual = repository.findById(id).orElse(new Membro());
        return repository.findFirstByCodMembroGreaterThanOrderByCodMembroAsc(id)
                .map(next -> carregar(model, next, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no último registro."));
    }

    private String carregar(Model model, Membro membro, String aviso) {
        model.addAttribute("membro", membro);
        if (aviso != null) {
            model.addAttribute("aviso", aviso);
        }
        return "membros";
    }
}
