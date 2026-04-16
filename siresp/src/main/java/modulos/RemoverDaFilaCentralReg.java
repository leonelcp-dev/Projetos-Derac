package modulos;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import dadosGerais.ParametrosArquivoDistribuicaoFilaCentralReg;
import dadosGerais.ParametrosArquivoFilasNominais;
import dadosGerais.ParametrosTabelaCDRRegulacoesConsulta;
import dadosGerais.ParametrosTabelaCDRRegulacoesExames;
import dadosGerais.ParametrosTabelaCDRResultadosConsulta;
import dadosGerais.ParametrosTabelaCDRResultadosExame;
import dominiosSIRESP.EspecialidadesSIRESP;
import dominiosSIRESP.ExamesSIRESP;
import dominiosSIRESP.StatusAgendamentoSIRESP;
import dominiosSIRESP.UnidadeSIRESP;
import extracao_dados.siresp.CriarImportacaoFilaCentralReg;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import modelosDados.CelulaExcel;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeLeito;
import modelosDados.EntidadesFilaCentralReg;
import modelosDados.LinhaCensoLeitos;
import modelosDados.UsuarioFilaCentralReg;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class RemoverDaFilaCentralReg {
	
	private int mesCompetencia;
	private int anoCompetencia;
	private String pastaRaizDosArquivos;
	private String pastaDownloads;
	private MesesFormatados meses;	
	private DateTimeFormatter formatoDataPasta;
	LocalDate dataProcessamento;
	String dataFormatadaPasta;

	public String remvoverRegistrosCentralReg(WebDriver driver)
	{			
		formatoDataPasta = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Agendamento");
		opcoes.add("Cadastro Demanda por Recurso");
		
		
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String opcoesSimNao[] = {"Sim", "Não"};
		int iniciar = JOptionPane.showOptionDialog(null, "Iniciar?", "Iniciar Processo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoesSimNao, null);
		
		pastaRaizDosArquivos = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram as filas a serem importadas", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
		
		if(iniciar == 1)
			return "Finalizado";
		
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
			else
			{
				posicaoPerfilDeAcesso = elemento.getText().indexOf(" - Agendador Reg");
				
				if(posicaoPerfilDeAcesso > 0)
				{
					String composicaoCNESNomeUnidade = elemento.getText().substring(0, posicaoPerfilDeAcesso);
					elementosRadioUnidades.put(composicaoCNESNomeUnidade, value);
				}
			}

		}
		
		EntidadeCDRNaoRegulada centralReg = new EntidadeCDRNaoRegulada("5733944", "CENTRAL REG DE CAMPINAS", "DIVERSOS", "CENTRAL REG DE CAMPINAS");
		
		String value = elementosRadioUnidades.get(centralReg.getCNES() + " - " + centralReg.getNomeUnidadeSIRESP());
		//System.out.println(value);
		
		if(value != null)
		{
			//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
		
			boolean visivel;
			do
			{

				//paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
				
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
			
				visivel = paginaWeb.clicarMenuUL(driver, 1, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes, OpenStrategy.HOVER);
			}while(!visivel);
			
			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_CDR_ABA_LISTAR.getTextoIdentificador());
			
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
			
			iniciarRemocoesDeEntradaNaFila(driver, paginaWeb);
			
		}
		else
			System.out.println("Unidade não encontrada: " + centralReg.getCNES() + " - " + centralReg.getUnidade() + "(" + centralReg.getDistrito() + ")");

		
		JOptionPane.showMessageDialog(null, "Processamento concluído com sucesso!");
		
		return "";	
	}
	
	private String iniciarRemocoesDeEntradaNaFila(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb)
	{
		String caminhoArquivosImportados = pastaRaizDosArquivos + "\\Importacao\\";
		String caminhoArquivosUnidades = pastaRaizDosArquivos + "\\Fila Unica\\";
		
		ArrayList<EntidadesFilaCentralReg> entidades = lerEntidades(caminhoArquivosImportados + "unidades.csv");
		
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
			
			AcoesArquivoExcel arquivoUnidade = new AcoesArquivoExcel(caminho, 1);
			
			if(entradasFilaCentralReg != null)
			{
				int linhaExcel = ParametrosArquivoDistribuicaoFilaCentralReg.LINHA_INICIAL_ARQUIVO_UNIDADE.getIndice();
				for(UsuarioFilaCentralReg entrada : entradasFilaCentralReg)
				{
					String retorno = "";
					
					ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
					
					if(!entrada.status.toUpperCase().equals("AGENDADO") && entrada.observacaoAutomatizacao.trim().equals(""))
					{
						paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_CADASTRO_DEMANDA_POR_RECURSO_FILTRO_TIPO_DEMANDA.getTextoIdentificador(), entrada.exameOuConsulta);
						while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
						
						paginaWeb.limparInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_TEXT_CODIGO_PACIENTE.getTextoIdentificador());
						paginaWeb.preencherInputTextByName(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_TEXT_CODIGO_PACIENTE.getTextoIdentificador(), entrada.codigo);
						
						paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_BUSCAR.getTextoIdentificador(), "name");
						while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
						
						if(entrada.exameOuConsulta.equals("Consulta"))
						{
							retorno = removerEntradaDeConsulta(driver, paginaWeb, entrada, entidade.getNomeSIRESP());
						}
						else if(entrada.exameOuConsulta.equals("Exame"))
						{
							retorno = removerEntradaDeExame(driver, paginaWeb, entrada, entidade.getNomeSIRESP());
						}
					}
					else
					{
						retorno = "Usuário já agendado";
					}
					
					if(entrada.observacaoAutomatizacao.trim().equals(""))
					{
						celulas.add(new CelulaExcel(linhaExcel, ParametrosArquivoDistribuicaoFilaCentralReg.INDICE_COLUNA_OBSERVACAO_AUTOMATIZACAO.getIndice(), retorno, "String"));
						arquivoUnidade.gravarDadosEmCelula(0, celulas);
					}
					
					linhaExcel++;
				}
			}
		}
		
		
		return "";
	}
	
	private String removerEntradaDeConsulta(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, UsuarioFilaCentralReg entradaDeFila, String nomeSIRESP)
	{
		String retornoProcesso = "";
		
		if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
		{
			ArrayList<ArrayList<String>> tabelaDeResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_TABELA_RESULTADO.getTextoIdentificador());
			
			if(tabelaDeResultados != null)
			{
				int linhaDaTabelaResultados = 1;
				for(ArrayList<String> linhaDaTabela : tabelaDeResultados)
				{
					if(linhaDaTabela.size() >= ParametrosTabelaCDRResultadosConsulta.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice())
					{
						String codigoPaciente = linhaDaTabela.get(ParametrosTabelaCDRResultadosConsulta.INDICE_COLUNA_CODIGO.getIndice()).trim();
						if(codigoPaciente.equals(entradaDeFila.codigo))
						{
							String idAcao = IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_ACAO_TABELA_RESULTADO.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESP.VALOR_DINAMICO_LINHA_TABELA_RESULTADOS.getTextoIdentificador(), String.valueOf(linhaDaTabelaResultados));
							
							paginaWeb.clicarLinkPeloXPath(driver, idAcao);
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
							
							paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESP.ID_CADASTRO_DEMANDA_POR_RECURSO_LISTAR_CDR_REGULACOES.getTextoIdentificador());
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
							
							ArrayList<ArrayList<String>> tabelaDeResultadosConsultas = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_TABELA_REGULACAO_CONSULTA.getTextoIdentificador());
							int numeroLinhaDaTabelaConsultas = 1;
							for(ArrayList<String> linhaDaTabelaConsulta : tabelaDeResultadosConsultas)
							{
								System.out.println("Colunas: " + linhaDaTabelaConsulta.size());
								if(linhaDaTabelaConsulta.size() >= ParametrosTabelaCDRRegulacoesConsulta.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice())
								{
									String unidade = linhaDaTabelaConsulta.get(ParametrosTabelaCDRRegulacoesConsulta.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice()).trim();
									String especialidade = linhaDaTabelaConsulta.get(ParametrosTabelaCDRRegulacoesConsulta.INDICE_COLUNA_ESPECIALIDADE.getIndice()).trim();
									String cid = linhaDaTabelaConsulta.get(ParametrosTabelaCDRRegulacoesConsulta.INDICE_COLUNA_CID.getIndice()).trim();
									String status = linhaDaTabelaConsulta.get(ParametrosTabelaCDRRegulacoesConsulta.INDICE_COLUNA_STATUS.getIndice()).trim();
									String dataEntrada = linhaDaTabelaConsulta.get(ParametrosTabelaCDRRegulacoesConsulta.INDICE_COLUNA_DATA_ENTRADA.getIndice()).trim().replace("-", "/");
									
									System.out.println(especialidade + "|" + entradaDeFila.especialidade + "|");
									System.out.println(cid + "|" + entradaDeFila.cid + "|");
									System.out.println(status + "|" + entradaDeFila.status + "|");
									System.out.println(dataEntrada + "|" + entradaDeFila.dataEntrada + "|");
									
									if(IdentificadoresPaginaWebSIRESP.TEXTO_UNIDADE_CENTRAL_REG_DE_CAMPINAS.getTextoIdentificador().equals(unidade.toUpperCase()) &&
									   entradaDeFila.especialidade.toUpperCase().equals(especialidade.toUpperCase()) && 
									   entradaDeFila.cid.toUpperCase().equals(cid.toUpperCase()) && 
									   entradaDeFila.status.toUpperCase().equals(status.toUpperCase()) && 
									   entradaDeFila.dataEntrada.toUpperCase().equals(dataEntrada.toUpperCase()))
									{
										idAcao = IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_ACAO_TABELA_REGULACAO_CONSULTA.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESP.VALOR_DINAMICO_LINHA_TABELA_RESULTADOS.getTextoIdentificador(), String.valueOf(numeroLinhaDaTabelaConsultas));
										
										paginaWeb.clicarLinkPeloXPath(driver, idAcao);
										while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
										
										paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_CADASTRO_DEMANDA_POR_RECURSO_ALTERACAO_HISTORICO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_CADASTRO_DEMANDA_POR_MOTIVO_ALTERACAO_HISTORICO.getTextoIdentificador());
										
										paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_CADASTRO_DEMANDA_POR_RECURSO_OBSERVACAO_ALTERACAO_HISTORICO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_CADASTRO_DEMANDA_POR_RECURSO_OBSERVACAO_ALTERACAO_HISTORICO.getTextoIdentificador() + nomeSIRESP);
										
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_REGISTRAR_ALTERACAO_HISTORICO.getTextoIdentificador(), "name");
										
										paginaWeb.confirmarAlertaJS(driver);
										
										while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
										
										retornoProcesso = "Removido da fila da CENTRAL REG CAMPINAS para ser transferido para a unidade " + nomeSIRESP;
									}
								}
								
								numeroLinhaDaTabelaConsultas++;
							}
							
							paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_VOLTAR_HISTORICO.getTextoIdentificador(), "name");
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
							
							break;
						}
					}
					linhaDaTabelaResultados++;
				}
			}
		}
		
		if(retornoProcesso.equals(""))
			retornoProcesso = "Erro ao encontrar correspondência para remoção da fila";
		
		return retornoProcesso;
	}
	
	private String removerEntradaDeExame(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, UsuarioFilaCentralReg entradaDeFila, String nomeSIRESP)
	{
		String retornoProcesso = "";
		
		if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_NENHUM_RESULTADO_ENCONTRADO.getTextoIdentificador()))
		{
			ArrayList<ArrayList<String>> tabelaDeResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_TABELA_RESULTADO.getTextoIdentificador());
			
			if(tabelaDeResultados != null)
			{
				int linhaDaTabelaResultados = 1;
				for(ArrayList<String> linhaDaTabela : tabelaDeResultados)
				{
					if(linhaDaTabela.size() >= ParametrosTabelaCDRResultadosExame.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice())
					{
						String codigoPaciente = linhaDaTabela.get(ParametrosTabelaCDRResultadosExame.INDICE_COLUNA_CODIGO.getIndice()).trim();
						if(codigoPaciente.equals(entradaDeFila.codigo))
						{
							String idAcao = IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_ACAO_TABELA_RESULTADO.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESP.VALOR_DINAMICO_LINHA_TABELA_RESULTADOS.getTextoIdentificador(), String.valueOf(linhaDaTabelaResultados));
							
							paginaWeb.clicarLinkPeloXPath(driver, idAcao);
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
							
							paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESP.ID_CADASTRO_DEMANDA_POR_RECURSO_LISTAR_CDR_REGULACOES.getTextoIdentificador());
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
							
							ArrayList<ArrayList<String>> tabelaDeResultadosExames = paginaWeb.obterTablePeloXPath(driver, IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_TABELA_REGULACAO_EXAME.getTextoIdentificador());
							int numeroLinhaDaTabelaExames = 1;
							for(ArrayList<String> linhaDaTabelaExame : tabelaDeResultadosExames)
							{
								if(linhaDaTabelaExame.size() >= ParametrosTabelaCDRRegulacoesExames.QUANTIDADE_ESPERADA_DE_COLUNAS.getIndice())
								{
									String unidade = linhaDaTabelaExame.get(ParametrosTabelaCDRRegulacoesExames.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice()).trim();
									String exame = linhaDaTabelaExame.get(ParametrosTabelaCDRRegulacoesExames.INDICE_COLUNA_EXAME.getIndice()).trim();
									String cid = linhaDaTabelaExame.get(ParametrosTabelaCDRRegulacoesExames.INDICE_COLUNA_CID.getIndice()).trim();
									String status = linhaDaTabelaExame.get(ParametrosTabelaCDRRegulacoesExames.INDICE_COLUNA_STATUS.getIndice()).trim();
									String dataEntrada = linhaDaTabelaExame.get(ParametrosTabelaCDRRegulacoesExames.INDICE_COLUNA_DATA_ENTRADA.getIndice()).trim().replace("-", "/");
									
									System.out.println(exame + "|" + entradaDeFila.especialidade + "|");
									System.out.println(cid + "|" + entradaDeFila.cid + "|");
									System.out.println(status + "|" + entradaDeFila.status + "|");
									System.out.println(dataEntrada + "|" + entradaDeFila.dataEntrada + "|");
									
									if(IdentificadoresPaginaWebSIRESP.TEXTO_UNIDADE_CENTRAL_REG_DE_CAMPINAS.getTextoIdentificador().equals(unidade.toUpperCase()) &&
									   entradaDeFila.especialidade.toUpperCase().equals(exame.toUpperCase()) &&
									   entradaDeFila.cid.toUpperCase().equals(cid.toUpperCase()) && 
									   entradaDeFila.status.toUpperCase().equals(status.toUpperCase()) && 
									   entradaDeFila.dataEntrada.toUpperCase().equals(dataEntrada.toUpperCase()))
									{
										idAcao = IdentificadoresPaginaWebSIRESP.XPATH_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_ACAO_TABELA_REGULACAO_EXAME.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESP.VALOR_DINAMICO_LINHA_TABELA_RESULTADOS.getTextoIdentificador(), String.valueOf(numeroLinhaDaTabelaExames));
										
										paginaWeb.clicarLinkPeloXPath(driver, idAcao);
										while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
										
										paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESP.ID_CADASTRO_DEMANDA_POR_RECURSO_ALTERACAO_HISTORICO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_CADASTRO_DEMANDA_POR_MOTIVO_ALTERACAO_HISTORICO.getTextoIdentificador());
										
										paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESP.ID_CADASTRO_DEMANDA_POR_RECURSO_OBSERVACAO_ALTERACAO_HISTORICO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.TEXTO_CADASTRO_DEMANDA_POR_RECURSO_OBSERVACAO_ALTERACAO_HISTORICO.getTextoIdentificador() + nomeSIRESP);
										
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										//paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_CANCELAR_ALTERACAO_HISTORICO.getTextoIdentificador(), "name");
										
										paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_REGISTRAR_ALTERACAO_HISTORICO.getTextoIdentificador(), "name");
										
										paginaWeb.confirmarAlertaJS(driver);
										
										while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
										
										retornoProcesso = "Removido da fila da CENTRAL REG CAMPINAS para ser transferido para a unidade " + nomeSIRESP;
									}
								}
								
								numeroLinhaDaTabelaExames++;
							}
							
							paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_CADASTRO_DEMANDA_POR_RECURSO_BOTAO_VOLTAR_HISTORICO.getTextoIdentificador(), "name");
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_REGULADA_SOLICITACOES_DIV_ESPERANDO.getTextoIdentificador()));
							
							break;
						}
					}
					linhaDaTabelaResultados++;
				}
			}
		}
		
		if(retornoProcesso.equals(""))
			retornoProcesso = "Erro ao encontrar correspondência para remoção da fila";
		
		return retornoProcesso;
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
	
}
