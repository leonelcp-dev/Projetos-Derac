package modulos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

import dadosGerais.CorrelacaoArquivosDemandaReprimida;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDR;
import dadosGerais.IdentificadoresPastasCompartilhadasCDRA;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoAgendamentosPendentesRegulada;
import dadosGerais.ParametrosArquivoDemandaReprimida;
import dadosGerais.ParametrosArquivoDemandaReprimidaCDR;
import dadosGerais.ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas;
import dadosGerais.ParametrosArquivoDemandaReprimidaRegulada;
import dadosGerais.ParametrosArquivoFilaCDRConsulta;
import dadosGerais.ParametrosArquivoFilaCDRExame;
import dadosGerais.ParametrosArquivoFilasNominais;
import dadosGerais.ParametrosArquivoFilasNominaisRegulada;
import dadosGerais.ParametrosArquivoNomenclaturas;
import dadosGerais.ParametrosArquivoNovasSolicitacoesConsolidado;
import dadosGerais.ParametrosArquivoReguladaConsolidado;
import dadosGerais.ParametrosArquivoSolicitacoesPendentesRegulada;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import modelosDados.CelulaExcel;
import modelosDados.CorrelacaoColunasArquivos;
import modelosDados.EntidadeExecutanteR1;
import modelosDados.NomenclaturaPadronizada;
import modelosDados.NovasSolicitacoesRegulada;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;
import utils.Utils;

public class DemandaReprimida {
	
	private String pastaBase;
	private String pastaArquivosFilasNominais;
	private String pastaArquivosDemandaReprimidaCDIDR;
	private String pastaArquivosDemandaReprimidaCDRA;
	private String composicaoPastaNoMes;
	private String dataDeAnalise;
	private LocalDate dataInformada;
	private String pastaBaseAmbulatorialCDIDR;
	private String pastaBaseDemandaReprimidaCDIDR;
	private String pastaBaseDemandaReprimidaCDRA;
	private MesesFormatados meses;
	private ArrayList<String> pastasPrincipais;
	private HashMap<String, String> relacaoUnidadeTipo;
	HashMap<String, NomenclaturaPadronizada> nomenclaturasPadronizadas;
	private IdentificadoresPastasCompartilhadasCDIDR diretoriosCDIDR; 
	private IdentificadoresPastasCompartilhadasCDRA diretoriosCDRA; 
	
	public String montarDemandaReprimidaDiaria(String ambiente)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDR.valueOf(ambiente.toUpperCase());
		diretoriosCDRA = IdentificadoresPastasCompartilhadasCDRA.valueOf(ambiente.toUpperCase());
		
		pastasPrincipais = new ArrayList<String>();
		pastasPrincipais.add("AGENDA REGULADA");
		pastasPrincipais.add("LESTE");
		pastasPrincipais.add("NOROESTE");
		pastasPrincipais.add("NORTE");
		pastasPrincipais.add("SUDOESTE");
		pastasPrincipais.add("SUL");
		pastasPrincipais.add("SULESTE");
		pastasPrincipais.add("DIVERSOS");
		
		pastaBase = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta compartilhada", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
				
