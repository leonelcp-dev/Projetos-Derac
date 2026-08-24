package modulosGEFIC;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

import dadosGerais.IdentificadoresPaginaWebGEFIC;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRGEFIC;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoGEFICFilas;
import dadosGerais.ParametrosArquivoGEFICFilasRelatorio;
import dadosGerais.ParametrosArquivoUrgenciaPlanilhaFinalizadoAgrupado;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.ExcelBinder;
import modelosDados.CelulaExcel;
import modelosDados.Demanda;
import modelosDados.FilaGEFIC;
import modelosDados.UrgenciaFinalizadoAgrupado;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;
import utils.Utils;

public class FilasNominaisGEFIC 
{
	private String pastaBase;
	private String pastaBaseDadosGEFIC;
	private String ambiente;
	private MesesFormatados meses;
	private IdentificadoresPastasCompartilhadasCDIDRGEFIC diretoriosCDIDR;
	
	public FilasNominaisGEFIC() 
	{
		meses = new MesesFormatados();
	}	
	
	public FilasNominaisGEFIC(String pastaBase, String ambiente, boolean ehOPM)
	{
		this.pastaBase = pastaBase;
		this.ambiente = ambiente;
		
		meses = new MesesFormatados();
		
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRGEFIC.valueOf(ambiente.toUpperCase());
		
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
			}
			
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
			
			HashMap<String, String> mapaDePastas = new HashMap<String, String>();
			
