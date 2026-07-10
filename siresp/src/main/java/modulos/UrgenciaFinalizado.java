package modulos;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.openqa.selenium.WebDriver;

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRUrgencia;
import dadosGerais.ParametrosArquivoOfertaDemanda;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFormaResolucao;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaProducaoRegulador;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaVagaZero;
import dadosGerais.ParametrosArquivoUrgenciaRelatorioProdutividade;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado;
import dadosGerais.ParametrosTabelaUrgenciaSolicitacoesPendentes;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.ExcelBinder;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import modelosDados.CelulaExcel;
import modelosDados.DadosAcumuladosVagaZero;
import modelosDados.EntidadeExecutanteR1;
import modelosDados.EntidadeLeito;
import modelosDados.IntervalosUrgencia;
import modelosDados.UrgenciaFinalizadoDetalhado;
import modelosDados.UrgenciaFormaResolucao;
import modelosDados.UrgenciaProducaoRegulador;
import modelosDados.UrgenciaProducaoReguladorMensal;
import modelosDados.UrgenciaVagaZero;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;
import modelosDados.UrgenciaFinalizadoAgrupado;

public class UrgenciaFinalizado 
{
	private String pastaBaseAmbulatorialCDIDR;
	private String pastaBase;
	private String pastaDownloads;

	HashMap<String, EntidadeLeito> mapaEntidadesFinalizacaoUrgencia;
	HashMap<String, EntidadeLeito> mapaEntidadesVagaZero;
	ArrayList<String> entidadesFinalizacaoUrgencia;
	ArrayList<String> entidadesVagaZero;
	
	private String dataDeAnalise;
	private LocalDate dataInformada;
	private String dataFormatada;
	
	ArrayList<String> unidadesSolicitantes;
	HashMap<String, UrgenciaFinalizadoAgrupado> urgenciasAgrupadasJaRegistradas;
	HashMap<String, UrgenciaFinalizadoDetalhado> urgenciasDetalhadasJaRegistradas;
	HashMap<String, UrgenciaVagaZero> urgenciasVagaZero;
	HashMap<String, UrgenciaFormaResolucao> urgenciasFormaDeResolucao;
	HashMap<String, UrgenciaProducaoRegulador> urgenciasProducaoRegulador;
	HashMap<String, UrgenciaProducaoReguladorMensal> urgenciasProducaoreguladorMensal;
	private IdentificadoresPastasCompartilhadasCDIDRUrgencia diretoriosCDIDR; 

