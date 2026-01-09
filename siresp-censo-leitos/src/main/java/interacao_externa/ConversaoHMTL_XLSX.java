package interacao_externa;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ConversaoHMTL_XLSX {

    static class StyleCache {
        private final Workbook wb;
        private final Map<String, CellStyle> cache = new HashMap<>();
        private final Map<String, Font> fontCache = new HashMap<>();

        StyleCache(Workbook wb) { this.wb = wb; }

        CellStyle getStyleFor(Map<String, String> css) {
            String key = cssKey(css);
            return cache.computeIfAbsent(key, k -> buildStyle(css));
        }

        private String cssKey(Map<String, String> css) {
            // chave determinística para cache (evita milhares de estilos duplicados)
            return String.join(";", css.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue()).toList());
        }

        private CellStyle buildStyle(Map<String, String> css) {
            CellStyle st = wb.createCellStyle();

            // Alinhamento
            String ta = css.getOrDefault("text-align", "").toLowerCase();
            if (ta.contains("center")) st.setAlignment(HorizontalAlignment.CENTER);
            else if (ta.contains("right")) st.setAlignment(HorizontalAlignment.RIGHT);
            else if (ta.contains("justify")) st.setAlignment(HorizontalAlignment.JUSTIFY);
            else st.setAlignment(HorizontalAlignment.LEFT);

            String va = css.getOrDefault("vertical-align", "").toLowerCase();
            if (va.contains("middle") || va.contains("center")) st.setVerticalAlignment(VerticalAlignment.CENTER);
            else if (va.contains("bottom")) st.setVerticalAlignment(VerticalAlignment.BOTTOM);
            else st.setVerticalAlignment(VerticalAlignment.TOP);

            // Bordas
            if (css.containsKey("border") || css.containsKey("border-top") ||
                css.containsKey("border-right") || css.containsKey("border-bottom") || css.containsKey("border-left")) {
                st.setBorderTop(BorderStyle.THIN);
                st.setBorderRight(BorderStyle.THIN);
                st.setBorderBottom(BorderStyle.THIN);
                st.setBorderLeft(BorderStyle.THIN);
                // (Opcional: cor de borda por lado, se desejar)
            }

            // Fundo (fill)
            String bg = css.getOrDefault("background-color", "");
            if (!bg.isBlank()) {
                var xssfColor = ColorUtil.xssfColorFromCss(bg);
                if (xssfColor != null) {
                    st.setFillForegroundColor(xssfColor);
                    st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }
            }

            // Fonte (negrito/itálico/underline/cor)
            Font f = fontFor(css);
            st.setFont(f);

            return st;
        }

        private Font fontFor(Map<String, String> css) {
            String key = "bold=" + css.getOrDefault("font-weight","") +
                         ";italic=" + css.getOrDefault("font-style","") +
                         ";underline=" + css.getOrDefault("text-decoration","") +
                         ";color=" + css.getOrDefault("color","") +
                         ";size=" + css.getOrDefault("font-size","") +
                         ";family=" + css.getOrDefault("font-family","");

            return fontCache.computeIfAbsent(key, k -> {
                Font f = wb.createFont();
                String fw = css.getOrDefault("font-weight","").toLowerCase();
                f.setBold(fw.contains("bold") || fw.matches("\\d{3,}")); // 700 etc.

                String fs = css.getOrDefault("font-style","").toLowerCase();
                f.setItalic(fs.contains("italic"));

                String td = css.getOrDefault("text-decoration","").toLowerCase();
                if (td.contains("underline")) f.setUnderline(FontUnderline.SINGLE.getByteValue());

                String color = css.getOrDefault("color","");
                var xssfColor = ColorUtil.xssfColorFromCss(color);
                if (xssfColor != null && f instanceof org.apache.poi.xssf.usermodel.XSSFFont xf) {
                    xf.setColor(xssfColor);
                }

                String size = css.getOrDefault("font-size","");
                double pt = CssUtil.fontSizeToPoints(size);
                if (pt > 0) f.setFontHeightInPoints((short)Math.round(pt));

                String family = css.getOrDefault("font-family","");
                if (!family.isBlank()) f.setFontName(CssUtil.normalizeFontFamily(family));

                return f;
            });
        }
    }

    static class CssUtil {
        static Map<String,String> parseInline(String style) {
            Map<String,String> map = new HashMap<>();
            if (style == null) return map;
            for (String part : style.split(";")) {
                int i = part.indexOf(':');
                if (i > 0) {
                    String k = part.substring(0,i).trim().toLowerCase();
                    String v = part.substring(i+1).trim();
                    map.put(k, v);
                }
            }
            return map;
        }

        static double fontSizeToPoints(String cssVal) {
            if (cssVal == null || cssVal.isBlank()) return -1;
            cssVal = cssVal.trim().toLowerCase();
            try {
                if (cssVal.endsWith("px")) {
                    double px = Double.parseDouble(cssVal.replace("px",""));
                    return px * 0.75; // ~96 dpi
                } else if (cssVal.endsWith("pt")) {
                    return Double.parseDouble(cssVal.replace("pt",""));
                } else {
                    // simplificação para valores numéricos puros
                    return Double.parseDouble(cssVal);
                }
            } catch (NumberFormatException e) { return -1; }
        }

        static String normalizeFontFamily(String ff) {
            // pick first family; remove quotes
            String first = ff.split(",")[0].trim();
            if (first.startsWith("\"") || first.startsWith("'")) first = first.substring(1);
            if (first.endsWith("\"") || first.endsWith("'")) first = first.substring(0, first.length()-1);
            return first;
        }

        // Largura de coluna do Excel é ~256 units por caractere médio (~7 px). Aproximação:
        static int excelColumnWidthFromPixels(int px) {
            return Math.max(256, (int)Math.round(px * (256.0/7.0)));
        }

        // Altura de linha em pontos (1 px ≈ 0.75 pt)
        static float excelRowHeightPointsFromPixels(int px) {
            return (float)(px * 0.75);
        }
    }

    static class ColorUtil {
        static org.apache.poi.xssf.usermodel.XSSFColor xssfColorFromCss(String cssColor) {
            if (cssColor == null || cssColor.isBlank()) return null;
            cssColor = cssColor.trim().toLowerCase();
            try {
                if (cssColor.startsWith("#")) {
                    int rgb = Integer.parseInt(cssColor.substring(1), 16);
                    byte[] bgr = new byte[] {
                            (byte)((rgb >> 16) & 0xFF),
                            (byte)((rgb >> 8) & 0xFF),
                            (byte)(rgb & 0xFF)
                    };
                    return new org.apache.poi.xssf.usermodel.XSSFColor(bgr, null);
                } else if (cssColor.startsWith("rgb(")) {
                    String[] p = cssColor.substring(4, cssColor.length()-1).split(",");
                    byte[] bgr = new byte[] {
                            (byte)Integer.parseInt(p[0].trim()),
                            (byte)Integer.parseInt(p[1].trim()),
                            (byte)Integer.parseInt(p[2].trim())
                    };
                    return new org.apache.poi.xssf.usermodel.XSSFColor(bgr, null);
                }
            } catch (Exception ignore) {}
            return null;
        }
    }

    static class ImageUtil {
        static byte[] imageBytesFromImgSrc(File htmlFile, String src) throws IOException {
            if (src == null || src.isBlank()) return null;
            src = src.trim();
            if (src.startsWith("data:image/")) {
                // data URI base64
                int comma = src.indexOf(',');
                String base64 = (comma >= 0) ? src.substring(comma+1) : "";
                return java.util.Base64.getDecoder().decode(base64);
            } else if (src.startsWith("http://") || src.startsWith("https://")) {
                // ATENÇÃO: precisa de rede; trate timeouts, SSL, etc.
                try (InputStream is = new java.net.URL(src).openStream();
                     ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    is.transferTo(bos);
                    return bos.toByteArray();
                }
            } else {
                // caminho relativo ao arquivo HTML
                File rel = new File(htmlFile.getParentFile(), src);
                if (rel.exists()) {
                    try (InputStream is = new FileInputStream(rel);
                         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                        is.transferTo(bos);
                        return bos.toByteArray();
                    }
                }
            }
            return null;
        }

        static void insertPicture(Workbook wb, Sheet sheet, byte[] bytes, int row, int col) {
            if (bytes == null) return;
            int type = Workbook.PICTURE_TYPE_PNG; // ajuste para JPG/GIF se necessário
            int picIdx = wb.addPicture(bytes, type);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
            anchor.setRow1(row);
            anchor.setCol1(col);
            Picture pict = drawing.createPicture(anchor, picIdx);
            pict.resize(1.0); // ajusta ao tamanho da célula (aprox.)
        }
    }

    public void iniciarConversao(String arquivoXLS, String arquivoXLSX) throws Exception {
        File htmlFile = new File(arquivoXLS); // é HTML
        Document doc = Jsoup.parse(htmlFile, "UTF-8");

        try (Workbook wb = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(arquivoXLSX)) {

            Element tabela = doc.selectFirst("table");
            if (tabela == null) throw new IllegalArgumentException("Nenhuma <table> encontrada.");

            Sheet sheet = wb.createSheet("Dados");
            StyleCache styleCache = new StyleCache(wb);

            // Se desejar ler largura/altura da tabela para col/row
            // (opcional; pode também ler por <col> / estilos nas <td>)
            List<Elements> linhas = tabela.select("tr").stream().map(tr -> tr.select("th, td")).toList();

            int totalRows = linhas.size();
            // Vamos construir uma grade considerando mesclagens:
            // ocupadas[r][c] = true se posição já tomada por um rowspan/colspan anterior
            boolean[][] ocupadas = new boolean[totalRows][512]; // limite provisório de colunas

            for (int r = 0; r < totalRows; r++) {
                Row excelRow = sheet.createRow(r);
                Elements cols = linhas.get(r);
                int cCursor = 0;

                Element cellElexterno = cols.get(0);
                
                for (Element cellEl : cols) {

                    // pular colunas já ocupadas por mesclagens prévias
                    while (cCursor < ocupadas[r].length && ocupadas[r][cCursor]) cCursor++;

                    // texto
                    String text = cellEl.text();
                    // estilo inline
                    Map<String,String> css = CssUtil.parseInline(cellEl.attr("style"));
                    // escrever célula no canto superior esquerdo
                    Cell cell = excelRow.createCell(cCursor);
                    cell.setCellValue(text);
                    cell.setCellStyle(styleCache.getStyleFor(css));

                    // imagens dentro da célula
                    for (Element img : cellEl.select("img")) {
                        byte[] bytes = ImageUtil.imageBytesFromImgSrc(htmlFile, img.attr("src"));
                        ImageUtil.insertPicture(wb, sheet, bytes, r, cCursor);
                    }

                    // lidar com rowspan/colspan
                    int rs = Math.max(1, parseInt(cellEl.attr("rowspan"), 1));
                    int cs = Math.max(1, parseInt(cellEl.attr("colspan"), 1));
                    if (rs > 1 || cs > 1) {
                        int r2 = r + rs - 1;
                        int c2 = cCursor + cs - 1;
                        sheet.addMergedRegion(new CellRangeAddress(r, r2, cCursor, c2));
                        // marcar ocupação
                        for (int rr = r; rr <= r2; rr++) {
                            for (int cc = cCursor; cc <= c2; cc++) {
                                ocupadas[rr][cc] = true;
                            }
                        }
                        // canto superior esquerdo deve permanecer acessível para valor/espaço
                        ocupadas[r][cCursor] = false;
                    } else {
                        ocupadas[r][cCursor] = true;
                    }

                    cCursor += cs; // avança colunas consumidas
                }

                // Altura de linha opcional a partir de style="height:NNpx"
                String h = CssUtil.parseInline(cellElOrRowStyle(cellElexterno)).getOrDefault("height","");
                int px = parsePx(h);
                if (px > 0) excelRow.setHeightInPoints(CssUtil.excelRowHeightPointsFromPixels(px));
            }

            // Largura de coluna (opcional): você pode calcular por maior comprimento de texto da coluna
            autoFitColumns(sheet);

            wb.write(fos);
        }
    }

    static String cellElOrRowStyle(Element cellEl) {
        // tenta pegar style do <td> ou do <tr>
        String s = cellEl.attr("style");
        Element tr = cellEl.parent();
        if ((s == null || s.isBlank()) && tr != null) s = tr.attr("style");
        return s == null ? "" : s;
    }

    static int parseInt(String val, int def) {
        try { return Integer.parseInt(val); } catch (Exception e) { return def; }
    }

    static int parsePx(String cssVal) {
        if (cssVal == null) return -1;
        cssVal = cssVal.trim().toLowerCase();
        try {
            if (cssVal.endsWith("px")) return (int)Math.round(Double.parseDouble(cssVal.replace("px","")));
            return -1;
        } catch (NumberFormatException e) { return -1; }
    }

    static void autoFitColumns(Sheet sheet) {
        // autoSizeColumn funciona, mas não considera imagens; pode ser custoso:
        int lastCol = 0;
        for (Row r : sheet) if (r != null && r.getLastCellNum() > lastCol) lastCol = r.getLastCellNum();
        for (int c = 0; c < lastCol; c++) {
            sheet.autoSizeColumn(c, true);
        }
    }
}

