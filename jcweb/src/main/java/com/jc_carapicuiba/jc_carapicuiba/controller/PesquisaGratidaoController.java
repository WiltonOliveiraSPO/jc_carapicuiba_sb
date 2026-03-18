package com.jc_carapicuiba.controller;

import com.jc_carapicuiba.model.Gratidao;
import com.jc_carapicuiba.repository.GratidaoRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/pesquisa-gratidao")
public class PesquisaGratidaoController {

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

    private final GratidaoRepository repository;

    public PesquisaGratidaoController(GratidaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String tela(Model model) {
        model.addAttribute("listaData", List.of());
        model.addAttribute("listaTipo", List.of());
        model.addAttribute("totalData", BigDecimal.ZERO);
        model.addAttribute("totalTipo", BigDecimal.ZERO);
        model.addAttribute("tipos", TIPOS_GRATIDAO);
        return "pesquisa_gratidao";
    }

    @GetMapping("/buscar-data")
    public String buscarData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                             Model model) {

        if (inicio == null || fim == null) {
            model.addAttribute("avisoData", "Informe data inicial e final.");
            model.addAttribute("listaData", List.of());
            model.addAttribute("totalData", BigDecimal.ZERO);
            model.addAttribute("tipos", TIPOS_GRATIDAO);
            return "pesquisa_gratidao";
        }

        if (inicio.isAfter(fim)) {
            model.addAttribute("avisoData", "A data inicial não pode ser maior que a data final.");
            model.addAttribute("listaData", List.of());
            model.addAttribute("totalData", BigDecimal.ZERO);
            model.addAttribute("tipos", TIPOS_GRATIDAO);
            return "pesquisa_gratidao";
        }

        LocalDateTime ini = inicio.atStartOfDay();
        LocalDateTime fimDt = fim.atTime(23, 59, 59);

        List<Gratidao> lista = repository.findByDtGratidaoBetweenOrderByDtGratidaoAsc(ini, fimDt);
        BigDecimal total = lista.stream()
                .map(Gratidao::getVlGratidao)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("listaData", lista);
        model.addAttribute("totalData", total);
        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);

        model.addAttribute("listaTipo", List.of());
        model.addAttribute("totalTipo", BigDecimal.ZERO);
        model.addAttribute("tipos", TIPOS_GRATIDAO);

        return "pesquisa_gratidao";
    }

    @GetMapping("/buscar-tipo")
    public String buscarTipo(@RequestParam String tipo, Model model) {
        if (tipo == null || tipo.isBlank()) {
            model.addAttribute("avisoTipo", "Selecione o tipo de gratidão.");
            model.addAttribute("listaTipo", List.of());
            model.addAttribute("totalTipo", BigDecimal.ZERO);
            model.addAttribute("tipos", TIPOS_GRATIDAO);
            return "pesquisa_gratidao";
        }

        List<Gratidao> lista = repository.findByTipoGratidaoOrderByDtGratidaoDesc(tipo);
        BigDecimal total = lista.stream()
                .map(Gratidao::getVlGratidao)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("listaTipo", lista);
        model.addAttribute("totalTipo", total);
        model.addAttribute("tipo", tipo);

        model.addAttribute("listaData", List.of());
        model.addAttribute("totalData", BigDecimal.ZERO);
        model.addAttribute("tipos", TIPOS_GRATIDAO);

        return "pesquisa_gratidao";
    }

    @Transactional(readOnly = true)
    @GetMapping("/excel-data")
    public void excelData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
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

        LocalDateTime ini = inicio.atStartOfDay();
        LocalDateTime fimDt = fim.atTime(23, 59, 59);
        List<Gratidao> lista = repository.findByDtGratidaoBetweenFetchMembro(ini, fimDt);

        gerarExcel(lista, response, "gratidao_por_data.xlsx");
    }

    @Transactional(readOnly = true)
    @GetMapping("/excel-tipo")
    public void excelTipo(@RequestParam String tipo, HttpServletResponse response) throws Exception {
        if (tipo == null || tipo.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Selecione o tipo de gratidão.");
            return;
        }

        List<Gratidao> lista = repository.findByTipoGratidaoFetchMembro(tipo);
        gerarExcel(lista, response, "gratidao_por_tipo.xlsx");
    }

    private void gerarExcel(List<Gratidao> lista, HttpServletResponse response, String nomeArquivo) throws Exception {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + nomeArquivo);

        try (XSSFWorkbook wb = new XSSFWorkbook(); OutputStream out = response.getOutputStream()) {
            Sheet sheet = wb.createSheet("Gratidão");

            int rowIdx = 0;
            Row header = sheet.createRow(rowIdx++);
            String[] cols = {"Data", "Membro", "Tipo", "Valor"};
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            BigDecimal total = BigDecimal.ZERO;

            CellStyle currencyStyle = wb.createCellStyle();
            DataFormat df = wb.createDataFormat();
            currencyStyle.setDataFormat(df.getFormat("R$ #,##0.00"));

            for (Gratidao g : lista) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(g.getDtGratidao() != null ? g.getDtGratidao().format(fmt) : "");
                row.createCell(1).setCellValue(g.getMembro() != null ? g.getMembro().getNome() : "");
                row.createCell(2).setCellValue(g.getTipoGratidao());

                Cell valorCell = row.createCell(3);
                valorCell.setCellValue(g.getVlGratidao() != null ? g.getVlGratidao().doubleValue() : 0.0);
                valorCell.setCellStyle(currencyStyle);

                if (g.getVlGratidao() != null) {
                    total = total.add(g.getVlGratidao());
                }
            }

            Row totalRow = sheet.createRow(rowIdx + 1);
            Cell label = totalRow.createCell(2);
            label.setCellValue("TOTAL");

            Cell totalCell = totalRow.createCell(3);
            totalCell.setCellValue(total.doubleValue());
            totalCell.setCellStyle(currencyStyle);

            CellStyle boldStyle = wb.createCellStyle();
            Font boldFont = wb.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            label.setCellStyle(boldStyle);
            totalCell.setCellStyle(boldStyle);

            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            wb.write(out);
        }
    }
}
