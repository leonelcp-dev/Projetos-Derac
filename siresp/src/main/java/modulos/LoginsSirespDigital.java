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
import modelosDados.CorrelacaoColunasArquivosAbsenteismo;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeAbsenteismo;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeLeito;
import modelosDados.LinhaCensoLeitos;
import modelosDados.MesFormatado;
import modelosDados.PerfilSIRESP;
import modelosDados.PerfisUsuariosSIRESP;
import modelosDados.PerfisUsuariosSIRESP.Usuario;
import modelosDados.UsuarioSIRESP;
import modelosDados.UsuariosVinculadosSIRESP;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class LoginsSirespDigital {
	
	private String pastaComDadosDeLogin;
	private Map<String, Boolean> usuariosDistintos;
	private Map<String, String> tabelaDeParaPerfis;
	
	
	public void listarTodosAcessosSIRESP(WebDriver driver)
	{
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		criarListaUsuariosDistintos();
		criarDeParaPerfis();
		
		driver.get("https://digital.siresp.saude.sp.gov.br/pt_BR/usuario-acl/");
		
		pastaComDadosDeLogin = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados dos logins a serem criados", "Pasta", JOptionPane.QUESTION_MESSAGE).trim();
		
		String arquivoResultante = pastaComDadosDeLogin + "\\dadosCadastroSIRESP.xlsx";
		
		ArrayList<ElementoSelecao> unidades = paginaWeb.obterItensDeUmSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador());
		
		
		for(ElementoSelecao elemento :  unidades)
		{
			String ErroAoAcessarUnidade = "";
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE.getTextoIdentificador());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), elemento.getText());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			if(elemento.getText().equals(IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE.getTextoIdentificador()) || !paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador()).equals(elemento.getText()))
			{
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
						Thread.sleep(1500);
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
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
						tentativas++;
					}while(tentativas <= 5 && !selecionado);
						
					
					while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					
					System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()) + " - " + IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
					
					if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()).equals(IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador().toUpperCase()))
					{
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
								usuarioSIRESP.setSenhaProvisoria(ParametrosArquivoLoginSIRESP.TEXTO_SENHA_PROVISORIA.getDescricao());
								usuarioSIRESP.setHorarioDeAcessoAoPortal(ParametrosArquivoLoginSIRESP.TEXTO_HORARIO.getDescricao());
								usuarioSIRESP.setLogin(usuario.getLogin());

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
								
								paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_VOLTAR_NOVO_USUARIO.getTextoIdentificador());
								while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
							}
						}
					}
					
				}
			}
			else
			{
				if(!elemento.getText().equals(IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE.getTextoIdentificador()))
				{
					
					//gravarErro
					AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(arquivoResultante, 0);
					ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
					
					arquivoExcel.abrirPlanilha(1, 0);
					int linha = arquivoExcel.getPrimeiraLinhaVazia() + 1;
					
					celulas.add(new CelulaExcel(linha, 0, elemento.getText(), "String"));
					celulas.add(new CelulaExcel(linha, 1, ErroAoAcessarUnidade, "String"));
					
					arquivoExcel.gravarDadosEmCelula(1, celulas);
				}
			}
		}
	}
	
	public void gravarEmXLSX(String caminho, UsuarioSIRESP usuario)
	{
		AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(caminho, 0);
		ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
		
		arquivoExcel.abrirPlanilha(0, 0);
		int linha = arquivoExcel.getPrimeiraLinhaVazia() + 1;
		
		celulas.add(new CelulaExcel(linha, 0, usuario.getUnidade(), "String"));
		celulas.add(new CelulaExcel(linha, 1, usuario.getNomeCompleto(), "String"));
		celulas.add(new CelulaExcel(linha, 2, usuario.getCPF(), "String"));
		celulas.add(new CelulaExcel(linha, 3, usuario.getRG(), "String"));
		celulas.add(new CelulaExcel(linha, 4, usuario.getEmail(), "String"));
		celulas.add(new CelulaExcel(linha, 5, usuario.getTelefone(), "String"));
		celulas.add(new CelulaExcel(linha, 6, usuario.getDataDeCriacao(), "String"));
		celulas.add(new CelulaExcel(linha, 7, usuario.getSenhaProvisoria(), "String"));
		celulas.add(new CelulaExcel(linha, 8, usuario.getHorarioDeAcessoAoPortal(), "String"));
		celulas.add(new CelulaExcel(linha, 9, usuario.getLogin(), "String"));
		celulas.add(new CelulaExcel(linha, 10, usuario.getStatus(), "String"));
		celulas.add(new CelulaExcel(linha, 11, usuario.getModulo(), "String"));
		celulas.add(new CelulaExcel(linha, 12, usuario.getPerfil(), "String"));
		
		arquivoExcel.gravarDadosEmCelula(0, celulas);
	}
	
	public String tratarLoginsSIRESP(WebDriver driver)
	{			
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		criarListaUsuariosDistintos();
		criarDeParaPerfis();
		
		driver.get("https://digital.siresp.saude.sp.gov.br/pt_BR/usuario-acl/");
		
		pastaComDadosDeLogin = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados dos logins a serem criados", "Pasta", JOptionPane.QUESTION_MESSAGE).trim();
		
	
		//definindo entidades para o censo de leitos
		ArrayList<UsuarioSIRESP> usuarios = lerEntidades(pastaComDadosDeLogin + "\\loginsSiresp.xlsx");
		
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
		}
		
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
		
		String unidadeAtual = "";
		
		String usuarioAtual = "";
		String loginAtual = "";
		
		
		ArrayList<PerfisUsuariosSIRESP> listaDePerfisAProcessar = new ArrayList<PerfisUsuariosSIRESP>();
		
		int linhaExcel = 1;
		for(UsuarioSIRESP usuario : usuarios)
		{
			int indexUnidade = 0;
			
			while(indexUnidade < listaDePerfisAProcessar.size() && !listaDePerfisAProcessar.get(indexUnidade).getUnidade().equals(usuario.getUnidade()))
			{				
				indexUnidade++;
			}
			
			if(indexUnidade == listaDePerfisAProcessar.size())
			{
				listaDePerfisAProcessar.add(new PerfisUsuariosSIRESP());
				listaDePerfisAProcessar.get(indexUnidade).setUnidade(usuario.getUnidade());
			}

			int indexUsuario = 0;
			
			while(indexUsuario < listaDePerfisAProcessar.get(indexUnidade).getUsuarios().size() && 
					!((usuario.getLogin() != null && !usuario.getLogin().equals("") && listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getLogin().equals(usuario.getLogin())) || 
					  (usuario.getNomeCompleto() != null && !usuario.getNomeCompleto().equals("") &&  listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getNomeCompleto().equals(usuario.getNomeCompleto()))))
			{
				indexUsuario++;
			}
			
			if(indexUsuario < listaDePerfisAProcessar.get(indexUnidade).getUsuarios().size())
			{
				listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).setUsuario(usuario);
				listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getPerfis().add(new PerfilSIRESP(usuario.getModulo(), usuario.getPerfil(), usuario.getHorarioDeAcessoAoPortal(), linhaExcel));
			}
			else
			{
				listaDePerfisAProcessar.get(indexUnidade).novoUsuario();
				listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).setUsuario(usuario);
				listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getPerfis().add(new PerfilSIRESP(usuario.getModulo(), usuario.getPerfil(), usuario.getHorarioDeAcessoAoPortal(), linhaExcel));
			}
			
			linhaExcel++;
		}
		
		
		ArrayList<UsuariosVinculadosSIRESP> usuariosVinculados = new ArrayList<UsuariosVinculadosSIRESP>();
				
		for(PerfisUsuariosSIRESP linhaPerfil : listaDePerfisAProcessar)
		{

			if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_UNIDADE.getTextoIdentificador()))
				paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_UNIDADE.getTextoIdentificador());
			
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE.getTextoIdentificador());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), linhaPerfil.getUnidade());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador()))
			{
				paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador());
				paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_TEXTO_FILTRO_MODULO.getTextoIdentificador(), linhaPerfil.getUsuarios().get(0).getUsuario().getModulo());
			}
				
			int tentativas = 0;
			boolean selecionado;
			do
			{
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador(), linhaPerfil.getUsuarios().get(0).getUsuario().getModulo().toUpperCase());
				tentativas++;
				
			}while(tentativas <= 5 && !selecionado);
			
			
