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
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import dadosGerais.IdentificadoresPaginaWebSIRESPDigital;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoAbsenteismoConsolidado;
import dadosGerais.ParametrosArquivoAbsenteismoConsultaBaixado;
import dadosGerais.ParametrosArquivoAbsenteismoExameBaixado;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoFilasNominais;
import dadosGerais.ParametrosArquivoLoginSIRESP;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import modelosDados.CelulaExcel;
import modelosDados.CorrelacaoColunasArquivos;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeAbsenteismo;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeLeito;
import modelosDados.LinhaCensoLeitos;
import modelosDados.MesFormatado;
import modelosDados.PerfilSIRESP;
import modelosDados.PerfisUsuariosSIRESP;
import modelosDados.PerfisUsuariosSIRESP.Usuario;
import modelosDados.UnidadesProcessadas;
import modelosDados.UsuarioSIRESP;
import modelosDados.UsuariosVinculadosSIRESP;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class LoginsSirespDigital {
	
	private String pastaComDadosDeLogin;
	private Map<String, Boolean> usuariosDistintos;
	private Map<String, String> tabelaDeParaPerfis;
	private Map<String, ArrayList<String>> perfisJaProcessados;
	private ArrayList<String> unidadesJaProcessadas;
	
	public void listarTodosAcessosSIRESP(WebDriver driver)
	{
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		int multiplicadorTempo = 4;
		driver.get("https://digital.siresp.saude.sp.gov.br/pt_BR/usuario-acl/");
		
		pastaComDadosDeLogin = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados dos logins a serem criados", "Pasta", JOptionPane.QUESTION_MESSAGE).trim();
		
		criarListaUsuariosDistintos();
		
		perfisJaProcessados = new HashMap<String, ArrayList<String>>();
		
		criarDeParaPerfis();
		
		//definindo entidades para o censo de leitos
		ArrayList<UsuarioSIRESP> usuarios = lerEntidades(pastaComDadosDeLogin + "\\dadosCadastroSIRESP.xlsx", ParametrosArquivoLoginSIRESP.NOME_PLANILHA_LOGINS.getDescricao());
		
		ArrayList<UnidadesProcessadas> unidadesRegistradasComoProcessadas = lerUnidadesJaFinalizadas(pastaComDadosDeLogin + "\\dadosCadastroSIRESP.xlsx", ParametrosArquivoLoginSIRESP.NOME_PLANILHA_PROCESSADOS.getDescricao());
		
		for(UsuarioSIRESP usuario : usuarios)
		{
			System.out.println("-----------------");
			System.out.println(
					"Prioridade: " + usuario.getPrioridade() +
					"\nUnidade: " + usuario.getUnidade() +
					"\nNome Completo: " + usuario.getNomeCompleto() +
					"\nCPF: " + usuario.getCPF() +
					"\nRG: " + usuario.getRG() +
					"\nE-mail: " + usuario.getEmail() +
					"\nTelefone: " + usuario.getTelefone() +
					"\nSenha: " + usuario.getSenhaProvisoria() +
					"\nPerfil: " + usuario.getPerfil() +
					"\nHorário: " + usuario.getHorarioDeAcessoAoPortal() +
					"\nLogin: " + usuario.getLogin() +
					"\nExecutado: " + usuario.getExecutado() +
					"\nObservação: " + usuario.getObservacao()
					);
			
			if(perfisJaProcessados.containsKey(usuario.getUnidade()))
			{
				perfisJaProcessados.get(usuario.getUnidade()).add(usuario.getLogin());
			}
			else
			{
				perfisJaProcessados.put(usuario.getUnidade(), new ArrayList<String>());
				perfisJaProcessados.get(usuario.getUnidade()).add(usuario.getLogin());
			}
			
		}
		
		unidadesJaProcessadas = new ArrayList<String>();
		for(UnidadesProcessadas unidade : unidadesRegistradasComoProcessadas)
		{
			unidadesJaProcessadas.add(unidade.getUnidade());
		}
		
		String arquivoResultante = pastaComDadosDeLogin + "\\dadosCadastroSIRESP.xlsx";
		
		ArrayList<ElementoSelecao> unidades = paginaWeb.obterItensDeUmSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador());
		
		
		for(ElementoSelecao elemento :  unidades)
		{
			if(!unidadesJaProcessadas.contains(elemento.getText()))
			{
				String ErroAoAcessarUnidade = "";
				
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE.getTextoIdentificador());
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), elemento.getText());
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador()).equals(elemento.getText()))
				{
					System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador()) + " - " + elemento.getText());
					ErroAoAcessarUnidade = IdentificadoresPaginaWebSIRESPDigital.ERRO_AO_ACESSAR_UNIDADE.getTextoIdentificador();
				}
				else
				{
					if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador()))
					{
						paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador());
						paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_TEXTO_FILTRO_MODULO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_MODULO.getTextoIdentificador());
					}

					int tentativas = 0;
					boolean selecionado;
					do
					{
						try {
							Thread.sleep(multiplicadorTempo * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_MODULO.getTextoIdentificador());
						tentativas++;
						
					}while(tentativas <= 5 && !selecionado);
					while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					
					if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador()).equals(IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_MODULO.getTextoIdentificador()))
					{
						System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador()) + " - " + IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_MODULO.getTextoIdentificador());
						ErroAoAcessarUnidade = IdentificadoresPaginaWebSIRESPDigital.ERRO_AO_ACESSAR_MODULO.getTextoIdentificador();
					}
					else
					{
						if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_PERFIL.getTextoIdentificador()))
							paginaWeb.escolherEmSelect2(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_TEXTO_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_OPCOES_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
						else
							paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
						
						tentativas = 0;
						do
						{
							try {
								Thread.sleep(multiplicadorTempo * 1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
							tentativas++;
						}while(tentativas <= 5 && !selecionado);
							
						
						while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
						
						System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()) + " - " + IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
						
						if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()).toUpperCase().equals(IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador().toUpperCase()) && !paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_SPAN_MENU_USUARIO.getTextoIdentificador()))
						{
							System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()) + " - " + IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador().toUpperCase());
							ErroAoAcessarUnidade = IdentificadoresPaginaWebSIRESPDigital.ERRO_AO_ACESSAR_PERFIL.getTextoIdentificador();
						}
					}
					
				}
				
				if(ErroAoAcessarUnidade.equals(""))
				{
					paginaWeb.clicarSpanPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_SPAN_USUARIO.getTextoIdentificador());
					
					while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					
					paginaWeb.clicarBotaoPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_BOTAO_PESQUISAR.getTextoIdentificador());
					
					while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					
					ArrayList<ArrayList<String>> tabela = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
					
					ArrayList<UsuariosVinculadosSIRESP> usuariosVinculados = new ArrayList<UsuariosVinculadosSIRESP>();
					
					int numLinha = 0;
					for(ArrayList<String> linha : tabela)
					{
						System.out.println(numLinha++);
						for(String celula : linha)
							System.out.println(celula + "\t");
						System.out.println();
						
						if(linha.size() > 1)
						{
							UsuariosVinculadosSIRESP usuarioVinculado = new UsuariosVinculadosSIRESP();
							usuarioVinculado.setIdLinha(linha.get(0));
							usuarioVinculado.setNome(linha.get(1));
							usuarioVinculado.setEmail(linha.get(2));
							usuarioVinculado.setLogin(linha.get(3));
							usuarioVinculado.setStatus(linha.get(4));
							
							usuariosVinculados.add(usuarioVinculado);
						}
					}
					
					for(UsuariosVinculadosSIRESP usuario : usuariosVinculados)
					{
	
						boolean processarUsuario = false;
						
						if(perfisJaProcessados.containsKey(elemento.getText()))
						{
							if(!perfisJaProcessados.get(elemento.getText()).contains(usuario.getLogin().toLowerCase()))
							{
								processarUsuario = true;
								perfisJaProcessados.get(elemento.getText()).add(usuario.getLogin().toLowerCase());
							}
						}
						else
						{
							perfisJaProcessados.put(elemento.getText(), new ArrayList<String>());
							perfisJaProcessados.get(elemento.getText()).add(usuario.getLogin().toLowerCase());
							processarUsuario = true;
						}
						
						if(processarUsuario)
						{
							paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_PESQUISAR_USUARIO_POR_LOGIN.getTextoIdentificador(), usuario.getLogin());
							
							paginaWeb.clicarBotaoPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_BOTAO_PESQUISAR.getTextoIdentificador());
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
							
							ArrayList<ArrayList<String>> tabelaUsuarios = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
							
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							for(ArrayList<String> linhaUsuario : tabelaUsuarios)
							{
		
								if(linhaUsuario.size() > 1)
								{
	
									if(linhaUsuario.get(3).equals(usuario.getLogin()))
									{
										//Consultando dados do usuário
										paginaWeb.clicarLinkPeloXPathDeLinhaEspecifica(driver, linhaUsuario.get(0), IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_EDITAR_USUARIO.getTextoIdentificador());
										while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
										
										//Criação de Login	Módulo
										UsuarioSIRESP usuarioSIRESP = new UsuarioSIRESP();
										usuarioSIRESP.setUnidade(elemento.getText());
										usuarioSIRESP.setNomeCompleto(paginaWeb.obterTextoInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_NOME.getTextoIdentificador()));
										usuarioSIRESP.setCPF(paginaWeb.obterTextoInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CPF.getTextoIdentificador()));
										usuarioSIRESP.setRG(paginaWeb.obterTextoInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_RG.getTextoIdentificador()));
										usuarioSIRESP.setEmail(paginaWeb.obterTextoInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_EMAIL.getTextoIdentificador()));
										usuarioSIRESP.setTelefone(paginaWeb.obterTextoInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CELULAR.getTextoIdentificador()));
										usuarioSIRESP.setDataDeCriacao(paginaWeb.obterTextoInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_DATA_DE_CRIACAO.getTextoIdentificador()));
										usuarioSIRESP.setStatus(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_SELECT_USUARIO_ATIVO.getTextoIdentificador()));
										usuarioSIRESP.setSenhaProvisoria(ParametrosArquivoLoginSIRESP.TEXTO_SENHA_PROVISORIA_NAO_SE_APLICA.getDescricao());
										usuarioSIRESP.setHorarioDeAcessoAoPortal(ParametrosArquivoLoginSIRESP.TEXTO_HORARIO.getDescricao());
										usuarioSIRESP.setLogin(usuario.getLogin().toLowerCase());
		
										paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_VOLTAR_EDICAO_USUARIO.getTextoIdentificador());
										while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
										
										//obtendo perfis de acesso do usuário
										paginaWeb.clicarLinkPeloXPathDeLinhaEspecifica(driver, linhaUsuario.get(0), IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_DROP_DOWN_LINHA_USUARIO.getTextoIdentificador());
										paginaWeb.clicarLinkPeloXPathDeLinhaEspecifica(driver, linhaUsuario.get(0), IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_EDITAR_PERFIL_USUARIO.getTextoIdentificador());
										
										while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
										
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										ArrayList<String> linhasPerfisVisiveis = paginaWeb.encontrarIDsPorXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_CLASS_NAME_LINHA_PERFIL_VISIVEL.getTextoIdentificador());
										
										if(linhasPerfisVisiveis.size() <= 0)
										{
											usuarioSIRESP.setModulo("Sem módulo");
											usuarioSIRESP.setPerfil("Sem perfil");
											gravarEmXLSX(arquivoResultante, usuarioSIRESP);
										}
										
										for(String linhaVisivel : linhasPerfisVisiveis)
										{
											String numeroLinha = linhaVisivel.replace(IdentificadoresPaginaWebSIRESPDigital.PREFIXO_ID_LINHA_PERFIL.getTextoIdentificador(), "");
											
											if(!numeroLinha.equals(""))
											{
												String idModuloLinha = IdentificadoresPaginaWebSIRESPDigital.ID_DINAMICO_MODULO_LOGIN.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinha);
												
												System.out.println(idModuloLinha);
												String modulo = paginaWeb.obterTextoInputText(driver, idModuloLinha);
												
												String idPerfilLinha = IdentificadoresPaginaWebSIRESPDigital.ID_DINAMICO_PERFIL_LOGIN.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinha);
												String perfil = paginaWeb.obterTextoInputText(driver, idPerfilLinha);
												perfil = tabelaDeParaPerfis.get(perfil);
												
												usuarioSIRESP.setModulo(modulo);
												usuarioSIRESP.setPerfil(perfil);
											}
											else
											{
												usuarioSIRESP.setModulo("Sem módulo");
												usuarioSIRESP.setPerfil("Sem perfil");
											}
											
											gravarEmXLSX(arquivoResultante, usuarioSIRESP);
										}
										
										if(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_VOLTAR_NOVO_USUARIO.getTextoIdentificador()))
										{
											paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_VOLTAR_NOVO_USUARIO.getTextoIdentificador());
											while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
										}
										else
										{
											paginaWeb.clicarSpanPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_SPAN_USUARIO.getTextoIdentificador());
											while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
										}
									}
								}
							}
						}
						
					}
					//gravarErro
					AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(arquivoResultante, 0);
					ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
					
					arquivoExcel.abrirPlanilha(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_PROCESSADOS.getDescricao(), 0);
					int linha = arquivoExcel.getUltimaLinhaPreenchida() + 1;
					
					celulas.add(new CelulaExcel(linha, 0, elemento.getText(), "String"));
					celulas.add(new CelulaExcel(linha, 1, ParametrosArquivoLoginSIRESP.TEXTO_UNIDADE_CONCLUIDA.getDescricao(), "String"));
					
					arquivoExcel.gravarDadosEmCelula(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_PROCESSADOS.getDescricao(), celulas, false, false, 0, null);
				}
				else
				{
					//gravarErro
					AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(arquivoResultante, 0);
					ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
					
					arquivoExcel.abrirPlanilha(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_PROCESSADOS.getDescricao(), 0);
					int linha = arquivoExcel.getUltimaLinhaPreenchida() + 1;
					
					celulas.add(new CelulaExcel(linha, 0, elemento.getText(), "String"));
					celulas.add(new CelulaExcel(linha, 1, ErroAoAcessarUnidade, "String"));
					
					arquivoExcel.gravarDadosEmCelula(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_PROCESSADOS.getDescricao(), celulas, false, false, 0, null);
				}
			}
		}
	}
	
	public void gravarEmXLSX(String caminho, UsuarioSIRESP usuario)
	{
		AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(caminho, 0);
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		arquivoExcel.abrirPlanilha(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_LOGINS.getDescricao(), 0);
		int linha = arquivoExcel.getUltimaLinhaPreenchida() + 1;
		
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_UNIDADE.getIndice(), usuario.getUnidade(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_NOME_COMPLETO.getIndice(), usuario.getNomeCompleto(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_CPF.getIndice(), usuario.getCPF(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_RG.getIndice(), usuario.getRG(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_EMAIL.getIndice(), usuario.getEmail(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_TELEFONE.getIndice(), usuario.getTelefone(), "String"));
		//celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_CPF.getIndice(), usuario.getDataDeCriacao(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_SENHA_PROVISORIA.getIndice(), usuario.getSenhaProvisoria(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_HORARIO.getIndice(), usuario.getHorarioDeAcessoAoPortal(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_LOGIN.getIndice(), usuario.getLogin(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_STATUS.getIndice(), usuario.getStatus(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_MODULO.getIndice(), usuario.getModulo(), "String"));
		celulas.add(new CelulaExcel(linha, ParametrosArquivoLoginSIRESP.INDICE_COLUNA_PERFIL.getIndice(), usuario.getPerfil(), "String"));
		
		arquivoExcel.gravarDadosEmCelula(0, celulas);
	}
	

	private ArrayList<UsuarioSIRESP> lerEntidades(String nomeArquivo, String planilha)
	{		
		ArrayList<UsuarioSIRESP> usuarios;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			usuarios = ExcelBinder.readSheet(
                    in,
                    UsuarioSIRESP.class,
                    planilha,     // ou null para a primeira
                    0,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return usuarios;

	}
	
	private ArrayList<UnidadesProcessadas> lerUnidadesJaFinalizadas(String nomeArquivo, String planilha)
	{		
		ArrayList<UnidadesProcessadas> unidades;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			unidades = ExcelBinder.readSheet(
                    in,
                    UnidadesProcessadas.class,
                    planilha,     // ou null para a primeira
                    0,           // linha do cabeçalho (0-based)
                    true         // pular linhas totalmente vazias
            );

        }
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
			
		}
		
		return unidades;

	}
	
	private void criarListaUsuariosDistintos()
	{
		usuariosDistintos = new HashMap<String, Boolean>();
		
		usuariosDistintos.put("DABSANTOS", true);
		usuariosDistintos.put("EMANARIN", false);
		usuariosDistintos.put("DVAMARAL", false);
		usuariosDistintos.put("KXSILVA", true);
		usuariosDistintos.put("FSVSANTOS", true);
		usuariosDistintos.put("OMRODRIGUES", true);
		usuariosDistintos.put("NALVARES", true);
		usuariosDistintos.put("MDDANDRADE", true);
		usuariosDistintos.put("VLEITAO", true);
		usuariosDistintos.put("LEOCPEREIRA", true);
		usuariosDistintos.put("AFBASSETTO", true);
		usuariosDistintos.put("VBFSILVA", true);
		usuariosDistintos.put("VIGUIMARAES", false);
		usuariosDistintos.put("VBERNI", true);
		usuariosDistintos.put("LODCOSTA", true);
	}
	
	private void criarDeParaPerfis()
	{
		tabelaDeParaPerfis = new HashMap<String, String>();
		
		tabelaDeParaPerfis.put("Ambulatorial", "1 | AMBULATORIAL");
		tabelaDeParaPerfis.put("1 | AMBULATORIAL", "Ambulatorial");
		tabelaDeParaPerfis.put("1|AMBULATORIAL", "Ambulatorial");
		tabelaDeParaPerfis.put("Solicitante", "SOLICITANTE_-_MRA | SOLICITANTE");
		tabelaDeParaPerfis.put("SOLICITANTE_-_MRA | SOLICITANTE", "Solicitante");
		tabelaDeParaPerfis.put("SOLICITANTE_-_MRA|SOLICITANTE", "Solicitante");
		tabelaDeParaPerfis.put("Regulador", "REGULADOR | REGULADOR");
		tabelaDeParaPerfis.put("REGULADOR | REGULADOR", "Regulador");
		tabelaDeParaPerfis.put("REGULADOR|REGULADOR", "Regulador");
		tabelaDeParaPerfis.put("Gestor de Acessos", "GESTOR_DE_ACESSOS | GESTOR DE ACESSOS");
		tabelaDeParaPerfis.put("GESTOR_DE_ACESSOS | GESTOR DE ACESSOS", "Gestor de Acessos");
		tabelaDeParaPerfis.put("GESTOR_DE_ACESSOS|Gestor de acessos", "Gestor de Acessos");
		tabelaDeParaPerfis.put("Executante", "EXECUTANTE_-_MRA | EXECUTANTE");
		tabelaDeParaPerfis.put("EXECUTANTE_-_MRA | EXECUTANTE", "Executante");
		tabelaDeParaPerfis.put("EXECUTANTE_-_MRA|EXECUTANTE", "Executante");
		tabelaDeParaPerfis.put("ATENDENTE | Atendente", "Atendente");
		tabelaDeParaPerfis.put("ATENDENTE|Atendente", "Atendente");
		tabelaDeParaPerfis.put("RECEPCAO | recepção", "Recepção");
		tabelaDeParaPerfis.put("DISTRIBUIDOR | distribuidor", "Distribuidor");
		tabelaDeParaPerfis.put("RECEPCAO|recepção", "Recepção");
		tabelaDeParaPerfis.put("DISTRIBUIDOR|distribuidor", "Distribuidor");
		tabelaDeParaPerfis.put("RELATORIOS|Relatórios", "Relatórios");
		tabelaDeParaPerfis.put("ADMINISTRADOR|DUOSYSTEM", "Administrador DuoSystem");
		tabelaDeParaPerfis.put("INTEGRACAO|integração", "Integração");
		tabelaDeParaPerfis.put("PACIENTE_-_HISTORICO|Paciente - Histórico", "Paciente - Histórico");
		tabelaDeParaPerfis.put("TARM|tarm", "TARM");
		tabelaDeParaPerfis.put("MASTER_CROSS|MASTER CROSS", "Master CROSS");
		tabelaDeParaPerfis.put("DRS|drs", "DRS");
	}
	
	private boolean ehUsuarioDistinto(String login)
	{
		if(usuariosDistintos.containsKey(login))
			return true;
		else
			return false;
	}
	
	private UsuariosVinculadosSIRESP encontrarUsuarioVinculadoPorLogin(ArrayList<UsuariosVinculadosSIRESP> usuarios, String login)
	{
		for(UsuariosVinculadosSIRESP usuario : usuarios)
		{
			if(usuario.getLogin().toUpperCase().equals(login.toUpperCase()))
				return usuario;
		}
			
		return null;
	}
	
	private ArrayList<UsuariosVinculadosSIRESP> encontrarUsuarioVinculadoPorNome(ArrayList<UsuariosVinculadosSIRESP> usuarios, String nome)
	{
		ArrayList<UsuariosVinculadosSIRESP> usuariosEncontrados = new ArrayList<UsuariosVinculadosSIRESP>();
		
		for(UsuariosVinculadosSIRESP usuario : usuarios)
		{
			if(usuario.getNome().toUpperCase().equals(nome.toUpperCase()))
				usuariosEncontrados.add(usuario);
		}
			
		return usuariosEncontrados;
	}
	
}
