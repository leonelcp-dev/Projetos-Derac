package modulos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

import dadosGerais.IdentificadoresPastasCompartilhadasCDIDR;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRLeitos;
import dadosGerais.IdentificadoresPastasCompartilhadasCDRA;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoCensoPlanilhaCadastro;
import dadosGerais.ParametrosArquivoCensoPlanilhaOrient;
import dadosGerais.ParametrosArquivoLeitosPlanilhaConsolidado;
import dadosGerais.ParametrosArquivoLeitosPlanilhaMonitoramento;
import dadosGerais.ParametrosArquivoOfertaDemanda;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.ExcelBinder;
import modelosDados.ConsolidadoLeitos;
import modelosDados.DadosAcumuladosLeitos;
import modelosDados.EntidadeLeito;
import modelosDados.LeitoCadastrado;
import modelosDados.MonitoramentoLeitos;
import modelosDados.NomenclaturaPadronizada;
import modelosDados.OfertaEDemanda;

public class LeitosUrgencia 
{
	private IdentificadoresPastasCompartilhadasCDIDRLeitos diretoriosCDIDR;
	private String pastaBase;
	private String dataDeAnalise;
	private String pastaBaseMonitoramentoLeitosCDIDR;
	
	private LocalDate dataInformada;
	private MesesFormatados meses;
	
	private ArrayList<EntidadeLeito> entidades;
	private HashMap<String, HashMap<String, MonitoramentoLeitos>> leitosPorHospitalJaMonitorados;
	private HashMap<String, HashMap<String, ConsolidadoLeitos>> leitosConsolidadosJaMonitorados;