		dataDeAnalise = JOptionPane.showInputDialog(null, "Insira a data do dia de análise (formato: dd/mm/yyyy)", "Data da Análise", JOptionPane.QUESTION_MESSAGE).trim();
		dataInformada = LocalDate.parse(dataDeAnalise, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		meses = new MesesFormatados();
		
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
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDRA.REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDRA.getTextoIdentificador()))
				pastaBaseDemandaReprimidaCDRA = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDRA.REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDRA.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Ambulatorial compartilhada");
				return "";
			}
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDR.REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDIDR.getTextoIdentificador()))
				pastaBaseDemandaReprimidaCDIDR = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDR.REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDIDR.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Demanda Reprimida compartilhada");
				return "";
			}
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDR.REFERENCIA_PASTAS_AMBULATORIAL.getTextoIdentificador()))
				pastaBaseAmbulatorialCDIDR = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDR.REFERENCIA_PASTAS_AMBULATORIAL.getTextoIdentificador());
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
		
		
		ArrayList<NomenclaturaPadronizada> nomenclaturas;
		
		try (FileInputStream in = new FileInputStream(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getCaminhoArquivoNomenclaturas())) {
			nomenclaturas = ExcelBinder.readSheet(in, NomenclaturaPadronizada.class, ParametrosArquivoNomenclaturas.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoNomenclaturas.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		nomenclaturasPadronizadas = new HashMap<String, NomenclaturaPadronizada>();
		for(NomenclaturaPadronizada nomenclatura : nomenclaturas)
		{
			nomenclatura.setInsercao(nomenclatura.getInsercao().replaceAll("\u00A0", ""));
			nomenclatura.setNomenclatura(nomenclatura.getNomenclatura().replaceAll("\u00A0", ""));
			nomenclatura.setFluxo(nomenclatura.getFluxo().replaceAll("\u00A0", ""));
			nomenclaturasPadronizadas.put(nomenclatura.getInsercao().trim().toUpperCase(), nomenclatura);
		}
		
		pastaArquivosFilasNominais = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaFilasNominais();
		pastaArquivosDemandaReprimidaCDIDR = pastaBaseDemandaReprimidaCDIDR + "\\" + diretoriosCDIDR.getArquivosDemandaReprimida();
		pastaArquivosDemandaReprimidaCDRA = pastaBaseDemandaReprimidaCDRA + "\\" + diretoriosCDRA.getArquivosDemandaReprimida();
		
		relacaoUnidadeTipo = new HashMap<String, String>();
		
		BufferedReader br;
		//obter tipos de unidades
		try {
			br = new BufferedReader(new FileReader(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoUnidadesDemandaReprimida(), StandardCharsets.ISO_8859_1));
			
			String linha;
			
			//lendo e descartando cabeçalho
			linha = br.readLine();
			
			while ((linha = br.readLine()) != null) {
			    String[] colunas = linha.split(";");
			    
			    relacaoUnidadeTipo.put(colunas[1], colunas[0]);
			}
			
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		consolidarDemandaReprimida(dataInformada, dataDeAnalise.replaceAll("/", "."));
		
		return "";
	}
	

	private String consolidarDemandaReprimida(LocalDate dataDaColeta, String dataFormatada)
	{
		int ano = dataDaColeta.getYear();
		int mes = dataDaColeta.getMonthValue();
		
		composicaoPastaNoMes = meses.getMeses().get(mes - 1).getMesNumero() + " " + meses.getMeses().get(mes - 1).getMesDescricao() + " " + ano;
		
		Pasta pasta = new Pasta(pastaArquivosFilasNominais + "\\" + ano + "\\" + composicaoPastaNoMes + "\\" + dataFormatada, false);
		
		String pastaDemandaReprimidaDoMes = pastaArquivosDemandaReprimidaCDIDR + "\\" + ano;
		Pasta pastaDemandaReprimida = new Pasta(pastaDemandaReprimidaDoMes, true);
		
		pastaDemandaReprimidaDoMes += "\\" + composicaoPastaNoMes;
		pastaDemandaReprimida = new Pasta(pastaDemandaReprimidaDoMes, true);
		
		Arquivo arquivo = new Arquivo(pastaArquivosDemandaReprimidaCDIDR, diretoriosCDIDR.getNomeArquivoDemandaReprimidaVazio());
		arquivo.CopiarArquivo(pastaDemandaReprimidaDoMes + "\\" + diretoriosCDIDR.getNomeArquivoDemandaReprimidaVazio());
		
		arquivo = new Arquivo(pastaDemandaReprimidaDoMes, diretoriosCDIDR.getNomeArquivoDemandaReprimidaVazio());
		arquivo.renomear(diretoriosCDIDR.getNomeArquivoDemandaReprimidaConsolidadoDiario().replace(IdentificadoresPastasCompartilhadasCDIDR.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), dataFormatada));
		
		procurarEConsolidarArquivo(pasta, false, "", "", arquivo.getCaminhoCompleto(), dataDaColeta);
		atualizarTabelasDinamicas(arquivo.getCaminhoCompleto());
		copiarTabelasDinamicas(arquivo.getCaminhoCompleto());
		ocultarPlanilhasCopia(arquivo.getCaminhoCompleto());
		copiarDemandaReprimidaParaCDRA(arquivo, dataDaColeta);
		
		return "";
	}
	
	private String copiarDemandaReprimidaParaCDRA(Arquivo arquivo, LocalDate dataDaColeta)
	{
		int ano = dataDaColeta.getYear();
		int mes = dataDaColeta.getMonthValue();
		
		composicaoPastaNoMes = meses.getMeses().get(mes - 1).getMesNumero() + " " + meses.getMeses().get(mes - 1).getMesDescricao() + " " + ano;
		
		String pastaDemandaReprimidaDoMes = pastaArquivosDemandaReprimidaCDRA + "\\" + ano;
		Pasta pastaDemandaReprimida = new Pasta(pastaDemandaReprimidaDoMes, true);
		
		pastaDemandaReprimidaDoMes += "\\" + composicaoPastaNoMes;
		pastaDemandaReprimida = new Pasta(pastaDemandaReprimidaDoMes, true);
		
		arquivo.CopiarArquivo(pastaDemandaReprimidaDoMes + "\\" + arquivo.getNomeDoArquivo());
		
		return "";
	}
	
	private String atualizarTabelasDinamicas(String caminhoArquivo)
	{
		AcoesArquivoExcel arquivoDemandaReprimida = new AcoesArquivoExcel(caminhoArquivo, 0);
		
		arquivoDemandaReprimida.abrirPlanilha(ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_CDR.getDescricao(), 0);
		int ultimaLinhaPreenchida = arquivoDemandaReprimida.getUltimaLinhaPreenchida();
		String areaTabelaDinamica = ParametrosArquivoDemandaReprimidaCDR.AREA_PARA_TABELA_DINAMICA_CDR.getDescricao() + (ultimaLinhaPreenchida + 1);
		System.out.println(areaTabelaDinamica);
		arquivoDemandaReprimida.atualizarTabelasDinamicas(ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_DINAMICA_CDR.getDescricao(), ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_CDR.getDescricao(), areaTabelaDinamica);

		arquivoDemandaReprimida.abrirPlanilha(ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_REGULADA.getDescricao(), 0);
		ultimaLinhaPreenchida = arquivoDemandaReprimida.getUltimaLinhaPreenchida();
		areaTabelaDinamica = ParametrosArquivoDemandaReprimidaRegulada.AREA_PARA_TABELA_DINAMICA_REGULADA.getDescricao() + (ultimaLinhaPreenchida + 1);
		System.out.println(areaTabelaDinamica);
		arquivoDemandaReprimida.atualizarTabelasDinamicas(ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_DINAMICA_REGULADA.getDescricao(), ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_REGULADA.getDescricao(), areaTabelaDinamica);
		
		return "";
	}
	
	private String ocultarPlanilhasCopia(String caminhoArquivo)
	{
		AcoesArquivoExcel arquivoDemandaReprimida = new AcoesArquivoExcel(caminhoArquivo, 0);
		
		arquivoDemandaReprimida.ocultarPlanilha(ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_CONSOLIDADO_CDR.getDescricao());

		arquivoDemandaReprimida.ocultarPlanilha(ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_CONSOLIDADO_REGULADA.getDescricao());
		
		return "";
	}
	
	private String copiarTabelasDinamicas(String caminhoArquivo)
	{
		AcoesArquivoExcel arquivoDemandaReprimida = new AcoesArquivoExcel(caminhoArquivo, 0);
		
		String[][] planilhasDemandaReprimida = new String[2][2];
		planilhasDemandaReprimida[0][0] = ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_DINAMICA_CDR.getDescricao();
		planilhasDemandaReprimida[1][0] = ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_DINAMICA_REGULADA.getDescricao();
		planilhasDemandaReprimida[0][1] = ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_CONSOLIDADO_CDR.getDescricao();
		planilhasDemandaReprimida[1][1] = ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_CONSOLIDADO_REGULADA.getDescricao();
				
		for(String[] planilha : planilhasDemandaReprimida)
		{
			int linhaConsolidado = ParametrosArquivoDemandaReprimida.LINHA_INICIAL_TABELA_DINAMICA.getIndice();
			
			arquivoDemandaReprimida.abrirPlanilha(planilha[0], 0);
			
			int linha = arquivoDemandaReprimida.getPrimeiraLinhaPreenchidaComValorEmUmaColuna(ParametrosArquivoDemandaReprimida.TEXTO_ROTULOS_DE_LINHA.getDescricao(), ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice(), 50);
			
			ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
			
			if(linha >= 0)
			{
				System.out.println("Linha: " + linha);
				
				linha++;
				String especialidade = arquivoDemandaReprimida.getValorDaCelulaString(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice()).toUpperCase().trim();
				
				if(especialidade == null)
					especialidade = "";
				
				while(!especialidade.equals(ParametrosArquivoDemandaReprimida.TEXTO_TOTAL_GERAL.getDescricao().trim().toUpperCase()))
				{
					System.out.println(especialidade);
					
					celulas.add(new CelulaExcel(linhaConsolidado, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice(), especialidade, "String"));
					
					int demandaReprimida = arquivoDemandaReprimida.getValorDaCelulaInt(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_CONTAGEM_ESPECIALIDADE.getIndice());
					celulas.add(new CelulaExcel(linhaConsolidado, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_CONTAGEM_ESPECIALIDADE.getIndice(), demandaReprimida, "Int"));
					
					int maximoDias = arquivoDemandaReprimida.getValorDaCelulaInt(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_MAX_TEMPO_ESPERA.getIndice());
					celulas.add(new CelulaExcel(linhaConsolidado, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_MAX_TEMPO_ESPERA.getIndice(), maximoDias, "Int"));
					
					linha++;
					especialidade = arquivoDemandaReprimida.getValorDaCelulaString(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice()).toUpperCase().trim();;
					if(especialidade == null)
						especialidade = "";
					
					linhaConsolidado++;
				}
				
				celulas.add(new CelulaExcel(linhaConsolidado, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice(), especialidade, "String"));
				
				int demandaReprimida = arquivoDemandaReprimida.getValorDaCelulaInt(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_CONTAGEM_ESPECIALIDADE.getIndice());
				celulas.add(new CelulaExcel(linhaConsolidado, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_CONTAGEM_ESPECIALIDADE.getIndice(), demandaReprimida, "Int"));
				
				int maximoDias = arquivoDemandaReprimida.getValorDaCelulaInt(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_MAX_TEMPO_ESPERA.getIndice());
				celulas.add(new CelulaExcel(linhaConsolidado, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_MAX_TEMPO_ESPERA.getIndice(), maximoDias, "Int"));
				
				arquivoDemandaReprimida.gravarDadosEmCelula(planilha[1], celulas, false, false, 0, null);
				arquivoDemandaReprimida.forcarCalculos();
			}
		}
		
		return "";
	}
	
	private String procurarEConsolidarArquivo(Pasta pasta, boolean pastaRegulada, String nomePastaPrincipal, String consultaOuExame, String caminhoArquivo, LocalDate dataDaColeta)
	{
		System.out.println(pasta.getCaminhoDaPasta());
		File[] conteudoDaPasta = pasta.listarDiretorio();
		
		for(File itemDaPasta : conteudoDaPasta)
		{
			System.out.println(pasta.getPasta().getPath() + "\\" + itemDaPasta.getName());
			
			if(pasta.ehPasta(pasta.getPasta().getPath() + "\\" + itemDaPasta.getName()))
			{
				Pasta subpasta = new Pasta(pasta.getPasta().getPath() + "\\" + itemDaPasta.getName(), false);
				
				if(pastasPrincipais.contains(itemDaPasta.getName()))
					nomePastaPrincipal = itemDaPasta.getName();
				
				if(itemDaPasta.getName().equals("CONSULTA") || itemDaPasta.getName().equals("EXAME"))
					consultaOuExame = itemDaPasta.getName();
				
				if(itemDaPasta.isDirectory())
				{
					if(itemDaPasta.getAbsolutePath().toUpperCase().contains(ParametrosArquivoFilasNominaisRegulada.TEXTO_REGULADA.getDescricao()))
					{
						procurarEConsolidarArquivo(subpasta, true, nomePastaPrincipal, consultaOuExame, caminhoArquivo, dataDaColeta);
					}
					else
					{
						procurarEConsolidarArquivo(subpasta, pastaRegulada, nomePastaPrincipal, consultaOuExame, caminhoArquivo, dataDaColeta);
					}
				}
			}
			else
			{
				if(pastaRegulada)
				{
					DemandasReguladas novasSolicitacoes = new DemandasReguladas();
					
					if((new File(itemDaPasta.getPath()).exists()))
						novasSolicitacoes.agruparDadosPorEspecialidadeRegulada(pasta, pastaBaseAmbulatorialCDIDR, caminhoArquivo, dataDaColeta, relacaoUnidadeTipo, nomenclaturasPadronizadas, itemDaPasta, diretoriosCDIDR);				
				}
				else
				{
					String arquivoCDR = pasta.getPasta().getPath() + "\\" + itemDaPasta.getName();
					System.out.println(arquivoCDR);
					
					if(arquivoCDR.endsWith(ParametrosArquivoFilaCDRConsulta.EXTENSAO_ARQUIVO.getDescricao()))
					{
						String unidade = itemDaPasta.getName().substring(0, itemDaPasta.getName().indexOf(" - " + consultaOuExame)).trim();
						String tipoUnidade = relacaoUnidadeTipo.get(unidade);
						
						AcoesArquivoExcel arquivoDemandaReprimida = new AcoesArquivoExcel(caminhoArquivo, 0);

						arquivoDemandaReprimida.abrirPlanilha(ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_CDR.getDescricao(), 0);
						int linhaPlanilhaCDR = arquivoDemandaReprimida.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoDemandaReprimidaCDR.LINHA_INICIAL_PLANILHA_CDR.getIndice() - 1, 0) + 1;
						
						arquivoDemandaReprimida.abrirPlanilha(ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.NOME_PLANILHA_FILIPETAS_NAO_IMPRESSAS.getDescricao(), 0);
						int linhaPlanilhaFilipetaNaoImpressa = arquivoDemandaReprimida.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.LINHA_INICIAL_PLANILHA_CDR.getIndice() - 1, 0) + 1;
						
						ArrayList<CelulaExcel> celulasCDR = new ArrayList<CelulaExcel>();
						ArrayList<CelulaExcel> celulasFilipetasNaoImpressas = new ArrayList<CelulaExcel>();
						
						CorrelacaoArquivosDemandaReprimida correlacaoCDR = new CorrelacaoArquivosDemandaReprimida();
						ArrayList<CorrelacaoColunasArquivos> colunasCDR = correlacaoCDR.obterCorrelacaoEntreArquivos("CDR", consultaOuExame);
						
						CorrelacaoArquivosDemandaReprimida correlacaoFilipeta = new CorrelacaoArquivosDemandaReprimida();
						ArrayList<CorrelacaoColunasArquivos> colunasFilipeta = correlacaoFilipeta.obterCorrelacaoEntreArquivos("Filipeta", consultaOuExame);
						
						BufferedReader br;
						try {

							Reader reader = new InputStreamReader(new FileInputStream(arquivoCDR), StandardCharsets.ISO_8859_1);

							CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
							
							String linha;
							
							int colunaNomePaciente;
							int colunaDataEntrada;
							int colunaEspecialidadeExame;
							int colunaStatus;
							String formatoData;
							String formatoHora;
							String formatoDataHora;
							
							if(consultaOuExame.equals("CONSULTA"))
							{
								colunaNomePaciente = ParametrosArquivoFilaCDRConsulta.INDICE_COLUNA_NOME.getIndice();
								colunaDataEntrada = ParametrosArquivoFilaCDRConsulta.INDICE_COLUNA_DATA_ENTRADA.getIndice();
								colunaEspecialidadeExame = ParametrosArquivoFilaCDRConsulta.INDICE_COLUNA_ESPECIALIDADE.getIndice();
								colunaStatus = ParametrosArquivoFilaCDRConsulta.INDICE_COLUNA_STATUS.getIndice();
								formatoData = ParametrosArquivoFilaCDRConsulta.FORMATO_DATA_CSV.getDescricao();
								formatoHora = ParametrosArquivoFilaCDRConsulta.FORMATO_HORA_CSV.getDescricao();
								formatoDataHora = ParametrosArquivoFilaCDRConsulta.FORMATO_DATA_HORA_CSV.getDescricao();
							}
							else
							{
								colunaNomePaciente = ParametrosArquivoFilaCDRExame.INDICE_COLUNA_NOME.getIndice();
								colunaDataEntrada = ParametrosArquivoFilaCDRExame.INDICE_COLUNA_DATA_ENTRADA.getIndice();
								colunaEspecialidadeExame = ParametrosArquivoFilaCDRExame.INDICE_COLUNA_EXAME.getIndice();	
								colunaStatus = ParametrosArquivoFilaCDRExame.INDICE_COLUNA_STATUS.getIndice();
								formatoData = ParametrosArquivoFilaCDRExame.FORMATO_DATA_CSV.getDescricao();
								formatoHora = ParametrosArquivoFilaCDRExame.FORMATO_HORA_CSV.getDescricao();
								formatoDataHora = ParametrosArquivoFilaCDRExame.FORMATO_DATA_HORA_CSV.getDescricao();
							}
							
							Iterable<CSVRecord> registros = format.parse(reader);
							for(CSVRecord registro : registros)						
							{
							    celulasCDR.add(new CelulaExcel(linhaPlanilhaCDR, ParametrosArquivoDemandaReprimidaCDR.INDICE_COLUNA_TIPO_DE_UNIDADE.getIndice(), tipoUnidade, "String"));
							    celulasCDR.add(new CelulaExcel(linhaPlanilhaCDR, ParametrosArquivoDemandaReprimidaCDR.INDICE_COLUNA_UNIDADE_DE_SAUDE.getIndice(), unidade, "String"));
							    
							    String nomeAbreviado = Utils.somenteIniciais(registro.get(colunaNomePaciente).replaceAll("\"", ""));
							    celulasCDR.add(new CelulaExcel(linhaPlanilhaCDR, ParametrosArquivoDemandaReprimidaCDR.INDICE_COLUNA_NOME_ABREVIADO.getIndice(), nomeAbreviado, "String"));
							    
							    System.out.println(linhaPlanilhaCDR + " " + registro.get(0));
							    System.out.println(registro.get(colunaDataEntrada));
							    LocalDate dataEntrada = LocalDate.parse(registro.get(colunaDataEntrada).replaceAll("\"", ""), DateTimeFormatter.ofPattern(formatoDataHora));
							    celulasCDR.add(new CelulaExcel(linhaPlanilhaCDR, ParametrosArquivoDemandaReprimidaCDR.INDICE_COLUNA_DATA_ENTRADA.getIndice(), dataEntrada, "Date"));
							    
							    int tempoDeEsperaEmDias = (int)ChronoUnit.DAYS.between(dataEntrada, dataDaColeta);
							    celulasCDR.add(new CelulaExcel(linhaPlanilhaCDR, ParametrosArquivoDemandaReprimidaCDR.INDICE_COLUNA_TEMPO_DE_ESPERA_EM_DIAS.getIndice(), tempoDeEsperaEmDias, "Int"));
							    
							    String especialidadeExame = registro.get(colunaEspecialidadeExame).replaceAll("\"", "");
							    String nomePadronizado = "";
							    String tipoAgendamento = "";
							    
							    if(nomenclaturasPadronizadas.containsKey(especialidadeExame.toUpperCase().trim()))
							    {
							    	nomePadronizado = nomenclaturasPadronizadas.get(especialidadeExame.toUpperCase().trim()).getNomenclatura();
							    	tipoAgendamento = nomenclaturasPadronizadas.get(especialidadeExame.toUpperCase().trim()).getFluxo();
							    }
							    celulasCDR.add(new CelulaExcel(linhaPlanilhaCDR, ParametrosArquivoDemandaReprimidaCDR.INDICE_COLUNA_NOMENCLATURA_CORRETA.getIndice(), nomePadronizado, "String"));
							    celulasCDR.add(new CelulaExcel(linhaPlanilhaCDR, ParametrosArquivoDemandaReprimidaCDR.INDICE_COLUNA_TIPO_DE_AGENDAMENTO.getIndice(), tipoAgendamento, "String"));
							    
							    correlacionarCDR(colunasCDR, registro, linhaPlanilhaCDR, celulasCDR, formatoData, formatoDataHora, formatoHora);
							    
							    linhaPlanilhaCDR++;
							    
							    if(registro.get(colunaStatus).equals(ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.TEXTO_STATUS_AGENDADO.getDescricao())) 
							    {
							    	celulasFilipetasNaoImpressas.add(new CelulaExcel(linhaPlanilhaFilipetaNaoImpressa, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.INDICE_COLUNA_TIPO_DE_UNIDADE.getIndice(), tipoUnidade, "String"));
							    	celulasFilipetasNaoImpressas.add(new CelulaExcel(linhaPlanilhaFilipetaNaoImpressa, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.INDICE_COLUNA_UNIDADE_DE_SAUDE.getIndice(), unidade, "String"));
								    
							    	celulasFilipetasNaoImpressas.add(new CelulaExcel(linhaPlanilhaFilipetaNaoImpressa, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.INDICE_COLUNA_NOME_ABREVIADO.getIndice(), nomeAbreviado, "String"));
								    
							    	celulasFilipetasNaoImpressas.add(new CelulaExcel(linhaPlanilhaFilipetaNaoImpressa, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.INDICE_COLUNA_DATA_ENTRADA.getIndice(), dataEntrada, "Date"));
							    	celulasFilipetasNaoImpressas.add(new CelulaExcel(linhaPlanilhaFilipetaNaoImpressa, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.INDICE_COLUNA_TEMPO_DE_ESPERA_EM_DIAS.getIndice(), tempoDeEsperaEmDias, "Int"));
								    
							    	celulasFilipetasNaoImpressas.add(new CelulaExcel(linhaPlanilhaFilipetaNaoImpressa, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.INDICE_COLUNA_NOMENCLATURA_CORRETA.getIndice(), nomePadronizado, "String"));
							    	celulasFilipetasNaoImpressas.add(new CelulaExcel(linhaPlanilhaFilipetaNaoImpressa, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.INDICE_COLUNA_TIPO_DE_AGENDAMENTO.getIndice(), tipoAgendamento, "String"));
								    
								    correlacionarCDR(colunasFilipeta, registro, linhaPlanilhaFilipetaNaoImpressa, celulasFilipetasNaoImpressas, formatoData, formatoDataHora, formatoHora);
								    
								    linhaPlanilhaFilipetaNaoImpressa++;
							    }
							}
							
							reader.close();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						arquivoDemandaReprimida.gravarDadosEmCelula(ParametrosArquivoDemandaReprimidaCDR.NOME_PLANILHA_CDR.getDescricao(), celulasCDR, true, false, ParametrosArquivoDemandaReprimidaCDR.LINHA_INICIAL_PLANILHA_CDR.getIndice(), null);
						arquivoDemandaReprimida.forcarCalculos();

						arquivoDemandaReprimida.gravarDadosEmCelula(ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.NOME_PLANILHA_FILIPETAS_NAO_IMPRESSAS.getDescricao(), celulasFilipetasNaoImpressas, true, false, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas.LINHA_INICIAL_PLANILHA_CDR.getIndice(), null);
						arquivoDemandaReprimida.forcarCalculos();
						
					}
						
				}
			}
		
		}
		
		return "";
	}
	
	private String correlacionarCDR(ArrayList<CorrelacaoColunasArquivos> correlacao, CSVRecord registro, int linhaArquivoConsolidado, ArrayList<CelulaExcel> celulas, String formatoData, String formatoDataHora, String formatoHora)
	{
		for(CorrelacaoColunasArquivos coluna : correlacao)
		{
			if(registro.get(coluna.getColunaSIRESP()).replaceAll("\"", "").equals(""))
			{
				celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), "", "String"));
			}
			else
			{
				if(coluna.getTipo().equals("String"))
				{
					String valor = registro.get(coluna.getColunaSIRESP()).replaceAll("\"", "");
					celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
				}else if(coluna.getTipo().equals("Date"))
				{
					LocalDate valor = LocalDate.parse(registro.get(coluna.getColunaSIRESP()).replaceAll("\"", ""), DateTimeFormatter.ofPattern(formatoData));
					celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
				}else if(coluna.getTipo().equals("DateTime"))
				{
					LocalDateTime valor = LocalDateTime.parse(registro.get(coluna.getColunaSIRESP()).replaceAll("\"", ""), DateTimeFormatter.ofPattern(formatoDataHora));
					celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
				}else if(coluna.getTipo().equals("Time"))
				{
					LocalTime valor = LocalTime.parse(registro.get(coluna.getColunaSIRESP()).replaceAll("\"", ""), DateTimeFormatter.ofPattern(formatoHora));
					celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
				}else if(coluna.getTipo().equals("Int"))
				{
					Integer valor = Integer.parseInt(registro.get(coluna.getColunaSIRESP()).replaceAll("\"", ""));
					celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
				}
			}	
		}
		
		return "";
	}
	
	

}
