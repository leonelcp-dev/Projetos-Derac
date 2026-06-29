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
import modelosDados.DadosAcumuladosVagaZero;
import modelosDados.EntidadeGEFIC;
import modelosDados.IntervalosUrgencia;
import modelosDados.OfertaEDemanda;
import modelosDados.UrgenciaAguardandoDetalhado;
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
					entidades.add(entidade);
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    	atualizarQuantidadeGeralDePacientes(paginaWeb, driver, deParaNomesEntidades);
    	atualizarQuantidadeEntradaSaidaDePacientes(paginaWeb, driver, deParaNomesEntidades, competencia);
		
		
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
	
	private String atualizarQuantidadeEntradaSaidaDePacientes(AcoesGeraisPaginaWeb paginaWeb, WebDriver driver, HashMap<String, String> deParaNomesEntidades, String competencia)
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
		contabilizarEntradas(paginaWeb, driver, competencia, competencia, entradas);
		contabilizarSaidas(paginaWeb, driver, competencia, competencia, saidas);
		
		
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
			opcoes.add("Quantidade de pacientes por serviço");
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_ENTRADAS_DATA_INICIAL.getTextoIdentificador(), dataInicial);
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPAHT_RELATORIO_ENTRADAS_DATA_FINAL.getTextoIdentificador(), dataFinal);
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_PESQUISAR.getTextoIdentificador(), "id");
		
		Arquivo arquivo = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICEntradaPacientes.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
		
		AcoesArquivoExcel arquivoTransferencias = new AcoesArquivoExcel(dataFinal, 0);
		
		int primeiraLinha = ParametrosArquivoGEFICEntradaPacientes.LINHA_INICIAL_ARQUIVO.getIndice();
		int ultimaLinha = arquivoTransferencias.getUltimaLinhaPreenchida();
		
		for(int linhaExcel = primeiraLinha; linhaExcel <= ultimaLinha; linhaExcel++)
		{
			String estabelecimento = arquivoTransferencias.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICEntradaPacientes.INDICE_COLUNA_ESTABELECIMENTO.getIndice()).toUpperCase();
			int quantidadeEntrada = arquivoTransferencias.getValorDaCelulaInt(linhaExcel, ParametrosArquivoGEFICEntradaPacientes.INDICE_COLUNA_QTDE_ENTRADA.getIndice());
			
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
			opcoes.add("Quantidade de entradas de pacientes OPM");
		}
		else
		{
			opcoes.add("Quantidade de pacientes por serviço");
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_INICIAL.getTextoIdentificador(), dataInicial);
		paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_RELATORIO_SAIDAS_DATA_FINAL.getTextoIdentificador(), dataFinal);
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_BOTAO_PESQUISAR.getTextoIdentificador(), "id");
		
		Arquivo arquivo = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_SAIDA_PACIENTES_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICSaidaPacientes.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
		
		AcoesArquivoExcel arquivoTransferencias = new AcoesArquivoExcel(dataFinal, 0);
		
		int primeiraLinha = ParametrosArquivoGEFICSaidaPacientes.LINHA_INICIAL_ARQUIVO.getIndice();
		int ultimaLinha = arquivoTransferencias.getUltimaLinhaPreenchida();
		
		for(int linhaExcel = primeiraLinha; linhaExcel <= ultimaLinha; linhaExcel++)
		{
			String estabelecimento = arquivoTransferencias.getValorDaCelulaString(linhaExcel, ParametrosArquivoGEFICSaidaPacientes.INDICE_COLUNA_ESTABELECIMENTO.getIndice()).toUpperCase();
			int quantidadeEntrada = arquivoTransferencias.getValorDaCelulaInt(linhaExcel, ParametrosArquivoGEFICSaidaPacientes.INDICE_COLUNA_QTDE_SAIDA.getIndice());
			
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
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_PESQUISAR.getTextoIdentificador(), "id");
		
		Arquivo arquivo = baixarArquivos(driver, paginaWeb, IdentificadoresPaginaWebGEFIC.ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_EXCEL.getTextoIdentificador(), ParametrosArquivoGEFICTransferenciaPacientes.EXTENSAO_ARQUIVO_RELATORIO_BAIXADO.getDescricao());
		
		AcoesArquivoExcel arquivoTransferencias = new AcoesArquivoExcel(dataFinal, 0);
		
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
	
	private Arquivo baixarArquivos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String idBotaoExcel, String formatoArquivo) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		paginaWeb.clicarLinkPeloXPath(driver, idBotaoExcel);
		
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
