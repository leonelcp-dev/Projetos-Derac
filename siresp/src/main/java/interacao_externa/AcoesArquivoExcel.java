package interacao_externa;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.DateFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition;

import modelosDados.CelulaExcel;


public class AcoesArquivoExcel {

	private String nomeDoAquivo;
	private Workbook arquivoXLSX;
	private Workbook arquivoXLS;
	private Sheet planilhaAtiva;
	private int ultimaLinhaPreenchida;
	private FileInputStream arquivoLeitura;
	//private ArrayList<CellStyle> estilosDasColunas;
	private Map<Integer, CellStyle> estilosDasColunas;
	
	public AcoesArquivoExcel(String nomeDoArquivo, int linhaBaseFormatacao)
	{
		this.setNomeDoAquivo(nomeDoArquivo);
		System.out.println(nomeDoArquivo);
		
		try
		{
			if(nomeDoArquivo.toLowerCase().endsWith("xls"))
			{
				arquivoLeitura = new FileInputStream(nomeDoArquivo);
	            arquivoXLS = new HSSFWorkbook(arquivoLeitura); 
	            abrirPlanilha(0, linhaBaseFormatacao);
			}
			else
			{
				ZipSecureFile.setMinInflateRatio(0.0);
				arquivoLeitura = new FileInputStream(nomeDoArquivo);
	            arquivoXLSX = new XSSFWorkbook(arquivoLeitura); 
	            abrirPlanilha(0, linhaBaseFormatacao);
			}
				
        } catch (Exception e) {
        	arquivoXLSX = null;
            e.printStackTrace();
        }

	}
	
	public String getNomePlanilhaDaPosicao(int posicao)
	{
		Sheet planilha = arquivoXLSX.getSheetAt(0);
		
		return planilha.getSheetName();
	}
	
