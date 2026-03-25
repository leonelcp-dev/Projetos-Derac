package modulos;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import dadosGerais.CorrelacaoArquivosAbsenteismo;
import dadosGerais.CorrelacaoArquivosOfertaDemanda;
import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoAbsenteismoConsolidado;
import dadosGerais.ParametrosArquivoAbsenteismoConsultaBaixado;
import dadosGerais.ParametrosArquivoAbsenteismoExameBaixado;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoFilasNominais;
import dadosGerais.ParametrosArquivoOfertaDemanda;
import dadosGerais.ParametrosTabelaProducaoConsolidadoConsultas;
import dadosGerais.ParametrosTabelaProducaoConsolidadoExames;
import dadosGerais.ParametrosTabelaProducaoExecutanteConsultas;
import dadosGerais.ParametrosTabelaProducaoExecutanteExames;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import modelosDados.CelulaExcel;
import modelosDados.CorrelacaoColunasArquivosAbsenteismo;
import modelosDados.CorrelacaoColunasOfertasDemandas;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeAbsenteismo;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeExecutante;
import modelosDados.EntidadeExecutanteR1;
import modelosDados.EntidadeLeito;
import modelosDados.LinhaCensoLeitos;
import modelosDados.MesFormatado;
import modelosDados.OfertaEDemanda;
import modelosDados.UsuarioSIRESP;
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
	
	HashMap<String, HashMap<String, OfertaEDemanda>> ofertasDemandasProcessadas;

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

		ArrayList<OfertaEDemanda> ofertasEDemandasJaRegistradas = lerOfertasJaProcessadas(pastaDestinoArquivos + "\\ConsolidadoOfertaEDemanda.xlsx", ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice() - 1);
		ofertasDemandasProcessadas = new HashMap<String, HashMap<String, OfertaEDemanda>>();
		
		int linhaArquivo = ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice();
		
		for(OfertaEDemanda oferta : ofertasEDemandasJaRegistradas)
		{
			oferta.setCompetencia(normalizarDataParaMesAno(oferta.getCompetencia()));
			
			if(ofertasDemandasProcessadas.containsKey(oferta.getUnidade() + oferta.getCompetencia()))
			{
				HashMap<String, OfertaEDemanda> mapaEspecialidade = ofertasDemandasProcessadas.get(oferta.getUnidade() + oferta.getCompetencia());
				
				if(mapaEspecialidade.containsKey(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase()))
				{
					OfertaEDemanda ofertaEncontrada = mapaEspecialidade.get(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase());
					ofertaEncontrada.setLinhaExcel(linhaArquivo);
				}
				else
				{
					oferta.setLinhaExcel(linhaArquivo);
					mapaEspecialidade.put(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase(), oferta);
				}
			}
			else
			{
				HashMap<String, OfertaEDemanda> mapaEspecialidade = new HashMap<String, OfertaEDemanda>();
				oferta.setLinhaExcel(linhaArquivo);
				mapaEspecialidade.put(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase(), oferta);
				
				ofertasDemandasProcessadas.put(oferta.getUnidade() + oferta.getCompetencia(), mapaEspecialidade);
			}
			
			linhaArquivo++;
		}
		
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
					
					//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
					
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
		String[][] tiposDeBusca = new String[2][2];

		tiposDeBusca[0][0] = "Consulta";
		tiposDeBusca[1][0] = "Exame";
		tiposDeBusca[0][1] = Integer.toString(ParametrosTabelaProducaoExecutanteConsultas.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		tiposDeBusca[1][1] = Integer.toString(ParametrosTabelaProducaoExecutanteExames.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		
		for(String[] tipoDeBusca : tiposDeBusca)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_PRODUCAO_EXECUTANTE_TIPO_RELATORIO.getTextoIdentificador(), tipoDeBusca[0]);
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_PRIMEIRO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_DRSVII_CAMPINAS.getTextoIdentificador());
			
			while(!paginaWeb.elementoEstaHabilitadoPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador()))
			{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Loop segundo nível");
			}
			
			System.out.println("Saiu do loop segundo nível");
			
			boolean possuiSMSCampinas = paginaWeb.verificarExistenciaDeTextoEmUmSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			
			if(possuiSMSCampinas) 
			{
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			}
			else
			{
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_SEGUNDO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_REGIAO_METROPOLITANO_CAMPINAS.getTextoIdentificador());
				
				while(!paginaWeb.elementoEstaHabilitadoPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_TERCEIRO_NIVEL.getTextoIdentificador()))
				{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Loop terceiro nível");
				}
				
				System.out.println("Saiu do loop terceiro nível");
				paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_UNIDADE_SOLICITANTE_TERCEIRO_NIVEL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_PRODUCAO_EXECUTANTE_SMS_CAMPINAS.getTextoIdentificador());
			}
				
			paginaWeb.MarcarElementoCheckBoxPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_EXIBIR_RECEPCAO.getTextoIdentificador());
			
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_FILTRO_MES.getTextoIdentificador(), meses.getMeses().get(mesCompetencia - 1).getMesDescricaoPrimeiraMaiuscula());
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_PRODUCAO_EXECUTANTE_FILTRO_ANO.getTextoIdentificador(), Integer.toString(anoCompetencia));
			
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_PRODUCAO_EXECUTANTE_BOTAO_BUSCAR.getTextoIdentificador(), "id");
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_PRODUCAO_EXECUTANTE_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
			{
				ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_PRODUCAO_EXECUTANTE_TABELA_RESULTADOS.getTextoIdentificador());
				System.out.println("Tabela encontrada");
				preencherDadosDeProdutividade(driver, paginaWeb, entidade, tipoDeBusca[0], tabelaResultados, Integer.parseInt(tipoDeBusca[1]));
			}
			else
			{
				
			}
		}
		
		return "";
	}
	
	private String montarRelatorioDeProdutividadeOutros(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade) 
	{
		String[][] tiposDeBusca = new String[2][3];
		
		
		tiposDeBusca[0][0] = "Consulta";
		tiposDeBusca[1][0] = "Exame";		
		tiposDeBusca[0][1] = Integer.toString(ParametrosTabelaProducaoConsolidadoConsultas.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		tiposDeBusca[1][1] = Integer.toString(ParametrosTabelaProducaoConsolidadoExames.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice());
		tiposDeBusca[0][2] = IdentificadoresPaginaWebSIRESP.TEXTO_CONSOLIDADO_MENSAL_RELATORIO_CONSULTAS.getTextoIdentificador();
		tiposDeBusca[1][2] = IdentificadoresPaginaWebSIRESP.TEXTO_CONSOLIDADO_MENSAL_RELATORIO_EXAMES.getTextoIdentificador();
		
		for(String[] tipoDeBusca : tiposDeBusca)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_CONSOLIDADO_MENSAL_TIPO_RELATORIO.getTextoIdentificador(), tipoDeBusca[2]);
			
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_CONSOLIDADO_MENSAL_FILTRO_MES.getTextoIdentificador(), meses.getMeses().get(mesCompetencia - 1).getMesDescricaoPrimeiraMaiuscula());
			paginaWeb.selecionarItemSelectPeloName(driver, IdentificadoresPaginaWebSIRESP.NAME_CONSOLIDADO_MENSAL_FILTRO_ANO.getTextoIdentificador(), Integer.toString(anoCompetencia));
			
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_CONSOLIDADO_MENSAL_BOTAO_BUSCAR.getTextoIdentificador(), "id");
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_CONSOLIDADO_MENSAL_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
			{
				ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_CONSOLIDADO_MENSAL_TABELA_RESULTADOS_CONSULTAS.getTextoIdentificador());
				System.out.println("Tabela encontrada");
				preencherDadosDeProdutividade(driver, paginaWeb, entidade, tipoDeBusca[0], tabelaResultados, Integer.parseInt(tipoDeBusca[1]));
			}
			else
			{
				
			}
		}
		
		return "";
	}
	
	private void preencherDadosDeProdutividade(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, EntidadeExecutanteR1 entidade, String tipoDeBusca, ArrayList<ArrayList<String>> tabelaResultados, int quantidadeEsperadaDeColunas)
	{
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaDestinoArquivos + "\\ConsolidadoOfertaEDemanda.xlsx", 0);
		
		arquivoConsolidado.abrirPlanilha(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), anoCompetencia);
		int ultimaLinhaLivre = arquivoConsolidado.getPrimeiraLinhaVazia() + 1;
		int linhaExcel;
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		String inicioCompetenciaFormatado = dataInicioCompetencia.format(formatter);
		
		CorrelacaoArquivosOfertaDemanda correlacoes = new CorrelacaoArquivosOfertaDemanda();
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		ArrayList<CorrelacaoColunasOfertasDemandas> colunasConsolidado = correlacoes.obterCorrelacaoEntreArquivos(tipoDeBusca, entidade.getVinculo());
		
		for(ArrayList<String> linhaDaTabela : tabelaResultados)
		{
			if(linhaDaTabela.size() >= quantidadeEsperadaDeColunas && !linhaDaTabela.get(0).trim().equals("Total"))
			{
				HashMap<String, OfertaEDemanda> mapaEspecialidade = null;
				OfertaEDemanda oferta = null;
				String especialidade = linhaDaTabela.get(colunasConsolidado.get(0).getColunaSIRESP().get(0));
				
				if(ofertasDemandasProcessadas.containsKey(entidade.getExecutante() + inicioCompetenciaFormatado))
				{
					mapaEspecialidade = ofertasDemandasProcessadas.get(entidade.getExecutante() + inicioCompetenciaFormatado);
					if(mapaEspecialidade.containsKey(tipoDeBusca + especialidade.toUpperCase()))
					{
						oferta = mapaEspecialidade.get(tipoDeBusca + especialidade.toUpperCase());
						linhaExcel = mapaEspecialidade.get(tipoDeBusca + especialidade.toUpperCase()).getLinhaExcel();
					}
					else
					{
						oferta = new OfertaEDemanda();
						mapaEspecialidade = new HashMap<String, OfertaEDemanda>();
						linhaExcel = ultimaLinhaLivre;
						ultimaLinhaLivre++;
					}
				}
				else
				{
					oferta = new OfertaEDemanda();
					mapaEspecialidade = new HashMap<String, OfertaEDemanda>();
					ofertasDemandasProcessadas.put(entidade.getExecutante() + inicioCompetenciaFormatado, mapaEspecialidade);
					linhaExcel = ultimaLinhaLivre;
					ultimaLinhaLivre++;
				}
				
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_UNIDADE.getIndice(), entidade.getExecutante(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_VINCULO.getIndice(), entidade.getVinculo(), "String"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_COMPETENCIA.getIndice(), dataInicioCompetencia, "Date mes/ano"));
				celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_TIPO_OFERTA.getIndice(), tipoDeBusca, ""));
				
				System.out.print(linhaExcel + "(T: " + linhaDaTabela.size() + ") E: (" + quantidadeEsperadaDeColunas + ") - ");
				for(String celula : linhaDaTabela)
					System.out.print(celula + "\t");
				System.out.println();
				
				for(CorrelacaoColunasOfertasDemandas correlacao : colunasConsolidado)
				{
						
					if(correlacao.getColunaSIRESP() == null)
						celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), "-", "String"));
					else
					{
						if(correlacao.getTipo().equals("String")) 
						{
							String valor = "";
							for(int indice : correlacao.getColunaSIRESP())
								valor += linhaDaTabela.get(indice);
							
							celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), valor, correlacao.getTipo()));
						}
						else
						{
							int divisor = 0;
							
							if(correlacao.getColunasDivisao().size() > 0)
							{
								for(int indice : correlacao.getColunasDivisao())
									if(!linhaDaTabela.get(indice).trim().equals(""))
										divisor += Integer.parseInt(linhaDaTabela.get(indice).replace(".", "").trim());
							}
							else
								divisor = 1;
							
							if(divisor == 0)
							{
								celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), "-", "String"));
							}
							else
							{				
								int soma = 0;
								
								for(int indice : correlacao.getColunaSIRESP())
									if(!linhaDaTabela.get(indice).trim().equals(""))
										soma += Integer.parseInt(linhaDaTabela.get(indice).replace(".", "").trim());
								
								int subtraendo = 0;
								
								for(int indice : correlacao.getColunasSubtracao())
									if(!linhaDaTabela.get(indice).trim().equals(""))
										subtraendo += Integer.parseInt(linhaDaTabela.get(indice).replace(".", "").trim());
								
								if(correlacao.getTipo().equals("Int"))
								{
									int resultado = (soma - subtraendo) / divisor;
									celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), resultado, correlacao.getTipo()));
								}
								else
								{
									double resultado = 1.0 * (soma - subtraendo) / divisor;
									celulas.add(new CelulaExcel(linhaExcel, correlacao.getColunaConsolidado(), resultado, correlacao.getTipo()));
								}
							}
						}
					}
				}
				montarObjetoOferta(oferta, entidade, tipoDeBusca, dataInicioCompetencia, celulas);
				
				if(mapaEspecialidade != null)
				{
					mapaEspecialidade.put(oferta.getTipoDeOferta() + oferta.getEspecialidade().toUpperCase(), oferta);
				}
			}
		}
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoOfertaDemanda.NOME_PLANILHA_CONSOLIDADA.getDescricao(), celulas, true, false, ParametrosArquivoOfertaDemanda.LINHA_INICIAL_ARQUIVO.getIndice(), null);
	}
	
	private OfertaEDemanda montarObjetoOferta(OfertaEDemanda oferta, EntidadeExecutanteR1 entidade, String tipoOferta, LocalDate dataInicioCompetencia, ArrayList<CelulaExcel> celulas)
	{
		oferta.setUnidade(entidade.getUnidade());
		oferta.setVinculo(entidade.getVinculo());
		
		Locale localeBR = Locale.of("pt", "BR");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
		oferta.setCompetencia(dataInicioCompetencia.format(formatter));
		
		oferta.setTipoDeOferta(tipoOferta);
		oferta.setEspecialidade(celulas.get(0).getValor().toString());
		
		oferta.setOfertaTotal(celulas.get(1).getTipo().equals("Int")?String.valueOf(celulas.get(1).getValor()):celulas.get(1).toString());
		oferta.setAgendamentoTotal(celulas.get(2).getTipo().equals("Int")?String.valueOf(celulas.get(2).getValor()):celulas.get(2).toString());
		oferta.setAgendamentoCota(celulas.get(3).getTipo().equals("Int")?String.valueOf(celulas.get(3).getValor()):celulas.get(3).toString());
		oferta.setAgendamentoBolsao(celulas.get(4).getTipo().equals("Int")?String.valueOf(celulas.get(4).getValor()):celulas.get(4).toString());
		oferta.setAgendamentoNaoDistribuido(celulas.get(5).getTipo().equals("Int")?String.valueOf(celulas.get(5).getValor()):celulas.get(5).toString());
		oferta.setAgendamentoExtra(celulas.get(6).getTipo().equals("Int")?String.valueOf(celulas.get(6).getValor()):celulas.get(6).toString());
		oferta.setRecepcaoAtendido(celulas.get(7).getTipo().equals("Int")?String.valueOf(celulas.get(7).getValor()):celulas.get(7).toString());
		oferta.setRecepcaoAusente(celulas.get(8).getTipo().equals("Int")?String.valueOf(celulas.get(8).getValor()):celulas.get(8).toString());
		oferta.setRecepcaoAusenteCalculado(celulas.get(9).getTipo().equals("Int")?String.valueOf(celulas.get(9).getValor()):celulas.get(9).toString());
		oferta.setRecepcaoDesistencia(celulas.get(10).getTipo().equals("Int")?String.valueOf(celulas.get(10).getValor()):celulas.get(10).toString());
		oferta.setRecepcaoDispensado(celulas.get(11).getTipo().equals("Int")?String.valueOf(celulas.get(11).getValor()):celulas.get(11).toString());
		oferta.setRecepcaoNaoInformado(celulas.get(12).getTipo().equals("Int")?String.valueOf(celulas.get(12).getValor()):celulas.get(12).toString());
		
		oferta.setLinhaExcel(celulas.get(0).getLinha());
		
		return oferta;
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
		paginaWeb.voltarAoTopoDaPagina(driver);	
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
	
	private ArrayList<OfertaEDemanda> lerOfertasJaProcessadas(String nomeArquivo, String planilha, int linhaCabecalho)
	{		
		ArrayList<OfertaEDemanda> ofertas;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			ofertas = ExcelBinder.readSheet(
                    in,
                    OfertaEDemanda.class,
                    planilha,     // ou null para a primeira
                    linhaCabecalho,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return ofertas;

	}
	

	private static String normalizarDataParaMesAno(String valor) {
	    if (valor == null || valor.isBlank())
	        return null;
	
	    Locale localeBR = Locale.of("pt", "BR"); // Java 21
	    DateTimeFormatter fmtMesAno = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
	
	    // 1️ Caso seja número serial do Excel
	    if (valor.matches("\\d+")) {
	        long serial = Long.parseLong(valor);
	        LocalDate data = LocalDate.of(1899, 12, 30).plusDays(serial); // Ajuste Excel
	        return fmtMesAno.format(data);
	    }
	
	    // 2️⃣ Caso seja dd/MM/yyyy
	    try {
	        DateTimeFormatter fmtCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy", localeBR);
	        LocalDate data = LocalDate.parse(valor, fmtCompleto);
	        return fmtMesAno.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e tenta o próximo formato
	    }
	
	    // 3️⃣ Caso seja mmm/yyyy (direto do Excel ou do POI)
	    try {
	        DateTimeFormatter fmtEntradaAbrev = DateTimeFormatter.ofPattern("MMM/yyyy", localeBR);
	        LocalDate data = LocalDate.parse("01/" + valor, DateTimeFormatter.ofPattern("dd/MMM/yyyy", localeBR));
	        return fmtMesAno.format(data);
	    } catch (DateTimeParseException e) {
	        // ignora e vai para erro final
	    }
	
	    throw new IllegalArgumentException("Formato de data inválido: " + valor);
	}

	
}
