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
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFichasAssumidas;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFichasFinalizadasAssumidasRegulador;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFichasFinalizadasAssumidasReguladorMensal;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFinalizadoDetalhado;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFormaResolucao;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaProducaoRegulador;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaVagaZero;
import dadosGerais.ParametrosArquivoUrgenciaRelatorioProdutividade;
import dadosGerais.ParametrosTabelaFichasAssumidasUrgencia;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado;
import dadosGerais.ParametrosTabelaUrgenciaSolicitacoesPendentes;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.ExcelBinder;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import modelosDados.CelulaExcel;
import modelosDados.DadosAcumuladosFichasAssumidas;
import modelosDados.DadosAcumuladosVagaZero;
import modelosDados.EntidadeExecutanteR1;
import modelosDados.EntidadeLeito;
import modelosDados.IntervalosUrgencia;
import modelosDados.UrgenciaFichasAssumidasRegulador;
import modelosDados.UrgenciaFichasAssumidasReguladorMensal;
import modelosDados.UrgenciaFichasFinalizadasAssumidasRegulador;
import modelosDados.UrgenciaFichasFinalizadasAssumidasReguladorMensal;
import modelosDados.UrgenciaFinalizadoDetalhado;
import modelosDados.UrgenciaFormaResolucao;
import modelosDados.UrgenciaProducaoRegulador;
import modelosDados.UrgenciaProducaoReguladorMensal;
import modelosDados.UrgenciaVagaZero;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;
import modelosDados.UrgenciaFinalizadoAgrupado;

public class UrgenciaFichasAssumidas 
{
	private String pastaBaseAmbulatorialCDIDR;
	private String pastaBase;
	private String pastaDownloads;

	private String dataDeAnalise;
	private LocalDate dataInformada;
	private String dataFormatada;
	
	private String caminhoArquivoConsolidado;
	private String ultimaPlanilhaProcessadaNoDia;
	private int primeiraLinhaDaUltimaPlanilhaProcessadaNoDia;
	private int colunaDataDaUltimaPlanilhaProcessadaNoDia;
	
	ArrayList<String> unidadesSolicitantes;
	ArrayList<String> reguladores;
	HashMap<String, UrgenciaFichasAssumidasRegulador> urgenciasFichasAssumidasRegulador;
	HashMap<String, UrgenciaFichasAssumidasReguladorMensal> urgenciasFichasAssumidasReguladorMensal;
	private IdentificadoresPastasCompartilhadasCDIDRUrgencia diretoriosCDIDR; 

	public UrgenciaFichasAssumidas(String pastaBase, String ambiente)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRUrgencia.valueOf(ambiente);
		pastaBaseAmbulatorialCDIDR = pastaBase;
		this.pastaBase = pastaBase;
		
		//setDadosReferenciaisParaAutomatizacaoDeLogin(true, ambiente);
	}
	
	public UrgenciaFichasAssumidas()
	{
		setDadosReferenciaisParaAutomatizacaoDeLogin(false, "");
	}
	
	public String copiarRelatorioUrgenciaParaCDIDR()
	{
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaConsolidadoUrgencia();
		Arquivo arquivo = new Arquivo(caminhoArquivo, diretoriosCDIDR.getNomeArquivoConsolidado());
		
		String pastaRelatorioCDIDR = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaConsolidadoUrgenciaCDIDR();
		
		String nomeArquivo = arquivo.getNomeDoArquivo();
		//LocalDate data = LocalDate.now();
		//nomeArquivo  = nomeArquivo.replace(".xlsx", " - " + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx");
		
		arquivo.CopiarArquivo(pastaRelatorioCDIDR + "\\" + nomeArquivo);
		
		return "";
	}
	
	public String copiarRelatorioUrgenciaParaLeitosCDIDR()
	{
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaConsolidadoUrgencia();
		Arquivo arquivo = new Arquivo(caminhoArquivo, diretoriosCDIDR.getNomeArquivoConsolidado());
		
		String pastaRelatorioCDIDR = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaConsolidadoLeitosCDIDR();
		
		String nomeArquivo = arquivo.getNomeDoArquivo();
		//LocalDate data = LocalDate.now();
		//nomeArquivo  = nomeArquivo.replace(".xlsx", " - " + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx");
		
		arquivo.CopiarArquivo(pastaRelatorioCDIDR + "\\" + nomeArquivo);
		
		return "";
	}
	
	private void setDadosReferenciaisParaAutomatizacaoDeLogin(boolean setCaminho, String ambiente)
	{
		ultimaPlanilhaProcessadaNoDia = ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.NOME_PLANILHA_MONITORAMENTO.getDescricao();
		primeiraLinhaDaUltimaPlanilhaProcessadaNoDia = ParametrosArquivoUrgenciaPlanilhaProducaoRegulador.LINHA_INICIAL_ARQUIVO.getIndice();
		colunaDataDaUltimaPlanilhaProcessadaNoDia = ParametrosArquivoUrgenciaPlanilhaFichasFinalizadasAssumidasRegulador.INDICE_COLUNA_DATA.getIndice();
		
		if(setCaminho)
		{
			definirPastaCDIDR(ambiente);
			caminhoArquivoConsolidado = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia();
		}
	}
	
	private String definirPastaCDIDR(String ambiente)
	{
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
    	
    	return "";
	}
	
