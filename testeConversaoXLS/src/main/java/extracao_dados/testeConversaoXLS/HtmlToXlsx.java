package extracao_dados.testeConversaoXLS;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.awt.Container;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converte um arquivo HTML (salvo erroneamente como .xls) para .xlsx,
 * preservando:
 *  - cores (fundo e fonte),
 *  - bordas (top/right/bottom/left),
 *  - alinhamento (horizontal e vertical),
 *  - negrito/itálico/tamanho,
 *  - mesclagem (colspan/rowspan),
 *  - tipos básicos (número, percentual, data dd/MM/yyyy e yyyy-MM-dd).
 *
 * Limitações: somente estilos inline; CSS externo/classe não é aplicado.
 */
public class HtmlToXlsx {

	
	String nomeDoArquivo;
	Document doc;
	Element container;
	
    public static void main(String[] args) throws Exception {
        
    	
    	Path input = Path.of("C:\\Users\\PMC514991-2\\Downloads\\relatoriosTemp_lista_leitos-13_01_2026_14-00-31.xls");     // É HTML
        Path output = Path.of("C:\\Users\\PMC514991-2\\Downloads\\convertido.xlsx");

        new HtmlToXlsx().converterArquivo(input, output);
        System.out.println("Conversão concluída: " + output);
    }

    public void converterArquivo(Path htmlFile, Path outXlsx) throws Exception {
    	nomeDoArquivo = "C:\\Users\\PMC514991-2\\Downloads\\CAPS AD SUDOESTE relatoriosTemp_lista_leitos-13_01_2026_13-28-49.xls";
    	doc = Jsoup.parse(new File(nomeDoArquivo), "ISO-8859-1");
    	container = doc.body();

    	printTree(doc, 0);

		Map<Integer, List<Element>> mapa = mapByDepth(doc.body());
		mapa.forEach((lvl, list) -> {
		    System.out.println("Nível " + lvl + " -> " + list.size() + " elementos");
		});
    	
        // Uma planilha por <table>; se quiser apenas a primeira, pegue tables.first()
    	
    	String itemDeBusca = "body > table";
    	
        Elements tables = doc.select("body > table");
        
//        ArrayList<composicaoTabela> tabelasArquivo = new ArrayList();
//        
//       tabelasArquivo.add(new composicaoTabela());
//       
//       tabelasArquivo.get(0).tabela = tables.get(0);
//       tabelasArquivo.get(0).tabelasFilhas = new ArrayList<composicaoTabela>();
//       
//       tabelasArquivo.get(0).tabelasFilhas.add(new composicaoTabela());
//       
//       tabelasArquivo.get(0).tabelasFilhas.get(0).tabela = tables.get(1);
//       
//       
//       tabelasArquivo.get(1).tabela = tables.get(6);
//       tabelasArquivo.get(1).tabelasFilhas = new ArrayList<composicaoTabela>();
        
        if (tables.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma <table> encontrada no HTML.");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            int sheetIndex = 1;
            String sheetName = "teste";
            int rowIndex = 0;
            int colIndex = 0;
            
           
            
            Referencia referencia = new Referencia(rowIndex, colIndex);
            
            XSSFSheet sheet = wb.createSheet(sheetName);
            
            for (Element table : tables) {
                     
                referencia = converterTable(wb, doc, sheet, table, referencia.linha, colIndex, itemDeBusca);
                referencia.linha++;
                
                criarNovaLinha(wb, sheet, referencia.linha);
                referencia.linha++;
                
                
                autoSizeColumnsSafe(sheet);
            }

            try (FileOutputStream fos = new FileOutputStream(outXlsx.toFile())) {
                wb.write(fos);
            }
        }
    }
    
    private Referencia converterTable(XSSFWorkbook wb, Node node, XSSFSheet sheet, Element table, int rowIndex, int colIndex, String itemDeBusca)
    {
    	Referencia referencia = buildSheetFromTable(wb, node, sheet, table, rowIndex, colIndex, itemDeBusca, true, 1, 3);
    	
    	return referencia;
    }

    // ====== Núcleo: constrói uma sheet a partir de uma <table> ======

    private void criarNovaLinha(XSSFWorkbook wb, XSSFSheet sheet, int numLinha)
    {
    	sheet.createRow(numLinha);
    }
   
