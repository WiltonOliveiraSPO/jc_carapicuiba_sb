package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Pedido;
import com.jc_carapicuiba.repository.PedidoRepository;
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
@RequestMapping("/pesquisa-pedidos")
public class PesquisaPedidosController {

    private final PedidoRepository repository;

    public PesquisaPedidosController(PedidoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String tela(Model model) {
        model.addAttribute("lista", List.of());
        model.addAttribute("totAgra", 0);
        model.addAttribute("totGraca", 0);
        model.addAttribute("totElev", 0);
        model.addAttribute("totAniv", 0);
        return "pesquisa_pedidos";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                         Model model) {

        if (inicio == null || fim == null) {
            model.addAttribute("erro", "Informe data inicial e final.");
            model.addAttribute("lista", List.of());
            model.addAttribute("totAgra", 0);
            model.addAttribute("totGraca", 0);
            model.addAttribute("totElev", 0);
            model.addAttribute("totAniv", 0);
            return "pesquisa_pedidos";
        }

        if (inicio.isAfter(fim)) {
            model.addAttribute("erro", "A data inicial não pode ser maior que a data final.");
            model.addAttribute("lista", List.of());
            model.addAttribute("totAgra", 0);
            model.addAttribute("totGraca", 0);
            model.addAttribute("totElev", 0);
            model.addAttribute("totAniv", 0);
            model.addAttribute("inicio", inicio);
            model.addAttribute("fim", fim);
            return "pesquisa_pedidos";
        }

        List<Pedido> lista = repository.findByDtPedidosBetweenOrderByDtPedidosAsc(inicio, fim);

        int totAgra = lista.stream().mapToInt(Pedido::getQtdeAgradecimento).sum();
        int totGraca = lista.stream().mapToInt(Pedido::getQtdeGraca).sum();
        int totElev = lista.stream().mapToInt(Pedido::getQtdeElevacao).sum();
        int totAniv = lista.stream().mapToInt(Pedido::getQtdeAnivFalec).sum();

        model.addAttribute("lista", lista);
        model.addAttribute("totAgra", totAgra);
        model.addAttribute("totGraca", totGraca);
        model.addAttribute("totElev", totElev);
        model.addAttribute("totAniv", totAniv);
        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);

        return "pesquisa_pedidos";
    }

    @GetMapping("/pdf")
    public void exportarPdf(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                            HttpServletResponse response) throws Exception {

        if (inicio == null || fim == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Informe data inicial e final.");
            return;
        }

        if (inicio.isAfter(fim)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "A data inicial não pode ser maior que a data final.");
            return;
        }

        List<Pedido> lista = repository.findByDtPedidosBetweenOrderByDtPedidosAsc(inicio, fim);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=pedidos_diarios.pdf");

        try (OutputStream out = response.getOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
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
                    "Johrei Center Carapicuíba - Pedidos Diários\n" +
                    "Período: " + inicio.format(fmt) + " a " + fim.format(fmt) + "\n\n",
                    tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            String[] headers = {"Data", "Agradecimento", "Graça", "Elevação", "Aniv. Falecimento"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            int totAgra = 0, totGraca = 0, totElev = 0, totAniv = 0;

            for (Pedido p : lista) {
                table.addCell(p.getDtPedidos().format(fmt));
                table.addCell(String.valueOf(p.getQtdeAgradecimento()));
                table.addCell(String.valueOf(p.getQtdeGraca()));
                table.addCell(String.valueOf(p.getQtdeElevacao()));
                table.addCell(String.valueOf(p.getQtdeAnivFalec()));

                totAgra += p.getQtdeAgradecimento();
                totGraca += p.getQtdeGraca();
                totElev += p.getQtdeElevacao();
                totAniv += p.getQtdeAnivFalec();
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            Font totalFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Paragraph totais = new Paragraph(
                    "Total Geral:\n" +
                    "Agradecimento: " + totAgra + "\n" +
                    "Graça: " + totGraca + "\n" +
                    "Elevação: " + totElev + "\n" +
                    "Aniv. Falecimento: " + totAniv,
                    totalFont);
            totais.setAlignment(Element.ALIGN_RIGHT);
            document.add(totais);

            document.close();
        }
    }
}
