package modulos;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

import dadosGerais.CorrelacaoArquivosAbsenteismo;
import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoAbsenteismoConsolidado;
import dadosGerais.ParametrosArquivoAbsenteismoConsultaBaixado;
import dadosGerais.ParametrosArquivoAbsenteismoExameBaixado;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoFilasNominais;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import interacao_externa.ConversaoHMTL_XLSX;
import modelosDados.CelulaExcel;
import modelosDados.CorrelacaoColunasArquivosAbsenteismo;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeAbsenteismo;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeExecutante;
import modelosDados.EntidadeExecutanteR1;
import modelosDados.EntidadeLeito;
import modelosDados.LinhaCensoLeitos;
import modelosDados.MesFormatado;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class OfertaDemandaDeAcessoR1 {
	
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
	LocalDate dataInicioCompetencia;
	LocalDate dataFinalCompetencia;
	String dataFormatadaInicioReferencia;
	String dataFormatadaFinalReferencia;
	String dataFormatadaInicioCompetencia;
	String dataFormatadaFinalCompetencia;

	public String calcularOfertaEDemanda(WebDriver driver)
	{			
		formatoDataPaginaWeb = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		formatoDataArquivo = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		

		
		//definindo a formatação dos meses para permitir que seja possível criar a estrutura das pastas
		meses = new MesesFormatados();
		
		
		String[] opcoesRotina = {"Executar rotina completa", "Executar apenas consolidação"}; 
        int escolhaRotina = JOptionPane.showOptionDialog( null, "O que deseja fazer?", "Rotinas", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoesRotina, opcoesRotina[0] );
        
		pastaDestinoArquivos = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados de Oferta e Demanda", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
		pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE).trim();
		String mes = JOptionPane.showInputDialog(null, "Qual o mês de análise?", "Mês de Referência", JOptionPane.QUESTION_MESSAGE).trim();
		String ano = JOptionPane.showInputDialog(null, "Qual o ano de análise?", "Ano de Referência", JOptionPane.QUESTION_MESSAGE).trim();
		
		mesCompetencia = Integer.parseInt(mes);
		anoCompetencia = Integer.parseInt(ano);
		
			
		if(mesCompetencia == 1)
		{
			mesReferencia = 12;
			anoReferencia = anoCompetencia - 1;
		}
		else
		{
			mesReferencia = mesCompetencia + 1;
			anoReferencia = anoCompetencia;
		}
		
		if(mesReferencia < 10)
			dataFormatadaInicioReferencia = "01-0" + mesReferencia + "-" + anoReferencia;
		else
			dataFormatadaInicioReferencia = "01-" + mesReferencia + "-" + anoReferencia;
		
		dataInicioReferencia = LocalDate.parse(dataFormatadaInicioReferencia, formatoDataPaginaWeb);
		
		dataFinalReferencia = dataInicioReferencia.with(TemporalAdjusters.lastDayOfMonth());
		dataFormatadaFinalReferencia = dataFinalReferencia.format(formatoDataPaginaWeb);
		
		//System.out.println(dataFormatadaPasta);
		
		
		
		if(mesCompetencia < 10)
			dataFormatadaInicioCompetencia = "01/0" + mesCompetencia + "/" + anoCompetencia;
		else
			dataFormatadaInicioCompetencia = "01/" + mesCompetencia + "/" + anoCompetencia;
		
		dataInicioCompetencia = LocalDate.parse(dataFormatadaInicioCompetencia, formatoDataArquivo);
		
		dataFinalCompetencia = dataInicioCompetencia.with(TemporalAdjusters.lastDayOfMonth());
		dataFormatadaFinalCompetencia = dataFinalCompetencia.format(formatoDataArquivo);
		
		//definindo entidades para o censo de leitos
		ArrayList<EntidadeExecutanteR1> entidades = lerEntidadesR1(pastaDestinoArquivos + "\\unidadesExecutantes.csv");
		
	
		pastaDestinoArquivos = pastaDestinoArquivos + "\\Absenteísmo\\" + anoCompetencia + "\\";
		
		if(escolhaRotina == 0)
		{
		
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
			
			for(EntidadeExecutanteR1 entidade : entidades)
			{
				driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
				
				ArrayList<String> opcoes = new ArrayList<>();
				opcoes.add("Relatório");
				opcoes.add("Produtividade  >>");
				
				if(entidade.getVinculo().equals(IdentificadoresPaginaWebSIRESP.TEXTO_VINCULO_ESTADUAL.getTextoIdentificador()))
					opcoes.add("P01 - Produção Executante");
				else
					opcoes.add("P06 - Consolidado");
				
				
				String value = elementosRadioUnidades.get(entidade.getCNES() + " - " + entidade.getNomeUnidadeSIRESP());
				//System.out.println(value);
				
				
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
					
					boolean visivel;
					do
					{
					
						visivel = acessarMenu(driver, paginaWeb, opcoes);
						
					
					}while(!visivel);
					
					paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					montarOfertaDemanda(driver, paginaWeb, entidade);
					
				}
				else
					System.out.println("Unidade não encontrada: " + entidade.getCNES() + " - " + entidade.getUnidade());
			}
		}
		
		consolidarArquivoMunicipal(entidades, mesCompetencia, anoCompetencia);
		
		return "";	
	}
	
	private ArrayList<EntidadeExecutanteR1> lerEntidadesR1(String nomeArquivo)
	{
		ArrayList<EntidadeExecutanteR1> entidades = new ArrayList<EntidadeExecutanteR1>();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
            	String cnes = registro.get("CNES");
            	String vinculo = registro.get("VINCULO");
                String unidade = registro.get("EXECUTANTE");
                String nomeSIRESP = registro.get("NOME SIRESP");
               
                entidades.add(new EntidadeExecutanteR1(cnes, vinculo, unidade, nomeSIRESP));
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	
	private String montarOfertaDemanda(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade) 
	{

		String[] tiposDeBusca = new String[2];
		
		if(entidade.getVinculo().equals(IdentificadoresPaginaWebSIRESP.TEXTO_VINCULO_ESTADUAL.getTextoIdentificador()))
		{
			montarRelatorioDeProdutividadeEstadual(driver, paginaWeb, entidade);
		}
		else
		{
			montarRelatorioDeProdutividadeOutros(driver, paginaWeb, entidade);
		}
		

		
		return "";
		
	}
	
	private String montarRelatorioDeProdutividadeEstadual(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade) 
	{
		String[] tiposDeBusca = new String[2];

		tiposDeBusca[0] = "Consulta";
		tiposDeBusca[1] = "Exame";
		
		for(String tipoDeBusca : tiposDeBusca)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_PRODUCAO_EXECUTANTE_TIPO_RELATORIO.getTextoIdentificador(), tipoDeBusca);
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_PRIMEIRO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_DRSVII_CAMPINAS.getTextoIdentificador());
			
			while(!paginaWeb.elementoEstaHabilitadoPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador()))
			{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			boolean possuiSMSCampinas = paginaWeb.verificarExistenciaDeTextoEmUmSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			
			if(possuiSMSCampinas) 
			{
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			}
			else
			{
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_TERCEIRO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_REGIAO_METROPOLITANO_CAMPINAS.getTextoIdentificador());
				
				while(!paginaWeb.elementoEstaHabilitadoPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_TERCEIRO_NIVEL.getTextoIdentificador()))
				{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_TERCEIRO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
				
				paginaWeb.MarcarElementoCheckBoxPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_EXIBIR_RECEPCAO.getTextoIdentificador());
				
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_PRODUCAO_EXECUTANTE_BOTAO_BUSCAR.getTextoIdentificador(), "id");
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
				
				if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_PRODUCAO_EXECUTANTE_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
				{
					ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_PRODUCAO_EXECUTANTE_TABELA_RESULTADOS.getTextoIdentificador());
					System.out.println("Tabela encontrada");
				}
			}
		}
		
		return "";
	}
	
	private String montarRelatorioDeProdutividadeOutros(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade) 
	{
		String[] tiposDeBusca = new String[2];
		
		tiposDeBusca[0] = IdentificadoresPaginaWebSIRESP.TEXTO_CONSOLIDADO_MENSAL_RELATORIO_CONSULTAS.getTextoIdentificador();
		tiposDeBusca[1] = IdentificadoresPaginaWebSIRESP.TEXTO_CONSOLIDADO_MENSAL_RELATORIO_EXAMES.getTextoIdentificador();
		
		for(String tipoDeBusca : tiposDeBusca)
		{
			
		}
		
		return "";
	}
	
	public String consolidarArquivoMunicipal(ArrayList<EntidadeExecutanteR1> entidades, int mesCompetencia, int anoCompetencia)
	{

		
		return "";
	}
	
	public String parametrizarArquivosVazios(WebDriver driver)
	{			

		
		return "";	
	}
	
	public boolean acessarMenu(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, ArrayList<String> opcoes)
	{
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
		
		boolean visivel;
		do
		{
			//buscando arquivos e baixando
			paginaWeb.voltarAoTopoDaPagina(driver);
		
			//visivel = paginaWeb.clicarMenuUL(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes);
		
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			visivel = paginaWeb.clicarMenuUL(driver, 2, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes, OpenStrategy.HOVER);
			
		
		}while(!visivel);
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return visivel;
	}
	
}
