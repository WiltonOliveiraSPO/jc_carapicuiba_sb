package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Membro;
import com.jc_carapicuiba.repository.MembroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/membros")
public class MembroController {

    private final MembroRepository repository;

    public MembroController(MembroRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {

        List<Membro> lista = repository.findAllByOrderByNomeAsc();

        model.addAttribute("membro", new Membro());
        model.addAttribute("lista", lista);

        return "membros";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Membro membro) {

        repository.save(membro);
        return "redirect:/membros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {

        Membro m = repository.findById(id).orElseThrow();

        model.addAttribute("membro", m);
        model.addAttribute("lista", repository.findAllByOrderByNomeAsc());

        return "membros";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {

        repository.deleteById(id);
        return "redirect:/membros";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String nome, Model model) {

        model.addAttribute("membro", new Membro());
        model.addAttribute("lista",
                repository.findByNomeContainingIgnoreCaseOrderByNome(nome));

        return "membros";
    }
}