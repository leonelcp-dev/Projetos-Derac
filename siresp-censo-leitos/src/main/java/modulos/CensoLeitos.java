package modulos;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.WebDriver;

import dadosGerais.MesesFormatados;
import interacao_pagina_web.AcoesGeraisPaginaWeb;
import modelosDados.EntidadeLeito;

/*
 * Nota: o censo dos leitos são monitorados 3 vezes ao dia, sendo que apenas o censo executado às 10:00 da manhã necessita da execução de consolidação dos leitos, arquivo 0 (zero).
 * O arquivo chamado 0 (zero) fica na pasta de cada entidade de acordo com nomenclatura pré estabelecida no DERAC
 * 
 * as demais execuções do censo se dão às 15:00 e 18:00 horas. Nestes dois casos não é necessária a consolidação do arquivo 0 (zero), os arquivos ficam salvos na pasta PRINT da entidade.
 * 
 * Estrutura de pastas do módulo de Censos
 * 
 * CENSO <ano>
 * 		<nomenclatura entidade> <ano>
 * 			<mês numérico> <mês descrito> <ano>
 * 				PRINT 15 e 18h
 * 				arquivo 0 (zero)
 * 				arquivos baixados às 10:00
 * 
 * Importante: de 10:00 até 11:00 é o horário combinado com as entidades para não alterarem o status do bloqueio de cada leito.
 * As rotinas de download do censo e de obtenção dos motivos do bloqueio deve finalizar até 11:00.
 * 
 * */


public class CensoLeitos {
	
	private String rotinaCompleta;
	private String PastaPrint;
	private String PastaPrintFormatada;
	private int mesCompetencia;
	private int anoCompetencia;

	public String executarCenso(WebDriver driver)
	{	
		String opcoesSimNao[] = {"Sim", "Não"};
		
				
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Internação");
		opcoes.add("Leito");
		
		LocalDate data = LocalDate.now();
		mesCompetencia = data.getMonthValue();
		anoCompetencia = data.getYear();
		
		//definindo a formatação dos meses para permitir que seja possível criar a estrutura das pastas
		MesesFormatados meses = new MesesFormatados();
		
		String pastaDestinoArquivos = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados do censo", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE);
		String pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE);
		int execucaoCompleta = JOptionPane.showOptionDialog(null, "Favor informar se será realizada a rotina completa com a execução da macro.", "Tipo de Execução", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoesSimNao, null);
		
		if(execucaoCompleta == 0)
		{
			rotinaCompleta = "Sim";
			PastaPrint = "";
			PastaPrintFormatada = "";
		}
		else
		{
			rotinaCompleta = "Não";
			PastaPrint = "PRINT 15 e 18h";
			PastaPrintFormatada = "\\PRINT 15 e 18h\\";
		}
		
		//definindo entidades para o censo de leitos
		ArrayList<EntidadeLeito> entidades = lerEntidades(pastaDestinoArquivos + "\\entidades.csv");
		
		
		paginaWeb.clicarMenuUL(driver, "site", "example", opcoes);
		
		return "";	
	}
	
	private ArrayList<EntidadeLeito> lerEntidades(String nomeArquivo)
	{
		ArrayList<EntidadeLeito> entidades = new ArrayList();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
                String siglaCenso = registro.get("Sigla Censo");
                String textoLeitosSIRESP = registro.get("Texto Leitos SIRESP");
                
                entidades.add(new EntidadeLeito(siglaCenso, textoLeitosSIRESP));
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
}
