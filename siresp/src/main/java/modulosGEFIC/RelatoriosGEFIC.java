package modulosGEFIC;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.openqa.selenium.WebDriver;

import dadosGerais.IdentificadoresPaginaWebGEFIC;
import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRGEFIC;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRUrgencia;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoGEFICFilas;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado;
import interacao_externa.AcoesGeraisPaginaWeb;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;
import utils.Utils;

public class RelatoriosGEFIC
{
	
	private String pastaBase;
	private String pastaDownloads;
	private String pastaBaseDadosGEFIC;
	private String ambiente;
	private MesesFormatados meses;
	private IdentificadoresPastasCompartilhadasCDIDRGEFIC diretoriosCDIDR;
	
	public RelatoriosGEFIC() 
	{
		meses = new MesesFormatados();
	}	
	
	public RelatoriosGEFIC(String pastaBase, String pastaDownloads, String ambiente)
	{
		this.pastaBase = pastaBase;
		this.pastaDownloads = pastaDownloads;
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
	
	public String baixarFilaNominalCompleta(WebDriver driver, boolean separarPorStatus, boolean ehOPM, LocalDate data)
	{
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		ArrayList<String> opcoes = new ArrayList<String>();
		
		String dataFormatada = data.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		
		if(ehOPM)
		{
			opcoes.add("Filas OPM");
		}
		else
		{
			opcoes.add("Filas");
		}
		
		ArrayList<String> listaStatus = new ArrayList<String>();
		if(separarPorStatus)
		{
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_AGUARDANDO_NA_FILA.getTextoIdentificador());
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_PROCEDIMENTO_REALIZADO.getTextoIdentificador());
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_REGISTRO_CANCELADO.getTextoIdentificador());
		}
		else
		{
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_TODOS.getTextoIdentificador());
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		selecionarTodosCampos(driver, paginaWeb);
		
		for(String status : listaStatus)
		{
			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_FILTRO_STATUS.getTextoIdentificador());
			
			paginaWeb.selecionarItemSelectULLIPeloTextoVisivelDeUmaLinhaPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_FILTRO_STATUS_UL.getTextoIdentificador(), status);
			
			//while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
			while(paginaWeb.elementoEstaVisivelPeloClassName(driver, IdentificadoresPaginaWebGEFIC.CLASS_NAME_AGUARDANDO.getTextoIdentificador()));
			
			Arquivo arquivoBaixado = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_FILAS_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICFilas.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
			//paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_FILAS_BOTAO_EXCEL.getTextoIdentificador(), "id");
			
			String nomeArquivo = "";
			if(ehOPM)
			{
				nomeArquivo = ParametrosArquivoGEFICFilas.NOME_ARQUIVO_FILA_OPM.getDescricao().replace(ParametrosArquivoGEFICFilas.MASCARA_DATA_DOWNLOAD.getDescricao(), dataFormatada).replace(ParametrosArquivoGEFICFilas.MASCARA_STATUS.getDescricao(), status);
				arquivoBaixado.renomear(nomeArquivo);
			}
			else
			{
				nomeArquivo = ParametrosArquivoGEFICFilas.NOME_ARQUIVO_FILA_GERAL.getDescricao().replace(ParametrosArquivoGEFICFilas.MASCARA_DATA_DOWNLOAD.getDescricao(), dataFormatada).replace(ParametrosArquivoGEFICFilas.MASCARA_STATUS.getDescricao(), status);
				arquivoBaixado.renomear(nomeArquivo);				
			}
			
			moverParaPastaFilas(arquivoBaixado, data);
			
			//while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
			while(paginaWeb.elementoEstaVisivelPeloClassName(driver, IdentificadoresPaginaWebGEFIC.CLASS_NAME_AGUARDANDO.getTextoIdentificador()));
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return "";
	}
	
	private String moverParaPastaFilas(Arquivo arquivo, LocalDate data)
	{
		String caminhoDaPasta = pastaBaseDadosGEFIC + "\\" + diretoriosCDIDR.getPastaArquivosBaixados();
		caminhoDaPasta = caminhoDaPasta + "\\" + data.getYear();
		Pasta pasta = new Pasta(caminhoDaPasta, true);
		
		caminhoDaPasta = caminhoDaPasta + "\\" + meses.getMeses().get(data.getMonthValue() - 1).getMesNumero() + " - " + Utils.primeiraMaiuscula(meses.getMeses().get(data.getMonthValue() - 1).getMesDescricao()) + " de " + data.getYear();
		pasta = new Pasta(caminhoDaPasta, true);
		
		caminhoDaPasta = caminhoDaPasta + "\\" + data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		pasta = new Pasta(caminhoDaPasta, true);
		
		arquivo.mover(caminhoDaPasta + "\\" + arquivo.getNomeDoArquivo());
		
		return "";
	}
	
	private Arquivo baixarArquivos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String idBotaoDownload, String extensaoArquivo) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		paginaWeb.clicarBotaoSubmit(driver, idBotaoDownload, "id");
		
		String arquivoMaisRecente;
			
		do
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arquivoMaisRecente = pastaOrigem.arquivoRecentementeModificado();
			
			System.out.println(arquivoMaisRecente + " ----- " + ultimoRecente);
		}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(extensaoArquivo));
			
		Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);

		return arquivo;
	}
	
	private String selecionarTodosCampos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb)
	{
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_BOTAO_MAIS_COLUNAS.getTextoIdentificador());
		
		for(int indice = 1; indice <= IdentificadoresPaginaWebGEFIC.XPATH_FILAS_CHECK_BOX_ACOES.getIndice(); indice++)
		{
			String xPathCheckBox = IdentificadoresPaginaWebGEFIC.XPATH_FILAS_CHECK_BOX_DINAMICO.getTextoIdentificador().replaceAll(IdentificadoresPaginaWebGEFIC.MASCARA_VALOR_DINAMICO.getTextoIdentificador(), String.valueOf(indice));
			
			if(!paginaWeb.elementoEstaSelecionadoPeloXPATH(driver, xPathCheckBox))
				paginaWeb.clicarLinkPeloXPath(driver, xPathCheckBox);
			
		}
		
		return "";
	}
	
}
