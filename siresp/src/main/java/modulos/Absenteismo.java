package modulos;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoFilasNominais;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import interacao_externa.ConversaoHMTL_XLSX;
import modelosDados.CelulaExcel;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeAbsenteismo;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeLeito;
import modelosDados.LinhaCensoLeitos;
import modelosDados.MesFormatado;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class Absenteismo {
	
	private int mesCompetencia;
	private int anoCompetencia;
	private int mesReferencia;
	private int anoReferencia;
	private String pastaDestinoArquivos;
	private String pastaDownloads;
	private MesesFormatados meses;	
	private DateTimeFormatter formatoDataPaginaWeb;
	private DateTimeFormatter formatoDataArquivo;
	LocalDate dataInicioReferencia;
	LocalDate dataFinalReferencia;
	String dataFormatadaInicioReferencia;
	String dataFormatadaFinalReferencia;

	public String verificarAbsenteismo(WebDriver driver)
	{			
		formatoDataPaginaWeb = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		formatoDataArquivo = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Relatório");
		opcoes.add("Pacientes  >>");
		opcoes.add("PC02 - Paciente X Solicitante");

		
		//definindo a formatação dos meses para permitir que seja possível criar a estrutura das pastas
		meses = new MesesFormatados();
		
		pastaDestinoArquivos = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados de Absenteismo", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
		pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE).trim();
		String mes = JOptionPane.showInputDialog(null, "Qual o mês de referência?", "Mês de Referência", JOptionPane.QUESTION_MESSAGE).trim();
		String ano = JOptionPane.showInputDialog(null, "Qual o ano de referência?", "Ano de Referência", JOptionPane.QUESTION_MESSAGE).trim();
		
		mesReferencia = Integer.parseInt(mes);
		anoReferencia = Integer.parseInt(ano);
		
		
		
		if(mesReferencia < 10)
			dataFormatadaInicioReferencia = "01-0" + mes + "-" + ano;
		else
			dataFormatadaInicioReferencia = "01-" + mes + "-" + ano;
		
		dataInicioReferencia = LocalDate.parse(dataFormatadaInicioReferencia, formatoDataPaginaWeb);
		
		dataFinalReferencia = dataInicioReferencia.with(TemporalAdjusters.lastDayOfMonth());
		dataFormatadaFinalReferencia = dataFinalReferencia.format(formatoDataPaginaWeb);
		
		//System.out.println(dataFormatadaPasta);
		
		if(mesReferencia == 12)
		{
			mesCompetencia = 1;
			anoCompetencia = anoReferencia + 1;
		}
		else
		{
			mesCompetencia = mesReferencia + 1;
			anoCompetencia = anoReferencia;
		}
		
		
		//definindo entidades para o censo de leitos
		ArrayList<EntidadeAbsenteismo> entidades = lerEntidades(pastaDestinoArquivos + "\\unidadessolicitantes.csv");
		
//		for(EntidadeAbsenteismo entidade : entidades)
//		{
//			String nomeDoArquivo = "C:\\Users\\PMC514991-2\\Documents\\Absenteismo\\Absenteísmo\\2026\\" + entidade.getNomeArquivoAbsenteismo();
//			AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(nomeDoArquivo, 0);
//			
//			for(MesFormatado mes : meses.getMeses())
//			{
//				arquivoExcel.abrirPlanilha(mes.getMesDescricaoSemAcentuacao(), anoCompetencia);
//				ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
//				celulas.add(new CelulaExcel(8, 7, entidade.getNomeArquivoAbsenteismo().replace(".xlsx", ""), nomeDoArquivo));
//				arquivoExcel.gravarDadosEmCelula(mes.getMesDescricaoSemAcentuacao(), celulas);
//			}
//		}
		
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		ArrayList<ElementoSelecao> listaUnidadeRadio = paginaWeb.getListaDeOpcoesRadioPorName(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_RADIO_UNIDADES.getTextoIdentificador());
		
		HashMap<String, String> elementosRadioUnidades = new HashMap<String, String>();
		
		for(ElementoSelecao elemento : listaUnidadeRadio)
		{
			String cnes = elemento.getText().substring(0, 7);
			String value = elemento.getValue();
			
			int posicaoPerfilDeAcesso = elemento.getText().indexOf(" - Administrador Unidade Reg");
			
			if(posicaoPerfilDeAcesso > 0)
			{
				String composicaoCNESNomeUnidade = elemento.getText().substring(0, posicaoPerfilDeAcesso);
				elementosRadioUnidades.put(composicaoCNESNomeUnidade, value);
				
				//System.out.println("CNES: " + cnes + "| Value: " + value + "| Composição: " + composicaoCNESNomeUnidade + "|");
			}

		}
		
		
		for(EntidadeAbsenteismo entidade : entidades)
		{
			driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
			
			
			String value = elementosRadioUnidades.get(entidade.getCNES() + " - " + entidade.getNomeUnidadeSIRESP());
			//System.out.println(value);
			
			
			if(value != null)
			{
				paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
			
				boolean visivel;
				do
				{

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
				

					//visivel = paginaWeb.clicarMenuUL(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes);
				
					visivel = paginaWeb.clicarMenuUL(driver, 2, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes, OpenStrategy.HOVER);
					
				
				}while(!visivel);
				
				paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				montarAbsenteismo(driver, paginaWeb, entidade);
				
			}
			else
				System.out.println("Unidade não encontrada: " + entidade.getCNES() + " - " + entidade.getUnidade() + "(" + entidade.getDistrito() + ")");
		}
		
		return "";	
	}
	
	private ArrayList<EntidadeAbsenteismo> lerEntidades(String nomeArquivo)
	{
		ArrayList<EntidadeAbsenteismo> entidades = new ArrayList();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
                String cnes = registro.get("CNES");
                String unidade = registro.get("Unidade");
                String distrito = registro.get("Distrito");
                String nomeUnidadeSIRESP = registro.get("Nome SIRESP");
                String nomeArquivoAbsenteismo = registro.get("Nome Arquivo") + ".xlsx";
                
                entidades.add(new EntidadeAbsenteismo(cnes, unidade, distrito, nomeUnidadeSIRESP, nomeArquivoAbsenteismo));
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	private String montarAbsenteismo(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeAbsenteismo entidade) 
	{
		Pasta pastaOrigem = new Pasta(pastaDownloads, false);
		String ultimoRecente = pastaOrigem.arquivoRecentementeModificado();
		
		String[] tiposDeBusca = new String[2];
		tiposDeBusca[0] = "Consulta";
		tiposDeBusca[1] = "Exame";
		
				
		for(int i = 0; i < tiposDeBusca.length; i++)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_ABSENTEISMO_FILTRO_TIPO_CONSULTA_EXAME.getTextoIdentificador(), tiposDeBusca[i]);
			
			
			ArrayList<ElementoSelecao> elementosSelectExecutantes = paginaWeb.obterItensDeUmSelect(driver, IdentificadoresPaginaWebSIRESP.ID_ABSENTEISMO_FILTRO_UNIDADE_EXECUTANTE.getTextoIdentificador());
			
			for(ElementoSelecao elemento : elementosSelectExecutantes)
			{
				if(!elemento.getText().equals(IdentificadoresPaginaWebSIRESP.TEXTO_ABSENTEISMO_SELECIONE_ITEM_UNIDADE_EXECUTANTE.getTextoIdentificador()))
				{
					paginaWeb.selecionarItemSelectPeloValue(driver, IdentificadoresPaginaWebSIRESP.ID_ABSENTEISMO_FILTRO_UNIDADE_EXECUTANTE.getTextoIdentificador(), elemento.getValue());
					
					paginaWeb.limparInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_INICIAL.getTextoIdentificador());
					paginaWeb.preencherInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_INICIAL.getTextoIdentificador(), dataFormatadaInicioReferencia.replaceAll("-", ""));
					
					paginaWeb.limparInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_FINAL.getTextoIdentificador());
					paginaWeb.preencherInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_FINAL.getTextoIdentificador(), dataFormatadaFinalReferencia.replaceAll("-", ""));
					
					paginaWeb.selecionarItemSelectPeloValue(driver, IdentificadoresPaginaWebSIRESP.ID_ABSENTEISMO_FILTRO_ORDENACAO_RELATORIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.VALOR_ABSENTEISMO_ORDENACAO_RELATORIO_DATA_HORA_AGENDAMENTO.getTextoIdentificador());
					
					paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_BOTAO_BUSCAR.getTextoIdentificador(), "name");
					
					do
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
					
					if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_ABSENTEISMO_MENSAGEM_NENHUM_REGISTRO_ENCONTADO.getTextoIdentificador()))
						System.out.println("Não baixar:" + entidade.getNomeArquivoAbsenteismo() + " - " + elemento.getText() + " - " + tiposDeBusca);
					else
						System.out.println("Baixar:" + entidade.getNomeArquivoAbsenteismo() + " - " + elemento.getText() + " - " + tiposDeBusca);
				}
			}
			
			
			
			
