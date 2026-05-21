package modulos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

import dadosGerais.CorrelacaoArquivosDemandaReprimida;
import dadosGerais.IdentificadoresPastasCompartilhadas;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoAgendamentosPendentesRegulada;
import dadosGerais.ParametrosArquivoDemandaReprimidaCDR;
import dadosGerais.ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas;
import dadosGerais.ParametrosArquivoDemandaReprimidaRegulada;
import dadosGerais.ParametrosArquivoFilaCDRConsulta;
import dadosGerais.ParametrosArquivoFilaCDRExame;
import dadosGerais.ParametrosArquivoFilasNominais;
import dadosGerais.ParametrosArquivoFilasNominaisRegulada;
import dadosGerais.ParametrosArquivoReguladaConsolidado;
import dadosGerais.ParametrosArquivoSolicitacoesPendentesRegulada;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import modelosDados.AgendamentosPendentesRegulada;
import modelosDados.CelulaExcel;
import modelosDados.CorrelacaoColunasArquivos;
import modelosDados.EntidadeExecutanteR1;
import modelosDados.NomenclaturaPadronizada;
import modelosDados.NovasSolicitacoesRegulada;
import modelosDados.SolicitacoesPendentesRegulada;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;
import utils.Utils;

public class DemandasReguladas {
	
	private IdentificadoresPastasCompartilhadas diretorios;

	
	public String agruparDadosPorEspecialidadeRegulada(Pasta pasta, String pastaBase, String caminhoArquivoDemandaReprimida, LocalDate dataDaColeta, HashMap<String, String> relacaoUnidadeTipo, HashMap<String, NomenclaturaPadronizada> nomenclaturasPadronizadas, File itemDaPasta, IdentificadoresPastasCompartilhadas diretorios)
	{
		System.out.println(pasta.getCaminhoDaPasta());
		File[] conteudoDaPasta = pasta.listarDiretorio();
		this.diretorios = diretorios;
		
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

		String pastaDestinoArquivosNovasSolicitacoes = pastaBase + "\\" + diretorios.getArquivosNovasSolicitacoesConsolidada();
		if(caminhoArquivoXLSX.contains(ParametrosArquivoFilasNominais.PREFIXO_NOME_ARQUIVO_REGULADA_AGENDAMENTO.getDescricao()))
		{
			
			if(caminhoArquivoXLSX.contains("CONSULTA"))
			{
				extrairConsolidarDadosDeAgendamentosRegulada(caminhoArquivoXLSX, ParametrosArquivoAgendamentosPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_CONSULTA.getDescricao(), "Agendamentos", pastaDestinoArquivosNovasSolicitacoes, "Consulta", pastaBase, caminhoArquivoDemandaReprimida, dataDaColeta, relacaoUnidadeTipo, nomenclaturasPadronizadas);
			}
			if(caminhoArquivoXLSX.contains("EXAME"))
			{
				extrairConsolidarDadosDeAgendamentosRegulada(caminhoArquivoXLSX, ParametrosArquivoAgendamentosPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_EXAME.getDescricao(), "Agendamentos", pastaDestinoArquivosNovasSolicitacoes, "Exame", pastaBase, caminhoArquivoDemandaReprimida, dataDaColeta, relacaoUnidadeTipo, nomenclaturasPadronizadas);
			}
		}
		else if(caminhoArquivoXLSX.contains(ParametrosArquivoFilasNominais.PREFIXO_NOME_ARQUIVO_REGULADA_SOLICITACOES.getDescricao()))
		{
			if(caminhoArquivoXLSX.contains("CONSULTA"))
			{
				extrairConsolidarDadosDeSolicitacoesRegulada(caminhoArquivoXLSX, ParametrosArquivoSolicitacoesPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_CONSULTA.getDescricao(), "Solicitações", pastaDestinoArquivosNovasSolicitacoes, "Consulta", pastaBase, caminhoArquivoDemandaReprimida, dataDaColeta, relacaoUnidadeTipo, nomenclaturasPadronizadas);
			}
			if(caminhoArquivoXLSX.contains("EXAME"))
			{
				extrairConsolidarDadosDeSolicitacoesRegulada(caminhoArquivoXLSX, ParametrosArquivoSolicitacoesPendentesRegulada.NOME_PLANILHA_ARQUIVO_DOWNLOAD_EXAME.getDescricao(), "Solicitações", pastaDestinoArquivosNovasSolicitacoes, "Exame", pastaBase, caminhoArquivoDemandaReprimida, dataDaColeta, relacaoUnidadeTipo, nomenclaturasPadronizadas);
			}
		}
		
		if(arquivoConvertido)
			arquivoConvertidoXLSX.delete();
		
		return "";
	}
	
