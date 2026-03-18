package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.PrimeiraVez;
import com.jc_carapicuiba.repository.PrimeiraVezRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/pesquisa-primeiravez")
public class PesquisaPrimeiraVezController {

    private final PrimeiraVezRepository repository;

    public PesquisaPrimeiraVezController(PrimeiraVezRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String tela(Model model) {
        model.addAttribute("lista", List.of());
        return "pesquisa_primeiravez";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String nome, Model model) {
        model.addAttribute("lista", repository.findByNomeContainingIgnoreCaseOrderByDataPrimVezDesc(nome));
        model.addAttribute("nome", nome);
        return "pesquisa_primeiravez";
    }

    @GetMapping("/pdf")
    public void pdf(@RequestParam Integer id, HttpServletResponse response) throws Exception {

        PrimeiraVez p = repository.findById(id).orElse(null);
        if (p == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro não encontrado.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=primeiravez_" + id + ".pdf");

        try (OutputStream out = response.getOutputStream()) {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);
            doc.open();

            try {
                ClassPathResource logoRes = new ClassPathResource("img/izunome_img.jpg");
                if (logoRes.exists()) {
                    Image logo = Image.getInstance(logoRes.getInputStream().readAllBytes());
                    logo.scalePercent(10);
                    logo.setAlignment(Image.ALIGN_LEFT);
                    doc.add(logo);
                }
            } catch (Exception ignored) {
                // Se a imagem falhar, o PDF ainda deve ser gerado.
            }

            Font tituloFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Paragraph titulo = new Paragraph(
                    "\nJohrei Center Carapicuíba - Primeira Vez\n\n",
                    tituloFont
            );
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            Font f = new Font(Font.HELVETICA, 11);
            doc.add(new Paragraph("Nome (Primeira vez): " + p.getNome(), f));
            doc.add(new Paragraph("Indicado por: " + (p.getMembro() != null ? p.getMembro().getNome() : ""), f));
            doc.add(new Paragraph("Endereço: " + p.getEndereco(), f));
            doc.add(new Paragraph("Telefone: " + p.getTelefone(), f));
            doc.add(new Paragraph("Email: " + p.getEmail(), f));

            if (p.getDataPrimVez() != null) {
                doc.add(new Paragraph("Data: " + p.getDataPrimVez().format(fmt), f));
            }

            doc.add(new Paragraph("\n\n"));

            Paragraph rodape = new Paragraph(
                    "Relatório gerado em: " + LocalDateTime.now().format(fmt),
                    new Font(Font.HELVETICA, 9)
            );
            rodape.setAlignment(Element.ALIGN_RIGHT);
            doc.add(rodape);

            doc.close();
        }
    }
}
