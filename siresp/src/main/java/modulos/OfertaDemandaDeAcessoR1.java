package modulos;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import dadosGerais.CorrelacaoArquivosOfertaDemanda;
import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDR;
import dadosGerais.IdentificadoresPastasCompartilhadasCDRA;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoAgendamentosPendentesRegulada;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoDemandaReprimida;
import dadosGerais.ParametrosArquivoFilasNominais;
import dadosGerais.ParametrosArquivoFilasNominaisRegulada;
import dadosGerais.ParametrosArquivoMapaDeOfertasOfertasParaDERAC;
import dadosGerais.ParametrosArquivoNomenclaturas;
import dadosGerais.ParametrosArquivoNovasSolicitacoesConsolidado;
import dadosGerais.ParametrosArquivoNovasSolicitacoesConsulta;
import dadosGerais.ParametrosArquivoNovasSolicitacoesExame;
import dadosGerais.ParametrosArquivoOfertaDemanda;
import dadosGerais.ParametrosArquivoOfertaPlanilhaDemanda;
import dadosGerais.ParametrosArquivoOfertas;
import dadosGerais.ParametrosArquivoOfertasParaBloqueio;
import dadosGerais.ParametrosArquivoReguladaConsolidado;
import dadosGerais.ParametrosArquivoSolicitacoesPendentesRegulada;
import dadosGerais.ParametrosTabelaAgendaHorarioConsultas;
import dadosGerais.ParametrosTabelaAgendaHorarioExame;
import dadosGerais.ParametrosTabelaManutencaoEquipamento;
import dadosGerais.ParametrosTabelaPacientesSemRecepcaoConsulta;
import dadosGerais.ParametrosTabelaPacientesSemRecepcaoExame;
import dadosGerais.ParametrosTabelaProducaoConsolidadoConsultas;
import dadosGerais.ParametrosTabelaProducaoConsolidadoExames;
import dadosGerais.ParametrosTabelaProducaoExecutanteConsultas;
import dadosGerais.ParametrosTabelaProducaoExecutanteExames;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import modelosDados.AgendamentosPendentesRegulada;
import modelosDados.CelulaExcel;
import modelosDados.CorrelacaoColunasOfertasDemandas;
import modelosDados.DeParaEspecialidadesPadronizadas;
import modelosDados.Demanda;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeExecutanteR1;
import modelosDados.EntradaOfertasParaDERAC;
import modelosDados.NomenclaturaPadronizada;
import modelosDados.NovasSolicitacoes;
import modelosDados.NovasSolicitacoesRegulada;
import modelosDados.OfertaEDemanda;
import modelosDados.RelacaoOfertasEmBloqueio;
import modelosDados.SolicitacoesPendentesRegulada;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class OfertaDemandaDeAcessoR1 {
	
	private int mesCompetencia;
	private int anoCompetencia;
	private int mesReferencia;
	private int anoReferencia;
	private String pastaBaseAmbulatorialCDIDR;
	private String pastaBase;
	private String pastaBaseDemandaReprimidaCDIDR;
	private String pastaBaseCDRA;
	private String pastaDownloads;
	private MesesFormatados meses;
	private DateTimeFormatter formatoDataPaginaWeb;
	private DateTimeFormatter formatoDataArquivo;
	LocalDate dataInicioReferencia;
	LocalDate dataFinalReferencia;
	LocalDate dataInicioCompetencia;
	LocalDate dataFinalCompetencia;
	String dataFormatadaInicioReferencia;
	String dataFormatadaFinalReferencia;
	String dataFormatadaInicioCompetencia;
	String dataFormatadaFinalCompetencia;
	
	ArrayList<String> unidadesSolicitantes;
	HashMap<String, HashMap<String, OfertaEDemanda>> ofertasDemandasProcessadas;
	HashMap<String, Demanda> demandasProcessadas;
	HashMap<String, String> relacoesOfertaEmBloqueios;
	HashMap<String, NomenclaturaPadronizada> nomenclaturasPadronizadas;
	HashMap<String, EntradaOfertasParaDERAC> mapaDeOfertasParaDERAC;
	HashMap<String, ArrayList<OfertaEDemanda>> demandasProcedimentos;
	HashMap<String, NovasSolicitacoes> novasSolicitacoesCDR;
	HashMap<String, NovasSolicitacoes> novasSolicitacoesRegulada;
	private IdentificadoresPastasCompartilhadasCDIDR diretoriosCDIDR; 
	private IdentificadoresPastasCompartilhadasCDRA diretoriosCDRA; 

	public OfertaDemandaDeAcessoR1(String pastaBase, String ambiente)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDR.valueOf(ambiente);
		pastaBaseAmbulatorialCDIDR = pastaBase;
	}
	
	public OfertaDemandaDeAcessoR1()
	{

	}
	
	public String calcularOfertaEDemanda(WebDriver driver, String competenciaInicial, String competenciaFinal, boolean testeNovasSolicitacoes, boolean executarNovasSolicitacoesCDR, boolean executarNovasSolicitacoesRegulada, boolean consolidarNovasSolicitacoesRegulada, boolean executarDemandaReprimida, boolean preencherProdutividade, boolean preencherOfertasParaDERAC, boolean preencherNomenclatura, boolean preencherBloqueio, boolean preencherRecepcao, String ambiente)
	{			
		formatoDataPaginaWeb = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		formatoDataArquivo = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDR.valueOf(ambiente.toUpperCase());
		diretoriosCDRA = IdentificadoresPastasCompartilhadasCDRA.valueOf(ambiente.toUpperCase());
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		//definindo a formatação dos meses para permitir que seja possível criar a estrutura das pastas
		meses = new MesesFormatados();
		
		String[] opcoesRotina = {"Executar rotina completa", "Executar apenas consolidação"}; 
        int escolhaRotina = JOptionPane.showOptionDialog( null, "O que deseja fazer?", "Rotinas", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoesRotina, opcoesRotina[0] );
        
		pastaBase = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta compartilhada", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
		pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE).trim();
		
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
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDR.REFERENCIA_PASTAS_AMBULATORIAL.getTextoIdentificador()))
				pastaBaseAmbulatorialCDIDR = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDR.REFERENCIA_PASTAS_AMBULATORIAL.getTextoIdentificador());
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
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDRA.REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDRA.getTextoIdentificador()))
				pastaBaseCDRA = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDRA.REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDRA.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Demanda Reprimida compartilhada");
				return "";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, "Erro ao encontrar o arquivos de parâmetros da pasta");
			return "";
		}
		
		gerarCopiaTemporariaRelatorioProducao();

		unidadesSolicitantes = lerEntidadesSolicitantes(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoUnidadesSolicitantes());
		
		novasSolicitacoesCDR = new HashMap<String, NovasSolicitacoes>();
		ArrayList<NovasSolicitacoes> novasSolicitacoesExistentes = lerEntradaDeNovaSolicitacoes(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoBDConsolidadoNovasSolicitacoesCDR(), ParametrosArquivoNovasSolicitacoesConsolidado.NOME_PLANILHA_BD_CDR.getDescricao(), ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice() - 1);
		int linhaExcel = ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice();
		
		for(NovasSolicitacoes entrada : novasSolicitacoesExistentes)
		{
			if(!entrada.getTipoSolicitacao().trim().equals(""))
			{
				String tipoSolicitacao = entrada.getTipoSolicitacao().trim();
				String especialidade = entrada.getEspecialidadeExame().toUpperCase().trim();
				String cid = entrada.getCID().trim();
				String unidade = entrada.getUnidadesCampinas().trim();
				String mes = entrada.getMesInclusao().trim();
				String ano = entrada.getAnoInclusao().trim();
				
				entrada.setQtdeSolicitacoes(0);
				entrada.setLinhaExcel(linhaExcel);
				
				String valorConcatenadoConsolidado = tipoSolicitacao + especialidade + cid + unidade + mes + ano;
				
				novasSolicitacoesCDR.put(valorConcatenadoConsolidado, entrada);
			}

			linhaExcel++;
		}
		
		novasSolicitacoesRegulada = new HashMap<String, NovasSolicitacoes>();
		novasSolicitacoesExistentes = lerEntradaDeNovaSolicitacoes(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoBDConsolidadoNovasSolicitacoesRegulada(), ParametrosArquivoNovasSolicitacoesConsolidado.NOME_PLANILHA_BD_REGULADA.getDescricao(), ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice() - 1);
		linhaExcel = ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice();
		
		for(NovasSolicitacoes entrada : novasSolicitacoesExistentes)
		{
			if(!entrada.getTipoSolicitacao().trim().equals(""))
			{
				String tipoSolicitacao = entrada.getTipoSolicitacao().trim();
				String especialidade = entrada.getEspecialidadeExame().toUpperCase().trim();
				String cid = entrada.getCID().trim();
				String unidade = entrada.getUnidadesCampinas().trim();
				String mes = entrada.getMesInclusao().trim();
				String ano = entrada.getAnoInclusao().trim();
				
				entrada.setQtdeSolicitacoes(0);
				entrada.setLinhaExcel(linhaExcel);
				
				String valorConcatenadoConsolidado = tipoSolicitacao + especialidade + cid + unidade + mes + ano;
				
				novasSolicitacoesRegulada.put(valorConcatenadoConsolidado, entrada);
			}

			linhaExcel++;

		}
		
		
		ArrayList<OfertaEDemanda> ofertasEDemandasJaRegistradas = lerOfertasJaProcessadas(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
		ofertasDemandasProcessadas = new HashMap<String, HashMap<String, OfertaEDemanda>>();
		
		int linhaArquivo = ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(OfertaEDemanda oferta : ofertasEDemandasJaRegistradas)
		{
			oferta.setCompetencia(normalizarDataParaMesAno(oferta.getCompetencia()));
			
			System.out.println(oferta.getUnidade() + oferta.getCompetencia());
			
			if(ofertasDemandasProcessadas.containsKey(oferta.getUnidade() + oferta.getCompetencia()))
			{
				HashMap<String, OfertaEDemanda> mapaEspecialidade = ofertasDemandasProcessadas.get(oferta.getUnidade() + oferta.getCompetencia());
				
				System.out.println(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase().trim());
				
				if(mapaEspecialidade.containsKey(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase().trim()))
				{
					OfertaEDemanda ofertaEncontrada = mapaEspecialidade.get(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase().trim());
					ofertaEncontrada.setLinhaExcel(linhaArquivo);
				}
				else
				{
					oferta.setLinhaExcel(linhaArquivo);
					mapaEspecialidade.put(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase().trim(), oferta);
				}
			}
			else
			{
				HashMap<String, OfertaEDemanda> mapaEspecialidade = new HashMap<String, OfertaEDemanda>();
				oferta.setLinhaExcel(linhaArquivo);
				mapaEspecialidade.put(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase().trim(), oferta);
				
				ofertasDemandasProcessadas.put(oferta.getUnidade() + oferta.getCompetencia(), mapaEspecialidade);
			}
			
			linhaArquivo++;
		}
		
		ArrayList<Demanda> demandasJaRegistradas = lerDemandasJaProcessadas(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), ParametrosArquivoOfertaPlanilhaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaPlanilhaDemanda.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
		demandasProcessadas = new HashMap<String, Demanda>();
		
		int linhaArquivoDemanda = ParametrosArquivoOfertaPlanilhaDemanda.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(Demanda demanda : demandasJaRegistradas)
		{
			demanda.setCompetencia(normalizarDataParaMesAno(demanda.getCompetencia()));
			demanda.setLinhaExcel(linhaArquivoDemanda);
			
			demandasProcessadas.put(demanda.getProcedimento() + demanda.getCompetencia(), demanda);
			
			linhaArquivoDemanda++;
		}
		
		ArrayList<RelacaoOfertasEmBloqueio> relacaoGrupoEspecificidade = lerRelacaoOfertasParaBloqueio(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoRelacoesEspecialidadesBloqueio(), ParametrosArquivoOfertasParaBloqueio.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertasParaBloqueio.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
		
		relacoesOfertaEmBloqueios = new HashMap<String, String>();
		for(RelacaoOfertasEmBloqueio relacao : relacaoGrupoEspecificidade)
		{
			relacoesOfertaEmBloqueios.put(relacao.getUnidade() + relacao.getTipoDeOferta() + relacao.getEquipamento().trim(), relacao.getGrupo().trim());
		}
		
		ArrayList<NomenclaturaPadronizada> nomenclaturas = lerPadronizacaoDeNomenclaturas(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getCaminhoArquivoNomenclaturas(), ParametrosArquivoNomenclaturas.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoNomenclaturas.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
		
		nomenclaturasPadronizadas = new HashMap<String, NomenclaturaPadronizada>();
		for(NomenclaturaPadronizada nomenclatura : nomenclaturas)
		{
			nomenclatura.setInsercao(nomenclatura.getInsercao().replaceAll("\u00A0", ""));
			nomenclatura.setNomenclatura(nomenclatura.getNomenclatura().replaceAll("\u00A0", ""));
			nomenclatura.setFluxo(nomenclatura.getFluxo().replaceAll("\u00A0", ""));
			nomenclaturasPadronizadas.put(nomenclatura.getInsercao().trim().toUpperCase(), nomenclatura);
		}
		
		String mesInicio;
		String anoInicio;
		String mesFim;
		String anoFim;
		
		if(competenciaInicial == null)
		{
			mesInicio = JOptionPane.showInputDialog(null, "Qual o mês de análise?", "Mês de Referência", JOptionPane.QUESTION_MESSAGE).trim();
			anoInicio = JOptionPane.showInputDialog(null, "Qual o ano de análise?", "Ano de Referência", JOptionPane.QUESTION_MESSAGE).trim();
			
			mesFim = mesInicio;
			anoFim = anoInicio;
		}
		else
		{
			mesInicio = competenciaInicial.split("/")[0];
			anoInicio = competenciaInicial.split("/")[1];
			
			mesFim = competenciaFinal.split("/")[0];
			anoFim = competenciaFinal.split("/")[1];
		}
		
		mesCompetencia = Integer.parseInt(mesInicio);
		anoCompetencia = Integer.parseInt(anoInicio);
		
		int ultimoMesProcessamento = Integer.parseInt(mesFim);
		int ultimoAnoProcessamento = Integer.parseInt(anoFim);
		
		while((anoCompetencia < ultimoAnoProcessamento) || (anoCompetencia == ultimoAnoProcessamento && mesCompetencia <= ultimoMesProcessamento))
		{
			demandasProcedimentos = new HashMap<String, ArrayList<OfertaEDemanda>>();			
			
			if(mesCompetencia == 1)
			{
				mesReferencia = 12;
				anoReferencia = anoCompetencia - 1;
			}
			else
			{
				mesReferencia = mesCompetencia - 1;
				anoReferencia = anoCompetencia;
			}
			
			if(mesReferencia < 10)
				dataFormatadaInicioReferencia = "01-0" + mesReferencia + "-" + anoReferencia;
			else
				dataFormatadaInicioReferencia = "01-" + mesReferencia + "-" + anoReferencia;
			
			dataInicioReferencia = LocalDate.parse(dataFormatadaInicioReferencia, formatoDataPaginaWeb);
			
			dataFinalReferencia = dataInicioReferencia.with(TemporalAdjusters.lastDayOfMonth());
			dataFormatadaFinalReferencia = dataFinalReferencia.format(formatoDataPaginaWeb);
			
			//System.out.println(dataFormatadaPasta);
			
			if(mesCompetencia < 10)
				dataFormatadaInicioCompetencia = "01/0" + mesCompetencia + "/" + anoCompetencia;
			else
				dataFormatadaInicioCompetencia = "01/" + mesCompetencia + "/" + anoCompetencia;
			
			dataInicioCompetencia = LocalDate.parse(dataFormatadaInicioCompetencia, formatoDataArquivo);
			
			dataFinalCompetencia = dataInicioCompetencia.with(TemporalAdjusters.lastDayOfMonth());
			dataFormatadaFinalCompetencia = dataFinalCompetencia.format(formatoDataArquivo);
			
			
			String caminhoArquivoOfertasParaDERAC = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosOfertasParaDERAC() + "\\" + anoCompetencia + "\\" + ParametrosArquivoMapaDeOfertasOfertasParaDERAC.NOME_PADRAO_ARQUIVO.getDescricao();
			String complementoNomePlanilha = meses.getMeses().get(mesCompetencia - 1).getMesNumero() + "." + anoCompetencia;
			String nomePlanilha = ParametrosArquivoMapaDeOfertasOfertasParaDERAC.NOME_PLANILHA_CONSOLIDADA.getDescricao().replace(ParametrosArquivoMapaDeOfertasOfertasParaDERAC.VALOR_DINAMICO.getDescricao(), complementoNomePlanilha);
			
			ArrayList<EntradaOfertasParaDERAC> mapaDeOfertas = lerMapaDeOfertasParaDERAC(caminhoArquivoOfertasParaDERAC, nomePlanilha, ParametrosArquivoNomenclaturas.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
			
			mapaDeOfertasParaDERAC = new HashMap<String, EntradaOfertasParaDERAC>();
			
			if(mapaDeOfertas != null)
			{
				for(EntradaOfertasParaDERAC oferta : mapaDeOfertas)
				{
					oferta.setEspecialidades(oferta.getEspecialidades().replaceAll("\u00A0", ""));
					oferta.setProcedimentos(oferta.getProcedimentos().replaceAll("\u00A0", ""));
					oferta.setProcedimentosExecutante(oferta.getProcedimentosExecutante().replaceAll("\u00A0", ""));
					oferta.setAgenda(oferta.getAgenda().replaceAll("\u00A0", ""));
					oferta.setTipo(oferta.getTipo().replaceAll("\u00A0", ""));
					oferta.setExecutante(oferta.getExecutante().replaceAll("\u00A0", ""));
					oferta.setOfertasParaDERAC(oferta.getOfertasParaDERAC().replaceAll("\u00A0", ""));
					oferta.setPlanoDeTrabalho(oferta.getPlanoDeTrabalho().replaceAll("\u00A0", ""));
					oferta.setMesDeReferencia(oferta.getMesDeReferencia().replaceAll("\u00A0", ""));
					
					if(mapaDeOfertasParaDERAC.containsKey(oferta.getExecutante().trim().toUpperCase() + oferta.getProcedimentos().trim().toUpperCase()))
					{
						EntradaOfertasParaDERAC ofertaExistente = mapaDeOfertasParaDERAC.get(oferta.getExecutante().trim().toUpperCase() + oferta.getProcedimentos().trim().toUpperCase());
						
						boolean ehNumeroExistente = false;
						boolean ehNumeroNovo = false;
						int soma = 0;
						try
						{
							soma +=  Integer.parseInt(ofertaExistente.getOfertasParaDERAC());
							ehNumeroExistente = true;
						}catch(NumberFormatException e)
						{
							e.printStackTrace();
						}
						
						System.out.println(oferta.getExecutante().trim().toUpperCase() + oferta.getProcedimentos().trim().toUpperCase() + " - " + soma);
						
						try
						{
							soma +=  Integer.parseInt(oferta.getOfertasParaDERAC());
							ehNumeroNovo = true;
						}catch(NumberFormatException e)
						{
							e.printStackTrace();
						}
						
						if(ehNumeroExistente || ehNumeroNovo)
						{
							ofertaExistente.setOfertasParaDERAC(String.valueOf(soma));
						}
						
						System.out.println(oferta.getExecutante().trim().toUpperCase() + oferta.getProcedimentos().trim().toUpperCase() + " - " + soma);
						
						mapaDeOfertasParaDERAC.put(oferta.getExecutante().trim().toUpperCase() + oferta.getProcedimentos().trim().toUpperCase(), ofertaExistente);
					}
					else
						mapaDeOfertasParaDERAC.put(oferta.getExecutante().trim().toUpperCase() + oferta.getProcedimentos().trim().toUpperCase(), oferta);
				}
			}
			
			//definindo entidades para o censo de leitos
			ArrayList<EntidadeExecutanteR1> entidades = lerEntidadesR1(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoUnidadesExecutantes());
			
			if(!testeNovasSolicitacoes)
			{
				if(escolhaRotina == 0)
				{
				
					driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
					
					paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
					paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
					ArrayList<ElementoSelecao> listaUnidadeRadio = paginaWeb.getListaDeOpcoesRadioPorName(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_RADIO_UNIDADES.getTextoIdentificador());
					
					HashMap<String, String> elementosRadioUnidades = new HashMap<String, String>();
					
					for(ElementoSelecao elemento : listaUnidadeRadio)
					{
						String cnes = elemento.getText().substring(0, 7);
						String value = elemento.getValue();
						
						int posicaoPerfilDeAcesso = elemento.getText().indexOf(" - Administrador Unidade Reg");
						
						if(posicaoPerfilDeAcesso > 0)
						{
							String composicaoCNESNomeUnidade = elemento.getText().substring(0, posicaoPerfilDeAcesso);
							elementosRadioUnidades.put(composicaoCNESNomeUnidade, value);
							
							//System.out.println("CNES: " + cnes + "| Value: " + value + "| Composição: " + composicaoCNESNomeUnidade + "|");
						}
			
					}
					
					for(EntidadeExecutanteR1 entidade : entidades)
					{
						driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
						
						ArrayList<String> opcoes = new ArrayList<>();
						opcoes.add("Relatório");
						opcoes.add("Produtividade  >>");
						
						if(entidade.getVinculo().equals(IdentificadoresPaginaWebSIRESP.TEXTO_VINCULO_ESTADUAL.getTextoIdentificador()))
							opcoes.add("P01 - Produção Executante");
						else
							opcoes.add("P06 - Consolidado");
						
						
						String value = elementosRadioUnidades.get(entidade.getCNES() + " - " + entidade.getNomeUnidadeSIRESP());
						//System.out.println(value);
						
						
						if(value != null)
						{
							paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
						
							paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
							
							boolean unidadeEncontrada = false;
							
							while(!unidadeEncontrada)
								unidadeEncontrada = paginaWeb.clicarRadioInputByValue(driver, value);
							
							paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_BOTAO_OK_ESCOLHER_UNIDADE.getTextoIdentificador(), "id");
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							boolean visivel;
							do
							{
							
								visivel = acessarMenu(driver, paginaWeb, opcoes);
								
							
							}while(!visivel);
							
							//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							montarOfertaDemanda(driver, paginaWeb, entidade, preencherProdutividade, preencherOfertasParaDERAC, preencherNomenclatura, preencherBloqueio, preencherRecepcao);
							
						}
						else
							System.out.println("Unidade não encontrada: " + entidade.getCNES() + " - " + entidade.getExecutante());
					}
					
					HashMap<String, Integer> entradasPorOferta = new HashMap<String, Integer>();
					
					if(dataInicioCompetencia.isBefore(LocalDate.now()))
					{
					
						if(executarNovasSolicitacoesCDR)
							preencherNovasSolicitacoesCDR(driver, paginaWeb, elementosRadioUnidades, entidades, entradasPorOferta);
						
						if(executarNovasSolicitacoesRegulada)
						{
							preencherNovasSolicitacoesRegulada(entidades, consolidarNovasSolicitacoesRegulada, entradasPorOferta);
						}
						
						if(executarNovasSolicitacoesCDR || executarNovasSolicitacoesRegulada)
							preencherConsolidacaoNovasSolicitacoes(entidades, entradasPorOferta);
						
						if(executarDemandaReprimida)
						{
							HashMap <String, Integer> demandaPorEspecialidade = new HashMap<String, Integer>();
							HashMap <String, Integer> ofertaPorEspecialidade = new HashMap<String, Integer>();
							HashMap <String, Integer> maximoTempoPorEspecialidade = new HashMap<String, Integer>();
							HashMap <String, Boolean> existeOfertaPorEspecialidade = new HashMap<String, Boolean>();
							
							preencherDemandaReprimida(entidades, ofertaPorEspecialidade, demandaPorEspecialidade, maximoTempoPorEspecialidade, existeOfertaPorEspecialidade);
							
							montarPlanilhaDeDemandas(entradasPorOferta, ofertaPorEspecialidade, demandaPorEspecialidade, maximoTempoPorEspecialidade, existeOfertaPorEspecialidade);
						}
					}
				}

			}
			else
				testeArquivoNovasSolicitacoes(executarNovasSolicitacoesCDR, executarNovasSolicitacoesRegulada, consolidarNovasSolicitacoesRegulada, executarDemandaReprimida);
			
			preencherDataDeProcessamento();
			
			mesCompetencia++;
			if(mesCompetencia > 12)
			{
				mesCompetencia = 1;
				anoCompetencia++;
			}
				
		}
		
		atualizarCopiaOriginalRelatorioProducao();
		copiarRelatorioProducaoParaCDIDR();
		copiarRelatorioProducaoParaCDRA();
		
		return "";	
	}
	
	private String preencherDataDeProcessamento()
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		LocalDate dataHoje = LocalDate.now();
		
		celulas.add(new CelulaExcel(ParametrosArquivoOfertaDemanda.INDICE_LINHA_DATA_PROCESSAMENTO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DATA_PROCESSAMENTO.getIndice(), dataHoje, "Date"));
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, false, false, 0, null);
		arquivoConsolidado.forcarCalculos();
		
		return "";
	}
	
	private ArrayList<EntidadeExecutanteR1> lerEntidadesR1(String nomeArquivo)
	{
		ArrayList<EntidadeExecutanteR1> entidades = new ArrayList<EntidadeExecutanteR1>();
		
        try (Reader reader = new InputStreamReader(new FileInputStream(nomeArquivo),StandardCharsets.ISO_8859_1);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
            	String cnes = registro.get("CNES");
            	String vinculo = registro.get("VINCULO");
                String unidade = registro.get("EXECUTANTE");
                String nomeSIRESP = registro.get("NOME SIRESP");
                String nomeOfertasParaDERAC = registro.get("NOME FPO");
               
                entidades.add(new EntidadeExecutanteR1(cnes, vinculo, unidade, nomeSIRESP, nomeOfertasParaDERAC));
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	private String preencherNovasSolicitacoes(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, HashMap<String, String> elementosRadioUnidades, ArrayList<EntidadeExecutanteR1> entidades, boolean executarNovasSolicitacoesCDR, boolean executarNovasSolicitacoesRegulada, boolean consolidarNovasSolicitacoesRegulada, boolean executarDemandaReprimida)
	{
		HashMap<String, Integer> entradasPorOferta = new HashMap<String, Integer>();

		if(executarNovasSolicitacoesCDR)
			preencherNovasSolicitacoesCDR(driver, paginaWeb, elementosRadioUnidades, entidades, entradasPorOferta);
		
		if(executarNovasSolicitacoesRegulada)
		{
			preencherNovasSolicitacoesRegulada(entidades, consolidarNovasSolicitacoesRegulada, entradasPorOferta);
			preencherConsolidacaoNovasSolicitacoes(entidades, entradasPorOferta);
		}
		
		if(executarDemandaReprimida)
		{
			HashMap <String, Integer> demandaPorEspecialidade = new HashMap<String, Integer>();
			HashMap <String, Integer> ofertaPorEspecialidade = new HashMap<String, Integer>();
			HashMap <String, Integer> maximoTempoPorEspecialidade = new HashMap<String, Integer>();
			HashMap <String, Boolean> existeOfertaPorEspecialidade = new HashMap<String, Boolean>();
			
			preencherDemandaReprimida(entidades, ofertaPorEspecialidade, demandaPorEspecialidade, maximoTempoPorEspecialidade, existeOfertaPorEspecialidade);
		}
		
		return "";
	}
	
	private String preencherDemandaReprimida(ArrayList<EntidadeExecutanteR1> entidades, HashMap <String, Integer> ofertaPorEspecialidade, HashMap <String, Integer> demandaPorEspecialidade, HashMap <String, Integer> maximoTempoPorEspecialidade, HashMap <String, Boolean> existeOfertaPorEspecialidade)
	{
		String caminhoDeParaEspecialidades = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoDeParaEspecialidades();
		
		ArrayList<DeParaEspecialidadesPadronizadas> listaDePara = new ArrayList<DeParaEspecialidadesPadronizadas>();
		try (FileInputStream in = new FileInputStream(caminhoDeParaEspecialidades)) { 
			listaDePara = ExcelBinder.readSheet(in, DeParaEspecialidadesPadronizadas.class, 0, 0, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		HashMap <String, String> deParaEspecialidades = new HashMap<String, String>();
		for(DeParaEspecialidadesPadronizadas dePara : listaDePara)
			deParaEspecialidades.put(dePara.getDe().toUpperCase().trim(), dePara.getPara().toUpperCase().trim());
		
		String pastaComFilasNominais = diretoriosCDIDR.getArquivosDemandaReprimida() + "\\" + anoCompetencia + "\\" + meses.getMeses().get(mesCompetencia - 1).getMesNumero() + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + " " + anoCompetencia;
			
		Pasta pasta = new Pasta(pastaBaseDemandaReprimidaCDIDR + "\\" + pastaComFilasNominais, false);
		
		File[] conteudoDaPasta = pasta.listarDiretorio();
			
		//Encontrando arquivo mais recente pelo nome
		String arquivoMaisRecente = "";
		LocalDate dataMaisRecente = null;
		for(File itemDaPasta : conteudoDaPasta)
		{
			
			if(!itemDaPasta.isDirectory())
			{
				if(itemDaPasta.getAbsolutePath().endsWith(".xlsx")) 
				{
					System.out.println(itemDaPasta.getAbsolutePath());
					LocalDate dataExtraida = extrairData(itemDaPasta.getAbsolutePath(), "\\b\\d{2}\\.\\d{2}\\.\\d{4}\\b", "dd.MM.yyyy");
					
					if(dataExtraida != null)
					{
						System.out.println(itemDaPasta.getAbsolutePath());
						if(dataMaisRecente == null)
						{
							dataMaisRecente = dataExtraida;
							arquivoMaisRecente = itemDaPasta.getAbsolutePath();
						}
						else if(dataExtraida.isAfter(dataMaisRecente))
						{
							dataMaisRecente = dataExtraida;
							arquivoMaisRecente = itemDaPasta.getAbsolutePath();
						}
					}
				}
			}
		}
		
		if(!arquivoMaisRecente.equals(""))
		{
			AcoesArquivoExcel arquivoDeParaEspecialidades = new AcoesArquivoExcel(caminhoDeParaEspecialidades, 0);
			int proximaLinhaVaziaDeParaEspecialidades = arquivoDeParaEspecialidades.getUltimaLinhaPreenchida() + 1;
			
			AcoesArquivoExcel arquivoDemandaReprimida = new AcoesArquivoExcel(arquivoMaisRecente, 0);
			
			String[] planilhasDemandaReprimida = new String[2];
			planilhasDemandaReprimida[0] = ParametrosArquivoDemandaReprimida.NOME_PLANILHA_DINAMICA_CDR.getDescricao();
			planilhasDemandaReprimida[1] = ParametrosArquivoDemandaReprimida.NOME_PLANILHA_DINAMICA_REGULADA.getDescricao();
			
			for(String planilha : planilhasDemandaReprimida)
			{
				arquivoDemandaReprimida.abrirPlanilha(planilha, 0);
				
				int linha = arquivoDemandaReprimida.getPrimeiraLinhaPreenchidaComValorEmUmaColuna(ParametrosArquivoDemandaReprimida.TEXTO_ROTULOS_DE_LINHA.getDescricao(), ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice(), 50);
				
				if(linha >= 0)
				{
					System.out.println("Linha: " + linha);
					
					linha++;
					String especialidade = arquivoDemandaReprimida.getValorDaCelulaString(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice()).toUpperCase().trim();
					
					while(!especialidade.equals(ParametrosArquivoDemandaReprimida.TEXTO_TOTAL_GERAL.getDescricao().trim().toUpperCase()))
					{
						String especialidadePadronizada;
						
						if(deParaEspecialidades.containsKey(especialidade))
						{
							especialidadePadronizada = deParaEspecialidades.get(especialidade);
						}
						else
						{
							especialidadePadronizada = especialidade;
							
							deParaEspecialidades.put(especialidade, especialidade);
							
							ArrayList<CelulaExcel> celulasDePara = new ArrayList<CelulaExcel>();
							celulasDePara.add(new CelulaExcel(proximaLinhaVaziaDeParaEspecialidades, 0, especialidade, "String"));
							celulasDePara.add(new CelulaExcel(proximaLinhaVaziaDeParaEspecialidades, 1, especialidade, "String"));
							celulasDePara.add(new CelulaExcel(proximaLinhaVaziaDeParaEspecialidades, 2, "Nova Entrada", "String"));
							
							arquivoDeParaEspecialidades.gravarDadosEmCelula(0, celulasDePara);
							proximaLinhaVaziaDeParaEspecialidades++;
						}
						
						int soma = 0;
						if(demandaPorEspecialidade.containsKey(especialidadePadronizada))
						{
							soma = demandaPorEspecialidade.get(especialidadePadronizada);
						}
						else
						{
							demandaPorEspecialidade.put(especialidadePadronizada, 0);
						}
						soma += arquivoDemandaReprimida.getValorDaCelulaInt(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_CONTAGEM_ESPECIALIDADE.getIndice());
						
						demandaPorEspecialidade.put(especialidadePadronizada, soma);
						
						int maximoTempo = 0;
						if(maximoTempoPorEspecialidade.containsKey(especialidadePadronizada))
						{
							maximoTempo = maximoTempoPorEspecialidade.get(especialidadePadronizada);
						}

						maximoTempo = (int)Math.max(maximoTempo, arquivoDemandaReprimida.getValorDaCelulaInt(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_MAX_TEMPO_ESPERA.getIndice()));
						
						maximoTempoPorEspecialidade.put(especialidadePadronizada, maximoTempo);
						existeOfertaPorEspecialidade.put(especialidadePadronizada, false);
						
						linha++;
						especialidade = arquivoDemandaReprimida.getValorDaCelulaString(linha, ParametrosArquivoDemandaReprimida.INDICE_COLUNA_ESPECIALIDADE.getIndice()).toUpperCase().trim();
					}
				}
			}
			
			consolidarDemandaReprimidaEmOfertasEDemandas(entidades, ofertaPorEspecialidade, demandaPorEspecialidade, maximoTempoPorEspecialidade, existeOfertaPorEspecialidade);
		}
		
		
		return "";
	}
	
	private String consolidarDemandaReprimidaEmOfertasEDemandas(ArrayList<EntidadeExecutanteR1> entidades, HashMap<String, Integer> ofertaPorEspecialidade, HashMap<String, Integer> demandaPorEspecialidade, HashMap<String, Integer> maximoTempoPorEspecialidade, HashMap<String, Boolean> existeOfertaPorEspecialidade)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice());
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
				
		for(EntidadeExecutanteR1 entidade : entidades)
		{
			HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
			
			if(mapaEspecialidades != null)
			{
				for(OfertaEDemanda oferta : mapaEspecialidades.values())
				{
					int soma = 0;
					try
					{
						soma = Integer.parseInt(oferta.getOfertaDisponivel());
					}catch(Exception e)
					{
						e.printStackTrace();
					}

					if(ofertaPorEspecialidade.containsKey(oferta.getProcedimento()))
					{
						soma += ofertaPorEspecialidade.get(oferta.getProcedimento());
					}
					
					ofertaPorEspecialidade.put(oferta.getProcedimento(), soma);
					existeOfertaPorEspecialidade.put(oferta.getProcedimento(), true);
				}
			}
		}
		
		for(EntidadeExecutanteR1 entidade : entidades)
		{
			HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
			
			if(mapaEspecialidades != null)
			{
				for(OfertaEDemanda oferta : mapaEspecialidades.values())
				{
					int ofertaTotal = 0;
					if(ofertaPorEspecialidade.containsKey(oferta.getProcedimento()))
						ofertaTotal = ofertaPorEspecialidade.get(oferta.getProcedimento());

					int demandaTotal = 0;
					if(demandaPorEspecialidade.containsKey(oferta.getProcedimento()))
						demandaTotal = demandaPorEspecialidade.get(oferta.getProcedimento());
					
					if(demandaTotal == 0)
					{
						celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA_DO_DIA.getIndice(), "-", "String"));
						celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getIndice(), "-", "String"));
					}
					else
					{
						celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA_DO_DIA.getIndice(), demandaTotal, "Int"));
						
						if(ofertaTotal == 0)
							celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getIndice(), "-", "String"));
						else
						{
							int tempoDeEspera = (int)Math.ceil(1.0 * demandaTotal / ofertaTotal);
							celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getIndice(), tempoDeEspera, "Int"));
						}
					}
					
					int maximoTempo = 0;
					if(maximoTempoPorEspecialidade.containsKey(oferta.getProcedimento()))
						maximoTempo = maximoTempoPorEspecialidade.get(oferta.getProcedimento());
					
					if(maximoTempo == 0)
					{
						celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_MAIOR_TEMPO_DE_ESPERA_EM_DIAS.getIndice(), "-", "String"));
					}
					else
					{
						celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_MAIOR_TEMPO_DE_ESPERA_EM_DIAS.getIndice(), maximoTempo, "Int"));
					}
				}
			}
		}
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String preencherNovasSolicitacoesRegulada(ArrayList<EntidadeExecutanteR1> entidades, boolean consolidar, HashMap<String, Integer> entradasPorOferta)
	{
		if(consolidar)
		{
			String pastaComFilasNominais = diretoriosCDIDR.getPastaFilasNominais() + "\\" + anoCompetencia + "\\" + meses.getMeses().get(mesCompetencia - 1).getMesNumero() + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + " " + anoCompetencia;
			
			Pasta pasta = new Pasta(pastaBaseAmbulatorialCDIDR + "\\" + pastaComFilasNominais, false);
			
			agruparDadosPorEspecialidadeRegulada(entidades, pasta, false);
		}
		
		String pastaComConsolidacao = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivosReguladasNovasSolicitacoes() + "\\" + anoCompetencia;		
		String nomeArquivo = meses.getMeses().get(mesCompetencia - 1).getMesNumero() + "." + String.valueOf(anoCompetencia).substring(2) + " - " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + "_Regulada_Consultas e Exames." + ParametrosArquivoReguladaConsolidado.EXTENSAO_ARQUIVO_FORMATADO.getDescricao();
		Arquivo arquivoFinal = new Arquivo(pastaComConsolidacao, nomeArquivo);
		
		System.out.println("Verificando a existência do arquivo " + pastaComConsolidacao + "\\" + nomeArquivo);
		
		ArrayList<String> fichasProcessadas = new ArrayList<String>();

		ArrayList<NovasSolicitacoesRegulada> listaDeSolicitacoes = null;
		if(!arquivoFinal.existe())
		{
			System.out.println("Não foi possível encontrar o arquivo: " + pastaComConsolidacao + "\\" + nomeArquivo); 
		}
		else
		{
			try (FileInputStream in = new FileInputStream(pastaComConsolidacao + "\\" + nomeArquivo)) { 
				listaDeSolicitacoes = ExcelBinder.readSheet(in, NovasSolicitacoesRegulada.class, 0, 0, true);
	        }
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		atribuirNovasSolicitacoesRegulada(listaDeSolicitacoes, entidades, entradasPorOferta);
		preencherConsolidacaoDeNovasSolicitacoes(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoBDConsolidadoNovasSolicitacoesRegulada(), listaDeSolicitacoes);
		
		return "";
	}
	
	private String agruparDadosPorEspecialidadeRegulada(ArrayList<EntidadeExecutanteR1> entidades, Pasta pasta, boolean pastaRegulada)
	{
		System.out.println(pasta.getCaminhoDaPasta());
		File[] conteudoDaPasta = pasta.listarDiretorio();
		
		for(File itemDaPasta : conteudoDaPasta)
		{
			System.out.println(pasta.getPasta().getPath() + "\\" + itemDaPasta.getName());
			
			if(pasta.ehPasta(pasta.getPasta().getPath() + "\\" + itemDaPasta.getName()))
			{
				Pasta subpasta = new Pasta(pasta.getPasta().getPath() + "\\" + itemDaPasta.getName(), false);
				
				if(itemDaPasta.isDirectory())
				{
					if(itemDaPasta.getAbsolutePath().toUpperCase().contains(ParametrosArquivoFilasNominaisRegulada.TEXTO_REGULADA.getDescricao()))
					{
						agruparDadosPorEspecialidadeRegulada(entidades, subpasta, true);
					}
					else
					{
						agruparDadosPorEspecialidadeRegulada(entidades, subpasta, pastaRegulada);
					}
				}
			}
			else
			{
				if(pastaRegulada)
				{
					if((new File(itemDaPasta.getPath()).exists()))
					{
						String caminhoArquivoXLSX = "";
						boolean arquivoConvertido = false;
						
						File arquivoConvertidoXLSX = null;
						
						String nomeArquivo = itemDaPasta.getPath();
						if(nomeArquivo.endsWith(ParametrosArquivoFilasNominaisRegulada.EXTENSAO_ARQUIVO_XLSX.getDescricao()))
						{
							caminhoArquivoXLSX = nomeArquivo;
						}
						else if(nomeArquivo.endsWith(ParametrosArquivoFilasNominaisRegulada.EXTENSAO_ARQUIVO_XLS.getDescricao()))
						{
							caminhoArquivoXLSX = itemDaPasta + "x";
							
							ConversaoHMTL_XLSX conversor = new ConversaoHMTL_XLSX();
							
							try
							{
								conversor.converterArquivoHTML(nomeArquivo, caminhoArquivoXLSX, true);
								
								arquivoConvertidoXLSX = new File(caminhoArquivoXLSX);
								arquivoConvertido = true;
							}catch(Exception e)
							{
								e.printStackTrace();
							}
						}
		
						String pastaDestinoArquivosNovasSolicitacoes = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivosNovasSolicitacoesConsolidada() + "\\";
						if(caminhoArquivoXLSX.contains(ParametrosArquivoFilasNominais.PREFIXO_NOME_ARQUIVO_REGULADA_AGENDAMENTO.getDescricao()))
						{
							
							if(caminhoArquivoXLSX.contains("CONSULTA"))
							{
								extrairConsolidarDadosDeAgendamentosRegulada(caminhoArquivoXLSX, ParametrosArquivoAgendamentosPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_CONSULTA.getDescricao(), "Agendamentos", pastaDestinoArquivosNovasSolicitacoes, "Consulta");
							}
							if(caminhoArquivoXLSX.contains("EXAME"))
							{
								extrairConsolidarDadosDeAgendamentosRegulada(caminhoArquivoXLSX, ParametrosArquivoAgendamentosPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_EXAME.getDescricao(), "Agendamentos", pastaDestinoArquivosNovasSolicitacoes, "Exame");
							}
						}
						else if(caminhoArquivoXLSX.contains(ParametrosArquivoFilasNominais.PREFIXO_NOME_ARQUIVO_REGULADA_SOLICITACOES.getDescricao()))
						{
							if(caminhoArquivoXLSX.contains("CONSULTA"))
							{
								extrairConsolidarDadosDeSolicitacoesRegulada(caminhoArquivoXLSX, ParametrosArquivoSolicitacoesPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_CONSULTA.getDescricao(), "Solicitações", pastaDestinoArquivosNovasSolicitacoes, "Consulta");
							}
							if(caminhoArquivoXLSX.contains("EXAME"))
							{
								extrairConsolidarDadosDeSolicitacoesRegulada(caminhoArquivoXLSX, ParametrosArquivoSolicitacoesPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_EXAME.getDescricao(), "Solicitações", pastaDestinoArquivosNovasSolicitacoes, "Exame");
							}
						}
						
						if(arquivoConvertido)
							arquivoConvertidoXLSX.delete();
					}
				}
			}
		
		}
		
		return "";
	}
	
	private String extrairConsolidarDadosDeAgendamentosRegulada(String arquivo, String nomePlanilha, String TipoArquivoRegulada, String pastaDestinoArquivosNovasSolicitacoes, String tipoDeOferta)
	{
		ArrayList<AgendamentosPendentesRegulada> agendamentos;
		HashMap<String, ArrayList<AgendamentosPendentesRegulada>> dadosDoArquivoOriginal = new HashMap<String, ArrayList<AgendamentosPendentesRegulada>>();
		
		AcoesArquivoExcel excel = new AcoesArquivoExcel(arquivo, 0);
		excel.abrirPlanilha(0, 0);
		
		int cabecalho = 0;
		
		if(excel.getValorDaCelulaString(0, 0) == null)
			return "Erro";
		
		if(excel.getValorDaCelulaString(0, 0).trim().equals("Solicitado em:"))
			cabecalho = 0;
		else if(excel.getValorDaCelulaString(ParametrosArquivoAgendamentosPendentesRegulada.LINHA_CABECALHO.getIndice(), 0).trim().equals("Solicitado em:"))
			cabecalho = ParametrosArquivoAgendamentosPendentesRegulada.LINHA_CABECALHO.getIndice();
		
		try (FileInputStream in = new FileInputStream(arquivo)) { 
			agendamentos = ExcelBinder.readSheet(in, AgendamentosPendentesRegulada.class, 0, cabecalho, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		for(AgendamentosPendentesRegulada agendamento : agendamentos)
		{
			agendamento.setSolicitadoEm(converterData(agendamento.getSolicitadoEm()));
			LocalDate data = LocalDate.parse(agendamento.getSolicitadoEm().substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String textoData = data.getMonthValue() + "-" + data.getYear();
			
			if(dadosDoArquivoOriginal.containsKey(textoData))
			{
				dadosDoArquivoOriginal.get(textoData).add(agendamento);
			}
			else
			{
				ArrayList<AgendamentosPendentesRegulada> agendamentosNaCompetencia = new ArrayList<AgendamentosPendentesRegulada>();
				dadosDoArquivoOriginal.put(textoData, agendamentosNaCompetencia);
				
				dadosDoArquivoOriginal.get(textoData).add(agendamento);
			}
		}
		
		for(String competencia : dadosDoArquivoOriginal.keySet())
		{
			String[] dados = competencia.split("-");
			int mes = Integer.parseInt(dados[0]);
			int ano = Integer.parseInt(dados[1]);

			//criando nome do arquivo consolidado
			String pastaArquivosBaixados = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivosReguladasNovasSolicitacoes();
			Pasta pastaDestino = new Pasta(pastaArquivosBaixados, true);
			pastaArquivosBaixados = pastaArquivosBaixados + "\\" + ano;
			pastaDestino = new Pasta(pastaArquivosBaixados, true);
			
			String nomeArquivo = meses.getMeses().get(mes - 1).getMesNumero() + "." + String.valueOf(ano).substring(2) + " - " + meses.getMeses().get(mes - 1).getMesDescricao() + "_Regulada_Consultas e Exames." + ParametrosArquivoReguladaConsolidado.EXTENSAO_ARQUIVO_FORMATADO.getDescricao();
			Arquivo arquivoFinal = new Arquivo(pastaArquivosBaixados, nomeArquivo);
			
			System.out.println("Verificando a existência do arquivo " + pastaArquivosBaixados + "\\" + nomeArquivo);
			
			ArrayList<String> fichasProcessadas = new ArrayList<String>();
			
			if(!arquivoFinal.existe())
			{
				arquivoFinal = new Arquivo(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosParaAutomatizacao(), ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
				System.out.println("Copiar como: " + pastaArquivosBaixados + "\\" + ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao()); 
				arquivoFinal.CopiarArquivo(pastaArquivosBaixados + "\\" + ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
				nomeArquivo = meses.getMeses().get(mes - 1).getMesNumero() + "." + String.valueOf(ano).substring(2) + " - " + meses.getMeses().get(mes - 1).getMesDescricao() + "_Regulada_Consultas e Exames." + ParametrosArquivoReguladaConsolidado.EXTENSAO_ARQUIVO_FORMATADO.getDescricao();
				arquivoFinal = new Arquivo(pastaArquivosBaixados, ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
				System.out.println("Criado: " + pastaArquivosBaixados + "\\" + nomeArquivo); 
				
				arquivoFinal.renomear(nomeArquivo);
			}
			else
			{
				ArrayList<NovasSolicitacoesRegulada> listaDeSolicitacoes = null;
				try (FileInputStream in = new FileInputStream(pastaArquivosBaixados + "\\" + nomeArquivo)) { 
					listaDeSolicitacoes = ExcelBinder.readSheet(in, NovasSolicitacoesRegulada.class, 0, 0, true);
		        }
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				if(listaDeSolicitacoes != null)
				{
					for(NovasSolicitacoesRegulada solicitacao : listaDeSolicitacoes)
					{
						fichasProcessadas.add(solicitacao.getFicha().trim());
					}
				}
			}
			
			AcoesArquivoExcel arquivoDoMes = new AcoesArquivoExcel(pastaArquivosBaixados + "\\" + nomeArquivo, ParametrosArquivoReguladaConsolidado.ARQUIVO_FINAL_LINHA_INICIAL.getIndice());
			arquivoDoMes.abrirPlanilha(ParametrosArquivoReguladaConsolidado.NOME_PLANILHA_ARQUIVO_FORMATADO.getDescricao(), ParametrosArquivoReguladaConsolidado.ARQUIVO_FINAL_LINHA_INICIAL.getIndice());
			
			int ultimaLinhaPreenchida = arquivoDoMes.getUltimaLinhaPreenchida() + 1;
			
			ArrayList<CelulaExcel> celulasArquivoMensal = new ArrayList<CelulaExcel>();
			
			for(AgendamentosPendentesRegulada agendamento : dadosDoArquivoOriginal.get(competencia))
			{
				if(!fichasProcessadas.contains(agendamento.getFicha().trim()))
				{
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_SOLICITADO_EM.getIndice(), LocalDate.parse(agendamento.getSolicitadoEm().substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy")), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_SOLICITADO_EM.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getIndice(), agendamento.getFicha(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getIndice(), agendamento.getCodigoPaciente(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), agendamento.getUnidadeSolicitante(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_TIPO_DE_OFERTA.getIndice(), tipoDeOferta, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_TIPO_DE_OFERTA.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), agendamento.getEspecialidadeExame(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getIndice(), agendamento.getHipotese(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ARQUIVO.getIndice(), arquivo, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ARQUIVO.getTipo()));
					
					ultimaLinhaPreenchida++;
				}
			}
			
			arquivoDoMes.forcarCalculos();
			arquivoDoMes.gravarDadosEmCelula(0, celulasArquivoMensal);
		}
		
		return "";
	}
	
	private String extrairConsolidarDadosDeSolicitacoesRegulada(String arquivo, String nomePlanilha, String TipoArquivoRegulada, String pastaDestinoArquivosNovasSolicitacoes, String tipoDeOferta)
	{
		ArrayList<SolicitacoesPendentesRegulada> solicitacoes;
		HashMap<String, ArrayList<SolicitacoesPendentesRegulada>> dadosDoArquivoOriginal = new HashMap<String, ArrayList<SolicitacoesPendentesRegulada>>();
		
		AcoesArquivoExcel excel = new AcoesArquivoExcel(arquivo, 0);
		excel.abrirPlanilha(0, 0);
		
		int cabecalho = 0;
		
		if(excel.getValorDaCelulaString(0, 0) == null)
			return "Erro";
		
		if(excel.getValorDaCelulaString(0, 0).trim().equals("Solicitado em:"))
			cabecalho = 0;
		else if(excel.getValorDaCelulaString(ParametrosArquivoSolicitacoesPendentesRegulada.LINHA_CABECALHO.getIndice() - 1, 0).trim().equals("Solicitado em:"))
			cabecalho = ParametrosArquivoSolicitacoesPendentesRegulada.LINHA_CABECALHO.getIndice();
		else if(excel.getValorDaCelulaString(ParametrosArquivoSolicitacoesPendentesRegulada.LINHA_CABECALHO.getIndice(), 0).trim().equals("Solicitado em:"))
			cabecalho = ParametrosArquivoSolicitacoesPendentesRegulada.LINHA_CABECALHO.getIndice();
		
		try (FileInputStream in = new FileInputStream(arquivo)) { 
			solicitacoes = ExcelBinder.readSheet(in, SolicitacoesPendentesRegulada.class, 0, cabecalho, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		for(SolicitacoesPendentesRegulada solicitacao : solicitacoes)
		{
			//System.out.println(solicitacao.getSolicitadoEm());
			solicitacao.setSolicitadoEm(converterData(solicitacao.getSolicitadoEm()));
			LocalDate data = LocalDate.parse(solicitacao.getSolicitadoEm().substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String textoData = data.getMonthValue() + "-" + data.getYear();
			
			if(dadosDoArquivoOriginal.containsKey(textoData))
			{
				dadosDoArquivoOriginal.get(textoData).add(solicitacao);
			}
			else
			{
				ArrayList<SolicitacoesPendentesRegulada> solicitacoesNaCompetencia = new ArrayList<SolicitacoesPendentesRegulada>();
				dadosDoArquivoOriginal.put(textoData, solicitacoesNaCompetencia);
				
				dadosDoArquivoOriginal.get(textoData).add(solicitacao);
			}
		}
		
		for(String competencia : dadosDoArquivoOriginal.keySet())
		{
			String[] dados = competencia.split("-");
			int mes = Integer.parseInt(dados[0]);
			int ano = Integer.parseInt(dados[1]);

			//criando nome do arquivo consolidado
			String pastaArquivosBaixados = pastaBaseDemandaReprimidaCDIDR + "\\" + diretoriosCDIDR.getArquivosReguladasNovasSolicitacoes();
			Pasta pastaDestino = new Pasta(pastaArquivosBaixados, true);
			pastaArquivosBaixados = pastaArquivosBaixados + "\\" + ano;
			pastaDestino = new Pasta(pastaArquivosBaixados, true);
			
			String nomeArquivo = meses.getMeses().get(mes - 1).getMesNumero() + "." + String.valueOf(ano).substring(2) + " - " + meses.getMeses().get(mes - 1).getMesDescricao() + "_Regulada_Consultas e Exames." + ParametrosArquivoReguladaConsolidado.EXTENSAO_ARQUIVO_FORMATADO.getDescricao();
			Arquivo arquivoFinal = new Arquivo(pastaArquivosBaixados, nomeArquivo);
			
			System.out.println("Verificando a existência do arquivo " + pastaArquivosBaixados + "\\" + nomeArquivo);
			
			ArrayList<String> fichasProcessadas = new ArrayList<String>();
			
			if(!arquivoFinal.existe())
			{
				arquivoFinal = new Arquivo(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosParaAutomatizacao(), ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
				System.out.println("Copiar como: " + pastaArquivosBaixados + "\\" + ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao()); 
				arquivoFinal.CopiarArquivo(pastaArquivosBaixados + "\\" + ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
				nomeArquivo = meses.getMeses().get(mes - 1).getMesNumero() + "." + String.valueOf(ano).substring(2) + " - " + meses.getMeses().get(mes - 1).getMesDescricao() + "_Regulada_Consultas e Exames." + ParametrosArquivoReguladaConsolidado.EXTENSAO_ARQUIVO_FORMATADO.getDescricao();
				arquivoFinal = new Arquivo(pastaArquivosBaixados, ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
				System.out.println("Criado: " + pastaArquivosBaixados + "\\" + nomeArquivo); 
				
				arquivoFinal.renomear(nomeArquivo);
			}
			else
			{
				ArrayList<NovasSolicitacoesRegulada> listaDeSolicitacoes = null;
				try (FileInputStream in = new FileInputStream(pastaArquivosBaixados + "\\" + nomeArquivo)) { 
					listaDeSolicitacoes = ExcelBinder.readSheet(in, NovasSolicitacoesRegulada.class, 0, 0, true);
		        }
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				if(listaDeSolicitacoes != null)
				{
					for(NovasSolicitacoesRegulada solicitacao : listaDeSolicitacoes)
					{
						fichasProcessadas.add(solicitacao.getFicha().trim());
					}
				}
			}
			
			AcoesArquivoExcel arquivoDoMes = new AcoesArquivoExcel(pastaArquivosBaixados + "\\" + nomeArquivo, ParametrosArquivoReguladaConsolidado.ARQUIVO_FINAL_LINHA_INICIAL.getIndice());
			arquivoDoMes.abrirPlanilha(ParametrosArquivoReguladaConsolidado.NOME_PLANILHA_ARQUIVO_FORMATADO.getDescricao(), ParametrosArquivoReguladaConsolidado.ARQUIVO_FINAL_LINHA_INICIAL.getIndice());
			
			int ultimaLinhaPreenchida = arquivoDoMes.getUltimaLinhaPreenchida() + 1;
			
			ArrayList<CelulaExcel> celulasArquivoMensal = new ArrayList<CelulaExcel>();
			
			for(SolicitacoesPendentesRegulada solicitacao : dadosDoArquivoOriginal.get(competencia))
			{
				if(!fichasProcessadas.contains(solicitacao.getFicha().trim()))
				{
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_SOLICITADO_EM.getIndice(), LocalDate.parse(solicitacao.getSolicitadoEm().substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy")), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_SOLICITADO_EM.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getIndice(), solicitacao.getFicha(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getIndice(), solicitacao.getCodigoPaciente(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), solicitacao.getUnidadeSolicitante(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_TIPO_DE_OFERTA.getIndice(), tipoDeOferta, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_TIPO_DE_OFERTA.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), solicitacao.getEspecialidadeExame(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getIndice(), solicitacao.getHipotese(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getTipo()));
					celulasArquivoMensal.add(new CelulaExcel(ultimaLinhaPreenchida, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ARQUIVO.getIndice(), arquivo, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ARQUIVO.getTipo()));
					
					ultimaLinhaPreenchida++;
				}
			}
			
			arquivoDoMes.forcarCalculos();
			arquivoDoMes.gravarDadosEmCelula(0, celulasArquivoMensal);
		}
		
		return "";
	}
	
	private String preencherNovasSolicitacoesCDR(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, HashMap<String, String> elementosRadioUnidades, ArrayList<EntidadeExecutanteR1> entidades, HashMap<String, Integer> entradasPorOferta)
	{
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Relatório");
		opcoes.add("Demanda por Recurso  >>");
		opcoes.add("D03 - Demanda por Recurso Qualitativo");
		
		
		String value = elementosRadioUnidades.get("5416655 - SMS - CAMPINAS");
		//System.out.println(value);
		
		
		if(value != null)
		{
			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
		
			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
			
			boolean unidadeEncontrada = paginaWeb.clicarRadioInputByValue(driver, value);
			
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_BOTAO_OK_ESCOLHER_UNIDADE.getTextoIdentificador(), "id");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean visivel;
			do
			{
			
				visivel = acessarMenu(driver, paginaWeb, opcoes);
				
			
			}while(!visivel);
			
			//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			EntidadeExecutanteR1 entidade = new EntidadeExecutanteR1("5416655", "PRÓPRIO", "SMS - CAMPINAS", "SMS - CAMPINAS", "SMS - CAMPINAS");
			
			baixarConsolidarArquivosDeNovasSolicitacoes(driver, paginaWeb, entidade, entidades, entradasPorOferta);
			
		}
		
		return "";
	}
	
	private String agruparDadosPorEspecialidade()
	{
		
		return "";
	}
	
	private String baixarConsolidarArquivosDeNovasSolicitacoes(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, ArrayList<EntidadeExecutanteR1> entidades, HashMap<String, Integer> entradasPorOferta) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		String pastaDestinoArquivosNovasSolicitacoes = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivosNovasSolicitacoesConsolidada();
		
		String[] tiposDeBusca = new String[2];
		tiposDeBusca[0] = "Consulta";
		tiposDeBusca[1] = "Exame";
		
		LocalDate dataFinal;
		LocalDate dataInicial;
		LocalDate hoje = LocalDate.now();
		
		if(hoje.isBefore(dataInicioCompetencia))
			dataInicial = hoje;
		else
			dataInicial = dataInicioCompetencia;
			
		if(hoje.isBefore(dataFinalCompetencia))
			dataFinal = hoje;
		else
			dataFinal = dataFinalCompetencia;
		
		String dataInicioFormatada;
		String dataFimFormatada;
		
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		dataInicioFormatada = dataInicial.format(formatoData);
		dataFimFormatada = dataFinal.format(formatoData);
		
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicial.format(formatter);
		
		int linhaArquivoConsolidadoMensal = ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_VAZIO_LINHA_INICIAL.getIndice();
		
		System.out.println(pastaDestinoArquivosNovasSolicitacoes + ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_CDR_NOME.getDescricao());
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaDestinoArquivosNovasSolicitacoes + "\\" + ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_CDR_NOME.getDescricao(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoNovasSolicitacoesConsolidado.NOME_PLANILHA_BD_CDR.getDescricao(), ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice());
		int proximaLinhaVaziaConsolidadoMunicipal = arquivoConsolidado.getUltimaLinhaPreenchida() + 1;
		
		//System.out.println(driver.getPageSource());
		
		String abaPrincipal = driver.getWindowHandle();
				
		for(int i = 0; i < tiposDeBusca.length; i++)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_FILTRO_TIPO_RELATORIO.getTextoIdentificador(), tiposDeBusca[i]);
			
			paginaWeb.limparInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_FILTRO_DATA_INICIAL.getTextoIdentificador());
			paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_FILTRO_DATA_INICIAL.getTextoIdentificador(), dataInicioFormatada.replaceAll("-", ""));
					
			paginaWeb.limparInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_FILTRO_DATA_FINAL.getTextoIdentificador());
			paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_FILTRO_DATA_FINAL.getTextoIdentificador(), dataFimFormatada.replaceAll("-", ""));
			paginaWeb.tirarFocoDoCampoTexto(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_FILTRO_DATA_FINAL.getTextoIdentificador());
			
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			
			if(tiposDeBusca[i].equals("Consulta")) 
			{
				paginaWeb.selecionarItemSelectPeloTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_RELATORIO_CDR_QUALITATIVO_FILTRO_TIPO_CONSULTA.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_RELATORIO_CDR_QUALITATIVO_FILTRO_TIPO_CONSULTA.getTextoIdentificador());
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_BOTAO_DOWNLOAD.getTextoIdentificador(), "name");
			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_CDR_QUALITATIVO_BOTAO_DOWNLOAD.getTextoIdentificador());
			
			do
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			
			
			String arquivoMaisRecente;
			int contador = 1;
			
			do
			{
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				System.out.println("Passada: " + contador++);
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				arquivoMaisRecente = pastaOrigem.arquivoRecentementeModificado();
				
				Set<String> abas = driver.getWindowHandles();
				
				boolean realizarNovaTentativa = false;
				boolean fecharAba = false;
				
				boolean existeBotaoBadGatewayGoBack = false;
				for (String aba : abas) {
			    	driver.switchTo().window(aba);
			    	//existeBotaoBadGatewayGoBack = !driver.findElements(By.xpath("//button[contains(translate(@aria-label, '\u00A0', ' '), 'Go back')]")).isEmpty();
			    	
			    		    	
			    	
			    	
			    	try
			    	{
//			    		System.out.println(driver.getPageSource());
//
//						java.util.List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
//						
//						for (int indice = 0; indice < iframes.size(); indice++) {
//						    System.out.println("iframe " + indice + ": " + iframes.get(indice).getAttribute("outerHTML"));
//						}

			    		existeBotaoBadGatewayGoBack = !driver.findElements(By.xpath("//button[contains(normalize-space(translate(@aria-label, '\u00A0', ' ')), 'Go back')]")).isEmpty();
			    	}
			    	catch(Exception e)
			    	{
			    		e.printStackTrace();
			    		
			    		fecharAba = true;
			    		realizarNovaTentativa = true;
			    	}
				    	
//			    	System.out.println(abaPrincipal);
//			    	System.out.println(aba);
//			    	System.out.println(existeBotaoBadGatewayGoBack);
//			    	//System.out.println(driver.getPageSource());
			    	
			    	if(existeBotaoBadGatewayGoBack)
			    	{
			    		realizarNovaTentativa = true;
			    		
			    		if(aba.equals(abaPrincipal))
			    		{
			    			System.out.println("Dentro da própria página");
			    			
				    		//WebElement button = driver.findElement(By.xpath("//button[contains(translate(@aria-label, '\u00A0', ' '), 'Go back')]"));
				    		WebElement button = driver.findElement(By.xpath("//button[contains(normalize-space(translate(@aria-label, '\u00A0', ' ')), 'Go back')]"));
							button.click();
			    		}
			    		else
			    		{
			    			System.out.println("Nova aba a ser fechada");
			    			
			    			fecharAba = true;
			    		}


			    	}
			    	
		    		if(realizarNovaTentativa) {
		    			
		    			if(fecharAba)
		    			{
				    		driver.close(); // fecha essa aba
				    		driver.switchTo().window(abaPrincipal);
		    			}
		    			
		    			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
		    			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_RELATORIO_CDR_QUALITATIVO_BOTAO_DOWNLOAD.getTextoIdentificador(), "name");
						paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_RELATORIO_CDR_QUALITATIVO_BOTAO_DOWNLOAD.getTextoIdentificador());
			    		break;
		    		}
        
				}
				if(!existeBotaoBadGatewayGoBack)
				{
					System.out.println("Reorganizando acesso aos iframes");
					
					driver.switchTo().window(abaPrincipal);
					paginaWeb.voltarAoTopoDaPagina(driver);
					paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
					paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
				}
				
				System.out.println(arquivoMaisRecente + " ----- " + ultimoRecente);
			}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(ParametrosArquivoNovasSolicitacoesConsolidado.EXTENSAO_ARQUIVO_BAIXADO.getDescricao()));
			
			Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);
			arquivo.renomear(tiposDeBusca[i].toUpperCase() + "." + ParametrosArquivoNovasSolicitacoesConsolidado.EXTENSAO_ARQUIVO_BAIXADO.getDescricao());
			
			String caminhoPastaOriginais = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosOriginaisProducao() + "\\" + anoCompetencia;
			
			Pasta pastaOriginais = new Pasta(caminhoPastaOriginais, true);
			
			String composicaoPastaNoMes = meses.getMeses().get(mesCompetencia - 1).getMesNumero() + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao();
			caminhoPastaOriginais += "\\" + composicaoPastaNoMes;
			pastaOriginais = new Pasta(caminhoPastaOriginais, true);
			caminhoPastaOriginais += "\\" + IdentificadoresPastasCompartilhadasCDIDR.TEXTO_PASTA_NOVAS_SOLICITACOES_CDR.getTextoIdentificador();
			pastaOriginais = new Pasta(caminhoPastaOriginais, true);
			
			System.out.println(caminhoPastaOriginais + "\\" + tiposDeBusca[i].toUpperCase() + "." + ParametrosArquivoNovasSolicitacoesConsolidado.EXTENSAO_ARQUIVO_BAIXADO.getDescricao());
			arquivo.mover(caminhoPastaOriginais + "\\" + tiposDeBusca[i].toUpperCase() + "." + ParametrosArquivoNovasSolicitacoesConsolidado.EXTENSAO_ARQUIVO_BAIXADO.getDescricao());
			
			arquivo = new Arquivo(caminhoPastaOriginais, tiposDeBusca[i].toUpperCase() + "." + ParametrosArquivoNovasSolicitacoesConsolidado.EXTENSAO_ARQUIVO_BAIXADO.getDescricao());
							
			ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
			
			manipularArquivosDeNovasSolicitacoes(arquivoConsolidado, entidade, tiposDeBusca[i], pastaDestinoArquivosNovasSolicitacoes, arquivo, entidades, entradasPorOferta);

		}
		
		return "";
	}
	
	private String baixarArquivos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, String tipoDeBusca, String nomeBotaoSubmit) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		paginaWeb.clicarBotaoSubmit(driver, nomeBotaoSubmit, "name");
			
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
		}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(ParametrosArquivoOfertaDemanda.EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO.getDescricao()));
			
		Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);
		arquivo.renomear(entidade.getExecutante() + " - " + tipoDeBusca.toUpperCase() + " " + arquivoMaisRecente);
		
		transferirArquivos(entidade, tipoDeBusca, arquivo);
		
		ultimoRecente = arquivo.getNomeDoArquivo();
			

		return "";
	}
	
	private String transferirArquivos(EntidadeExecutanteR1 entidade, String tipoDeBusca, Arquivo arquivo)
	{
		
		Pasta pasta = new Pasta(pastaBaseAmbulatorialCDIDR, true);
			
		String pastaEntidade = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosOriginaisProducao();
		pasta = new Pasta(pastaEntidade, true);
			
		pastaEntidade = pastaEntidade + "\\" + anoCompetencia;
		pasta = new Pasta(pastaEntidade, true);
		
		String mes = "";
		if(mesCompetencia < 10)
			mes = "0" + mesCompetencia;
		else
			mes = "" + mesCompetencia;
		
		pastaEntidade = pastaEntidade + "\\" + mes + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao().toUpperCase();
		pasta = new Pasta(pastaEntidade, true);
		
		pastaEntidade = pastaEntidade + "\\" + tipoDeBusca.toUpperCase();
		pasta = new Pasta(pastaEntidade, true);
		
		apagarArquivosDaEntidade(pastaEntidade, entidade);
		
		arquivo.mover(pastaEntidade + "\\" + arquivo.getNomeDoArquivo());
				
		return "";
	}
	
	private String apagarArquivosDaEntidade(String pasta, EntidadeExecutanteR1 entidade)
	{
		Pasta pastaFinal = new Pasta(pasta, false);
		
		File[] conteudoDaPasta = pastaFinal.listarDiretorio();
		
		//Encontrando arquivo mais recente pelo nome
		String arquivoMaisRecente = "";

		for(File itemDaPasta : conteudoDaPasta)
		{
			if(!itemDaPasta.isDirectory())
			{
				if(itemDaPasta.getName().startsWith(entidade.getExecutante()))
					itemDaPasta.delete();
			}
			
		}
		
		return "";
	}
	
	public String testeArquivoNovasSolicitacoes(boolean executarNovasSolicitacoesCDR, boolean executarNovasSolicitacoesRegulada, boolean consolidarNovasSolicitacoesRegulada, boolean executarDemandaReprimida)
	{
		String pastaDestinoArquivosNovasSolicitacoes = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivosNovasSolicitacoesConsolidada();

		HashMap<String, Integer> entradasPorOferta = new HashMap<String, Integer>();
		ArrayList<EntidadeExecutanteR1> entidades = lerEntidadesR1(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoUnidadesExecutantes());
		EntidadeExecutanteR1 entidade = new EntidadeExecutanteR1("5416655", "PRÓPRIO", "SMS - CAMPINAS", "SMS - CAMPINAS", "SMS - CAMPINAS");
		
		if(dataInicioCompetencia.isBefore(LocalDate.now()))
		{
		
			if(executarNovasSolicitacoesCDR)
			{
				AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaDestinoArquivosNovasSolicitacoes + "\\" + ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_CDR_NOME.getDescricao(), 0);
				
				String pastaArquivosCDR = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosOriginaisProducao() + "\\" + anoCompetencia + "\\";
				if(mesCompetencia < 10)
					pastaArquivosCDR += "0" + mesCompetencia + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + "\\NOVAS ENTRADAS CDR";
				else
					pastaArquivosCDR += mesCompetencia + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + "\\NOVAS ENTRADAS CDR";
				
				Arquivo arquivoConsulta = new Arquivo(pastaArquivosCDR, "CONSULTA.xls");
				Arquivo arquivoExame = new Arquivo(pastaArquivosCDR, "EXAME.xls");
	
				manipularArquivosDeNovasSolicitacoes(arquivoConsolidado, entidade, "Consulta", pastaDestinoArquivosNovasSolicitacoes, arquivoConsulta, entidades, entradasPorOferta);
				manipularArquivosDeNovasSolicitacoes(arquivoConsolidado, entidade, "Exame", pastaDestinoArquivosNovasSolicitacoes, arquivoExame, entidades, entradasPorOferta);
			}
			
			if(executarNovasSolicitacoesRegulada)
			{
				preencherNovasSolicitacoesRegulada(entidades, consolidarNovasSolicitacoesRegulada, entradasPorOferta);
			}
			
			if(executarNovasSolicitacoesCDR || executarNovasSolicitacoesRegulada)
				preencherConsolidacaoNovasSolicitacoes(entidades, entradasPorOferta);
			
			if(executarDemandaReprimida)
			{
				HashMap <String, Integer> demandaPorEspecialidade = new HashMap<String, Integer>();
				HashMap <String, Integer> ofertaPorEspecialidade = new HashMap<String, Integer>();
				HashMap <String, Integer> maximoTempoPorEspecialidade = new HashMap<String, Integer>();
				HashMap <String, Boolean> existeOfertaPorEspecialidade = new HashMap<String, Boolean>();
				
				preencherDemandaReprimida(entidades, ofertaPorEspecialidade, demandaPorEspecialidade, maximoTempoPorEspecialidade, existeOfertaPorEspecialidade);
				
				montarPlanilhaDeDemandas(entradasPorOferta, ofertaPorEspecialidade, demandaPorEspecialidade, maximoTempoPorEspecialidade, existeOfertaPorEspecialidade);
			}
		}
		
		
		return "";
	}
	
	private String montarPlanilhaDeDemandas(HashMap<String, Integer> entradasPorOferta, HashMap<String, Integer> ofertaPorEspecialidade, HashMap<String, Integer> demandasPorEspecialidade, HashMap<String, Integer> maximoTempoPorEspecialidade, HashMap<String, Boolean> existeOfertaPorEspecialidade)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaPlanilhaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), 0);
		int ultimaLinhaLivre = arquivoConsolidado.getUltimaLinhaPreenchida() + 1;
		int linhaExcel;
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		CorrelacaoArquivosOfertaDemanda correlacoes = new CorrelacaoArquivosOfertaDemanda();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		ArrayList<String> especialidades = new ArrayList<String>();
		
		for(String entrada : entradasPorOferta.keySet())
			especialidades.add(entrada);
		
		for(String demanda : demandasPorEspecialidade.keySet())
			if(!especialidades.contains(demanda))
				especialidades.add(demanda);
		

		Demanda demanda;
		for(String especialidade : especialidades)
		{
			if(demandasProcessadas.containsKey(especialidade + inicioCompetenciaFormatado))
			{
				demanda = demandasProcessadas.get(especialidade + inicioCompetenciaFormatado);
				linhaExcel = demanda.getLinhaExcel();
			}
			else
			{
				demanda = new Demanda();
				
				linhaExcel = ultimaLinhaLivre;
				ultimaLinhaLivre++;
				demanda.setLinhaExcel(linhaExcel);
			}
			
			demanda.setProcedimento(especialidade);
			demanda.setCompetencia(inicioCompetenciaFormatado);
			
			if(entradasPorOferta.containsKey(especialidade))
				demanda.setNovasSolicitacoes(String.valueOf(entradasPorOferta.get(especialidade)));
			else
				demanda.setNovasSolicitacoes("-");
			
			if(demandasPorEspecialidade.containsKey(especialidade))
				demanda.setDemandaReprimida(String.valueOf(demandasPorEspecialidade.get(especialidade)));
			else
				demanda.setDemandaReprimida("-");
			
			if(existeOfertaPorEspecialidade.containsKey(especialidade))
			{
				int ofertaTotal = 0; 
						
				if(existeOfertaPorEspecialidade.get(especialidade).booleanValue())
				{
					ofertaTotal = ofertaPorEspecialidade.get(especialidade);
					demanda.setOfertaTotal(String.valueOf(ofertaTotal));
				}
				else
				{
					demanda.setOfertaTotal("-");
					demanda.setTempoDeEspera("-");
				}
				
				if(!demanda.getDemandaReprimida().equals("-"))
				{
					demanda.setOfertaTotal(String.valueOf(ofertaTotal));
					demanda.setMaisVelhoNaFila(String.valueOf(maximoTempoPorEspecialidade.get(especialidade)));
					
					if(ofertaTotal > 0)
					{
						demanda.setTempoDeEspera(String.valueOf((int) 1.0 * demandasPorEspecialidade.get(especialidade) / ofertaPorEspecialidade.get(especialidade)));
					}
					else
					{
						demanda.setTempoDeEspera("-");
					}
				}
				else
				{
					demanda.setTempoDeEspera("-");
					demanda.setMaisVelhoNaFila("-");
				}
									
			}
			else
			{
				demanda.setOfertaTotal("-");
				demanda.setTempoDeEspera("-");
				
				if(!demanda.getDemandaReprimida().equals("-"))
				{
					demanda.setMaisVelhoNaFila(String.valueOf(maximoTempoPorEspecialidade.get(especialidade)));
				}
				else
				{
					demanda.setMaisVelhoNaFila("-");
				}
			}
				

			celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_PROCEDIMENTOS.getIndice(), demanda.getProcedimento(), "String"));
			celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_COMPETENCIA.getIndice(), dataInicioCompetencia, "Date mes/ano"));

			if(demanda.getNovasSolicitacoes().equals("-"))
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES.getIndice(), demanda.getNovasSolicitacoes(), "String"));
			else
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES.getIndice(), Integer.parseInt(demanda.getNovasSolicitacoes()), "Int"));
			
			if(demanda.getDemandaReprimida().equals("-"))
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA.getIndice(), demanda.getDemandaReprimida(), "String"));
			else
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA.getIndice(), Integer.parseInt(demanda.getDemandaReprimida()), "Int"));
			
			if(demanda.getOfertaTotal().equals("-"))
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), demanda.getOfertaTotal(), "String"));
			else
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), Integer.parseInt(demanda.getOfertaTotal()), "Int"));
			
			if(demanda.getTempoDeEspera().equals("-"))
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getIndice(), demanda.getTempoDeEspera(), "String"));
			else
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getIndice(), Integer.parseInt(demanda.getTempoDeEspera()), "Int"));
			
			if(demanda.getMaisVelhoNaFila().equals("-"))
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_MAIS_VELHO_NA_FILA.getIndice(), demanda.getMaisVelhoNaFila(), "String"));
			else
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_MAIS_VELHO_NA_FILA.getIndice(), Integer.parseInt(demanda.getMaisVelhoNaFila()), "Int"));
				
		}
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaPlanilhaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaPlanilhaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String manipularArquivosDeNovasSolicitacoes(AcoesArquivoExcel arquivoConsolidado, EntidadeExecutanteR1 entidade, String tipoDeBusca, String pastaDestinoArquivosNovasSolicitacoes, Arquivo arquivo, ArrayList<EntidadeExecutanteR1> entidades, HashMap<String, Integer> dadosDeOfertas)
	{
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoNovasSolicitacoesConsolidado.NOME_PLANILHA_BD_CDR.getDescricao(), ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice());
		int proximaLinhaVaziaConsolidadoMunicipal = arquivoConsolidado.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getIndice()) + 1;
		
		//configurando a manipulação de arquivos
		entidade.setArquivoBaixadoXLS(arquivo.getNomeDoArquivo());
		entidade.setCaminhoCompletoArquivoBaixadoXLS(arquivo.getCaminhoCompleto());
		
		entidade.setArquivoBaixadoXLSX(arquivo.getNomeDoArquivo() + "x");
		entidade.setCaminhoCompletoArquivoBaixadoXLSX(arquivo.getCaminhoCompleto() + "x");
		
		//criando nome do arquivo consolidado
		String pastaArquivosBaixados = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivosCDRNovasSolicitacoes();
		Pasta pastaDestino = new Pasta(pastaArquivosBaixados, true);
		pastaArquivosBaixados = pastaArquivosBaixados + "\\" + anoCompetencia;
		pastaDestino = new Pasta(pastaArquivosBaixados, true);
		
		Arquivo arquivoFinal = new Arquivo(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosParaAutomatizacao(), ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
		
		arquivoFinal.CopiarArquivo(pastaArquivosBaixados + "\\" + ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
		
		String nomeArquivo = meses.getMeses().get(mesCompetencia - 1).getMesNumero() + "." + String.valueOf(anoCompetencia).substring(2) + " - " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + " " + tipoDeBusca.toUpperCase() + "." + ParametrosArquivoNovasSolicitacoesConsolidado.EXTENSAO_ARQUIVO_CONSOLIDADO.getDescricao();
		arquivoFinal = new Arquivo(pastaArquivosBaixados, ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
		arquivoFinal.renomear(nomeArquivo);

		
		AcoesArquivoExcel arquivoMensal = new AcoesArquivoExcel(arquivoFinal.getCaminhoCompleto(), 0);
		arquivoMensal.abrirPlanilha(ParametrosArquivoNovasSolicitacoesConsulta.NOME_PLANILHA_ARQUIVO_FORMATADO.getDescricao(), 0);
		int primeiraLinhaVaziaArquivoMensal = arquivoMensal.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoNovasSolicitacoesConsulta.ARQUIVO_FINAL_LINHA_INICIAL.getIndice(), ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_COD_PACIENTE.getIndice()) + 1;
		int linhaArquivoMensal = primeiraLinhaVaziaArquivoMensal;
					
		ConversaoHMTL_XLSX conversor = new ConversaoHMTL_XLSX();
		
		try
		{
			conversor.converterArquivoHTML(entidade.getCaminhoCompletoArquivoBaixadoXLS(), entidade.getCaminhoCompletoArquivoBaixadoXLSX(), false);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(arquivoFinal.getCaminhoCompleto());
		
		AcoesArquivoExcel arquivoSIRESP = new AcoesArquivoExcel(entidade.getCaminhoCompletoArquivoBaixadoXLSX(), 0);
		System.out.println(entidade.getCaminhoCompletoArquivoBaixadoXLSX());
		
		ArrayList<CelulaExcel> celulasArquivoConsolidado = new ArrayList<CelulaExcel>();
		ArrayList<CelulaExcel> celulasArquivoMensal = new ArrayList<CelulaExcel>();
		
		int ultimaLinhaArquivoSIRESP = arquivoSIRESP.getUltimaLinhaPreenchida();
		
		int primeiraLinhaArquivoSIRESP = 0;
		if(tipoDeBusca.equals("Exame"))
			primeiraLinhaArquivoSIRESP = ParametrosArquivoNovasSolicitacoesExame.ARQUIVO_BAIXADO_LINHA_INICIAL.getIndice();
		else if(tipoDeBusca.equals("Consulta"))
			primeiraLinhaArquivoSIRESP = ParametrosArquivoNovasSolicitacoesConsulta.ARQUIVO_BAIXADO_LINHA_INICIAL.getIndice();
		
		int colunaUnidadeSolicitante = 0;
		if(tipoDeBusca.equals("Exame"))
			colunaUnidadeSolicitante = ParametrosArquivoNovasSolicitacoesExame.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice();
		else if(tipoDeBusca.equals("Consulta"))
			colunaUnidadeSolicitante = ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice();
		
		int colunaDataInclusao = 0;
		if(tipoDeBusca.equals("Exame"))
			colunaDataInclusao = ParametrosArquivoNovasSolicitacoesExame.INDICE_COLUNA_DATA_INCLUSAO.getIndice();
		else if(tipoDeBusca.equals("Consulta"))
			colunaDataInclusao = ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_DATA_INCLUSAO.getIndice();
		
		String nomePlanilhaArquivoMensal = "";
		if(tipoDeBusca.equals("Exame"))
			nomePlanilhaArquivoMensal = ParametrosArquivoNovasSolicitacoesExame.NOME_PLANILHA_ARQUIVO_FORMATADO.getDescricao();
		else if(tipoDeBusca.equals("Consulta"))
			nomePlanilhaArquivoMensal = ParametrosArquivoNovasSolicitacoesConsulta.NOME_PLANILHA_ARQUIVO_FORMATADO.getDescricao();
		
		ArrayList<NovasSolicitacoes> solicitacoesDaCompetencia = new ArrayList<NovasSolicitacoes>();
		
		for(int linha = primeiraLinhaArquivoSIRESP; linha <= ultimaLinhaArquivoSIRESP; linha++)
		{
			System.out.println(linha + " " + colunaUnidadeSolicitante);
			
			String unidadeSolicitante = arquivoSIRESP.getValorDaCelulaString(linha, colunaUnidadeSolicitante).trim();
			
			System.out.println(linha + " " + unidadeSolicitante);

			if(unidadesSolicitantes.contains(unidadeSolicitante))
			{
				//arquivo Consolidado Municipal
				
//				celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getIndice(), tipoDeBusca, ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getTipo()));
//				
//				LocalDateTime valorDateTime = arquivoSIRESP.getValorDaCelulaDateTime(linha, ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_DATA_INCLUSAO.getIndice(), ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_DATA_INCLUSAO.getFormato());
//				LocalDate valorData = valorDateTime.toLocalDate();
//				
//				celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_DATA_INCLUSAO.getIndice(), valorData, "dd/MM/yyyy"));
//				celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ANO_INCLUSAO.getIndice(), anoCompetencia, ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ANO_INCLUSAO.getTipo()));
//				
//				CorrelacaoArquivosNovasSolicitacoes correlacoes = new CorrelacaoArquivosNovasSolicitacoes();
//				
//				ArrayList<CorrelacaoColunasArquivos> colunasConsolidado = correlacoes.obterCorrelacaoEntreArquivos(tipoDeBusca);
//								
//				for(CorrelacaoColunasArquivos coluna : colunasConsolidado)
//				{
//					//System.out.println("ColunaSIRESP: " + coluna.getColunaSIRESP() + " Coluna Consolidado: " + coluna.getColunaConsolidado() + " Formato: " + coluna.getFormato());
//					
//					if(arquivoSIRESP.ehCelulaVazia(linha, coluna.getColunaSIRESP()))
//					{
//						celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, coluna.getColunaSIRESP(), "", "String"));
//					}
//					else
//					{
//						if(coluna.getTipo().equals("String"))
//						{
//							String valor = arquivoSIRESP.getValorDaCelulaString(linha, coluna.getColunaSIRESP());
//							celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
//						}else if(coluna.getTipo().equals("Date"))
//						{
//							LocalDate valor = arquivoSIRESP.getValorDaCelulaDate(linha, coluna.getColunaSIRESP());
//							celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
//						}else if(coluna.getTipo().equals("DateTime"))
//						{
//							LocalDateTime valor = arquivoSIRESP.getValorDaCelulaDateTime(linha, coluna.getColunaSIRESP(), coluna.getFormato());
//							celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
//						}else if(coluna.getTipo().equals("Time"))
//						{
//							LocalTime valor = arquivoSIRESP.getValorDaCelulaTime(linha, coluna.getColunaSIRESP(), coluna.getFormato());
//							celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
//						}else if(coluna.getTipo().equals("Int"))
//						{
//							Integer valor = Integer.parseInt(arquivoSIRESP.getValorDaCelulaString(linha, coluna.getColunaSIRESP()));
//							celulasArquivoConsolidado.add(new CelulaExcel(proximaLinhaVaziaConsolidadoMunicipal, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
//						}
//					}
//					
//				}
//			
//				proximaLinhaVaziaConsolidadoMunicipal++;
				
				//construindo arquivo Consolidado
				
				//String tipoSolicitacao = arquivoSIRESP.getValorDaCelulaString(linha, ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_TIPO_SOLICITACAO.getIndice()).trim();
				String tipoSolicitacao = tipoDeBusca;
				String especialidade = arquivoSIRESP.getValorDaCelulaString(linha, ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice()).toUpperCase().trim();
				String cid = arquivoSIRESP.getValorDaCelulaString(linha, ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_CID.getIndice()).trim();
				String unidade = arquivoSIRESP.getValorDaCelulaString(linha, ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice()).trim();
				
				LocalDateTime dataInclusao = arquivoSIRESP.getValorDaCelulaDateTime(linha, ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_DATA_INCLUSAO.getIndice(), ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_DATA_INCLUSAO.getFormato());
				String mes = meses.getMeses().get(dataInclusao.getMonthValue() - 1).getMesDescricao();
				String ano = String.valueOf(dataInclusao.getYear());
				
				String valorConcatenadoConsolidado = tipoSolicitacao + especialidade + cid + unidade + mes + ano;

				//System.out.println(valorConcatenadoConsolidado);
				
				if(novasSolicitacoesCDR.containsKey(valorConcatenadoConsolidado))
				{
					NovasSolicitacoes entradaNovaSolicitacao = novasSolicitacoesCDR.get(valorConcatenadoConsolidado);
					entradaNovaSolicitacao.setQtdeSolicitacoes(entradaNovaSolicitacao.getQtdeSolicitacoes() + 1);
				}
				else
				{
					NovasSolicitacoes entradaNovaSolicitacao = new NovasSolicitacoes();
					entradaNovaSolicitacao.setTipoSolicitacao(tipoDeBusca);
					entradaNovaSolicitacao.setEspecialidadeExame(especialidade);
					entradaNovaSolicitacao.setCID(cid);
					entradaNovaSolicitacao.setUnidadesCampinas(unidade);
					entradaNovaSolicitacao.setMesInclusao(mes);
					entradaNovaSolicitacao.setAnoInclusao(ano);
					entradaNovaSolicitacao.setQtdeSolicitacoes(1);
					entradaNovaSolicitacao.setLinhaExcel(proximaLinhaVaziaConsolidadoMunicipal);
					
					if(nomenclaturasPadronizadas.containsKey(especialidade.toUpperCase()))
						entradaNovaSolicitacao.setNomenclaturaPadronizada(nomenclaturasPadronizadas.get(especialidade.toUpperCase()).getNomenclatura());
					else
						entradaNovaSolicitacao.setNomenclaturaPadronizada("Não encontrada");
					
					novasSolicitacoesCDR.put(valorConcatenadoConsolidado, entradaNovaSolicitacao);
					
					proximaLinhaVaziaConsolidadoMunicipal++;
				}
				
				//arquivo Consolidado Mensal
				
				for(int coluna = 0; coluna <= ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_UNIDADE_INDICADA_PARA_AGENDAMENTO.getIndice(); coluna++)
				{
					//System.out.println("Censo: " + linhaCenso + ", Diário: " + linhaDiario + ", Coluna: " + coluna);
					//arquivoConsolidado.copiarFormatoEntreLinhas(linhaCenso - 1, linhaCenso);
					
					if(arquivoSIRESP.ehCelulaVazia(linha, coluna))
					{
						celulasArquivoMensal.add(new CelulaExcel(linhaArquivoMensal, coluna, "", "String"));
					}
					else
					{
						//System.out.println(ParametrosArquivoCenso.poIdUnico(coluna).getDescricao() + " " + ParametrosArquivoCenso.poIdUnico(coluna).getTipo());
						
						if(ParametrosArquivoNovasSolicitacoesConsulta.poIdUnico(coluna).getTipo().equals("String"))
						{
							if(arquivoSIRESP.ehCelulaComString(linha, coluna))
							{
								if(arquivoSIRESP.getValorDaCelulaString(linha, coluna).matches("^\\d+$"))
									celulasArquivoMensal.add(new CelulaExcel(linhaArquivoMensal, coluna, Integer.parseInt(arquivoSIRESP.getValorDaCelulaString(linha, coluna)), "String"));
								else
									celulasArquivoMensal.add(new CelulaExcel(linhaArquivoMensal, coluna, arquivoSIRESP.getValorDaCelulaString(linha, coluna), "String"));
							}
							else
								celulasArquivoMensal.add(new CelulaExcel(linhaArquivoMensal, coluna, Integer.toString(arquivoSIRESP.getValorDaCelulaInt(linha, coluna)), "String"));
						}
						else if(ParametrosArquivoNovasSolicitacoesConsulta.poIdUnico(coluna).getTipo().equals("Int"))
							celulasArquivoMensal.add(new CelulaExcel(linhaArquivoMensal, coluna, Integer.valueOf(arquivoSIRESP.getValorDaCelulaInt(linha, coluna)), "String"));
						else if(ParametrosArquivoNovasSolicitacoesConsulta.poIdUnico(coluna).getTipo().equals("Date"))
						{
							celulasArquivoMensal.add(new CelulaExcel(linhaArquivoMensal, coluna, arquivoSIRESP.getValorDaCelulaDate(linha, coluna), "Date"));
						}
				
					}
				}
				linhaArquivoMensal++;
			}
		}
		
		for(NovasSolicitacoes entrada : novasSolicitacoesCDR.values())
			solicitacoesDaCompetencia.add(entrada);
		
		System.out.println(solicitacoesDaCompetencia.size());
		
		for(NovasSolicitacoes entrada : solicitacoesDaCompetencia)
		{
			if(entrada.getMesInclusao().equals(meses.getMeses().get(mesCompetencia - 1).getMesDescricao()) && entrada.getAnoInclusao().equals(String.valueOf(anoCompetencia)))
			{
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getIndice(), entrada.getTipoSolicitacao(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), entrada.getEspecialidadeExame(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_NOMENCLATURA_PADRONIZADA.getIndice(), entrada.getNomenclaturaPadronizada(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_NOMENCLATURA_PADRONIZADA.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_CID.getIndice(), entrada.getCID(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_CID.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_UNIDADES_CAMPINAS.getIndice(), entrada.getUnidadesCampinas(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_UNIDADES_CAMPINAS.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_MES_INCLUSAO.getIndice(), entrada.getMesInclusao(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_MES_INCLUSAO.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ANO_INCLUSAO.getIndice(), entrada.getAnoInclusao(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ANO_INCLUSAO.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_NOVAS_SOLICITACOES.getIndice(), entrada.getQtdeSolicitacoes(), "Int"));
			}
		}
		
		arquivoConsolidado.forcarCalculos();
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoNovasSolicitacoesConsolidado.NOME_PLANILHA_BD_CDR.getDescricao(), celulasArquivoConsolidado, true, false, ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice(), null);
		
		arquivoMensal.forcarCalculos();
		arquivoMensal.gravarDadosEmCelula(nomePlanilhaArquivoMensal, celulasArquivoMensal, true, false, ParametrosArquivoNovasSolicitacoesConsulta.ARQUIVO_FINAL_LINHA_INICIAL.getIndice(), null);
		
		atribuirNovasSolicitacoesCDR(solicitacoesDaCompetencia, entidades, dadosDeOfertas, tipoDeBusca);

//		Arquivo arquivoAApagar = new Arquivo(pastaDownloads, entidade.getArquivoBaixadoXLS());
//		arquivoAApagar.apagar();
//		
//		arquivoAApagar = new Arquivo(pastaDownloads, entidade.getArquivoBaixadoXLSX());
//		arquivoAApagar.apagar();
		
		return "";
	}
	
	private void atribuirNovasSolicitacoesCDR(ArrayList<NovasSolicitacoes> entradasDeNovasSolicitacoes, ArrayList<EntidadeExecutanteR1> entidades, HashMap<String, Integer> entradasPorOfertas, String tipoDeBusca)
	{
		
		for(NovasSolicitacoes entrada : entradasDeNovasSolicitacoes)
		{
			String valorNormalizado;
			
			if(entrada.getTipoSolicitacao().equals(tipoDeBusca))
			{
				if(nomenclaturasPadronizadas.containsKey(entrada.getEspecialidadeExame().toUpperCase()))
				{
					valorNormalizado = nomenclaturasPadronizadas.get(entrada.getEspecialidadeExame().toUpperCase()).getNomenclatura();
					
					if(entradasPorOfertas.containsKey(valorNormalizado))
					{
//						if(valorNormalizado.equals("UROLOGIA - VASECTOMIA"))
//							System.out.println(entradasPorOfertas.get(valorNormalizado) + " - " + entrada.getQtdeSolicitacoes());
						
						int quantidade = entradasPorOfertas.get(valorNormalizado) + entrada.getQtdeSolicitacoes();
						entradasPorOfertas.put(valorNormalizado, quantidade);
					}
					else
					{
						int quantidade = entrada.getQtdeSolicitacoes();
						entradasPorOfertas.put(valorNormalizado, quantidade);
					}
				}
			}
		}
		
	}
	
	private void atribuirNovasSolicitacoesRegulada(ArrayList<NovasSolicitacoesRegulada> entradasDeNovasSolicitacoes, ArrayList<EntidadeExecutanteR1> entidades, HashMap <String, Integer> entradasPorOfertas)
	{
		
		for(NovasSolicitacoesRegulada entrada : entradasDeNovasSolicitacoes)
		{
			String valorNormalizado;
			
			if(nomenclaturasPadronizadas.containsKey(entrada.getEspecialidadeExame().toUpperCase()))
			{
				valorNormalizado = nomenclaturasPadronizadas.get(entrada.getEspecialidadeExame().toUpperCase()).getNomenclatura();
				
				if(entradasPorOfertas.containsKey(valorNormalizado))
				{
					int quantidade = 1 + entradasPorOfertas.get(valorNormalizado);
					entradasPorOfertas.put(valorNormalizado, quantidade);
				}
				else
				{
					int quantidade = 1;
					entradasPorOfertas.put(valorNormalizado, quantidade);
				}
			}
		}
	}
	
	private void preencherConsolidacaoDeNovasSolicitacoes(String nomeArquivo, ArrayList<NovasSolicitacoesRegulada> entradasDeNovasSolicitacoes)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(nomeArquivo, 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoNovasSolicitacoesConsolidado.NOME_PLANILHA_BD_REGULADA.getDescricao(), ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice());
		int proximaLinhaVaziaConsolidadoMunicipal = arquivoConsolidado.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getIndice()) + 1;
	
		//String tipoSolicitacao = arquivoSIRESP.getValorDaCelulaString(linha, ParametrosArquivoNovasSolicitacoesConsulta.INDICE_COLUNA_TIPO_SOLICITACAO.getIndice()).trim();
		
		for(NovasSolicitacoesRegulada solicitacao : entradasDeNovasSolicitacoes)
		{
			String tipoSolicitacao = solicitacao.getTipoDeOferta();
			String especialidade = solicitacao.getEspecialidadeExame();
			String cid = solicitacao.getHipotese();
			String unidade = solicitacao.getUnidadeSolicitante();
			
			//solicitacao.setSolicitadoEm(converterData(solicitacao.getSolicitadoEm()));
			//System.out.println(solicitacao.getSolicitadoEm());
			//LocalDateTime dataInclusao = LocalDateTime.parse(solicitacao.getSolicitadoEm().substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			//String mes = meses.getMeses().get(dataInclusao.getMonthValue() - 1).getMesDescricao();
			//String ano = String.valueOf(dataInclusao.getYear());
			String mes = meses.getMeses().get(mesCompetencia - 1).getMesDescricao();
			String ano = String.valueOf(anoCompetencia);
			
			String valorConcatenadoConsolidado = tipoSolicitacao + especialidade.toUpperCase() + cid + unidade + mes + ano;
	
			//System.out.println(valorConcatenadoConsolidado);
			
			if(novasSolicitacoesRegulada.containsKey(valorConcatenadoConsolidado))
			{
				NovasSolicitacoes entradaNovaSolicitacao = novasSolicitacoesRegulada.get(valorConcatenadoConsolidado);
				entradaNovaSolicitacao.setQtdeSolicitacoes(entradaNovaSolicitacao.getQtdeSolicitacoes() + 1);
			}
			else
			{
				NovasSolicitacoes entradaNovaSolicitacao = new NovasSolicitacoes();
				entradaNovaSolicitacao.setTipoSolicitacao(tipoSolicitacao);
				entradaNovaSolicitacao.setEspecialidadeExame(especialidade);
				entradaNovaSolicitacao.setCID(cid);
				entradaNovaSolicitacao.setUnidadesCampinas(unidade);
				entradaNovaSolicitacao.setMesInclusao(mes);
				entradaNovaSolicitacao.setAnoInclusao(ano);
				entradaNovaSolicitacao.setQtdeSolicitacoes(1);
				entradaNovaSolicitacao.setLinhaExcel(proximaLinhaVaziaConsolidadoMunicipal);
				
				if(nomenclaturasPadronizadas.containsKey(especialidade.toUpperCase()))
					entradaNovaSolicitacao.setNomenclaturaPadronizada(nomenclaturasPadronizadas.get(especialidade.toUpperCase()).getNomenclatura());
				else
					entradaNovaSolicitacao.setNomenclaturaPadronizada("Não encontrada");
				
				novasSolicitacoesRegulada.put(valorConcatenadoConsolidado, entradaNovaSolicitacao);
				
				proximaLinhaVaziaConsolidadoMunicipal++;
			}
		}
		
		ArrayList<CelulaExcel> celulasArquivoConsolidado = new ArrayList<CelulaExcel>();
		
		for(NovasSolicitacoes entrada : novasSolicitacoesRegulada.values())
		{
			if(entrada.getMesInclusao().equals(meses.getMeses().get(mesCompetencia - 1).getMesDescricao()) && entrada.getAnoInclusao().equals(String.valueOf(anoCompetencia)))
			{
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getIndice(), entrada.getTipoSolicitacao(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_TIPO_SOLICITACAO.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), entrada.getEspecialidadeExame(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_NOMENCLATURA_PADRONIZADA.getIndice(), entrada.getNomenclaturaPadronizada(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_NOMENCLATURA_PADRONIZADA.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_CID.getIndice(), entrada.getCID(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_CID.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_UNIDADES_CAMPINAS.getIndice(), entrada.getUnidadesCampinas(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_UNIDADES_CAMPINAS.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_MES_INCLUSAO.getIndice(), entrada.getMesInclusao(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_MES_INCLUSAO.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ANO_INCLUSAO.getIndice(), entrada.getAnoInclusao(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_ANO_INCLUSAO.getTipo()));
				celulasArquivoConsolidado.add(new CelulaExcel(entrada.getLinhaExcel(), ParametrosArquivoNovasSolicitacoesConsolidado.INDICE_COLUNA_NOVAS_SOLICITACOES.getIndice(), entrada.getQtdeSolicitacoes(), "Int"));
			}
		}
		
		arquivoConsolidado.forcarCalculos();
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoNovasSolicitacoesConsolidado.NOME_PLANILHA_BD_REGULADA.getDescricao(), celulasArquivoConsolidado, true, false, ParametrosArquivoNovasSolicitacoesConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL.getIndice(), null);
	}
	
	private String preencherConsolidacaoNovasSolicitacoes(ArrayList<EntidadeExecutanteR1> entidades, HashMap<String, Integer> entradasPorOfertas)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice());
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		for(EntidadeExecutanteR1 entidade : entidades)		
		{
			HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
			
			if(mapaEspecialidades != null)
			{
				for(OfertaEDemanda oferta : mapaEspecialidades.values())
				{
					if(entradasPorOfertas.containsKey(oferta.getProcedimento()))
					{
						oferta.setNovasSolicitacoes(String.valueOf(entradasPorOfertas.get(oferta.getProcedimento())));
						celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES_MENSAIS.getIndice(), entradasPorOfertas.get(oferta.getProcedimento()), "Int"));
					}
					else
					{
						celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES_MENSAIS.getIndice(), "-", "String"));
					}
				}
			}
		}
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertas.NOME_PLANILHA_OFERTAS.getDescricao(), celulas, false, false, 0, null);
		
		return "";
	}
	
	private String montarOfertaDemanda(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, boolean preencherProdutividade, boolean preencherOfertasParaDERAC, boolean preencherNomenclaturas, boolean preencherBloqueio, boolean preencherRecepcao) 
	{
		ArrayList<String> tiposDeVinculoComOfertasParaDERAC = new ArrayList<String>();
		tiposDeVinculoComOfertasParaDERAC.add("CONVÊNIO");
		tiposDeVinculoComOfertasParaDERAC.add("CONTRATO");
		tiposDeVinculoComOfertasParaDERAC.add("CONVÊNIO - SUBCONTRATADA");
				
		String[] tiposDeBusca = new String[2];
		
		if(preencherProdutividade)
		{	
			if(entidade.getVinculo().equals(IdentificadoresPaginaWebSIRESP.TEXTO_VINCULO_ESTADUAL.getTextoIdentificador()))
			{
				montarRelatorioDeProdutividadeEstadual(driver, paginaWeb, entidade);
			}
			else
			{
				montarRelatorioDeProdutividadeOutros(driver, paginaWeb, entidade);
			}
		}
		
		
		if(preencherBloqueio)
		{
			String[] buscas = new String[2];
			buscas[0] = "Consulta";
			buscas[1] = "Exame";
			
			preencherInformacoesDeBloqueio(driver, paginaWeb, entidade, buscas);
		}
		
		if(preencherRecepcao && dataInicioCompetencia.isBefore(LocalDate.now()))
		{
			String[] buscas = new String[2];
			buscas[0] = "Consulta";
			buscas[1] = "Exame";
						
			preencherInformacoesDeRecepcao(driver, paginaWeb, entidade, buscas);
		}
		
		if(preencherNomenclaturas)
			preencherInformacoesDeNomenclatura(entidade);
		
		if(preencherOfertasParaDERAC)
		{
			if(tiposDeVinculoComOfertasParaDERAC.contains(entidade.getVinculo()))
			{
				montarInformacoesDeOfertasParaDERACComPlanoDeTrabalho(entidade);
			}
			else
			{
				montarInformacoesDeOfertasParaDERACPorMediaDeOfertas(entidade);
			
			}
		}
		
		return "";
		
	}
	
	private String montarInformacoesDeOfertasParaDERACComPlanoDeTrabalho(EntidadeExecutanteR1 entidade)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice());
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
		
		if(mapaEspecialidades != null)
		{
			for(OfertaEDemanda oferta : mapaEspecialidades.values())
			{
				if(mapaDeOfertasParaDERAC.containsKey(entidade.getNomeOfertasParaDERAC().trim().toUpperCase() + oferta.getProcedimento().trim().toUpperCase()))
				{
					EntradaOfertasParaDERAC ofertasParaDERAC = mapaDeOfertasParaDERAC.get(entidade.getNomeOfertasParaDERAC().trim().toUpperCase() + oferta.getProcedimento().trim().toUpperCase());
					
					if(ofertasParaDERAC.getOfertasParaDERAC().trim().equals(""))
					{
						oferta.setOfertasPrevistas("Não informado");
					}
					else
					{
						oferta.setOfertasPrevistas(ofertasParaDERAC.getOfertasParaDERAC());
					}
					
				}
				else
				{
					oferta.setOfertasPrevistas("-");
				}

				try
				{
					celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTAS_PREVISTAS.getIndice(), Integer.parseInt(oferta.getOfertasPrevistas()), "Int"));
				}
				catch(NumberFormatException e)
				{
					celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTAS_PREVISTAS.getIndice(), oferta.getOfertasPrevistas(), "String"));
				}
			}
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		}
		
		return "";
	}
	
	private String montarInformacoesDeOfertasParaDERACPorMediaDeOfertas(EntidadeExecutanteR1 entidade)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice());
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
		
		if(mapaEspecialidades != null)
		{
			for(OfertaEDemanda oferta : mapaEspecialidades.values())
			{
				int somaDasOfertas = 0;
				int quantidadeDeOfertas = 0;
				
				if(!oferta.getOfertaDisponivel().equals(""))
				{
					somaDasOfertas += Integer.parseInt(oferta.getOfertaDisponivel());
					quantidadeDeOfertas++;
				}
				
				LocalDate mesDeAnalise = dataInicioCompetencia.minusMonths(0).withDayOfMonth(1);
				
				for(int mes = 1; mes <= 11; mes++)
				{
					mesDeAnalise = mesDeAnalise.minusMonths(1).withDayOfMonth(1);
					String competencia = mesDeAnalise.format(formatter);
					
					HashMap<String, OfertaEDemanda> mapaEspecialidadesDeAnalise = ofertasDemandasProcessadas.get(entidade.getExecutante() + competencia);
					
					if(mapaEspecialidadesDeAnalise != null)
					{
						if(mapaEspecialidadesDeAnalise.containsKey(oferta.getTipoDeOferta() + oferta.getEspecialidade()))
						{
							OfertaEDemanda ofertaDeAnalise = mapaEspecialidadesDeAnalise.get(oferta.getTipoDeOferta() + oferta.getEspecialidade());
							if(!ofertaDeAnalise.getOfertaDisponivel().equals(""))
							{
								somaDasOfertas += Integer.parseInt(ofertaDeAnalise.getOfertaDisponivel());
								quantidadeDeOfertas++;
							}
						}
					}
				}
				
				if(quantidadeDeOfertas == 0)
				{
					celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTAS_PREVISTAS.getIndice(), "-", "String"));
				}
				else
				{
					int ofertasParaDERAC = (int)Math.ceil(1.0 * somaDasOfertas / quantidadeDeOfertas);
					celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTAS_PREVISTAS.getIndice(), ofertasParaDERAC, "Int"));					
				}
			}
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		}
		
		return "";
	}
	
	private String montarRelatorioDeProdutividadeEstadual(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade) 
	{
		String[][] tiposDeBusca = new String[2][2];

		tiposDeBusca[0][0] = "Consulta";
		tiposDeBusca[1][0] = "Exame";
		tiposDeBusca[0][1] = Integer.toString(ParametrosTabelaProducaoExecutanteConsultas.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		tiposDeBusca[1][1] = Integer.toString(ParametrosTabelaProducaoExecutanteExames.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		
		for(String[] tipoDeBusca : tiposDeBusca)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_PRODUCAO_EXECUTANTE_TIPO_RELATORIO.getTextoIdentificador(), tipoDeBusca[0]);
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_PRIMEIRO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_DRSVII_CAMPINAS.getTextoIdentificador());
			
			while(!paginaWeb.elementoEstaHabilitadoPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador()))
			{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Loop segundo nível");
			}
			
			System.out.println("Saiu do loop segundo nível");
			
			boolean possuiSMSCampinas = paginaWeb.verificarExistenciaDeTextoEmUmSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			
			if(possuiSMSCampinas) 
			{
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			}
			else
			{
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_REGIAO_METROPOLITANO_CAMPINAS.getTextoIdentificador());
				
				while(!paginaWeb.elementoEstaHabilitadoPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_TERCEIRO_NIVEL.getTextoIdentificador()))
				{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Loop terceiro nível");
				}
				
				System.out.println("Saiu do loop terceiro nível");
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_TERCEIRO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			}
				
			paginaWeb.MarcarElementoCheckBoxPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_EXIBIR_RECEPCAO.getTextoIdentificador());
			paginaWeb.MarcarElementoCheckBoxPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_MOSTRAR_AGENDAMENTO_UNIDADES_ABAIXO.getTextoIdentificador());
			
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_FILTRO_MES.getTextoIdentificador(), meses.getMeses().get(mesCompetencia - 1).getMesDescricaoPrimeiraMaiuscula());
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_FILTRO_ANO.getTextoIdentificador(), Integer.toString(anoCompetencia));
			
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_PRODUCAO_EXECUTANTE_BOTAO_BUSCAR.getTextoIdentificador(), "id");
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_PRODUCAO_EXECUTANTE_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
			{
				ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_PRODUCAO_EXECUTANTE_TABELA_RESULTADOS.getTextoIdentificador());
				System.out.println("Tabela encontrada");
				preencherDadosDeProdutividade(driver, paginaWeb, entidade, tipoDeBusca[0], tabelaResultados, Integer.parseInt(tipoDeBusca[1]));
				
				baixarArquivos(driver, paginaWeb, entidade, tipoDeBusca[0], IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_BOTAO_DOWNLOAD.getTextoIdentificador());
				
				
			}
			else
			{
				
			}
		}
		
		return "";
	}
	
	private String montarRelatorioDeProdutividadeOutros(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade) 
	{
		String[][] tiposDeBusca = new String[2][3];
		
		tiposDeBusca[0][0] = "Consulta";
		tiposDeBusca[1][0] = "Exame";		
		tiposDeBusca[0][1] = Integer.toString(ParametrosTabelaProducaoConsolidadoConsultas.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		tiposDeBusca[1][1] = Integer.toString(ParametrosTabelaProducaoConsolidadoExames.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		tiposDeBusca[0][2] = IdentificadoresPaginaWebSIRESP.TEXTO_CONSOLIDADO_MENSAL_RELATORIO_CONSULTAS.getTextoIdentificador();
		tiposDeBusca[1][2] = IdentificadoresPaginaWebSIRESP.TEXTO_CONSOLIDADO_MENSAL_RELATORIO_EXAMES.getTextoIdentificador();
		
		for(String[] tipoDeBusca : tiposDeBusca)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_CONSOLIDADO_MENSAL_TIPO_RELATORIO.getTextoIdentificador(), tipoDeBusca[2]);
			
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_CONSOLIDADO_MENSAL_FILTRO_MES.getTextoIdentificador(), meses.getMeses().get(mesCompetencia - 1).getMesDescricaoPrimeiraMaiuscula());
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_CONSOLIDADO_MENSAL_FILTRO_ANO.getTextoIdentificador(), Integer.toString(anoCompetencia));
			
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_CONSOLIDADO_MENSAL_BOTAO_BUSCAR.getTextoIdentificador(), "id");
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_CONSOLIDADO_MENSAL_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
			{
				ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_CONSOLIDADO_MENSAL_TABELA_RESULTADOS_CONSULTAS.getTextoIdentificador());
				System.out.println("Tabela encontrada");
				preencherDadosDeProdutividade(driver, paginaWeb, entidade, tipoDeBusca[0], tabelaResultados, Integer.parseInt(tipoDeBusca[1]));
				
				baixarArquivos(driver, paginaWeb, entidade, tipoDeBusca[0], IdentificadoresPaginaWebSIRESP.NAME_CONSOLIDADO_MENSAL_BOTAO_DOWNLOAD.getTextoIdentificador());
			}
			else
			{
				
			}
		}
		
		return "";
	}
	