//	public String obterProducaoReguladorUrgencia(WebDriver driver, String ambiente, String data, String caminhoPastaBase, String caminhoPastaDownloads)
//	{			
//		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRUrgencia.valueOf(ambiente.toUpperCase());
//		
//		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
//    	
//		pastaBase = caminhoPastaBase;
//		pastaDownloads = caminhoPastaDownloads;
//		
//		if(data == null)
//			dataDeAnalise = JOptionPane.showInputDialog(null, "Insira a data do dia de análise (formato: dd/mm/yyyy)", "Data da Análise", JOptionPane.QUESTION_MESSAGE).trim();
//		else
//			dataDeAnalise = data;
//		
//		dataInformada = LocalDate.parse(dataDeAnalise, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//		dataFormatada = dataInformada.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//		
//		definirPastaCDIDR(ambiente);
//		
//		//gerarCopiaTemporariaRelatorioProducao();
//    	
//		
//		//Fichas Assumidas Regulador Detalhada
//    	ArrayList<UrgenciaFichasAssumidasRegulador> listaFichasAssumidasRegulador = new ArrayList<>();
//    	
//    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
//    		listaFichasAssumidasRegulador = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasRegulador.class, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
//        }
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return null;
//			
//		}
//    	
//		urgenciasFichasAssumidasRegulador = new HashMap<String, UrgenciaFichasAssumidasRegulador>();
//		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice();
//		
//		for(UrgenciaFichasAssumidasRegulador urgencia : listaFichasAssumidasRegulador)
//		{
//			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData(), "dd/MM/yyyy"));
//			urgencia.setLinhaExcel(linhaArquivo);
//			urgencia.setLinhaUtilizada(false);
//			
//			urgenciasFichasAssumidasRegulador.put(urgencia.getData() + urgencia.getRegulador() + urgencia.getPeriodo(), urgencia);
//			
//			linhaArquivo++;
//		}
//		
//		//Fichas Assumidas Regulador Detalhada Mensal
//    	ArrayList<UrgenciaFichasAssumidasReguladorMensal> listaFichasAssumidasReguladorMensal = new ArrayList<>();
//    	
//    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
//    		listaFichasAssumidasReguladorMensal = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasReguladorMensal.class, ParametrosArquivoUrgenciaPlanilhaFichasFinalizadasAssumidasReguladorMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasFinalizadasAssumidasReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
//        }
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//    	
//		urgenciasFichasAssumidasReguladorMensal = new HashMap<String, UrgenciaFichasAssumidasReguladorMensal>();
//		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFichasFinalizadasAssumidasReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice();
//		
//		for(UrgenciaFichasAssumidasReguladorMensal urgencia : listaFichasAssumidasReguladorMensal)
//		{
//			urgencia.setCompetencia(normalizarDataParaDiaMesAno(urgencia.getCompetencia(), "MMM/yyyy"));
//			urgencia.setLinhaExcel(linhaArquivo);
//			urgencia.setLinhaUtilizada(false);
//			
//			urgenciasFichasAssumidasReguladorMensal.put(urgencia.getCompetencia() + urgencia.getRegulador() + urgencia.getPeriodo(), urgencia);
//			
//			linhaArquivo++;
//		}
//		
//		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
//					
//		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
//		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
//
//		ArrayList<String> opcoes = new ArrayList<String>();
//		opcoes.add("Relatório");
//		opcoes.add("Acompanhamento Regulado");
//
//		String[] opcoesPeriodo = new String[2];
//		opcoesPeriodo[0] = IdentificadoresPaginaWebSIRESP.TEXTO_PERIODO_DIURNO.getTextoIdentificador();
//		opcoesPeriodo[1] = IdentificadoresPaginaWebSIRESP.TEXTO_PERIODO_NORTURNO.getTextoIdentificador();
//		
//		for(String opcaoPeriodo : opcoesPeriodo)
//		{
//		
//			boolean visivel;
//			do
//			{
//			
//				visivel = acessarMenu(driver, paginaWeb, opcoes);
//				
//			
//			}while(!visivel);
//			
//	
//			paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_ACOMPANHAMENTO_REGULADO_URGENCIA_DATA_INICIAL.getTextoIdentificador(), dataFormatada.replaceAll("-", ""));
//			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_ACOMPANHAMENTO_REGULADO_URGENCIA_PERIODO.getTextoIdentificador(), opcaoPeriodo);
//		
//			HashMap<String, DadosAcumuladosFichasAssumidas> fichasAssumidas = new HashMap<String, DadosAcumuladosFichasAssumidas>();
//		
//			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_ACOMPANHAMENTO_REGULADO_URGENCIA_BOTAO_PESQUISAR.getTextoIdentificador());
//			
//			while(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_ACOMPANHAMENTO_REGULADO_TABELA_RESULTADOS.getTextoIdentificador()))
//			{
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_ACOMPANHAMENTO_REGULADO_TABELA_RESULTADOS.getTextoIdentificador());
//			
//			montarDadosDeFichasAssumidas(dataInformada, fichasAssumidas, urgenciasFichasAssumidasRegulador, tabelaResultados);
//		}
//		
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String textoDataFinalizacao = dataInformada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//		
//		montarPlanilhaFichasAssumidas(dataInformada, textoDataFinalizacao, urgenciasFichasAssumidasRegulador);
//		montarPlanilhaFichasAssumidasMensal(dataInformada, urgenciasFichasAssumidasReguladorMensal);
//		
//		ordenarPlanilhaFichasAssumidas();
//		//atualizarCopiaOriginalRelatorioProducao();
//		//copiarRelatorioProducaoParaCDIDR();
//		copiarRelatorioUrgenciaParaCDIDR();
//		copiarRelatorioUrgenciaParaLeitosCDIDR();
//		
//		return "";	
//	}
//	
//	private String montarDadosDeFichasAssumidas(LocalDate dataHoraDeExtracao, String turno, HashMap<String, DadosAcumuladosFichasAssumidas> fichasAssumidas, HashMap<String, Integer> urgenciasFichasAssumidasRegulador, ArrayList<ArrayList<String>> tabelaResultados)
//	{
//		
//		String textoDataFinalizacao = dataHoraDeExtracao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//
//		int linhaTabela = ParametrosTabelaFichasAssumidasUrgencia.LINHA_INICIAL_TABELA.getIndice();
//		
//		ArrayList<String> linha = tabelaResultados.get(linhaTabela);
//		while(!linha.get(0).equals(ParametrosTabelaFichasAssumidasUrgencia.TEXTO_TOTAL.getDescricao()))
//		{
//			String regulador = linha.get(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOR.getIndice());
//			
//			String textoMapa = textoDataFinalizacao + regulador + turno;
//				
//				if(urgenciasAgrupadas.containsKey(textoMapa))
//				{
//					intervalos = urgenciasAgrupadas.get(textoMapa);
//				}
//				else
//				{
//					intervalos = criarEstruturaDeIntervalosDeUrgencia();
//					urgenciasAgrupadas.put(textoMapa, intervalos);
//				}
//				
//				for(IntervalosUrgencia intervalo : intervalos)
//					if(horas > intervalo.getInicioIntervalo() && horas <= intervalo.getFinalIntervalo())
//						intervalo.incrementarQuantidade();
//				
//				textoMapa += ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + horasExatas;
//				
//				if(urgenciasDetalhadas.containsKey(textoMapa))
//				{
//					int quantidade = urgenciasDetalhadas.get(textoMapa);
//					quantidade++;
//					urgenciasDetalhadas.put(textoMapa, quantidade);
//				}
//				else
//				{
//					urgenciasDetalhadas.put(textoMapa, 1);
//				}
//			}
//
//			String formaDeResolucao = registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_FORMA_DE_RESOLUÇÃO.getIndice()).trim();
//			//Vagas Zero
//			if(entidadesVagaZero.contains(entidadeExecutante))
//			{
//				String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao() + 
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_UNIDADE_EXECUTANTE.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
//				
//				DadosAcumuladosVagaZero vagaZero;
//				
//				if(vagasZero.containsKey(textoMapa))
//				{
//					vagaZero = vagasZero.get(textoMapa);
//				}
//				else
//				{
//					vagaZero = new DadosAcumuladosVagaZero();
//					vagasZero.put(textoMapa, vagaZero);
//				}
//				
//				if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_VAGA_ZERO.getDescricao()))
//				{
//					vagaZero.incrementarVagaZero();
//					vagaZero.incrementarTotal();
//				}
//				else if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getDescricao()))
//				{
//					vagaZero.incrementarEncaminhadoParaAvaliacaoDeComplexidadeAdequada();
//					vagaZero.incrementarTotal();
//				}
//				else if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getDescricao()))
//				{
//					vagaZero.incrementarEncaminhadoParaReferenciaPactuada();
//					vagaZero.incrementarTotal();
//				}
//				else if(formaDeResolucao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_AUTOMATICAMENTE_PAR_REFERENCIA_PACTUADA.getDescricao()))
//				{
//					vagaZero.incrementarEncaminhadoAutomaticamenteParaReferenciaPactuada();
//					vagaZero.incrementarTotal();
//				}
//			}
//			
//			String localDeRegulacao = registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_LOCAL_REGULACAO.getIndice()).trim();
//			
//			if(localDeRegulacao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_CENTRAL_MUNICIPAL_REGULACAO_CAMPINAS.getDescricao()) || entidadesFinalizacaoUrgencia.contains(entidadeExecutante))
//			{
//				String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + 
//						formaDeResolucao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				
//				if(entidadeExecutante.equals(""))
//					textoMapa += ParametrosArquivoUrgenciaPlanilhaFormaResolucao.TEXTO_EXECUTANTE_VAZIO.getDescricao() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				else
//					textoMapa += entidadeExecutante + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				
//				textoMapa += localDeRegulacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
//				
//				if(urgenciasFormaDeResolucao.containsKey(textoMapa))
//				{
//					int quantidade = urgenciasFormaDeResolucao.get(textoMapa);
//					quantidade++;
//					urgenciasFormaDeResolucao.put(textoMapa, quantidade);
//				}
//				else
//				{
//					urgenciasFormaDeResolucao.put(textoMapa, 1);
//				}
//			}
//			
//			if(localDeRegulacao.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_CENTRAL_MUNICIPAL_REGULACAO_CAMPINAS.getDescricao()))
//			{
//				String regulador = registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_REGULADOR_FINAl.getIndice());
//				
//				if(regulador.equals(ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_TRACO_VAZIO.getDescricao()))
//					regulador = ParametrosArquivoUrgenciaRelatorioProdutividade.TEXTO_SES_SP.getDescricao();
//				else
//				{
//					if(!reguladores.contains(regulador))
//						reguladores.add(regulador);
//				}
//				
//				String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + 
//						regulador + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				
//				if(entidadeExecutante.equals(""))
//					textoMapa += ParametrosArquivoUrgenciaPlanilhaFormaResolucao.TEXTO_EXECUTANTE_VAZIO.getDescricao() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				else
//					textoMapa += entidadeExecutante + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				
//				textoMapa += registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
//				
//				if(urgenciasProducaoRegulador.containsKey(textoMapa))
//				{
//					int quantidade = urgenciasProducaoRegulador.get(textoMapa);
//					quantidade++;
//					urgenciasProducaoRegulador.put(textoMapa, quantidade);
//				}
//				else
//				{
//					urgenciasProducaoRegulador.put(textoMapa, 1);
//				}
//			}
//			
//			String reguladorInicial = registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_REGULADOR_INICIAL.getIndice());
//			
//			if(reguladores.contains(reguladorInicial))
//			{
//				String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() + 
//						reguladorInicial + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				
//				if(entidadeExecutante.equals(""))
//					textoMapa += ParametrosArquivoUrgenciaPlanilhaFormaResolucao.TEXTO_EXECUTANTE_VAZIO.getDescricao() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				else
//					textoMapa += entidadeExecutante + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao();
//				
//				textoMapa += registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_RECURSO_SOLICITADO_1.getIndice()).trim() + ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado.DIVISOR_CAMPOS.getDescricao() +
//						registro.get(ParametrosArquivoUrgenciaRelatorioProdutividade.INDICE_COLUNA_TIPO_DE_FICHA.getIndice()).replace("Ficha ", "").trim();
//				
//				if(urgenciasFichasAssumidasRegulador.containsKey(textoMapa))
//				{
//					int quantidade = urgenciasFichasAssumidasRegulador.get(textoMapa);
//					quantidade++;
//					urgenciasFichasAssumidasRegulador.put(textoMapa, quantidade);
//				}
//				else
//				{
//					urgenciasFichasAssumidasRegulador.put(textoMapa, 1);
//				}
//			}
//			
//			linhaTabela++;
//			linha = tabelaResultados.get(linhaTabela);
//			
//		}
//		
//		return "";
//	}
//	
//
//	private String montarPlanilhaVagaZero(LocalDate dataHoraDeExtracao, String textoDataExtracao, HashMap<String, DadosAcumuladosVagaZero> vagasZero)
//	{
//		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
//		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
//		
//		int linhaArquivo = arquivoCenso.getUltimaLinhaPreenchida();
//		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
//		
//		for(String chave : vagasZero.keySet())
//		{
//			DadosAcumuladosVagaZero dadosVagaZero = vagasZero.get(chave);
//			
//			if(dadosVagaZero.getTotal() > 0)
//			{
//			
//				String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao(), "");
//				
//				UrgenciaVagaZero urgencia;
//				int linha;
//				if(urgenciasVagaZero.containsKey(chaveJaRegistrada))
//				{
//					urgencia = urgenciasVagaZero.get(chaveJaRegistrada);
//					urgencia.setLinhaUtilizada(true);
//					
//					linha = urgencia.getLinhaExcel();
//				}
//				else
//				{
//					urgencia = new UrgenciaVagaZero();
//					urgencia.setData(textoDataExtracao);
//					urgencia.setExecutante(chave.split(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao())[1]);
//					urgencia.setRecurso(chave.split(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao())[2]);
//					urgencia.setFicha(chave.split(ParametrosArquivoUrgenciaPlanilhaVagaZero.DIVISOR_CAMPOS.getDescricao())[3]);
//					
//					linhaArquivo++;
//					linha = linhaArquivo;
//					urgencia.setLinhaExcel(linha);
//					
//					urgenciasVagaZero.put(chaveJaRegistrada, urgencia);
//					
//				}
//				
//				//System.out.println(textoDataExtracao);
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), "String"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), "String"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), "String"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_TOTAL.getIndice(), dadosVagaZero.getTotal(), "Integer"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_VAGA_ZERO.getIndice(), dadosVagaZero.getVagaZero(), "Integer"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getIndice(), dadosVagaZero.getEncaminhadoParaReferenciaPactuada(), "Integer"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getIndice(), dadosVagaZero.getEncaminhadoParaAvaliacaoDeComplexidadeAdequada(), "Integer"));
//				celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_AUTOMATICAMENTE_PARA_REFERENCIA_PACTUADA.getIndice(), dadosVagaZero.getEncaminhadoAutomaticamenteParaReferenciaPactuada(), "Integer"));
//			}
//		}
//		
//		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice(), null);
//		
//		return "";
//	}
//	
//
//	
//	public boolean acessarMenu(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, ArrayList<String> opcoes)
//	{
//		paginaWeb.voltarAoTopoDaPagina(driver);	
//		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
//		
//		boolean visivel;
//		do
//		{
//			//buscando arquivos e baixando
//			paginaWeb.voltarAoTopoDaPagina(driver);
//		
//			//visivel = paginaWeb.clicarMenuUL(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes);
//		
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			visivel = paginaWeb.clicarMenuUL(driver, 2, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes, OpenStrategy.HOVER);
//			
//		
//		}while(!visivel);
//		
//		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return visivel;
//	}
//	
//	private static String normalizarDataParaDiaMesAno(String valor, String formato) {
//	    if (valor == null || valor.isBlank())
//	        return null;
//	
//	    DateTimeFormatter fmtMesAno = DateTimeFormatter.ofPattern(formato);
//	
//	    // 1️ Caso seja número serial do Excel
//	    if (valor.matches("\\d+")) {
//	        long serial = Long.parseLong(valor);
//	        LocalDate data = LocalDate.of(1899, 12, 30).plusDays(serial); // Ajuste Excel
//	        return fmtMesAno.format(data);
//	    }
//	
//	    // 2️ Caso seja dd/MM/yyyy
//	    try {
//	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
//	        return fmtMesAno.format(data);
//	    } catch (DateTimeParseException e) {
//	        // ignora e tenta o próximo formato
//	    }
//	    
//	    // 3️ Caso seja M/d/yy
//	    try {
//	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("M/d/yy");
//	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
//	        return fmtMesAno.format(data);
//	    } catch (DateTimeParseException e) {
//	        // ignora e tenta o próximo formato
//	    }
//	
//	    // 4 Caso seja mmm/yyyy (direto do Excel ou do POI)
//	    try {
//	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy");
//	        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy"));
//	        return fmtMesAno.format(data);
//	    } catch (DateTimeParseException e) {
//	        // ignora e vai para erro final
//	    }
//	
//	    throw new IllegalArgumentException("Formato de data inválido: " + valor);
//	}
//	
//	private static String normalizarDataParaAnoMesDia(String valor) {
//	    if (valor == null || valor.isBlank())
//	        return null;
//	
//	    Locale localeBR = Locale.of("pt", "BR"); // Java 21
//	    DateTimeFormatter fmtAnoMes = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//	
//	    // 1️ Caso seja número serial do Excel
//	    if (valor.matches("\\d+")) {
//	        long serial = Long.parseLong(valor);
//	        LocalDate data = LocalDate.of(1899, 12, 30).plusDays(serial); // Ajuste Excel
//	        return fmtAnoMes.format(data);
//	    }
//	
//	    // 2️ Caso seja dd/MM/yyyy
//	    try {
//	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
//	        return fmtAnoMes.format(data);
//	    } catch (DateTimeParseException e) {
//	        // ignora e tenta o próximo formato
//	    }
//	
//	    // 3️ Caso seja mmm/yyyy (direto do Excel ou do POI)
//	    try {
//	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
//	        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy", localeBR));
//	        return fmtAnoMes.format(data);
//	    } catch (DateTimeParseException e) {
//	        // ignora e vai para erro final
//	    }
//	
//	    throw new IllegalArgumentException("Formato de data inválido: " + valor);
//	}
//	
//	
//	
//	public String ordenarPlanilhaVagaZero()
//	{
//		ArrayList<UrgenciaVagaZero> listaUrgencias = new ArrayList<UrgenciaVagaZero>();
//    	
//    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
//    	
//    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
//    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaVagaZero.class, ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
//        }
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return null;
//			
//		}
//		
//		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice();
//		
//		for(UrgenciaVagaZero urgencia : listaUrgencias)
//		{
//			String dataExtracao = urgencia.getData();
//			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao, "dd/MM/yyyy"));
//			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
//		}
//		
//		Collections.sort(listaUrgencias, Comparator
//		    .comparing(UrgenciaVagaZero::getDataOrdenacao).reversed()
//		    .thenComparing(UrgenciaVagaZero::getExecutante)
//		    .thenComparing(UrgenciaVagaZero::getRecurso)
//		    .thenComparing(UrgenciaVagaZero::getFicha)
//		);		
//		
//		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
//		
//		for(UrgenciaVagaZero urgencia : listaUrgencias)
//		{
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_DATA.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_EXECUTANTE.getIndice(), urgencia.getExecutante(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_EXECUTANTE.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_RECURSO.getIndice(), urgencia.getRecurso(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_RECURSO.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_FICHA.getIndice(), urgencia.getFicha(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_FICHA.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_TOTAL.getIndice(), urgencia.getTotal(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_TOTAL.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_VAGA_ZERO.getIndice(), urgencia.getVagaZero(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_VAGA_ZERO.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getIndice(), urgencia.getEncaminhadoParaReferenciaPactuada(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_REFERENCIA_PACTUADA.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getIndice(), urgencia.getEncaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA.getTipo()));
//			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_AUTOMATICAMENTE_PARA_REFERENCIA_PACTUADA.getIndice(), urgencia.getEncaminhadoAutomaticamenteParaReferenciaPactuada(), ParametrosArquivoUrgenciaPlanilhaVagaZero.INDICE_COLUNA_ENCAMINHADO_AUTOMATICAMENTE_PARA_REFERENCIA_PACTUADA.getTipo()));
//
//			
//			linhaArquivo++;
//		}
//		
//		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
//		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
//		
//		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaVagaZero.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaVagaZero.LINHA_INICIAL_ARQUIVO.getIndice(), null);
//		
//		return "";
//	}
//	
//	
//	private CelulaExcel criarCelula(int linha, int coluna, String valor, String tipo)
//	{
//		CelulaExcel celula = null;
//		
//		if(tipo.equals("String"))
//			celula = new CelulaExcel(linha, coluna, valor, tipo);
//		else if(tipo.equals("Int"))
//		{
//			try
//			{
//				int valorInteiro = Integer.parseInt(valor);
//				celula = new CelulaExcel(linha, coluna, valorInteiro, tipo);
//			}
//			catch(NumberFormatException e)
//			{
//				celula = new CelulaExcel(linha, coluna, valor, "String");
//			}
//		}
//		else if(tipo.equals("Porcentagem"))
//		{
//			try
//			{
//				String valorReal = valor.replace("%", "").replace(",", ".");
//				Double valorPorcentagem = Double.parseDouble(valorReal)/100;
//				celula = new CelulaExcel(linha, coluna, valorPorcentagem, tipo);
//			}
//			catch(NumberFormatException e)
//			{
//				celula = new CelulaExcel(linha, coluna, valor, "String");
//			}
//		}
//		else if(tipo.equals("Date"))
//		{
//			 try 
//			 {
//		        LocalDate data = LocalDate.parse(valor, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//
//		        celula = new CelulaExcel(linha, coluna, data, tipo);
//		        
//			 } catch (DateTimeParseException e) {
//		    	celula = new CelulaExcel(linha, coluna, valor, "String");
//		    }
//		}
//		else if(tipo.equals("Time"))
//		{
//			 try 
//			 {
//		        LocalTime horario = LocalTime.parse(valor, DateTimeFormatter.ofPattern("HH:mm:ss"));
//
//		        celula = new CelulaExcel(linha, coluna, horario, tipo);
//		        
//			 } catch (DateTimeParseException e) {
//		    	celula = new CelulaExcel(linha, coluna, valor, "String");
//		    }
//		}
//		else if(tipo.equals("Date mes/ano"))
//		{
//			 try 
//			 {
//				Locale localeBR = Locale.of("pt", "BR"); // Java 21
//		        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
//		        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy", localeBR));
//
//		        celula = new CelulaExcel(linha, coluna, data, tipo);
//		        
//			 } catch (DateTimeParseException e) {
//		    	celula = new CelulaExcel(linha, coluna, valor, "String");
//		    }
//		}
//		
//		return celula;
//	}
//
//	public String getUltimaPlanilhaProcessadaNoDia() {
//		return ultimaPlanilhaProcessadaNoDia;
//	}
//
//	public void setUltimaPlanilhaProcessadaNoDia(String ultimaPlanilhaProcessadaNoDia) {
//		this.ultimaPlanilhaProcessadaNoDia = ultimaPlanilhaProcessadaNoDia;
//	}
//
//	public int getPrimeiraLinhaDaUltimaPlanilhaProcessadaNoDia() {
//		return primeiraLinhaDaUltimaPlanilhaProcessadaNoDia;
//	}
//
//	public void setPrimeiraLinhaDaUltimaPlanilhaProcessadaNoDia(int primeiraLinhaDaUltimaPlanilhaProcessadaNoDia) {
//		this.primeiraLinhaDaUltimaPlanilhaProcessadaNoDia = primeiraLinhaDaUltimaPlanilhaProcessadaNoDia;
//	}
//
//	public int getColunaDataDaUltimaPlanilhaProcessadaNoDia() {
//		return colunaDataDaUltimaPlanilhaProcessadaNoDia;
//	}
//
//	public void setColunaDataDaUltimaPlanilhaProcessadaNoDia(int colunaDataDaUltimaPlanilhaProcessadaNoDia) {
//		this.colunaDataDaUltimaPlanilhaProcessadaNoDia = colunaDataDaUltimaPlanilhaProcessadaNoDia;
//	}
//
//	public String getCaminhoArquivoConsolidado() {
//		return caminhoArquivoConsolidado;
//	}
//
//	public void setCaminhoArquivoConsolidado(String caminhoArquivoConsolidado) {
//		this.caminhoArquivoConsolidado = caminhoArquivoConsolidado;
//	}
	
}