	private String extrairConsolidarDadosDeAgendamentosRegulada(String arquivo, String nomePlanilha, String TipoArquivoRegulada, String pastaDestinoArquivosNovasSolicitacoes, String tipoDeOferta, String pastaBase, String caminhoArquivoDemandaReprimida, LocalDate dataDaColeta, HashMap<String, String> relacaoUnidadeTipo, HashMap<String, NomenclaturaPadronizada> nomenclaturasPadronizadas)
	{
		MesesFormatados meses = new MesesFormatados();
		
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
			agendamento.setSolicitadoEm(Utils.converterData(agendamento.getSolicitadoEm()));
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
			String pastaArquivosBaixados = pastaBase + "\\" + diretorios.getArquivosReguladasNovasSolicitacoes();
			Pasta pastaDestino = new Pasta(pastaArquivosBaixados, true);
			pastaArquivosBaixados = pastaArquivosBaixados + "\\" + ano;
			pastaDestino = new Pasta(pastaArquivosBaixados, true);
			
			String nomeArquivo = meses.getMeses().get(mes - 1).getMesNumero() + "." + String.valueOf(ano).substring(2) + " - " + meses.getMeses().get(mes - 1).getMesDescricao() + "_Regulada_Consultas e Exames." + ParametrosArquivoReguladaConsolidado.EXTENSAO_ARQUIVO_FORMATADO.getDescricao();
			Arquivo arquivoFinal = new Arquivo(pastaArquivosBaixados, nomeArquivo);
			
			System.out.println("Verificando a existência do arquivo " + pastaArquivosBaixados + "\\" + nomeArquivo);
			
			ArrayList<String> fichasProcessadas = new ArrayList<String>();
			
			if(!arquivoFinal.existe())
			{
				arquivoFinal = new Arquivo(pastaDestinoArquivosNovasSolicitacoes, ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
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
		//DemandaReprimida
		montarDemandaReprimidaReguladaDeAgendamentos(agendamentos, caminhoArquivoDemandaReprimida, dataDaColeta, relacaoUnidadeTipo, nomenclaturasPadronizadas);
		
		return "";
	}
	
	private String extrairConsolidarDadosDeSolicitacoesRegulada(String arquivo, String nomePlanilha, String TipoArquivoRegulada, String pastaDestinoArquivosNovasSolicitacoes, String tipoDeOferta, String pastaBase, String caminhoArquivoDemandaReprimida, LocalDate dataDaColeta, HashMap<String, String> relacaoUnidadeTipo, HashMap<String, NomenclaturaPadronizada> nomenclaturasPadronizadas)
	{
		MesesFormatados meses = new MesesFormatados();
		
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
		
		//Novas Solicitacoes
		for(SolicitacoesPendentesRegulada solicitacao : solicitacoes)
		{
			//System.out.println(solicitacao.getSolicitadoEm());
			solicitacao.setSolicitadoEm(Utils.converterData(solicitacao.getSolicitadoEm()));
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
			String pastaArquivosBaixados = pastaBase + "\\" + diretorios.getArquivosReguladasNovasSolicitacoes();
			Pasta pastaDestino = new Pasta(pastaArquivosBaixados, true);
			pastaArquivosBaixados = pastaArquivosBaixados + "\\" + ano;
			pastaDestino = new Pasta(pastaArquivosBaixados, true);
			
			String nomeArquivo = meses.getMeses().get(mes - 1).getMesNumero() + "." + String.valueOf(ano).substring(2) + " - " + meses.getMeses().get(mes - 1).getMesDescricao() + "_Regulada_Consultas e Exames." + ParametrosArquivoReguladaConsolidado.EXTENSAO_ARQUIVO_FORMATADO.getDescricao();
			Arquivo arquivoFinal = new Arquivo(pastaArquivosBaixados, nomeArquivo);
			
			System.out.println("Verificando a existência do arquivo " + pastaArquivosBaixados + "\\" + nomeArquivo);
			
			ArrayList<String> fichasProcessadas = new ArrayList<String>();
			
			if(!arquivoFinal.existe())
			{
				arquivoFinal = new Arquivo(pastaDestinoArquivosNovasSolicitacoes, ParametrosArquivoReguladaConsolidado.ARQUIVO_MUNICIPAL_VAZIO.getDescricao());
				
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
		
		
		//DemandaReprimida
		montarDemandaReprimidaReguladaDeSolicitacoes(solicitacoes, caminhoArquivoDemandaReprimida, dataDaColeta, relacaoUnidadeTipo, nomenclaturasPadronizadas);
		
		return "";
	}
	
	private String montarDemandaReprimidaReguladaDeAgendamentos(ArrayList<AgendamentosPendentesRegulada> agendamentos, String caminhoArquivoDemandaReprimida, LocalDate dataDaColeta, HashMap<String, String> relacaoUnidadeTipo, HashMap<String, NomenclaturaPadronizada> nomenclaturasPadronizadas)
	{
		AcoesArquivoExcel arquivoDemandaReprimida = new AcoesArquivoExcel(caminhoArquivoDemandaReprimida, 0);

		arquivoDemandaReprimida.abrirPlanilha(ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_REGULADA.getDescricao(), 0);
		int linhaPlanilhaRegulada = arquivoDemandaReprimida.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoDemandaReprimidaRegulada.LINHA_INICIAL_PLANILHA_REGULADA.getIndice() - 1, 0) + 1;
		
		ArrayList<CelulaExcel> celulasRegulada = new ArrayList<CelulaExcel>();
		
		for(AgendamentosPendentesRegulada agendamento : agendamentos)
		{
			LocalDate dataSolicitacao = LocalDate.parse(agendamento.getSolicitadoEm().substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_SOLICITADO_EM.getIndice(), dataSolicitacao, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_SOLICITADO_EM.getTipo()));
			
		    int tempoDeEsperaEmDias = (int)ChronoUnit.DAYS.between(dataSolicitacao, dataDaColeta);
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_TEMPO_DE_ESPERA_EM_DIAS.getIndice(), tempoDeEsperaEmDias, "Int"));

		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_FICHA.getIndice(), agendamento.getFicha(), "String"));
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_CODIGO.getIndice(), agendamento.getCodigoPaciente(), "String"));
		    
		    String nomeAbreviado = Utils.somenteIniciais(agendamento.getPaciente());
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_NOME_ABREVIADO.getIndice(), nomeAbreviado, "String"));

			String unidade = agendamento.getUnidadeSolicitante();
			String tipoUnidade = relacaoUnidadeTipo.get(unidade);
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_UNIDADE_TIPO.getIndice(), tipoUnidade, "String"));
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), agendamento.getUnidadeSolicitante(), "String"));
			
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_NOME_FICHA.getIndice(), agendamento.getNomeFicha(), "String"));

		    
		    String especialidadeExame = agendamento.getEspecialidadeExame();
		    String nomePadronizado = "";
		    
		    if(nomenclaturasPadronizadas.containsKey(especialidadeExame.toUpperCase().trim()))
		    {
		    	nomePadronizado = nomenclaturasPadronizadas.get(especialidadeExame.toUpperCase().trim()).getNomenclatura();
		    }
		    
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_GRUPO_DE_COTAS_E_ESPECIALIDADES_NOMENCLATURA.getIndice(), nomePadronizado, "String"));
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), agendamento.getEspecialidadeExame(), "String"));
		    
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_HIPOTESE.getIndice(), agendamento.getHipotese(), "String"));
			    
		    linhaPlanilhaRegulada++;
			    
		}
		
		arquivoDemandaReprimida.gravarDadosEmCelula(ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_REGULADA.getDescricao(), celulasRegulada, true, false, ParametrosArquivoDemandaReprimidaRegulada.LINHA_INICIAL_PLANILHA_REGULADA.getIndice(), null);
		arquivoDemandaReprimida.forcarCalculos();
		
		return "";
	}
	
	private String montarDemandaReprimidaReguladaDeSolicitacoes(ArrayList<SolicitacoesPendentesRegulada> solicitacoes, String caminhoArquivoDemandaReprimida, LocalDate dataDaColeta, HashMap<String, String> relacaoUnidadeTipo, HashMap<String, NomenclaturaPadronizada> nomenclaturasPadronizadas)
	{
		AcoesArquivoExcel arquivoDemandaReprimida = new AcoesArquivoExcel(caminhoArquivoDemandaReprimida, 0);

		arquivoDemandaReprimida.abrirPlanilha(ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_REGULADA.getDescricao(), 0);
		int linhaPlanilhaRegulada = arquivoDemandaReprimida.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoDemandaReprimidaRegulada.LINHA_INICIAL_PLANILHA_REGULADA.getIndice() - 1, 0) + 1;
		
		ArrayList<CelulaExcel> celulasRegulada = new ArrayList<CelulaExcel>();
		
		for(SolicitacoesPendentesRegulada solicitacao : solicitacoes)
		{
			LocalDate dataSolicitacao = LocalDate.parse(solicitacao.getSolicitadoEm().substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_SOLICITADO_EM.getIndice(), dataSolicitacao, ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_SOLICITADO_EM.getTipo()));
			
		    int tempoDeEsperaEmDias = (int)ChronoUnit.DAYS.between(dataSolicitacao, dataDaColeta);
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_TEMPO_DE_ESPERA_EM_DIAS.getIndice(), tempoDeEsperaEmDias, "Int"));

		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_FICHA.getIndice(), solicitacao.getFicha(), "String"));
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_CODIGO.getIndice(), solicitacao.getCodigoPaciente(), "String"));
		    
		    String nomeAbreviado = Utils.somenteIniciais(solicitacao.getPaciente());
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_NOME_ABREVIADO.getIndice(), nomeAbreviado, "String"));

			String unidade = solicitacao.getUnidadeSolicitante();
			String tipoUnidade = relacaoUnidadeTipo.get(unidade);
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_UNIDADE_TIPO.getIndice(), tipoUnidade, "String"));
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), solicitacao.getUnidadeSolicitante(), "String"));
			
			celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_NOME_FICHA.getIndice(), solicitacao.getNomeFicha(), "String"));

		    
		    String especialidadeExame = solicitacao.getEspecialidadeExame();
		    String nomePadronizado = "";
		    
		    if(nomenclaturasPadronizadas.containsKey(especialidadeExame.toUpperCase().trim()))
		    {
		    	nomePadronizado = nomenclaturasPadronizadas.get(especialidadeExame.toUpperCase().trim()).getNomenclatura();
		    }
		    
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_GRUPO_DE_COTAS_E_ESPECIALIDADES_NOMENCLATURA.getIndice(), nomePadronizado, "String"));
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), solicitacao.getEspecialidadeExame(), "String"));
		    
		    celulasRegulada.add(new CelulaExcel(linhaPlanilhaRegulada, ParametrosArquivoDemandaReprimidaRegulada.INDICE_COLUNA_HIPOTESE.getIndice(), solicitacao.getHipotese(), "String"));
			    
		    linhaPlanilhaRegulada++;
			    
		}
		
		arquivoDemandaReprimida.gravarDadosEmCelula(ParametrosArquivoDemandaReprimidaRegulada.NOME_PLANILHA_REGULADA.getDescricao(), celulasRegulada, true, false, ParametrosArquivoDemandaReprimidaRegulada.LINHA_INICIAL_PLANILHA_REGULADA.getIndice(), null);
		arquivoDemandaReprimida.forcarCalculos();
		
		return "";
	}
		
}