//	private String preencherInformacoesDeBloqueio(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, String[] tiposDeBusca)
//	{
//		preencherInformacoesDeBloqueioEmConsultas(driver, paginaWeb, entidade, tiposDeBusca[0]);
//		
//		preencherInformacoesDeBloqueioEmExames(driver, paginaWeb, entidade, tiposDeBusca[1]);
//		
//		return "";
//	}
	
	
	private String preencherInformacoesDeBloqueio(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, String[] tiposDeBusca)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice());
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Agendamento");
		opcoes.add("Horários");
		
		HashMap<String, Integer> bloqueiosPorGrupo = new HashMap<String, Integer>();
		
		HashMap<String, Integer> colunaTipoDeOferta = new HashMap<String, Integer>();
		colunaTipoDeOferta.put("Consulta-Coluna Bloqueio", ParametrosTabelaAgendaHorarioConsultas.INDICE_COLUNA_BLOQUEADO.getIndice());
		colunaTipoDeOferta.put("Exame-Coluna Bloqueio", ParametrosTabelaAgendaHorarioExame.INDICE_COLUNA_BLOQUEADO.getIndice());
		colunaTipoDeOferta.put("Consulta-Coluna Especialidade", ParametrosTabelaAgendaHorarioConsultas.INDICE_COLUNA_ESPECIALIDADE.getIndice());
		colunaTipoDeOferta.put("Exame-Coluna Especialidade", ParametrosTabelaAgendaHorarioExame.INDICE_COLUNA_EQUIPAMENTO.getIndice());
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean visivel;
		do
		{
		
			visivel = acessarMenu(driver, paginaWeb, opcoes);
			
		
		}while(!visivel);
		
		//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
		
		if(mapaEspecialidades != null)
		{
			for(OfertaEDemanda oferta : mapaEspecialidades.values())
			{
				oferta.setOfertaBloqueada("0");
				System.out.println(oferta.getLinhaExcel() + ": " + oferta.getEspecialidade());
			}
			
			for(String tipoDeBusca : tiposDeBusca)
			{
		
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_AGENDA_HORARIOS_FILTRO_TIPO_RELATORIO.getTextoIdentificador(), tipoDeBusca);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_AGENDA_HORARIO_FILTRO_ANO.getTextoIdentificador(), Integer.toString(anoCompetencia));
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_AGENDA_HORARIO_FILTRO_MES.getTextoIdentificador(), meses.getMeses().get(mesCompetencia - 1).getMesDescricaoPrimeiraMaiuscula());
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_AGENDA_HORARIO_BOTAO_BUSCAR.getTextoIdentificador(), "name");
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_AGENDA_HORARIO_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
				{
					ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_AGENDA_HORARIO_TABELA_RESULTADOS.getTextoIdentificador());
					System.out.println("Tabela encontrada");
					
					int colunaEspecialidade = colunaTipoDeOferta.get(tipoDeBusca + "-Coluna Especialidade");
					int colunaBloqueio = colunaTipoDeOferta.get(tipoDeBusca + "-Coluna Bloqueio");
				
					
					boolean planilhaRelacoesAtualizada = false;
					for(ArrayList<String> linhaDaTabela : tabelaResultados)
					{
						if(!linhaDaTabela.get(colunaEspecialidade).trim().equals("Especialidade") && !linhaDaTabela.get(colunaEspecialidade).trim().equals("Equipamento") && !linhaDaTabela.get(colunaEspecialidade - 1).trim().equals("Total"))
						{
							System.out.println(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(colunaEspecialidade).trim() + "|");
							
							if(!relacoesOfertaEmBloqueios.containsKey(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(colunaEspecialidade).trim().toUpperCase()))
							{
								System.out.println(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(colunaEspecialidade).trim());
								atualizarPlanilhaDeRelacoes(driver, paginaWeb, tipoDeBusca, entidade, linhaDaTabela.get(colunaEspecialidade), planilhaRelacoesAtualizada);
								
								planilhaRelacoesAtualizada = true;
							}
							
							System.out.println(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(colunaEspecialidade).trim().toUpperCase());
							if(!relacoesOfertaEmBloqueios.containsKey(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(colunaEspecialidade).trim().toUpperCase()))
							{
								AcoesArquivoExcel arquivoConsolidadoBloqueio = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoRelacoesEspecialidadesBloqueio(), 0);
								arquivoConsolidadoBloqueio.abrirPlanilha(ParametrosArquivoOfertasParaBloqueio.NOME_PLANILHA_CONSOLIDADA.getDescricao(), 0);
								int primeiraLinhaVazia = arquivoConsolidadoBloqueio.getUltimaLinhaPreenchida() + 1;
								
								ArrayList<CelulaExcel> celulasDoBloqueio = new ArrayList<CelulaExcel>();
	
								celulasDoBloqueio.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_UNIDADE.getIndice(), entidade.getExecutante(), "String"));
								celulasDoBloqueio.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_TIPO_OFERTA.getIndice(), tipoDeBusca, "String"));
								celulasDoBloqueio.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_GRUPO.getIndice(), "Não encontrada correspondência", "String"));
								celulasDoBloqueio.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_EQUIPAMENTO.getIndice(), linhaDaTabela.get(colunaEspecialidade).trim().toUpperCase(), "String"));
								
								relacoesOfertaEmBloqueios.put(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(colunaEspecialidade).trim().toUpperCase(), "Não encontrada correspondência");
								
								arquivoConsolidadoBloqueio.gravarDadosEmCelula(ParametrosArquivoOfertasParaBloqueio.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulasDoBloqueio, false, false, 0, null);
							}
							
							String grupo = relacoesOfertaEmBloqueios.get(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(colunaEspecialidade).trim().toUpperCase());	
							String textoBloqueadoDaEspecialidade = linhaDaTabela.get(colunaBloqueio);
							int valorBloqueadoDaEspecialidade;
							
							if(textoBloqueadoDaEspecialidade.trim().equals(""))
								valorBloqueadoDaEspecialidade = 0;
							else
							{
								valorBloqueadoDaEspecialidade = Integer.parseInt(textoBloqueadoDaEspecialidade.trim());
							}
												
							int valorBloqueadoDoGrupo = 0;
							if(bloqueiosPorGrupo.containsKey(grupo))
							{
								valorBloqueadoDoGrupo = bloqueiosPorGrupo.get(grupo);
							}
							
							bloqueiosPorGrupo.put(grupo, valorBloqueadoDoGrupo + valorBloqueadoDaEspecialidade);
						}
					}
					
			
					for(OfertaEDemanda oferta : mapaEspecialidades.values())
					{
						if(oferta != null && oferta.getTipoDeOferta().equals(tipoDeBusca))
						{
							System.out.println(oferta.getEspecialidade());
							
							int ofertaTotal;
							if(oferta.getOfertaDisponivel().trim().equals(""))
								ofertaTotal = 0;
							else
								ofertaTotal = Integer.parseInt(oferta.getOfertaDisponivel().trim());
							
							int agendamentoTotal;
							if(oferta.getAgendamentoTotal().trim().equals(""))
								agendamentoTotal = 0;
							else
								agendamentoTotal = Integer.parseInt(oferta.getAgendamentoTotal().trim());

							int bloqueado;
							if(!bloqueiosPorGrupo.containsKey(oferta.getEspecialidade().toUpperCase()))
								bloqueado = 0;
							else
								bloqueado = bloqueiosPorGrupo.get(oferta.getEspecialidade().toUpperCase());
							
							System.out.println(oferta.getUnidade() + " " + oferta.getEspecialidade() + " (" + colunaBloqueio + ") " + ofertaTotal + " " + agendamentoTotal + " " + bloqueado);
							
							int bloqueioCalculado = (int)Math.min(Math.max(0, ofertaTotal - agendamentoTotal), bloqueado);
							
							oferta.setOfertaBloqueada(Integer.toString(bloqueioCalculado));
						}
					}
				}
			}
			
			for(OfertaEDemanda oferta : mapaEspecialidades.values())
			{
				celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_BLOQUEADA.getIndice(), Integer.parseInt(oferta.getOfertaBloqueada()), "Int"));
			}
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		}
		
		return "";
	}
	
	private String preencherInformacoesDeNomenclatura(EntidadeExecutanteR1 entidade)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice());
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
		
		if(mapaEspecialidades != null)
		{
			for(OfertaEDemanda oferta : mapaEspecialidades.values())
			{
				if(nomenclaturasPadronizadas.containsKey(oferta.getEspecialidade().trim().toUpperCase()))
				{
					NomenclaturaPadronizada nomenclatura = nomenclaturasPadronizadas.get(oferta.getEspecialidade().trim().toUpperCase());
					
					oferta.setProcedimento(nomenclatura.getNomenclatura());
					oferta.setClassificacao(nomenclatura.getFluxo());
					
					ArrayList<OfertaEDemanda> listaDeOfertas = demandasProcedimentos.get(oferta.getProcedimento() + inicioCompetenciaFormatado);
					
					if(listaDeOfertas == null)
					{
						listaDeOfertas = new ArrayList<OfertaEDemanda>();
						listaDeOfertas.add(oferta);
						demandasProcedimentos.put(oferta.getProcedimento() + inicioCompetenciaFormatado, listaDeOfertas);
					}
					else
					{
						listaDeOfertas.add(oferta);
					}
					
				}
				else
				{
					oferta.setProcedimento("-");
					oferta.setClassificacao("-");
				}

				celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_PROCEDIMENTOS.getIndice(), oferta.getProcedimento(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_PROCEDIMENTOS.getTipo()));
				celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CLASSIFICACAO.getIndice(), oferta.getClassificacao(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CLASSIFICACAO.getTipo()));
			}
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		}
		
		return "";
	}
	
	private String preencherInformacoesDeRecepcao(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, String[] tiposDeBusca)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice());
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Relatório");
		opcoes.add("Pacientes  >>");
		opcoes.add("PC05 - Paciente sem Recepção");
		
		HashMap<String, Integer> colunaTipoDeOferta = new HashMap<String, Integer>();
		colunaTipoDeOferta.put("Consulta-Coluna Especialidade", ParametrosTabelaPacientesSemRecepcaoConsulta.INDICE_COLUNA_ESPECIALIDADE.getIndice());
		colunaTipoDeOferta.put("Exame-Coluna Especialidade", ParametrosTabelaPacientesSemRecepcaoExame.INDICE_COLUNA_GRUPO_DE_COTA.getIndice());
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean visivel;
		do
		{
		
			visivel = acessarMenu(driver, paginaWeb, opcoes);
			
		
		}while(!visivel);
		
		//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocalDate dataFinal;
		LocalDate dataInicial;
		LocalDate hoje = LocalDate.now();
		
		if(hoje.isBefore(dataInicioCompetencia))
			dataInicial = hoje;
		else
			dataInicial = dataInicioCompetencia;
			
		if(hoje.isBefore(dataFinalCompetencia))
			dataFinal = hoje;
		else
			dataFinal = dataFinalCompetencia;
		
		String dataInicioFormatada;
		String dataFimFormatada;
		
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		dataInicioFormatada = dataInicial.format(formatoData);
		dataFimFormatada = dataFinal.format(formatoData);
		
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicial.format(formatter);
		
		HashMap<String, OfertaEDemanda> mapaEspecialidades = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
		
		if(mapaEspecialidades != null)
		{
			for(OfertaEDemanda oferta : mapaEspecialidades.values())
			{
				oferta.setRecepcaoFechada("Sim");
				System.out.println(oferta.getLinhaExcel() + ": " + oferta.getEspecialidade());
			}
		
			for(String tipoDeBusca : tiposDeBusca)
			{
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_PACIENTES_SEM_RECEPCAO_FILTRO_TIPO_RELATORIO.getTextoIdentificador(), tipoDeBusca);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				paginaWeb.preencherInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_PACIENTES_SEM_RECEPCAO_FILTRO_DATA_INICIAL.getTextoIdentificador(), dataInicioFormatada.replace("-", ""));
				paginaWeb.preencherInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_PACIENTES_SEM_RECEPCAO_FILTRO_DATA_FINAL.getTextoIdentificador(), dataFimFormatada.replace("-", ""));
				
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_PACIENTES_SEM_RECEPCAO_BOTAO_BUSCAR.getTextoIdentificador(), "name");
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_PACIENTES_SEM_RECEPCAO_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
				{
					paginaWeb.clicarRadioInputPeloId(driver, IdentificadoresPaginaWebSIRESP.ID_PACIENTES_SEM_RECEPCAO_FITLRO_UNIDADE.getTextoIdentificador());
					while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
					
					ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_PACIENTES_SEM_RECEPCAO_TABELA_RESULTADOS.getTextoIdentificador());
					System.out.println("Tabela encontrada");
					
					int colunaEspecialidade = colunaTipoDeOferta.get(tipoDeBusca + "-Coluna Especialidade");
					
					ArrayList<String> especialidadesSemRecepcao = new ArrayList<String>();
					
					for(ArrayList<String> linhaDaTabela : tabelaResultados)
					{
						if(!especialidadesSemRecepcao.contains(linhaDaTabela.get(colunaEspecialidade).toUpperCase().trim()))
						{
							especialidadesSemRecepcao.add(linhaDaTabela.get(colunaEspecialidade).toUpperCase().trim());
						}
					}
					
					for(String especialidade : especialidadesSemRecepcao)
					{
						OfertaEDemanda oferta = mapaEspecialidades.get(tipoDeBusca + especialidade);
						
						if(oferta != null)
						{
							oferta.setRecepcaoFechada("Não");
						}
					}
				}
			}
			for(OfertaEDemanda oferta : mapaEspecialidades.values())
			{
				celulas.add(new CelulaExcel(oferta.getLinhaExcel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_RECEPCAO_FECHADA.getIndice(), oferta.getRecepcaoFechada(), "String"));
			}
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		}
		
		return "";
	}
	
	private void preencherDadosDeProdutividade(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, String tipoDeBusca, ArrayList<ArrayList<String>> tabelaResultados, int quantidadeEsperadaDeColunas)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), anoCompetencia);
		int ultimaLinhaLivre = arquivoConsolidado.getUltimaLinhaPreenchida() + 1;
		int linhaExcel;
		int ofertaPreExistente = -1;
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		CorrelacaoArquivosOfertaDemanda correlacoes = new CorrelacaoArquivosOfertaDemanda();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		ArrayList<CorrelacaoColunasOfertasDemandas> colunasConsolidado = correlacoes.obterCorrelacaoEntreArquivos(tipoDeBusca, entidade.getVinculo());
		
		for(ArrayList<String> linhaDaTabela : tabelaResultados)
		{
			if(linhaDaTabela.size() >= quantidadeEsperadaDeColunas && !linhaDaTabela.get(0).trim().equals("Total"))
			{
				HashMap<String, OfertaEDemanda> mapaEspecialidade = null;
				ArrayList<String> dadosSequenciais = new ArrayList<String>();
				OfertaEDemanda oferta = null;
				String especialidade = linhaDaTabela.get(colunasConsolidado.get(0).getColunaSIRESP().get(0)).trim();
				
				System.out.println(entidade.getExecutante() + inicioCompetenciaFormatado);
				
				if(ofertasDemandasProcessadas.containsKey(entidade.getExecutante() + inicioCompetenciaFormatado))
				{
					System.out.println(tipoDeBusca + especialidade.toUpperCase());
					
					mapaEspecialidade = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
					if(mapaEspecialidade.containsKey(tipoDeBusca + especialidade.toUpperCase()))
					{
						oferta = mapaEspecialidade.get(tipoDeBusca + especialidade.toUpperCase());
						ofertaPreExistente = Integer.parseInt(oferta.getOfertaDisponivel());
						linhaExcel = mapaEspecialidade.get(tipoDeBusca + especialidade.toUpperCase()).getLinhaExcel();
					}
					else
					{
						oferta = new OfertaEDemanda();
						//mapaEspecialidade = new HashMap<String, OfertaEDemanda>();
						ofertaPreExistente = -1;
						linhaExcel = ultimaLinhaLivre;
						ultimaLinhaLivre++;
					}
				}
				else
				{
					oferta = new OfertaEDemanda();
					mapaEspecialidade = new HashMap<String, OfertaEDemanda>();
					ofertaPreExistente = -1;
					ofertasDemandasProcessadas.put(entidade.getExecutante() + inicioCompetenciaFormatado, mapaEspecialidade);
					linhaExcel = ultimaLinhaLivre;
					ultimaLinhaLivre++;
				}
				
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_UNIDADE.getIndice(), entidade.getExecutante(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_VINCULO.getIndice(), entidade.getVinculo(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_COMPETENCIA.getIndice(), dataInicioCompetencia, "Date mes/ano"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_TIPO_OFERTA.getIndice(), tipoDeBusca, ""));
				
//				System.out.print(linhaExcel + "(T: " + linhaDaTabela.size() + ") E: (" + quantidadeEsperadaDeColunas + ") - ");
//				for(String celula : linhaDaTabela)
//					System.out.print(celula + "\t");
//				System.out.println();
				
				for(CorrelacaoColunasOfertasDemandas correlacao : colunasConsolidado)
				{
						
					if(correlacao.getColunaSIRESP() == null)
					{
						celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), "-", "String"));
						dadosSequenciais.add("-");
					}
					else
					{
						if(correlacao.getTipo().equals("String")) 
						{
							String valor = "";
							for(int indice : correlacao.getColunaSIRESP())
								valor += linhaDaTabela.get(indice).trim();
							
							celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), valor.toUpperCase(), correlacao.getTipo()));
							dadosSequenciais.add(valor);
						}
						else
						{
							int divisor = 0;
							
							if(correlacao.getColunasDivisao().size() > 0)
							{
								for(int indice : correlacao.getColunasDivisao())
									if(!linhaDaTabela.get(indice).trim().equals(""))
										divisor += Integer.parseInt(linhaDaTabela.get(indice).replace(".", "").trim());
							}
							else
								divisor = 1;
							
							if(divisor == 0)
							{
								celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), "-", "String"));
								dadosSequenciais.add("-");
							}
							else
							{				
								int soma = 0;
								
								for(int indice : correlacao.getColunaSIRESP())
									if(!linhaDaTabela.get(indice).trim().equals(""))
										soma += Integer.parseInt(linhaDaTabela.get(indice).replace(".", "").trim());
								
								int subtraendo = 0;
								
								for(int indice : correlacao.getColunasSubtracao())
									if(!linhaDaTabela.get(indice).trim().equals(""))
										subtraendo += Integer.parseInt(linhaDaTabela.get(indice).replace(".", "").trim());
								
								if(correlacao.getTipo().equals("Int"))
								{
									int resultado = (soma - subtraendo) / divisor;
									celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), resultado, correlacao.getTipo()));
									dadosSequenciais.add(Integer.toString(resultado));
								}
								else
								{
									double resultado = 1.0 * (soma - subtraendo) / divisor;
									celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), resultado, correlacao.getTipo()));
									dadosSequenciais.add(Double.toString(resultado));
								}
							}
						}
					}
				}
				montarObjetoOferta(oferta, entidade, tipoDeBusca, dataInicioCompetencia, linhaExcel, especialidade, dadosSequenciais, ofertaPreExistente);
				
				//gerando os dados das colunas de taxas
				if(oferta.getTaxaAtendido().equals("-"))
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_ATENDIDO.getIndice(), "-", "String"));
				else
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_ATENDIDO.getIndice(), Double.parseDouble(oferta.getTaxaAtendido()), "Porcentagem"));
				
				if(oferta.getTaxaAusente().equals("-"))
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_AUSENTE.getIndice(), "-", "String"));
				else
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_AUSENTE.getIndice(), Double.parseDouble(oferta.getTaxaAusente()), "Porcentagem"));
				
				if(oferta.getTaxaDesistencia().equals("-"))
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DESISTENCIA.getIndice(), "-", "String"));
				else
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DESISTENCIA.getIndice(), Double.parseDouble(oferta.getTaxaDesistencia()), "Porcentagem"));
				
				if(oferta.getTaxaDispensado().equals("-"))
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DISPENSADO.getIndice(), "-", "String"));
				else
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DISPENSADO.getIndice(), Double.parseDouble(oferta.getTaxaDispensado()), "Porcentagem"));
				
				if(oferta.getTaxaNaoInformado().equals("-"))
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_NAO_INFORMADO.getIndice(), "-", "String"));
				else
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_NAO_INFORMADO.getIndice(), Double.parseDouble(oferta.getTaxaNaoInformado()), "Porcentagem"));
				
				if(oferta.getDiferencaDeOferta().equals("-"))
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DIFERENCA_DE_OFERTA.getIndice(), "-", "String"));
				else
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DIFERENCA_DE_OFERTA.getIndice(), Double.parseDouble(oferta.getDiferencaDeOferta()), "Int"));
				
				if(mapaEspecialidade != null)
				{
					System.out.println("(" + oferta.getLinhaExcel() + ") " + oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase().trim());
					mapaEspecialidade.put(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase().trim(), oferta);
				}
			}
		}
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
	}
	
	private OfertaEDemanda montarObjetoOferta(OfertaEDemanda oferta, EntidadeExecutanteR1 entidade, String tipoOferta, LocalDate dataInicioCompetencia, int linhaExcel, String especialidade, ArrayList<String> dadosSequenciais, int ofertaPreExistente)
	{
		oferta.setUnidade(entidade.getExecutante());
		oferta.setVinculo(entidade.getVinculo());
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		oferta.setCompetencia(dataInicioCompetencia.format(formatter));
		
		oferta.setTipoDeOferta(tipoOferta);
		oferta.setEspecialidade(especialidade.toUpperCase());
		
		oferta.setOfertaDisponivel(dadosSequenciais.get(1));
		oferta.setAgendamentoTotal(dadosSequenciais.get(2));
		oferta.setAgendamentoCota(dadosSequenciais.get(3));
		oferta.setAgendamentoBolsao(dadosSequenciais.get(4));
		oferta.setAgendamentoNaoDistribuido(dadosSequenciais.get(5));
		oferta.setAgendamentoExtra(dadosSequenciais.get(6));
		oferta.setRecepcaoAtendido(dadosSequenciais.get(7));
		oferta.setRecepcaoAusente(dadosSequenciais.get(8));
		oferta.setRecepcaoAusenteCalculado(dadosSequenciais.get(9));
		oferta.setRecepcaoDesistencia(dadosSequenciais.get(10));
		oferta.setRecepcaoDispensado(dadosSequenciais.get(11));
		oferta.setRecepcaoNaoInformado(dadosSequenciais.get(12));
		
		if(oferta.getAgendamentoTotal().trim().equals("0"))
		{
			oferta.setTaxaAtendido("-");
			oferta.setTaxaAusente("-");
			oferta.setTaxaDesistencia("-");
			oferta.setTaxaDispensado("-");
			oferta.setTaxaNaoInformado("-");
		}
		else
		{
			try
			{
				Double valor = 1.0 * Integer.parseInt(oferta.getRecepcaoAtendido()) / Integer.parseInt(oferta.getAgendamentoTotal());
				oferta.setTaxaAtendido(String.valueOf(valor));
				
			}catch(NumberFormatException e)
			{
				oferta.setTaxaAtendido("-");
			}
			
			try
			{
				Double valor = 1.0 * Integer.parseInt(oferta.getRecepcaoAusente()) / Integer.parseInt(oferta.getAgendamentoTotal());
				oferta.setTaxaAusente(String.valueOf(valor));
				
			}catch(NumberFormatException e)
			{
				oferta.setTaxaAusente("-");
			}
			
			try
			{
				Double valor = 1.0 * Integer.parseInt(oferta.getRecepcaoDesistencia()) / Integer.parseInt(oferta.getAgendamentoTotal());
				oferta.setTaxaDesistencia(String.valueOf(valor));
				
			}catch(NumberFormatException e)
			{
				oferta.setTaxaDesistencia("-");
			}
			
			try
			{
				Double valor = 1.0 * Integer.parseInt(oferta.getRecepcaoDispensado()) / Integer.parseInt(oferta.getAgendamentoTotal());
				oferta.setTaxaDispensado(String.valueOf(valor));
				
			}catch(NumberFormatException e)
			{
				oferta.setTaxaDispensado("-");
			}
			
			try
			{
				Double valor = 1.0 * Integer.parseInt(oferta.getRecepcaoNaoInformado()) / Integer.parseInt(oferta.getAgendamentoTotal());
				oferta.setTaxaNaoInformado(String.valueOf(valor));
				
			}catch(NumberFormatException e)
			{
				oferta.setTaxaNaoInformado("-");
			}
		}
		
		if(ofertaPreExistente >= 0)
			oferta.setDiferencaDeOferta(String.valueOf(Integer.parseInt(oferta.getOfertaDisponivel()) - ofertaPreExistente));
		else
			oferta.setDiferencaDeOferta("-");
		
		oferta.setOfertaBloqueada("");
		oferta.setRecepcaoFechada("");
		
		if(oferta.getObservacao() == null)
			oferta.setObservacao("");
		
		oferta.setLinhaExcel(linhaExcel);
		
		return oferta;
	}
	

	private String atualizarPlanilhaDeRelacoes(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String tipoDeBusca, EntidadeExecutanteR1 entidade, String especialidade, boolean planilhaRelacoesAtualizada)
	{
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Manutenção");
		opcoes.add("Equipamento");
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoRelacoesEspecialidadesBloqueio(), 0);
		
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertasParaBloqueio.NOME_PLANILHA_CONSOLIDADA.getDescricao(), 0);
		int primeiraLinhaVazia = arquivoConsolidado.getUltimaLinhaPreenchida() + 1;
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		if(tipoDeBusca.equals("Consulta"))
		{
			relacoesOfertaEmBloqueios.put(entidade.getExecutante() + tipoDeBusca + especialidade.trim().toUpperCase(), especialidade.trim());
			
			celulas.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_UNIDADE.getIndice(), entidade.getExecutante(), "String"));
			celulas.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_TIPO_OFERTA.getIndice(), tipoDeBusca, "String"));
			celulas.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_GRUPO.getIndice(), especialidade.trim().toUpperCase(), "String"));
			celulas.add(new CelulaExcel(primeiraLinhaVazia, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_EQUIPAMENTO.getIndice(), especialidade.trim().toUpperCase(), "String"));
		}
		else if(!planilhaRelacoesAtualizada)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean visivel;
			do
			{
			
				visivel = acessarMenu(driver, paginaWeb, opcoes);
				
			
			}while(!visivel);
			
			//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<ElementoSelecao> itens = paginaWeb.obterItensDeUmSelectPeloNome(driver, IdentificadoresPaginaWebSIRESP.NAME_MANUTENCAO_EQUIPAMENTO_GRUPO_DE_COTA.getTextoIdentificador());
			
			int linha = primeiraLinhaVazia;
			for(ElementoSelecao elemento : itens)
			{
				if(!elemento.getText().equals(IdentificadoresPaginaWebSIRESP.TEXTO_MANUTENCAO_EQUIPAMENTO_VALOR_PADRAO_GRUPO_DE_COTA.getTextoIdentificador()))
				{
					paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_MANUTENCAO_EQUIPAMENTO_GRUPO_DE_COTA.getTextoIdentificador(), elemento.getText());
					paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_MANUTENCAO_EQUIPAMENTO_LISTAGEM_ATIVOS.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_MANUTENCAO_EQUIPAMENTO_LISTAGEM_TODOS.getTextoIdentificador());
					
					paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_MANUTENCAO_EQUIPAMENTO_BOTAO_BUSCAR.getTextoIdentificador(), "name");
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ArrayList<ArrayList<String>> tabelaDeResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_MANUTENCAO_EQUIPAMENTO_TABELA_RESULTADOS.getTextoIdentificador());
					
					if(tabelaDeResultados != null)
					{
						for(ArrayList<String> linhaDaTabela : tabelaDeResultados)
						{
							if(!linhaDaTabela.get(ParametrosTabelaManutencaoEquipamento.INDICE_COLUNA_NOME_ASSOCIACO.getIndice()).trim().equals(IdentificadoresPaginaWebSIRESP.TEXTO_MANUTENCAO_EQUIPAMENTO_TABELA_RESULTADOS_PRIMEIRA_CELULA.getTextoIdentificador()))
							{
								if(!relacoesOfertaEmBloqueios.containsKey(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(ParametrosTabelaManutencaoEquipamento.INDICE_COLUNA_NOME_ASSOCIACO.getIndice()).trim()))
								{
									relacoesOfertaEmBloqueios.put(entidade.getExecutante() + tipoDeBusca + linhaDaTabela.get(ParametrosTabelaManutencaoEquipamento.INDICE_COLUNA_NOME_ASSOCIACO.getIndice()).trim(), elemento.getText().trim());
									
									celulas.add(new CelulaExcel(linha, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_UNIDADE.getIndice(), entidade.getExecutante(), "String"));
									celulas.add(new CelulaExcel(linha, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_TIPO_OFERTA.getIndice(), tipoDeBusca, "String"));
									celulas.add(new CelulaExcel(linha, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_GRUPO.getIndice(), elemento.getText().trim(), "String"));
									celulas.add(new CelulaExcel(linha, ParametrosArquivoOfertasParaBloqueio.INDICE_COLUNA_EQUIPAMENTO.getIndice(), linhaDaTabela.get(ParametrosTabelaManutencaoEquipamento.INDICE_COLUNA_NOME_ASSOCIACO.getIndice()).trim().toUpperCase(), "String"));
									
									linha++;
								}
							}							
						}
					}

				}
			}
		}
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertasParaBloqueio.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, false, false, 0, null);
		
		return "";
	}
	
	public String parametrizarArquivosVazios(WebDriver driver)
	{			

		
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
	
	private ArrayList<OfertaEDemanda> lerOfertasJaProcessadas(String nomeArquivo, String planilha, int linhaCabecalho)
	{		
		ArrayList<OfertaEDemanda> ofertas;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			ofertas = ExcelBinder.readSheet(
                    in,
                    OfertaEDemanda.class,
                    planilha,     // ou null para a primeira
                    linhaCabecalho,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return ofertas;

	}
	
	private ArrayList<Demanda> lerDemandasJaProcessadas(String nomeArquivo, String planilha, int linhaCabecalho)
	{		
		ArrayList<Demanda> ofertas;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			ofertas = ExcelBinder.readSheet(
                    in,
                    Demanda.class,
                    planilha,     // ou null para a primeira
                    linhaCabecalho,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return ofertas;

	}
	
	private ArrayList<NovasSolicitacoes> lerEntradaDeNovaSolicitacoes(String nomeArquivo, String planilha, int linhaCabecalho)
	{		
		ArrayList<NovasSolicitacoes> ofertas;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			ofertas = ExcelBinder.readSheet(
                    in,
                    NovasSolicitacoes.class,
                    planilha,     // ou null para a primeira
                    linhaCabecalho,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return ofertas;

	}
	
	private ArrayList<RelacaoOfertasEmBloqueio> lerRelacaoOfertasParaBloqueio(String nomeArquivo, String planilha, int linhaCabecalho)
	{		
		ArrayList<RelacaoOfertasEmBloqueio> ofertas;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			ofertas = ExcelBinder.readSheet(
                    in,
                    RelacaoOfertasEmBloqueio.class,
                    planilha,     // ou null para a primeira
                    linhaCabecalho,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return ofertas;

	}
	
	private ArrayList<EntradaOfertasParaDERAC> lerMapaDeOfertasParaDERAC(String nomeArquivo, String planilha, int linhaCabecalho)
	{		
		ArrayList<EntradaOfertasParaDERAC> ofertas;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			ofertas = ExcelBinder.readSheet(
                    in,
                    EntradaOfertasParaDERAC.class,
                    planilha,     // ou null para a primeira
                    linhaCabecalho,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return ofertas;

	}
	
	private ArrayList<NomenclaturaPadronizada> lerPadronizacaoDeNomenclaturas(String nomeArquivo, String planilha, int linhaCabecalho)
	{		
		ArrayList<NomenclaturaPadronizada> ofertas;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			ofertas = ExcelBinder.readSheet(
                    in,
                    NomenclaturaPadronizada.class,
                    planilha,     // ou null para a primeira
                    linhaCabecalho,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return ofertas;

	}
	
	private ArrayList<String> lerEntidadesSolicitantes(String nomeArquivo)
	{
		ArrayList<String> entidades = new ArrayList();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
               
                String nomeUnidadeSIRESP = registro.get("Nome SIRESP");
                entidades.add(nomeUnidadeSIRESP);
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	private LocalDate extrairData(String texto, String padraoRegex, String fomatoData)
	{
		
        Pattern pattern = Pattern.compile(padraoRegex);
        Matcher matcher = pattern.matcher(texto);

        LocalDate data;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fomatoData);

        if (matcher.find()) {
            String dataTexto = matcher.group();
            
            try
            {
            	data = LocalDate.parse(dataTexto, formatter);
            }catch(DateTimeParseException e)
            {
            	e.printStackTrace();
            	data = null;
            }
        }
        else
        	data = null;

        return data;
	}
	
	private String copiarRelatorioProducaoParaCDRA()
	{
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaRelatorioOfertaEDemanda();
		Arquivo arquivo = new Arquivo(caminhoArquivo, ParametrosArquivoOfertaDemanda.NOME_ARQUIVO_CONSOLIDADO.getDescricao());
		
		String pastaRelatorioCDRA = pastaBaseCDRA + "\\" + diretoriosCDRA.getPastaRelatorioOfertaDemanda();
		
		arquivo.CopiarArquivo(pastaRelatorioCDRA + "\\" + arquivo.getNomeDoArquivo());
		
		return "";
	}
	
	private String copiarRelatorioProducaoParaCDIDR()
	{
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaRelatorioOfertaEDemanda();
		Arquivo arquivo = new Arquivo(caminhoArquivo, ParametrosArquivoOfertaDemanda.NOME_ARQUIVO_CONSOLIDADO.getDescricao());
		
		String pastaRelatorioCDIDR = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaRelatorioOfertaEDemandaCDIDR();
		
		arquivo.CopiarArquivo(pastaRelatorioCDIDR + "\\" + arquivo.getNomeDoArquivo());
		
		return "";
	}
	
	private String gerarCopiaTemporariaRelatorioProducao()
	{
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaRelatorioOfertaEDemanda();
		Arquivo arquivo = new Arquivo(caminhoArquivo, ParametrosArquivoOfertaDemanda.NOME_ARQUIVO_CONSOLIDADO.getDescricao());
		
		String pastaRelatorio = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaRelatorioOfertaEDemanda();
		
		arquivo.CopiarArquivo(pastaRelatorio + "\\" + ParametrosArquivoOfertaDemanda.NOME_ARQUIVO_CONSOLIDADO_EM_PROCESSAMENTO.getDescricao());
		
		return "";
	}
	
	private String atualizarCopiaOriginalRelatorioProducao()
	{
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaRelatorioOfertaEDemanda();
		Arquivo arquivo = new Arquivo(caminhoArquivo, ParametrosArquivoOfertaDemanda.NOME_ARQUIVO_CONSOLIDADO_EM_PROCESSAMENTO.getDescricao());
		
		String pastaRelatorio = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getPastaRelatorioOfertaEDemanda();
		
		arquivo.CopiarArquivo(pastaRelatorio + "\\" + ParametrosArquivoOfertaDemanda.NOME_ARQUIVO_CONSOLIDADO.getDescricao());
		
		return "";
	}

	private static String converterData(String Data)
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
	
	private static String normalizarDataParaMesAno(String valor) {
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
	
	private static String normalizarDataParaAnoMes(String valor) {
	    if (valor == null || valor.isBlank())
	        return null;
	
	    Locale localeBR = Locale.of("pt", "BR"); // Java 21
	    DateTimeFormatter fmtAnoMes = DateTimeFormatter.ofPattern("yyyy-MM", localeBR);
	
	    // 1️ Caso seja número serial do Excel
	    if (valor.matches("\\d+")) {
	        long serial = Long.parseLong(valor);
	        LocalDate data = LocalDate.of(1899, 12, 30).plusDays(serial); // Ajuste Excel
	        return fmtAnoMes.format(data);
	    }
	
	    // 2️ Caso seja dd/MM/yyyy
	    try {
	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy", localeBR);
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
	
	public String ordenarPlanilhaDeOfertas()
	{
		ArrayList<OfertaEDemanda> ofertasEDemandasJaRegistradas = lerOfertasJaProcessadas(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
		ofertasDemandasProcessadas = new HashMap<String, HashMap<String, OfertaEDemanda>>();
		
		int linhaArquivo = ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(OfertaEDemanda oferta : ofertasEDemandasJaRegistradas)
		{
			String competenciaExtraida = oferta.getCompetencia();
			oferta.setCompetencia(normalizarDataParaMesAno(competenciaExtraida));
			oferta.setCompetenciaOrdenacao(normalizarDataParaAnoMes(competenciaExtraida));
		}
		
		Collections.sort(ofertasEDemandasJaRegistradas, Comparator
		    .comparing(OfertaEDemanda::getCompetenciaOrdenacao).reversed()
		    .thenComparing(OfertaEDemanda::getUnidade)
		    .thenComparing(OfertaEDemanda::getTipoDeOferta)
		    .thenComparing(OfertaEDemanda::getEspecialidade)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(OfertaEDemanda oferta : ofertasEDemandasJaRegistradas)
		{
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_UNIDADE.getIndice(), oferta.getUnidade(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_UNIDADE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_VINCULO.getIndice(), oferta.getVinculo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_VINCULO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_COMPETENCIA.getIndice(), oferta.getCompetencia(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_COMPETENCIA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_TIPO_OFERTA.getIndice(), oferta.getTipoDeOferta(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_TIPO_OFERTA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_PROCEDIMENTOS.getIndice(), oferta.getProcedimento(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_PROCEDIMENTOS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), oferta.getEspecialidade(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CLASSIFICACAO.getIndice(), oferta.getClassificacao(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CLASSIFICACAO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTAS_PREVISTAS.getIndice(), oferta.getOfertasPrevistas(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTAS_PREVISTAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES_MENSAIS.getIndice(), oferta.getNovasSolicitacoes(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES_MENSAIS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getIndice(), oferta.getOfertaDisponivel(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_BLOQUEADA.getIndice(), oferta.getOfertaBloqueada(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_BLOQUEADA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getIndice(), oferta.getAgendamentoTotal(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getIndice(), oferta.getAgendamentoCota(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getIndice(), oferta.getAgendamentoBolsao(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getIndice(), oferta.getAgendamentoNaoDistribuido(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getIndice(), oferta.getAgendamentoExtra(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getIndice(), oferta.getRecepcaoAtendido(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getIndice(), oferta.getRecepcaoAusente(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getIndice(), oferta.getRecepcaoAusenteCalculado(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getIndice(), oferta.getRecepcaoDesistencia(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getIndice(), oferta.getRecepcaoDispensado(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getIndice(), oferta.getRecepcaoNaoInformado(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_ATENDIDO.getIndice(), oferta.getTaxaAtendido(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_ATENDIDO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_AUSENTE.getIndice(), oferta.getTaxaAusente(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_AUSENTE.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DESISTENCIA.getIndice(), oferta.getTaxaDesistencia(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DESISTENCIA.getTipo()));			
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DISPENSADO.getIndice(), oferta.getTaxaDispensado(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_DISPENSADO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_NAO_INFORMADO.getIndice(), oferta.getTaxaNaoInformado(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_NAO_INFORMADO.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA_DO_DIA.getIndice(), oferta.getDemandaReprimida(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA_DO_DIA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getIndice(), oferta.getTempoDeEspera(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_MAIOR_TEMPO_DE_ESPERA_EM_DIAS.getIndice(), oferta.getMaisVelhoNaFila(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_MAIOR_TEMPO_DE_ESPERA_EM_DIAS.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_RECEPCAO_FECHADA.getIndice(), oferta.getRecepcaoFechada(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_RECEPCAO_FECHADA.getTipo()));
			celulas.add(criarCelula(linhaArquivo, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DIFERENCA_DE_OFERTA.getIndice(), oferta.getDiferencaDeOferta(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_DIFERENCA_DE_OFERTA.getTipo()));
			
			linhaArquivo++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaDeDemandas()
	{
		ArrayList<Demanda> demandasJaRegistradas = lerDemandasJaProcessadas(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), ParametrosArquivoOfertaPlanilhaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaPlanilhaDemanda.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
		demandasProcessadas = new HashMap<String, Demanda>();
		
		int linhaArquivoDemanda = ParametrosArquivoOfertaPlanilhaDemanda.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(Demanda demanda : demandasJaRegistradas)
		{
			String competenciaExtraida = demanda.getCompetencia();
			demanda.setCompetencia(normalizarDataParaMesAno(competenciaExtraida));
			demanda.setCompetenciaOrdenacao(normalizarDataParaAnoMes(competenciaExtraida));
		}
		
		Collections.sort(demandasJaRegistradas, Comparator
		    .comparing(Demanda::getCompetenciaOrdenacao).reversed()
		    .thenComparing(Demanda::getProcedimento)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(Demanda demanda : demandasJaRegistradas)
		{
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_PROCEDIMENTOS.getIndice(), demanda.getProcedimento(), ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_PROCEDIMENTOS.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_COMPETENCIA.getIndice(), demanda.getCompetencia(), ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_COMPETENCIA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES.getIndice(), demanda.getNovasSolicitacoes(), ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_NOVAS_SOLICITACOES.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA.getIndice(), demanda.getDemandaReprimida(), ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_DEMANDA_REPRIMIDA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), demanda.getOfertaTotal(), ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getIndice(), demanda.getTempoDeEspera(), ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_MAIS_VELHO_NA_FILA.getIndice(), demanda.getMaisVelhoNaFila(), ParametrosArquivoOfertaPlanilhaDemanda.INDICE_COLUNA_MAIS_VELHO_NA_FILA.getTipo()));
			
			linhaArquivoDemanda++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getNomeArquivoOfertaDemanda(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaPlanilhaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaPlanilhaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaPlanilhaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
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