//			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_AMBULATORIAL_CDR_BOTAO_DOWNLOAD.getTextoIdentificador(), "name");
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
//			}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(ParametrosArquivoFilasNominais.EXTENSAO_ARQUIVO_CDR.getDescricao()));
//			
//			Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);
//			arquivo.renomear(entidade.getUnidade() + " - " + tiposDeBusca[i].toUpperCase() + " " + arquivoMaisRecente);
//			
//			transferirArquivos(entidade, tiposDeBusca[i], arquivo);
//			
//			ultimoRecente = arquivo.getNomeDoArquivo();
		}
		

		return "";
	}
	
	private String transferirArquivos(EntidadeAbsenteismo entidade, String tipoDeBusca, Arquivo arquivo)
	{
		
		Pasta pasta = new Pasta(pastaDestinoArquivos, true);
			
		String pastaEntidade = pastaDestinoArquivos + "\\" + anoCompetencia;
		pasta = new Pasta(pastaEntidade, true);
			
		pastaEntidade = pastaEntidade + "\\" + meses.getMeses().get(mesCompetencia - 1).getMesNumero() + " " + meses.getMeses().get(mesCompetencia - 1).getMesDescricao() + " " + anoCompetencia;
		pasta = new Pasta(pastaEntidade, true);
		
		//pastaEntidade = pastaEntidade + "\\" + dataFormatadaPasta;
		pasta = new Pasta(pastaEntidade, true);
		
		pastaEntidade = pastaEntidade + "\\" + entidade.getDistrito();
		pasta = new Pasta(pastaEntidade, true);
		
		pastaEntidade = pastaEntidade + "\\" + tipoDeBusca.toUpperCase();
		pasta = new Pasta(pastaEntidade, true);
			
		arquivo.mover(pastaEntidade + "\\" + arquivo.getNomeDoArquivo());
			
				
		return "";
	}
	
}