	public UrgenciaFinalizado(String pastaBase, String ambiente)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRUrgencia.valueOf(ambiente);
		pastaBaseAmbulatorialCDIDR = pastaBase;
	}
	
	public UrgenciaFinalizado()
	{

	}
	
	public String obterAgrupamentoDeEsperaUrgencia(WebDriver driver, String ambiente, String data, String caminhoPastaBase, String caminhoPastaDownloads)
	{			
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRUrgencia.valueOf(ambiente.toUpperCase());
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
    	
		pastaBase = caminhoPastaBase;
		pastaDownloads = caminhoPastaDownloads;
		
		if(data == null)
			dataDeAnalise = JOptionPane.showInputDialog(null, "Insira a data do dia de análise (formato: dd/mm/yyyy)", "Data da Análise", JOptionPane.QUESTION_MESSAGE).trim();
		else
			dataDeAnalise = data;
		
		dataInformada = LocalDate.parse(dataDeAnalise, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		dataFormatada = dataInformada.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		
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
				return "";
			}
			
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
			
			HashMap<String, String> mapaDePastas = new HashMap<String, String>();
			
			Iterable<CSVRecord> registros = format.parse(reader);
			for(CSVRecord registro : registros)						
			{
				mapaDePastas.put(registro.get(0) + registro.get(1), registro.get(2));
			}
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDRUrgencia.REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR.getTextoIdentificador()))
				pastaBaseAmbulatorialCDIDR = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDRUrgencia.REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Ambulatorial compartilhada");
				return "";
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, "Erro ao encontrar o arquivos de parâmetros da pasta");
			return "";
		}
		
		//gerarCopiaTemporariaRelatorioProducao();
    	
		mapaEntidadesFinalizacaoUrgencia = new HashMap<String, EntidadeLeito>();
		mapaEntidadesVagaZero = new HashMap<String, EntidadeLeito>();
		entidadesFinalizacaoUrgencia = new ArrayList<String>();
		entidadesVagaZero = new ArrayList<String>();
		
		BufferedReader br;
		//obter tipos de unidades
		try {
			br = new BufferedReader(new FileReader(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoEntidades(), StandardCharsets.ISO_8859_1));
			
			String linha;
			
			//lendo e descartando cabeçalho
			linha = br.readLine();
			
			while ((linha = br.readLine()) != null) {
			    String[] colunas = linha.split(";");
			    
			    //Monitora Vaga zero
			    if(colunas[4].equals("SIM"))
			    {
			    	EntidadeLeito entidade = new EntidadeLeito(colunas[0], colunas[1]);
			    	mapaEntidadesVagaZero.put(colunas[3], entidade);
			    	entidadesVagaZero.add(colunas[3]);
			    }
			    
			    //Monitora Finalizado
			    if(colunas[5].equals("SIM"))
			    {
			    	EntidadeLeito entidade = new EntidadeLeito(colunas[0], colunas[1]);
			    	mapaEntidadesFinalizacaoUrgencia.put(colunas[3], entidade);
			    	entidadesFinalizacaoUrgencia.add(colunas[3]);
			    }
			}
			
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	//consolidados urgência
    	ArrayList<UrgenciaFinalizadoAgrupado> listaUrgencias = new ArrayList<UrgenciaFinalizadoAgrupado>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFinalizadoAgrupado.class, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasAgrupadasJaRegistradas = new HashMap<String, UrgenciaFinalizadoAgrupado>();
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFinalizadoAgrupado urgencia : listaUrgencias)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData(), "dd/MM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			
			urgenciasAgrupadasJaRegistradas.put(urgencia.getData() + urgencia.getSolicitante() + urgencia.getRecurso() + urgencia.getFicha(), urgencia);
			
			linhaArquivo++;
		}
		
		//consolidados urgência por hora
    	ArrayList<UrgenciaFinalizadoDetalhado> listaUrgenciasPorHora = new ArrayList<>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgenciasPorHora = ExcelBinder.readSheet(in, UrgenciaFinalizadoDetalhado.class, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasDetalhadasJaRegistradas = new HashMap<String, UrgenciaFinalizadoDetalhado>();
		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFinalizadoDetalhado urgencia : listaUrgenciasPorHora)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData(), "dd/MM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasDetalhadasJaRegistradas.put(urgencia.getData() + urgencia.getSolicitante() + urgencia.getRecurso() + urgencia.getFicha() + urgencia.getHorasDeEspera(), urgencia);
			
			linhaArquivo++;
		}
		
		//Vagas zero
    	ArrayList<UrgenciaVagaZero> listaVagasZero = new ArrayList<>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaVagasZero = ExcelBinder.readSheet(in, UrgenciaVagaZero.class, ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasVagaZero = new HashMap<String, UrgenciaVagaZero>();
		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaVagaZero urgencia : listaVagasZero)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData(), "dd/MM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasVagaZero.put(urgencia.getData() + urgencia.getExecutante() + urgencia.getRecurso() + urgencia.getFicha(), urgencia);
			
			linhaArquivo++;
		}
		
		//Formas de Resolução
    	ArrayList<UrgenciaFormaResolucao> listaFormasDeResolucao = new ArrayList<>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaFormasDeResolucao = ExcelBinder.readSheet(in, UrgenciaFormaResolucao.class, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasFormaDeResolucao = new HashMap<String, UrgenciaFormaResolucao>();
		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFormaResolucao urgencia : listaFormasDeResolucao)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData(), "dd/MM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasFormaDeResolucao.put(urgencia.getData() + urgencia.getFormaDeResolucao() + urgencia.getSolicitante() + urgencia.getExecutante() + urgencia.getLocalDeRegulacao() + urgencia.getRecurso() + urgencia.getFicha(), urgencia);
			
			linhaArquivo++;
		}
		
		//Produção Regulador Detalhada
    	ArrayList<UrgenciaProducaoRegulador> listaProducaoRegulador = new ArrayList<>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaProducaoRegulador = ExcelBinder.readSheet(in, UrgenciaProducaoRegulador.class, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasProducaoRegulador = new HashMap<String, UrgenciaProducaoRegulador>();
		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaProducaoRegulador urgencia : listaProducaoRegulador)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData(), "dd/MM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasProducaoRegulador.put(urgencia.getData() + urgencia.getRegulador() + urgencia.getExecutante() + urgencia.getRecurso() + urgencia.getFicha(), urgencia);
			
			linhaArquivo++;
		}
		
		//Produção Regulador Detalhada Mensal
    	ArrayList<UrgenciaProducaoReguladorMensal> listaProducaoReguladorMensal = new ArrayList<>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaProducaoReguladorMensal = ExcelBinder.readSheet(in, UrgenciaProducaoReguladorMensal.class, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasProducaoreguladorMensal = new HashMap<String, UrgenciaProducaoReguladorMensal>();
		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaProducaoReguladorMensal urgencia : listaProducaoReguladorMensal)
		{
			urgencia.setCompetencia(normalizarDataParaDiaMesAno(urgencia.getCompetencia(), "MMM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasProducaoreguladorMensal.put(urgencia.getCompetencia() + urgencia.getRegulador(), urgencia);
			
			linhaArquivo++;
		}
		
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
					
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());

		ArrayList<String> opcoes = new ArrayList<String>();
		opcoes.add("Relatório");
		opcoes.add("Produtividade");
		
		boolean visivel;
		do
		{
		
			visivel = acessarMenu(driver, paginaWeb, opcoes);
			
		
		}while(!visivel);
		

		paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_PRODUTIVIDADE_URGENCIA_DATA_INICIAL.getTextoIdentificador(), dataFormatada.replaceAll("-", ""));
		paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_PRODUTIVIDADE_URGENCIA_DATA_FINAL.getTextoIdentificador(), dataFormatada.replaceAll("-", ""));
		
		String[] opcoesTranferenciaPelaRegulacao = new String[2];
		opcoesTranferenciaPelaRegulacao[0] = IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_PRODUTIVIDADE_URGENCIA_TRANSFERIDOS_PELA_REGULACAO.getTextoIdentificador();
		opcoesTranferenciaPelaRegulacao[1] = IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_PRODUTIVIDADE_URGENCIA_NAO_TRANSFERIDOS_PELA_REGULACAO.getTextoIdentificador();
		
	
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		HashMap<String, ArrayList<IntervalosUrgencia>> urgenciasAgrupadas = new HashMap<String, ArrayList<IntervalosUrgencia>>();
		HashMap<String, Integer> urgenciasDetalhadas = new HashMap<String, Integer>();
		HashMap<String, DadosAcumuladosVagaZero> vagasZero = new HashMap<String, DadosAcumuladosVagaZero>();
		HashMap<String, Integer> urgenciasFormaDeResolucao = new HashMap<String, Integer>();
		HashMap<String, Integer> urgenciasProducaoRegulador = new HashMap<String, Integer>();
		HashMap<String, Integer> urgenciasProducaoReguladorMensal = new HashMap<String, Integer>();
		
		for(String opcaoRegulacao : opcoesTranferenciaPelaRegulacao)
		{
			while(!paginaWeb.compararValorAtributoCSSPorXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_PRODUTIVIDADE_URGENCIA_DIV_AGUARDANDO.getTextoIdentificador(), "display", "none"));
			
			paginaWeb.clicarLinkPeloXPath(driver, opcaoRegulacao);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Arquivo arquivo = baixarArquivos(driver, paginaWeb, ultimoRecente);
			
			System.out.println("Abrindo: " + arquivo.getCaminhoCompleto());

			montarDadosDeUrgenciaFinalizados(arquivo.getCaminhoCompleto(), dataInformada, urgenciasAgrupadas, urgenciasDetalhadas, vagasZero, urgenciasFormaDeResolucao, urgenciasProducaoRegulador);
			
			arquivo.apagar();
		}
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String textoDataFinalizacao = dataInformada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		montarPlanilhaUrgenciaAgrupada(dataInformada, textoDataFinalizacao, urgenciasAgrupadas);
		montarPlanilhaUrgenciaDetalhada(dataInformada, textoDataFinalizacao, urgenciasDetalhadas);		
		montarPlanilhaVagaZero(dataInformada, textoDataFinalizacao, vagasZero);
		montarPlanilhaFormaDeResolucao(dataInformada, textoDataFinalizacao, urgenciasFormaDeResolucao);
		montarPlanilhaProducaoRegulador(dataInformada, textoDataFinalizacao, urgenciasProducaoRegulador);
		
		montarDadosProducaoReguladorMensal(urgenciasProducaoReguladorMensal);
		montarPlanilhaProducaoReguladorMensal(dataInformada, urgenciasProducaoReguladorMensal);
		
		ordenarPlanilhaAgrupada();
		ordenarPlanilhaDetalhada();
		ordenarPlanilhaVagaZero();
		ordenarPlanilhaFormasDeResolucao();
		ordenarPlanilhaProducaoRegulador();
		ordenarPlanilhaProducaoReguladorMensal();
		//atualizarCopiaOriginalRelatorioProducao();
		//copiarRelatorioProducaoParaCDIDR();
		//copiarRelatorioProducaoParaCDRA();
		
		return "";	
	}
	
	private Arquivo baixarArquivos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String ultimoRecente) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_PRODUTIVIDADE_URGENCIA_BOTAO_EXPORTAR.getTextoIdentificador());
		
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
		}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao()));
			
		Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);

		return arquivo;
	}
	
	private ArrayList<IntervalosUrgencia> criarEstruturaDeIntervalosDeUrgencia()
	{
		ArrayList<IntervalosUrgencia> intervalos = new ArrayList<IntervalosUrgencia>();
		
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_0_6_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_0_6_HORAS.getIndice(), -1, 6));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_6_12_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_6_12_HORAS.getIndice(), 6, 12));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_12_24_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_12_24_HORAS.getIndice(), 12, 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_24_48_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_24_48_HORAS.getIndice(), 24, 48));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_2_3_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_2_3_DIAS.getIndice(), 2 * 24, 3 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_3_5_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_3_5_DIAS.getIndice(), 3 * 24, 5 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_5_7_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_5_7_DIAS.getIndice(), 5 * 24, 7 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_7_10_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_7_10_DIAS.getIndice(), 7 * 24, 10 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_10_13_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_10_13_DIAS.getIndice(), 10 * 24, 13 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_13_15_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_13_15_DIAS.getIndice(), 13 * 24, 15 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_15_17_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_15_17_DIAS.getIndice(), 15 * 24, 17 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_17_20_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_17_20_DIAS.getIndice(), 17 * 24, 20 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_20_25_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_20_25_DIAS.getIndice(), 20 * 24, 25 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_25_30_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_25_30_DIAS.getIndice(), 25 * 24, 30 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getIndice(), 30 * 24, Integer.MAX_VALUE));
		
		return intervalos;
	}
	
	private String montarDadosDeUrgenciaFinalizados(String caminhoArquivo, LocalDate dataHoraDeExtracao, HashMap<String, ArrayList<IntervalosUrgencia>> urgenciasAgrupadas, HashMap<String, Integer> urgenciasDetalhadas, HashMap<String, DadosAcumuladosVagaZero> vagasZero, HashMap<String, Integer> urgenciasFormaDeResolucao, HashMap<String, Integer> urgenciasProducaoRegulador)
	{
		
		String textoDataFinalizacao = dataHoraDeExtracao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		Reader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(caminhoArquivo), StandardCharsets.ISO_8859_1);


			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
			
			Iterable<CSVRecord> registros;
			registros = format.parse(reader);

			for(CSVRecord registro : registros)						
			{
				String entidadeExecutante = registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_UNIDADE_EXECUTANTE.getIndice());
				
				//Finalizações por unidade
				if(entidadesFinalizacaoUrgencia.contains(entidadeExecutante))
				{
					String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + 
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
					
					LocalDateTime dataHoraSolicitado = LocalDateTime.parse(registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_DATA_HORA_SOLICITACAO.getIndice()), DateTimeFormatter.ofPattern(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_DATA_HORA_SOLICITACAO.getFormato()));
					LocalDateTime dataHoraFinalizado = LocalDateTime.parse(registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_DATA_HORA_FINALIZACAO.getIndice()), DateTimeFormatter.ofPattern(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_DATA_HORA_FINALIZACAO.getFormato()));
					
					Duration tempoEntreDatas = Duration.between(dataHoraSolicitado, dataHoraFinalizado);
					
					double horas = 1.0 * tempoEntreDatas.toMinutes() / 60;
					int horasExatas = (int)Math.ceil(horas);
					
					ArrayList<IntervalosUrgencia> intervalos;
					
					if(urgenciasAgrupadas.containsKey(textoMapa))
					{
						intervalos = urgenciasAgrupadas.get(textoMapa);
					}
					else
					{
						intervalos = criarEstruturaDeIntervalosDeUrgencia();
						urgenciasAgrupadas.put(textoMapa, intervalos);
					}
					
					for(IntervalosUrgencia intervalo : intervalos)
						if(horas > intervalo.getInicioIntervalo() && horas <= intervalo.getFinalIntervalo())
							intervalo.incrementarQuantidade();
					
					textoMapa += ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + horasExatas;
					
					if(urgenciasDetalhadas.containsKey(textoMapa))
					{
						int quantidade = urgenciasDetalhadas.get(textoMapa);
						quantidade++;
						urgenciasDetalhadas.put(textoMapa, quantidade);
					}
					else
					{
						urgenciasDetalhadas.put(textoMapa, 1);
					}
				}

				String formaDeResolucao = registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_FORMA_DE_RESOLUÇÃO.getIndice()).trim();
				//Vagas Zero
				if(entidadesVagaZero.contains(entidadeExecutante))
				{
					String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao() + 
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_UNIDADE_EXECUTANTE.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
					
					DadosAcumuladosVagaZero vagaZero;
					
					if(vagasZero.containsKey(textoMapa))
					{
						vagaZero = vagasZero.get(textoMapa);
					}
					else
					{
						vagaZero = new DadosAcumuladosVagaZero();
						vagasZero.put(textoMapa, vagaZero);
					}
					
					if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_VAGA_ZERO.getDescricao()))
					{
						vagaZero.incrementarVagaZero();
						vagaZero.incrementarTotal();
					}
					else if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getDescricao()))
					{
						vagaZero.incrementarEncaminhadoParaAvaliacaoDeComplexidadeAdequada();
						vagaZero.incrementarTotal();
					}
					else if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getDescricao()))
					{
						vagaZero.incrementarEncaminhadoParaReferenciaPactuada();
						vagaZero.incrementarTotal();
					}
					else if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_AUTOMATICAMENTE_PAR_REFERENCIA_PACTUADA.getDescricao()))
					{
						vagaZero.incrementarEncaminhadoAutomaticamenteParaReferenciaPactuada();
						vagaZero.incrementarTotal();
					}
				}
				
				String localDeRegulacao = registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_LOCAL_REGULACAO.getIndice()).trim();
				
				if(localDeRegulacao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_CENTRAL_MUNICIPAL_REGULACAO_CAMPINAS.getDescricao()) || entidadesFinalizacaoUrgencia.contains(entidadeExecutante))
				{
					String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + 
							formaDeResolucao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
					
					if(entidadeExecutante.equals(""))
						textoMapa += ParametrosArquivoUrgenciaPlanilhaFormaResolucao.TEXTO_EXECUTANTE_VAZIO.getDescricao() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
					else
						textoMapa += entidadeExecutante + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
					
					textoMapa += localDeRegulacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
					
					if(urgenciasFormaDeResolucao.containsKey(textoMapa))
					{
						int quantidade = urgenciasFormaDeResolucao.get(textoMapa);
						quantidade++;
						urgenciasFormaDeResolucao.put(textoMapa, quantidade);
					}
					else
					{
						urgenciasFormaDeResolucao.put(textoMapa, 1);
					}
				}
				
				if(localDeRegulacao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_CENTRAL_MUNICIPAL_REGULACAO_CAMPINAS.getDescricao()))
				{
					String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + 
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_REGULADOR_FINAl.getIndice()) + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
					
					if(entidadeExecutante.equals(""))
						textoMapa += ParametrosArquivoUrgenciaPlanilhaFormaResolucao.TEXTO_EXECUTANTE_VAZIO.getDescricao() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
					else
						textoMapa += entidadeExecutante + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
					
					textoMapa += registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
							registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
					
					if(urgenciasProducaoRegulador.containsKey(textoMapa))
					{
						int quantidade = urgenciasProducaoRegulador.get(textoMapa);
						quantidade++;
						urgenciasProducaoRegulador.put(textoMapa, quantidade);
					}
					else
					{
						urgenciasProducaoRegulador.put(textoMapa, 1);
					}
				}
				
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public String montarDadosProducaoReguladorMensal(HashMap<String, Integer> producaoReguladorMensal)
	{
		ArrayList<UrgenciaProducaoRegulador> listaUrgencias = new ArrayList<UrgenciaProducaoRegulador>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaProducaoRegulador.class, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
	
		for(UrgenciaProducaoRegulador urgencia : listaUrgencias)
		{
			String competencia = normalizarDataParaDiaMesAno(urgencia.getData(), "MMM/yyyy");

			String chave = competencia + ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.DIVISOR_CAMPOS.getDescricao() + urgencia.getRegulador();
			if(producaoReguladorMensal.containsKey(chave))
			{
				int quantidade = producaoReguladorMensal.get(chave);
				quantidade += Integer.parseInt(urgencia.getQuantidade());
				producaoReguladorMensal.put(chave, quantidade);
			}
			else
			{
				int quantidade = Integer.parseInt(urgencia.getQuantidade());
				producaoReguladorMensal.put(chave, quantidade);
			}
		}
		
		return "";
	}
	
	private String montarPlanilhaUrgenciaAgrupada(LocalDate dataHoraDeExtracao, String textoDataExtracao, HashMap<String, ArrayList<IntervalosUrgencia>> urgenciasAgrupadas)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : urgenciasAgrupadas.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao(), "");
			
			UrgenciaFinalizadoAgrupado urgencia;
			int linha;
			if(urgenciasAgrupadasJaRegistradas.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasAgrupadasJaRegistradas.get(chaveJaRegistrada);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				urgencia = new UrgenciaFinalizadoAgrupado();
				urgencia.setData(textoDataExtracao);
				urgencia.setSolicitante(chave.split(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao())[1]);
				urgencia.setRecurso(chave.split(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao())[2]);
				urgencia.setFicha(chave.split(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao())[3]);
				
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
				urgenciasAgrupadasJaRegistradas.put(chaveJaRegistrada, urgencia);
				
			}
			
			ArrayList<IntervalosUrgencia> intervalos = urgenciasAgrupadas.get(chave);
			int totalGeral = 0;
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
			
			for(IntervalosUrgencia intervalo : intervalos)
			{
				totalGeral += intervalo.getQuantidade();
				
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), intervalo.getIndiceTabela(), intervalo.getQuantidade(), "Integer"));
			}
			
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_TOTAL_GERAL.getIndice(), totalGeral, "Integer"));
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String montarPlanilhaUrgenciaDetalhada(LocalDate dataHoraDeExtracao, String textoDataExtracao, HashMap<String, Integer> urgenciasAgrupadas)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : urgenciasAgrupadas.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.DIVISOR_CAMPOS.getDescricao(), "");
			
			UrgenciaFinalizadoDetalhado urgencia;
			int linha;
			
			System.out.println(chaveJaRegistrada);
			if(urgenciasDetalhadasJaRegistradas.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasDetalhadasJaRegistradas.get(chaveJaRegistrada);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				urgencia = new UrgenciaFinalizadoDetalhado();
				urgencia.setData(textoDataExtracao);
				urgencia.setSolicitante(chave.split(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.DIVISOR_CAMPOS.getDescricao())[1]);
				urgencia.setRecurso(chave.split(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.DIVISOR_CAMPOS.getDescricao())[2]);
				urgencia.setFicha(chave.split(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.DIVISOR_CAMPOS.getDescricao())[3]);
				urgencia.setHorasDeEspera(chave.split(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.DIVISOR_CAMPOS.getDescricao())[4]);
				
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
				urgenciasDetalhadasJaRegistradas.put(chaveJaRegistrada, urgencia);
				
			}
			
			int quantidadeEmEspera = urgenciasAgrupadas.get(chave);
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_HORAS_DE_ESPERA.getIndice(), Integer.parseInt(urgencia.getHorasDeEspera()), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_QUANTIDADE.getIndice(), quantidadeEmEspera, "Integer"));
			
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String montarPlanilhaVagaZero(LocalDate dataHoraDeExtracao, String textoDataExtracao, HashMap<String, DadosAcumuladosVagaZero> vagasZero)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : vagasZero.keySet())
		{
			DadosAcumuladosVagaZero dadosVagaZero = vagasZero.get(chave);
			
			if(dadosVagaZero.getTotal() > 0)
			{
			
				String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao(), "");
				
				UrgenciaVagaZero urgencia;
				int linha;
				if(urgenciasVagaZero.containsKey(chaveJaRegistrada))
				{
					urgencia = urgenciasVagaZero.get(chaveJaRegistrada);
					urgencia.setLinhaUtilizada(true);
					
					linha = urgencia.getLinhaExcel();
				}
				else
				{
					urgencia = new UrgenciaVagaZero();
					urgencia.setData(textoDataExtracao);
					urgencia.setExecutante(chave.split(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao())[1]);
					urgencia.setRecurso(chave.split(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao())[2]);
					urgencia.setFicha(chave.split(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao())[3]);
					
					linhaArquivo++;
					linha = linhaArquivo;
					urgencia.setLinhaExcel(linha);
					
					urgenciasVagaZero.put(chaveJaRegistrada, urgencia);
					
				}
				
				//System.out.println(textoDataExtracao);
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), "String"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_TOTAL.getIndice(), dadosVagaZero.getTotal(), "Integer"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_VAGA_ZERO.getIndice(), dadosVagaZero.getVagaZero(), "Integer"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getIndice(), dadosVagaZero.getEncaminhadoParaReferenciaPactuada(), "Integer"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getIndice(), dadosVagaZero.getEncaminhadoParaAvaliacaoDeComplexidadeAdequada(), "Integer"));
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_AUTOMATICAMENTE_PARA_REFERENCIA_PACTUADA.getIndice(), dadosVagaZero.getEncaminhadoAutomaticamenteParaReferenciaPactuada(), "Integer"));
			}
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String montarPlanilhaFormaDeResolucao(LocalDate dataHoraDeExtracao, String textoDataExtracao, HashMap<String, Integer> formasDeResolucao)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : formasDeResolucao.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.DIVISOR_CAMPOS.getDescricao(), "").replace(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.TEXTO_EXECUTANTE_VAZIO.getDescricao(), "");
			
			UrgenciaFormaResolucao urgencia;
			int linha;
			
			System.out.println(chaveJaRegistrada);
			if(urgenciasFormaDeResolucao.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasFormaDeResolucao.get(chaveJaRegistrada);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				String[] componentesChave = chave.split(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.DIVISOR_CAMPOS.getDescricao());
				
				urgencia = new UrgenciaFormaResolucao();
				urgencia.setData(textoDataExtracao);
				urgencia.setFormaDeResolucao(componentesChave[1]);
				urgencia.setSolicitante(componentesChave[2]);
				
				if(componentesChave[3].equals(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.TEXTO_EXECUTANTE_VAZIO.getDescricao()))
					urgencia.setExecutante("");
				else
					urgencia.setExecutante(componentesChave[3]);
				
				urgencia.setLocalDeRegulacao(componentesChave[4]);
				urgencia.setRecurso(componentesChave[5]);
				urgencia.setFicha(componentesChave[6]);
				
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
				urgenciasFormaDeResolucao.put(chaveJaRegistrada, urgencia);
				
			}
			
			int quantidade = formasDeResolucao.get(chave);
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_FORMA_DE_RESOLUCAO.getIndice(), urgencia.getFormaDeResolucao(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_LOCAL_DE_REGULACAO.getIndice(), urgencia.getLocalDeRegulacao(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_QUANTIDADE.getIndice(), quantidade, "Integer"));
			
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String montarPlanilhaProducaoRegulador(LocalDate dataHoraDeExtracao, String textoDataExtracao, HashMap<String, Integer> producaoRegulador)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : producaoRegulador.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.DIVISOR_CAMPOS.getDescricao(), "").replace(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.TEXTO_EXECUTANTE_VAZIO.getDescricao(), "");
			
			UrgenciaProducaoRegulador urgencia;
			int linha;
			
			System.out.println(chaveJaRegistrada);
			if(urgenciasProducaoRegulador.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasProducaoRegulador.get(chaveJaRegistrada);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				String[] componentesChave = chave.split(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.DIVISOR_CAMPOS.getDescricao());
				
				urgencia = new UrgenciaProducaoRegulador();
				urgencia.setData(textoDataExtracao);
				urgencia.setRegulador(componentesChave[1]);
				
				if(componentesChave[2].equals(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.TEXTO_EXECUTANTE_VAZIO.getDescricao()))
					urgencia.setExecutante("");
				else
					urgencia.setExecutante(componentesChave[2]);
				
				urgencia.setRecurso(componentesChave[3]);
				urgencia.setFicha(componentesChave[4]);
				
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
				urgenciasProducaoRegulador.put(chaveJaRegistrada, urgencia);
				
			}
			
			int quantidade = producaoRegulador.get(chave);
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_QUANTIDADE.getIndice(), quantidade, "Integer"));
			
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String montarPlanilhaProducaoReguladorMensal(LocalDate dataHoraDeExtracao, HashMap<String, Integer> producaoRegulador)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : producaoRegulador.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.DIVISOR_CAMPOS.getDescricao(), "");
			
			UrgenciaProducaoReguladorMensal urgencia;
			int linha;
			
			System.out.println(chaveJaRegistrada);
			if(urgenciasProducaoreguladorMensal.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasProducaoreguladorMensal.get(chaveJaRegistrada);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				String[] componentesChave = chave.split(ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.DIVISOR_CAMPOS.getDescricao());
				
				urgencia = new UrgenciaProducaoReguladorMensal();
				urgencia.setCompetencia(componentesChave[0]);
				urgencia.setRegulador(componentesChave[1]);
								
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
				urgenciasProducaoreguladorMensal.put(chaveJaRegistrada, urgencia);
				
			}
			
			int quantidade = producaoRegulador.get(chave);
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_COMPENTENCIA.getIndice(), urgencia.getCompetencia(), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_QUANTIDADE.getIndice(), quantidade, "Integer"));
			
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public boolean acessarMenu(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, ArrayList<String> opcoes)
	{
		paginaWeb.voltarAoTopoDaPagina(driver);	
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
		
		boolean visivel;
		do
		{
			//buscando arquivos e baixando
			paginaWeb.voltarAoTopoDaPagina(driver);
		
			//visivel = paginaWeb.clicarMenuUL(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes);
		
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			visivel = paginaWeb.clicarMenuUL(driver, 2, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes, OpenStrategy.HOVER);
			
		
		}while(!visivel);
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return visivel;
	}
	
	private static String normalizarDataParaDiaMesAno(String valor, String formato) {
	    if (valor == null || valor.isBlank())
	        return null;
	
	    DateTimeFormatter fmtMesAno = DateTimeFormatter.ofPattern(formato);
	
	    // 1️ Caso seja número serial do Excel
	    if (valor.matches("\\d+")) {
	        long serial = Long.parseLong(valor);
	        LocalDate data = LocalDate.of(1899, 12, 30).plusDays(serial); // Ajuste Excel
	        return fmtMesAno.format(data);
	    }
	
	    // 2️ Caso seja dd/MM/yyyy
	    try {
	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
	        return fmtMesAno.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e tenta o próximo formato
	    }
	    
	    // 3️ Caso seja M/d/yy
	    try {
	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("M/d/yy");
	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
	        return fmtMesAno.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e tenta o próximo formato
	    }
	
	    // 4 Caso seja mmm/yyyy (direto do Excel ou do POI)
	    try {
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy");
	        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy"));
	        return fmtMesAno.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	
	    throw new IllegalArgumentException("Formato de data inválido: " + valor);
	}
	
	private static String normalizarDataParaAnoMesDia(String valor) {
	    if (valor == null || valor.isBlank())
	        return null;
	
	    Locale localeBR = Locale.of("pt", "BR"); // Java 21
	    DateTimeFormatter fmtAnoMes = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	    // 1️ Caso seja número serial do Excel
	    if (valor.matches("\\d+")) {
	        long serial = Long.parseLong(valor);
	        LocalDate data = LocalDate.of(1899, 12, 30).plusDays(serial); // Ajuste Excel
	        return fmtAnoMes.format(data);
	    }
	
	    // 2️ Caso seja dd/MM/yyyy
	    try {
	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
	        return fmtAnoMes.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e tenta o próximo formato
	    }
	
	    // 3️ Caso seja mmm/yyyy (direto do Excel ou do POI)
	    try {
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
	        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy", localeBR));
	        return fmtAnoMes.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	
	    throw new IllegalArgumentException("Formato de data inválido: " + valor);
	}
	
	public String ordenarPlanilhaAgrupada()
	{
		ArrayList<UrgenciaFinalizadoAgrupado> listaUrgencias = new ArrayList<UrgenciaFinalizadoAgrupado>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFinalizadoAgrupado.class, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFinalizadoAgrupado urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao, "dd/MM/yyyy"));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaFinalizadoAgrupado::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaFinalizadoAgrupado::getSolicitante)
		    .thenComparing(UrgenciaFinalizadoAgrupado::getRecurso)
		    .thenComparing(UrgenciaFinalizadoAgrupado::getFicha)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaFinalizadoAgrupado urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_SOLICITANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_RECURSO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_FICHA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_TOTAL_GERAL.getIndice(), urgencia.getTotalGeral(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_TOTAL_GERAL.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_0_6_HORAS.getIndice(), urgencia.getPeriodo_0_6_horas(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_0_6_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_6_12_HORAS.getIndice(), urgencia.getPeriodo_6_12_horas(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_6_12_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_12_24_HORAS.getIndice(), urgencia.getPeriodo_12_24_horas(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_12_24_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_24_48_HORAS.getIndice(), urgencia.getPeriodo_24_48_horas(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_24_48_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_2_3_DIAS.getIndice(), urgencia.getPeriodo_2_3_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_2_3_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_3_5_DIAS.getIndice(), urgencia.getPeriodo_3_5_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_3_5_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_5_7_DIAS.getIndice(), urgencia.getPeriodo_5_7_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_5_7_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_7_10_DIAS.getIndice(), urgencia.getPeriodo_7_10_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_7_10_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_10_13_DIAS.getIndice(), urgencia.getPeriodo_10_13_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_10_13_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_13_15_DIAS.getIndice(), urgencia.getPeriodo_13_15_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_13_15_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_15_17_DIAS.getIndice(), urgencia.getPeriodo_15_17_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_15_17_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_17_20_DIAS.getIndice(), urgencia.getPeriodo_17_20_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_17_20_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_20_25_DIAS.getIndice(), urgencia.getPeriodo_20_25_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_20_25_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_25_30_DIAS.getIndice(), urgencia.getPeriodo_25_30_dias(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_25_30_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getIndice(), urgencia.getPeriodo_30_dias_acima(), ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getTipo()));
			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaDetalhada()
	{
		ArrayList<UrgenciaFinalizadoDetalhado> listaUrgencias = new ArrayList<UrgenciaFinalizadoDetalhado>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFinalizadoDetalhado.class, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFinalizadoDetalhado urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao, "dd/MM/yyyy"));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
			
			urgencia.setHorasDeEsperaOrdenacao(Integer.parseInt(urgencia.getHorasDeEspera()));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaFinalizadoDetalhado::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaFinalizadoDetalhado::getSolicitante)
		    .thenComparing(UrgenciaFinalizadoDetalhado::getRecurso)
		    .thenComparing(UrgenciaFinalizadoDetalhado::getFicha)
		    .thenComparing(UrgenciaFinalizadoDetalhado::getHorasDeEsperaOrdenacao)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaFinalizadoDetalhado urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_SOLICITANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_RECURSO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_FICHA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_HORAS_DE_ESPERA.getIndice(), urgencia.getHorasDeEspera(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_HORAS_DE_ESPERA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_QUANTIDADE.getIndice(), urgencia.getQuantidade(), ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.INDICE_COLUNA_QUANTIDADE.getTipo()));

			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaVagaZero()
	{
		ArrayList<UrgenciaVagaZero> listaUrgencias = new ArrayList<UrgenciaVagaZero>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaVagaZero.class, ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaVagaZero urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao, "dd/MM/yyyy"));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaVagaZero::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaVagaZero::getExecutante)
		    .thenComparing(UrgenciaVagaZero::getRecurso)
		    .thenComparing(UrgenciaVagaZero::getFicha)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaVagaZero urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_EXECUTANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_RECURSO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_FICHA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_TOTAL.getIndice(), urgencia.getTotal(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_TOTAL.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_VAGA_ZERO.getIndice(), urgencia.getVagaZero(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_VAGA_ZERO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getIndice(), urgencia.getEncaminhadoParaReferenciaPactuada(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getIndice(), urgencia.getEncaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_AUTOMATICAMENTE_PARA_REFERENCIA_PACTUADA.getIndice(), urgencia.getEncaminhadoAutomaticamenteParaReferenciaPactuada(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_AUTOMATICAMENTE_PARA_REFERENCIA_PACTUADA.getTipo()));

			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaFormasDeResolucao()
	{
		ArrayList<UrgenciaFormaResolucao> listaUrgencias = new ArrayList<UrgenciaFormaResolucao>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFormaResolucao.class, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFormaResolucao.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFormaResolucao urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao, "dd/MM/yyyy"));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaFormaResolucao::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaFormaResolucao::getFormaDeResolucao)
		    .thenComparing(UrgenciaFormaResolucao::getSolicitante)
		    .thenComparing(UrgenciaFormaResolucao::getExecutante)
		    .thenComparing(UrgenciaFormaResolucao::getLocalDeRegulacao)
		    .thenComparing(UrgenciaFormaResolucao::getRecurso)
		    .thenComparing(UrgenciaFormaResolucao::getFicha)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaFormaResolucao urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_FORMA_DE_RESOLUCAO.getIndice(), urgencia.getFormaDeResolucao(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_FORMA_DE_RESOLUCAO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_SOLICITANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_EXECUTANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_LOCAL_DE_REGULACAO.getIndice(), urgencia.getLocalDeRegulacao(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_LOCAL_DE_REGULACAO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_RECURSO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_FICHA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_QUANTIDADE.getIndice(), urgencia.getQuantidade(), ParametrosArquivoUrgenciaPlanilhaFormaResolucao.INDICE_COLUNA_QUANTIDADE.getTipo()));			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFormaResolucao.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFormaResolucao.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaProducaoRegulador()
	{
		ArrayList<UrgenciaProducaoRegulador> listaUrgencias = new ArrayList<UrgenciaProducaoRegulador>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaProducaoRegulador.class, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaProducaoRegulador urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao, "dd/MM/yyyy"));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaProducaoRegulador::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaProducaoRegulador::getRegulador)
		    .thenComparing(UrgenciaProducaoRegulador::getExecutante)
		    .thenComparing(UrgenciaProducaoRegulador::getRecurso)
		    .thenComparing(UrgenciaProducaoRegulador::getFicha)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaProducaoRegulador urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_REGULADOR.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_EXECUTANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_RECURSO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_FICHA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_QUANTIDADE.getIndice(), urgencia.getQuantidade(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.INDICE_COLUNA_QUANTIDADE.getTipo()));			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaProducaoReguladorMensal()
	{
		ArrayList<UrgenciaProducaoReguladorMensal> listaUrgencias = new ArrayList<UrgenciaProducaoReguladorMensal>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaProducaoReguladorMensal.class, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaProducaoReguladorMensal urgencia : listaUrgencias)
		{
			String competencia = urgencia.getCompetencia();
			urgencia.setCompetencia(normalizarDataParaDiaMesAno(competencia, "MMM/yyyy"));
			urgencia.setCompetenciaOrdenacao(normalizarDataParaAnoMesDia(competencia));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaProducaoReguladorMensal::getCompetenciaOrdenacao).reversed()
		    .thenComparing(UrgenciaProducaoReguladorMensal::getRegulador)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaProducaoReguladorMensal urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_COMPENTENCIA.getIndice(), urgencia.getCompetencia(), ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_COMPENTENCIA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_REGULADOR.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_QUANTIDADE.getIndice(), urgencia.getQuantidade(), ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.INDICE_COLUNA_QUANTIDADE.getTipo()));			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private CelulaExcel criarCelula(int linha, int coluna, String valor, String tipo)
	{
		CelulaExcel celula = null;
		
		if(tipo.equals("String"))
			celula = new CelulaExcel(linha, coluna, valor, tipo);
		else if(tipo.equals("Int"))
		{
			try
			{
				int valorInteiro = Integer.parseInt(valor);
				celula = new CelulaExcel(linha, coluna, valorInteiro, tipo);
			}
			catch(NumberFormatException e)
			{
				celula = new CelulaExcel(linha, coluna, valor, "String");
			}
		}
		else if(tipo.equals("Porcentagem"))
		{
			try
			{
				String valorReal = valor.replace("%", "").replace(",", ".");
				Double valorPorcentagem = Double.parseDouble(valorReal)/100;
				celula = new CelulaExcel(linha, coluna, valorPorcentagem, tipo);
			}
			catch(NumberFormatException e)
			{
				celula = new CelulaExcel(linha, coluna, valor, "String");
			}
		}
		else if(tipo.equals("Date"))
		{
			 try 
			 {
		        LocalDate data = LocalDate.parse(valor, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		        celula = new CelulaExcel(linha, coluna, data, tipo);
		        
			 } catch (DateTimeParseException e) {
		    	celula = new CelulaExcel(linha, coluna, valor, "String");
		    }
		}
		else if(tipo.equals("Time"))
		{
			 try 
			 {
		        LocalTime horario = LocalTime.parse(valor, DateTimeFormatter.ofPattern("HH:mm:ss"));

		        celula = new CelulaExcel(linha, coluna, horario, tipo);
		        
			 } catch (DateTimeParseException e) {
		    	celula = new CelulaExcel(linha, coluna, valor, "String");
		    }
		}
		else if(tipo.equals("Date mes/ano"))
		{
			 try 
			 {
				Locale localeBR = Locale.of("pt", "BR"); // Java 21
		        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy", localeBR));

		        celula = new CelulaExcel(linha, coluna, data, tipo);
		        
			 } catch (DateTimeParseException e) {
		    	celula = new CelulaExcel(linha, coluna, valor, "String");
		    }
		}
		
		return celula;
	}
	
}