    private Referencia buildSheetFromTable(XSSFWorkbook wb, Node node, XSSFSheet sheet, Element table, int rowIndex, int colIndex, String itemDeBusca, boolean criarLinha, int iteracao, int nivel) {
        // Cache de estilos para não criar estilos duplicados (limit ~64k)
    	 StyleCache styleCache = new StyleCache(wb);

        // Data formats (datas e percentuais)
        DataFormat dataFormat = wb.createDataFormat();
        short dfDateBR = dataFormat.getFormat("dd/MM/yyyy");
        short dfPercent = dataFormat.getFormat("0%");
        int finalColIndex = colIndex;

        // Rastreia rowspans remanescentes por coluna (col -> quantas linhas ainda ocupa)
        List<Integer> remainingRowSpans = new ArrayList<>();

        // Opcional: margin superior, pode haver <thead> / <tbody>
        itemDeBusca += " > tr";
        
        printIteracao(iteracao, itemDeBusca);
        
        //Elements rows = container.select(itemDeBusca);
        
        //printIteracao(iteracao, "Tr: " + rows.size());
       //if (rows.isEmpty()) rows = table.select("tr");

        int linhaFinal = rowIndex;
        int colunaFinal = colIndex;
        
        int linhaAtual = rowIndex;
        int colunaAtual = colIndex;
        
        int cont = 1;
        
        printIteracao(iteracao, "Entrando com : " + rowIndex + " " + colIndex);
        
        for (Node childTable : table.childNodes())
        {
        
        	if(childTable instanceof Element && ((Element)childTable).tagName().toLowerCase().equals("tbody"))
        	{
        		Element tbody = (Element)childTable;
        
        
		        for (Node childTR : tbody.childNodes())
		        {
		        //for (Element tr : rows) {
		        	
		        	if(childTR instanceof Element && ((Element)childTR).tagName().toLowerCase().equals("tr"))
		        	{        	
		        		Element tr = (Element)childTR;
		        		
			        	XSSFRow excelRow;
			        	
			        	if(criarLinha)
			            	excelRow = sheet.createRow(rowIndex);
			        	else
			        	{
			        		excelRow = sheet.getRow(rowIndex);
			        		criarLinha = true;
			        	}
			        		
			            int internalColIndex = colIndex;
			
			            // Pule colunas ocupadas por rowspans remanescentes
			            internalColIndex = skipOccupiedColumns(internalColIndex, remainingRowSpans);
			            
			            int firstRow = rowIndex;
			            int lastRow = rowIndex;
			            int firstCol = internalColIndex;
			            int lastCol = internalColIndex;
			
			            colunaAtual = colIndex;
			            //Elements cells = tr.select("> th, > td");
			            
			            for (Node childTD : tr.childNodes())
			            {
			            //for (Element tr : rows) {
			            	
			            	if(childTD instanceof Element && ((Element)childTD).tagName().toLowerCase().equals("td"))
			            	{ 
			            		Element cell = (Element)childTD;
				            	
				                // Avance colIndex até achar posição livre (por conta de rowSpan de linhas anteriores)
				                internalColIndex = skipOccupiedColumns(internalColIndex, remainingRowSpans);
				
				                int colspan = parseSpan(cell.attr("colspan"));
				                int rowspan = parseSpan(cell.attr("rowspan"));
				                if (colspan < 1) colspan = 1;
				                if (rowspan < 1) rowspan = 1;
				
				                // Cria célula no canto superior esquerdo do bloco
				                XSSFCell excelCell = excelRow.createCell(colunaAtual);
				
				                // Tipo e valor
				                //String rawText = cell.text();
				                
				                String rawText = "";
				                boolean tdComTable = false;
				                for (Node elementoTD : cell.childNodes())
					            {
				                	if(elementoTD instanceof TextNode)
				                	{
				                		rawText = rawText + parseTextNode(elementoTD, nivel + 1);
				                	}
				                	else if(elementoTD instanceof Element && (((Element)elementoTD).tagName().equals("b") || ((Element)elementoTD).tagName().equals("span")))
				                	{
				                			Element b = (Element)elementoTD;
				                			
				                			for(Node child : b.childNodes())
				                				rawText = rawText + " " + parseTextNode(child, nivel + 2).trim();
                	               	}
				                	else
				                	{
				                		if(elementoTD instanceof Element)
				                		{
				                			if(((Element)elementoTD).tagName().equals("table"))
				                			{
				                				Referencia ref = buildSheetFromTable(wb, node, sheet, (Element)elementoTD, linhaAtual, colunaAtual, itemDeBusca + " > table", false, iteracao++, nivel + 4);
						                		
						                		linhaFinal = Math.max(linhaFinal, ref.linha);
						                		colunaFinal = Math.max(colunaFinal, ref.coluna);
						                		
						                		linhaAtual = ref.linha;
						                		colunaAtual = ref.coluna;
						                		
						                		tdComTable = true;
				                			}
				                		}
				                	}
					            }
				                
				                
				               

				                
				                
				                printIteracao(iteracao, "Antes: " + rawText);
				                
				                //printIteracao(iteracao, "Buscar por: " + itemDeBusca);
				                
				                //Elements tabelasInternas = cell.select("table");
				                
				                
				                                
				                if(!tdComTable)
				                {
					                CellValueType type = detectType(rawText.trim());
					                
					                if(type == CellValueType.DATE)
					                	rawText = rawText.replace("-","/");
					                
				                	applyValue(wb, excelCell, rawText.trim(), type);
				                
				
					                // Estilo
					                Map<String, String> css = extractCss(cell);
					                boolean isHeader = cell.tagName().equalsIgnoreCase("th");
					                XSSFCellStyle style = styleCache.getOrCreateStyle(css, isHeader);
					
					                // Ajusta data/percentual via data format se necessário
					                if (type == CellValueType.DATE) {
					                    style = styleCache.cloneWithDataFormat(style, "dd/MM/yyyy");
					                } else if (type == CellValueType.PERCENT) {
					                    style = styleCache.cloneWithDataFormat(style, "0%");
					                }
					                excelCell.setCellStyle(style);
					
					                // Mesclagem (se colspan/rowspan > 1)
					                if (colspan > 1 || rowspan > 1) {
					                    firstRow = linhaAtual;
					                    lastRow = linhaAtual + rowspan - 1;
					                    firstCol = colunaAtual;
					                    lastCol = colunaAtual + colspan - 1;
					                    
					                    linhaAtual = linhaAtual + rowspan - 1;
					                    colunaAtual = colunaAtual + colspan - 1;
					
					                    // Cria células "fantasmas" dentro da área mesclada para evitar Nulls ao autosize (opcional)
					                    for (int r = firstRow; r <= lastRow; r++) {
					                        XSSFRow rRef = sheet.getRow(r);
					                        if (rRef == null) rRef = sheet.createRow(r);
					                        for (int c = firstCol; c <= lastCol; c++) {
					                            XSSFCell cc = rRef.getCell(c);
					                            if (cc == null) cc = rRef.createCell(c);
					                            if (r == firstRow && c == firstCol) continue; // já criada
					                            cc.setCellStyle(style);
					                        }
					                    }
					
					                    printIteracao(iteracao, "Tentativa de merge: [" + firstRow + ", " + lastRow +"] - [" + firstCol + ", " + lastCol + "]");
					                    
					                    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
					
					                    printIteracao(iteracao, "Merge: [" + firstRow + ", " + lastRow +"] - [" + firstCol + ", " + lastCol + "]");
					                    
					                    // Atualiza remainingRowSpans para colunas ocupadas
					                    ensureSize(remainingRowSpans, lastCol + 1);
					                    for (int c = firstCol; c <= lastCol; c++) {
					                        // rowspan>1 ocupa as próximas (rowspan-1) linhas
					                        remainingRowSpans.set(c, Math.max(remainingRowSpans.get(c), rowspan - 1));
					                    }
					                }
					                linhaFinal = linhaAtual;
				            		colunaFinal = colunaAtual;
				
				                }
				
				                // Avança colIndex para depois do colspan
				                colunaAtual++;
				                internalColIndex += colspan;
				            }
			            }
			
			            // Ao final da linha, decrementa os rowSpans remanescentes
			            for (int i = 0; i < remainingRowSpans.size(); i++) {
			                if (remainingRowSpans.get(i) > 0) {
			                    remainingRowSpans.set(i, remainingRowSpans.get(i) - 1);
			                }
			            }
			            finalColIndex = Math.max(finalColIndex, internalColIndex);
			
			            linhaAtual++;
			            rowIndex++;	
			        }
		        }
        	}
    	}
        
        printIteracao(iteracao, "Saindo com : " + linhaFinal + " " + colunaFinal);
        Referencia referencia = new Referencia(linhaFinal, colunaFinal);
        
        return referencia;
    }
    