			Iterable<CSVRecord> registros = format.parse(reader);
			for(CSVRecord registro : registros)						
			{
				mapaDePastas.put(registro.get(0) + registro.get(1), registro.get(2));
			}
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDRGEFIC.REFERENCIA_PASTAS_MONITORAMENTO_GEFIC.getTextoIdentificador()))
				pastaBaseDadosGEFIC = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDRGEFIC.REFERENCIA_PASTAS_MONITORAMENTO_GEFIC.getTextoIdentificador());
			else
			{
				JOptionPane.showMessageDialog(null, "Não foi identificada a localização da pasta Ambulatorial compartilhada");
			}
			
			//gerarFilasNominaisPorStatus("07/2026", "14/08/2026", IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_PROCEDIMENTO_REALIZADO.getTextoIdentificador(), false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, "Erro ao encontrar o arquivos de parâmetros da pasta");
		}
	}
	
	public String gerarFilasNominaisPorStatus(String competencia, String dataArquivo, String status, boolean ehOPM)
	{
		LocalDate inicioCompetencia = LocalDate.parse("01/" + competencia, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate finalCompetencia = inicioCompetencia.with(TemporalAdjusters.lastDayOfMonth());
		
		LocalDate dataDownload = LocalDate.parse(dataArquivo, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		HashMap<String, ArrayList<FilaGEFIC>> filaPorEstabelecimento = new HashMap<String, ArrayList<FilaGEFIC>>();
		
		//consolidados urgência
    	ArrayList<FilaGEFIC> filaGEFIC = new ArrayList<FilaGEFIC>();
    	
    	System.out.println(pastaBaseDadosGEFIC + "\\" + diretoriosCDIDR.getPastaArquivosBaixados());
    	
    	String caminhoArquivo = caminhoArquivoBaixado(LocalDate.parse(dataArquivo, DateTimeFormatter.ofPattern("dd/MM/yyyy")), ehOPM);
    	
    	try (FileInputStream in = new FileInputStream(caminhoArquivo)) {
    		filaGEFIC = ExcelBinder.readSheet(in, FilaGEFIC.class, 0, ParametrosArquivoGEFICFilas.LINHA_INICIAL_ARQUIVO.getIndice() - 1, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
    	
    	for(FilaGEFIC entradaNaFila : filaGEFIC)
    	{
    		LocalDate dataSaidaDaFila = LocalDate.parse(entradaNaFila.getDatadesaida(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    		
    		if(dataSaidaDaFila.isBefore(inicioCompetencia) || dataSaidaDaFila.isAfter(finalCompetencia))
    			filaGEFIC.remove(entradaNaFila);
    		else
    		{
    			try {
    				entradaNaFila.setTempoEsperaNumerico(Integer.parseInt(entradaNaFila.getTempodeEspera()));
    			}catch(Exception e)
	    		{
	    			entradaNaFila.setTempoEsperaNumerico(0);
	    		}
    		}
    	}
    	
    	Collections.sort(filaGEFIC, Comparator
    		    .comparing(FilaGEFIC::getEspecialidade)
    		    .thenComparing(FilaGEFIC::getSubespecialidade)
    		    .thenComparing(FilaGEFIC::getProcedimento)
    		    .thenComparing(FilaGEFIC::getTempoEsperaNumerico).reversed()
    		);	
    	
    	ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
    	int linhaArquivo = ParametrosArquivoGEFICFilasRelatorio.LINHA_INICIAL_ARQUIVO.getIndice();
    	
    	for(FilaGEFIC entradaNaFila : filaGEFIC)
    	{
    		if(filaPorEstabelecimento.containsKey(entradaNaFila.getEstabelecimento()))
    		{
    			ArrayList<FilaGEFIC> filaDoEstabelecimento = filaPorEstabelecimento.get(entradaNaFila.getEstabelecimento());
    			filaDoEstabelecimento.add(entradaNaFila);
    		}
    		else
    		{
    			ArrayList<FilaGEFIC> filaDoEstabelecimento = new ArrayList<FilaGEFIC>();
    			filaDoEstabelecimento.add(entradaNaFila);
    			
    			filaPorEstabelecimento.put(entradaNaFila.getEstabelecimento(), filaDoEstabelecimento);
    		}
    		
    		celulas.addAll(criarLinhaFilaNominal(entradaNaFila, linhaArquivo));
    		
    		linhaArquivo++;
    	}
    	
    	String pastaArquivoVazio = pastaBaseDadosGEFIC + "\\" + diretoriosCDIDR.getPastaAutomatizacao();
    	Arquivo arquivo = new Arquivo(pastaArquivoVazio, diretoriosCDIDR.getArquivoFilaNominalVazio());
    	
    	copiarArquivoVazioParaPastaFilas(arquivo, dataDownload);
    	
    	String nome = "Realizados.xlsx";
    	arquivo.renomear(nome);
    	
    	AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(arquivo.getCaminhoCompleto(), ParametrosArquivoGEFICFilasRelatorio.LINHA_INICIAL_ARQUIVO.getIndice());
    	arquivoExcel.gravarDadosEmCelula(ParametrosArquivoGEFICFilasRelatorio.NOME_PLANILHA.getDescricao(), celulas, true, false, ParametrosArquivoGEFICFilasRelatorio.LINHA_INICIAL_ARQUIVO.getIndice(), null);
    	
    	for(String estabelecimento : filaPorEstabelecimento.keySet())
    	{
    		celulas = new ArrayList<CelulaExcel>();
        	linhaArquivo = ParametrosArquivoGEFICFilasRelatorio.LINHA_INICIAL_ARQUIVO.getIndice();
        	
    		ArrayList<FilaGEFIC> fila = filaPorEstabelecimento.get(estabelecimento);
    		
    		for(FilaGEFIC entradaNaFila : fila)
        	{
        		celulas.addAll(criarLinhaFilaNominal(entradaNaFila, linhaArquivo));
        		
        		linhaArquivo++;
        	}
        	
        	pastaArquivoVazio = pastaBaseDadosGEFIC + "\\" + diretoriosCDIDR.getPastaAutomatizacao();
        	arquivo = new Arquivo(pastaArquivoVazio, diretoriosCDIDR.getArquivoFilaNominalVazio());
        	
        	copiarArquivoVazioParaPastaFilas(arquivo, dataDownload);
        	
        	nome = "Realizados - " + estabelecimento + ".xlsx";
        	arquivo.renomear(nome);
        	
        	arquivoExcel = new AcoesArquivoExcel(arquivo.getCaminhoCompleto(), ParametrosArquivoGEFICFilasRelatorio.LINHA_INICIAL_ARQUIVO.getIndice());
        	arquivoExcel.gravarDadosEmCelula(ParametrosArquivoGEFICFilasRelatorio.NOME_PLANILHA.getDescricao(), celulas, true, false, ParametrosArquivoGEFICFilasRelatorio.LINHA_INICIAL_ARQUIVO.getIndice(), null);
    	}
		
		return "";
	}
	
	private String copiarArquivoVazioParaPastaFilas(Arquivo arquivo, LocalDate data)
	{
		String caminhoDaPasta = pastaBaseDadosGEFIC + "\\" + diretoriosCDIDR.getRelatoriosFechamento();
		caminhoDaPasta = caminhoDaPasta + "\\" + data.getYear();
		Pasta pasta = new Pasta(caminhoDaPasta, true);
		
		caminhoDaPasta = caminhoDaPasta + "\\" + meses.getMeses().get(data.getMonthValue() - 1).getMesNumero() + " - " + Utils.primeiraMaiuscula(meses.getMeses().get(data.getMonthValue() - 1).getMesDescricao()) + " de " + data.getYear();
		pasta = new Pasta(caminhoDaPasta, true);
		
		caminhoDaPasta = caminhoDaPasta + "\\" + data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		pasta = new Pasta(caminhoDaPasta, true);
		
		arquivo.CopiarArquivo(caminhoDaPasta + "\\" + arquivo.getNomeDoArquivo());
		
		return "";
	}
	
	private ArrayList<CelulaExcel> criarLinhaFilaNominal(FilaGEFIC entradaNaFila, int linha)
	{
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_INICIAIS_USUARIO.getIndice(), Utils.somenteIniciais(entradaNaFila.getPaciente()), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_INICIAIS_USUARIO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_PACIENTE.getIndice(), entradaNaFila.getPaciente(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_PACIENTE.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_IDADE.getIndice(), entradaNaFila.getIdade(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_IDADE.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_PRIODIZACAO.getIndice(), entradaNaFila.getPriorizacao(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_PRIODIZACAO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_ESPECIALIDADE.getIndice(), entradaNaFila.getEspecialidade(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_ESPECIALIDADE.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_SUBESPECIALIDADE.getIndice(), entradaNaFila.getSubespecialidade(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_SUBESPECIALIDADE.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_PROCEDIMENTO.getIndice(),entradaNaFila.getProcedimento(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_PROCEDIMENTO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_CID.getIndice(), entradaNaFila.getCID(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_CID.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_INDICACAO.getIndice(), LocalDate.parse(entradaNaFila.getDataindicacao(), DateTimeFormatter.ofPattern(ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_INDICACAO.getFormato())), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_INDICACAO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_INSERCAO.getIndice(), LocalDate.parse(entradaNaFila.getDatadeinsercao(), DateTimeFormatter.ofPattern(ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_INSERCAO.getFormato())), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_INSERCAO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_EXECUCAO.getIndice(), LocalDate.parse(entradaNaFila.getDatadeexecucao(), DateTimeFormatter.ofPattern(ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_EXECUCAO.getFormato())), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_EXECUCAO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_SAIDA.getIndice(), LocalDate.parse(entradaNaFila.getDatadesaida(), DateTimeFormatter.ofPattern(ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_SAIDA.getFormato())), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_DATA_SAIDA.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_ESTABELECIMENTO.getIndice(), entradaNaFila.getEstabelecimento(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_ESTABELECIMENTO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_SITUACAO.getIndice(), entradaNaFila.getSituacao(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_SITUACAO.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_CIDADE.getIndice(), entradaNaFila.getCidade(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_CIDADE.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_TEMPO_ESPERA.getIndice(), Integer.parseInt(entradaNaFila.getTempodeEspera()), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_TEMPO_ESPERA.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_TEMPO_MEDIO_ESPERA.getIndice(), Integer.parseInt(entradaNaFila.getTempomediodeEspera()), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_TEMPO_MEDIO_ESPERA.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_TEMPO_MAXIMO_ESPERA.getIndice(), Integer.parseInt(entradaNaFila.getTempomaximodeEspera()), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_TEMPO_MAXIMO_ESPERA.getTipo()));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_OBSERVACOES.getIndice(), entradaNaFila.getObservacoes(), ParametrosArquivoGEFICFilasRelatorio.INDICE_COLUNA_OBSERVACOES.getTipo()));
		
		return celulas;
	}
	
	private String caminhoArquivoBaixado(LocalDate data, boolean ehOPM)
	{
		String nomeArquivo;
		
		nomeArquivo = data.getYear() + "\\" + meses.getMeses().get(data.getMonthValue() - 1).getMesNumero() + " - " + Utils.primeiraMaiuscula(meses.getMeses().get(data.getMonthValue() - 1).getMesDescricao()) + " de " + data.getYear() + "\\" + data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		
		if(ehOPM)
			nomeArquivo += "\\" + ParametrosArquivoGEFICFilas.NOME_ARQUIVO_FILA_OPM.getDescricao().replace(ParametrosArquivoGEFICFilas.MASCARA_DATA_DOWNLOAD.getDescricao(), data.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).replace(ParametrosArquivoGEFICFilas.MASCARA_STATUS.getDescricao(), IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_PROCEDIMENTO_REALIZADO.getTextoIdentificador());
		else
			nomeArquivo += "\\" + ParametrosArquivoGEFICFilas.NOME_ARQUIVO_FILA_GERAL.getDescricao().replace(ParametrosArquivoGEFICFilas.MASCARA_DATA_DOWNLOAD.getDescricao(), data.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).replace(ParametrosArquivoGEFICFilas.MASCARA_STATUS.getDescricao(), IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_PROCEDIMENTO_REALIZADO.getTextoIdentificador());
		
		String caminhoArquivo = pastaBaseDadosGEFIC + "\\" + diretoriosCDIDR.getPastaArquivosBaixados() + "\\" + nomeArquivo;
		
		return caminhoArquivo;
	}
	
}
