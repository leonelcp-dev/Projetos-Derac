package modulosGEFIC;


import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;

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
import dadosGerais.ParametrosArquivoConsolidadoGEFIC;
import dadosGerais.ParametrosArquivoGEFICEntradaPacientes;
import dadosGerais.ParametrosArquivoGEFICSaidaPacientes;
import dadosGerais.ParametrosArquivoGEFICSaidaPacientesAnalitico;
import dadosGerais.ParametrosArquivoGEFICTransferenciaPacientes;
import dadosGerais.ParametrosArquivoLeitosPlanilhaMonitoramento;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFormaResolucao;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaVagaZero;
import dadosGerais.ParametrosArquivoUrgenciaRelatorioProdutividade;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado;
import dadosGerais.ParametrosTabelaUrgenciaSolicitacoesPendentes;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.ExcelBinder;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import modelosDados.CelulaExcel;
import modelosDados.DadoAnaliticoSaidaPacienteGEFIC;
import modelosDados.DadosAcumuladosVagaZero;
import modelosDados.EntidadeGEFIC;
import modelosDados.IntervalosUrgencia;
import modelosDados.OfertaEDemanda;
import modelosDados.StatusNormalizadosGEFIC;
import modelosDados.UrgenciaAguardandoDetalhado;
import modelosDados.UrgenciaFinalizadoAgrupado;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;
import modelosDados.UrgenciaAguardandoAgrupado;

public class ConsolidadoGEFIC 
{
	private String pastaBaseAmbulatorialCDIDR;
	private String pastaBase;
	private String pastaDownloads;
	LocalDate dataInicioReferencia;
	LocalDate dataFinalReferencia;
	LocalDate dataInicioCompetencia;
	LocalDate dataFinalCompetencia;
	String dataFormatadaInicioReferencia;
	String dataFormatadaFinalReferencia;
	String dataFormatadaInicioCompetencia;
	String dataFormatadaFinalCompetencia;
	boolean ehOPM;
	boolean jaConsolidouTransferencias;
	HashMap<String, Integer> transferidosPorOrigem;
	HashMap<String, Integer> transferidosPorDestino;
	
	ArrayList<EntidadeGEFIC> entidades;
	private IdentificadoresPastasCompartilhadasCDIDRGEFIC diretoriosCDIDR; 

	public ConsolidadoGEFIC(String pastaBase, String ambiente, boolean ehOPM)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRGEFIC.valueOf(ambiente);
		pastaBaseAmbulatorialCDIDR = pastaBase;
		entidades = new ArrayList<EntidadeGEFIC>();
		this.ehOPM = ehOPM;
		
		transferidosPorOrigem = new HashMap<String, Integer>();
		transferidosPorDestino = new HashMap<String, Integer>();
		
		jaConsolidouTransferencias = false;
	}
	
	public ConsolidadoGEFIC()
	{
		transferidosPorOrigem = new HashMap<String, Integer>();
		transferidosPorDestino = new HashMap<String, Integer>();
		
		jaConsolidouTransferencias = false;
	}
	
	public String gerarArquivoConsolidadoGEFIC(WebDriver driver, String ambiente, String pastaBaseInformada, String pastaDownloads, boolean ehOPM, String competencia)
	{			
		this.ehOPM = ehOPM;
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRGEFIC.valueOf(ambiente.toUpperCase());
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
    	this.pastaBase = pastaBaseInformada;
    	this.pastaDownloads = pastaDownloads;
		
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
		
		HashMap<String, String> deParaNomesEntidades = new HashMap<String, String>();
		HashMap<String, String> deParaSiglasEntidades = new HashMap<String, String>();   
		try {
			Reader reader = new InputStreamReader(new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoEntidades()), StandardCharsets.ISO_8859_1);

			String tipoUnidade;
			if(ehOPM)
			{
				tipoUnidade = IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_OPM.getTextoIdentificador();
			}
			else
			{
				tipoUnidade = IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_CIRURGIA_ELETIVA.getTextoIdentificador();
			}

			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
			
			Iterable<CSVRecord> registros = format.parse(reader);
			
			entidades = new ArrayList<EntidadeGEFIC>();

			for(CSVRecord registro : registros)						
			{
				if(registro.get(2).equals(tipoUnidade))
				{
					EntidadeGEFIC entidade = new EntidadeGEFIC(registro.get(0), registro.get(1), registro.get(2));
					deParaNomesEntidades.put(registro.get(1).toUpperCase(), registro.get(0).toUpperCase());
					deParaSiglasEntidades.put(registro.get(0).toUpperCase(), registro.get(1));
					entidades.add(entidade);
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<StatusNormalizadosGEFIC> listaStatus = new ArrayList<StatusNormalizadosGEFIC>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoStatusNormalizados());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoStatusNormalizados())) {
    		listaStatus = ExcelBinder.readSheet(in, StatusNormalizadosGEFIC.class, 0, 0, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
    	ArrayList<String> statusRealizadosCirurgiaEletiva = new ArrayList<String>();
    	ArrayList<String> statusRealizadosOPM = new ArrayList<String>();
    	
    	HashMap<String, String> deParaStatusCirurgiasEletivas = new HashMap<String, String>();
    	HashMap<String, String> deParaStatusOPM = new HashMap<String, String>();
    	
    	for(StatusNormalizadosGEFIC status : listaStatus)
    	{
    		if(status.getModulo().toUpperCase().equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_CIRURGIA_ELETIVA.getDescricao()))
    		{
    			if(status.getRealizado().equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SIM.getDescricao()))
    				statusRealizadosCirurgiaEletiva.add(status.getStatus());
    			else
    				deParaStatusCirurgiasEletivas.put(status.getStatus(), status.getNormalizado());
    		}
    		else if(status.getModulo().toUpperCase().equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_OPM.getDescricao()))
    		{
    			if(status.getRealizado().equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SIM.getDescricao()))
    				statusRealizadosOPM.add(status.getStatus());
    			else
    				deParaStatusOPM.put(status.getStatus(), status.getNormalizado());
    		}
    			
    	}
		
    	//atualizarQuantidadeGeralDePacientes(paginaWeb, driver, deParaNomesEntidades);
    	//atualizarQuantidadeEntradaSaidaDePacientes(paginaWeb, driver, deParaSiglasEntidades, competencia);
    	
    	if(ehOPM)
    	{
    		atualizarStatusDasSaidasDePacientes(paginaWeb, driver, deParaSiglasEntidades, competencia, statusRealizadosOPM, deParaStatusOPM);
    	}
    	else
    	{
    		atualizarStatusDasSaidasDePacientes(paginaWeb, driver, deParaSiglasEntidades, competencia, statusRealizadosCirurgiaEletiva, deParaStatusCirurgiasEletivas);
    	}
		
		
		//driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
					