	public String consolidarDadosDeLeitos(String ambiente)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRLeitos.valueOf(ambiente.toUpperCase());
		
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
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDRLeitos.REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR.getTextoIdentificador()))
				pastaBaseMonitoramentoLeitosCDIDR = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDRLeitos.REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Monitoramento de leitos compartilhada");
				return "";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, "Erro ao encontrar o arquivos de parâmetros da pasta");
			return "";
		}
		
		montarRelatoriosDeLeitos();
		
		return "";
	}
	
	private String montarRelatoriosDeLeitos()
	{
		ArrayList<MonitoramentoLeitos> leitosJaMonitorados;
		
		try (FileInputStream in = new FileInputStream(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
			leitosJaMonitorados = ExcelBinder.readSheet(in, MonitoramentoLeitos.class, ParametrosArquivoLeitosPlanilhaMonitoramento.NOME_PLANILHA_MONITORAMENTO.getDescricao(), ParametrosArquivoLeitosPlanilhaMonitoramento.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		leitosPorHospitalJaMonitorados = new HashMap<String, HashMap<String, MonitoramentoLeitos>>();
		
		int linhaArquivo = ParametrosArquivoLeitosPlanilhaMonitoramento.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(MonitoramentoLeitos leito : leitosJaMonitorados)
		{
			leito.setDataExtracao(normalizarDataParaDiaMesAno(leito.getDataExtracao()));
			
			System.out.println(leito.getUnidade() + leito.getDataExtracao());
			
			if(leitosPorHospitalJaMonitorados.containsKey(leito.getUnidade() + leito.getDataExtracao()))
			{
				HashMap<String, MonitoramentoLeitos> mapaEspecialidade = leitosPorHospitalJaMonitorados.get(leito.getUnidade() + leito.getDataExtracao());
				
				System.out.println(leito.getEspecialidade());
				
				if(mapaEspecialidade.containsKey(leito.getEspecialidade()))
				{
					MonitoramentoLeitos leitoEncontrado = mapaEspecialidade.get(leito.getEspecialidade());
					leitoEncontrado.setLinhaExcel(linhaArquivo);
				}
				else
				{
					leito.setLinhaExcel(linhaArquivo);
					mapaEspecialidade.put(leito.getEspecialidade(), leito);
				}
			}
			else
			{
				HashMap<String, MonitoramentoLeitos> mapaEspecialidade = new HashMap<String, MonitoramentoLeitos>();
				leito.setLinhaExcel(linhaArquivo);
				mapaEspecialidade.put(leito.getEspecialidade(), leito);
				
				leitosPorHospitalJaMonitorados.put(leito.getUnidade() + leito.getDataExtracao(), mapaEspecialidade);
			}
			
			linhaArquivo++;
		}
		
		ArrayList<ConsolidadoLeitos> consolidadosJaMonitorados;
		
		try (FileInputStream in = new FileInputStream(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
			consolidadosJaMonitorados = ExcelBinder.readSheet(in, ConsolidadoLeitos.class, ParametrosArquivoLeitosPlanilhaConsolidado.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoLeitosPlanilhaConsolidado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		leitosConsolidadosJaMonitorados = new HashMap<String, HashMap<String, ConsolidadoLeitos>>();
		
		linhaArquivo = ParametrosArquivoLeitosPlanilhaConsolidado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(ConsolidadoLeitos leito : consolidadosJaMonitorados)
		{
			leito.setDataExtracao(normalizarDataParaDiaMesAno(leito.getDataExtracao()));
			
			System.out.println(leito.getDataExtracao());
			
			if(leitosConsolidadosJaMonitorados.containsKey(leito.getDataExtracao()))
			{
				HashMap<String, ConsolidadoLeitos> mapaEspecialidade = leitosConsolidadosJaMonitorados.get(leito.getDataExtracao());
				
				System.out.println(leito.getEspecialidade());
				
				if(mapaEspecialidade.containsKey(leito.getEspecialidade()))
				{
					ConsolidadoLeitos leitoEncontrado = mapaEspecialidade.get(leito.getEspecialidade());
					leitoEncontrado.setLinhaExcel(linhaArquivo);
				}
				else
				{
					leito.setLinhaExcel(linhaArquivo);
					mapaEspecialidade.put(leito.getEspecialidade(), leito);
				}
			}
			else
			{
				HashMap<String, ConsolidadoLeitos> mapaEspecialidade = new HashMap<String, ConsolidadoLeitos>();
				leito.setLinhaExcel(linhaArquivo);
				mapaEspecialidade.put(leito.getEspecialidade(), leito);
				
				leitosConsolidadosJaMonitorados.put(leito.getDataExtracao(), mapaEspecialidade);
			}
			
			linhaArquivo++;
		}
		
		entidades = new ArrayList<EntidadeLeito>();
		BufferedReader br;
		//obter tipos de unidades
		try {
			br = new BufferedReader(new FileReader(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoEntidades(), StandardCharsets.ISO_8859_1));
			
			String linha;
			
			//lendo e descartando cabeçalho
			linha = br.readLine();
			
			while ((linha = br.readLine()) != null) {
			    String[] colunas = linha.split(";");
			    
			    if(colunas[2].equals("SIM"))
			    {
			    	EntidadeLeito entidade = new EntidadeLeito(colunas[0], colunas[1]);
			    	entidades.add(entidade);
			    }
			}
			
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String composicaoMesAno = meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesNumero() + " " + meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesDescricao() + " " + dataInformada.getYear();
		String textoAEncontrar = "EM " + meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesDescricao() + " " + dataInformada.getYear() + ":";
		
		System.out.println("Arquivo: " + composicaoMesAno + " texto a encontrar" + textoAEncontrar);
				
		for(EntidadeLeito entidade : entidades)
		{
			String caminhoRelativoArquivo = pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getPastaArquivosCenso() + "\\CENSOS " + dataInformada.getYear() + "\\" + entidade.getNomePasta() + " " + dataInformada.getYear() + "\\" + composicaoMesAno;
			String nomeArquivoCenso;
			
			if(entidade.getNomePasta().charAt(0) >= '0' && entidade.getNomePasta().charAt(0) <= '9')
			{
				String numero = entidade.getNomePasta().substring(0, entidade.getNomePasta().indexOf(" "));
				nomeArquivoCenso = entidade.getNomePasta().replace(numero, "0") + " censos " + meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesDescricao() + " " + dataInformada.getYear() + ".xlsx";
			}
			else
				nomeArquivoCenso = "0 " + entidade.getNomePasta() + " censos " + meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesDescricao() + " " + dataInformada.getYear() + ".xlsx";
				
			System.out.println(caminhoRelativoArquivo + "\\" + nomeArquivoCenso);
			
			AcoesArquivoExcel arquivoCenso = new AcoesArquivoExcel(caminhoRelativoArquivo + "\\" + nomeArquivoCenso, 0);
			
			String planilhaDeCadastro = obterNomePlanilhaCadastroLeitos(arquivoCenso, textoAEncontrar);
			
			System.out.println("Planilha encontrada: " + planilhaDeCadastro);
			
			if(!planilhaDeCadastro.equals(""))
			{
				HashMap<String, LeitoCadastrado> leitosCadastrados = obterCadastroDeLeitosDaUnidade(arquivoCenso, planilhaDeCadastro);
				
			}
			else
				System.out.println("Não foi encontrada a planilha de cadastro dos leitos da unidade: " + entidade.getNomeSIRESP());
		}
		
		return "";
	}
	
	private String preencherPlanilhaMonitoramentoDosLeitos(AcoesArquivoExcel arquivoCenso, HashMap<String, LeitoCadastrado> leitosCadastrados, HashMap<String, DadosAcumuladosLeitos> dadosConsolidados)
	{
		arquivoCenso.abrirPlanilha(ParametrosArquivoCenso.NOME_PLANILHA_CENSO.getDescricao(), 0);
		
		int primeiraLinhaArquivo = ParametrosArquivoCenso.LINHA_INICIAL_ARQUIVO_CENSO.getIdUnico();
		int ultimaLinhaPreenchidaDoArquivoCenso = arquivoCenso.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoCenso.LINHA_INICIAL_ARQUIVO_CENSO.getIndice(), ParametrosArquivoCenso.INDICE_COLUNA_DATA_RELATORIO.getIndice());
		
		for(int linhaArquivo = primeiraLinhaArquivo; linhaArquivo <= ultimaLinhaPreenchidaDoArquivoCenso; linhaArquivo++)
		{
			LocalDate dataProcessamento = arquivoCenso.getValorDaCelulaDate(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_DATA_RELATORIO.getIndice());
			
			if(dataProcessamento.isEqual(dataInformada))
			{
				String unidade = arquivoCenso.getValorDaCelulaString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_UNIDADE.getIndice());
				
				String descricaoEnfermaria = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_DESCRICAO_ENFERMARIA.getIndice(), "");
				String descricaoLeito = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_DESCRICAO_LEITO.getIndice(), "");
				
				LeitoCadastrado leito = leitosCadastrados.get(descricaoEnfermaria.toUpperCase() + descricaoLeito.toUpperCase());
				if(leito != null)
				{				
					String especialidade = leito.getEspecialidade();
					
					DadosAcumuladosLeitos consolidadoPorHospitalEspecialidade;
					DadosAcumuladosLeitos consolidadoPorEspecialidade;
					
					if(dadosConsolidados.containsKey(unidade + especialidade))
					{
						consolidadoPorHospitalEspecialidade = dadosConsolidados.get(unidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao() + especialidade);
					}
					else
					{
						consolidadoPorHospitalEspecialidade = new DadosAcumuladosLeitos();
						dadosConsolidados.put(unidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao() + especialidade, consolidadoPorHospitalEspecialidade);
					}
					
					if(dadosConsolidados.containsKey(especialidade))
					{
						consolidadoPorEspecialidade = dadosConsolidados.get(especialidade);
					}
					else
					{
						consolidadoPorEspecialidade = new DadosAcumuladosLeitos();
						dadosConsolidados.put(unidade + especialidade, consolidadoPorEspecialidade);
					}
					
					String tipoDeLeito = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_TIPO_DE_LEITO_3.getIndice(), "");
					
					//Leitos Disponiveis
					if(leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO.getDescricao()))
					{
						consolidadoPorHospitalEspecialidade.incrementarReservaInterna();
						consolidadoPorEspecialidade.incrementarReservaInterna();
					}
					else
					{
						if(!tipoDeLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarTotalDisponivel();
							consolidadoPorEspecialidade.incrementarTotalDisponivel();
						}
					}
					
					//LeitosOcupados
					String situacaoLeito = arquivoCenso.getValorDaCelulaString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_SITUACAO.getIndice());
					String motivoDoBloqueio = arquivoCenso.getValorDaCelulaString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_MOTIVO_DO_BLOQUEIO.getIndice());
					
					if(situacaoLeito.equals(ParametrosArquivoCenso.TEXTO_LEITO_OCUPADO.getDescricao()))
					{
						consolidadoPorHospitalEspecialidade.incrementarTotalOcupado();
						consolidadoPorEspecialidade.incrementarTotalOcupado();
						
						if(tipoDeLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarExtraOcupado();
							consolidadoPorEspecialidade.incrementarExtraOcupado();							
						}
						else if(leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarInternoOcupado();
							consolidadoPorEspecialidade.incrementarInternoOcupado();		
						}
						else
						{
							consolidadoPorHospitalEspecialidade.incrementarRegularOcupado();
							consolidadoPorEspecialidade.incrementarRegularOcupado();	
						}
					}
					else if(situacaoLeito.equals(ParametrosArquivoCenso.TEXTO_LEITO_VAZIO.getDescricao()))
					{
						if(!tipoDeLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()) && !motivoDoBloqueio.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_BLOQUEADO.getDescricao()) &&
								!leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarLeitosVagos();
							consolidadoPorEspecialidade.incrementarLeitosVagos();	
						}
					}
					
					//Bloqueados --- parei aqui ----
				}
			}
		}
		
		return "";
	}
	
	private String obterNomePlanilhaCadastroLeitos(AcoesArquivoExcel arquivoCenso, String textoAEncontrar)
	{
		arquivoCenso.abrirPlanilha(ParametrosArquivoCensoPlanilhaOrient.NOME_PLANILHA.getDescricao(), 0);
		
		String nomeDaPlanilha = "";
		
		int linhaArquivo = 0;
		int quantidadeDeLinhas = arquivoCenso.getUltimaLinhaPreenchida();
		
		String valorDaCelula = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_REFERENCIA_DADOS.getIndice(), "dd/MM/yyyy");
		
		while(linhaArquivo <= quantidadeDeLinhas && nomeDaPlanilha.equals(""))
		{
			if(valorDaCelula != null && valorDaCelula.equals(textoAEncontrar))
			{
				System.out.println(valorDaCelula);
				
				linhaArquivo++;
				valorDaCelula = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_REFERENCIA_DADOS.getIndice(), "dd/MM/yyyy");
				String textoColuna = "";
				if(valorDaCelula != null)
					textoColuna = valorDaCelula;
				
				while(linhaArquivo <= quantidadeDeLinhas && !textoColuna.equals(ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_INICIO_VIGENCIA.getDescricao()))
				{
					linhaArquivo++;
					valorDaCelula = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_REFERENCIA_DADOS.getIndice(), "dd/MM/yyyy");
					
					if(valorDaCelula != null)
						textoColuna = valorDaCelula;
				}
				if(linhaArquivo <= quantidadeDeLinhas)
				{
					linhaArquivo++;
					valorDaCelula = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_INICIO_VIGENCIA.getIndice(), "dd/MM/yyyy");
					
					textoColuna = "";
					if(valorDaCelula != null)
						textoColuna = valorDaCelula;
					
					while(linhaArquivo <= quantidadeDeLinhas && nomeDaPlanilha.equals(""))
					{
						try
						{
							String inicioIntervalo = valorDaCelula;
							String finalIntervalo = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_FINAL_VIGENCIA.getIndice(), "dd/MM/yyyy");
							
							LocalDate dataInicio = LocalDate.parse(inicioIntervalo, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
							LocalDate dataFim = LocalDate.parse(finalIntervalo, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
							
							if(dataInformada.isEqual(dataInicio) || dataInformada.isEqual(dataFim) || (dataInformada.isAfter(dataInicio) && dataInformada.isBefore(dataFim)))
							{
								nomeDaPlanilha = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_PLANILHA_RELATORIO.getIndice(), "dd/MM/yyyy");
							}
							
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						linhaArquivo++;
						valorDaCelula = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_INICIO_VIGENCIA.getIndice(), "dd/MM/yyyy");
						
						if(valorDaCelula != null)
							textoColuna = valorDaCelula;
					}
				}
			}
			else
			{
				linhaArquivo++;
				valorDaCelula = arquivoCenso.getValorDaCelulaString(linhaArquivo, ParametrosArquivoCensoPlanilhaOrient.INDICE_COLUNA_REFERENCIA_DADOS.getIndice());
			}
			
		}
		
		return nomeDaPlanilha;
	}
	
	private HashMap<String, LeitoCadastrado> obterCadastroDeLeitosDaUnidade(AcoesArquivoExcel arquivoCenso, String nomeDaPlanilha)
	{
		HashMap<String, LeitoCadastrado> leitosCadastrados = new HashMap<String, LeitoCadastrado>();
		
		arquivoCenso.abrirPlanilha(nomeDaPlanilha, 0);
		
		int quantidadeDeLinhas = arquivoCenso.getUltimaLinhaPreenchida();
		
		for(int linhaPlanilha = ParametrosArquivoCensoPlanilhaCadastro.LINHA_INICIAL_ARQUIVO.getIndice(); linhaPlanilha <= quantidadeDeLinhas; linhaPlanilha++)
		{
			String status = arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_ATIVO.getIndice());
			
			if(status.equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_STATUS_LEITO_ATIVO.getDescricao()))
			{
				LeitoCadastrado leito = new LeitoCadastrado();
				leito.setUnidade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_UNIDADE.getIndice()));
				leito.setDescricaoEnfermaria(arquivoCenso.getValorDaCelulaComoString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_DESCRICAO_ENFERMARIA.getIndice(), ""));
				leito.setDescricaoLeito(arquivoCenso.getValorDaCelulaComoString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_DESCRICAO_LEITO.getIndice(), ""));
				leito.setAtividade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_ATIVIDADE.getIndice()));
				leito.setEspecialidade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_ESPECIALIDADE.getIndice()));
				leito.setStatus(status);
				leito.setContabilizaNaTaxaDeOcupacao(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_CONTABILIZA_NA_TAXA_DE_OCUPACAO.getIndice()));
				
				leitosCadastrados.put(leito.getDescricaoEnfermaria().toUpperCase() + leito.getDescricaoLeito().toUpperCase(), leito);
			}
		}
		
		return leitosCadastrados;
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
	
}
