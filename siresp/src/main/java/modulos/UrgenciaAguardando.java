package modulos;


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

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRUrgencia;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoLeitosPlanilhaMonitoramento;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado;
import dadosGerais.ParametrosTabelaUrgenciaSolicitacoesPendentes;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.ExcelBinder;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import modelosDados.CelulaExcel;
import modelosDados.IntervalosUrgencia;
import modelosDados.OfertaEDemanda;
import modelosDados.UrgenciaAguardandoDetalhado;
import modelosDados.UrgenciaAguardandoAgrupado;

public class UrgenciaAguardando 
{
	private String pastaBaseAmbulatorialCDIDR;
	private String pastaBase;
	LocalDate dataInicioReferencia;
	LocalDate dataFinalReferencia;
	LocalDate dataInicioCompetencia;
	LocalDate dataFinalCompetencia;
	String dataFormatadaInicioReferencia;
	String dataFormatadaFinalReferencia;
	String dataFormatadaInicioCompetencia;
	String dataFormatadaFinalCompetencia;
	
	ArrayList<String> unidadesSolicitantes;
	HashMap<String, UrgenciaAguardandoAgrupado> urgenciasAgrupadasJaRegistradas;
	HashMap<String, UrgenciaAguardandoDetalhado> urgenciasDetalhadasJaRegistradas;
	private IdentificadoresPastasCompartilhadasCDIDRUrgencia diretoriosCDIDR; 

