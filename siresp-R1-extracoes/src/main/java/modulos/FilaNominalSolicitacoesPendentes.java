package modulos;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoFilasNominais;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.ConversaoHMTL_XLSX;
import modelosDados.CelulaExcel;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeLeito;
import modelosDados.LinhaCensoLeitos;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class FilaNominalSolicitacoesPendentes {
	
	private int mesCompetencia;
	private int anoCompetencia;
	private String pastaDestinoArquivos;
	private String pastaDownloads;
	private MesesFormatados meses;	
	private DateTimeFormatter formatoDataPasta;
	LocalDate dataProcessamento;
	String dataFormatadaPasta;

	public String baixarFilaAgendamentosPendentes(WebDriver driver)
	{			
		formatoDataPasta = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Regulação");
		opcoes.add("Solicitações Pendentes");
		
		dataProcessamento = LocalDate.now();
		mesCompetencia = dataProcessamento.getMonthValue();
		anoCompetencia = dataProcessamento.getYear();
		
		dataFormatadaPasta = dataProcessamento.format(formatoDataPasta);
		
		System.out.println(dataFormatadaPasta);
		
		//definindo a formatação dos meses para permitir que seja possível criar a estrutura das pastas
		meses = new MesesFormatados();
		
		pastaDestinoArquivos = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados das filas nominais", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE);
		pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE);
		
	
		//definindo entidades para o censo de leitos
		ArrayList<EntidadeCDRNaoRegulada> entidades = new ArrayList<EntidadeCDRNaoRegulada>();
		entidades.add(new EntidadeCDRNaoRegulada("5416655", "SMS - CAMPINAS", "AGENDA REGULADA"));
		
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
		ArrayList<ElementoSelecao> listaUnidadeRadio = paginaWeb.getListaDeOpcoesRadioPorName(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_RADIO_UNIDADES.getTextoIdentificador());
		
		HashMap<String, String> elementosRadioUnidades = new HashMap<String, String>();
		
		for(ElementoSelecao elemento : listaUnidadeRadio)
		{
			String cnes = elemento.getText().substring(0, 7);
			String value = elemento.getValue();
			
			elementosRadioUnidades.put(cnes, value);
		}
				
		for(EntidadeCDRNaoRegulada entidade : entidades)
		{
			driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
			
			
			String value = elementosRadioUnidades.get(entidade.getCNES());
			
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
				
				//buscando arquivos e baixando
				paginaWeb.voltarAoTopoDaPagina(driver);
				paginaWeb.clicarMenuUL(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes);
				paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				baixarArquivos(driver, paginaWeb, entidade);
				
			}
			else
				System.out.println("Unidade não encontrada: " + entidade.getCNES() + " - " + entidade.getUnidade() + "(" + entidade.getDistrito() + ")");
		}
		
		return "";	
	}
	

	private String baixarArquivos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeCDRNaoRegulada entidade) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		String[] tiposDeBusca = new String[2];
		tiposDeBusca[0] = "Consulta";
		tiposDeBusca[1] = "Exame";
		
		for(int i = 0; i < tiposDeBusca.length; i++)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_FILTRO_TIPO_CONSULTA_EXAME.getTextoIdentificador(), tiposDeBusca[i]);
			paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_LINK_SELECIONE.getTextoIdentificador());
			
			ArrayList<ElementoSelecao> opcoes = paginaWeb.obterItensDeUmSelect(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_LISTAGEM_ESCOLHER_ESPECIALIDADE.getTextoIdentificador());
			
			for(ElementoSelecao opcao : opcoes)
			{
				paginaWeb.selecionarItemSelectPeloValue(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_LISTAGEM_ESCOLHER_ESPECIALIDADE.getTextoIdentificador(), opcao.getValue());
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_BOTAO_MOVER_SELECIONADOS_PARA_DIREITA.getTextoIdentificador(), "id");
				
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_BOTAO_ACEITAR_SELECIONADOS.getTextoIdentificador(), "id");
				
				do
				{
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_BOTAO_DOWNLOAD.getTextoIdentificador(), "id");
			}
			
//			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_AGENDAMENTOS_BOTAO_DOWNLOAD.getTextoIdentificador(), "id");
//			
//			String arquivoMaisRecente;
//			
//			do
//			{
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				arquivoMaisRecente = pastaOrigem.arquivoRecentementeModificado();
//				
//				System.out.println(arquivoMaisRecente + " ----- " + ultimoRecente);
//			}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(ParametrosArquivoFilasNominais.EXTENSAO_ARQUIVO_REGULADA_AGENDAMENTO.getDescricao()));
//			
//			Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);
//			arquivo.renomear(ParametrosArquivoFilasNominais.PREFIXO_NOME_ARQUIVO_REGULADA_AGENDAMENTO.getDescricao() + " - " + tiposDeBusca[i].toUpperCase() + " " + arquivoMaisRecente);
//			
//			transferirArquivos(entidade, tiposDeBusca[i], arquivo);
//			
//			ultimoRecente = arquivo.getNomeDoArquivo();
		}
		

		return "";
	}
	
	private String transferirArquivos(EntidadeCDRNaoRegulada entidade, String tipoDeBusca, Arquivo arquivo)
	{
		
		Pasta pasta = new Pasta(pastaDestinoArquivos, true);
			
		String pastaEntidade = pastaDestinoArquivos + "\\" + anoCompetencia;
		pasta = new Pasta(pastaEntidade, true);
			
		pastaEntidade = pastaEntidade + "\\" + meses.getMeses().get(mesCompetencia - 1).getMesNumero() + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + " " + anoCompetencia;
		pasta = new Pasta(pastaEntidade, true);
		
		pastaEntidade = pastaEntidade + "\\" + dataFormatadaPasta;
		pasta = new Pasta(pastaEntidade, true);
		
		pastaEntidade = pastaEntidade + "\\" + entidade.getDistrito();
		pasta = new Pasta(pastaEntidade, true);
		
		pastaEntidade = pastaEntidade + "\\" + tipoDeBusca.toUpperCase();
		pasta = new Pasta(pastaEntidade, true);
			
		arquivo.mover(pastaEntidade + "\\" + arquivo.getNomeDoArquivo());
			
				
		return "";
	}
	
}