    static String parseTextNode(Node node, int level)
    {
    	String texto = "";
    	
    	if(node instanceof TextNode)
    	{
    		texto = ((TextNode)node).text().trim();
    	}
    	

    	else
    	{
	        for (Node child : node.childNodes()) {
        	
	        	texto = texto + " " + parseTextNode(child, level + 1).trim();
	        }
        }
        
        return texto;
    }
    
/*    private Referencia buildSheetFromTable(XSSFWorkbook wb, Node node, XSSFSheet sheet, Element table, int rowIndex, int colIndex, String itemDeBusca, boolean criarLinha, int iteracao) {
        // Cache de estilos para não criar estilos duplicados (limit ~64k)
    	 StyleCache styleCache = new StyleCache(wb);

        // Data formats (datas e percentuais)
        DataFormat dataFormat = wb.createDataFormat();
        short dfDateBR = dataFormat.getFormat("dd/MM/yyyy");
        short dfPercent = dataFormat.getFormat("0%");
        int finalColIndex = colIndex;

        // Rastreia rowspans remanescentes por coluna (col -> quantas linhas ainda ocupa)
        List<Integer> remainingRowSpans = new ArrayList<>();

        // Opcional: margin superior, pode haver <thead> / <tbody>
        itemDeBusca += " > tr";
        
        printIteracao(iteracao, itemDeBusca);
        
        Elements rows = container.select(itemDeBusca);
        
        printIteracao(iteracao, "Tr: " + rows.size());
       //if (rows.isEmpty()) rows = table.select("tr");

        int linhaFinal = rowIndex;
        int colunaFinal = colIndex;
        
        int linhaAtual = rowIndex;
        int colunaAtual = colIndex;
        
        int cont = 1;
        
        printIteracao(iteracao, "Entrando com : " + rowIndex + " " + colIndex);
        
        for (Element tr : rows) {
        	
        	printIteracao(iteracao, "Tr: " + rows.size());
        	
        	XSSFRow excelRow;
        	
        	if(criarLinha)
            	excelRow = sheet.createRow(rowIndex);
        	else
        	{
        		excelRow = sheet.getRow(rowIndex);
        		criarLinha = true;
        	}
        		
            int internalColIndex = colIndex;

            // Pule colunas ocupadas por rowspans remanescentes
            internalColIndex = skipOccupiedColumns(internalColIndex, remainingRowSpans);
            
            int firstRow = rowIndex;
            int lastRow = rowIndex;
            int firstCol = internalColIndex;
            int lastCol = internalColIndex;

            colunaAtual = colIndex;
            Elements cells = tr.select("> th, > td");
            for (Element cell : cells) {
            	
                // Avance colIndex até achar posição livre (por conta de rowSpan de linhas anteriores)
                internalColIndex = skipOccupiedColumns(internalColIndex, remainingRowSpans);

                int colspan = parseSpan(cell.attr("colspan"));
                int rowspan = parseSpan(cell.attr("rowspan"));
                if (colspan < 1) colspan = 1;
                if (rowspan < 1) rowspan = 1;

                // Cria célula no canto superior esquerdo do bloco
                XSSFCell excelCell = excelRow.createCell(colunaAtual);

                // Tipo e valor
                String rawText = cell.text();
                CellValueType type = detectType(rawText);
                
                
                printIteracao(iteracao, "Antes: " + rawText);
                
                //printIteracao(iteracao, "Buscar por: " + itemDeBusca);
                
                Elements tabelasInternas = cell.select("table");
                                
                if(tabelasInternas.isEmpty())
                {
                	applyValue(wb, excelCell, rawText, type);
                

	                // Estilo
	                Map<String, String> css = extractCss(cell);
	                boolean isHeader = cell.tagName().equalsIgnoreCase("th");
	                XSSFCellStyle style = styleCache.getOrCreateStyle(css, isHeader);
	
	                // Ajusta data/percentual via data format se necessário
	                if (type == CellValueType.DATE) {
	                    style = styleCache.cloneWithDataFormat(style, "dd/MM/yyyy");
	                } else if (type == CellValueType.PERCENT) {
	                    style = styleCache.cloneWithDataFormat(style, "0%");
	                }
	                excelCell.setCellStyle(style);
	
	                // Mesclagem (se colspan/rowspan > 1)
	                if (colspan > 1 || rowspan > 1) {
	                    firstRow = linhaAtual;
	                    lastRow = linhaAtual + rowspan - 1;
	                    firstCol = colunaAtual;
	                    lastCol = colunaAtual + colspan - 1;
	                    
	                    linhaAtual = linhaAtual + rowspan - 1;
	                    colunaAtual = colunaAtual + colspan - 1;
	
	                    // Cria células "fantasmas" dentro da área mesclada para evitar Nulls ao autosize (opcional)
	                    for (int r = firstRow; r <= lastRow; r++) {
	                        XSSFRow rRef = sheet.getRow(r);
	                        if (rRef == null) rRef = sheet.createRow(r);
	                        for (int c = firstCol; c <= lastCol; c++) {
	                            XSSFCell cc = rRef.getCell(c);
	                            if (cc == null) cc = rRef.createCell(c);
	                            if (r == firstRow && c == firstCol) continue; // já criada
	                            cc.setCellStyle(style);
	                        }
	                    }
	
	                    printIteracao(iteracao, "Tentativa de merge: [" + firstRow + ", " + lastRow +"] - [" + firstCol + ", " + lastCol + "]");
	                    
	                    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	
	                    printIteracao(iteracao, "Merge: [" + firstRow + ", " + lastRow +"] - [" + firstCol + ", " + lastCol + "]");
	                    
	                    // Atualiza remainingRowSpans para colunas ocupadas
	                    ensureSize(remainingRowSpans, lastCol + 1);
	                    for (int c = firstCol; c <= lastCol; c++) {
	                        // rowspan>1 ocupa as próximas (rowspan-1) linhas
	                        remainingRowSpans.set(c, Math.max(remainingRowSpans.get(c), rowspan - 1));
	                    }
	                }
	                linhaFinal = linhaAtual;
            		colunaFinal = colunaAtual;

                }else
                	for(Element tabelaInterna : tabelasInternas)
                	{
                		applyValue(wb, excelCell, "", CellValueType.STRING);
                		Referencia ref = buildSheetFromTable(wb, node, sheet, tabelaInterna, linhaAtual, colunaAtual, itemDeBusca + " > table", false, iteracao++);
                		
                		linhaFinal = Math.max(linhaFinal, ref.linha);
                		colunaFinal = Math.max(colunaFinal, ref.coluna);
                		
                		linhaAtual = ref.linha;
                		colunaAtual = ref.coluna;
                	}

                // Avança colIndex para depois do colspan
                colunaAtual++;
                internalColIndex += colspan;
            }

            // Ao final da linha, decrementa os rowSpans remanescentes
            for (int i = 0; i < remainingRowSpans.size(); i++) {
                if (remainingRowSpans.get(i) > 0) {
                    remainingRowSpans.set(i, remainingRowSpans.get(i) - 1);
                }
            }
            finalColIndex = Math.max(finalColIndex, internalColIndex);

            linhaAtual++;
            rowIndex++;
        }
        
        
        printIteracao(iteracao, "Saindo com : " + linhaFinal + " " + colunaFinal);
        Referencia referencia = new Referencia(linhaFinal, colunaFinal);
        
        return referencia;
    }*/
    