	public UrgenciaAguardando(String pastaBase, String ambiente)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRUrgencia.valueOf(ambiente);
		pastaBaseAmbulatorialCDIDR = pastaBase;
	}
	
	public UrgenciaAguardando()
	{

	}
	
	public String obterAgrupamentoDeEsperaUrgencia(WebDriver driver, String ambiente)
	{			
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRUrgencia.valueOf(ambiente.toUpperCase());
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
    	pastaBase = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta compartilhada", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
		
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
		
    	//consolidados urgência
    	ArrayList<UrgenciaAguardandoAgrupado> listaUrgencias = new ArrayList<UrgenciaAguardandoAgrupado>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaAguardandoAgrupado.class, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasAgrupadasJaRegistradas = new HashMap<String, UrgenciaAguardandoAgrupado>();
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaAguardandoAgrupado urgencia : listaUrgencias)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData()));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasAgrupadasJaRegistradas.put(urgencia.getData() + urgencia.getSolicitante() + urgencia.getRecurso() + urgencia.getFicha(), urgencia);
			
			linhaArquivo++;
		}
		
		//consolidados urgência por hora
    	ArrayList<UrgenciaAguardandoDetalhado> listaUrgenciasPorHora = new ArrayList<UrgenciaAguardandoDetalhado>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgenciasPorHora = ExcelBinder.readSheet(in, UrgenciaAguardandoDetalhado.class, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasDetalhadasJaRegistradas = new HashMap<String, UrgenciaAguardandoDetalhado>();
		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaAguardandoDetalhado urgencia : listaUrgenciasPorHora)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData()));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasDetalhadasJaRegistradas.put(urgencia.getData() + urgencia.getSolicitante() + urgencia.getRecurso() + urgencia.getFicha() + urgencia.getHorasDeEspera(), urgencia);
			
			linhaArquivo++;
		}
		
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
					
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());

		ArrayList<String> opcoes = new ArrayList<String>();
		opcoes.add("Urgência");
		opcoes.add("Solicitações Pendentes");
		
		boolean visivel;
		do
		{
		
			visivel = acessarMenu(driver, paginaWeb, opcoes);
			
		
		}while(!visivel);
		

		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_BOTAO_PESQUISAR.getTextoIdentificador());
		
		while(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_CABECALHO_TABELA_RESULTADOS.getTextoIdentificador()));
		System.out.println("Cabeçalho visível");
		
		while(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_DIV_CARREGANDO.getTextoIdentificador()));
		System.out.println("DIV carregando");
		
		while(!paginaWeb.compararValorAtributoCSSPorXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_DIV_CARREGANDO.getTextoIdentificador(), "display", "none"));
		System.out.println("Block encerrado carregando");
		
		LocalDateTime dataHora = LocalDateTime.now();
		
		String textoPaginas = paginaWeb.obterTextoPorXPathDeSpan(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_TEXTO_QUANTIDADE_PAGINAS.getTextoIdentificador());
		textoPaginas = textoPaginas.replace("/", "").trim();
		
		if(!textoPaginas.equals(""))
		{
			int qtdePaginas = Integer.parseInt(textoPaginas);
			
			ArrayList<ArrayList<String>> tabelaResultados = new ArrayList<ArrayList<String>>();
			
			for(int pagina = 1; pagina <= qtdePaginas; pagina++)
			{
				ArrayList<ArrayList<String>> tabelaDaPagina = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_TABELA_RESULTADOS.getTextoIdentificador());
				tabelaResultados.addAll(tabelaDaPagina);
				
				if(pagina < qtdePaginas)
				{
					paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_BOTAO_AVANCAR_UMA_PAGINA.getTextoIdentificador());
					
					while(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_DIV_CARREGANDO.getTextoIdentificador()));
					System.out.println("DIV carregando" + (pagina + 1));
					
					while(!paginaWeb.compararValorAtributoCSSPorXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_SOLICITACOES_PENDENTES_URGENCIA_DIV_CARREGANDO.getTextoIdentificador(), "display", "none"));
					System.out.println("Block encerrado carregando" + (pagina + 1));
				}
			}
			
			System.out.println("Páginas: " + textoPaginas);
			System.out.println("Quantidade de registros: " + tabelaResultados.size());
			
			montarDadosDeUrgencia(tabelaResultados, dataHora);
		}
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		ordenarPlanilhaAgrupada();
		ordenarPlanilhaDetalhada();
		//atualizarCopiaOriginalRelatorioProducao();
		//copiarRelatorioProducaoParaCDIDR();
		//copiarRelatorioProducaoParaCDRA();
		
		return "";	
	}
	
	private ArrayList<IntervalosUrgencia> criarEstruturaDeIntervalosDeUrgencia()
	{
		ArrayList<IntervalosUrgencia> intervalos = new ArrayList<IntervalosUrgencia>();
		
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_0_6_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_0_6_HORAS.getIndice(), -1, 6));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_6_12_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_6_12_HORAS.getIndice(), 6, 12));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_12_24_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_12_24_HORAS.getIndice(), 12, 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_24_48_HORAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_24_48_HORAS.getIndice(), 24, 48));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_2_3_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_2_3_DIAS.getIndice(), 2 * 24, 3 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_3_5_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_3_5_DIAS.getIndice(), 3 * 24, 5 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_5_7_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_5_7_DIAS.getIndice(), 5 * 24, 7 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_7_10_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_7_10_DIAS.getIndice(), 7 * 24, 10 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_10_13_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_10_13_DIAS.getIndice(), 10 * 24, 13 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_13_15_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_13_15_DIAS.getIndice(), 13 * 24, 15 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_15_17_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_15_17_DIAS.getIndice(), 15 * 24, 17 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_17_20_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_17_20_DIAS.getIndice(), 17 * 24, 20 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_20_25_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_20_25_DIAS.getIndice(), 20 * 24, 25 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_25_30_DIAS.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_25_30_DIAS.getIndice(), 25 * 24, 30 * 24));
		intervalos.add(new IntervalosUrgencia(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getIndice(), 30 * 24, Integer.MAX_VALUE));
		
		return intervalos;
	}
	
	private String montarDadosDeUrgencia(ArrayList<ArrayList<String>> tabelaDeResultados, LocalDateTime dataHoraDeExtracao)
	{
		HashMap<String, ArrayList<IntervalosUrgencia>> urgenciasAgrupadas = new HashMap<String, ArrayList<IntervalosUrgencia>>();
		HashMap<String, Integer> urgenciasDetalhadas = new HashMap<String, Integer>();
		
		String textoDataExtracao = dataHoraDeExtracao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String textoHorarioExtracao = dataHoraDeExtracao.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		for(ArrayList<String> linha : tabelaDeResultados)
		{
			if(!linha.get(ParametrosTabelaUrgenciaSolicitacoesPendentes.INDICE_COLUNA_ASSUMIDO_EM.getIndice()).trim().equals(""))
			{
				String textoMapa = textoDataExtracao + ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao() + 
						linha.get(ParametrosTabelaUrgenciaSolicitacoesPendentes.INDICE_COLUNA_SOLICITANTE.getIndice()) + ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao() +
						linha.get(ParametrosTabelaUrgenciaSolicitacoesPendentes.INDICE_COLUNA_RECURSO.getIndice()) + ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao() +
						linha.get(ParametrosTabelaUrgenciaSolicitacoesPendentes.INDICE_COLUNA_FICHA.getIndice());
				
				LocalDateTime dataHoraSolicitado = LocalDateTime.parse(linha.get(ParametrosTabelaUrgenciaSolicitacoesPendentes.INDICE_COLUNA_SOLICITACAO_EM.getIndice()), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
				
				Duration tempoEntreDatas = Duration.between(dataHoraSolicitado, dataHoraDeExtracao);
				
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
				
				textoMapa += ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao() + horasExatas;
				
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
		}
		
		montarPlanilhaUrgenciaAgrupada(dataHoraDeExtracao, textoDataExtracao, textoHorarioExtracao, urgenciasAgrupadas);
		montarPlanilhaUrgenciaDetalhada(dataHoraDeExtracao, textoDataExtracao, textoHorarioExtracao, urgenciasDetalhadas);
		
		return "";
	}
	
	private String montarPlanilhaUrgenciaAgrupada(LocalDateTime dataHoraDeExtracao, String textoDataExtracao, String textoHorarioExtracao, HashMap<String, ArrayList<IntervalosUrgencia>> urgenciasAgrupadas)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : urgenciasAgrupadas.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao(), "");
			
			UrgenciaAguardandoAgrupado urgencia;
			int linha;
			if(urgenciasAgrupadasJaRegistradas.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasAgrupadasJaRegistradas.get(chaveJaRegistrada);
				urgencia.setHorarioExtracao(textoHorarioExtracao);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				urgencia = new UrgenciaAguardandoAgrupado();
				urgencia.setData(textoDataExtracao);
				urgencia.setHorarioExtracao(textoHorarioExtracao);
				urgencia.setSolicitante(chave.split(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao())[1]);
				urgencia.setRecurso(chave.split(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao())[2]);
				urgencia.setFicha(chave.split(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.DIVISOR_CAMPOS.getDescricao())[3]);
				
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
			}
			
			ArrayList<IntervalosUrgencia> intervalos = urgenciasAgrupadas.get(chave);
			int totalGeral = 0;
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_HORARIO_EXTRACAO.getIndice(), LocalTime.parse(textoHorarioExtracao, DateTimeFormatter.ofPattern("HH:mm:ss")), "Time/Seconds"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
			
			for(IntervalosUrgencia intervalo : intervalos)
			{
				totalGeral += intervalo.getQuantidade();
				
				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), intervalo.getIndiceTabela(), intervalo.getQuantidade(), "Integer"));
			}
			
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_TOTAL_GERAL.getIndice(), totalGeral, "Integer"));
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		removerLinhasUrgenciaAgrupadas(textoDataExtracao);
		
		return "";
	}
	
	private String removerLinhasUrgenciaAgrupadas(String textoDataExtracao)
	{
		ArrayList<Integer> linhasExluir = new ArrayList<Integer>();
		
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		for(String chave : urgenciasAgrupadasJaRegistradas.keySet().stream().filter(chave -> chave.contains(textoDataExtracao)).collect(Collectors.toList())) 
		{
			UrgenciaAguardandoAgrupado urgencia = urgenciasAgrupadasJaRegistradas.get(chave);
			
			if(!urgencia.isLinhaUtilizada())
				linhasExluir.add(urgencia.getLinhaExcel());
		}
		
		System.out.println("Quantidade de linhas a excluir: " + linhasExluir.size());
		
		Collections.sort(linhasExluir, Collections.reverseOrder());
		
		for(Integer linha : linhasExluir)
		{
			System.out.println("Excluindo linha: " + linha);
			arquivoCenso.apagarLinha(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), linha);
		}
		
		return "";
	}
	
	private String montarPlanilhaUrgenciaDetalhada(LocalDateTime dataHoraDeExtracao, String textoDataExtracao, String textoHorarioExtracao, HashMap<String, Integer> urgenciasAgrupadas)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : urgenciasAgrupadas.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.DIVISOR_CAMPOS.getDescricao(), "");
			
			UrgenciaAguardandoDetalhado urgencia;
			int linha;
			if(urgenciasDetalhadasJaRegistradas.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasDetalhadasJaRegistradas.get(chaveJaRegistrada);
				urgencia.setHorarioExtracao(textoHorarioExtracao);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				urgencia = new UrgenciaAguardandoDetalhado();
				urgencia.setData(textoDataExtracao);
				urgencia.setHorarioExtracao(textoHorarioExtracao);
				urgencia.setSolicitante(chave.split(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.DIVISOR_CAMPOS.getDescricao())[1]);
				urgencia.setRecurso(chave.split(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.DIVISOR_CAMPOS.getDescricao())[2]);
				urgencia.setFicha(chave.split(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.DIVISOR_CAMPOS.getDescricao())[3]);
				urgencia.setHorasDeEspera(chave.split(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.DIVISOR_CAMPOS.getDescricao())[4]);
				
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
			}
			
			int quantidadeEmEspera = urgenciasAgrupadas.get(chave);
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_HORARIO_EXTRACAO.getIndice(), LocalTime.parse(textoHorarioExtracao, DateTimeFormatter.ofPattern("HH:mm:ss")), "Time/Seconds"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_HORAS_DE_ESPERA.getIndice(), Integer.parseInt(urgencia.getHorasDeEspera()), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_QUANTIDADE.getIndice(), quantidadeEmEspera, "Integer"));
			
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		removerLinhasDetalhadas(textoDataExtracao);
		
		return "";
	}
	
	private String removerLinhasDetalhadas(String textoDataExtracao)
	{
		ArrayList<Integer> linhasExluir = new ArrayList<Integer>();
		
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		for(String chave : urgenciasDetalhadasJaRegistradas.keySet().stream().filter(chave -> chave.contains(textoDataExtracao)).collect(Collectors.toList())) 
		{
			UrgenciaAguardandoDetalhado urgencia = urgenciasDetalhadasJaRegistradas.get(chave);
			
			if(!urgencia.isLinhaUtilizada())
				linhasExluir.add(urgencia.getLinhaExcel());
		}
		
		System.out.println("Quantidade de linhas a excluir: " + linhasExluir.size());
		
		Collections.sort(linhasExluir, Collections.reverseOrder());
		
		for(Integer linha : linhasExluir)
		{
			System.out.println("Excluindo linha: " + linha);
			arquivoCenso.apagarLinha(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), linha);
		}
		
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
	
	private static String normalizarDataParaDiaMesAno(String valor) {
	    if (valor == null || valor.isBlank())
	        return null;
	
	    DateTimeFormatter fmtMesAno = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
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
	
	    // 3️ Caso seja mmm/yyyy (direto do Excel ou do POI)
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
		ArrayList<UrgenciaAguardandoAgrupado> listaUrgencias = new ArrayList<UrgenciaAguardandoAgrupado>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaAguardandoAgrupado.class, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaAguardandoAgrupado urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaAguardandoAgrupado::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaAguardandoAgrupado::getSolicitante)
		    .thenComparing(UrgenciaAguardandoAgrupado::getRecurso)
		    .thenComparing(UrgenciaAguardandoAgrupado::getFicha)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaAguardandoAgrupado urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_HORARIO_EXTRACAO.getIndice(), urgencia.getHorarioExtracao(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_HORARIO_EXTRACAO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_SOLICITANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_RECURSO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_FICHA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_TOTAL_GERAL.getIndice(), urgencia.getTotalGeral(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_TOTAL_GERAL.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_0_6_HORAS.getIndice(), urgencia.getPeriodo_0_6_horas(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_0_6_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_6_12_HORAS.getIndice(), urgencia.getPeriodo_6_12_horas(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_6_12_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_12_24_HORAS.getIndice(), urgencia.getPeriodo_12_24_horas(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_12_24_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_24_48_HORAS.getIndice(), urgencia.getPeriodo_24_48_horas(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_24_48_HORAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_2_3_DIAS.getIndice(), urgencia.getPeriodo_2_3_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_2_3_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_3_5_DIAS.getIndice(), urgencia.getPeriodo_3_5_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_3_5_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_5_7_DIAS.getIndice(), urgencia.getPeriodo_5_7_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_5_7_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_7_10_DIAS.getIndice(), urgencia.getPeriodo_7_10_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_7_10_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_10_13_DIAS.getIndice(), urgencia.getPeriodo_10_13_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_10_13_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_13_15_DIAS.getIndice(), urgencia.getPeriodo_13_15_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_13_15_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_15_17_DIAS.getIndice(), urgencia.getPeriodo_15_17_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_15_17_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_17_20_DIAS.getIndice(), urgencia.getPeriodo_17_20_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_17_20_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_20_25_DIAS.getIndice(), urgencia.getPeriodo_20_25_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_20_25_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_25_30_DIAS.getIndice(), urgencia.getPeriodo_25_30_dias(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_25_30_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getIndice(), urgencia.getPeriodo_30_dias_acima(), ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.INDICE_COLUNA_30_DIAS_ACIMA.getTipo()));
			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaDetalhada()
	{
		ArrayList<UrgenciaAguardandoDetalhado> listaUrgencias = new ArrayList<UrgenciaAguardandoDetalhado>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaAguardandoDetalhado.class, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaAguardandoDetalhado urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
			
			urgencia.setHorasDeEsperaOrdenacao(Integer.parseInt(urgencia.getHorasDeEspera()));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaAguardandoDetalhado::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaAguardandoDetalhado::getSolicitante)
		    .thenComparing(UrgenciaAguardandoDetalhado::getRecurso)
		    .thenComparing(UrgenciaAguardandoDetalhado::getFicha)
		    .thenComparing(UrgenciaAguardandoDetalhado::getHorasDeEsperaOrdenacao)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaAguardandoDetalhado urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_HORARIO_EXTRACAO.getIndice(), urgencia.getHorarioExtracao(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_HORARIO_EXTRACAO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_SOLICITANTE.getIndice(), urgencia.getSolicitante(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_SOLICITANTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_RECURSO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_FICHA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_HORAS_DE_ESPERA.getIndice(), urgencia.getHorasDeEspera(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_HORAS_DE_ESPERA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_QUANTIDADE.getIndice(), urgencia.getQuantidade(), ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.INDICE_COLUNA_QUANTIDADE.getTipo()));

			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
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
		
		return celula;
	}
	
}