//		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
//		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());

		
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						

		//atualizarCopiaOriginalRelatorioProducao();
		//copiarRelatorioProducaoParaCDIDR();
		//copiarRelatorioProducaoParaCDRA();
		
		return "";	
	}
	
	private String atualizarQuantidadeGeralDePacientes(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, HashMap<String, String> deParaNomesEntidades)
	{
		//gerarCopiaTemporariaRelatorioProducao();
		
		LocalDate dataAtual = LocalDate.now();
		
		String mesAtual = (new MesesFormatados()).getMeses().get(dataAtual.getMonthValue() - 1).getMesDescricao();
		
		ArrayList<String> opcoes = new ArrayList<String>();
		
		if(ehOPM)
		{
			opcoes.add("Quantidade de pacientes por serviço OPM");
		}
		else
		{
			opcoes.add("Quantidade de pacientes por serviço");
		}
	
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		ArrayList<ArrayList<String>> tabelaRodape = paginaWeb.obterTableComDivPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_QUANTIDADE_PACIENTES_RODAPE_TABELA.getTextoIdentificador(), IdentificadoresPaginaWebGEFIC.CLASS_NAME_RELATORIO_QUANTIDADE_PACIENTES_LINHA_RODAPE_TABELA.getTextoIdentificador(), "");
		
		for(ArrayList<String> linha : tabelaRodape)
		{
			for(String celula : linha)
				System.out.print(celula + "||\t");
			
			System.out.println("Células: " + linha.size());			
		}
		
		AcoesArquivoExcel arquivoConsolidado;
		if(ehOPM)
			arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoGEFICOPM().replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), String.valueOf(dataAtual.getYear())), 0);
		else
			arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoGEFICCirurgiasEletivas().replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), String.valueOf(dataAtual.getYear())), 0);
		
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_GERAL.getDescricao(), 0);
		
		int colunaExcel = ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice();
		int linhaExcel = ParametrosArquivoConsolidadoGEFIC.INDICE_LINHA_INICIAL_RELATORIOS.getIndice() - 1;
		
		String conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, colunaExcel, "");
		while(!conteudoCelula.equals(mesAtual) && colunaExcel < 20)
		{
			colunaExcel++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, colunaExcel, ""); 
			System.out.println(conteudoCelula);
		}
		
		if(colunaExcel < 20)
		{
			int somaQuantidade = 0;
			ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
			for(ArrayList<String> linha : tabelaRodape)
			{
				if(linha.size() > 1)
				{
					String unidade = deParaNomesEntidades.get(linha.get(0).toUpperCase().trim().replace(":", ""));
					
					if(unidade != null)
					{
						int quantidade = Integer.parseInt(linha.get(1));
						
						linhaExcel = ParametrosArquivoConsolidadoGEFIC.INDICE_LINHA_INICIAL_RELATORIOS.getIndice();
						
						conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
						while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()) && !conteudoCelula.equals(unidade))
						{
							linhaExcel++;
							conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
							
							System.out.println(conteudoCelula);
						}
						
						if(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
						{
							somaQuantidade += quantidade;
							celulas.add(new CelulaExcel(linhaExcel, colunaExcel, quantidade, "Integer"));
						}
					}
				}
			}
			
			linhaExcel = ParametrosArquivoConsolidadoGEFIC.INDICE_LINHA_INICIAL_RELATORIOS.getIndice();
			
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
			{
				String conteudoCelulaDoMes = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, colunaExcel, "").trim();
				if(conteudoCelulaDoMes.equals(""))
					celulas.add(new CelulaExcel(linhaExcel, colunaExcel, 0, ""));
					
				linhaExcel++;
				conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
				
				System.out.println(conteudoCelula);
			}
			
			if(conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
			{
				celulas.add(new CelulaExcel(linhaExcel, colunaExcel, somaQuantidade, "Integer"));
			}
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_GERAL.getDescricao(), celulas, false, false, 0, null);
		}
		
		
		return "";
	}
	
	private String atualizarQuantidadeEntradaSaidaDePacientes(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, HashMap<String, String> deParaSiglasEntidades, String competencia)
	{
		dataFormatadaInicioCompetencia = "01/" + competencia;
		
		dataInicioCompetencia = LocalDate.parse(dataFormatadaInicioCompetencia, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		dataFinalCompetencia = dataInicioCompetencia.with(TemporalAdjusters.lastDayOfMonth());
		dataFormatadaFinalCompetencia = dataFinalCompetencia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		if(!jaConsolidouTransferencias)
		{
			contabilizarTransferencias(paginaWeb, driver, dataFormatadaInicioCompetencia, dataFormatadaFinalCompetencia);
			jaConsolidouTransferencias = true;
		}
		
		HashMap<String, Integer> entradas = new HashMap<String, Integer>();
		HashMap<String, Integer> saidas = new HashMap<String, Integer>();
		contabilizarEntradas(paginaWeb, driver, dataFormatadaInicioCompetencia, dataFormatadaFinalCompetencia, entradas);
		
		for(String entrada : entradas.keySet())
			System.out.println(entrada + ": " + entradas.get(entrada));
		
		contabilizarSaidas(paginaWeb, driver, dataFormatadaInicioCompetencia, dataFormatadaFinalCompetencia, saidas);
		
		for(String saida : saidas.keySet())
			System.out.println(saida + ": " + saidas.get(saida));
		
		preencherPlanilhaEntradaSaida(deParaSiglasEntidades, dataInicioCompetencia, entradas, saidas);
		
		return "";
	}
	
	private ArrayList<CelulaExcel> preencherTabelaPlanilhaEntradaSaida(AcoesArquivoExcel arquivoConsolidado, int linhaInicial, int colunaExcel, HashMap<String, String> deParaSiglasEntidades, HashMap<String, Integer> entradas, HashMap<String, Integer> saidas, String somarOuSubtrairTransferencias)
	{
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		int colunaEntrada = colunaExcel;
		int colunaSaida = colunaEntrada + 1;
		int linhaExcel = linhaInicial;
		
		int somaQuantidadeEntradas = 0;
		int somaQuantidadeSaidas = 0;
		
		String conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
		while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
		{
			String unidade = deParaSiglasEntidades.get(conteudoCelula).toUpperCase();
			
			if(unidade != null)
			{
				int quantidade = 0;
				
				if(entradas.containsKey(unidade))
				{
					quantidade = entradas.get(unidade);
				}
				if(somarOuSubtrairTransferencias != null)
				{
					if(transferidosPorDestino.containsKey(unidade))
					{
						int quantidadeOperacao = transferidosPorDestino.get(unidade);
						
						if(somarOuSubtrairTransferencias.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SOMAR.getDescricao()))
							quantidade = quantidade + quantidadeOperacao;
						else if(somarOuSubtrairTransferencias.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SUBTRAIR.getDescricao()))
							quantidade = quantidade - quantidadeOperacao;
					}
				}
				somaQuantidadeEntradas += quantidade;
				
				celulas.add(new CelulaExcel(linhaExcel, colunaEntrada, quantidade, "Integer"));
				
				quantidade = 0;
				
				if(saidas.containsKey(unidade))
				{
					quantidade = saidas.get(unidade);
				}
				if(somarOuSubtrairTransferencias != null)
				{
					if(transferidosPorOrigem.containsKey(unidade))
					{
						int quantidadeOperacao = transferidosPorOrigem.get(unidade);
						
						if(somarOuSubtrairTransferencias.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SOMAR.getDescricao()))
							quantidade = quantidade + quantidadeOperacao;
						else if(somarOuSubtrairTransferencias.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SUBTRAIR.getDescricao()))
							quantidade = quantidade - quantidadeOperacao;
					}
				}
				
				somaQuantidadeSaidas += quantidade;
				
				celulas.add(new CelulaExcel(linhaExcel, colunaSaida, quantidade, "Integer"));
			}
			else
			{
				celulas.add(new CelulaExcel(linhaExcel, colunaEntrada, 0, "Integer"));
				celulas.add(new CelulaExcel(linhaExcel, colunaSaida, 0, "Integer"));
			}
			
			linhaExcel++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			
			System.out.println(conteudoCelula);
		}
		
		celulas.add(new CelulaExcel(linhaExcel, colunaEntrada, somaQuantidadeEntradas, "Integer"));
		celulas.add(new CelulaExcel(linhaExcel, colunaSaida, somaQuantidadeSaidas, "Integer"));
		
		return celulas;
	}
	
	private String preencherPlanilhaEntradaSaida(HashMap<String, String> deParaSiglasEntidades, LocalDate data, HashMap<String, Integer> entradas, HashMap<String, Integer> saidas)
	{
		String mesAnalise = (new MesesFormatados()).getMeses().get(data.getMonthValue() - 1).getMesDescricao();
		
		AcoesArquivoExcel arquivoConsolidado;
		if(ehOPM)
			arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoGEFICOPM().replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), String.valueOf(data.getYear())), 0);
		else
			arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoGEFICCirurgiasEletivas().replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), String.valueOf(data.getYear())), 0);
		
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_ENTRADA_SAIDA.getDescricao(), 0);
		
		int colunaExcelEntrada = ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice();
		int linhaExcelTabelaGeral = ParametrosArquivoConsolidadoGEFIC.INDICE_LINHA_INICIAL_ENTRADA_SAIDA.getIndice();
		
		int linhaExcelTabelaEntradaSaida = linhaExcelTabelaGeral;
		
		String conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelTabelaEntradaSaida, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
		while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SERVICO.getDescricao()))
		{
			linhaExcelTabelaEntradaSaida++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelTabelaEntradaSaida, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			
			System.out.println(conteudoCelula);
		}
		
		linhaExcelTabelaEntradaSaida+=3;
		
		int linhaExcelTabelaTransferencias = linhaExcelTabelaEntradaSaida;
		conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelTabelaTransferencias, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
		while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_SERVICO.getDescricao()))
		{
			linhaExcelTabelaTransferencias++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelTabelaTransferencias, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			
			System.out.println(conteudoCelula);
		}
		
		linhaExcelTabelaTransferencias+=3;
				
		conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelTabelaGeral - 2, colunaExcelEntrada, "");
		while(!conteudoCelula.equals(mesAnalise) && colunaExcelEntrada < 40)
		{
			colunaExcelEntrada++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelTabelaGeral - 2, colunaExcelEntrada, ""); 
			System.out.println(conteudoCelula);
		}
		
		if(colunaExcelEntrada < 40)
		{
			ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
			
			celulas.addAll(preencherTabelaPlanilhaEntradaSaida(arquivoConsolidado, linhaExcelTabelaGeral, colunaExcelEntrada, deParaSiglasEntidades, entradas, saidas, ParametrosArquivoConsolidadoGEFIC.TEXTO_SOMAR.getDescricao()));
			celulas.addAll(preencherTabelaPlanilhaEntradaSaida(arquivoConsolidado, linhaExcelTabelaEntradaSaida, colunaExcelEntrada, deParaSiglasEntidades, entradas, saidas, null));
			celulas.addAll(preencherTabelaPlanilhaEntradaSaida(arquivoConsolidado, linhaExcelTabelaTransferencias, colunaExcelEntrada, deParaSiglasEntidades, transferidosPorDestino, transferidosPorOrigem, null));
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_ENTRADA_SAIDA.getDescricao(), celulas, false, false, 0, null);
		}
		
		return "";
	}
	
	private String contabilizarEntradas(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, String dataInicial, String dataFinal, HashMap<String, Integer> entradas)
	{
		ArrayList<String> opcoes = new ArrayList<String>();
		
		if(ehOPM)
		{
			opcoes.add("Quantidade de entradas de pacientes OPM");
		}
		else
		{
			opcoes.add("Quantidade de entradas de pacientes");
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));

		paginaWeb.limparInputTextPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_ENTRADAS_DATA_INDICACAO_INICIAL.getTextoIdentificador());
		paginaWeb.tirarFocoDoCampoTextoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_ENTRADAS_DATA_INDICACAO_INICIAL.getTextoIdentificador());
		paginaWeb.limparInputTextPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPAHT_RELATORIO_ENTRADAS_DATA_INDICACAO_FINAL.getTextoIdentificador());
		paginaWeb.tirarFocoDoCampoTextoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPAHT_RELATORIO_ENTRADAS_DATA_INDICACAO_FINAL.getTextoIdentificador());
		
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_ENTRADAS_DATA_INICIAL.getTextoIdentificador(), dataInicial);
		paginaWeb.tirarFocoDoCampoTextoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_ENTRADAS_DATA_INICIAL.getTextoIdentificador());
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPAHT_RELATORIO_ENTRADAS_DATA_FINAL.getTextoIdentificador(), dataFinal);
		paginaWeb.tirarFocoDoCampoTextoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPAHT_RELATORIO_ENTRADAS_DATA_FINAL.getTextoIdentificador());
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_PESQUISAR.getTextoIdentificador(), "id");
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		Arquivo arquivo = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICEntradaPacientes.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AcoesArquivoExcel arquivoEntradas = new AcoesArquivoExcel(arquivo.getCaminhoCompleto(), 0);
		
		
		int primeiraLinha = ParametrosArquivoGEFICEntradaPacientes.LINHA_INICIAL_ARQUIVO.getIndice();
		arquivoEntradas.abrirPlanilha(0, 0);
		int ultimaLinha = arquivoEntradas.getUltimaLinhaPreenchida();
		
		System.out.println(arquivo.getCaminhoCompleto() + " - " + ultimaLinha);
		for(int linhaExcel = primeiraLinha; linhaExcel <= ultimaLinha; linhaExcel++)
		{
			String estabelecimento = arquivoEntradas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICEntradaPacientes.INDICE_COLUNA_ESTABELECIMENTO.getIndice()).toUpperCase();
			int quantidadeEntrada = arquivoEntradas.getValorDaCelulaInt(linhaExcel, ParametrosArquivoGEFICEntradaPacientes.INDICE_COLUNA_QTDE_ENTRADA.getIndice());
			
			if(entradas.containsKey(estabelecimento))
			{
				int quantidade = entradas.get(estabelecimento);
				quantidade += quantidadeEntrada;
				entradas.put(estabelecimento, quantidade);
			}
			else
				entradas.put(estabelecimento, quantidadeEntrada);
		}
		
		arquivo.apagar();
		
		return "";
	}
	
	private String contabilizarSaidas(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, String dataInicial, String dataFinal, HashMap<String, Integer> saidas)
	{
		ArrayList<String> opcoes = new ArrayList<String>();
		
		if(ehOPM)
		{
			opcoes.add("Quantidade de saídas de pacientes OPM");
		}
		else
		{
			opcoes.add("Quantidade de saídas de pacientes");
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_INICIAL.getTextoIdentificador(), dataInicial);
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_FINAL.getTextoIdentificador(), dataFinal);
		paginaWeb.tirarFocoDoCampoTextoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_FINAL.getTextoIdentificador());
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_BOTAO_PESQUISAR.getTextoIdentificador(), "id");
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO_SAIDA.getTextoIdentificador()));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Arquivo arquivo = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICSaidaPacientes.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
		
		AcoesArquivoExcel arquivoSaidas = new AcoesArquivoExcel(arquivo.getCaminhoCompleto(), 0);
		
		int primeiraLinha = ParametrosArquivoGEFICSaidaPacientes.LINHA_INICIAL_ARQUIVO.getIndice();
		int ultimaLinha = arquivoSaidas.getUltimaLinhaPreenchida();
		
		for(int linhaExcel = primeiraLinha; linhaExcel <= ultimaLinha; linhaExcel++)
		{
			String estabelecimento = arquivoSaidas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientes.INDICE_COLUNA_ESTABELECIMENTO.getIndice()).toUpperCase();
			int quantidadeEntrada = arquivoSaidas.getValorDaCelulaInt(linhaExcel, ParametrosArquivoGEFICSaidaPacientes.INDICE_COLUNA_QTDE_SAIDA.getIndice());
			
			if(saidas.containsKey(estabelecimento))
			{
				int quantidade = saidas.get(estabelecimento);
				quantidade += quantidadeEntrada;
				saidas.put(estabelecimento, quantidade);
			}
			else
				saidas.put(estabelecimento, quantidadeEntrada);
		}
		
		arquivo.apagar();
		
		return "";
	}
	
	private String contabilizarTransferencias(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, String dataInicial, String dataFinal)
	{
		ArrayList<String> opcoes = new ArrayList<String>();
		opcoes.add("Quantidade de pacientes que fizeram transferência");
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_TRANSFERENCIAS_DATA_INICIAL.getTextoIdentificador(), dataInicial);
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_TRANSFERENCIAS_DATA_FINAL.getTextoIdentificador(), dataFinal);
		paginaWeb.tirarFocoDoCampoTextoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_FINAL.getTextoIdentificador());
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_PESQUISAR.getTextoIdentificador(), "id");
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		Arquivo arquivo = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICTransferenciaPacientes.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AcoesArquivoExcel arquivoTransferencias = new AcoesArquivoExcel(arquivo.getCaminhoCompleto(), 0);
		
		int primeiraLinha = ParametrosArquivoGEFICTransferenciaPacientes.LINHA_INICIAL_ARQUIVO.getIndice();
		int ultimaLinha = arquivoTransferencias.getUltimaLinhaPreenchida();
		
		for(int linhaExcel = primeiraLinha; linhaExcel <= ultimaLinha; linhaExcel++)
		{
			String estabelecimentoOrigem = arquivoTransferencias.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICTransferenciaPacientes.INDICE_COLUNA_ESTABELECIMENTO_ORIGEM.getIndice()).toUpperCase();
			String estabelecimentoDestino = arquivoTransferencias.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICTransferenciaPacientes.INDICE_COLUNA_ESTABELECIMENTO_DESTINO.getIndice()).toUpperCase();
			
			if(transferidosPorOrigem.containsKey(estabelecimentoOrigem))
			{
				int quantidade = transferidosPorOrigem.get(estabelecimentoOrigem);
				quantidade++;
				transferidosPorOrigem.put(estabelecimentoOrigem, quantidade);
			}
			else
				transferidosPorOrigem.put(estabelecimentoOrigem, 1);
			
			if(transferidosPorDestino.containsKey(estabelecimentoDestino))
			{
				int quantidade = transferidosPorDestino.get(estabelecimentoDestino);
				quantidade++;
				transferidosPorDestino.put(estabelecimentoDestino, quantidade);
			}
			else
				transferidosPorDestino.put(estabelecimentoDestino, 1);
		}
		
		arquivo.apagar();
		
		return "";
	}
	
	private String atualizarStatusDasSaidasDePacientes(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, HashMap<String, String> deParaSiglasEntidades, String competencia, ArrayList<String> statusRealizados, HashMap<String, String> deParaStatus)
	{
		dataFormatadaInicioCompetencia = "01/" + competencia;
		
		dataInicioCompetencia = LocalDate.parse(dataFormatadaInicioCompetencia, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		dataFinalCompetencia = dataInicioCompetencia.with(TemporalAdjusters.lastDayOfMonth());
		dataFormatadaFinalCompetencia = dataFinalCompetencia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String mesAnalise = (new MesesFormatados()).getMeses().get(dataInicioCompetencia.getMonthValue() - 1).getMesDescricao();
		
		String planilhaRealizado;
		String planilhaCancelado;
		
		ArrayList<String> opcoes = new ArrayList<String>();
		
		if(ehOPM)
		{
			opcoes.add("Quantidade de saídas de pacientes OPM");
			planilhaRealizado = ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_OPM_ENTREGUES.getDescricao();
			planilhaCancelado = ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_OPM_CANCELADAS.getDescricao();
		}
		else
		{
			opcoes.add("Quantidade de saídas de pacientes");
			planilhaRealizado = ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_CIRURGIAS_REALIZADAS.getDescricao();
			planilhaCancelado = ParametrosArquivoConsolidadoGEFIC.NOME_PLANILHA_CIRURGIAS_CANCELADAS.getDescricao();
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_INICIAL.getTextoIdentificador(), dataFormatadaInicioCompetencia);
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_FINAL.getTextoIdentificador(), dataFormatadaFinalCompetencia);
		paginaWeb.tirarFocoDoCampoTextoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_FINAL.getTextoIdentificador());
		
		paginaWeb.clicarRadioInputPeloId(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_OPCAO_ANALITICO.getTextoIdentificador());
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		AcoesArquivoExcel arquivoConsolidado;
		if(ehOPM)
			arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoGEFICOPM().replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), String.valueOf(dataInicioCompetencia.getYear())), 0);
		else
			arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoGEFICCirurgiasEletivas().replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), String.valueOf(dataInicioCompetencia.getYear())), 0);
		
		arquivoConsolidado.abrirPlanilha(planilhaRealizado, 0);
		
		int colunaExcel = ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice();
		int linhaExcelArquivoConsolidado = ParametrosArquivoConsolidadoGEFIC.INDICE_LINHA_INICIAL_RELATORIOS.getIndice();
		int linhaExcelArquivoBaixado = ParametrosArquivoGEFICSaidaPacientesAnalitico.LINHA_INICIAL_ARQUIVO.getIndice();
		
		String conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelArquivoConsolidado - 1, colunaExcel, "");
		while(!conteudoCelula.equals(mesAnalise) && colunaExcel < 20)
		{
			colunaExcel++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelArquivoConsolidado - 1, colunaExcel, ""); 
			System.out.println(conteudoCelula);
		}
		
		HashMap<String, Integer> quantidadeRealizadaPorUnidade = new HashMap<String, Integer>();
		HashMap<String, Integer> quantidadeCanceladaPorUnidade = new HashMap<String, Integer>();
		HashMap<String, HashMap<String, Integer>> quantidadeCanceladaPorMotivoPorUnidade = new HashMap<String, HashMap<String, Integer>>();
		
		ArrayList<DadoAnaliticoSaidaPacienteGEFIC> casosMotivoOutros = new ArrayList<DadoAnaliticoSaidaPacienteGEFIC>();
		
		conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelArquivoConsolidado, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
		while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
		{
			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_ESTABELECIMENTO.getTextoIdentificador());
			
			String estabelecimento = deParaSiglasEntidades.get(conteudoCelula);
			
			if(estabelecimento != null)
			{
				String siglaEstabelecimento = conteudoCelula;
				
				ArrayList<String> opcoesEstabelecimentos = new ArrayList<String>();
				opcoesEstabelecimentos.add(estabelecimento);
				
				System.out.print(estabelecimento);
				
				//paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_UL_ESTABELECIMENTO.getTextoIdentificador(), opcoesEstabelecimentos);
				//paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_TEXT_ESTABELECIMENTO.getTextoIdentificador(), estabelecimento);
				while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO_SAIDA.getTextoIdentificador()));
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//paginaWeb.digitarEmInputTextPorXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_TEXT_ESTABELECIMENTO.getTextoIdentificador(), estabelecimento);
				//paginaWeb.selecionarItemSelectULLIPeloTitleDeUmaLinha(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_SELECT_ESTABELECIMENTOS.getTextoIdentificador(), estabelecimento);
				paginaWeb.selecionarItemSelectULLIPeloTitleDeUmaLinhaPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_UL_ESTABELECIMENTO.getTextoIdentificador(), estabelecimento);
				
				while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO_SAIDA.getTextoIdentificador()));
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(paginaWeb.obterValorDeUmAtributoDeUmElementoPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_ESTABELECIMENTO.getTextoIdentificador(), "title").equals(estabelecimento))
				{				
					paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_BOTAO_PESQUISAR.getTextoIdentificador(), "id");
					while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO_SAIDA.getTextoIdentificador()));
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Arquivo arquivo = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICSaidaPacientes.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
					
					AcoesArquivoExcel arquivoSaidas = new AcoesArquivoExcel(arquivo.getCaminhoCompleto(), 0);
					int primeiraLinha = ParametrosArquivoGEFICSaidaPacientesAnalitico.LINHA_INICIAL_ARQUIVO.getIndice();
					int ultimaLinha = arquivoSaidas.getUltimaLinhaPreenchida();
	
					int quantidadeRealizada = 0;
					int quantidadeCancelada = 0; 
	
					HashMap<String, Integer> quantidadeCanceladaPorMotivo = new HashMap<String, Integer>();
					
					for(int linhaExcel = primeiraLinha; linhaExcel <= ultimaLinha; linhaExcel++)
					{
						String motivoSaida = arquivoSaidas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_MOTIVO_SAIDA.getIndice());
		
						if(statusRealizados.contains(motivoSaida))
						{
							quantidadeRealizada++;
						}
						else
						{
							quantidadeCancelada++;
							
							String motivoSaidaNormalizado = deParaStatus.get(motivoSaida);
							
							if(motivoSaidaNormalizado != null)
							{
								if(quantidadeCanceladaPorMotivo.containsKey(motivoSaidaNormalizado)) 
								{
									int quantidade = quantidadeCanceladaPorMotivo.get(motivoSaidaNormalizado);
									quantidade++;
									quantidadeCanceladaPorMotivo.put(motivoSaidaNormalizado, quantidade);
								}
								else
								{
									quantidadeCanceladaPorMotivo.put(motivoSaidaNormalizado, 1);
								}
								
								if(motivoSaidaNormalizado.equals(ParametrosArquivoGEFICSaidaPacientesAnalitico.TEXTO_MOTIVO_OUTROS.getDescricao()))
								{
									DadoAnaliticoSaidaPacienteGEFIC motivoOutros = new DadoAnaliticoSaidaPacienteGEFIC();
									motivoOutros.setSiglaEstabelecimento(siglaEstabelecimento);
									motivoOutros.setEstabelecimento(estabelecimento);
									motivoOutros.setPaciente(arquivoSaidas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_PACIENTE.getIndice()));
									motivoOutros.setDataSaida(arquivoSaidas.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_DATA_SAIDA.getIndice(), "dd/MM/yyyy"));
									motivoOutros.setMotivoSaida(arquivoSaidas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_MOTIVO_SAIDA.getIndice()));
									motivoOutros.setDataNascimento(arquivoSaidas.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_DATA_NASCIMENTO.getIndice(), "dd/MM/yyyy"));
									motivoOutros.setIdade(arquivoSaidas.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_IDADE.getIndice(), ""));
									motivoOutros.setEspecialidade(arquivoSaidas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_ESPECIALIDADE.getIndice()));
									motivoOutros.setSubespecialidade(arquivoSaidas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_SUBESPECIALIDADE.getIndice()));
									motivoOutros.setProcedimento(arquivoSaidas.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientesAnalitico.INDICE_COLUNA_PROCEDIMENTO.getIndice()));
									
									casosMotivoOutros.add(motivoOutros);
								}
							}
						}
					}
					
					quantidadeRealizadaPorUnidade.put(siglaEstabelecimento, quantidadeRealizada);
					quantidadeCanceladaPorUnidade.put(siglaEstabelecimento, quantidadeCancelada);
					quantidadeCanceladaPorMotivoPorUnidade.put(siglaEstabelecimento, quantidadeCanceladaPorMotivo);
					
					arquivo.apagar();
					
					paginaWeb.clicarLinkPeloCSSSelector(driver, IdentificadoresPaginaWebGEFIC.CLASS_RELATORIO_SAIDAS_REMOVER_ESTABELECIMENTO_SELECIONADO.getTextoIdentificador());
				}
				else
				{
					quantidadeRealizadaPorUnidade.put(siglaEstabelecimento, 0);
					quantidadeCanceladaPorUnidade.put(siglaEstabelecimento, 0);
					quantidadeCanceladaPorMotivoPorUnidade.put(siglaEstabelecimento, new HashMap<String, Integer>());
				}
			}


			linhaExcelArquivoConsolidado++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcelArquivoConsolidado, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			
			System.out.println(conteudoCelula);
		}
		
		ArrayList<CelulaExcel> celulasAtendidos = obterCelulasPacienteAtendido(arquivoConsolidado, planilhaRealizado, colunaExcel, quantidadeRealizadaPorUnidade);
		ArrayList<CelulaExcel> celulasCancelados = obterCelulasPacienteCancelado(arquivoConsolidado, planilhaCancelado, colunaExcel, quantidadeCanceladaPorUnidade, quantidadeCanceladaPorMotivoPorUnidade);
		
		arquivoConsolidado.forcarCalculos();
		arquivoConsolidado.gravarDadosEmCelula(planilhaRealizado, celulasAtendidos, false, false, 0, null);
		arquivoConsolidado.gravarDadosEmCelula(planilhaCancelado, celulasCancelados, false, false, 0, null);
		
		registrarObservacaoOutros(paginaWeb, driver, casosMotivoOutros);
		
		return "";
	}
	
	private String registrarObservacaoOutros(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, ArrayList<DadoAnaliticoSaidaPacienteGEFIC> casosOutros)
	{
		ArrayList<String> opcoes = new ArrayList<String>();
		
		if(ehOPM)
		{
			opcoes.add("Filas OPM");
		}
		else
		{
			opcoes.add("Filas");
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_FILTRO_STATUS.getTextoIdentificador());
		
		paginaWeb.selecionarItemSelectULLIPeloTitleDeUmaLinhaPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_FILTRO_STATUS_UL.getTextoIdentificador(), IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_REGISTRO_CANCELADO.getTextoIdentificador());
		
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_BOTAO_MAIS_COLUNAS.getTextoIdentificador());
		
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_CHECK_BOX_PROCEDIMENTO.getTextoIdentificador());
		
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_CHECK_BOX_DATA_SAIDA.getTextoIdentificador());
		
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_BOTAO_FILTROS.getTextoIdentificador());
		
		DadoAnaliticoSaidaPacienteGEFIC caso = casosOutros.get(0);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_FILTRO_NOME_PACIENTE.getTextoIdentificador(), caso.getPaciente());
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_FILAS_BOTAO_PESQUISAR.getTextoIdentificador(), "id");

		
		return "";
	}
	
	private ArrayList<CelulaExcel> obterCelulasPacienteAtendido(AcoesArquivoExcel arquivoConsolidado, String nomePlanilha, int coluna, HashMap<String, Integer> quantidadePorEstabelecimento)
	{
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		int linhaExcel = ParametrosArquivoConsolidadoGEFIC.INDICE_LINHA_INICIAL_RELATORIOS.getIndice();
		
		arquivoConsolidado.abrirPlanilha(nomePlanilha, 0);
		
		String conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
		while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
		{
			int quantidade = 0;
			if(quantidadePorEstabelecimento.containsKey(conteudoCelula))
				quantidade = quantidadePorEstabelecimento.get(conteudoCelula);
			else
				quantidade = 0;

			celulas.add(new CelulaExcel(linhaExcel, coluna, quantidade, "Integer"));
			
			linhaExcel++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			
			System.out.println(conteudoCelula);
		}
		
		return celulas;
	}
	
	private ArrayList<CelulaExcel> obterCelulasPacienteCancelado(AcoesArquivoExcel arquivoConsolidado, String nomePlanilha, int coluna, HashMap<String, Integer> quantidadePorEstabelecimento, HashMap<String, HashMap<String, Integer>> motivosPorEstabelecimento)
	{
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		int linhaExcel = ParametrosArquivoConsolidadoGEFIC.INDICE_LINHA_INICIAL_RELATORIOS.getIndice();
		
		arquivoConsolidado.abrirPlanilha(nomePlanilha, 0);
		
		String conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
		while(!conteudoCelula.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
		{
			int quantidade = 0;
			
			String siglaEstabelecimento = conteudoCelula;
			
			if(quantidadePorEstabelecimento.containsKey(conteudoCelula))
				quantidade = quantidadePorEstabelecimento.get(conteudoCelula);
			else
				quantidade = 0;

			celulas.add(new CelulaExcel(linhaExcel, coluna, quantidade, "Integer"));
			
			int linhaTabelaMotivosDetalhados = linhaExcel + 1;
			
			String conteudoCelulaMotivos = arquivoConsolidado.getValorDaCelulaComoString(linhaTabelaMotivosDetalhados, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			while(!conteudoCelulaMotivos.equals(siglaEstabelecimento) && !conteudoCelulaMotivos.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_FINAL_RELATORIO.getDescricao()))
			{
				linhaTabelaMotivosDetalhados++;
				conteudoCelulaMotivos = arquivoConsolidado.getValorDaCelulaComoString(linhaTabelaMotivosDetalhados, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			}
			
			if(conteudoCelulaMotivos.equals(siglaEstabelecimento))
			{
				linhaTabelaMotivosDetalhados+=2;
				
				conteudoCelulaMotivos = arquivoConsolidado.getValorDaCelulaComoString(linhaTabelaMotivosDetalhados, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
				
				HashMap<String, Integer> motivosCancelamento = motivosPorEstabelecimento.get(siglaEstabelecimento);
				
				if(motivosCancelamento != null)
				{
					while(!conteudoCelulaMotivos.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
					{
						int quantidadeMotivo;
						if(motivosCancelamento.containsKey(conteudoCelulaMotivos))
							quantidadeMotivo = motivosCancelamento.get(conteudoCelulaMotivos);
						else
							quantidadeMotivo = 0;
							
						celulas.add(new CelulaExcel(linhaTabelaMotivosDetalhados, coluna, quantidadeMotivo, "Integer"));
						
						linhaTabelaMotivosDetalhados++;
						conteudoCelulaMotivos = arquivoConsolidado.getValorDaCelulaComoString(linhaTabelaMotivosDetalhados, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
					}
				}
				else
				{
					while(!conteudoCelulaMotivos.equals(ParametrosArquivoConsolidadoGEFIC.TEXTO_TOTAL.getDescricao()))
					{
						celulas.add(new CelulaExcel(linhaTabelaMotivosDetalhados, coluna, 0, "Integer"));
						
						linhaTabelaMotivosDetalhados++;
						conteudoCelulaMotivos = arquivoConsolidado.getValorDaCelulaComoString(linhaTabelaMotivosDetalhados, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
					}
				}
			}
			
			linhaExcel++;
			conteudoCelula = arquivoConsolidado.getValorDaCelulaComoString(linhaExcel, ParametrosArquivoConsolidadoGEFIC.INDICE_COLUNA_INICIAL_RELATORIOS.getIndice(), "").trim();
			
			System.out.println(conteudoCelula);
		}
		
		return celulas;
	}
	
	private Arquivo baixarArquivos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String idBotaoExcel, String formatoArquivo) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		paginaWeb.clicarBotaoSubmit(driver, idBotaoExcel, "id");
		
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
		}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(formatoArquivo));
			
		Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);

		return arquivo;
	}
	
	
}
