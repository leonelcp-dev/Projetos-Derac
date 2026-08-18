package modulosGEFIC;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRGEFIC;
import dadosGerais.MesesFormatados;
import modelosDados.CelulaExcel;

public class FilasNominaisGEFIC 
{
	private String pastaBase;
	private String pastaBaseDadosGEFIC;
	private String ambiente;
	private MesesFormatados meses;
	private IdentificadoresPastasCompartilhadasCDIDRGEFIC diretoriosCDIDR;
	
	public FilasNominaisGEFIC() 
	{
		meses = new MesesFormatados();
	}	
	
	public FilasNominaisGEFIC(String pastaBase, String ambiente, boolean ehOPM)
	{
		this.pastaBase = pastaBase;
		this.ambiente = ambiente;
		
		meses = new MesesFormatados();
		
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRGEFIC.valueOf(ambiente.toUpperCase());
		
    	try {
			
			Reader reader = null;
			
			if(ambiente.equals("TESTE"))
				reader = new InputStreamReader(new FileInputStream(pastaBase + "\\Documents\\SIRESP\\parametros_pasta.csv"), StandardCharsets.ISO_8859_1);
			else if(ambiente.equals("PRODUCAO"))
			{
				reader = new InputStreamReader(new FileInputStream(pastaBase + "\\Documents\\SIRESP\\parametros_pasta.csv"), StandardCharsets.ISO_8859_1);
				//reader = new InputStreamReader(new FileInputStream("parametros_pasta.csv"), StandardCharsets.ISO_8859_1);
			}
				
			if(reader == null)
			{
				JOptionPane.showMessageDialog(null, "Não foi informado o ambiente da execução");
			}
			
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
			
			HashMap<String, String> mapaDePastas = new HashMap<String, String>();
			
			Iterable<CSVRecord> registros = format.parse(reader);
			for(CSVRecord registro : registros)						
			{
				mapaDePastas.put(registro.get(0) + registro.get(1), registro.get(2));
			}
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDRGEFIC.REFERENCIA_PASTAS_MONITORAMENTO_GEFIC.getTextoIdentificador()))
				pastaBaseDadosGEFIC = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDRGEFIC.REFERENCIA_PASTAS_MONITORAMENTO_GEFIC.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Ambulatorial compartilhada");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, "Erro ao encontrar o arquivos de parâmetros da pasta");
		}
	}
	
	public String gerarFilasNominaisPorStatus(String dataArquivo, String status)
	{
		HashMap<String, ArrayList<CelulaExcel>> celulasPorUnidade;
		
		
		
		return "";
	}
	
}