//			paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_PERFIL.getTextoIdentificador());
//			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_TEXTO_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
//			
//			tentativas = 0;
//			do
//			{
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
//				tentativas++;
//			}while(tentativas <= 5 && !selecionado);
			
			if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_PERFIL.getTextoIdentificador()))
				paginaWeb.escolherEmSelect2(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_TEXTO_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_OPCOES_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
			else
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
			
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			paginaWeb.clicarSpanPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_SPAN_USUARIO.getTextoIdentificador());
			
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			paginaWeb.clicarBotaoPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_BOTAO_PESQUISAR.getTextoIdentificador());
			
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			ArrayList<ArrayList<String>> tabela = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
			
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
			
			UsuariosVinculadosSIRESP usuarioVinculado = null;
			
			for(Usuario usuario : linhaPerfil.getUsuarios())
			{
				if(!usuario.getUsuario().getExecutado().equals(ParametrosArquivoLoginSIRESP.TEXTO_CONFIRMACAO_EXECUTADO.getDescricao()))
				{
					if(usuario.getUsuario().getLogin() != null && !usuario.getUsuario().getLogin().equals(""))
						usuarioVinculado = encontrarUsuarioVinculadoPorLogin(usuariosVinculados, usuario.getUsuario().getLogin());
					
					if(usuarioVinculado == null)
					{
						ArrayList<UsuariosVinculadosSIRESP> usuariosVinculadosComMesmoNome = encontrarUsuarioVinculadoPorNome(usuariosVinculados, usuario.getUsuario().getNomeCompleto());
						
						if(usuariosVinculadosComMesmoNome.size() == 1)
							usuarioVinculado = usuariosVinculadosComMesmoNome.get(0);
						else if(usuariosVinculadosComMesmoNome.size() > 1)
						{
							for(UsuariosVinculadosSIRESP usuarioComMesmoNome : usuariosVinculadosComMesmoNome)
							{
								if(ehUsuarioDistinto(usuarioComMesmoNome.getLogin()))
									usuarioVinculado = usuarioComMesmoNome;
							}
							if(usuarioVinculado == null)
								usuarioVinculado = usuariosVinculadosComMesmoNome.get(0);
						}
					}
					
					if(usuarioVinculado == null)
					{
						cadastrarUsuarioNoSIRESP(driver, paginaWeb, usuario.getUsuario());
		
						ArrayList<ArrayList<String>> tabelaUsuario = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
						
						//atribuirPerfil
						//atribuirLoginAtual
					}
					else
					{
						ArrayList<UsuariosVinculadosSIRESP> usuariosVinculadosComMesmoNome = encontrarUsuarioVinculadoPorNome(usuariosVinculados, usuario.getUsuario().getNomeCompleto());
						
						ArrayList<PerfilSIRESP> perfis = usuario.getPerfis();
						
						for(UsuariosVinculadosSIRESP usuarioVinculadoPorNome : usuariosVinculadosComMesmoNome)
						{
							if(usuarioVinculadoPorNome.getLogin().equals(usuario.getUsuario().getLogin()))
							{
							
								paginaWeb.preencherInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_PESQUISAR_USUARIO_POR_LOGIN.getTextoIdentificador(), usuarioVinculadoPorNome.getLogin());
								
								paginaWeb.clicarBotaoPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_BOTAO_PESQUISAR.getTextoIdentificador());
								while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
								
								paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_DROP_DOWN_LINHA_USUARIO.getTextoIdentificador());
								paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_EDITAR_PERFIL_USUARIO.getTextoIdentificador());
								while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
								
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								
								paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_VOLTAR_NOVO_USUARIO.getTextoIdentificador());
								while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
							}
						}
						
												
						//editarCadastro
						//atribuirPerfisAoUsuario
						
						//atribuirLoginAtual
						//removerUsuarioVinculadoDaListaDeUsuariosVinculadosSIRESP
					}
				}
			}

			break;
			
		}
		
		return "";	
	}
	
	
	private void cadastrarUsuarioNoSIRESP(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, UsuarioSIRESP usuario)
	{
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_NOVO_USUARIO.getTextoIdentificador());
		while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
		
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_NOME.getTextoIdentificador(), usuario.getNomeCompleto());
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_EMAIL.getTextoIdentificador(), usuario.getEmail());
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_RG.getTextoIdentificador(), usuario.getRG());
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CPF.getTextoIdentificador(), usuario.getCPF());
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CELULAR.getTextoIdentificador(), usuario.getTelefone());
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_SENHA.getTextoIdentificador(), usuario.getSenhaProvisoria());
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CONFIRMAR_SENHA.getTextoIdentificador(), usuario.getSenhaProvisoria());
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPDigital.ID_BOTAO_CADASTRAR_USUARIO.getTextoIdentificador(), "id");
		while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
		
		if(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_MODAL_CADASTRO_EXISTENTE.getTextoIdentificador()))
		{
			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_SIM_EDITAR_CADASTRO_EXISTENTE.getTextoIdentificador());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_NOME.getTextoIdentificador(), usuario.getNomeCompleto());
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_EMAIL.getTextoIdentificador(), usuario.getEmail());
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_RG.getTextoIdentificador(), usuario.getRG());
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CPF.getTextoIdentificador(), usuario.getCPF());
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CELULAR.getTextoIdentificador(), usuario.getTelefone());
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPDigital.ID_BOTAO_ATUALIZAR_USUARIO.getTextoIdentificador(), "id");
		}
	}
	
	private ArrayList<UsuarioSIRESP> lerEntidades(String nomeArquivo)
	{		
		ArrayList<UsuarioSIRESP> usuarios;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			usuarios = ExcelBinder.readSheet(
                    in,
                    UsuarioSIRESP.class,
                    "Plan1",     // ou null para a primeira
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
