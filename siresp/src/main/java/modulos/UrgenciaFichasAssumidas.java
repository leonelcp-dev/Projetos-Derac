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
import dadosGerais.IdentificadoresPastasCompartilhadasCDTI;
import dadosGerais.ParametrosArquivoOfertaDemanda;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaAguardandoDetalhado;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFichasAssumidas;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal;
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
	private String pastaBaseCDTI;
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
	private IdentificadoresPastasCompartilhadasCDTI diretoriosCDTI;

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
	
	private String copiarRelatorioProducaoParaCDTI()
	{
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaConsolidadoUrgencia();
		Arquivo arquivo = new Arquivo(caminhoArquivo, diretoriosCDIDR.getNomeArquivoConsolidado());
		
		String pastaRelatorioCDTI = pastaBaseCDTI + "\\" + diretoriosCDTI.getPastaLeitosUrgencia();
		
		String nomeArquivo = arquivo.getNomeDoArquivo();
		
		arquivo.CopiarArquivo(pastaRelatorioCDTI + "\\" + nomeArquivo);
		
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
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDTI.REFERENCIA_PASTAS_CDTI.getTextoIdentificador()))
				pastaBaseCDTI = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDTI.REFERENCIA_PASTAS_CDTI.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Demanda Reprimida compartilhada CDTI");
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
	
	public String obterProducaoReguladorUrgencia(WebDriver driver, String ambiente, String data, String caminhoPastaBase, String caminhoPastaDownloads)
	{			
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRUrgencia.valueOf(ambiente.toUpperCase());
		diretoriosCDTI = IdentificadoresPastasCompartilhadasCDTI.valueOf(ambiente.toUpperCase());
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
    	
		pastaBase = caminhoPastaBase;
		pastaDownloads = caminhoPastaDownloads;
		
		if(data == null)
			dataDeAnalise = JOptionPane.showInputDialog(null, "Insira a data do dia de análise (formato: dd/mm/yyyy)", "Data da Análise", JOptionPane.QUESTION_MESSAGE).trim();
		else
			dataDeAnalise = data;
		
		dataInformada = LocalDate.parse(dataDeAnalise, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		dataFormatada = dataInformada.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		
		definirPastaCDIDR(ambiente);
		
		//gerarCopiaTemporariaRelatorioProducao();
    	
		
		//Fichas Assumidas Regulador Detalhada
    	ArrayList<UrgenciaFichasAssumidasRegulador> listaFichasAssumidasRegulador = new ArrayList<>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaFichasAssumidasRegulador = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasRegulador.class, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
		urgenciasFichasAssumidasRegulador = new HashMap<String, UrgenciaFichasAssumidasRegulador>();
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFichasAssumidasRegulador urgencia : listaFichasAssumidasRegulador)
		{
			urgencia.setData(normalizarDataParaDiaMesAno(urgencia.getData(), "dd/MM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasFichasAssumidasRegulador.put(urgencia.getData() + urgencia.getRegulador() + urgencia.getPeriodo(), urgencia);
			
			linhaArquivo++;
		}
		
		//Fichas Assumidas Regulador Detalhada Mensal
    	ArrayList<UrgenciaFichasAssumidasReguladorMensal> listaFichasAssumidasReguladorMensal = new ArrayList<>();
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaFichasAssumidasReguladorMensal = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasReguladorMensal.class, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasFinalizadasAssumidasReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
    	
		urgenciasFichasAssumidasReguladorMensal = new HashMap<String, UrgenciaFichasAssumidasReguladorMensal>();
		linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFichasAssumidasReguladorMensal urgencia : listaFichasAssumidasReguladorMensal)
		{
			urgencia.setCompetencia(normalizarDataParaDiaMesAno(urgencia.getCompetencia(), "MMM/yyyy"));
			urgencia.setLinhaExcel(linhaArquivo);
			urgencia.setLinhaUtilizada(false);
			
			urgenciasFichasAssumidasReguladorMensal.put(urgencia.getCompetencia() + urgencia.getRegulador() + urgencia.getPeriodo(), urgencia);
			
			linhaArquivo++;
		}
		
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
					
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());

		ArrayList<String> opcoes = new ArrayList<String>();
		opcoes.add("Relatório");
		opcoes.add("Acompanhamento Regulado");

		String[] opcoesPeriodo = new String[2];
		opcoesPeriodo[0] = IdentificadoresPaginaWebSIRESP.TEXTO_PERIODO_DIURNO.getTextoIdentificador();
		opcoesPeriodo[1] = IdentificadoresPaginaWebSIRESP.TEXTO_PERIODO_NORTURNO.getTextoIdentificador();
		
		HashMap<String, DadosAcumuladosFichasAssumidas> fichasAssumidas = new HashMap<String, DadosAcumuladosFichasAssumidas>();
		
		for(String opcaoPeriodo : opcoesPeriodo)
		{
		
			boolean visivel;
			do
			{
			
				visivel = acessarMenu(driver, paginaWeb, opcoes);
				
			
			}while(!visivel);
			
	
			paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_ACOMPANHAMENTO_REGULADO_URGENCIA_DATA_INICIAL.getTextoIdentificador(), dataFormatada.replaceAll("-", ""));
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_ACOMPANHAMENTO_REGULADO_URGENCIA_PERIODO.getTextoIdentificador(), opcaoPeriodo);
		
			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_ACOMPANHAMENTO_REGULADO_URGENCIA_BOTAO_PESQUISAR.getTextoIdentificador());
			
			while(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_ACOMPANHAMENTO_REGULADO_TABELA_RESULTADOS.getTextoIdentificador()))
			{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_ACOMPANHAMENTO_REGULADO_TABELA_RESULTADOS.getTextoIdentificador());
			
			montarDadosDeFichasAssumidas(dataInformada, opcaoPeriodo, fichasAssumidas, tabelaResultados);
		}
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String textoDataFinalizacao = dataInformada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		montarPlanilhaFichasAssumidas(dataInformada, textoDataFinalizacao, fichasAssumidas);
		
		HashMap<String, DadosAcumuladosFichasAssumidas> fichasAssumidasMensais = new HashMap<String, DadosAcumuladosFichasAssumidas>();
		montarDadosFichasAssumidasMensal(fichasAssumidasMensais);
		
		montarPlanilhaFichasAssumidasMensais(dataInformada, fichasAssumidasMensais);
		
		preencherDataDeProcessamento();
		ordenarPlanilhaFichasAssumidas();
		ordenarPlanilhaFichasAssumidasMensais();
		//atualizarCopiaOriginalRelatorioProducao();
		//copiarRelatorioProducaoParaCDIDR();
		copiarRelatorioUrgenciaParaCDIDR();
		copiarRelatorioUrgenciaParaLeitosCDIDR();
		copiarRelatorioProducaoParaCDTI();
		
		return "";	
	}
	
	private String montarDadosDeFichasAssumidas(LocalDate dataHoraDeExtracao, String turno, HashMap<String, DadosAcumuladosFichasAssumidas> fichasAssumidasPorRegulador, ArrayList<ArrayList<String>> tabelaResultados)
	{
		
		String textoDataFinalizacao = dataHoraDeExtracao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		DadosAcumuladosFichasAssumidas fichasAssumidas;

		int linhaTabela = ParametrosTabelaFichasAssumidasUrgencia.LINHA_INICIAL_TABELA.getIndice();
		
		ArrayList<String> linha = tabelaResultados.get(linhaTabela);
		while(!linha.get(0).equals(ParametrosTabelaFichasAssumidasUrgencia.TEXTO_TOTAL.getDescricao()))
		{
			String regulador = linha.get(ParametrosTabelaFichasAssumidasUrgencia.INDICE_COLUNA_MEDICO_REGULADOR.getIndice());
			String casosAssumidos = linha.get(ParametrosTabelaFichasAssumidasUrgencia.INDICE_COLUNA_CASOS_ASSUMIDOS.getIndice());
			String casosEncaminhados = linha.get(ParametrosTabelaFichasAssumidasUrgencia.INDICE_COLUNA_CASOS_ENCAMINHADOS.getIndice());
			String casosRegulados = linha.get(ParametrosTabelaFichasAssumidasUrgencia.INDICE_COLUNA_CASOS_REGULADOS_QUANTIDADE.getIndice());
			String casosPendentes = linha.get(ParametrosTabelaFichasAssumidasUrgencia.INDICE_COLUNA_CASOS_PENDENTES_QUANTIDADE.getIndice());
		
			String textoMapa = textoDataFinalizacao + ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.DIVISOR_CAMPOS.getDescricao() +  
							   regulador + ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.DIVISOR_CAMPOS.getDescricao() +
							   turno;

			fichasAssumidas = new DadosAcumuladosFichasAssumidas();
			
			if(casosAssumidos.equals(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.TEXTO_CELULA_VAZIA.getDescricao()))
				fichasAssumidas.setAssumidos(0);
			else
				fichasAssumidas.setAssumidos(Integer.parseInt(casosAssumidos));
			
			if(casosEncaminhados.equals(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.TEXTO_CELULA_VAZIA.getDescricao()))
				fichasAssumidas.setEncaminhados(0);
			else
				fichasAssumidas.setEncaminhados(Integer.parseInt(casosEncaminhados));
			
			if(casosRegulados.equals(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.TEXTO_CELULA_VAZIA.getDescricao()))
				fichasAssumidas.setRegulados(0);
			else
				fichasAssumidas.setRegulados(Integer.parseInt(casosRegulados));
			
			if(casosPendentes.equals(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.TEXTO_CELULA_VAZIA.getDescricao()))
				fichasAssumidas.setPendentes(0);
			else
				fichasAssumidas.setPendentes(Integer.parseInt(casosPendentes));

			fichasAssumidasPorRegulador.put(textoMapa, fichasAssumidas);
			
			linhaTabela++;
			linha = tabelaResultados.get(linhaTabela);
			
		}
		
		return "";
	}
	

	private String montarPlanilhaFichasAssumidas(LocalDate dataExtracao, String textoDataExtracao, HashMap<String, DadosAcumuladosFichasAssumidas> fichasAssumidasPorRegulador)
	{
		AcoesArquivoExcel arquivoUrgencia = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoUrgencia.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
				
		int linhaArquivo = arquivoUrgencia.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOR.getIndice());
		System.out.println("Diário: " + linhaArquivo);
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : fichasAssumidasPorRegulador.keySet())
		{
			DadosAcumuladosFichasAssumidas dadosFichasAssumidas = fichasAssumidasPorRegulador.get(chave);
			
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.DIVISOR_CAMPOS.getDescricao(), "");
			
			UrgenciaFichasAssumidasRegulador urgencia;
			int linha;
			if(urgenciasFichasAssumidasRegulador.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasFichasAssumidasRegulador.get(chaveJaRegistrada);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				urgencia = new UrgenciaFichasAssumidasRegulador();
				urgencia.setData(textoDataExtracao);
				urgencia.setRegulador(chave.split(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.DIVISOR_CAMPOS.getDescricao())[1]);
				urgencia.setPeriodo(chave.split(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.DIVISOR_CAMPOS.getDescricao())[2]);
				
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
				urgenciasFichasAssumidasRegulador.put(chaveJaRegistrada, urgencia);
				
			}
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_DATA.getIndice(), LocalDate.parse(textoDataExtracao, DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_PERIODO.getIndice(), urgencia.getPeriodo(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_ASSUMIDOS.getIndice(), dadosFichasAssumidas.getAssumidos(), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_ENCAMINHADOS.getIndice(), dadosFichasAssumidas.getEncaminhados(), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOS.getIndice(), dadosFichasAssumidas.getRegulados(), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_PENDENTES.getIndice(), dadosFichasAssumidas.getPendentes(), "Integer"));
		}
		
		arquivoUrgencia.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String montarPlanilhaFichasAssumidasMensais(LocalDate dataExtracao, HashMap<String, DadosAcumuladosFichasAssumidas> fichasAssumidasPorRegulador)
	{
		AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoCenso.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoCenso.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.LINHA_INICIAL_ARQUIVO.getIndice(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_REGULADOR.getIndice());
		System.out.println("Mensal: " + linhaArquivo);
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : fichasAssumidasPorRegulador.keySet())
		{
			String chaveJaRegistrada = chave.replaceAll(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.DIVISOR_CAMPOS.getDescricao(), "");
			
			UrgenciaFichasAssumidasReguladorMensal urgencia;
			int linha;
			
			System.out.println(chaveJaRegistrada);
			if(urgenciasFichasAssumidasReguladorMensal.containsKey(chaveJaRegistrada))
			{
				urgencia = urgenciasFichasAssumidasReguladorMensal.get(chaveJaRegistrada);
				urgencia.setLinhaUtilizada(true);
				
				linha = urgencia.getLinhaExcel();
			}
			else
			{
				String[] componentesChave = chave.split(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.DIVISOR_CAMPOS.getDescricao());
				
				urgencia = new UrgenciaFichasAssumidasReguladorMensal();
				urgencia.setCompetencia(componentesChave[0]);
				urgencia.setRegulador(componentesChave[1]);
				urgencia.setPeriodo(componentesChave[2]);
								
				linhaArquivo++;
				linha = linhaArquivo;
				urgencia.setLinhaExcel(linha);
				
				urgenciasFichasAssumidasReguladorMensal.put(chaveJaRegistrada, urgencia);
				
			}
			
			DadosAcumuladosFichasAssumidas fichas = fichasAssumidasPorRegulador.get(chave);
			
			//System.out.println(textoDataExtracao);
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_COMPETENCIA.getIndice(), urgencia.getCompetencia(), "Date"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_PERIODO.getIndice(), urgencia.getPeriodo(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), "String"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_ASSUMIDOS.getIndice(), fichas.getAssumidos(), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_ENCAMINHADOS.getIndice(), fichas.getEncaminhados(), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_REGULADOS.getIndice(), fichas.getRegulados(), "Integer"));
			celulas.add(new CelulaExcel(urgencia.getLinhaExcel(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_PENDENTES.getIndice(), fichas.getPendentes(), "Integer"));
			
		}
		
		arquivoCenso.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String montarPlanilhaFichasAssumidas(HashMap<String, DadosAcumuladosFichasAssumidas> producaoReguladorMensal)
	{
		ArrayList<UrgenciaFichasAssumidasRegulador> listaUrgencias = new ArrayList<UrgenciaFichasAssumidasRegulador>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasRegulador.class, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
	
		for(UrgenciaFichasAssumidasRegulador urgencia : listaUrgencias)
		{
			String competencia = normalizarDataParaDiaMesAno(urgencia.getData(), "MMM/yyyy");

			String chave = competencia + ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.DIVISOR_CAMPOS.getDescricao() + urgencia.getRegulador() +  ParametrosArquivoUrgenciaPlanilhaProducaoReguladorMensal.DIVISOR_CAMPOS.getDescricao() + urgencia.getPeriodo();
			if(producaoReguladorMensal.containsKey(chave))
			{
				DadosAcumuladosFichasAssumidas fichas = producaoReguladorMensal.get(chave);
				fichas.setAssumidos(fichas.getAssumidos() + Integer.parseInt(urgencia.getAssumidos()));
				fichas.setEncaminhados(fichas.getEncaminhados() + Integer.parseInt(urgencia.getEncaminhados()));
				fichas.setRegulados(fichas.getRegulados() + Integer.parseInt(urgencia.getRegulados()));
				fichas.setPendentes(fichas.getPendentes() + Integer.parseInt(urgencia.getPendentes()));
				
				producaoReguladorMensal.put(chave, fichas);
			}
			else
			{
				DadosAcumuladosFichasAssumidas fichas = new DadosAcumuladosFichasAssumidas();
				fichas.setAssumidos(Integer.parseInt(urgencia.getAssumidos()));
				fichas.setEncaminhados(Integer.parseInt(urgencia.getEncaminhados()));
				fichas.setRegulados(Integer.parseInt(urgencia.getRegulados()));
				fichas.setPendentes(Integer.parseInt(urgencia.getPendentes()));
				
				producaoReguladorMensal.put(chave, fichas);
			}
		}
		
		return "";
	}
	
	public String montarDadosFichasAssumidasMensal(HashMap<String, DadosAcumuladosFichasAssumidas> producaoReguladorMensal)
	{
		ArrayList<UrgenciaFichasAssumidasRegulador> listaUrgencias = new ArrayList<UrgenciaFichasAssumidasRegulador>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasRegulador.class, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
	
		for(UrgenciaFichasAssumidasRegulador urgencia : listaUrgencias)
		{
			String competencia = normalizarDataParaDiaMesAno(urgencia.getData(), "MMM/yyyy");

			String chave = competencia + ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.DIVISOR_CAMPOS.getDescricao() + urgencia.getRegulador() +  ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.DIVISOR_CAMPOS.getDescricao() + urgencia.getPeriodo();
			if(producaoReguladorMensal.containsKey(chave))
			{
				DadosAcumuladosFichasAssumidas fichas = producaoReguladorMensal.get(chave);
				fichas.setAssumidos(fichas.getAssumidos() + Integer.parseInt(urgencia.getAssumidos()));
				fichas.setEncaminhados(fichas.getEncaminhados() + Integer.parseInt(urgencia.getEncaminhados()));
				fichas.setRegulados(fichas.getRegulados() + Integer.parseInt(urgencia.getRegulados()));
				fichas.setPendentes(fichas.getPendentes() + Integer.parseInt(urgencia.getPendentes()));
				
				producaoReguladorMensal.put(chave, fichas);
			}
			else
			{
				DadosAcumuladosFichasAssumidas fichas = new DadosAcumuladosFichasAssumidas();
				fichas.setAssumidos(Integer.parseInt(urgencia.getAssumidos()));
				fichas.setEncaminhados(Integer.parseInt(urgencia.getEncaminhados()));
				fichas.setRegulados(Integer.parseInt(urgencia.getRegulados()));
				fichas.setPendentes(Integer.parseInt(urgencia.getPendentes()));
				
				producaoReguladorMensal.put(chave, fichas);
			}
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
	
	
	
	public String ordenarPlanilhaFichasAssumidas()
	{
		ArrayList<UrgenciaFichasAssumidasRegulador> listaUrgencias = new ArrayList<UrgenciaFichasAssumidasRegulador>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasRegulador.class, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFichasAssumidasRegulador urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getData();
			urgencia.setData(normalizarDataParaDiaMesAno(dataExtracao, "dd/MM/yyyy"));
			urgencia.setDataOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaFichasAssumidasRegulador::getDataOrdenacao).reversed()
		    .thenComparing(UrgenciaFichasAssumidasRegulador::getPeriodo)
		    .thenComparing(UrgenciaFichasAssumidasRegulador::getRegulador)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaFichasAssumidasRegulador urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_DATA.getIndice(), urgencia.getData(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOR.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_PERIODO.getIndice(), urgencia.getPeriodo(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_PERIODO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_ASSUMIDOS.getIndice(), urgencia.getAssumidos(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_ASSUMIDOS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_ENCAMINHADOS.getIndice(), urgencia.getEncaminhados(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_ENCAMINHADOS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOS.getIndice(), urgencia.getRegulados(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_REGULADOS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_PENDENTES.getIndice(), urgencia.getPendentes(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_PENDENTES.getTipo()));
			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaFichasAssumidasMensais()
	{
		ArrayList<UrgenciaFichasAssumidasReguladorMensal> listaUrgencias = new ArrayList<UrgenciaFichasAssumidasReguladorMensal>();
    	
    	System.out.println(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia());
    	
    	try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
    		listaUrgencias = ExcelBinder.readSheet(in, UrgenciaFichasAssumidasReguladorMensal.class, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivo = ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(UrgenciaFichasAssumidasReguladorMensal urgencia : listaUrgencias)
		{
			String dataExtracao = urgencia.getCompetencia();
			urgencia.setCompetencia(normalizarDataParaDiaMesAno(dataExtracao, "MMM/yyyy"));
			urgencia.setCompetenciaOrdenacao(normalizarDataParaAnoMesDia(dataExtracao));
		}
		
		Collections.sort(listaUrgencias, Comparator
		    .comparing(UrgenciaFichasAssumidasReguladorMensal::getCompetenciaOrdenacao).reversed()
		    .thenComparing(UrgenciaFichasAssumidasReguladorMensal::getPeriodo)
		    .thenComparing(UrgenciaFichasAssumidasReguladorMensal::getRegulador)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(UrgenciaFichasAssumidasReguladorMensal urgencia : listaUrgencias)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_COMPETENCIA.getIndice(), urgencia.getCompetencia(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_COMPETENCIA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_REGULADOR.getIndice(), urgencia.getRegulador(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_REGULADOR.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_PERIODO.getIndice(), urgencia.getPeriodo(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_PERIODO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_ASSUMIDOS.getIndice(), urgencia.getAssumidos(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_ASSUMIDOS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_ENCAMINHADOS.getIndice(), urgencia.getEncaminhados(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_ENCAMINHADOS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_REGULADOS.getIndice(), urgencia.getRegulados(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_REGULADOS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_PENDENTES.getIndice(), urgencia.getPendentes(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_PENDENTES.getTipo()));
			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
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

	public String getUltimaPlanilhaProcessadaNoDia() {
		return ultimaPlanilhaProcessadaNoDia;
	}

	public void setUltimaPlanilhaProcessadaNoDia(String ultimaPlanilhaProcessadaNoDia) {
		this.ultimaPlanilhaProcessadaNoDia = ultimaPlanilhaProcessadaNoDia;
	}

	public int getPrimeiraLinhaDaUltimaPlanilhaProcessadaNoDia() {
		return primeiraLinhaDaUltimaPlanilhaProcessadaNoDia;
	}

	public void setPrimeiraLinhaDaUltimaPlanilhaProcessadaNoDia(int primeiraLinhaDaUltimaPlanilhaProcessadaNoDia) {
		this.primeiraLinhaDaUltimaPlanilhaProcessadaNoDia = primeiraLinhaDaUltimaPlanilhaProcessadaNoDia;
	}

	public int getColunaDataDaUltimaPlanilhaProcessadaNoDia() {
		return colunaDataDaUltimaPlanilhaProcessadaNoDia;
	}

	public void setColunaDataDaUltimaPlanilhaProcessadaNoDia(int colunaDataDaUltimaPlanilhaProcessadaNoDia) {
		this.colunaDataDaUltimaPlanilhaProcessadaNoDia = colunaDataDaUltimaPlanilhaProcessadaNoDia;
	}

	public String getCaminhoArquivoConsolidado() {
		return caminhoArquivoConsolidado;
	}

	public void setCaminhoArquivoConsolidado(String caminhoArquivoConsolidado) {
		this.caminhoArquivoConsolidado = caminhoArquivoConsolidado;
	}
	
	private String preencherDataDeProcessamento()
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		LocalDate dataHoje = LocalDate.now();
		
		celulas.add(new CelulaExcel(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_LINHA_DATA_PROCESSAMENTO.getIndice(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.INDICE_COLUNA_DATA_PROCESSAMENTO.getIndice(), dataHoje, "Date"));
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFichasAssumidas.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, false, false, 0, null);
		arquivoConsolidado.forcarCalculos();
		
		celulas.clear();
		celulas.add(new CelulaExcel(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_LINHA_DATA_PROCESSAMENTO.getIndice(), ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.INDICE_COLUNA_DATA_PROCESSAMENTO.getIndice(), dataHoje, "Date"));
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, false, false, 0, null);
		arquivoConsolidado.forcarCalculos();
		
		return "";
	}
}
