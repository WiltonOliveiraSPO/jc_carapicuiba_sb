package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Gratidao;
import com.jc_carapicuiba.model.Membro;
import com.jc_carapicuiba.repository.MembroRepository;
import com.jc_carapicuiba.service.GratidaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gratidao")
public class GratidaoController {

    private static final List<String> TIPOS_GRATIDAO = List.of(
            "Mensal",
            "Diário",
            "Gratidão Especial",
            "Cultos Especiais",
            "Pedido de Prece",
            "Solo Sagrado",
            "Outorga de Ohikari",
            "Outorga de Shoko",
            "Reoutorga",
            "Paraíso no lar",
            "Sorei Saishi",
            "Construção",
            "Cerimonial",
            "Imagem do lar",
            "Mitamaya",
            "Caligrafias",
            "Publicações / Kit Outorga",
            "Horta",
            "Outorga Daikomyo",
            "Outorga Komyo",
            "Joshô",
            "Ensino",
            "Reconsagração"
    );

    private final GratidaoService service;
    private final MembroRepository membroRepository;

    public GratidaoController(GratidaoService service, MembroRepository membroRepository) {
        this.service = service;
        this.membroRepository = membroRepository;
    }

    @GetMapping
    public String listar(Model model) {
        return carregar(model, new Gratidao(), null);
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Gratidao gratidao,
                         @RequestParam Integer codMembro) {

        Membro m = membroRepository.findById(codMembro).orElseThrow();
        gratidao.setMembro(m);

        service.salvar(gratidao);
        return "redirect:/gratidao";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        return carregar(model, service.buscar(id), null);
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        service.excluir(id);
        return "redirect:/gratidao";
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
        Gratidao atual = (id == null) ? service.primeiro() : service.buscar(id);
        return service.anteriorOptional(id)
                .map(prev -> carregar(model, prev, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no primeiro registro."));
    }

    @GetMapping("/proximo")
    public String proximo(@RequestParam(required = false) Integer id, Model model) {
        Gratidao atual = (id == null) ? service.ultimo() : service.buscar(id);
        return service.proximoOptional(id)
                .map(next -> carregar(model, next, null))
                .orElseGet(() -> carregar(model, atual, "Você já está no último registro."));
    }

    private String carregar(Model model, Gratidao gratidao, String aviso) {
        model.addAttribute("gratidao", gratidao);
        model.addAttribute("membros", membroRepository.findAllByOrderByNomeAsc());
        model.addAttribute("tipos", TIPOS_GRATIDAO);
        if (aviso != null) {
            model.addAttribute("aviso", aviso);
        }
        return "gratidao";
    }
}
