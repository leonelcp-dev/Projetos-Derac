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

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoCenso;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import modelosDados.EntidadeLeito;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

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
	private String pastaDestinoArquivos;
	private String pastaDownloads;
	private int execucaoCompleta;
	private MesesFormatados meses;	

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
		meses = new MesesFormatados();
		
		pastaDestinoArquivos = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados do censo", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE);
		pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE);
		execucaoCompleta = JOptionPane.showOptionDialog(null, "Favor informar se será realizada a rotina completa com a execução da macro.", "Tipo de Execução", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoesSimNao, null);
		System.out.println(execucaoCompleta);
		
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
				
		System.out.println(IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
		System.out.println(IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador());
		
		paginaWeb.clicarMenuUL(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes);
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
		baixarArquivosCenso(driver, paginaWeb, entidades);
		
		buscarMotivoBloqueio(driver, paginaWeb, entidades);
		
		return "";	
	}
	
	private ArrayList<EntidadeLeito> lerEntidades(String nomeArquivo)
	{
		ArrayList<EntidadeLeito> entidades = new ArrayList();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

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
	
	private String baixarArquivosCenso(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, ArrayList<EntidadeLeito> entidades) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		for(EntidadeLeito entidade : entidades)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_LEITOS_FILTRO_UNIDADE.getTextoIdentificador(), entidade.getNomeSIRESP());
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_LEITOS_BOTAO_DOWNLOAD.getTextoIdentificador());
			
			String arquivoMaisRecente;
			
			do
			{
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				arquivoMaisRecente = pastaOrigem.arquivoRecentementeModificado();
				
				System.out.println(arquivoMaisRecente + " ----- " + ultimoRecente);
			}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith("xls"));
			
			Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);
			arquivo.renomear(entidade.getNomePasta() + " " + arquivoMaisRecente);
			
			ultimoRecente = arquivo.getNomeDoArquivo();
			
			entidade.setArquivoBaixado(arquivo.getCaminhoCompleto());
		}
		
		return "";
	}
	
	private String buscarMotivoBloqueio(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, ArrayList<EntidadeLeito> entidades) 
	{
		
		for(EntidadeLeito entidade : entidades)
		{
			AcoesArquivoExcel arquivoExcelEntidade = new AcoesArquivoExcel(entidade.getArquivoBaixado());
			
			if(arquivoExcelEntidade.isAberto())
			{
				for(int contLinha = ParametrosArquivoCenso.LINHA_INICIAL_ARQUIVO.getIndice(); contLinha < arquivoExcelEntidade.getPrimeiraLinhaVazia(); contLinha++)
				{
					if(arquivoExcelEntidade.getValorDaCeula(contLinha, ParametrosArquivoCenso.INDICE_COLUNA_BLOQUEADO.getIndice()).equals(ParametrosArquivoCenso.TEXTO_CONFIRMA_BLOQUEIO.getDescricao()))
					{
						//preenchendo os filtros para buscar o motivo do bloqueio
						String especialidade = arquivoExcelEntidade.getValorDaCeula(contLinha, ParametrosArquivoCenso.INDICE_COLUNA_ESPECIALIDADE.getIndice());
						String descricaoLeito = arquivoExcelEntidade.getValorDaCeula(contLinha, ParametrosArquivoCenso.INDICE_COLUNA_DESCRICAO_LEITO.getIndice());
						String descricaoEnfermaria = arquivoExcelEntidade.getValorDaCeula(contLinha, ParametrosArquivoCenso.INDICE_COLUNA_DESCRICAO_ENFERMARIA.getIndice());
						String status = arquivoExcelEntidade.getValorDaCeula(contLinha, ParametrosArquivoCenso.INDICE_COLUNA_STATUS.getIndice());
						String sexo = arquivoExcelEntidade.getValorDaCeula(contLinha, ParametrosArquivoCenso.INDICE_COLUNA_SEXO.getIndice());
						
						paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_LEITOS_FILTRO_ESPECIALIDADE.getTextoIdentificador(), especialidade);
						paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_LEITOS_FILTRO_STATUS.getTextoIdentificador(), status);
						paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_LEITOS_FILTRO_SEXO.getTextoIdentificador(), sexo);
						
						paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_LEITOS_FILTRO_DESCRICAO.getTextoIdentificador(), "");
						paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_LEITOS_FILTRO_DESCRICAO.getTextoIdentificador(), descricaoLeito);
						
						paginaWeb.clicarBotaoSubmit(driver, especialidade);
						
						//é necessário verificar a tabela resultante, visto que o mesmo leito pode aparecer mais de uma vez, diferenciando-os pela coluna enfermaria
						
						ArrayList<ArrayList<String>> tabelaCenso = paginaWeb.obterTable(driver, IdentificadoresPaginaWebSIRESP.ID_TABELA_CENSO.getTextoIdentificador());
						
						for(ArrayList<String> linha : tabelaCenso)
						{
							if(linha.get(IdentificadoresPaginaWebSIRESP.NUM_COLUNA_TABELA_CENSO_DESCRICAO_ENFERMARIA.getIndice()).equals(descricaoEnfermaria))
							{
								//clicar no botão de bloqueio
							}
						}
						
					}
				}
			}
		}
		
		return "";
	}
	
}
