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
import modelosDados.CorrelacaoColunasArquivos;
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
	private String pastaRaizDosArquivos;
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

	public String verificarAbsenteismo(WebDriver driver, String competenciaInicial, String competenciaFinal)
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
		
		
		String[] opcoesRotina = {"Executar rotina completa", "Executar apenas consolidação"}; 
        int escolhaRotina = JOptionPane.showOptionDialog( null, "O que deseja fazer?", "Rotinas", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoesRotina, opcoesRotina[0] );
        
        pastaRaizDosArquivos = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados de Absenteismo", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
		pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE).trim();
		
		String mesInicio;
		String anoInicio;
		String mesFim;
		String anoFim;
		
		if(competenciaInicial == null)
		{
			mesInicio = JOptionPane.showInputDialog(null, "Qual o mês de análise?", "Mês de Referência", JOptionPane.QUESTION_MESSAGE).trim();
			anoInicio = JOptionPane.showInputDialog(null, "Qual o ano de análise?", "Ano de Referência", JOptionPane.QUESTION_MESSAGE).trim();
			
			mesFim = mesInicio;
			anoFim = anoInicio;
		}
		else
		{
			mesInicio = competenciaInicial.split("/")[0];
			anoInicio = competenciaInicial.split("/")[1];
			
			mesFim = competenciaFinal.split("/")[0];
			anoFim = competenciaFinal.split("/")[1];
		}
		
		mesCompetencia = Integer.parseInt(mesInicio);
		anoCompetencia = Integer.parseInt(anoInicio);
		
		int ultimoMesProcessamento = Integer.parseInt(mesFim);
		int ultimoAnoProcessamento = Integer.parseInt(anoFim);
		
		while((anoCompetencia < ultimoAnoProcessamento) || (anoCompetencia == ultimoAnoProcessamento && mesCompetencia <= ultimoMesProcessamento))
		{
			if(mesCompetencia == 1)
			{
				mesReferencia = 12;
				anoReferencia = anoCompetencia - 1;
			}
			else
			{
				mesReferencia = mesCompetencia - 1;
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
			ArrayList<EntidadeAbsenteismo> entidades = lerEntidades(pastaRaizDosArquivos + "\\unidadessolicitantes.csv", anoCompetencia);
			
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
			
			pastaDestinoArquivos = pastaRaizDosArquivos + "\\" + anoCompetencia + "\\";
			
			if(escolhaRotina == 0)
			{
			
				driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
			}
			
			consolidarArquivoMunicipal(entidades, mesCompetencia, anoCompetencia);
			
			mesCompetencia++;
			if(mesCompetencia > 12)
			{
				mesCompetencia = 1;
				anoCompetencia++;
			}
		}
		
		return "";	
	}
	
	private ArrayList<EntidadeAbsenteismo> lerEntidades(String nomeArquivo, int anoCompetencia)
	{
		ArrayList<EntidadeAbsenteismo> entidades = new ArrayList();
		
		String ano = String.valueOf(anoCompetencia);
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
                String cnes = registro.get("CNES");
                String unidade = registro.get("Unidade");
                String distrito = registro.get("Distrito");
                String nomeUnidadeSIRESP = registro.get("Nome SIRESP");
                String nomeParaGrafico = registro.get("Nome Gráfico");
                String nomeArquivoAbsenteismo = registro.get("Nome Arquivo");
                
                entidades.add(new EntidadeAbsenteismo(cnes, unidade, distrito, nomeUnidadeSIRESP, nomeArquivoAbsenteismo, nomeParaGrafico, ano));
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
		
		int linhaArquivoConsolidado = ParametrosArquivoAbsenteismoConsolidado.LINHA_INICIAL_ARQUIVO_CONSOLIDADO.getIndice();
				
		for(int i = 0; i < tiposDeBusca.length; i++)
		{
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_ABSENTEISMO_FILTRO_TIPO_CONSULTA_EXAME.getTextoIdentificador(), tiposDeBusca[i]);
			
			paginaWeb.limparInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_INICIAL.getTextoIdentificador());
			paginaWeb.preencherInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_INICIAL.getTextoIdentificador(), dataFormatadaInicioCompetencia.replaceAll("/", ""));
					
			paginaWeb.limparInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_FINAL.getTextoIdentificador());
			paginaWeb.preencherInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_FILTRO_DATA_FINAL.getTextoIdentificador(), dataFormatadaFinalCompetencia.replaceAll("/", ""));
			
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
			
			if(!paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_ABSENTEISMO_MENSAGEM_NENHUM_REGISTRO_ENCONTADO.getTextoIdentificador()))
			{
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_ABSENTEISMO_BOTAO_DOWNLOAD.getTextoIdentificador(), "name");
				
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
				}while(arquivoMaisRecente.equals(ultimoRecente) || !arquivoMaisRecente.endsWith(ParametrosArquivoCenso.EXTENSAO_ARQUIVO_CENSO.getDescricao()));
				
				Arquivo arquivo = new Arquivo(pastaDownloads, arquivoMaisRecente);
								
				ultimoRecente = arquivo.getNomeDoArquivo();
				
				entidade.setArquivoBaixadoXLS(arquivo.getNomeDoArquivo());
				entidade.setCaminhoCompletoArquivoBaixadoXLS(arquivo.getCaminhoCompleto());
				
				entidade.setArquivoBaixadoXLSX(arquivo.getNomeDoArquivo() + "x");
				entidade.setCaminhoCompletoArquivoBaixadoXLSX(arquivo.getCaminhoCompleto()+"x");
				
				ConversaoHMTL_XLSX conversor = new ConversaoHMTL_XLSX();
				
				try
				{
					conversor.converterArquivo(entidade.getCaminhoCompletoArquivoBaixadoXLS(), entidade.getCaminhoCompletoArquivoBaixadoXLSX());
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
				AcoesArquivoExcel arquivoSIRESP = new AcoesArquivoExcel(entidade.getCaminhoCompletoArquivoBaixadoXLSX(), 0);
				AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaDestinoArquivos + entidade.getNomeArquivoAbsenteismo(), 0);
				
				ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MES_DE_REFERENCIA.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MES_DE_REFERENCIA.getIndice(), dataInicioCompetencia, "Date mes/ano"));
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_TEXTO_MES_DE_REFERENCIA.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_TEXTO_MES_DE_REFERENCIA.getIndice(), "*ref. " + meses.getMeses().get(mesReferencia - 1).getMesDescricao() + " " + anoReferencia, "String"));
				
				arquivoConsolidado.gravarDadosEmCelula(meses.getMeses().get(mesCompetencia - 1).getMesDescricaoSemAcentuacao(), celulas, false, false, i, null);
				
				celulas = new ArrayList<CelulaExcel>();
				
				int ultimaLinhaArquivoSIRESP = arquivoSIRESP.getPrimeiraLinhaVazia();
				
				int primeiraLinhaArquivoSIRESP = 0;
				if(tiposDeBusca[i].equals("Exame"))
					primeiraLinhaArquivoSIRESP = ParametrosArquivoAbsenteismoExameBaixado.LINHA_INICIAL_ARQUIVO_SIRESP.getIndice();
				else if(tiposDeBusca[i].equals("Consulta"))
					primeiraLinhaArquivoSIRESP = ParametrosArquivoAbsenteismoConsultaBaixado.LINHA_INICIAL_ARQUIVO_SIRESP.getIndice();
				
				for(int linha = primeiraLinhaArquivoSIRESP; linha <= ultimaLinhaArquivoSIRESP; linha++)
				{
					celulas.add(new CelulaExcel(linhaArquivoConsolidado, ParametrosArquivoAbsenteismoConsolidado.INDICE_COLUNA_TIPO.getIndice(), tiposDeBusca[i], ParametrosArquivoAbsenteismoConsolidado.INDICE_COLUNA_TIPO.getTipo()));
					
					CorrelacaoArquivosAbsenteismo correlacoes = new CorrelacaoArquivosAbsenteismo();
					
					ArrayList<CorrelacaoColunasArquivos> colunasConsolidado = correlacoes.obterCorrelacaoEntreArquivos(tiposDeBusca[i]);
					
					
					for(CorrelacaoColunasArquivos coluna : colunasConsolidado)
					{
						//System.out.println("ColunaSIRESP: " + coluna.getColunaSIRESP() + " Coluna Consolidado: " + coluna.getColunaConsolidado() + " Formato: " + coluna.getFormato());
						
						if(arquivoSIRESP.ehCelulaVazia(linha, coluna.getColunaSIRESP()))
						{
							celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaSIRESP(), "", "String"));
						}
						else
						{
							if(coluna.getTipo().equals("String"))
							{
								String valor = arquivoSIRESP.getValorDaCelulaString(linha, coluna.getColunaSIRESP());
								celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
							}else if(coluna.getTipo().equals("Date"))
							{
								LocalDate valor = arquivoSIRESP.getValorDaCelulaDate(linha, coluna.getColunaSIRESP());
								celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
							}else if(coluna.getTipo().equals("DateTime"))
							{
								LocalDateTime valor = arquivoSIRESP.getValorDaCelulaDateTime(linha, coluna.getColunaSIRESP(), coluna.getFormato());
								celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
							}else if(coluna.getTipo().equals("Time"))
							{
								LocalTime valor = arquivoSIRESP.getValorDaCelulaTime(linha, coluna.getColunaSIRESP(), coluna.getFormato());
								celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
							}else if(coluna.getTipo().equals("Int"))
							{
								Integer valor = Integer.parseInt(arquivoSIRESP.getValorDaCelulaString(linha, coluna.getColunaSIRESP()));
								celulas.add(new CelulaExcel(linhaArquivoConsolidado, coluna.getColunaConsolidado(), valor, coluna.getTipo()));
							}
						}
						
					}
					
					linhaArquivoConsolidado++;
				}
				arquivoConsolidado.forcarCalculos();
				arquivoConsolidado.gravarDadosEmCelula(meses.getMeses().get(mesCompetencia - 1).getMesDescricaoSemAcentuacao(), celulas, true, false, ParametrosArquivoAbsenteismoConsolidado.LINHA_INICIAL_ARQUIVO_CONSOLIDADO.getIndice(), null);

				Arquivo arquivoAApagar = new Arquivo(pastaDownloads, entidade.getArquivoBaixadoXLS());
				arquivoAApagar.apagar();
				
				arquivoAApagar = new Arquivo(pastaDownloads, entidade.getArquivoBaixadoXLSX());
				arquivoAApagar.apagar();
			}
			else
				System.out.println("Não foi encontrado arquivo resultados para " + entidade.getNomeUnidadeSIRESP());
		}
			
		return "";
		
	}
	
	public String consolidarArquivoMunicipal(ArrayList<EntidadeAbsenteismo> entidades, int mesCompetencia, int anoCompetencia)
	{
		int totalConsultasAgendadas = 0;
		int totalFaltasEmConsultas = 0;
		int totalExamesAgendados = 0;
		int totalFaltasEmExames = 0;

		String nomeDaPlanilha = meses.getMeses().get(mesCompetencia - 1).getMesDescricaoSemAcentuacao();
		for(EntidadeAbsenteismo entidade : entidades)
		{
			AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaDestinoArquivos + "\\" + entidade.getNomeArquivoAbsenteismo(), 0);
			arquivoConsolidado.abrirPlanilha(nomeDaPlanilha, 0);
			
			entidade.setQuantidadeConsultasAgendadas(arquivoConsolidado.obterValorInteiroDeUmaCelulaComFormula(nomeDaPlanilha, ParametrosArquivoAbsenteismoConsolidado.LINHA_TOTAL_CONSULTAS_AGENDADAS.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_TOTAL_CONSULTAS_AGENDADAS.getIndice()));
			entidade.setQuantidadeFaltasConsultas(arquivoConsolidado.obterValorInteiroDeUmaCelulaComFormula(nomeDaPlanilha, ParametrosArquivoAbsenteismoConsolidado.LINHA_TOTAL_FALTAS_CONSULTAS.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_TOTAL_FALTAS_CONSULTAS.getIndice()));
			
			entidade.setQuantidadeExamesAgendados(arquivoConsolidado.obterValorInteiroDeUmaCelulaComFormula(nomeDaPlanilha, ParametrosArquivoAbsenteismoConsolidado.LINHA_TOTAL_EXAMES_AGENDADOS.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_TOTAL_EXAMES_AGENDADOS.getIndice()));
			entidade.setQuantidadeFaltasExames(arquivoConsolidado.obterValorInteiroDeUmaCelulaComFormula(nomeDaPlanilha, ParametrosArquivoAbsenteismoConsolidado.LINHA_TOTAL_FALTAS_EXAMES.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_TOTAL_FALTAS_EXAMES.getIndice()));
			
			totalConsultasAgendadas += entidade.getQuantidadeConsultasAgendadas();
			totalFaltasEmConsultas += entidade.getQuantidadeFaltasConsultas();
			totalExamesAgendados += entidade.getQuantidadeExamesAgendados();
			totalFaltasEmExames += entidade.getQuantidadeFaltasExames();
		}
		
		double absenteismoMunicipalEmConsultas = 1.0 * totalFaltasEmConsultas / totalConsultasAgendadas;
		double absenteismoMunicipalEmExames = 1.0 * totalFaltasEmExames / totalExamesAgendados;
		
		String nomeArquivoConsolidado = pastaDestinoArquivos + "\\" + ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_NOME.getDescricao().replaceAll(ParametrosArquivoAbsenteismoConsolidado.TEXTO_DINAMICO_PARA_SUBSTITUICAO.getDescricao(), String.valueOf(anoCompetencia));
		AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(nomeArquivoConsolidado, 0);
		
		ArrayList<CelulaExcel> celulasArquivoConsolidadoValoresConsulta = new ArrayList<CelulaExcel>();
		ArrayList<CelulaExcel> celulasArquivoConsolidadoValoresExame = new ArrayList<CelulaExcel>();
		
		ArrayList<CelulaExcel> celulasArquivoConsolidadoPlanilhaMMA = new ArrayList<CelulaExcel>();
		celulasArquivoConsolidadoPlanilhaMMA.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL_PLANILHA_MMA.getIndice() + (mesCompetencia - 1), ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_CONSULTAS_PLANILHA_MMA.getIndice(), absenteismoMunicipalEmConsultas, "Porcentagem"));
		celulasArquivoConsolidadoPlanilhaMMA.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL_PLANILHA_MMA.getIndice() + (mesCompetencia - 1), ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_EXAMES_PLANILHA_MMA.getIndice(), absenteismoMunicipalEmExames, "Porcentagem"));
		celulasArquivoConsolidadoPlanilhaMMA.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL_PLANILHA_MMA.getIndice() + (mesCompetencia - 1), ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_REFERENCIA_PLANILHA_MMA.getIndice(), "*ref. " + meses.getMeses().get(mesReferencia - 1).getMesDescricao() + " " + anoReferencia, "String"));
		
		arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoAbsenteismoConsolidado.NOME_PLANILHA_MMA.getDescricao(), celulasArquivoConsolidadoPlanilhaMMA, false, false, 0, null);
		arquivoConsolidado.forcarCalculos();
		
		arquivoConsolidado.abrirPlanilha(nomeDaPlanilha, 0);
		
		int linhaInicialArquivoMunicipal = ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL_UNIDADES.getIndice();
		int linhaFinalArquivoMunicipal = arquivoConsolidado.getPrimeiraLinhaVazia();
		
		int diferencaDeLinhaEntrePlanilhas = ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL_PLANILHA_VALORES.getIndice() - ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_LINHA_INICIAL_UNIDADES.getIndice();
		
		for(EntidadeAbsenteismo entidade : entidades)
		{
			arquivoConsolidado.abrirPlanilha(nomeDaPlanilha, 0);
			String nomeArquivoUnidade= pastaDestinoArquivos + "\\" + entidade.getNomeArquivoAbsenteismo();
			
			AcoesArquivoExcel arquivoUnidade = new AcoesArquivoExcel(nomeArquivoUnidade, 0);
			
			ArrayList<CelulaExcel> celulasArquivoUnidade = new ArrayList<CelulaExcel>();
			ArrayList<CelulaExcel> celulasArquivoConsolidado = new ArrayList<CelulaExcel>();
			
			celulasArquivoUnidade.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS.getIndice(), absenteismoMunicipalEmConsultas, "Porcentagem"));
			celulasArquivoUnidade.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES.getIndice(), absenteismoMunicipalEmExames, "Porcentagem"));
			
			int linha = linhaInicialArquivoMunicipal;
			while(linha <= linhaFinalArquivoMunicipal && !arquivoConsolidado.getValorDaCelulaString(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_UNIDADE.getIndice()).equals(entidade.getNomePadraoAbsenteismo()))
			{
				//System.out.println(linha + ": " + arquivoConsolidado.getValorDaCelulaString(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_UNIDADE.getIndice()) + " x " + entidade.getNomePadraoAbsenteismo());
				
				linha++;
			}
			
			if(linha <= linhaFinalArquivoMunicipal)
			{
				if(entidade.getAbsenteismoEmConsultas() < 0)
					celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_CONSULTA_UNIDADE.getIndice(), "-", "String"));
				else
					celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_CONSULTA_UNIDADE.getIndice(), entidade.getAbsenteismoEmConsultas(), "Porcentagem"));
				
				if(entidade.getAbsenteismoEmExames() < 0)
					celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_EXAME_UNIDADE.getIndice(), "-", "String"));
				else
					celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_EXAME_UNIDADE.getIndice(), entidade.getAbsenteismoEmExames(), "Porcentagem"));
				
				celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_CONSULTA_MUNICIPAL.getIndice(), absenteismoMunicipalEmConsultas, "Porcentagem"));
				celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_EXAME_MUNICIPAL.getIndice(), absenteismoMunicipalEmExames, "Porcentagem"));

				celulasArquivoConsolidadoValoresConsulta.add(new CelulaExcel(linha + diferencaDeLinhaEntrePlanilhas, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_AGENDADOS_PLANILHA_VALORES.getIndice() + (mesCompetencia - 1), entidade.getQuantidadeConsultasAgendadas(), "Int"));
				celulasArquivoConsolidadoValoresConsulta.add(new CelulaExcel(linha + diferencaDeLinhaEntrePlanilhas, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_AUSENTES_PLANILHA_VALORES.getIndice() + (mesCompetencia - 1), entidade.getQuantidadeFaltasConsultas(), "Int"));
				
				celulasArquivoConsolidadoValoresExame.add(new CelulaExcel(linha + diferencaDeLinhaEntrePlanilhas, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_AGENDADOS_PLANILHA_VALORES.getIndice() + (mesCompetencia - 1), entidade.getQuantidadeExamesAgendados(), "Int"));
				celulasArquivoConsolidadoValoresExame.add(new CelulaExcel(linha + diferencaDeLinhaEntrePlanilhas, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_AUSENTES_PLANILHA_VALORES.getIndice() + (mesCompetencia - 1), entidade.getQuantidadeFaltasExames(), "Int"));
				
				celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_DISTRITO.getIndice(), entidade.getDistrito(), "String"));
				celulasArquivoConsolidado.add(new CelulaExcel(linha, ParametrosArquivoAbsenteismoConsolidado.ARQUIVO_MUNICIPAL_COLUNA_MES_REFERENCIA.getIndice(), dataInicioCompetencia, "Date"));
			}
			
			arquivoUnidade.gravarDadosEmCelula(nomeDaPlanilha, celulasArquivoUnidade, false, false, 0, null);
			arquivoUnidade.forcarCalculos();
			
			arquivoConsolidado.gravarDadosEmCelula(nomeDaPlanilha, celulasArquivoConsolidado, false, false, 0, null);
			arquivoConsolidado.forcarCalculos();
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoAbsenteismoConsolidado.NOME_PLANILHA_VALORES_CONSULTA.getDescricao(), celulasArquivoConsolidadoValoresConsulta, false, false, 0, null);
			arquivoConsolidado.forcarCalculos();
			
			arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoAbsenteismoConsolidado.NOME_PLANILHA_VALORES_EXAME.getDescricao(), celulasArquivoConsolidadoValoresExame, false, false, 0, null);
			arquivoConsolidado.forcarCalculos();
		}
		
		return "";
	}
	
	public String parametrizarArquivosVazios(WebDriver driver)
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
		String ano = JOptionPane.showInputDialog(null, "Qual o ano de análise?", "Ano de Referência", JOptionPane.QUESTION_MESSAGE).trim();
		
		anoCompetencia = Integer.parseInt(ano);
		
		//definindo entidades para o censo de leitos
		ArrayList<EntidadeAbsenteismo> entidades = lerEntidades(pastaDestinoArquivos + "\\unidadessolicitantes.csv", anoCompetencia);
		pastaDestinoArquivos = pastaDestinoArquivos  + "\\" + anoCompetencia + "\\";
		
		for(EntidadeAbsenteismo entidade : entidades)
		{
		
			AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(pastaDestinoArquivos + entidade.getNomeArquivoAbsenteismo(), 0);
				
			for(mesCompetencia = 1; mesCompetencia <= 12; mesCompetencia++)
			{
				if(mesCompetencia == 1)
				{
					mesReferencia = 12;
					anoReferencia = anoCompetencia - 1;
				}
				else
				{
					mesReferencia = mesCompetencia - 1;
					anoReferencia = anoCompetencia;
				}
				
				if(mesReferencia < 10)
					dataFormatadaInicioReferencia = "01-0" + mesReferencia + "-" + anoReferencia;
				else
					dataFormatadaInicioReferencia = "01-" + mesReferencia + "-" + anoReferencia;
				
				dataInicioReferencia = LocalDate.parse(dataFormatadaInicioReferencia, formatoDataPaginaWeb);
				
				dataFinalReferencia = dataInicioReferencia.with(TemporalAdjusters.lastDayOfMonth());
				dataFormatadaFinalReferencia = dataFinalReferencia.format(formatoDataPaginaWeb);
				
				if(mesCompetencia < 10)
					dataFormatadaInicioCompetencia = "01/0" + mesCompetencia + "/" + anoCompetencia;
				else
					dataFormatadaInicioCompetencia = "01/" + mesCompetencia + "/" + anoCompetencia;
				
				dataInicioCompetencia = LocalDate.parse(dataFormatadaInicioCompetencia, formatoDataArquivo);
				
				dataFinalCompetencia = dataInicioCompetencia.with(TemporalAdjusters.lastDayOfMonth());
				dataFormatadaFinalCompetencia = dataFinalCompetencia.format(formatoDataArquivo);
				
				ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
				
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MES_DE_REFERENCIA.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MES_DE_REFERENCIA.getIndice(), dataInicioCompetencia, "Date mes/ano"));
				
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_TEXTO_MES_DE_REFERENCIA.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_TEXTO_MES_DE_REFERENCIA.getIndice(), null, "String"));
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_TEXTO_MES_DE_REFERENCIA.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_TEXTO_MES_DE_REFERENCIA.getIndice(), "*ref. " + meses.getMeses().get(mesReferencia - 1).getMesDescricao() + " " + anoReferencia, "String"));
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_NOME_UNIDADE.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_NOME_UNIDADE.getIndice(), entidade.getNomePadraoAbsenteismo(), "String"));
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS.getIndice(), null, "Porcentagem"));
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS.getIndice(), 0, "Porcentagem"));
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES.getIndice(), null, "Porcentagem"));
				celulas.add(new CelulaExcel(ParametrosArquivoAbsenteismoConsolidado.LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES.getIndice(), ParametrosArquivoAbsenteismoConsolidado.COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES.getIndice(), 0, "Porcentagem"));
				
				arquivoConsolidado.gravarDadosEmCelula(meses.getMeses().get(mesCompetencia - 1).getMesDescricaoSemAcentuacao(), celulas, false, false, 0, null);
				
			}
			arquivoConsolidado.editarTituloGrafico(ParametrosArquivoAbsenteismoConsolidado.NOME_PLANILHA_GRAFICO.getDescricao(), ParametrosArquivoAbsenteismoConsolidado.INDICE_DESENHO_GRAFICO_PLANILHA.getIndice(), entidade.getNomeParaGrafico());
		}
		
		return "";	
	}
	
	
}
