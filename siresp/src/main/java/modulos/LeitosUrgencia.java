package modulos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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

import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

import dadosGerais.IdentificadoresPastasCompartilhadasCDIDR;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRLeitos;
import dadosGerais.IdentificadoresPastasCompartilhadasCDRA;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoCensoPlanilhaCadastro;
import dadosGerais.ParametrosArquivoCensoPlanilhaCadastroExtra;
import dadosGerais.ParametrosArquivoCensoPlanilhaOrient;
import dadosGerais.ParametrosArquivoLeitosPlanilhaConsolidado;
import dadosGerais.ParametrosArquivoLeitosPlanilhaMonitoramento;
import dadosGerais.ParametrosArquivoOfertaDemanda;
import dadosGerais.ParametrosArquivoLeitosPlanilhaMonitoramento;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.ExcelBinder;
import modelosDados.CelulaExcel;
import modelosDados.ConsolidadoLeitos;
import modelosDados.DadosAcumuladosLeitos;
import modelosDados.Demanda;
import modelosDados.EntidadeLeito;
import modelosDados.LeitoCadastrado;
import modelosDados.LeitoExtraCadastrado;
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
		preencherDataDeProcessamento();
		ordenarPlanilhaDeMonitoramentoDeLeitos();
		ordenarPlanilhaConsolidadaDeLeitos();
		
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
			System.out.println(leito.getDataExtracao());
			
			leito.setDataExtracao(normalizarDataParaDiaMesAno(leito.getDataExtracao()));
			
			System.out.println(leito.getUnidade() + leito.getDataExtracao());
			
			if(leitosPorHospitalJaMonitorados.containsKey(leito.getUnidade() + leito.getDataExtracao()))
			{
				HashMap<String, MonitoramentoLeitos> mapaEspecialidade = leitosPorHospitalJaMonitorados.get(leito.getUnidade() + leito.getDataExtracao());
				
				System.out.println(leito.getEspecialidade() + leito.getEnfermaria().toUpperCase());
				
				if(mapaEspecialidade.containsKey(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase()))
				{
					MonitoramentoLeitos leitoEncontrado = mapaEspecialidade.get(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase());
					leitoEncontrado.setLinhaExcel(linhaArquivo);
				}
				else
				{
					leito.setLinhaExcel(linhaArquivo);
					mapaEspecialidade.put(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase(), leito);
				}
			}
			else
			{
				HashMap<String, MonitoramentoLeitos> mapaEspecialidade = new HashMap<String, MonitoramentoLeitos>();
				leito.setLinhaExcel(linhaArquivo);
				mapaEspecialidade.put(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase(), leito);
				
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
				
				System.out.println(leito.getEspecialidade().toUpperCase());
				
				if(mapaEspecialidade.containsKey(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase()))
				{
					ConsolidadoLeitos leitoEncontrado = mapaEspecialidade.get(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase());
					leitoEncontrado.setLinhaExcel(linhaArquivo);
				}
				else
				{
					leito.setLinhaExcel(linhaArquivo);
					mapaEspecialidade.put(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase(), leito);
				}
			}
			else
			{
				HashMap<String, ConsolidadoLeitos> mapaEspecialidade = new HashMap<String, ConsolidadoLeitos>();
				leito.setLinhaExcel(linhaArquivo);
				mapaEspecialidade.put(leito.getEspecialidade().toUpperCase() + leito.getEnfermaria().toUpperCase(), leito);
				
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
		
		HashMap<String, String> deParaEspecialidadeLeitos = new HashMap<String, String>();
		
		//obter tipos de unidades
		try {
			br = new BufferedReader(new FileReader(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoDeParaEspecialidades(), StandardCharsets.ISO_8859_1));
			
			String linha;
			
			//lendo e descartando cabeçalho
			linha = br.readLine();
			
			while ((linha = br.readLine()) != null) {
			    String[] colunas = linha.split(";");
			    
			    deParaEspecialidadeLeitos.put(colunas[0] + colunas[1], colunas[2]);
			}
			
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String composicaoMesAno = meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesNumero() + " " + meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesDescricao() + " " + dataInformada.getYear();
		String textoAEncontrar = "EM " + meses.getMeses().get(dataInformada.getMonthValue() - 1).getMesDescricao() + " " + dataInformada.getYear() + ":";
		
		System.out.println("Arquivo: " + composicaoMesAno + " texto a encontrar" + textoAEncontrar);

		HashMap<String, DadosAcumuladosLeitos> dadosConsolidados = new HashMap<String, DadosAcumuladosLeitos>();
		
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
				HashMap<String, LeitoExtraCadastrado> leitosExtrasCadastrados = obterCadastroDeLeitosExtrasDaUnidade(arquivoCenso);
				obterDadosDaPlanilhaMonitoramentoDosLeitos(arquivoCenso, leitosCadastrados, leitosExtrasCadastrados, dadosConsolidados, deParaEspecialidadeLeitos);
				preencherPlanilhaMonitoramentoDosLeitos(entidade, dataDeAnalise, dadosConsolidados);	
			}
			else
				System.out.println("Não foi encontrada a planilha de cadastro dos leitos da unidade: " + entidade.getNomeSIRESP());
		}
		
		preencherPlanilhaConsolidadaDosLeitos(dataDeAnalise, dadosConsolidados);
		
		return "";
	}
	
	private String preencherPlanilhaMonitoramentoDosLeitos(EntidadeLeito entidade, String dataFormatada, HashMap<String, DadosAcumuladosLeitos> dadosConsolidados)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoLeitosPlanilhaMonitoramento.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		int linhaArquivo = arquivoConsolidado.getUltimaLinhaPreenchida();
		if(linhaArquivo != ParametrosArquivoLeitosPlanilhaMonitoramento.LINHA_INICIAL_ARQUIVO.getIndice())
			linhaArquivo++;
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : dadosConsolidados.keySet().stream().filter(chave -> chave.contains(entidade.getNomeSIRESP())).collect(Collectors.toList())) 
		{
			if(chave.contains(ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao()))
			{
				System.out.println(chave);
				String[] partes = chave.split(ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao());
				String especialidade = partes[1];
				DadosAcumuladosLeitos dadosLeito = dadosConsolidados.get(entidade.getNomeSIRESP() + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao() + especialidade);
				
				System.out.println(especialidade);
				
				partes = especialidade.split(ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao());
				especialidade = partes[0];
				String enfermaria = partes[1];
				
				int linhaExcel;
				if(leitosPorHospitalJaMonitorados.containsKey(entidade.getNomeSIRESP() + dataFormatada))
				{
					HashMap<String, MonitoramentoLeitos> leitos = leitosPorHospitalJaMonitorados.get(entidade.getNomeSIRESP() + dataFormatada);
					
					if(leitos.containsKey(especialidade.toUpperCase() + enfermaria.toUpperCase()))
					{
						linhaExcel = leitos.get(especialidade.toUpperCase() + enfermaria.toUpperCase()).getLinhaExcel();
					}
					else
					{
						linhaExcel = linhaArquivo;
						linhaArquivo++;
					}
					
				}
				else
				{
					linhaExcel = linhaArquivo;
					linhaArquivo++;
				}
				
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_UNIDADE.getIndice(), entidade.getNomeSIRESP(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_DATA.getIndice(), dataInformada, "Date"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ESPECIALIDADE.getIndice(), especialidade.toUpperCase(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ENFERMARIA.getIndice(), enfermaria.toUpperCase(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_DISPONIVEL.getIndice(), dadosLeito.getTotalDisponivel(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_RESERVA_INTERNA.getIndice(), dadosLeito.getReservaInterna(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_USO_NAO_CONVENIADO.getIndice(), dadosLeito.getUsoNaoConveniado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_OCUPADO.getIndice(), dadosLeito.getTotalOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_REGULAR_OCUPADO.getIndice(), dadosLeito.getRegularOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO.getIndice(), dadosLeito.getExtraPactuadoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO.getIndice(), dadosLeito.getExtraNaoPactuadoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_INTERNO_OCUPADO.getIndice(), dadosLeito.getInternoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_NAO_CONVENIADO_OCUPADO.getIndice(), dadosLeito.getNaoConveniadoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_BLOQUEADO.getIndice(), dadosLeito.getTotalBloqueado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ISOLAMENTO_BLOQUEADO.getIndice(), dadosLeito.getBloqueadoIsolamento(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO.getIndice(), dadosLeito.getBloqueadoAguardandoPaciente(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_OUTROS_BLOQUEADO.getIndice(), dadosLeito.getBloqueadoOutros(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_LEITOS_VAGOS.getIndice(), dadosLeito.getLeitosVagos(), "Int"));
				
				if(dadosLeito.getTotalDisponivel() > 0)
				{
					double taxaDeOcupacao = 1.0 * dadosLeito.getTotalOcupado() / dadosLeito.getTotalDisponivel();
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TAXA_DE_OCUPACAO.getIndice(), taxaDeOcupacao, "Porcentagem"));
				}
				else
				{
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TAXA_DE_OCUPACAO.getIndice(), "-", "String"));
				}
				
			}
		}
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLeitosPlanilhaMonitoramento.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoLeitosPlanilhaMonitoramento.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String preencherPlanilhaConsolidadaDosLeitos(String dataFormatada, HashMap<String, DadosAcumuladosLeitos> dadosConsolidados)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoLeitosPlanilhaConsolidado.NOME_PLANILHA_CONSOLIDADA.getDescricao(), 0);
		
		int linhaArquivo = arquivoConsolidado.getUltimaLinhaPreenchida();
		if(linhaArquivo != ParametrosArquivoLeitosPlanilhaConsolidado.LINHA_INICIAL_ARQUIVO.getIndice())
			linhaArquivo++;
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(String chave : dadosConsolidados.keySet().stream().filter(chave -> !chave.contains(ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao())).collect(Collectors.toList())) 
		{
			if(chave.contains(ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao()))
			{
				String[] partes = chave.split(ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao());
				String especialidade = partes[0];
				String enfermaria = partes[1];
				
				DadosAcumuladosLeitos dadosLeito = dadosConsolidados.get(especialidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao() + enfermaria);
				
				System.out.println(especialidade.toUpperCase());
				
				int linhaExcel;
				if(leitosConsolidadosJaMonitorados.containsKey(dataFormatada))
				{
					HashMap<String, ConsolidadoLeitos> leitos = leitosConsolidadosJaMonitorados.get(dataFormatada);
					
					if(leitos.containsKey(especialidade.toUpperCase() + enfermaria.toUpperCase()))
					{
						linhaExcel = leitos.get(especialidade.toUpperCase() + enfermaria.toUpperCase()).getLinhaExcel();
					}
					else
					{
						linhaExcel = linhaArquivo;
						linhaArquivo++;
					}
					
				}
				else
				{
					linhaExcel = linhaArquivo;
					linhaArquivo++;
				}
				
				//parei aqui, escrever arquivo
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_DATA.getIndice(), dataInformada, "Date"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ESPECIALIDADE.getIndice(), especialidade.toUpperCase(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ENFERMARIA.getIndice(), enfermaria.toUpperCase(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_DISPONIVEL.getIndice(), dadosLeito.getTotalDisponivel(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_RESERVA_INTERNA.getIndice(), dadosLeito.getReservaInterna(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_USO_NAO_CONVENIADO.getIndice(), dadosLeito.getUsoNaoConveniado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_OCUPADO.getIndice(), dadosLeito.getTotalOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_REGULAR_OCUPADO.getIndice(), dadosLeito.getRegularOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO.getIndice(), dadosLeito.getExtraPactuadoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO.getIndice(), dadosLeito.getExtraNaoPactuadoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_INTERNO_OCUPADO.getIndice(), dadosLeito.getInternoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_NAO_CONVENIADO_OCUPADO.getIndice(), dadosLeito.getNaoConveniadoOcupado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_BLOQUEADO.getIndice(), dadosLeito.getTotalBloqueado(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ISOLAMENTO_BLOQUEADO.getIndice(), dadosLeito.getBloqueadoIsolamento(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO.getIndice(), dadosLeito.getBloqueadoAguardandoPaciente(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_OUTROS_BLOQUEADO.getIndice(), dadosLeito.getBloqueadoOutros(), "Int"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_LEITOS_VAGOS.getIndice(), dadosLeito.getLeitosVagos(), "Int"));
				
				if(dadosLeito.getTotalDisponivel() > 0)
				{
					double taxaDeOcupacao = 1.0 * dadosLeito.getTotalOcupado() / dadosLeito.getTotalDisponivel();
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TAXA_DE_OCUPACAO.getIndice(), taxaDeOcupacao, "Porcentagem"));
				}
				else
				{
					celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TAXA_DE_OCUPACAO.getIndice(), "-", "String"));
				}
				
			}
		}
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLeitosPlanilhaConsolidado.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoLeitosPlanilhaConsolidado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	private String obterDadosDaPlanilhaMonitoramentoDosLeitos(AcoesArquivoExcel arquivoCenso, HashMap<String, LeitoCadastrado> leitosCadastrados, HashMap<String, LeitoExtraCadastrado> leitosExtrasCadastrados, HashMap<String, DadosAcumuladosLeitos> dadosConsolidados, HashMap<String, String> deParaEspecialidadesLeitos)
	{
		arquivoCenso.abrirPlanilha(ParametrosArquivoCenso.NOME_PLANILHA_CENSO.getDescricao(), 0);
		
		int primeiraLinhaArquivo = ParametrosArquivoCenso.LINHA_INICIAL_ARQUIVO_CENSO.getIndice();
		int ultimaLinhaPreenchidaDoArquivoCenso = arquivoCenso.getUlimtaLinhaPreenchidaEmUmaColuna(ParametrosArquivoCenso.LINHA_INICIAL_ARQUIVO_CENSO.getIndice(), ParametrosArquivoCenso.INDICE_COLUNA_DATA_RELATORIO.getIndice());
		
		for(int linhaArquivo = primeiraLinhaArquivo; linhaArquivo <= ultimaLinhaPreenchidaDoArquivoCenso; linhaArquivo++)
		{
			LocalDate dataProcessamento = arquivoCenso.getValorDaCelulaDate(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_DATA_RELATORIO.getIndice());
			
			if(dataProcessamento.isEqual(dataInformada))
			{
				String unidade = arquivoCenso.getValorDaCelulaString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_UNIDADE.getIndice());
				
				String descricaoEnfermaria = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_DESCRICAO_ENFERMARIA.getIndice(), "").toUpperCase();
				String descricaoLeito = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_DESCRICAO_LEITO.getIndice(), "").toUpperCase();
				String statusLeito = arquivoCenso.getValorDaCelulaString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_STATUS.getIndice());
				
				String tipoDeLeito = arquivoCenso.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_TIPO_DE_LEITO_3.getIndice(), "");
				String pactuado = ParametrosArquivoCensoPlanilhaCadastroExtra.TEXTO_NAO_PACTUADO.getDescricao();
				
				LeitoCadastrado leito;
				
				if(tipoDeLeito.equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()))
				{
					leito = new LeitoCadastrado();
					
					leito.setUnidade(unidade);
					leito.setDescricaoEnfermaria(descricaoEnfermaria);
					leito.setDescricaoLeito(descricaoLeito);
					leito.setAtividade("");
					leito.setEspecialidade(arquivoCenso.getValorDaCelulaString(linhaArquivo, ParametrosArquivoCenso.INDICE_COLUNA_ESPECIALIDADE.getIndice()).toUpperCase().trim());
					leito.setStatus(statusLeito);
					leito.setContabilizaNaTaxaDeOcupacao(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO_USO_INTERNO.getDescricao());
					
					if(leitosExtrasCadastrados.containsKey(descricaoEnfermaria + descricaoLeito))
					{
						LeitoExtraCadastrado leitoExtra = leitosExtrasCadastrados.get(descricaoEnfermaria.toUpperCase() + descricaoLeito.toUpperCase());
						leito.setEnfermaria(leitoExtra.getEspecialidade());
						
						if(!leitoExtra.getPactuado().trim().equals(""))
							pactuado = leitoExtra.getPactuado().trim();
					}
					else
					{
						leito.setEnfermaria(leito.getEspecialidade());
					}
				}
				else
				{
					leito = leitosCadastrados.get(descricaoEnfermaria.toUpperCase() + descricaoLeito.toUpperCase());
				}
				
				if(leito != null && (statusLeito.equals(ParametrosArquivoCenso.TEXTO_LEITO_ATIVO.getDescricao()) || statusLeito.equals(ParametrosArquivoCenso.TEXTO_LEITO_BLOQUEADO.getDescricao())))
				{
					if(deParaEspecialidadesLeitos.containsKey(leito.getUnidade() + leito.getEspecialidade()))
						leito.setEspecialidade(deParaEspecialidadesLeitos.get(leito.getUnidade() + leito.getEspecialidade()));
					
					String especialidade = leito.getEspecialidade().toUpperCase().trim();
					String enfermaria = leito.getEnfermaria().toUpperCase().trim();
					
					DadosAcumuladosLeitos consolidadoPorHospitalEspecialidade;
					DadosAcumuladosLeitos consolidadoPorEspecialidade;
					
					if(dadosConsolidados.containsKey(unidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao() + especialidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao() + enfermaria))
					{
						consolidadoPorHospitalEspecialidade = dadosConsolidados.get(unidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao() + especialidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao() + enfermaria);
					}
					else
					{
						consolidadoPorHospitalEspecialidade = new DadosAcumuladosLeitos();
						dadosConsolidados.put(unidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_UNIDADE_ESPECIALIDADE.getDescricao() + especialidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao() + enfermaria, consolidadoPorHospitalEspecialidade);
					}
					
					if(dadosConsolidados.containsKey(especialidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao() + enfermaria))
					{
						consolidadoPorEspecialidade = dadosConsolidados.get(especialidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao() + enfermaria);
					}
					else
					{
						consolidadoPorEspecialidade = new DadosAcumuladosLeitos();
						dadosConsolidados.put(especialidade + ParametrosArquivoLeitosPlanilhaMonitoramento.DIVISOR_ESPECIALIDADE_ENFERMARIA.getDescricao() + enfermaria, consolidadoPorEspecialidade);
					}
					
					//Leitos Disponiveis
					if(leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO_USO_INTERNO.getDescricao()))
					{
						if(!tipoDeLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarReservaInterna();
							consolidadoPorEspecialidade.incrementarReservaInterna();
						}
					}
					else if(leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO_NAO_CONVENIADO.getDescricao()))
					{
						if(!tipoDeLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarUsoNaoConveniado();
							consolidadoPorEspecialidade.incrementarUsoNaoConveniado();
						}
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
						if(pactuado.equals(ParametrosArquivoCensoPlanilhaCadastroExtra.TEXTO_PACTUADO.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarTotalOcupado();
							consolidadoPorEspecialidade.incrementarTotalOcupado();
						}
						
						if(tipoDeLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()))
						{
							if(pactuado.equals(ParametrosArquivoCensoPlanilhaCadastroExtra.TEXTO_NAO_PACTUADO.getDescricao()))
							{
								consolidadoPorHospitalEspecialidade.incrementarExtraNaoPactuadoOcupado();
								consolidadoPorEspecialidade.incrementarExtraNaoPactuadoOcupado();
							}
							else
							{
								consolidadoPorHospitalEspecialidade.incrementarExtraPactuadoOcupado();
								consolidadoPorEspecialidade.incrementarExtraPactuadoOcupado();
							}
						}
						else if(leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO_USO_INTERNO.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarInternoOcupado();
							consolidadoPorEspecialidade.incrementarInternoOcupado();		
						}
						else if(leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO_NAO_CONVENIADO.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarNaoConveniadoOcupado();;
							consolidadoPorEspecialidade.incrementarNaoConveniadoOcupado();		
						}
						else
						{
							consolidadoPorHospitalEspecialidade.incrementarRegularOcupado();
							consolidadoPorEspecialidade.incrementarRegularOcupado();	
						}
					}
					else if(situacaoLeito.equals(ParametrosArquivoCenso.TEXTO_LEITO_VAZIO.getDescricao()))
					{
						if(!tipoDeLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_EXTRA.getDescricao()) && !statusLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_BLOQUEADO.getDescricao()) &&
								!statusLeito.toUpperCase().equals(ParametrosArquivoCenso.TEXTO_LEITO_INATIVO.getDescricao()) && 
								!leito.getContabilizaNaTaxaDeOcupacao().equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO_USO_INTERNO.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarLeitosVagos();
							consolidadoPorEspecialidade.incrementarLeitosVagos();	
						}
					}
					
					//Leitos bloqueados
					if(statusLeito.equals(ParametrosArquivoCenso.TEXTO_LEITO_BLOQUEADO.getDescricao()))
					{
						consolidadoPorHospitalEspecialidade.incrementarTotalBloqueado();
						consolidadoPorEspecialidade.incrementarTotalBloqueado();	
						
						if(motivoDoBloqueio.equals(ParametrosArquivoCenso.TEXTO_MOTIVO_BLOQUEIO_AGUARDANDO_PACIENTE.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarBloqueadoAguardandoPaciente();
							consolidadoPorEspecialidade.incrementarBloqueadoAguardandoPaciente();	
						}
						else if(motivoDoBloqueio.equals(ParametrosArquivoCenso.TEXTO_MOTIVO_BLOQUEIO_ISOLAMENTO.getDescricao()))
						{
							consolidadoPorHospitalEspecialidade.incrementarBloqueadoPorIsolamento();
							consolidadoPorEspecialidade.incrementarBloqueadoPorIsolamento();	
						}
						else
						{
							consolidadoPorHospitalEspecialidade.incrementarBloqueadoOutros();
							consolidadoPorEspecialidade.incrementarBloqueadoOutros();	
						}
					}
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
			String status = arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_ATIVO.getIndice()).trim();
			
			if(status.equals(ParametrosArquivoCensoPlanilhaCadastro.TEXTO_STATUS_LEITO_ATIVO.getDescricao()))
			{
				LeitoCadastrado leito = new LeitoCadastrado();
				leito.setUnidade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_UNIDADE.getIndice()));
				leito.setDescricaoEnfermaria(arquivoCenso.getValorDaCelulaComoString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_DESCRICAO_ENFERMARIA.getIndice(), ""));
				leito.setDescricaoLeito(arquivoCenso.getValorDaCelulaComoString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_DESCRICAO_LEITO.getIndice(), ""));
				leito.setAtividade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_ATIVIDADE.getIndice()));
				leito.setEspecialidade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_ESPECIALIDADE.getIndice()).toUpperCase().trim());
				leito.setStatus(status);
				leito.setContabilizaNaTaxaDeOcupacao(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_CONTABILIZA_NA_TAXA_DE_OCUPACAO.getIndice()));
				leito.setEnfermaria(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastro.INDICE_COLUNA_ENFERMARIA.getIndice()));
				
				leitosCadastrados.put(leito.getDescricaoEnfermaria().toUpperCase() + leito.getDescricaoLeito().toUpperCase(), leito);
			}
		}
		
		return leitosCadastrados;
	}
	
	private HashMap<String, LeitoExtraCadastrado> obterCadastroDeLeitosExtrasDaUnidade(AcoesArquivoExcel arquivoCenso)
	{
		HashMap<String, LeitoExtraCadastrado> leitosCadastrados = new HashMap<String, LeitoExtraCadastrado>();
		
		arquivoCenso.abrirPlanilha(ParametrosArquivoCensoPlanilhaCadastroExtra.NOME_PLANILHA.getDescricao(), 0);
		
		int quantidadeDeLinhas = arquivoCenso.getUltimaLinhaPreenchida();
		
		for(int linhaPlanilha = ParametrosArquivoCensoPlanilhaCadastroExtra.LINHA_INICIAL_ARQUIVO.getIndice(); linhaPlanilha <= quantidadeDeLinhas; linhaPlanilha++)
		{
			LeitoExtraCadastrado leito = new LeitoExtraCadastrado();
			leito.setUnidade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastroExtra.INDICE_COLUNA_UNIDADE.getIndice()));
			leito.setDescricaoEnfermaria(arquivoCenso.getValorDaCelulaComoString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastroExtra.INDICE_COLUNA_DESCRICAO_ENFERMARIA.getIndice(), ""));
			leito.setDescricaoLeito(arquivoCenso.getValorDaCelulaComoString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastroExtra.INDICE_COLUNA_DESCRICAO_LEITO.getIndice(), ""));
			leito.setEspecialidade(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastroExtra.INDICE_COLUNA_ESPECIALIDADE.getIndice()));
			leito.setPactuado(arquivoCenso.getValorDaCelulaString(linhaPlanilha, ParametrosArquivoCensoPlanilhaCadastroExtra.INDICE_COLUNA_PACTUADO.getIndice()).toUpperCase().trim());
			
			leitosCadastrados.put(leito.getDescricaoEnfermaria().toUpperCase() + leito.getDescricaoLeito().toUpperCase(), leito);
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
	
	public String ordenarPlanilhaDeMonitoramentoDeLeitos()
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
		
		int linhaArquivoDemanda = ParametrosArquivoLeitosPlanilhaMonitoramento.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(MonitoramentoLeitos leito : leitosJaMonitorados)
		{
			String dataExtracao = leito.getDataExtracao();
			leito.setDataExtracao(normalizarDataParaDiaMesAno(dataExtracao));
			leito.setDataExtracaoOrdenacao(normalizarDataParaAnoMesDia(leito.getDataExtracao()));
		}
		
		Collections.sort(leitosJaMonitorados, Comparator
		    .comparing(MonitoramentoLeitos::getDataExtracaoOrdenacao).reversed()
		    .thenComparing(MonitoramentoLeitos::getUnidade)
		    .thenComparing(MonitoramentoLeitos::getEspecialidade)
		    .thenComparing(MonitoramentoLeitos::getEnfermaria)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(MonitoramentoLeitos leito : leitosJaMonitorados)
		{
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_UNIDADE.getIndice(), leito.getUnidade(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_UNIDADE.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_DATA.getIndice(), leito.getDataExtracao(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ESPECIALIDADE.getIndice(), leito.getEspecialidade(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ESPECIALIDADE.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ENFERMARIA.getIndice(), leito.getEnfermaria(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ENFERMARIA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_DISPONIVEL.getIndice(), leito.getTotalDisponivel(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_DISPONIVEL.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_RESERVA_INTERNA.getIndice(), leito.getReservaInterna(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_RESERVA_INTERNA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_USO_NAO_CONVENIADO.getIndice(), leito.getUsoNaoConveniado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_USO_NAO_CONVENIADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_OCUPADO.getIndice(), leito.getTotalOcupado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_REGULAR_OCUPADO.getIndice(), leito.getRegularOcupado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_REGULAR_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO.getIndice(), leito.getExtraPactuadoOcupado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO.getIndice(), leito.getExtraNaoPactuadoOcupado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_INTERNO_OCUPADO.getIndice(), leito.getInternoOcupado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_INTERNO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_NAO_CONVENIADO_OCUPADO.getIndice(), leito.getNaoConveniadoOcupado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_NAO_CONVENIADO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_BLOQUEADO.getIndice(), leito.getTotalBloqueado(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TOTAL_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ISOLAMENTO_BLOQUEADO.getIndice(), leito.getBloqueadoIsolamento(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_ISOLAMENTO_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO.getIndice(), leito.getBloqueadoAguardandoPaciente(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_OUTROS_BLOQUEADO.getIndice(), leito.getBloqueadoOutros(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_OUTROS_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_LEITOS_VAGOS.getIndice(), leito.getLeitosVagos(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_LEITOS_VAGOS.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TAXA_DE_OCUPACAO.getIndice(), leito.getTaxaDeOcupacao(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_TAXA_DE_OCUPACAO.getTipo()));
			
			linhaArquivoDemanda++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoLeitosPlanilhaMonitoramento.NOME_PLANILHA_MONITORAMENTO.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLeitosPlanilhaMonitoramento.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, true, false, ParametrosArquivoLeitosPlanilhaMonitoramento.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
		return "";
	}
	
	public String ordenarPlanilhaConsolidadaDeLeitos()
	{
		ArrayList<ConsolidadoLeitos> leitosJaMonitorados;
		
		try (FileInputStream in = new FileInputStream(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia())) {
			leitosJaMonitorados = ExcelBinder.readSheet(in, ConsolidadoLeitos.class, ParametrosArquivoLeitosPlanilhaConsolidado.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoLeitosPlanilhaConsolidado.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		int linhaArquivoDemanda = ParametrosArquivoLeitosPlanilhaConsolidado.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(ConsolidadoLeitos leito : leitosJaMonitorados)
		{
			String dataExtracao = leito.getDataExtracao();
			leito.setDataExtracao(normalizarDataParaDiaMesAno(dataExtracao));
			leito.setDataExtracaoOrdenacao(normalizarDataParaAnoMesDia(leito.getDataExtracao()));
			
			//System.out.println(leito.getDataExtracaoOrdenacao());
		}
		
		Collections.sort(leitosJaMonitorados, Comparator
		    .comparing(ConsolidadoLeitos::getDataExtracaoOrdenacao).reversed()
		    .thenComparing(ConsolidadoLeitos::getEspecialidade)
		    .thenComparing(ConsolidadoLeitos::getEnfermaria)
		);		
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		for(ConsolidadoLeitos leito : leitosJaMonitorados)
		{
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_DATA.getIndice(), leito.getDataExtracao(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_DATA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ESPECIALIDADE.getIndice(), leito.getEspecialidade(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ESPECIALIDADE.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ENFERMARIA.getIndice(), leito.getEnfermaria(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ENFERMARIA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_DISPONIVEL.getIndice(), leito.getTotalDisponivel(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_DISPONIVEL.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_RESERVA_INTERNA.getIndice(), leito.getReservaInterna(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_RESERVA_INTERNA.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_USO_NAO_CONVENIADO.getIndice(), leito.getUsoNaoConveniado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_USO_NAO_CONVENIADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_OCUPADO.getIndice(), leito.getTotalOcupado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_REGULAR_OCUPADO.getIndice(), leito.getRegularOcupado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_REGULAR_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO.getIndice(), leito.getExtraPactuadoOcupado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO.getIndice(), leito.getExtraNaoPactuadoOcupado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_INTERNO_OCUPADO.getIndice(), leito.getInternoOcupado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_INTERNO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_NAO_CONVENIADO_OCUPADO.getIndice(), leito.getNaoConveniadoOcupado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_NAO_CONVENIADO_OCUPADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_BLOQUEADO.getIndice(), leito.getTotalBloqueado(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TOTAL_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ISOLAMENTO_BLOQUEADO.getIndice(), leito.getBloqueadoIsolamento(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_ISOLAMENTO_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO.getIndice(), leito.getBloqueadoAguardandoPaciente(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_OUTROS_BLOQUEADO.getIndice(), leito.getBloqueadoOutros(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_OUTROS_BLOQUEADO.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_LEITOS_VAGOS.getIndice(), leito.getLeitosVagos(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_LEITOS_VAGOS.getTipo()));
			celulas.add(criarCelula(linhaArquivoDemanda, ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TAXA_DE_OCUPACAO.getIndice(), leito.getTaxaDeOcupacao(), ParametrosArquivoLeitosPlanilhaConsolidado.INDICE_COLUNA_TAXA_DE_OCUPACAO.getTipo()));
			
			linhaArquivoDemanda++;
		}
		
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoLeitosPlanilhaConsolidado.NOME_PLANILHA_CONSOLIDADA.getDescricao(), 0);
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLeitosPlanilhaConsolidado.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoLeitosPlanilhaConsolidado.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		
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
	
	private String preencherDataDeProcessamento()
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaBaseMonitoramentoLeitosCDIDR + "\\" + diretoriosCDIDR.getArquivoConsolidadoUrgencia(), 0);
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		LocalDate dataHoje = LocalDate.now();
		
		celulas.add(new CelulaExcel(ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_LINHA_DATA_PROCESSAMENTO.getIndice(), ParametrosArquivoLeitosPlanilhaMonitoramento.INDICE_COLUNA_DATA_PROCESSAMENTO.getIndice(), dataHoje, "Date"));
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLeitosPlanilhaMonitoramento.NOME_PLANILHA_MONITORAMENTO.getDescricao(), celulas, false, false, 0, null);
		arquivoConsolidado.forcarCalculos();
		
		return "";
	}
	
}
