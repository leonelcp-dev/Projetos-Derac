package modulosGEFIC;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;

import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRGEFIC;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoGEFICNaoConformidades;
import interacao_externa.AcoesArquivoExcel;
import modelosDados.AnaliseNaoConformidades;
import modelosDados.CelulaExcel;
import modelosDados.EntidadeGEFIC;
import utils.Utils;

public class NaoConformidadesGEFIC 
{
	
	private IdentificadoresPastasCompartilhadasCDIDRGEFIC diretoriosCDIDR;
	private String pastaBaseAmbulatorialCDIDR;
	private ArrayList<EntidadeGEFIC> entidades;
	private String ambiente;
	private String pastaBase;
	private MesesFormatados meses;
	
	public NaoConformidadesGEFIC(String pastaBase, String ambiente)
	{
		diretoriosCDIDR = IdentificadoresPastasCompartilhadasCDIDRGEFIC.valueOf(ambiente);
		this.pastaBase = pastaBase;
		entidades = new ArrayList<EntidadeGEFIC>();
		meses = new MesesFormatados();
		
		this.ambiente = ambiente;
	}
	
	private String definirDiretorios(boolean ehOPM, String competenciaInicial)
	{
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
				return "";
			}
			
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
			
			HashMap<String, String> mapaDePastas = new HashMap<String, String>();
			
			Iterable<CSVRecord> registros = format.parse(reader);
			for(CSVRecord registro : registros)						
			{
				mapaDePastas.put(registro.get(0) + registro.get(1), registro.get(2));
			}
			
			if(mapaDePastas.containsKey(ambiente + IdentificadoresPastasCompartilhadasCDIDRGEFIC.REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR.getTextoIdentificador()))
				pastaBaseAmbulatorialCDIDR = pastaBase + "\\" + mapaDePastas.get(ambiente + IdentificadoresPastasCompartilhadasCDIDRGEFIC.REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR.getTextoIdentificador());
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
	
