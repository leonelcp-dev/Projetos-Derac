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

public class CadastroUsuarioSIRESPDigital {
	
	private String pastaComDadosDeLogin;
	private Map<String, Boolean> usuariosDistintos;
	private Map<String, String> tabelaDeParaPerfis;
	
	
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
	
	public String cadastrarListaDeAcessosSIRESP(WebDriver driver)
	{			
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		ArrayList<String> usuariosJaEditados = new ArrayList<String>();
		
		int multiplicadorTempo = 4;
		
		criarListaUsuariosDistintos();
		criarDeParaPerfis();
		
		driver.get("https://digital.siresp.saude.sp.gov.br/pt_BR/usuario-acl/");
		
		pastaComDadosDeLogin = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados dos logins a serem criados", "Pasta", JOptionPane.QUESTION_MESSAGE).trim();
		String caminhoCompletoArquivo = pastaComDadosDeLogin + "\\loginsSiresp.xlsx";
	
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
			
			if(!usuario.getExecutado().equals(ParametrosArquivoLoginSIRESP.TEXTO_CONFIRMACAO_EXECUTADO.getDescricao()) && usuario.getObservacao().equals(""))
			{
			
				while(indexUnidade < listaDePerfisAProcessar.size() && !listaDePerfisAProcessar.get(indexUnidade).getUnidade().equals(usuario.getUnidade()))
				{				
					indexUnidade++;
				}
				
				if(indexUnidade == listaDePerfisAProcessar.size())
				{
					listaDePerfisAProcessar.add(new PerfisUsuariosSIRESP());
					listaDePerfisAProcessar.get(indexUnidade).setUnidade(usuario.getUnidade());
					listaDePerfisAProcessar.get(indexUnidade).novoUsuario();
					listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(0).setUsuario(usuario);
					listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(0).getPerfis().add(new PerfilSIRESP(usuario.getModulo(), usuario.getPerfil(), usuario.getHorarioDeAcessoAoPortal(), linhaExcel));
				}
				else
				{
		
					int indexUsuario = 0;
					
					if(usuario.getLogin() != null && !usuario.getLogin().equals(""))
					{
						boolean encontrado = false;
						
						while(indexUsuario < listaDePerfisAProcessar.get(indexUnidade).getUsuarios().size() && !encontrado)
						{
							//System.out.println(listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getLogin() + " - " + usuario.getLogin());
							if(listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getLogin().equals(usuario.getLogin()) || listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getNomeCompleto().equals(usuario.getNomeCompleto()))
								encontrado = true;
							else
								indexUsuario++;
						}
							
						if(indexUsuario < listaDePerfisAProcessar.get(indexUnidade).getUsuarios().size())
						{
							System.out.println("Incrementa usuário: " + listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getLogin() + " - " + usuario.getLogin());
							listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).setUsuario(usuario);
							listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getPerfis().add(new PerfilSIRESP(usuario.getModulo(), usuario.getPerfil(), usuario.getHorarioDeAcessoAoPortal(), linhaExcel));
						}
						else
						{
							listaDePerfisAProcessar.get(indexUnidade).novoUsuario();
							listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).setUsuario(usuario);
							listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getPerfis().add(new PerfilSIRESP(usuario.getModulo(), usuario.getPerfil(), usuario.getHorarioDeAcessoAoPortal(), linhaExcel));
							System.out.println("Novo usuário: " + listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getLogin() + " - " + usuario.getLogin());
						}
					}
					else
					{
						System.out.println("Veio por null: " + listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getUsuario().getLogin() + " - " + usuario.getLogin());
						listaDePerfisAProcessar.get(indexUnidade).novoUsuario();
						indexUsuario = listaDePerfisAProcessar.get(indexUnidade).getUsuarios().size() - 1;
						listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).setUsuario(usuario);
						listaDePerfisAProcessar.get(indexUnidade).getUsuarios().get(indexUsuario).getPerfis().add(new PerfilSIRESP(usuario.getModulo(), usuario.getPerfil(), usuario.getHorarioDeAcessoAoPortal(), linhaExcel));
					}
				}
			}
			
			
			linhaExcel++;
		}
		
		for(PerfisUsuariosSIRESP linhaPerfil : listaDePerfisAProcessar)
		{
			for(PerfisUsuariosSIRESP.Usuario usuario : linhaPerfil.getUsuarios())
			{
				System.out.println(linhaPerfil.getUnidade() + usuario.getUsuario().getLogin());
			}
		}
		
		for(PerfisUsuariosSIRESP linhaPerfil : listaDePerfisAProcessar)
		{
			
			String ErroAoAcessarUnidade = "";
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE.getTextoIdentificador());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), linhaPerfil.getUnidade());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador()).equals(linhaPerfil.getUnidade()))
			{
				System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador()) + " - " + linhaPerfil.getUnidade());
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
					
					if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()).toUpperCase().equals(IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador().toUpperCase()))
					{
						System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()) + " - " + IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_PERFIL.getTextoIdentificador().toUpperCase());
						ErroAoAcessarUnidade = IdentificadoresPaginaWebSIRESPDigital.ERRO_AO_ACESSAR_PERFIL.getTextoIdentificador();
					}
				}
				
			}
			
			if(ErroAoAcessarUnidade.equals(""))
			{
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				paginaWeb.clicarSpanPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_SPAN_USUARIO.getTextoIdentificador());
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				
				for(PerfisUsuariosSIRESP.Usuario usuario : linhaPerfil.getUsuarios())
				{
					ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
					
					if(usuario.getUsuario().getLogin() != null && !usuario.getUsuario().getLogin().equals(""))
					{
						paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_PESQUISAR_USUARIO_POR_LOGIN.getTextoIdentificador(), usuario.getUsuario().getLogin());
						paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_SELECT_TELA_INTERNA_FITLRO_STATUS.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.TEXTO_FILTRO_STATUS_VALOR_ATIVO.getTextoIdentificador());
												
						paginaWeb.clicarBotaoPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_BOTAO_PESQUISAR.getTextoIdentificador());
						while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					}
					
					ArrayList<ArrayList<String>> tabela = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
					
					ArrayList<UsuariosVinculadosSIRESP> usuariosVinculados = new ArrayList<UsuariosVinculadosSIRESP>();
					
					UsuariosVinculadosSIRESP usuarioVinculado = null;
					
					usuariosVinculados = obterUsuariosTabelaSIRESP(tabela);
					
					if(usuariosVinculados.size() > 0 &&  usuario.getUsuario().getLogin() != null && !usuario.getUsuario().getLogin().equals(""))
						usuarioVinculado = encontrarUsuarioVinculadoPorLogin(usuariosVinculados, usuario.getUsuario().getLogin());
					
					boolean cadastradoComSucesso = true;
					
					if(usuarioVinculado == null)
					{
						cadastradoComSucesso = cadastrarUsuarioNoSIRESP(driver, paginaWeb, usuario.getUsuario());
						
						if(cadastradoComSucesso)
						{
							ArrayList<ArrayList<String>> tabelaUsuario = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
							usuariosVinculados = obterUsuariosTabelaSIRESP(tabelaUsuario);
							//usuariosVinculados = obterUsuariosTabelaSIRESP(tabelaUsuario);
							usuarioVinculado = usuariosVinculados.get(0);
						}
					}
					
					
					if(cadastradoComSucesso)
					{
						
						if(!usuarioVinculado.getEmail().toUpperCase().equals(usuario.getUsuario().getEmail().toUpperCase()))
						{
							//ArrayList<ArrayList<String>> tabelaUsuario = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
							
							paginaWeb.clicarLinkPeloXPathDeLinhaEspecifica(driver, usuarioVinculado.getIdLinha(), IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_EDITAR_USUARIO.getTextoIdentificador());
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
							
							editarCadastroDeUsuarioNoSIRESP(driver, paginaWeb, usuario.getUsuario());
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
						}
				
						
						//obtendo perfis de acesso do usuário
						paginaWeb.clicarLinkPeloXPathDeLinhaEspecifica(driver, usuarioVinculado.getIdLinha(), IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_DROP_DOWN_LINHA_USUARIO.getTextoIdentificador());
						paginaWeb.clicarLinkPeloXPathDeLinhaEspecifica(driver, usuarioVinculado.getIdLinha(), IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_EDITAR_PERFIL_USUARIO.getTextoIdentificador());
						while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
						
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						ArrayList<String> linhasPerfisVisiveis = paginaWeb.encontrarIDsPorXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_CLASS_NAME_LINHA_PERFIL_VISIVEL.getTextoIdentificador());
						
						int linhaTabelaPerfilUsuario =  -1;
						ArrayList<String> perfisExistentes = new ArrayList<String>();
						for(String linhaVisivel : linhasPerfisVisiveis)
						{
							String numeroLinha = linhaVisivel.replace(IdentificadoresPaginaWebSIRESPDigital.PREFIXO_ID_LINHA_PERFIL.getTextoIdentificador(), "");
							
							if(!numeroLinha.equals(""))
							{
								String idModuloLinha = IdentificadoresPaginaWebSIRESPDigital.ID_DINAMICO_MODULO_LOGIN.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinha);
								
								if(paginaWeb.elementoEstaVazio(driver, idModuloLinha))
								{
									System.out.println("Vazio: " + idModuloLinha);
									linhaTabelaPerfilUsuario = Integer.parseInt(numeroLinha);
								}
								else
								{
									System.out.println("Preenchido: " + idModuloLinha);
									String modulo = paginaWeb.obterTextoInputText(driver, idModuloLinha);
									
									String idPerfilLinha = IdentificadoresPaginaWebSIRESPDigital.ID_DINAMICO_PERFIL_LOGIN.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinha);
									String perfil = paginaWeb.obterTextoInputText(driver, idPerfilLinha);
									perfisExistentes.add(tabelaDeParaPerfis.get(modulo) + "|" + tabelaDeParaPerfis.get(perfil));
									
									System.out.println("Adicionado como já existente: " + tabelaDeParaPerfis.get(modulo) + "|" + tabelaDeParaPerfis.get(perfil));
								}
							}
							else
								linhaTabelaPerfilUsuario = 0;
							
						}
						
						int contPerfil = 0;
						int numeroDaLinha = 0;
						int perfisAtribuidos = 0;
						
						if(linhaTabelaPerfilUsuario >= 0)
						{
							//paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPDigital.ID_BOTAO_ADICIONAR_PERFIL.getTextoIdentificador(), "id");
							
							adicionarPerfilAoUsuarioNaUnidade(driver, paginaWeb, usuario.getPerfis().get(0), numeroDaLinha);
							
							perfisAtribuidos++;
							
							celulas.add(new CelulaExcel(usuario.getPerfis().get(0).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_LOGIN.getIndice(), usuarioVinculado.getLogin().toLowerCase(), "String"));
							celulas.add(new CelulaExcel(usuario.getPerfis().get(0).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_EXECUTADO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_CONFIRMACAO_EXECUTADO.getDescricao(), "String"));
							celulas.add(new CelulaExcel(usuario.getPerfis().get(0).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_METODO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_METODO_EXECUTADO.getDescricao(), "String"));
							celulas.add(new CelulaExcel(usuario.getPerfis().get(0).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_OBSERVACAO.getIndice(), "", "String"));
							celulas.add(new CelulaExcel(usuario.getPerfis().get(0).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_STATUS.getIndice(), "", "String"));
							
							contPerfil++;
						}
						else
						{
							linhasPerfisVisiveis = paginaWeb.encontrarIDsPorXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_CLASS_NAME_TODAS_LINHAS.getTextoIdentificador());
							String numeroLinha = linhasPerfisVisiveis.get(linhasPerfisVisiveis.size()-1).replace(IdentificadoresPaginaWebSIRESPDigital.PREFIXO_ID_LINHA_PERFIL.getTextoIdentificador(), "");
							
							if(numeroLinha.equals(""))
								numeroDaLinha = -1;
							else
								numeroDaLinha = Integer.parseInt(numeroLinha);
						}
						
						
						
						for(;contPerfil < usuario.getPerfis().size(); contPerfil++)
						{
							if(!perfisExistentes.contains(usuario.getPerfis().get(contPerfil).getModulo() + "|" + usuario.getPerfis().get(contPerfil).getPerfil()))
							{
								System.out.println("Atribuir: " + usuario.getPerfis().get(contPerfil).getModulo() + "|" + usuario.getPerfis().get(contPerfil).getPerfil());
								
								paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPDigital.ID_BOTAO_ADICIONAR_PERFIL.getTextoIdentificador(), "id");
								numeroDaLinha++;
								
								adicionarPerfilAoUsuarioNaUnidade(driver, paginaWeb, usuario.getPerfis().get(contPerfil), numeroDaLinha);
								perfisAtribuidos++;
								
								perfisExistentes.add(usuario.getPerfis().get(contPerfil).getModulo() + "|" + usuario.getPerfis().get(contPerfil).getPerfil());
								
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_LOGIN.getIndice(), usuarioVinculado.getLogin().toLowerCase(), "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_EXECUTADO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_CONFIRMACAO_EXECUTADO.getDescricao(), "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_METODO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_METODO_EXECUTADO.getDescricao(), "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_OBSERVACAO.getIndice(), "", "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_STATUS.getIndice(), "", "String"));
							}
							else
							{
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_LOGIN.getIndice(), usuarioVinculado.getLogin().toLowerCase(), "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_EXECUTADO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_CONFIRMACAO_EXECUTADO.getDescricao(), "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_METODO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_METODO_EXECUTADO.getDescricao(), "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_OBSERVACAO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_PERFIL_JA_VINCULADO.getDescricao(), "String"));
								celulas.add(new CelulaExcel(usuario.getPerfis().get(contPerfil).getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_STATUS.getIndice(), "", "String"));
							}
						}
						
						if(perfisAtribuidos > 0)
						{
							paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPDigital.ID_BOTAO_CADASTRAR_PERFIS.getTextoIdentificador(), "id");
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
						}
						else
						{
							paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_VOLTAR_NOVO_PERFIL_USUARIO.getTextoIdentificador());
							while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
						}
						
						AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(caminhoCompletoArquivo, 0);
						arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_ATIVA.getDescricao(), celulas, true, false, 0, null);
					}
					else
					{
						System.out.println("Erro ao cadastrar usuário");
						
						for(PerfilSIRESP perfil : usuario.getPerfis())
						{
							celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_EXECUTADO.getIndice(), "", "String"));
							celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_METODO.getIndice(), "", "String"));
							celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_OBSERVACAO.getIndice(), ParametrosArquivoLoginSIRESP.TEXTO_ERRO_AO_CADASTRAR_USUARIO.getDescricao(), "String"));
							celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_STATUS.getIndice(), "", "String"));
						}
						
						AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(caminhoCompletoArquivo, 0);
						arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_ATIVA.getDescricao(), celulas, true, false, 0, null);
						
						paginaWeb.clicarSpanPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.TITULO_TELA_INTERNA_SPAN_USUARIO.getTextoIdentificador());
						while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					}
					
				}
			}
			else
			{
				ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
				
				for(PerfisUsuariosSIRESP.Usuario usuario : linhaPerfil.getUsuarios())
				{
					for(PerfilSIRESP perfil : usuario.getPerfis())
					{
						celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_EXECUTADO.getIndice(), "", "String"));
						celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_METODO.getIndice(), "", "String"));
						celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_OBSERVACAO.getIndice(), ErroAoAcessarUnidade, "String"));
						celulas.add(new CelulaExcel(perfil.getLinhaExcel(), ParametrosArquivoLoginSIRESP.INDICE_COLUNA_STATUS.getIndice(), "", "String"));
					}
				}
				
				AcoesArquivoExcel arquivoConsolidado = new AcoesArquivoExcel(caminhoCompletoArquivo, 0);
				arquivoConsolidado.gravarDadosEmCelula(ParametrosArquivoLoginSIRESP.NOME_PLANILHA_ATIVA.getDescricao(), celulas, true, false, 0, null);
			}

		}
		
		return "";	
	}
	
	private boolean adicionarPerfilAoUsuarioNaUnidade(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, PerfilSIRESP perfis, int numeroDaLinha)
	{

		String numeroLinhaCampos = String.valueOf(numeroDaLinha);
		String numeroLinhaDiv = String.valueOf(numeroDaLinha + 1);
		
		if(!numeroLinhaCampos.equals(""))
		{
			String idModuloLinha = IdentificadoresPaginaWebSIRESPDigital.ID_DINAMICO_MODULO_LOGIN.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinhaCampos);
			
			System.out.println(idModuloLinha);
			
			String xPathDivLinha = "";
			String xPathAcesso = "";
			
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				paginaWeb.digitarEmInputText(driver, idModuloLinha, perfis.getModulo());
				
				xPathAcesso = IdentificadoresPaginaWebSIRESPDigital.XPATH_DINAMICO_LINHA_PERFIL.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinhaDiv);
				xPathDivLinha = xPathAcesso.replace(IdentificadoresPaginaWebSIRESPDigital.SEGUNDA_MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.ID_DIV_DINAMICA_ACESSOS_MODULO.getTextoIdentificador());
				
				if(numeroDaLinha == 0)
					xPathDivLinha = xPathDivLinha.replace(IdentificadoresPaginaWebSIRESPDigital.SPAN_DEMAIS_LINHAS.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.SPAN_DEMAIS_LINHA_LINHA_ZERO.getTextoIdentificador());
					
				System.out.println(xPathDivLinha);

				paginaWeb.selecionarItemSelectULLIPeloDataValueDeUmaLinha(driver, xPathDivLinha, tabelaDeParaPerfis.get(perfis.getModulo()));
				
			}while(!paginaWeb.obterTextoInputText(driver, idModuloLinha).equals(tabelaDeParaPerfis.get(perfis.getModulo())));
			
			String idPerfilLinha = IdentificadoresPaginaWebSIRESPDigital.ID_DINAMICO_PERFIL_LOGIN.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinhaCampos);
			
			do
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				paginaWeb.digitarEmInputText(driver, idPerfilLinha, perfis.getPerfil());
				
	//			try {
	//				Thread.sleep(3000);
	//			} catch (InterruptedException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	
				xPathDivLinha = xPathAcesso.replace(IdentificadoresPaginaWebSIRESPDigital.SEGUNDA_MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.ID_DIV_DINAMICA_ACESSOS_PERFIL.getTextoIdentificador());
				

				if(numeroDaLinha == 0)
					xPathDivLinha = xPathDivLinha.replace(IdentificadoresPaginaWebSIRESPDigital.SPAN_DEMAIS_LINHAS.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.SPAN_DEMAIS_LINHA_LINHA_ZERO.getTextoIdentificador());
				
				
				paginaWeb.selecionarItemSelectULLIPeloDataValueDeUmaLinha(driver, xPathDivLinha, tabelaDeParaPerfis.get(perfis.getPerfil()));
			}while(!paginaWeb.obterTextoInputText(driver, idPerfilLinha).equals(tabelaDeParaPerfis.get(perfis.getPerfil())));
			
			String idHorarioLinha = IdentificadoresPaginaWebSIRESPDigital.ID_DINAMICO_HORARIO_LOGIN.getTextoIdentificador().replace(IdentificadoresPaginaWebSIRESPDigital.MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador(), numeroLinhaCampos);
			paginaWeb.selecionarItemSelect(driver, idHorarioLinha, IdentificadoresPaginaWebSIRESPDigital.TEXTO_SEM_RESTRICAO_DE_HORARIO.getTextoIdentificador());
		}
		
		return true;
	}
	
	private ArrayList<UsuariosVinculadosSIRESP> obterUsuariosTabelaSIRESP(ArrayList<ArrayList<String>> tabela)
	{
		ArrayList<UsuariosVinculadosSIRESP> usuariosVinculados = new ArrayList<UsuariosVinculadosSIRESP>();
		
		UsuariosVinculadosSIRESP usuarioVinculado = null;
		int numLinha = 0;
		for(ArrayList<String> linha : tabela)
		{
			System.out.println(numLinha++);
			for(String celula : linha)
				System.out.println(celula + "\t");
			System.out.println();
			
			if(linha.size() > 1)
			{
				usuarioVinculado = new UsuariosVinculadosSIRESP();
				usuarioVinculado.setIdLinha(linha.get(0));
				usuarioVinculado.setNome(linha.get(1));
				usuarioVinculado.setEmail(linha.get(2));
				usuarioVinculado.setLogin(linha.get(3));
				usuarioVinculado.setStatus(linha.get(4));
				
				usuariosVinculados.add(usuarioVinculado);
			}
		}
		
		return usuariosVinculados;
	}
	
	
	private boolean cadastrarUsuarioNoSIRESP(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, UsuarioSIRESP usuario)
	{
		boolean cadastradoComSucesso = true;
		
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_NOVO_USUARIO.getTextoIdentificador());
		while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
		
		paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_NOME.getTextoIdentificador(), usuario.getNomeCompleto());
		paginaWeb.tirarFocoDoCampoTexto(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_NOME.getTextoIdentificador());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_MODAL_ALERTA_NOMES_ABREVIADOS.getTextoIdentificador()))
			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_CONFIRMAR_NOMES_ABREVIADOS.getTextoIdentificador());
		
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
			
			cadastradoComSucesso = editarCadastroDeUsuarioNoSIRESP(driver, paginaWeb, usuario);
		}
		
		return cadastradoComSucesso;
	}
	
	private boolean editarCadastroDeUsuarioNoSIRESP(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, UsuarioSIRESP usuario)
	{
		boolean cadastradoComSucesso = true;
		
		if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_ALERTA_ERRO_INCLUIR_USUARIO.getTextoIdentificador()))
		{
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_NOME.getTextoIdentificador(), usuario.getNomeCompleto());
			paginaWeb.tirarFocoDoCampoTexto(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_NOME.getTextoIdentificador());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_MODAL_ALERTA_NOMES_ABREVIADOS.getTextoIdentificador()))
				paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_CONFIRMAR_NOMES_ABREVIADOS.getTextoIdentificador());
			
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_EMAIL.getTextoIdentificador(), usuario.getEmail());
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_RG.getTextoIdentificador(), usuario.getRG());
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CPF.getTextoIdentificador(), usuario.getCPF());
			paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TEXT_USUARIO_CELULAR.getTextoIdentificador(), usuario.getTelefone());
			
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPDigital.ID_BOTAO_ATUALIZAR_USUARIO.getTextoIdentificador(), "id");
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
		}
		else
		{
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPDigital.ID_BOTAO_INCLUIR_USUARIO.getTextoIdentificador(), "id");
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
		}
		
		if(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_ALERTA_ERRO_INCLUIR_USUARIO.getTextoIdentificador()))
		{
			cadastradoComSucesso = false;
		}
		else if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_CONFIRMACAO_SUCESSO_INCLUIR_USUARIO.getTextoIdentificador()))
		{
			cadastradoComSucesso = false;
		}
		
		
		return cadastradoComSucesso;
	}
	
	private ArrayList<UsuarioSIRESP> lerEntidades(String nomeArquivo)
	{		
		ArrayList<UsuarioSIRESP> usuarios;
		
		try (FileInputStream in = new FileInputStream(nomeArquivo)) {
			usuarios = ExcelBinder.readSheet(
                    in,
                    UsuarioSIRESP.class,
                    null,     // ou null para a primeira
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
		tabelaDeParaPerfis.put("Gestor de Acessos", "GESTOR_DE_ACESSOS | Gestor de acessos");
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
