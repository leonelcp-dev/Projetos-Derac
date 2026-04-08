package extracao_dados.siresp;

import java.awt.image.WritableRenderedImage;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import dadosGerais.ParametrosArquivoAgendamentosPendentesRegulada;
import dominiosSIRESP.UnidadeSIRESP;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.ExcelBinder;
import modelosDados.AgendamentosPendentesRegulada;
import modelosDados.CelulaExcel;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadesFilaCentralReg;
import modelosDados.UsuarioFilaCentralReg;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class CriarImportacaoFilaCentralReg {

	public static void main( String[] args )
    {
		String caminhoArquivos = "C:\\Users\\PMC514991-2\\Documents\\Importacao\\";
		String arquivoBase = "modelo_padrao_cdr.csv";
		String arquivoUnidades = "Dados XLSX\\UNIDADES.xlsx";
		String arquivoEspecialidades = "Dados XLSX\\ESPECIALIDADE.xlsx";
		String arquivoExames = "Dados XLSX\\EXAMES.xlsx";
		String arquivoStatusAgendamento = "Dados XLSX\\STATUS_AGENDAMENTO.xlsx";
		
		CriarImportacaoFilaCentralReg filaCentralReg = new CriarImportacaoFilaCentralReg();
		
		ArrayList<EntidadesFilaCentralReg> entidades = filaCentralReg.lerEntidades(caminhoArquivos + "unidades.csv");
		entidades.add(new EntidadesFilaCentralReg("Outros", "Outros", ""));
		
		//Lendo tabelas de Domínio - Unidades
		ArrayList<UnidadeSIRESP> unidades;
		HashMap<String, ArrayList<UnidadeSIRESP>> unidadesSIRESP = new HashMap<String, ArrayList<UnidadeSIRESP>>();
		
		try (FileInputStream in = new FileInputStream(caminhoArquivos + arquivoUnidades)) { 
			//unidades = ExcelBinder.readSheet(in, AgendamentosPendentesRegulada.class, nomePlanilha, ParametrosArquivoAgendamentosPendentesRegulada.ARQUIVO_BAIXADO_LINHA_INICIAL.getIndice(), true);
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		for(EntidadesFilaCentralReg entidade : entidades)
		{
			String caminho = caminhoArquivos + entidade.getDistrito();
			
			Pasta pasta = new Pasta(caminho, true);
			
			Arquivo arquivo = new Arquivo(caminhoArquivos, arquivoBase);
			arquivo.CopiarArquivo(caminho + "\\" + entidade.getNomeArquivo());
			
			AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(caminho + "\\" + entidade.getNomeArquivo(), 0);
			ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
			
			int linha =  1;
			for(UsuarioFilaCentralReg usuario : entidade.getPacientes())
			{
				celulas.add(new CelulaExcel(linha, 0, usuario.exameOuConsulta, "String"));
				celulas.add(new CelulaExcel(linha, 1, usuario.codigo, "String"));
				celulas.add(new CelulaExcel(linha, 2, usuario.nome, "String"));
				celulas.add(new CelulaExcel(linha, 3, usuario.telefone, "String"));
				celulas.add(new CelulaExcel(linha, 4, usuario.municipio, "String"));
				celulas.add(new CelulaExcel(linha, 5, usuario.especialidade, "String"));
				celulas.add(new CelulaExcel(linha, 6, usuario.cid, "String"));
				celulas.add(new CelulaExcel(linha, 7, usuario.tipo, "String"));
				celulas.add(new CelulaExcel(linha, 8, usuario.profissional, "String"));
				celulas.add(new CelulaExcel(linha, 9, usuario.idade, "String"));
				celulas.add(new CelulaExcel(linha, 10, usuario.mesAnoPretendido, "String"));
				celulas.add(new CelulaExcel(linha, 11, usuario.turno, "String"));
				celulas.add(new CelulaExcel(linha, 12, usuario.dataAgenda, "String"));
				celulas.add(new CelulaExcel(linha, 13, usuario.Horario, "String"));
				celulas.add(new CelulaExcel(linha, 14, usuario.dataEntrada, "String"));
				celulas.add(new CelulaExcel(linha, 15, usuario.status, "String"));
				celulas.add(new CelulaExcel(linha, 16, usuario.filipeta, "String"));
				celulas.add(new CelulaExcel(linha, 17, usuario.retFilipeta, "String"));
				celulas.add(new CelulaExcel(linha, 18, usuario.prioridade, "String"));
				celulas.add(new CelulaExcel(linha, 19, usuario.aceitaTeleconsulta, "String"));
				celulas.add(new CelulaExcel(linha, 20, usuario.observacao, "String"));
				celulas.add(new CelulaExcel(linha, 21, usuario.observacaoStatus, "String"));
				celulas.add(new CelulaExcel(linha, 22, usuario.alteracaoEspecialidadeExameDe, "String"));
				celulas.add(new CelulaExcel(linha, 23, usuario.alteracaoEspecialidadeExamePara, "String"));
				celulas.add(new CelulaExcel(linha, 24, usuario.observacao2, "String"));
				celulas.add(new CelulaExcel(linha, 25, usuario.usuario2, "String"));
				celulas.add(new CelulaExcel(linha, 26, usuario.dataDeAlteracao, "String"));
				celulas.add(new CelulaExcel(linha, 27, usuario.alteracaoCIDDe, "String"));
				celulas.add(new CelulaExcel(linha, 28, usuario.alteracaoCIDPara, "String"));
				celulas.add(new CelulaExcel(linha, 29, usuario.observacao3, "String"));
				celulas.add(new CelulaExcel(linha, 30, usuario.usuario3, "String"));
				celulas.add(new CelulaExcel(linha, 31, usuario.dataAlteracao3, "String"));
				
				linha++;
			}
			
			arquivoExcel.gravarDadosEmCelula(0, celulas);
		}
		
		for(EntidadesFilaCentralReg entidade : entidades)
		{
			System.out.println(entidade.getUnidade() + "\t" + entidade.getPacientes().size());
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
                String escritoComo = registro.get("Escrito como");
                
                entidades.add(new EntidadesFilaCentralReg(unidade, distrito, escritoComo));
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