	public String identificarNaoConformidades(boolean ehOPM, String competenciaInicial, boolean sobrescrever) 
	{
		definirDiretorios(ehOPM, competenciaInicial);
		
		String mesTexto = competenciaInicial.split("/")[0];
		int mes = Integer.parseInt(mesTexto);
		String ano = competenciaInicial.split("/")[1];
		
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoMonitoramentoNaoConformidades();
		
		if(ehOPM)
			caminhoArquivo = caminhoArquivo.replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_OPM.getTextoIdentificador() + " " + ano);
		else
			caminhoArquivo = caminhoArquivo.replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_ELETIVAS.getTextoIdentificador() + " " + ano);
		
		AcoesArquivoExcel arquivoMonitoramento = new AcoesArquivoExcel(caminhoArquivo, 0);
		
		String planilha = meses.getMeses().get(mes - 1).getMesDescricao() + " " + ano;
		
		arquivoMonitoramento.abrirPlanilha(planilha, 0);
		
		int linhaArquivo = ParametrosArquivoGEFICNaoConformidades.LINHA_INICIAL_ARQUIVO.getIndice();
		
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		String valorDataFinalizacao = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getIndice(), "dd/MM/yyyy");
		
		while(!valorDataFinalizacao.trim().equals(""))
		{
			String valorNaoConformidade = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_NAO_CONFORMIDADE.getIndice(), "");
			
			String valorDataFinalizacaoFichaSIRESP = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getIndice(), "dd/MM/yyyy");
			String valorDataInsercaoGEFIC = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_INSERCAO_GEFIC.getIndice(), "dd/MM/yyyy");
			String valorDataSaidaGEFIC = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_SAIDA_GEFIC.getIndice(), "dd/MM/yyyy");
			String valorDataExecucaoGEFIC = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_EXECUCAO.getIndice(), "dd/MM/yyyy");
			
			LocalDate dataFinalizacaoFichaSIRESP = Utils.converterStringParaData(valorDataFinalizacaoFichaSIRESP, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getFormato());
			LocalDate dataInsercaoGEFIC = Utils.converterStringParaData(valorDataInsercaoGEFIC, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_INSERCAO_GEFIC.getFormato());
			LocalDate dataSaidaGEFIC = Utils.converterStringParaData(valorDataSaidaGEFIC, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_SAIDA_GEFIC.getFormato());;
			LocalDate dataExecucaoGEFIC = Utils.converterStringParaData(valorDataExecucaoGEFIC, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_EXECUCAO.getFormato());;
			
			if(sobrescrever || (!sobrescrever && valorNaoConformidade.equals("")))
			{
				String naoConformidade = "";
				
				if(dataInsercaoGEFIC == null)
					naoConformidade = ParametrosArquivoGEFICNaoConformidades.TEXTO_NAO_INSERIDO.getDescricao();
				else
				{
					if(dataInsercaoGEFIC.isAfter(dataFinalizacaoFichaSIRESP))
					{
						naoConformidade = ParametrosArquivoGEFICNaoConformidades.TEXTO_INSERCAO_APOS_CIRURGIA.getDescricao();
					}
				}
				
				if(dataSaidaGEFIC == null)
				{
					if(dataInsercaoGEFIC != null)
					{
						if(!naoConformidade.equals(""))
							naoConformidade += ParametrosArquivoGEFICNaoConformidades.TEXTO_SEPARADOR_NAO_CONFORMIDADES.getDescricao();
						
						naoConformidade += ParametrosArquivoGEFICNaoConformidades.TEXTO_SEM_ATUALIZCAO_DE_STATUS.getDescricao();
					}
							
				}
				else
				{
					if(dataExecucaoGEFIC != null && dataSaidaGEFIC.isAfter(dataExecucaoGEFIC.plusDays(ParametrosArquivoGEFICNaoConformidades.TEXTO_SAIDA_APOS_7_DIAS.getIndice())))
					{
						if(!naoConformidade.equals(""))
							naoConformidade += ParametrosArquivoGEFICNaoConformidades.TEXTO_SEPARADOR_NAO_CONFORMIDADES.getDescricao();
						
						naoConformidade += ParametrosArquivoGEFICNaoConformidades.TEXTO_SAIDA_APOS_7_DIAS.getDescricao();
					}
				}
				
				if(dataExecucaoGEFIC != null)
				{
					if(dataFinalizacaoFichaSIRESP.isAfter(dataExecucaoGEFIC))
					{
						if(!naoConformidade.equals(""))
							naoConformidade += ParametrosArquivoGEFICNaoConformidades.TEXTO_SEPARADOR_NAO_CONFORMIDADES.getDescricao();
						
						naoConformidade += ParametrosArquivoGEFICNaoConformidades.TEXTO_FICHA_SIRESP_INSERIDA_APOS_CIRURGIA.getDescricao();
					}
				}
				
				celulas.add(new CelulaExcel(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_NAO_CONFORMIDADE.getIndice(), naoConformidade, "String"));
			}
			
			linhaArquivo++;
			valorDataFinalizacao = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getIndice(), "dd/MM/yyyy");
		}
		
		arquivoMonitoramento.forcarCalculos();
		arquivoMonitoramento.gravarDadosEmCelula(planilha, celulas, true, false, ParametrosArquivoGEFICNaoConformidades.LINHA_INICIAL_ARQUIVO.getIndice(), null);
		arquivoMonitoramento.formatarColuna(planilha, ParametrosArquivoGEFICNaoConformidades.LINHA_INICIAL_ARQUIVO.getIndice(), ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_INSERCAO_GEFIC.getIndice(), "Date");
		arquivoMonitoramento.formatarColuna(planilha, ParametrosArquivoGEFICNaoConformidades.LINHA_INICIAL_ARQUIVO.getIndice(), ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_SAIDA_GEFIC.getIndice(), "Date");
		arquivoMonitoramento.formatarColuna(planilha, ParametrosArquivoGEFICNaoConformidades.LINHA_INICIAL_ARQUIVO.getIndice(), ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_EXECUCAO.getIndice(), "Date");
			
		return "";
	}
	
	public ArrayList<AnaliseNaoConformidades> obterAnaliseNaoConformidades(boolean ehOPM, String competenciaInicial) 
	{
		definirDiretorios(ehOPM, competenciaInicial);
		
		String mesTexto = competenciaInicial.split("/")[0];
		int mes = Integer.parseInt(mesTexto);
		String ano = competenciaInicial.split("/")[1];
		
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoMonitoramentoNaoConformidades();
		
		if(ehOPM)
			caminhoArquivo = caminhoArquivo.replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_OPM.getTextoIdentificador() + " " + ano);
		else
			caminhoArquivo = caminhoArquivo.replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_ELETIVAS.getTextoIdentificador() + " " + ano);
		
		AcoesArquivoExcel arquivoMonitoramento = new AcoesArquivoExcel(caminhoArquivo, 0);
		
		String planilha = meses.getMeses().get(mes - 1).getMesDescricao() + " " + ano;
		
		arquivoMonitoramento.abrirPlanilha(planilha, 0);
		
		int linhaArquivo = ParametrosArquivoGEFICNaoConformidades.LINHA_INICIAL_ARQUIVO.getIndice();
		
		ArrayList<AnaliseNaoConformidades> naoConformidades = new ArrayList<AnaliseNaoConformidades>();
		
		String valorDataFinalizacao = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getIndice(), "dd/MM/yyyy");
		
		while(!valorDataFinalizacao.trim().equals(""))
		{
			AnaliseNaoConformidades analiseNaoConformidade = new AnaliseNaoConformidades();
			analiseNaoConformidade.setExecutante(arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_EXECUTANTE.getIndice(), ""));
			analiseNaoConformidade.setNaoConformidades(arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_NAO_CONFORMIDADE.getIndice(), ""));
			analiseNaoConformidade.setAnalise(arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_ALERTA.getIndice(), ""));
			analiseNaoConformidade.setLinhaExcel(linhaArquivo);
			
			naoConformidades.add(analiseNaoConformidade);
			
			linhaArquivo++;
			valorDataFinalizacao = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getIndice(), "dd/MM/yyyy");
		}
		
			
		return naoConformidades;
	}
	
	public String contabilizarNaoConformidades(boolean ehOPM, String competenciaInicial, HashMap<String, HashMap<String, Integer>> naoConformidadesPorUnidade)
	{
	
		definirDiretorios(ehOPM, competenciaInicial);
		
		String mesTexto = competenciaInicial.split("/")[0];
		int mes = Integer.parseInt(mesTexto);
		String ano = competenciaInicial.split("/")[1];
		
		String caminhoArquivo = pastaBaseAmbulatorialCDIDR + "\\" + diretoriosCDIDR.getArquivoMonitoramentoNaoConformidades();
		
		if(ehOPM)
			caminhoArquivo = caminhoArquivo.replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_OPM.getTextoIdentificador() + " " + ano);
		else
			caminhoArquivo = caminhoArquivo.replace(IdentificadoresPastasCompartilhadasCDIDRGEFIC.MASCARA_NOMES_DINAMICOS.getTextoIdentificador(), IdentificadoresPastasCompartilhadasCDIDRGEFIC.TEXTO_IDENTIFICADOR_ELETIVAS.getTextoIdentificador() + " " + ano);
		
		AcoesArquivoExcel arquivoMonitoramento = new AcoesArquivoExcel(caminhoArquivo, 0);
		
		String planilha = meses.getMeses().get(mes - 1).getMesDescricao() + " " + ano;
		
		arquivoMonitoramento.abrirPlanilha(planilha, 0);
		
		int linhaArquivo = ParametrosArquivoGEFICNaoConformidades.LINHA_INICIAL_ARQUIVO.getIndice();
		
		ArrayList<AnaliseNaoConformidades> naoConformidades = new ArrayList<AnaliseNaoConformidades>();
		
		String valorDataFinalizacao = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getIndice(), "dd/MM/yyyy");
		
		while(!valorDataFinalizacao.trim().equals(""))
		{
			String valorNaoConformidade = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_NAO_CONFORMIDADE.getIndice(), "");
			String unidade = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_EXECUTANTE.getIndice(), "");
			
			if(!valorNaoConformidade.equals(""))
			{
				String valorAlerta = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_ALERTA.getIndice(), "");
				if(valorAlerta.contains(ParametrosArquivoGEFICNaoConformidades.TEXTO_DESCONSIDERAR.getDescricao()))
				{
					valorAlerta = valorAlerta.replaceAll(ParametrosArquivoGEFICNaoConformidades.TEXTO_DESCONSIDERAR.getDescricao(), "");
					
					if(valorAlerta.trim().equals(""))
						valorNaoConformidade = "";
					else
					{
						String[] naoConformidadesDesconsideradas = valorAlerta.split(ParametrosArquivoGEFICNaoConformidades.TEXTO_SEPARADOR_NAO_CONFORMIDADES.getDescricao().trim());
						
						for(String desconsiderar : naoConformidadesDesconsideradas)
						{
							//System.out.println(desconsiderar);
							valorNaoConformidade = valorNaoConformidade.replaceAll(desconsiderar.trim(), "");
						}
					}
				}
				
				String[] naoConformidadesConsideradas = valorNaoConformidade.split(ParametrosArquivoGEFICNaoConformidades.TEXTO_SEPARADOR_NAO_CONFORMIDADES.getDescricao().trim());
				
				for(String naoConformidade : naoConformidadesConsideradas)
				{
					naoConformidade = naoConformidade.trim();
					
					if(!naoConformidade.equals(""))
					{
						if(naoConformidadesPorUnidade.containsKey(unidade))
						{
							HashMap<String, Integer> naoConformidadesDaUnidade = naoConformidadesPorUnidade.get(unidade);
							if(naoConformidadesDaUnidade.containsKey(naoConformidade))
							{
								int quantidade = naoConformidadesDaUnidade.get(naoConformidade);
								quantidade++;
								naoConformidadesDaUnidade.put(naoConformidade, quantidade);
							}
							else
							{
								naoConformidadesDaUnidade.put(naoConformidade, 1);
							}
						}
						else
						{
							HashMap<String, Integer> naoConformidadesDaUnidade = new HashMap<String, Integer>();
							naoConformidadesDaUnidade.put(naoConformidade, 1);
							naoConformidadesPorUnidade.put(unidade, naoConformidadesDaUnidade);
						}
					}
				}
			}
			
			linhaArquivo++;
			valorDataFinalizacao = arquivoMonitoramento.getValorDaCelulaComoString(linhaArquivo, ParametrosArquivoGEFICNaoConformidades.INDICE_COLUNA_DATA_FINALIZACAO.getIndice(), "dd/MM/yyyy");
		}
		
		return "";
	}

}
