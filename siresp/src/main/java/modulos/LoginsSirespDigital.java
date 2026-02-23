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
import modelosDados.UsuarioSIRESP;
import modelosDados.UsuariosVinculadosSIRESP;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class LoginsSirespDigital {
	
	private String pastaComDadosDeLogin;
	private Map<String, Boolean> usuariosDistintos;;
	
	
	public String tratarLoginsSIRESP(WebDriver driver)
	{			
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		criarListaUsuariosDistintos();
		
	
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
		
		ArrayList<UsuariosVinculadosSIRESP> usuariosVinculados = new ArrayList<UsuariosVinculadosSIRESP>();
		
		for(UsuarioSIRESP usuario : usuarios)
		{
			
			if(!unidadeAtual.equals(usuario.getUnidade()))
			{
				if(usuariosVinculados.size() > 0)
				{
					//removerPerfisDeCadaUsuarioVinculado
					//salvarEmArquivoOsRegistrosRemovidos
					//zerarListaDeUsuariosVinculado
				}
				
				if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_UNIDADE.getTextoIdentificador()))
					paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_UNIDADE.getTextoIdentificador());
				paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), usuario.getUnidade());
				
				if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador()))
				{
					paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador());
					paginaWeb.digitarEmInputText(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INICIAL_TEXTO_FILTRO_MODULO.getTextoIdentificador(), usuario.getModulo());
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
					
					selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador(), usuario.getModulo().toUpperCase());
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
				
				for(ArrayList<String> linha : tabela)
				{
					for(String celula : linha)
						System.out.println(celula + "\t");
					System.out.println();
					
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
			
			if(usuario.getLogin() != null && usuario.getLogin().equals(""))
				usuarioVinculado = encontrarUsuarioVinculadoPorLogin(usuariosVinculados, usuario.getLogin());
			
			
			if(usuarioVinculado == null)
			{
				ArrayList<UsuariosVinculadosSIRESP> usuariosVinculadosComMesmoNome = encontrarUsuarioVinculadoPorNome(usuariosVinculados, usuario.getNomeCompleto());
				
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
				cadastrarUsuarioNoSIRESP(driver, paginaWeb, usuario);

				ArrayList<ArrayList<String>> tabela = paginaWeb.obterTableComIdDaLinhaEPaginacao(driver, IdentificadoresPaginaWebSIRESPDigital.ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO.getTextoIdentificador(), IdentificadoresPaginaWebSIRESPDigital.CLASS_NAME_TELA_INTERNA_PAGINACAO.getTextoIdentificador());
				
				
				
				//atribuirPerfil
				//atribuirLoginAtual
			}
			else
			{
				//seHaMaisCadastrosComMesmoNome
				//buscarPerfisCadastradosParaOUsuarioNosOutrosNomes
				
				//editarCadastro
				//atribuirPerfisAoUsuario
				
				//atribuirLoginAtual
				//removerUsuarioVinculadoDaListaDeUsuariosVinculadosSIRESP
			}
			
			unidadeAtual = usuario.getUnidade();
			usuarioAtual = usuario.getNomeCompleto();
			break;
			
		}
		
		return "";	
	}
	
	
	private void cadastrarUsuarioNoSIRESP(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, UsuarioSIRESP usuario)
	{
		paginaWeb.clicarLinkPeloTitulo(driver, IdentificadoresPaginaWebSIRESPDigital.XPATH_BOTAO_NOVO_USUARIO.getTextoIdentificador());
		
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
		
		usuariosDistintos.put("DANIBDSANTOS", true);
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
		usuariosDistintos.put("VABERNI", true);
		usuariosDistintos.put("LODCOSTA", true);
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
