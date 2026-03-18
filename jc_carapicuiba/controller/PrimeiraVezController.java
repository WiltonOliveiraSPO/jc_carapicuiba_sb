package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Membro;
import com.jc_carapicuiba.model.PrimeiraVez;
import com.jc_carapicuiba.repository.MembroRepository;
import com.jc_carapicuiba.service.PrimeiraVezService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/primeiravez")
public class PrimeiraVezController {

    private final PrimeiraVezService service;
    private final MembroRepository membroRepository;

    public PrimeiraVezController(PrimeiraVezService service, MembroRepository membroRepository) {
        this.service = service;
        this.membroRepository = membroRepository;
    }

    @GetMapping
    public String listar(Model model) {
        return carregar(model, new PrimeiraVez(), null);
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute PrimeiraVez primeiraVez,
                         @RequestParam Integer codMembro) {

        Membro m = membroRepository.findById(codMembro).orElseThrow();
        primeiraVez.setMembro(m);

        service.salvar(primeiraVez);
        return "redirect:/primeiravez";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        return carregar(model, service.buscar(id), null);
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        service.excluir(id);
        return "redirect:/primeiravez";
    }

    @GetMapping("/primeiro")
    public String primeiro(Model model) {
        return carregar(model, service.primeiro(), null);
    }

    @GetMapping("/ultimo")
    public String ultimo(Model model) {
        return carregar(model, service.ultimo(), null);
    }

    @GetMapping("/anterior")
    public String anterior(@RequestParam(required = false) Integer id, Model model) {
        PrimeiraVez atual = (id == null) ? service.primeiro() : service.buscar(id);
        return service.anteriorOptional(id)
                .map(prev -> carregar(model, prev, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no primeiro registro."));
    }

    @GetMapping("/proximo")
    public String proximo(@RequestParam(required = false) Integer id, Model model) {
        PrimeiraVez atual = (id == null) ? service.ultimo() : service.buscar(id);
        return service.proximoOptional(id)
                .map(next -> carregar(model, next, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no último registro."));
    }

    private String carregar(Model model, PrimeiraVez primeiraVez, String aviso) {
        model.addAttribute("primeiraVez", primeiraVez);
        model.addAttribute("membros", membroRepository.findAllByOrderByNomeAsc());
        if (aviso != null) {
            model.addAttribute("aviso", aviso);
        }
        return "primeiravez";
    }
}
