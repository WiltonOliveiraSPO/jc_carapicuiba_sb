package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Membro;
import com.jc_carapicuiba.repository.MembroRepository;
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
@RequestMapping("/pesquisa-membros")
public class PesquisaMembrosController {

    private final MembroRepository repository;

    public PesquisaMembrosController(MembroRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String tela(Model model) {
        model.addAttribute("lista", List.of());
        return "pesquisa_membros";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String nome, Model model) {
        model.addAttribute("lista", repository.findByNomeContainingIgnoreCaseOrderByNome(nome));
        model.addAttribute("nome", nome);
        return "pesquisa_membros";
    }

    @GetMapping("/pdf")
    public void pdf(@RequestParam Integer id, HttpServletResponse response) throws Exception {

        Membro m = repository.findById(id).orElse(null);
        if (m == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Membro não encontrado.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=membro_" + id + ".pdf");

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
                    "\nJohrei Center Carapicuíba - Dados cadastrais do membro\n\n",
                    tituloFont
            );
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            Font f = new Font(Font.HELVETICA, 11);
            doc.add(new Paragraph("Código: " + m.getCodMembro(), f));
            doc.add(new Paragraph("Nome: " + m.getNome(), f));
            doc.add(new Paragraph("Endereço: " + m.getEndereco(), f));
            doc.add(new Paragraph("Telefone: " + m.getTelefone(), f));
            doc.add(new Paragraph("Email: " + m.getEmail(), f));
            doc.add(new Paragraph("CPF: " + m.getCpf(), f));

            if (m.getDataOutorga() != null) {
                doc.add(new Paragraph("Data de Outorga: " + m.getDataOutorga().format(fmt), f));
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
