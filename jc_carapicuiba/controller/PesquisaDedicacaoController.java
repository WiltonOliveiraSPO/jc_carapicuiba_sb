package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Dedicacao;
import com.jc_carapicuiba.repository.DedicacaoRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/pesquisa-dedicacao")
public class PesquisaDedicacaoController {

    private final DedicacaoRepository repository;

    public PesquisaDedicacaoController(DedicacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String tela(Model model) {
        model.addAttribute("lista", List.of());
        model.addAttribute("totMembros", 0);
        model.addAttribute("totFreq", 0);
        model.addAttribute("totPrim", 0);
        return "pesquisa_dedicacao";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                         Model model) {

        List<Dedicacao> lista = repository.findByDtDedicacaoBetweenOrderByDtDedicacaoAsc(inicio, fim);

        int totMembros = lista.stream().mapToInt(Dedicacao::getQtdeMembros).sum();
        int totFreq = lista.stream().mapToInt(Dedicacao::getQtdeFrequentadores).sum();
        int totPrim = lista.stream().mapToInt(Dedicacao::getQtdePrimVez).sum();

        model.addAttribute("lista", lista);
        model.addAttribute("totMembros", totMembros);
        model.addAttribute("totFreq", totFreq);
        model.addAttribute("totPrim", totPrim);
        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);

        return "pesquisa_dedicacao";
    }

    @GetMapping("/pdf")
    public void exportarPdf(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                            HttpServletResponse response) throws Exception {

        if (inicio == null || fim == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Informe data inicial e final.");
            return;
        }

        List<Dedicacao> lista = repository.findByDtDedicacaoBetweenOrderByDtDedicacaoAsc(inicio, fim);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=dedicacao_diaria.pdf");

        try (OutputStream out = response.getOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            try {
                ClassPathResource logoRes = new ClassPathResource("img/izunome_img.jpg");
                if (logoRes.exists()) {
                    Image logo = Image.getInstance(logoRes.getInputStream().readAllBytes());
                    logo.scaleToFit(80, 80);
                    logo.setAlignment(Image.ALIGN_LEFT);
                    document.add(logo);
                }
            } catch (Exception ignored) {
                // Se a imagem falhar, o PDF ainda deve ser gerado.
            }

            Font tituloFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Paragraph titulo = new Paragraph(
                    "Johrei Center Carapicuíba - Dedicação Diária - Frequência\n" +
                    "Período: " + inicio.format(fmt) + " a " + fim.format(fmt) + "\n\n",
                    tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 2f, 3f, 2f});

            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            String[] headers = {"Data", "Membros", "Frequentadores", "Primeira Vez"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                table.addCell(cell);
            }

            int totMembros = 0, totFreq = 0, totPrim = 0;

            for (Dedicacao d : lista) {
                table.addCell(d.getDtDedicacao().format(fmt));
                table.addCell(String.valueOf(d.getQtdeMembros()));
                table.addCell(String.valueOf(d.getQtdeFrequentadores()));
                table.addCell(String.valueOf(d.getQtdePrimVez()));

                totMembros += d.getQtdeMembros();
                totFreq += d.getQtdeFrequentadores();
                totPrim += d.getQtdePrimVez();
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            Font totalFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Paragraph totais = new Paragraph(
                    "Total Geral:\n" +
                    "Membros: " + totMembros + "\n" +
                    "Frequentadores: " + totFreq + "\n" +
                    "Primeira Vez: " + totPrim,
                    totalFont);
            totais.setAlignment(Element.ALIGN_RIGHT);
            document.add(totais);

            document.close();
        }
    }
}
