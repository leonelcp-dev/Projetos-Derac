package extracao_dados.siresp;

import java.awt.image.WritableRenderedImage;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.ParametrosArquivoAgendamentosPendentesRegulada;
import dominiosSIRESP.EspecialidadesSIRESP;
import dominiosSIRESP.ExamesSIRESP;
import dominiosSIRESP.StatusAgendamentoSIRESP;
import dominiosSIRESP.UnidadeSIRESP;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.ExcelBinder;
import modelosDados.AgendamentosPendentesRegulada;
import modelosDados.CelulaExcel;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadesFilaCentralReg;
import modelosDados.LinhaImportacaoSIRESP;
import modelosDados.UsuarioFilaCentralReg;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class CriarImportacaoFilaCentralReg {

	public static void main( String[] args )
    {
		String caminhoArquivosImportados = "C:\\Users\\PMC514991-2\\Documents\\Importacao\\";
		String caminhoArquivosUnidades = "C:\\Users\\PMC514991-2\\Documents\\Fila Unica\\";
		String arquivoBase = "modelo_padrao_cdr.csv";
		String arquivoUnidades = "Dados XLSX\\UNIDADES.xlsx";
		String arquivoEspecialidades = "Dados XLSX\\ESPECIALIDADE.xlsx";
		String arquivoExames = "Dados XLSX\\EXAMES.xlsx";
		String arquivoStatusAgendamento = "Dados XLSX\\STATUS_AGENDAMENTO.xlsx";
		
		CriarImportacaoFilaCentralReg filaCentralReg = new CriarImportacaoFilaCentralReg();
		
		ArrayList<EntidadesFilaCentralReg> entidades = filaCentralReg.lerEntidades(caminhoArquivosImportados + "unidades.csv");
		
		//Lendo tabelas de Domínio - Unidades
		ArrayList<UnidadeSIRESP> unidades = null;
		HashMap<String, UnidadeSIRESP> unidadesSIRESP = new HashMap<String, UnidadeSIRESP>();
		
		try (FileInputStream in = new FileInputStream(caminhoArquivosImportados + arquivoUnidades)) { 
			unidades = ExcelBinder.readSheet(in, UnidadeSIRESP.class, 0, 0, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(unidades != null)
			for(UnidadeSIRESP unidade : unidades)
				unidadesSIRESP.put(unidade.getUnidadeFantasia().toUpperCase().replaceAll("\u00A0", "").trim(), unidade);
		
		//Lendo tabelas de Domínio - Especialidades
		ArrayList<EspecialidadesSIRESP> especialidades = null;
		HashMap<String, EspecialidadesSIRESP> especialidadesSIRESP = new HashMap<String, EspecialidadesSIRESP>();
		
		try (FileInputStream in = new FileInputStream(caminhoArquivosImportados + arquivoEspecialidades)) { 
			especialidades = ExcelBinder.readSheet(in, EspecialidadesSIRESP.class, 0, 0, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(especialidades != null)
			for(EspecialidadesSIRESP especialidade : especialidades)
				especialidadesSIRESP.put(especialidade.getNomeEspecialidade().toUpperCase().replaceAll("\u00A0", "").trim(), especialidade);
		
		//Lendo tabelas de Domínio - Exames
		ArrayList<ExamesSIRESP> exames = null;
		HashMap<String, ExamesSIRESP> examesSIRESP = new HashMap<String, ExamesSIRESP>();
		
		try (FileInputStream in = new FileInputStream(caminhoArquivosImportados + arquivoExames)) { 
			exames = ExcelBinder.readSheet(in, ExamesSIRESP.class, 0, 0, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(exames != null)
			for(ExamesSIRESP exame : exames)
				examesSIRESP.put(exame.getNomeExame().toUpperCase().replaceAll("\u00A0", "").trim(), exame);
		
		//Lendo tabelas de Domínio - Status Agendamento
		ArrayList<StatusAgendamentoSIRESP> statusAgendamento = null;
		HashMap<String, StatusAgendamentoSIRESP> statusAgendamentoSIRESP = new HashMap<String, StatusAgendamentoSIRESP>();
		
		try (FileInputStream in = new FileInputStream(caminhoArquivosImportados + arquivoStatusAgendamento)) { 
			statusAgendamento = ExcelBinder.readSheet(in, StatusAgendamentoSIRESP.class, 0, 0, true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(statusAgendamento != null)
			for(StatusAgendamentoSIRESP status : statusAgendamento)
				statusAgendamentoSIRESP.put(status.getStatus().toUpperCase(), status);
		//Fim da leitura das tabelas de domínio
		
		AcoesArquivoExcel estatisticas = new AcoesArquivoExcel(caminhoArquivosImportados + "\\Estatisticas Importacao.xlsx", 0);
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		int linhaArquivoEstatisticas = 1;
		
		for(EntidadesFilaCentralReg entidade : entidades)
		{
			String caminho = caminhoArquivosUnidades + entidade.getDistrito() + "\\" + entidade.getNomeArquivo();
			
			System.out.println(caminho);
			
			ArrayList<UsuarioFilaCentralReg> entradasFilaCentralReg = null;
			
			try (FileInputStream in = new FileInputStream(caminho)) { 
				entradasFilaCentralReg = ExcelBinder.readSheet(in, UsuarioFilaCentralReg.class, 0, 0, true);
	        }
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			ArrayList<LinhaImportacaoSIRESP> importar = new ArrayList<LinhaImportacaoSIRESP>();
			ArrayList<LinhaImportacaoSIRESP> naoImportar = new ArrayList<LinhaImportacaoSIRESP>();
			
			if(entradasFilaCentralReg != null)
			{
				for(UsuarioFilaCentralReg entradaFila : entradasFilaCentralReg)
				{
					LinhaImportacaoSIRESP linha = new LinhaImportacaoSIRESP();
					
					linha.setObservacao("");
					
					//Codigo e nome da Unidade
					if(unidadesSIRESP.containsKey(entidade.getNomeSIRESP().toUpperCase()))
					{
						linha.setUnidadeSolicitanteCodigo(unidadesSIRESP.get(entidade.getNomeSIRESP().toUpperCase()).getCodUnidade().replaceAll("\u00A0", "").trim());
						linha.setUnidadeSolicitanteNome(unidadesSIRESP.get(entidade.getNomeSIRESP().toUpperCase()).getUnidadeFantasia().replaceAll("\u00A0", "").trim());
					}
					else
					{
						linha.setUnidadeSolicitanteCodigo("");
						linha.setUnidadeSolicitanteNome("");
						linha.setObservacao(linha.getObservacao() + "Unidade não encontrada;");
					}
					
					//Codigo e nome do paciente
					linha.setCodPaciente(entradaFila.codigo.trim());
					linha.setNomePaciente(entradaFila.nome.trim());
					
					//Data de entrada do paciente
					linha.setDataEntrada(entradaFila.dataEntrada.substring(0, 10));
					
					//Tipo - Consulta ou Exame
					if(entradaFila.exameOuConsulta.equals("Consulta"))
					{
						linha.setTipo("C");
						
						if(especialidadesSIRESP.containsKey(entradaFila.especialidade.toUpperCase()))
						{
							linha.setId_ConsultaExame(especialidadesSIRESP.get(entradaFila.especialidade.trim().toUpperCase()).getIdEspecialidade().replaceAll("\u00A0", "").trim());
							linha.setNomeExameEspecialidade(especialidadesSIRESP.get(entradaFila.especialidade.trim().toUpperCase()).getNomeEspecialidade().replaceAll("\u00A0", "").trim());
						}
						else
						{
							linha.setId_ConsultaExame("");
							linha.setNomeExameEspecialidade("");	
							linha.setObservacao(linha.getObservacao() + "Não foi encontrada a especialidade da consulta;");
						}
							
					}
					else if(entradaFila.exameOuConsulta.equals("Exame"))
					{
						linha.setTipo("E");
						
						if(examesSIRESP.containsKey(entradaFila.especialidade.toUpperCase()))
						{
							linha.setId_ConsultaExame(examesSIRESP.get(entradaFila.especialidade.trim().toUpperCase()).getIdExame().replaceAll("\u00A0", "").trim());
							linha.setNomeExameEspecialidade(examesSIRESP.get(entradaFila.especialidade.trim().toUpperCase()).getNomeExame().replaceAll("\u00A0", "").trim());
						}
						else
						{
							linha.setId_ConsultaExame("");
							linha.setNomeExameEspecialidade("");			
							linha.setObservacao(linha.getObservacao() + "Não foi encontrado o nome do exame;");
						}
					}
					else
					{
						linha.setTipo("");
						linha.setId_ConsultaExame("");
						linha.setNomeExameEspecialidade("");
						linha.setObservacao(linha.getObservacao() + "Tipo não encontrado;");
					}
					
					//Tipo da Consulta ou Exame
					switch(entradaFila.tipo.toUpperCase().trim())
					{
						case "1ª CONSULTA": linha.setTipoConsultaExame("1"); break;
						case "INTERCONSULTA": linha.setTipoConsultaExame("I"); break;
						case "RETORNO": linha.setTipoConsultaExame("R"); break;
						case "INTERNO": linha.setTipoConsultaExame("IN"); break;
						case "EXTERNO": linha.setTipoConsultaExame("EX"); break;
						default: linha.setTipoConsultaExame(""); linha.setObservacao(linha.getObservacao() + "Tipo Consulta Exame Não Encontrado;"); break;
					}
					
					//Prioridade
					if(entradaFila.prioridade.trim().toUpperCase().equals("X"))
						linha.setPrioridade("S");
					else if(entradaFila.prioridade.trim().equals(""))
						linha.setPrioridade("N");
					else
					{
						linha.setPrioridade("");
						linha.setObservacao(linha.getObservacao() + "Valor inesperado no campo prioridade;");
					}
					
					if(!entradaFila.status.trim().toUpperCase().equals("AGENDADO"))
						linha.setStatus("0");
					else
					{
						linha.setStatus("");
						linha.setObservacao(linha.getObservacao() + "Usuário já agendado;");
					}
					
					linha.setCid(entradaFila.cid.trim());
					
					//CRM Profissional Retorno
					linha.setCrmProfissionalRetorno("");
					linha.setUnidadeIndicadaAgendamento("");
					linha.setAnoPretendido("");
					linha.setMes_pretendido("");
					linha.setTurno("");
					
					if(linha.getObservacao().equals(""))
					{
						linha.setObservacao("Paciente transferido da unidade " + IdentificadoresPaginaWebSIRESP.TEXTO_UNIDADE_POLICLINICA_III.getTextoIdentificador());
						importar.add(linha);
					}
					else
					{
						naoImportar.add(linha);
					}
				}
				
				if(importar.size() > 0)
				{
					
					filaCentralReg.gravarCSVDeImportacao("Importar", entidade, caminhoArquivosImportados, arquivoBase, importar);
				}
				
				if(naoImportar.size() > 0)
				{
					
					filaCentralReg.gravarCSVDeImportacao("Não Importar", entidade, caminhoArquivosImportados, arquivoBase, naoImportar);
				}
				
				celulas.add(new CelulaExcel(linhaArquivoEstatisticas, 0, entidade.getDistrito(), "String"));
				celulas.add(new CelulaExcel(linhaArquivoEstatisticas, 1, entidade.getNomeSIRESP(), "String"));
				celulas.add(new CelulaExcel(linhaArquivoEstatisticas, 2, importar.size(), "Int"));
				celulas.add(new CelulaExcel(linhaArquivoEstatisticas, 3, naoImportar.size(), "Int"));
				
				linhaArquivoEstatisticas++;
				
				estatisticas.gravarDadosEmCelula("Plan1", celulas, false, false, 0, null);
			}
		}
    }
	
	private void gravarCSVDeImportacao(String pastaIntermediaria, EntidadesFilaCentralReg entidade, String caminhoArquivosImportados, String arquivoBase, ArrayList<LinhaImportacaoSIRESP> listaImportacao)
	{
		Pasta pasta = new Pasta(caminhoArquivosImportados + "\\" + pastaIntermediaria + "\\" + entidade.getDistrito(), true);
		
		Arquivo arquivo = new Arquivo(caminhoArquivosImportados, arquivoBase);
		String caminhoArquivoCSV = caminhoArquivosImportados + "\\" + pastaIntermediaria + "\\" + entidade.getDistrito() + "\\" + entidade.getNomeArquivo();
		caminhoArquivoCSV = caminhoArquivoCSV.replaceAll(".xlsx", ".csv");
		arquivo.CopiarArquivo(caminhoArquivoCSV);
		

		FileWriter writer;
		Charset charset = Charset.forName("Windows-1252");
		try {
			writer = new FileWriter(caminhoArquivoCSV, charset, true);

		
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").build());
		
			for(LinhaImportacaoSIRESP linha : listaImportacao)
			{
				csvPrinter.printRecord(linha.getUnidadeSolicitanteCodigo(), 
									   linha.getUnidadeSolicitanteNome(), 
									   linha.getCodPaciente(), 
									   linha.getNomePaciente(), 
									   linha.getDataEntrada(), 
									   linha.getTipo(), 
									   linha.getTipoConsultaExame(), 
									   linha.getId_ConsultaExame(), 
									   linha.getNomeExameEspecialidade(), 
									   linha.getPrioridade(), 
									   linha.getCrmProfissionalRetorno(), 
									   linha.getStatus(), 
									   linha.getUnidadeIndicadaAgendamento(), 
									   linha.getAnoPretendido(), 
									   linha.getMes_pretendido(), 
									   linha.getTurno(), 
									   linha.getCid(), 
									   linha.getObservacao());
			}
		    
		    csvPrinter.flush();
		    csvPrinter.close();		
		        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private ArrayList<EntidadesFilaCentralReg> lerEntidades(String nomeArquivo)
	{
		ArrayList<EntidadesFilaCentralReg> entidades = new ArrayList();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
                String unidade = registro.get("Unidade");
                String distrito = registro.get("Distrito");
                String nomeSIRESP = registro.get("Nome SIRESP");
                
                entidades.add(new EntidadesFilaCentralReg(unidade, distrito, "", nomeSIRESP, ".xlsx"));
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	private ArrayList<UsuarioFilaCentralReg> lerArquivo(String nomeArquivo, String ExameOuConsulta)
	{
		ArrayList<UsuarioFilaCentralReg> entidades = new ArrayList();
		
		Charset charset = Charset.forName("Windows-1252");
        try (Reader reader = new FileReader(nomeArquivo, charset);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
            	//																				

            	UsuarioFilaCentralReg usuario = new UsuarioFilaCentralReg();
            	
            	usuario.exameOuConsulta = ExameOuConsulta;
            	usuario.codigo = registro.get("Código");
            	usuario.nome = registro.get("Nome");
            	usuario.telefone = registro.get("Telefone");
            	usuario.municipio = registro.get("Município");
            	if(ExameOuConsulta.equals("Consulta"))
            		usuario.especialidade = registro.get("Especialidade");
            	else if(ExameOuConsulta.equals("Exame"))
            		usuario.especialidade = registro.get("Exame");
            	else
            		usuario.especialidade = "";
            	usuario.cid = registro.get("Cid");
            	usuario.tipo = registro.get("Tipo " + ExameOuConsulta);
            	usuario.profissional = registro.get("Profissional");
            	usuario.idade = registro.get("Idade do Paciente");
            	usuario.mesAnoPretendido = registro.get("Mês/Ano Pretendido");
            	usuario.turno = registro.get("Turno");
            	usuario.dataAgenda = registro.get("Data Agenda");
            	usuario.Horario = registro.get("Horário");
            	usuario.dataEntrada = registro.get("Data Entrada");
            	usuario.status = registro.get("Status");
            	usuario.filipeta = registro.get("Filipeta");
            	usuario.retFilipeta = registro.get("Ret. Filipeta");
            	usuario.prioridade = registro.get("Prioridade");
            	
            	if(ExameOuConsulta.equals("Consulta"))
            		usuario.aceitaTeleconsulta = registro.get("Aceita Teleconsulta");
            	else
            		usuario.aceitaTeleconsulta = "";
            		
            	usuario.observacao = registro.get("Observação");
            	usuario.observacaoStatus = registro.get("Observação Status");
            	usuario.alteracaoEspecialidadeExameDe = registro.get("Alteração Especialidade/Exame - De");
            	usuario.alteracaoEspecialidadeExamePara = registro.get("Alteração Especialidade/Exame - Para");
            	usuario.observacao2 = registro.get("Observação 2");
            	usuario.usuario2 = registro.get("Usuário 2");
            	usuario.dataDeAlteracao = registro.get("Data de alteração");
            	usuario.alteracaoCIDDe = registro.get("Alteração CID - De");
            	usuario.alteracaoCIDPara = registro.get("Alteração CID - Para");
            	usuario.observacao3 = registro.get("Observação 3");
            	usuario.usuario3 = registro.get("Usuário 3");
            	usuario.dataAlteracao3 = registro.get("Data de alteração 3");
            	
                entidades.add(usuario);
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	

	public static String removerAcentos(String s) {
	    if (s == null) return null;
	    // NFD decompõe: "ç" -> "c" + [marca de cedilha], "á" -> "a" + [acento agudo], etc.
	    String decomposed = Normalizer.normalize(s, Form.NFD);
	    // Remove todas as marcas combinantes (diacríticos)
	    return decomposed.replaceAll("\\p{M}+", "");
	}

	
}