	public void converterXLS_to_XLSX(String caminhoXlsx) {
		
		if(this.arquivoXLS == null)
		{
			try
			{

				InputStream fis = new FileInputStream(nomeDoAquivo);
				InputStream in = FileMagic.prepareToCheckMagic(fis);

				FileMagic fm = FileMagic.valueOf(in);
			    switch (fm) {
			        case OLE2:
			        	{
			        		System.out.println("É .xls (OLE2/HSSF)");
			        		converterXLS(caminhoXlsx);
			        	}
			        case OOXML: 
			        	{
			        		System.out.println("É .xlsx (OOXML/XSSF)");
			        	}
			        default: 
			        	{
			        		converterHTML(caminhoXlsx);
			        		
//			        		ConversaoHMTL_XLSX conversor = new ConversaoHMTL_XLSX();
//			        		
//			        		try
//			        		{
//			        			conversor.iniciarConversao(nomeDoAquivo, caminhoXlsx);
//			        		}catch(Exception e) {}
			        		System.out.println("Não é Excel (pode ser HTML/CSV/etc).");
			        	}
			    }
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			converterXLS(caminhoXlsx);
		}
	}
	
    private void converterXLS(String caminhoXlsx) {
        try (              // abre .xls
             Workbook wbDestino = new XSSFWorkbook();                // prepara .xlsx
             OutputStream out = new FileOutputStream(caminhoXlsx)) {

            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evalOrigem = arquivoXLS.getCreationHelper().createFormulaEvaluator();
            FormulaEvaluator evalDestino = wbDestino.getCreationHelper().createFormulaEvaluator();

            for (int s = 0; s < arquivoXLS.getNumberOfSheets(); s++) {
                Sheet sheetOrigem = arquivoXLS.getSheetAt(s);
                Sheet sheetDestino = wbDestino.createSheet(sheetOrigem.getSheetName());

                // Copiar regiões mescladas
                for (int i = 0; i < sheetOrigem.getNumMergedRegions(); i++) {
                    CellRangeAddress region = sheetOrigem.getMergedRegion(i);
                    sheetDestino.addMergedRegion(region);
                }

                // Determinar última coluna para copiar larguras
                int ultimaCol = 0;
                for (Row r : sheetOrigem) {
                    if (r != null && r.getLastCellNum() > ultimaCol) {
                        ultimaCol = r.getLastCellNum();
                    }
                }
                for (int c = 0; c < ultimaCol; c++) {
                    sheetDestino.setColumnWidth(c, sheetOrigem.getColumnWidth(c));
                }

                // Copiar linhas e células
                for (int rIdx = sheetOrigem.getFirstRowNum(); rIdx <= sheetOrigem.getLastRowNum(); rIdx++) {
                    Row rowOrigem = sheetOrigem.getRow(rIdx);
                    if (rowOrigem == null) continue;

                    Row rowDestino = sheetDestino.createRow(rIdx);
                    rowDestino.setHeight(rowOrigem.getHeight());

                    short firstCell = rowOrigem.getFirstCellNum();
                    short lastCell = rowOrigem.getLastCellNum();
                    if (firstCell < 0 || lastCell < 0) continue;

                    for (int cIdx = firstCell; cIdx < lastCell; cIdx++) {
                        Cell cellOrigem = rowOrigem.getCell(cIdx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        Cell cellDestino = rowDestino.createCell(cIdx);
                        if (cellOrigem == null) continue;

                        // Clonar estilo básico
                        CellStyle estilo = wbDestino.createCellStyle();
                        estilo.cloneStyleFrom(cellOrigem.getCellStyle());
                        cellDestino.setCellStyle(estilo);

                        // Copiar valor conforme tipo
                        switch (cellOrigem.getCellType()) {
                            case STRING: cellDestino.setCellValue(cellOrigem.getRichStringCellValue());
                            case NUMERIC: {
                                if (DateUtil.isCellDateFormatted(cellOrigem)) {
                                    cellDestino.setCellValue(cellOrigem.getDateCellValue());
                                } else {
                                    cellDestino.setCellValue(cellOrigem.getNumericCellValue());
                                }
                            }
                            case BOOLEAN: cellDestino.setCellValue(cellOrigem.getBooleanCellValue());
                            case FORMULA: {
                                // Preservar fórmula
                                cellDestino.setCellFormula(cellOrigem.getCellFormula());
                                // Opcional: avaliar no destino
                                try { evalDestino.evaluateFormulaCell(cellDestino); } catch (Exception ignored) {}
                            }
                            case BLANK: cellDestino.setBlank();
                            default: {
                                // Fallback: valor como texto formatado
                                String texto = formatter.formatCellValue(cellOrigem, evalOrigem);
                                cellDestino.setCellValue(texto);
                            }
                        }
                    }
                }
            }

            wbDestino.write(out);
        }catch(IOException e)
        {
        	e.printStackTrace();
        }
    }
    

    private void converterHTML(String caminhoXLSX) 
    {

    	try
    	{
	        Document doc = Jsoup.parse(new File(nomeDoAquivo), "UTF-8");
	        Elements rows = doc.select("table tr");
	
	        try (Workbook wb = new XSSFWorkbook();
	             FileOutputStream fos = new FileOutputStream(caminhoXLSX)) {
	            Sheet sheet = wb.createSheet("Dados");
	            int r = 0;
	            for (var row : rows) {
	                Row excelRow = sheet.createRow(r++);
	                var cells = row.select("th, td");
	                for (int c = 0; c < cells.size(); c++) {
	                    excelRow.createCell(c).setCellValue(cells.get(c).text());
	                }
	            }
	            wb.write(fos);
	        }catch(IOException e)
	        {
	        	e.printStackTrace();
	        }
	           
        }catch(IOException e)
        {
        	e.printStackTrace();
        }
    }

	
	public boolean isAberto()
	{
		if(arquivoXLSX == null)
			return false;
		else
			return true;
	}
	
	public boolean fecharArquivo()
	{
		try
		{
			arquivoLeitura.close();
		}catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	

	public void gravarDadosEmCelula(int planilha, ArrayList<CelulaExcel> celulas) 
	{
        Path caminho = Paths.get(nomeDoAquivo); // ajuste o caminho

        // Abrir, alterar e salvar (try-with-resources fecha tudo corretamente)
        try {

        	for(CelulaExcel celula : celulas)
        		setCellValue(planilhaAtiva, celula.getLinha(), celula.getColuna(), celula.getValor());   // B3: linha 1, coluna 2

            // 4) Salvar (sobrescrevendo o mesmo arquivo)
            try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
            	arquivoXLSX.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void gravarDadosEmCelula(String planilha, ArrayList<CelulaExcel> celulas) 
	{
        Path caminho = Paths.get(nomeDoAquivo); // ajuste o caminho

        // Abrir, alterar e salvar (try-with-resources fecha tudo corretamente)
        try {

        	for(CelulaExcel celula : celulas)
        		setCellValue(planilhaAtiva, celula.getLinha(), celula.getColuna(), celula.getValor());   // B3: linha 1, coluna 2

            // 4) Salvar (sobrescrevendo o mesmo arquivo)
            try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
            	arquivoXLSX.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void gravarDadosEmCelula(String nomePlanilha, ArrayList<CelulaExcel> celulas, boolean copiarFormato, boolean copiarFormulas, int linhaInicial, ArrayList<Integer> celulasComFormulas) 
	{
        Path caminho = Paths.get(nomeDoAquivo); // ajuste o caminho
        abrirPlanilha(nomePlanilha, linhaInicial);

        int linhaCelulaAnterior = linhaInicial;
        
        ArrayList<Integer> linhasFormatadas = new ArrayList<Integer>();
        
        estilosDasColunas = new HashMap<Integer, CellStyle>();
        
        // Abrir, alterar e salvar (try-with-resources fecha tudo corretamente)
        try {

        	for(CelulaExcel celula : celulas)
        	{
        		if(linhaCelulaAnterior != celula.getLinha())
        		{
        			
        			
        			if(!linhasFormatadas.contains(celula.getLinha()))
        			{
        				//System.out.println("De: " + linhaInicial + "Para: " + celula.getLinha());
            			//System.out.println("------------");
	        			if(copiarFormato)
	        				copiarFormato(linhaInicial, celula.getLinha());
	        			if(copiarFormulas)
	        				copiarFormulas(linhaInicial, celula.getLinha(), celulasComFormulas);
	        			linhasFormatadas.add(celula.getLinha());
        			}
        		}
        		
        		//System.out.println("Nome da planilha: " + nomePlanilha + ", Linha: " + celula.getLinha() + ", Coluna: " + celula.getColuna() + ", Valor: " + celula.getValor() + ", Tipo: " + celula.getTipo());
        		setCellValue(planilhaAtiva, celula.getLinha(), celula.getColuna(), celula.getValor());   // B3: linha 1, coluna 2
                setFormat(planilhaAtiva, celula.getLinha(), celula.getColuna(), celula.getValor(), celula.getTipo(), copiarFormato);
                
                linhaCelulaAnterior = celula.getLinha();
        	}

            // 4) Salvar (sobrescrevendo o mesmo arquivo)
            try (FileOutputStream fos = new FileOutputStream(caminho.toFile())) {
                arquivoXLSX.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Helpers ---

    // Cria linha/célula se não existir e retorna a célula
    private static Cell ensureCell(Sheet sheet, int rowIdx, int colIdx) {
        Row row = sheet.getRow(rowIdx);
        if (row == null) row = sheet.createRow(rowIdx);
        return row.getCell(colIdx, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    // Define valor de forma segura (String, Number, Boolean, Date)
    private static void setCellValue(Sheet sheet, int rowIdx, int colIdx, Object value) {
        Cell cell = ensureCell(sheet, rowIdx, colIdx);
        if (value == null) {
            cell.setBlank();
            return;
        }
        
        if (value instanceof String s) cell.setCellValue(s);
        else if (value instanceof Number n) cell.setCellValue(n.doubleValue());
        else if (value instanceof Boolean b) cell.setCellValue(b);
        else if (value instanceof LocalDate d) cell.setCellValue(d);
        else if (value instanceof LocalDateTime d) cell.setCellValue(d);
        else cell.setCellValue(String.valueOf(value)); // fallback como texto
    }
    
    private void setFormat(Sheet sheet, int rowIdx, int colIdx, Object value, String tipo, boolean copiarFormato) {
        Cell cell = ensureCell(sheet, rowIdx, colIdx);
        if (value == null) {
            cell.setBlank();
            return;
        }
        
        if (tipo.equals("Date"))
        {
        	if(copiarFormato)
        	{
        		CellStyle dateStyle;
        		if(estilosDasColunas.containsKey(colIdx))
        			dateStyle = estilosDasColunas.get(colIdx);
        		else
        		{
        			dateStyle = arquivoXLSX.createCellStyle();
        			dateStyle.cloneStyleFrom(cell.getCellStyle());
        			
        			estilosDasColunas.put(colIdx, dateStyle);
        		}
        		DataFormat df = arquivoXLSX.createDataFormat();
        		dateStyle.setDataFormat(df.getFormat("dd/MM/yyyy"));
        		cell.setCellStyle(dateStyle);
        	}
        	else
        	{
        		DataFormat df = arquivoXLSX.createDataFormat();
        		CellStyle dateStyle = cell.getCellStyle();
        		dateStyle.setDataFormat(df.getFormat("dd/MM/yyyy"));
        		cell.setCellStyle(dateStyle);
        	}
        }
        else if(tipo.equals("Date mes/ano"))
        {
        	DataFormat df = arquivoXLSX.createDataFormat();
        	CellStyle dateStyle = cell.getCellStyle();
			dateStyle.setDataFormat(df.getFormat("[$-pt-BR]mmm/yyyy"));
			cell.setCellStyle(dateStyle);
        }
        else if (tipo.equals("DateTime"))
        {
        	DataFormat df = arquivoXLSX.createDataFormat();
			CellStyle dateStyle = cell.getCellStyle();
			dateStyle.setDataFormat(df.getFormat("dd/MM/yyyy HH:mm"));
			cell.setCellStyle(dateStyle);
        }
        else if (tipo.equals("Time"))
        {
        	DataFormat df = arquivoXLSX.createDataFormat();
			CellStyle dateStyle = cell.getCellStyle();
			dateStyle.setDataFormat(df.getFormat("HH:mm"));
			cell.setCellStyle(dateStyle);
        }
        else if(tipo.equals("String"))
        {
        	CellStyle general = cell.getCellStyle();
			general.setDataFormat((short) 0); // General
			cell.setCellStyle(general);
        }
        else if(tipo.equals("Integer"))
        {
        	CellStyle general = cell.getCellStyle();
			general.setDataFormat((short) 0); // General
			cell.setCellStyle(general);
        }
        else if(tipo.equals("Porcentagem"))
        {
        	DataFormat df = arquivoXLSX.createDataFormat();
        	CellStyle general = cell.getCellStyle();
			general.setDataFormat(df.getFormat("0.00%")); // General
			cell.setCellStyle(general);
        }
    }

    private static void autoSizeAllColumns(Sheet sheet) {
        int lastCol = 0;
        for (Row r : sheet) if (r != null && r.getLastCellNum() > lastCol) lastCol = r.getLastCellNum();
        for (int c = 0; c < lastCol; c++) sheet.autoSizeColumn(c, true);
    }

	
	public void abrirPlanilha(int planilha, int linhaBaseFormatacao)
	{
		planilhaAtiva = arquivoXLSX.getSheetAt(planilha);
		
		ultimaLinhaPreenchida = planilhaAtiva.getLastRowNum();
	}
	
	public void abrirPlanilha(String nome, int linhaBaseFormatacao)
	{
		System.out.println(nome);
		planilhaAtiva = arquivoXLSX.getSheet(nome);
		
		ultimaLinhaPreenchida = planilhaAtiva.getLastRowNum();
	}

	public String getNomeDoAquivo() {
		return nomeDoAquivo;
	}

	public void setNomeDoAquivo(String nomeDoAquivo) {
		this.nomeDoAquivo = nomeDoAquivo;
	}

	public int getUltimaLinhaPreenchida() {
		return ultimaLinhaPreenchida;
	}
	
	public int getUlimtaLinhaPreenchidaEmUmaColuna(int primeiraLinha, int coluna) {
		int contadorLinha = primeiraLinha;
		boolean celulaVazia = false;
		
		Row linha = planilhaAtiva.getRow(contadorLinha);
		Cell celula = linha.getCell(coluna);
		
		try
		{
			String valor = celula.getStringCellValue();
			
			if(valor.trim().equals(""))
				celulaVazia = true;
				
		}catch(Exception e)
		{
			
		}
		
		while(celula != null && !celulaVazia)
		{
			contadorLinha++;
			
			linha = planilhaAtiva.getRow(contadorLinha);
			
			if(linha != null)
			{
				celula = linha.getCell(coluna);
				
				try
				{
					String valor = celula.getStringCellValue();
					
					if(valor.trim().equals(""))
						celulaVazia = true;
						
				}catch(Exception e)
				{
					
				}
			}
			else
				celulaVazia = true;
		}
		
		return contadorLinha - 1;
	}
	
	public int getPrimeiraLinhaPreenchidaComValorEmUmaColuna(String texto, int coluna, int maxBusca) {
		int contadorLinha = 0;
		boolean celulaEncontrada = false;
		
		Row linha = planilhaAtiva.getRow(contadorLinha);
		Cell celula;
		
		if(linha != null)
		{
			celula = linha.getCell(coluna);
			
			try
			{
				String valor = celula.getStringCellValue();
				
				if(valor.trim().equals(texto))
					celulaEncontrada = true;
					
			}catch(Exception e)
			{
				
			}
		}	
		
		while(!celulaEncontrada && contadorLinha <= maxBusca)
		{
			contadorLinha++;
			
			linha = planilhaAtiva.getRow(contadorLinha);
			
			if(linha != null)
			{
				celula = linha.getCell(coluna);
				
				try
				{
					String valor = celula.getStringCellValue();
					
					if(valor.trim().equals(texto))
						celulaEncontrada = true;
						
				}catch(Exception e)
				{
					
				}
			}
		}
		
		if(contadorLinha > maxBusca)
			contadorLinha = -1;
		
		return contadorLinha;
	}

	public void setPrimeiraLinhaVazia(int primeiraLinhaVazia) {
		this.ultimaLinhaPreenchida = primeiraLinhaVazia;
	}
	

	public boolean ehCelulaVazia(int Linha, int Coluna) {
		Row linha = planilhaAtiva.getRow(Linha);
		Cell celula = linha.getCell(Coluna);
		
	    if (celula == null) return true;
	
	    if (celula.getCellType() == CellType.BLANK) {
	        return true;
	    }
	
	    if (celula.getCellType() == CellType.STRING) {
	        return celula.getStringCellValue().trim().isEmpty();
	    }
	
	    return false;
	}

	
	public String getValorDaCelulaString(int Linha, int Coluna)
	{
		Row linha = planilhaAtiva.getRow(Linha);
		
		Cell celula = null;
		
		if(linha != null)
		{
			celula = linha.getCell(Coluna);
			if(celula == null)
				return "";
		}
		else
			return null;
		
		return celula.getStringCellValue();
	}
	

	public String getValorDaCelulaComoString(int Linha, int Coluna, String formatoSeData) {

		Row linha = planilhaAtiva.getRow(Linha);
		if(linha == null)
			return "";
		
		Cell celula = linha.getCell(Coluna);
		if(celula == null)
			return "";
		
        return valorStringDaCelula(celula, formatoSeData);
    }
	
	private String valorStringDaCelula(Cell celula, String formatoSeData)
	{
		switch (celula.getCellType()) {

	        case STRING:
	            return celula.getStringCellValue();
	
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(celula)) {
	                // Formata data no padrão desejado
	                SimpleDateFormat sdf = new SimpleDateFormat(formatoSeData);
	                return sdf.format(celula.getDateCellValue());
	            } else {
	                // Remove .0 de números inteiros, se quiser
	                double value = celula.getNumericCellValue();
	                if (value == (long) value) {
	                    return String.valueOf((long) value);
	                } else {
	                    return String.valueOf(value);
	                }
	            }
	
	        case BOOLEAN:
	            return String.valueOf(celula.getBooleanCellValue());
	
	        case FORMULA:
	            // Avalia a fórmula e retorna o resultado como string
	            FormulaEvaluator evaluator = celula.getSheet()
	                    .getWorkbook()
	                    .getCreationHelper()
	                    .createFormulaEvaluator();
	
	            return valorStringDaCelula(evaluator.evaluateInCell(celula), formatoSeData);
	
	        case BLANK:
	            return "";
	
	        default:
	            return "";
		}
	}

	
	public int getValorDaCelulaInt(int Linha, int Coluna)
	{
		Row linha = planilhaAtiva.getRow(Linha);
		Cell celula = linha.getCell(Coluna);
		if(celula == null)
			return -1;
		
		return (int)celula.getNumericCellValue();
	}
	
	public LocalDate getValorDaCelulaDate(int Linha, int Coluna)
	{
		Row linha = planilhaAtiva.getRow(Linha);
		Cell celula = linha.getCell(Coluna);
		LocalDate data = null;
				
		try
		{
		
			if(celula == null || celula.getLocalDateTimeCellValue() == null)
				return null;

			data = celula.getLocalDateTimeCellValue().toLocalDate();
		}catch(Exception e)
		{
			String textoCelula = celula.getStringCellValue();
			//System.out.println(textoCelula);
			data = LocalDate.parse(textoCelula.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
		
		return data;
	}
	
	public LocalDateTime getValorDaCelulaDateTime(int Linha, int Coluna)
	{
		Row linha = planilhaAtiva.getRow(Linha);
		Cell celula = linha.getCell(Coluna);
		LocalDateTime data = null;
				
		try
		{
		
			if(celula == null || celula.getLocalDateTimeCellValue() == null)
				return null;

			data = celula.getLocalDateTimeCellValue();
		}catch(Exception e)
		{
			String textoCelula = celula.getStringCellValue();
			//System.out.println(textoCelula);
			data = LocalDateTime.parse(textoCelula.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
		
		return data;
	}
	
	public LocalDateTime getValorDaCelulaDateTime(int Linha, int Coluna, String formato)
	{
		Row linha = planilhaAtiva.getRow(Linha);
		Cell celula = linha.getCell(Coluna);
		LocalDateTime data = null;
				
		try
		{
		
			if(celula == null || celula.getLocalDateTimeCellValue() == null)
				return null;

			data = celula.getLocalDateTimeCellValue();
		}catch(Exception e)
		{
			String textoCelula = celula.getStringCellValue();
			//System.out.println(textoCelula);
			data = LocalDateTime.parse(textoCelula.substring(0, formato.length()), DateTimeFormatter.ofPattern(formato));
		}
		
		return data;
	}
	
	public LocalTime getValorDaCelulaTime(int Linha, int Coluna, String formato)
	{
		Row linha = planilhaAtiva.getRow(Linha);
		Cell celula = linha.getCell(Coluna);
		LocalTime data = null;
				
		try
		{
		
			if(celula == null || celula.getLocalDateTimeCellValue() == null)
				return null;

			data = celula.getLocalDateTimeCellValue().toLocalTime();
		}catch(Exception e)
		{
			String textoCelula = celula.getStringCellValue();
			//System.out.println(textoCelula);
			data = LocalTime.parse(textoCelula.substring(0, formato.length()), DateTimeFormatter.ofPattern(formato));
		}
		
		return data;
	}
	
	

	public void copiarFormatoEntreLinhas(int indiceLinhaOrigem, int indiceLinhaDestino) 
	{
       try 
       {
            // Linha origem (com formatação)
            Row linhaOrigem = planilhaAtiva.getRow(indiceLinhaOrigem); // Ex.: linha 2 (índice 1)
            // Linha destino (onde aplicar formatação)
            Row linhaDestino = planilhaAtiva.getRow(indiceLinhaDestino); // Ex.: linha 6 (índice 5)
            if (linhaDestino == null) {
                linhaDestino = planilhaAtiva.createRow(indiceLinhaDestino);
            }
            
            linhaDestino.setHeightInPoints(19.5f);

            for (int i = 0; i < linhaOrigem.getLastCellNum(); i++) {
                Cell celOrigem = linhaOrigem.getCell(i);
                if (celOrigem == null) continue;

                // Cria célula destino
                Cell celDestino = linhaDestino.getCell(i);
                if (celDestino == null) {
                    celDestino = linhaDestino.createCell(i);
                }

                // Copia estilo
                CellStyle estiloOrigem = celOrigem.getCellStyle();
                CellStyle novoEstilo = arquivoXLSX.createCellStyle();
                novoEstilo.cloneStyleFrom(estiloOrigem);
                celDestino.setCellStyle(novoEstilo);

                // Opcional: copiar valor também
                // celDestino.setCellValue(celOrigem.toString());
            }

            // Salvar arquivo
            try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
                arquivoXLSX.write(fos);
            }

            //System.out.println("Formatação copiada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void copiarFormato(int indiceLinhaOrigem, int indiceLinhaDestino) 
	{
       try 
       {
            // Linha origem (com formatação)
            Row linhaOrigem = planilhaAtiva.getRow(indiceLinhaOrigem); // Ex.: linha 2 (índice 1)
            // Linha destino (onde aplicar formatação)
            Row linhaDestino = planilhaAtiva.getRow(indiceLinhaDestino); // Ex.: linha 6 (índice 5)
            if (linhaDestino == null) {
                linhaDestino = planilhaAtiva.createRow(indiceLinhaDestino);
            }
            
            linhaDestino.setHeightInPoints(19.5f);

            for (int i = 0; i < linhaOrigem.getLastCellNum(); i++) {
                Cell celOrigem = linhaOrigem.getCell(i);
                if (celOrigem == null) continue;

                // Cria célula destino
                Cell celDestino = linhaDestino.getCell(i);
                if (celDestino == null) {
                    celDestino = linhaDestino.createCell(i);
                }

                // Copia estilo
                //CellStyle estiloOrigem = celOrigem.getCellStyle();
                //CellStyle novoEstilo = arquivoXLSX.createCellStyle();
               // novoEstilo.cloneStyleFrom(estiloOrigem);
                celDestino.setCellStyle(celOrigem.getCellStyle());

                // Opcional: copiar valor também
                // celDestino.setCellValue(celOrigem.toString());
            }

//            // Salvar arquivo
//            try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
//                arquivoXLSX.write(fos);
//            }

            //System.out.println("Formatação copiada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void desabilitarCalculos()
	{
		arquivoXLSX.setForceFormulaRecalculation(false);
	}
	
	public void forcarCalculos()
	{
		arquivoXLSX.setForceFormulaRecalculation(true);
	}
	

	public void atualizarTabelasDinamicas(String nomePlanilhaDinamica, String nomePlanilhaDados, String referencia) {
	
	    Path caminho = Paths.get(nomeDoAquivo);
	
	    try {
	        XSSFWorkbook wb = (XSSFWorkbook) arquivoXLSX;
	        XSSFSheet sheetPivot = wb.getSheet(nomePlanilhaDinamica);
	
	        for (XSSFPivotTable pivotTable : sheetPivot.getPivotTables()) {
	
	            CTPivotCacheDefinition cacheDef = pivotTable.getPivotCacheDefinition().getCTPivotCacheDefinition();
	
	            cacheDef.getCacheSource().getWorksheetSource().setSheet(nomePlanilhaDados);
	
	            cacheDef.getCacheSource().getWorksheetSource().setRef(referencia);
	
	            cacheDef.setRefreshOnLoad(true);
	        }
	
	        try (FileOutputStream fos = new FileOutputStream(caminho.toFile())) {
	            wb.write(fos);
	        }
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	public void copiarFormulas(int linhaComFormula, int linhaOndeAplicarAFormula, ArrayList<Integer> colunas){

        try  
        {
            Row linhaOrigem = planilhaAtiva.getRow(linhaComFormula);
            Row linhaDestino = planilhaAtiva.getRow(linhaOndeAplicarAFormula);
            
            arquivoXLSX.setForceFormulaRecalculation(true);
            
            for(int coluna : colunas)
            {
            	Cell celulaOrigem = linhaOrigem.getCell(coluna);

	            if (linhaDestino == null) linhaDestino = planilhaAtiva.createRow(linhaOndeAplicarAFormula);
	            
	            Cell celulaDestino = linhaDestino.getCell(coluna);
	            if (celulaDestino == null) celulaDestino = linhaDestino.createCell(coluna);
	
	            if (celulaOrigem != null && celulaOrigem.getCellType() == CellType.FORMULA) {
	                // Copia a fórmula textual
	                celulaDestino.setCellFormula(celulaOrigem.getCellFormula().replaceAll("([A-Z]+)(" + (linhaComFormula + 1) + ")", "$1" + (linhaOndeAplicarAFormula + 1)));
	            } 
	            
	            // (Opcional) Copiar estilo da célula
//	            CellStyle style = celulaOrigem.getCellStyle();
//
//	            
//	            style.cloneStyleFrom(celulaOrigem.getCellStyle());
	            celulaDestino.setCellStyle(celulaOrigem.getCellStyle());
	
	            // (Opcional) Avaliar fórmulas
	            FormulaEvaluator evaluator = arquivoXLSX.getCreationHelper().createFormulaEvaluator();
	            evaluator.evaluateFormulaCell(celulaDestino);
	
//	            try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
//	                arquivoXLSX.write(fos);
//	            }
            }
        }catch(Exception e)
        {
        	e.printStackTrace();
        }

	}
	
	public void copiarFormulasDaLinhaAnterior(int linhaComFormula, ArrayList<Integer> colunas){

        try  
        {
            Row linhaOrigem = planilhaAtiva.getRow(linhaComFormula);
            Row linhaDestino = planilhaAtiva.getRow(linhaComFormula + 1);
            
            arquivoXLSX.setForceFormulaRecalculation(true);
            
            for(int coluna : colunas)
            {
            	Cell celulaOrigem = linhaOrigem.getCell(coluna);

	            if (linhaDestino == null) linhaDestino = planilhaAtiva.createRow(linhaComFormula + 1);
	            
	            Cell celulaDestino = linhaDestino.getCell(coluna);
	            if (celulaDestino == null) celulaDestino = linhaDestino.createCell(coluna);
	
	            if (celulaOrigem != null && celulaOrigem.getCellType() == CellType.FORMULA) {
	                // Copia a fórmula textual
	                celulaDestino.setCellFormula(celulaOrigem.getCellFormula().replaceAll("([A-Z]+)(" + (linhaComFormula + 1) + ")", "$1" + (linhaComFormula + 2)));
	            } 
	            
	            // (Opcional) Copiar estilo da célula
//	            CellStyle style = arquivoXLSX.createCellStyle();
//	            style.cloneStyleFrom(celulaOrigem.getCellStyle());
	            celulaDestino.setCellStyle(celulaOrigem.getCellStyle());
	
	            // (Opcional) Avaliar fórmulas
	            FormulaEvaluator evaluator = arquivoXLSX.getCreationHelper().createFormulaEvaluator();
	            evaluator.evaluateFormulaCell(celulaDestino);
	
	            try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
	                arquivoXLSX.write(fos);
	            }
            }
        }catch(IOException e)
        {
        	e.printStackTrace();
        }

	}
	
	public String ocultarPlanilha(int indicePlanilha)
	{
		try
		{
			arquivoXLSX.setSheetHidden(indicePlanilha, true);
		 try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
             arquivoXLSX.write(fos);
         }
			 
	    }catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
		
		return "";
	}
	
	public String ocultarPlanilha(String nomePlanilha)
	{
		try
		{
			int indicePlanilha;
			
			indicePlanilha = arquivoXLSX.getSheetIndex(nomePlanilha);
			
			arquivoXLSX.setSheetHidden(indicePlanilha, true);
			
			 try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
	             arquivoXLSX.write(fos);
	         }
			 
	    }catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
		
		return "";
	}
	
	public boolean ehCelulaComString(int linha, int coluna)
	{
		Cell cell = ensureCell(planilhaAtiva, linha, coluna);
        
		
		if (cell != null && cell.getCellType() == CellType.STRING) {
		    return true;
		}

		return false;
	}
	

    public void editarTituloGrafico(String nomeDaPlanilha, int indiceImagemGrafica, String tituloDoGrafico)
    {
    	try
    	{

	        Path caminho = Paths.get(nomeDoAquivo); // ajuste o caminho
	        abrirPlanilha(nomeDaPlanilha, 0);
	        
	        XSSFSheet sheet = (XSSFSheet) arquivoXLSX.getSheet(nomeDaPlanilha); // ou getSheetAt(0)
	
	        XSSFDrawing drawing = sheet.getDrawingPatriarch();
	        if (drawing == null) {
	            throw new IllegalStateException("A planilha não possui desenho/gráficos.");
	        }
	
	        ArrayList<XSSFChart> charts = (ArrayList<XSSFChart>)drawing.getCharts();
	        if (charts.isEmpty()) {
	            throw new IllegalStateException("Nenhum gráfico encontrado na planilha.");
	        }
	
	        XSSFChart chart = charts.get(indiceImagemGrafica); // pegue o gráfico desejado
	        chart.setTitleText(tituloDoGrafico);
	        chart.setTitleOverlay(false); // exibe título como elemento separado (sem sobrepor o gráfico)
	
	        try (FileOutputStream fos = new FileOutputStream(caminho.toFile())) {
	            arquivoXLSX.write(fos);
	        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int obterValorInteiroDeUmaCelulaComFormula(String nomeDaPlanilha, int linha, int coluna)
    {
		Path caminho = Paths.get(nomeDoAquivo); // ajuste o caminho
		abrirPlanilha(nomeDaPlanilha, 0);
		

		Row row = planilhaAtiva.getRow(linha);     // linha 2 (0-based)
        Cell cell = row.getCell(coluna);    // coluna D (0-based)

        FormulaEvaluator evaluator = arquivoXLSX.getCreationHelper().createFormulaEvaluator();
        CellValue cv = evaluator.evaluate(cell); // NÃO altera a célula

		int valor = (int)cv.getNumberValue();
        
		return valor;
    }
    
    public void formatarColuna(String nomeDaPlanilha, int linhaInicial, int coluna, String tipo)
    {
    	Path caminho = Paths.get(nomeDoAquivo); // ajuste o caminho
    	
    	abrirPlanilha(nomeDaPlanilha, linhaInicial);
    	
    	try {
    	
	    	CellStyle dateStyle;
	    	
	    	Row row = planilhaAtiva.getRow(linhaInicial);     // linha 2 (0-based)
	        Cell cell = row.getCell(coluna);
	    	
			dateStyle = arquivoXLSX.createCellStyle();
			dateStyle.cloneStyleFrom(cell.getCellStyle());
			
			if (tipo.equals("Date"))
	        {
	   			dateStyle = arquivoXLSX.createCellStyle();
	   			dateStyle.cloneStyleFrom(cell.getCellStyle());
	       		DataFormat df = arquivoXLSX.createDataFormat();
	      		dateStyle.setDataFormat(df.getFormat("dd/MM/yyyy"));
	        }
	        else if(tipo.equals("Date mes/ano"))
	        {
	   			dateStyle = arquivoXLSX.createCellStyle();
	   			dateStyle.cloneStyleFrom(cell.getCellStyle());
	        	DataFormat df = arquivoXLSX.createDataFormat();
				dateStyle.setDataFormat(df.getFormat("[$-pt-BR]mmm/yyyy"));
	        }
	        else if (tipo.equals("DateTime"))
	        {
	   			dateStyle = arquivoXLSX.createCellStyle();
	   			dateStyle.cloneStyleFrom(cell.getCellStyle());
	        	DataFormat df = arquivoXLSX.createDataFormat();
				dateStyle.setDataFormat(df.getFormat("dd/MM/yyyy HH:mm"));
	        }
	        else if (tipo.equals("Time"))
	        {
	   			dateStyle = arquivoXLSX.createCellStyle();
	   			dateStyle.cloneStyleFrom(cell.getCellStyle());
	        	DataFormat df = arquivoXLSX.createDataFormat();
				dateStyle.setDataFormat(df.getFormat("HH:mm"));
	        }
	        else if(tipo.equals("String"))
	        {
	   			dateStyle = arquivoXLSX.createCellStyle();
	   			dateStyle.cloneStyleFrom(cell.getCellStyle());
	        	CellStyle general = cell.getCellStyle();
				general.setDataFormat((short) 0); // General
	        }
	        else if(tipo.equals("Integer"))
	        {
	   			dateStyle = arquivoXLSX.createCellStyle();
	   			dateStyle.cloneStyleFrom(cell.getCellStyle());
	        	CellStyle general = cell.getCellStyle();
				general.setDataFormat((short) 0); // General
	        }
	        else if(tipo.equals("Porcentagem"))
	        {
	        	DataFormat df = arquivoXLSX.createDataFormat();
	        	CellStyle general = cell.getCellStyle();
				general.setDataFormat(df.getFormat("0.00%")); // General
	        }
	        
	        for(int linha = linhaInicial; linha < ultimaLinhaPreenchida; linha++)
	        {
	        	row = planilhaAtiva.getRow(linha);     // linha 2 (0-based)
	        	
	        	if(row != null)
	        	{
		            cell = row.getCell(coluna);
		            if(cell != null)
		            	cell.setCellStyle(dateStyle);
	        	}
	        }
	        try (FileOutputStream fos = new FileOutputStream(caminho.toFile())) {
	            arquivoXLSX.write(fos);
	        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
/*
 * public void copiarFormulasDaLinhaAnterior(int linhaComFormula,
 * ArrayList<Integer> colunas){
 * 
 * 
 * CellCopyPolicy policy = new CellCopyPolicy.Builder() .cellFormula(true) //
 * copia fórmula .cellStyle(true) // copia estilo .cellValue(true) // copia
 * valor se não for fórmula .condenseRows(false) .mergeHyperlink(true) .build();
 * 
 * 
 * try { Row linhaOrigem = planilhaAtiva.getRow(linhaComFormula); Row
 * linhaDestino = planilhaAtiva.getRow(linhaComFormula + 1);
 * 
 * for(int coluna : colunas) { linhaDestino.getCell(coluna)
 * 
 * try (FileOutputStream fos = new FileOutputStream(nomeDoAquivo)) {
 * arquivoXLSX.write(fos); } } }catch(IOException e) { e.printStackTrace(); }
 * 
 * }
 */
	
	
}
