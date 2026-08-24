package utils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class Utils {
	
	public static String converterData(String Data)
	{

	    Locale localeBR = Locale.of("pt", "BR"); // Java 21
	    DateTimeFormatter fmtDataCompleta = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
	    try {
	    	
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	        LocalDateTime data = LocalDateTime.parse(Data, fmtEntradaAbrev);
	        
	        return fmtDataCompleta.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	    
	    try {
	    	
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	        LocalDateTime data = LocalDateTime.parse(Data, fmtEntradaAbrev);
	        
	        return fmtDataCompleta.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	    
	    try {
	    	
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDateTime data = LocalDateTime.parse(Data, fmtEntradaAbrev);
	        
	        return fmtData.format(data) + " 00:00";
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	    
	      
	    try {
	    	
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("M/d/yy H:mm");
	        LocalDateTime data = LocalDateTime.parse(Data, fmtEntradaAbrev);
	        
	        return fmtDataCompleta.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	    
	    try {
	    	
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("M/d/yy", Locale.US);
	        LocalDateTime data = LocalDateTime.parse(Data, fmtEntradaAbrev);
	        
	        return fmtData.format(data) + " 00:00";
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	    
	    throw new IllegalArgumentException("Formato de data inválido: " + Data);
	}
	
	public static String normalizarDataParaMesAno(String valor) {
	    if (valor == null || valor.isBlank())
	        return null;
	
	    Locale localeBR = Locale.of("pt", "BR"); // Java 21
	    DateTimeFormatter fmtMesAno = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
	
	    // 1️ Caso seja número serial do Excel
	    if (valor.matches("\\d+")) {
	        long serial = Long.parseLong(valor);
	        LocalDate data = LocalDate.of(1899, 12, 30).plusDays(serial); // Ajuste Excel
	        return fmtMesAno.format(data);
	    }
	
	    // 2️ Caso seja dd/MM/yyyy
	    try {
	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy", localeBR);
	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
	        return fmtMesAno.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e tenta o próximo formato
	    }
	
	    // 3️ Caso seja mmm/yyyy (direto do Excel ou do POI)
	    try {
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
	        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy", localeBR));
	        return fmtMesAno.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	
	    throw new IllegalArgumentException("Formato de data inválido: " + valor);
	}
	
	public static String somenteIniciais(String texto)
	{
		String iniciais = "";
		
		texto = texto.trim();
		String[] palavras = texto.split(" ");
		
		for(String palavra : palavras)
		{
			palavra = palavra.trim();
			
			if(palavra.length() > 0)
				iniciais += palavra.charAt(0) + " ";
		}
		
		return iniciais.trim();
	}
	
	public static LocalDate converterStringParaData(String data, String formato)
	{
		try 
		{
			return LocalDate.parse(data, DateTimeFormatter.ofPattern(formato));
		}catch(Exception e)
		{
			return null;
		}
	}
	
	public static String primeiraMaiuscula(String texto) 
	{
		String[] palavras = texto.toLowerCase().split(" ");
		StringBuilder resultado = new StringBuilder();

		for (String palavra : palavras) 
		{

			if (!palavra.isEmpty()) 
			{
				resultado.append(Character.toUpperCase(palavra.charAt(0))).append(palavra.substring(1)).append(" ");
			}
		}
		return resultado.toString().trim();

	}
	
	public static String obterTextoDeImagem(String caminhoImagem)
	{
		String texto = "";
		
    	String nomeUsuario = System.getProperty("user.name");
    	System.out.println(nomeUsuario);
		
		try
		{
			ITesseract tesseract = new Tesseract();
			// Pasta onde ficam os arquivos .traineddata
			tesseract.setDatapath("C:\\Users\\" + nomeUsuario + "\\Documents\\SIRESP\\Dicionarios OCR");
			tesseract.setLanguage("por");
			texto = tesseract.doOCR(new File(caminhoImagem));
			System.out.println("Texto: " + texto);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
		
		return texto;
	}

}