    private void printIteracao(int iteracao, String texto) 
    {
    	for(int i = 0; i < iteracao; i++)
    		System.out.print("     ");
    	System.out.println(texto);
    }

    // Pula colunas ocupadas por rowspan de linhas anteriores
    private int skipOccupiedColumns(int colIndex, List<Integer> remainingRowSpans) {
        while (colIndex < remainingRowSpans.size() && remainingRowSpans.get(colIndex) != null && remainingRowSpans.get(colIndex) > 0) {
            colIndex++;
        }
        return colIndex;
    }

    private int parseSpan(String span) {
        if (span == null || span.isBlank()) return 1;
        try {
            return Math.max(1, Integer.parseInt(span.trim()));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void ensureSize(List<Integer> list, int size) {
        while (list.size() < size) list.add(0);
    }

    // ====== Tipos e valores ======

    enum CellValueType { STRING, NUMBER, PERCENT, DATE }

    private CellValueType detectType(String text) {
        if (text == null) return CellValueType.STRING;
        String s = text.trim();

        // Percentual: "10%" / "10,5%" / "10.5%"
        if (s.matches("^-?\\d{1,3}([\\.,]\\d+)?%$")) {
            return CellValueType.PERCENT;
        }

        // Número (com , ou . como decimal) e opcional separador de milhar
        if (s.matches("^-?\\d{1,3}(\\.\\d{3})*(,\\d+)?$") || s.matches("^-?\\d+(\\.\\d+)?$")) {
            return CellValueType.NUMBER;
        }

        // Data comum BR/ISO
        if (s.matches("^\\d{2}/\\d{2}/\\d{4}$") || s.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            return CellValueType.DATE;
        }
        
     // Data comum BR/ISO
        if (s.matches("^\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}$") || s.matches("^\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}$")) {
            return CellValueType.DATE;
        }

        return CellValueType.STRING;
    }

    private void applyValue(XSSFWorkbook wb, XSSFCell cell, String raw, CellValueType type) {
    	
    	System.out.println("Valor inserido: " + raw);
    	//cell.setCellValue(raw);
    	
        switch (type) {
            case NUMBER: {
            	System.out.println("Number: " + raw);
            	
                double val = parseDoubleFlexible(raw);
                cell.setCellValue(val);
            }
            case PERCENT: {
            	System.out.println("Percent: " + raw);
            	
                String s = raw.trim().replace("%", "").trim();
                double val = parseDoubleFlexible(s) / 100.0;
                cell.setCellValue(val);
            }
            case DATE: {
            	raw = raw.replace('-', '/');
            	
            	System.out.println("Data: " + raw);
            	
            	CellStyle dateStyle = wb.createCellStyle();
                CreationHelper creationHelper = wb.getCreationHelper();
                short dateFormat = creationHelper.createDataFormat().getFormat("dd/MM/yyyy");
                dateStyle.setDataFormat(dateFormat);

                // Aplica estilo à célula
                cell.setCellStyle(dateStyle);
            	
                LocalDate ld = parseLocalDateFlexible(raw.trim());
                if (ld != null) {
                    cell.setCellValue(java.sql.Date.valueOf(ld));

                } else {
                    cell.setCellValue(raw);
                }
                
            }
            default: cell.setCellValue(raw);
        }
    }

    private double parseDoubleFlexible(String s) {
        // Tenta normalizar pt-BR (milhares com ponto, decimal com vírgula)
        String norm = s.trim()
                .replaceAll("\\.", "") // remove separador de milhar
                .replace(",", ".");    // vírgula decimal -> ponto
        try {
            return Double.parseDouble(norm);
        } catch (NumberFormatException e) {
            // fallback
            try {
                return Double.parseDouble(s.trim());
            } catch (NumberFormatException ex) {
                return Double.NaN;
            }
        }
    }

    private LocalDate parseLocalDateFlexible(String s) {
        List<DateTimeFormatter> fmts = List.of(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd
        );
        for (DateTimeFormatter f : fmts) {
            try {
                return LocalDate.parse(s, f);
            } catch (Exception ignored) { }
        }
        return null;
    }

    // ====== Extração e mapeamento de CSS/atributos ======

    private Map<String, String> extractCss(Element cell) {
        Map<String, String> css = new LinkedHashMap<>();

        // Atributos HTML "legados"
        if (cell.hasAttr("align")) css.putIfAbsent("text-align", cell.attr("align")); // left|center|right
        if (cell.hasAttr("valign")) css.putIfAbsent("vertical-align", cell.attr("valign")); // top|middle|bottom
        if (cell.hasAttr("bgcolor")) css.putIfAbsent("background-color", cell.attr("bgcolor"));

        // Inline style
        String styleAttr = cell.attr("style");
        css.putAll(parseInlineStyle(styleAttr));

        // Tag <th> geralmente em negrito
        if (cell.tagName().equalsIgnoreCase("th")) {
            css.putIfAbsent("font-weight", "bold");
            css.putIfAbsent("text-align", "center");
            css.putIfAbsent("vertical-align", "middle");
        }

        return css;
    }

    private Map<String, String> parseInlineStyle(String style) {
        Map<String, String> map = new LinkedHashMap<>();
        if (style == null || style.isBlank()) return map;

        // Divide por ';' e depois por ':'
        String[] parts = style.split(";");
        for (String p : parts) {
            String[] kv = p.split(":", 2);
            if (kv.length == 2) {
                String k = kv[0].trim().toLowerCase(Locale.ROOT);
                String v = kv[1].trim();
                if (!k.isEmpty() && !v.isEmpty()) {
                    map.put(k, v);
                }
            }
        }

        // Tratar abreviações
        if (map.containsKey("border")) {
            // border: 1px solid #000
            String v = map.get("border");
            map.putIfAbsent("border-top", v);
            map.putIfAbsent("border-right", v);
            map.putIfAbsent("border-bottom", v);
            map.putIfAbsent("border-left", v);
        }

        if (map.containsKey("background")) {
            // background pode carregar a cor
            String v = map.get("background");
            if (!map.containsKey("background-color")) {
                String color = extractColorFromBackground(v);
                if (color != null) map.put("background-color", color);
            }
        }

        return map;
    }

    private String extractColorFromBackground(String background) {
        // Encontra #RRGGBB ou rgb(...)
        Matcher m1 = Pattern.compile("#[0-9a-fA-F]{6}").matcher(background);
        if (m1.find()) return m1.group();
        Matcher m2 = Pattern.compile("rgb\\s*\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)").matcher(background);
        if (m2.find()) return "rgb(" + m2.group(1) + "," + m2.group(2) + "," + m2.group(3) + ")";
        return null;
    }

    // ====== Ajuste de largura de colunas ======

    private void autoSizeColumnsSafe(XSSFSheet sheet) {
        int maxCols = 0;
        for (int r = 0; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                maxCols = Math.max(maxCols, row.getLastCellNum());
            }
        }
        for (int c = 0; c < maxCols; c++) {
            try {
                sheet.autoSizeColumn(c);
            } catch (Exception ignored) {}
        }
    }

    // ====== Cache de estilos e mapeamento CSS -> XSSFCellStyle ======

    static class StyleCache {
        private final XSSFWorkbook wb;
        private final Map<String, XSSFCellStyle> cache = new HashMap<>();
        private final Map<String, Short> dataFormatCache = new HashMap<>();

        StyleCache(XSSFWorkbook wb) { this.wb = wb; }

        XSSFCellStyle getOrCreateStyle(Map<String, String> css, boolean header) {
            String key = canonicalKey(css, header, null);
            return cache.computeIfAbsent(key, k -> createStyle(css, header, null));
        }

        XSSFCellStyle cloneWithDataFormat(XSSFCellStyle base, String fmt) {
            String key = canonicalKey(Collections.emptyMap(), false, fmt) + "||BASE:" + base.hashCode();
            XSSFCellStyle cloned = cache.get(key);
            if (cloned != null) return cloned;

            cloned = wb.createCellStyle();
            cloned.cloneStyleFrom(base);

            short fmtIdx = dataFormatCache.computeIfAbsent(fmt, f -> wb.createDataFormat().getFormat(f));
            cloned.setDataFormat(fmtIdx);
            cache.put(key, cloned);
            return cloned;
        }

        private XSSFCellStyle createStyle(Map<String, String> css, boolean header, String dataFmt) {
            XSSFCellStyle style = wb.createCellStyle();
            XSSFFont font = (XSSFFont) wb.createFont();

            // --- Alinhamento horizontal ---
            String ta = val(css, "text-align");
            if (ta != null) {
            	
            	HorizontalAlignment alinhamentoHorizonal = null;
            	switch (ta.toLowerCase(Locale.ROOT)) 
                {
                    case "centre": alinhamentoHorizonal = HorizontalAlignment.CENTER;
                    case "center": alinhamentoHorizonal = HorizontalAlignment.CENTER;
                    case "right": alinhamentoHorizonal = HorizontalAlignment.RIGHT;
                    case "justify": alinhamentoHorizonal = HorizontalAlignment.JUSTIFY;
                    default: alinhamentoHorizonal = HorizontalAlignment.LEFT;
                }
            	
                style.setAlignment(alinhamentoHorizonal);
            }

            // --- Alinhamento vertical ---
            String va = val(css, "vertical-align");
            if (va != null) {
            	
            	VerticalAlignment alinhamentoVertical = null;
            	switch (va.toLowerCase(Locale.ROOT)) 
            	{
	                case "top": alinhamentoVertical = VerticalAlignment.TOP;
	                case "middle": alinhamentoVertical = VerticalAlignment.CENTER;
	                case "center": alinhamentoVertical = VerticalAlignment.CENTER;
	                case "centre": alinhamentoVertical = VerticalAlignment.CENTER;
	                case "bottom": alinhamentoVertical = VerticalAlignment.BOTTOM;
	                default: alinhamentoVertical = VerticalAlignment.CENTER;
            	}
            	
                style.setVerticalAlignment(alinhamentoVertical);
            } else {
                style.setVerticalAlignment(VerticalAlignment.CENTER);
            }

            // --- Negrito / Itálico ---
            String fw = val(css, "font-weight");
            if (fw != null && (fw.equalsIgnoreCase("bold") || fw.equals("700"))) font.setBold(true);
            String fs = val(css, "font-style");
            if ("italic".equalsIgnoreCase(fs)) font.setItalic(true);

            // --- Tamanho da fonte ---
            String fz = val(css, "font-size");
            if (fz != null) {
                short pts = parseFontPoints(fz);
                if (pts > 0) font.setFontHeightInPoints(pts);
            }

            // --- Cor da fonte ---
            String color = coalesce(val(css, "color"), val(css, "font-color"));
            if (color != null) {
                XSSFColor xcolor = parseCssColor(color);
                if (xcolor != null) font.setColor(xcolor);
            }

            // --- Fundo ---
            String bg = val(css, "background-color");
            if (bg != null) {
                XSSFColor bgc = parseCssColor(bg);
                if (bgc != null) {
                    style.setFillForegroundColor(bgc);
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }
            }

            // --- Bordas (top/right/bottom/left) ---
            applyBorderFromCss(style, css, "top");
            applyBorderFromCss(style, css, "right");
            applyBorderFromCss(style, css, "bottom");
            applyBorderFromCss(style, css, "left");

            // Cabeçalho: se marcado e não tiver override no CSS, força bold e centralizado
            if (header) {
                if (fw == null) font.setBold(true);
                if (ta == null) style.setAlignment(HorizontalAlignment.CENTER);
            }

            style.setWrapText(true); // quebra de linha automática se necessário
            style.setFont(font);

            // Data format opcional
            if (dataFmt != null) {
                short fmtIdx = dataFormatCache.computeIfAbsent(dataFmt, f -> wb.createDataFormat().getFormat(f));
                style.setDataFormat(fmtIdx);
            }

            return style;
        }

        private void applyBorderFromCss(XSSFCellStyle style, Map<String, String> css, String side) {
            String prop = "border-" + side;
            String v = val(css, prop);
            if (v == null) return;

            // Formato esperado: "<width> <style> <color>" (ex.: "1px solid #000")
            BorderStyle bs = BorderStyle.THIN;
            XSSFColor color = null;

            // Largura -> estilo
            Matcher mWidth = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*px").matcher(v);
            if (mWidth.find()) {
                double px = Double.parseDouble(mWidth.group(1));
                bs = widthToBorderStyle(px);
            }

            if (v.contains("none")) {
                bs = BorderStyle.NONE;
            }

            // Cor (#RRGGBB ou rgb())
            String c = findColorToken(v);
            if (c != null) color = parseCssColor(c);

            switch (side) {
                case "top": {
                    style.setBorderTop(bs);
                    if (color != null) style.setTopBorderColor(color);
                }
                case "right": {
                    style.setBorderRight(bs);
                    if (color != null) style.setRightBorderColor(color);
                }
                case "bottom": {
                    style.setBorderBottom(bs);
                    if (color != null) style.setBottomBorderColor(color);
                }
                case "left": {
                    style.setBorderLeft(bs);
                    if (color != null) style.setLeftBorderColor(color);
                }
            }
        }

        private String canonicalKey(Map<String, String> css, boolean header, String dataFmt) {
            // Normaliza para chave única de cache (ordem consistente)
            List<String> keys = new ArrayList<>(css.keySet());
            Collections.sort(keys);
            StringBuilder sb = new StringBuilder();
            for (String k : keys) {
                sb.append(k.toLowerCase(Locale.ROOT)).append('=').append(css.get(k).trim()).append(';');
            }
            sb.append("|header=").append(header);
            if (dataFmt != null) sb.append("|fmt=").append(dataFmt);
            return sb.toString();
        }

        private String val(Map<String, String> css, String key) {
            // case-insensitive
            for (Map.Entry<String, String> e : css.entrySet()) {
                if (e.getKey().equalsIgnoreCase(key)) return e.getValue();
            }
            return null;
        }

        private String coalesce(String a, String b) { return a != null ? a : b; }

        private String findColorToken(String s) {
            Matcher m1 = Pattern.compile("#[0-9a-fA-F]{6}").matcher(s);
            if (m1.find()) return m1.group();
            Matcher m2 = Pattern.compile("rgb\\s*\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)").matcher(s);
            if (m2.find()) return "rgb(" + m2.group(1) + "," + m2.group(2) + "," + m2.group(3) + ")";
            // nomes básicos (ex.: black, red, blue) — suporte simples
            Matcher m3 = Pattern.compile("\\b(black|white|red|green|blue|gray|grey|yellow|orange|purple|magenta|cyan)\\b", Pattern.CASE_INSENSITIVE).matcher(s);
            if (m3.find()) return m3.group(1);
            return null;
        }

        private BorderStyle widthToBorderStyle(double px) {
            if (px <= 0.5) return BorderStyle.HAIR;
            if (px <= 1.0) return BorderStyle.THIN;
            if (px <= 2.0) return BorderStyle.MEDIUM;
            if (px <= 3.5) return BorderStyle.THICK;
            return BorderStyle.MEDIUM; // fallback
        }

        private short parseFontPoints(String cssSize) {
            String v = cssSize.trim().toLowerCase(Locale.ROOT);
            try {
                if (v.endsWith("pt")) {
                    return (short) Math.round(Double.parseDouble(v.replace("pt", "").trim()));
                } else if (v.endsWith("px")) {
                    double px = Double.parseDouble(v.replace("px", "").trim());
                    // Conversão aproximada: 1px ≈ 0.75pt (96dpi)
                    return (short) Math.round(px * 0.75);
                } else {
                    // número puro = pt
                    return (short) Math.round(Double.parseDouble(v));
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        private XSSFColor parseCssColor(String cssColor) {
            if (cssColor == null || cssColor.isBlank()) return null;
            String v = cssColor.trim().toLowerCase(Locale.ROOT);

            if (v.startsWith("#") && v.length() == 7) {
                int r = Integer.parseInt(v.substring(1, 3), 16);
                int g = Integer.parseInt(v.substring(3, 5), 16);
                int b = Integer.parseInt(v.substring(5, 7), 16);
                return new XSSFColor(new java.awt.Color(r, g, b), null);
            }
            Matcher m = Pattern.compile("rgb\\s*\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)").matcher(v);
            if (m.find()) {
                int r = clamp(Integer.parseInt(m.group(1)));
                int g = clamp(Integer.parseInt(m.group(2)));
                int b = clamp(Integer.parseInt(m.group(3)));
                return new XSSFColor(new java.awt.Color(r, g, b), null);
            }
            // nomes simples
            Map<String, java.awt.Color> named = Map.of(
                    "black", java.awt.Color.BLACK,
                    "white", java.awt.Color.WHITE,
                    "red", java.awt.Color.RED,
                    "green", new java.awt.Color(0, 128, 0),
                    "blue", java.awt.Color.BLUE,
                    "gray", java.awt.Color.GRAY,
                    "grey", java.awt.Color.GRAY,
                    "yellow", java.awt.Color.YELLOW,
                    "orange", java.awt.Color.ORANGE,
                    "cyan", java.awt.Color.CYAN
            );
            java.awt.Color c = named.get(v);
            return (c != null) ? new XSSFColor(c, null) : null;
        }

        private int clamp(int x) { return Math.max(0, Math.min(255, x)); }
    }
    
    static class Referencia
    {
    	public Referencia(int linha, int coluna)
    	{
    		this.linha = linha;
    		this.coluna = coluna;
    	}
    	
    	public int linha;
    	public int coluna;
    }
    

    public static Map<Integer, List<Element>> mapByDepth(Element root) {
	    Map<Integer, List<Element>> byDepth = new LinkedHashMap<>();
	    final int[] rootDepth = {-1};
	
	    NodeTraversor.traverse(new NodeVisitor() {
	        @Override
	        public void head(Node node, int depth) {
	            if (rootDepth[0] == -1) rootDepth[0] = depth;
	            int rel = depth - rootDepth[0];
	            if (node instanceof Element el) {
	                byDepth.computeIfAbsent(rel, k -> new ArrayList<>()).add(el);
	            }
	        }
	        @Override public void tail(Node node, int depth) {}
	    }, root);
	
	    return byDepth;
    }
    

    static void printTree(Node node, int level) {
        String indent = "  ".repeat(level);
        String info = switch (node) {
            case Element e -> "<" + e.tagName() + (e.id().isEmpty() ? "" : " id=" + e.id()) + ">";
            case TextNode t -> "TextNode(\"" + t.text().trim() + "\")";
            case Comment c -> "Comment(" + c.getString() + ")";
            default -> node.nodeName();
        };
        System.out.println(indent + info);

        for (Node child : node.childNodes()) {
            printTree(child, level + 1);
        }
    }

    

}
