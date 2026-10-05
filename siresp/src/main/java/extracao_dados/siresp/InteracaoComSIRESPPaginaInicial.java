package extracao_dados.siresp;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.DataTruncation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.DuplicateHeaderMode;
import org.openqa.selenium.BuildInfo;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.network.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.IdentificadoresPaginaWebSIRESPInicio;
import dadosGerais.IdentificadoresPastasCompartilhadasCDIDRUrgencia;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import io.opentelemetry.sdk.metrics.data.Data;
import modelosDados.EntidadeLeito;
import modelosDados.UrgenciaAguardandoDetalhado;
import modulos.AbrirGoogleChrome;
import modulos.Absenteismo;
import modulos.CadastroUsuarioSIRESPDigital;
import modulos.CensoLeitos;
import modulos.DemandaReprimida;
import modulos.FilaNominalAgendamentosPendentes;
import modulos.FilaNominalCDRNaoRegulada;
import modulos.FilaNominalSolicitacoesPendentes;
import modulos.LoginsSirespDigital;
import modulos.OfertaDemandaDeAcesso;
import modulos.OfertaDemandaDeAcessoR1;
import modulos.RemoverDaFilaCentralReg;
import modulos.RemoverDuplicadoDeFila;
import modulos.UrgenciaAguardando;
import modulos.UrgenciaFichasAssumidas;
import modulos.UrgenciaFinalizado;
import utils.Utils;

/**
 * Para acessar o selenium em uma sessão já existente, o Google Chrome deve ser aberto em modo de depuração
 * "C:\Program Files\Google\Chrome\Application\chrome.exe" --remote-debugging-port=9222 --user-data-dir="C:\chrome-temp"
 *
 */
public class InteracaoComSIRESPPaginaInicial 
{
	
	public static String CPF;
	public static String RG;
	
	private static String XpathModulo;
	private static String IdLogin;
	private static String IdSenha;
	private static String IdTextoCaptcha;
	private static String IdBotaoEntrar;
	private static String idImagemCaptcha;
	
    public static void main( String[] args )
    {
    	
    	String nomeUsuario = System.getProperty("user.name");
    	System.out.println(nomeUsuario);
		HashMap<String, String> mapaDeAcessos = new HashMap<String, String>();
    	
    	try {
			
			Reader reader = null;
			
			reader = new InputStreamReader(new FileInputStream("C:\\Users\\" + nomeUsuario + "\\Documents\\SIRESP\\parametros_acesso.csv"), StandardCharsets.ISO_8859_1);
				
			CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(';').setQuote('"').setHeader().setSkipHeaderRecord(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).build();
			
			Iterable<CSVRecord> registros = format.parse(reader);
			for(CSVRecord registro : registros)						
			{
				mapaDeAcessos.put(registro.get(0), registro.get(1));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, "Erro ao encontrar o arquivo com as credenciais de acesso");
			return;
		}
		
    	String usuarioComum = mapaDeAcessos.get("USUARIO_CDR");
    	String senhaUsuarioComum = mapaDeAcessos.get("SENHA_CDR");
    	String cpfUsuarioComum = mapaDeAcessos.get("CPF_CDR");
    	String rgUsuarioComum = mapaDeAcessos.get("RG_CDR");
    	
    	String usuarioTARM = mapaDeAcessos.get("USUARIO_REGULADA");
    	String senhaUsuarioTARM = mapaDeAcessos.get("SENHA_REGULADA");
    	String cpfUsuarioTARM = mapaDeAcessos.get("CPF_REGULADA");
    	String rgUsuarioTARM = mapaDeAcessos.get("RG_REGULADA");
    	
    	String usuarioUrgencia = mapaDeAcessos.get("USUARIO_URGENCIA");
    	String senhaUsuarioUrgencia = mapaDeAcessos.get("SENHA_URGENCIA");
    	String cpfUsuarioUrgencia = mapaDeAcessos.get("CPF_URGENCIA");
    	String rgUsuarioUrgencia = mapaDeAcessos.get("RG_URGENCIA");
    	
    	//ChromeOptions options = new ChromeOptions();
    	
    	int escolha = -1;
    	int maximoEscolha = 11;
    	boolean abrirMenuInicial = false;
    	String ambiente;
    	
    	if(args.length == 0)
    	{
    		abrirMenuInicial = true;
    		ambiente = "TESTE";
    	}
    	else
    	{
    		try
    		{
    			escolha = Integer.parseInt(args[0]);
    			
    			if(escolha < 0 || escolha > maximoEscolha)
    				abrirMenuInicial = true;
    			
    		}catch(Exception e)
    		{
    			abrirMenuInicial = true;
    		}
    		
    		if(args.length > 1)
    			ambiente = args[1];
    		else
    			ambiente = "TESTE";
    	}
    	
//    	options.addArguments("user-data-dir=C:/Usuários/" + nomeUsuario + "/AppData/Local/Google/Chrome/User Data");
//    	options.addArguments("profile-directory=Default");
    	
    	//System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe");

//    	ChromeOptions options = new ChromeOptions(); 
//    	options.addArguments("user-data-dir=C:/chrome-temp"); 
//    	options.addArguments("profile-directory=Default");    	
//        
//    	options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
//    	options.addArguments("--remote-allow-origins=*"); 
    	
    	String[] opcoesChrome = {"Sim", "Não"}; 
        //int escolhaChrome = JOptionPane.showOptionDialog( null, "Deseja abrir o Google Chrome no modo de processamento?", "Google Chrome", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoesChrome, opcoesChrome[0] );
    	int escolhaChrome = 0;
    	
    	boolean chromeRunning = isPortOpen("localhost", 9222);
        
        if(!chromeRunning)
        {
        	AbrirGoogleChrome chrome = new AbrirGoogleChrome();
        	try {
				chrome.abrir(nomeUsuario);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
		//System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe"); 
		ChromeOptions options = new ChromeOptions(); 
		options.addArguments("user-data-dir=C:\\chrome-temp"); 
		options.addArguments("profile-directory=Default"); // ou "Profile 1" 
		options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");

    	WebDriver driver = new ChromeDriver(options);

    	//driver.get("about:blank");
    	//((JavascriptExecutor) driver).executeScript("window.focus();");
    	
    	String paginaInicial = "https://www.siresp.saude.sp.gov.br";
    	InteracaoComSIRESPPaginaInicial.CPF = "04633274627";
    	InteracaoComSIRESPPaginaInicial.RG = "558864338";
    	
        driver.get(paginaInicial);
        
        //driver.navigate().refresh();
        
        BuildInfo buildInfo = new BuildInfo();
        System.out.println(buildInfo.getReleaseLabel());

        System.out.println("Page Title: " + driver.getTitle());
        
        if(abrirMenuInicial)
        {
	        String[] opcoes = {
	        		"Censo diário de Leitos"
	        		, "Filas Nominais CDR (Não Regulada)"
	        		, "Filas Nominais Regulada (Agendamentos)"
	        		, "Filas Nominais Regulada (Solicitacoes)"
	        		, "Absenteísmo"
	        		, "Perfis novo SIRESP"
	        		}; 
	        escolha = JOptionPane.showOptionDialog( null, "Escolha uma opção:", "Caixa de Seleção", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0] );
        }
        
        if(escolha == 0)
        {
        	boolean consolidarAlgumMes = false;
        	
        	if(args.length > 2)
        	{
        		if(args[2].equals("SIM"))
        		{
        			consolidarAlgumMes = true;
        		}
        	}
        	
	        CensoLeitos censo = new CensoLeitos();
	        censo.executarCenso(driver, consolidarAlgumMes);
        }
        else if(escolha == 1)
        {
        	AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
        	
        	String retorno;
        	
        	driver.get(paginaInicial);
        	retorno = InteracaoComSIRESPPaginaInicial.acessarModuloAmbulatorial(driver, paginaWeb, paginaInicial, usuarioTARM, senhaUsuarioTARM, cpfUsuarioTARM, rgUsuarioTARM, nomeUsuario);
        	if(retorno.equals(""))
        	{
        		FilaNominalAgendamentosPendentes filaNominalReguladaAgendamento = new FilaNominalAgendamentosPendentes("C:\\Users\\" + nomeUsuario + "\\OneDrive - Prefeitura Municipal de Campinas\\01. AMBULATORIAL\\FILAS NOMINAIS - UNIDADES", "C:\\Users\\" + nomeUsuario + "\\Downloads");
        		filaNominalReguladaAgendamento.baixarFilaAgendamentosPendentes(driver);
				
				FilaNominalSolicitacoesPendentes filaNominalReguladaSolicitacao = new FilaNominalSolicitacoesPendentes("C:\\Users\\" + nomeUsuario + "\\OneDrive - Prefeitura Municipal de Campinas\\01. AMBULATORIAL\\FILAS NOMINAIS - UNIDADES", "C:\\Users\\" + nomeUsuario + "\\Downloads");
				filaNominalReguladaSolicitacao.baixarFilaSolicitacoesPendentes(driver);
				
				paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPInicio.XPATH_BOTAO_FECHAR.getTextoIdentificador());
				///html/body/form/table/tbody/tr[2]/td/table/tbody/tr/td/table[1]/tbody/tr/td[5]/a
        	}
        	
        	driver.get(paginaInicial);
        	retorno = InteracaoComSIRESPPaginaInicial.acessarModuloAmbulatorial(driver, paginaWeb, paginaInicial, usuarioComum, senhaUsuarioComum, cpfUsuarioComum, rgUsuarioComum, nomeUsuario);
        	
        	if(retorno.equals(""))
        	{
        		FilaNominalCDRNaoRegulada filaNominalCDR = new FilaNominalCDRNaoRegulada("C:\\Users\\" + nomeUsuario + "\\OneDrive - Prefeitura Municipal de Campinas\\01. AMBULATORIAL\\FILAS NOMINAIS - UNIDADES", "C:\\Users\\" + nomeUsuario + "\\Downloads");
				filaNominalCDR.baixarFilaCDR(driver);
				
				paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPInicio.XPATH_BOTAO_FECHAR.getTextoIdentificador());
//				///html/body/form/table/tbody/tr[2]/td/table/tbody/tr/td/table[1]/tbody/tr/td[5]/a
        	}

        	LocalDate dataHoje = LocalDate.now();
        	DemandaReprimida demandaReprimida = new DemandaReprimida("C:\\Users\\" + nomeUsuario, dataHoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        	demandaReprimida.montarDemandaReprimidaDiaria("PRODUCAO");
        	
//        	String competenciaInicial;
//        	String competenciaFinal;
//        	
//        	int dia = dataHoje.getDayOfMonth();
//        	int mes = dataHoje.getMonthValue();
//        	int ano = dataHoje.getYear();
//        	int anoAnterior = ano;
//        	int proximoAno = ano;
//        	
//        	int mesAnterior = mes - 1;
//        	int proximoMes = mes + 1;
//        	
//        	if(mesAnterior < 1)
//        	{
//        		mesAnterior = 12;
//        		anoAnterior = ano - 1;
//        	}
//        	
//        	if(proximoMes > 12)
//        	{
//        		proximoMes = 1;
//        		proximoAno = ano + 1;
//        	}
//        	
//        	if(dataHoje.getDayOfMonth() <= 10)
//        	{
//        		if(mesAnterior < 10)
//        			competenciaInicial = "0";
//        		competencialInicial
//        	}
        }
        else if(escolha == 2)
        {
        	FilaNominalAgendamentosPendentes filaNominalRegulada = new FilaNominalAgendamentosPendentes();
        	filaNominalRegulada.baixarFilaAgendamentosPendentes(driver);
        }
        else if(escolha == 3)
        {
        	FilaNominalSolicitacoesPendentes filaNominalRegulada = new FilaNominalSolicitacoesPendentes();
        	filaNominalRegulada.baixarFilaSolicitacoesPendentes(driver);
        } 
        else if(escolha == 4)
        {
        	Absenteismo absenteismo = new Absenteismo();
        	//absenteismo.verificarAbsenteismo(driver, null, null);
        	absenteismo.verificarAbsenteismo(driver, null, null);
        	//absenteismo.parametrizarArquivosVazios(driver);C:\Users\PMC514991-2\Documents\Oferta e Demanda
        }
        else if(escolha == 5)
        {
        	CadastroUsuarioSIRESPDigital cadastroSIRESP = new CadastroUsuarioSIRESPDigital();
        	cadastroSIRESP.cadastrarListaDeAcessosSIRESP(driver);
        }
        else if(escolha == 6)
        {
        	LoginsSirespDigital loginsSIRESP = new LoginsSirespDigital();
        	loginsSIRESP.listarTodosAcessosSIRESP(driver);
        }
        else if(escolha == 7)
        {
        	OfertaDemandaDeAcesso ofertaDemanda = new OfertaDemandaDeAcesso();
        	ofertaDemanda.calcularOfertaEDemanda(driver);
        }
        else if(escolha == 8)	
        {
        	OfertaDemandaDeAcessoR1 ofertaDemanda = new OfertaDemandaDeAcessoR1();
        	//ofertaDemanda.calcularOfertaEDemanda(driver, "05/2026", "05/2026", false, false, false, false, false, false, true, false, false, false, ambiente);
        	//ofertaDemanda.calcularOfertaEDemanda(driver, "05/2026", "05/2026", true, true, true, true, true, false, false, false, false, false, ambiente);
        	
        	//diário
        	ofertaDemanda.calcularOfertaEDemanda(driver, null, null, false, true, true, false, true, true, true, true, true, true, ambiente);
        	
        	//consolidação de novas demandas regulada
        	//ofertaDemanda.calcularOfertaEDemanda(driver, null, null, false, true, true, true, true, true, true, true, true, true, ambiente);
        }
        
        else if(escolha == 9)
        {
        	RemoverDaFilaCentralReg filaCentralReg = new RemoverDaFilaCentralReg();
        	filaCentralReg.remvoverRegistrosCentralReg(driver);
        	
        	/*
        	RemoverDuplicadoDeFila filaCentralReg = new RemoverDuplicadoDeFila();
        	filaCentralReg.remvoverRegistrosCentralReg(driver);
        	*/
        }
        else if(escolha == 10)
        {
        	AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
        	
        	String pastaBase = "C:\\Users\\" + nomeUsuario;
        	String pastaDownloads = "C:\\Users\\" + nomeUsuario + "\\Downloads";
        	
        	String retorno;
        	
        	driver.get(paginaInicial);
        	retorno = InteracaoComSIRESPPaginaInicial.acessarModuloUrgencia(driver, paginaWeb, paginaInicial, usuarioUrgencia, senhaUsuarioUrgencia, cpfUsuarioUrgencia, rgUsuarioUrgencia, nomeUsuario);
        	if(retorno.equals(""))
        	{
	        	UrgenciaAguardando urgenciaAguardando = new UrgenciaAguardando(pastaBase, ambiente);
	        	urgenciaAguardando.obterAgrupamentoDeEsperaUrgencia(driver, ambiente);
	       
	        	UrgenciaFinalizado urgenciaFinalizado = new UrgenciaFinalizado(pastaBase, ambiente);
	        	UrgenciaFichasAssumidas urgenciaFichasAssumidas = new UrgenciaFichasAssumidas(pastaBase, ambiente);
	        	
	        	//String pastaBase = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta compartilhada", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
	    		//String pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE).trim();
	        	//urgenciaFinalizado.obterAgrupamentoDeEsperaUrgencia(driver, ambiente, null, pastaBase, pastaDownloads);
	        	
	        	
	        	//recuperando a última data processada, caso o arquivo esteja vazio, será considerada a data de ontem (atual - 1)
	        	AcoesArquivoExcel arquivoExcel = new AcoesArquivoExcel(urgenciaFinalizado.getCaminhoArquivoConsolidado(), 0);
	        	arquivoExcel.abrirPlanilha(urgenciaFinalizado.getUltimaPlanilhaProcessadaNoDia(), urgenciaFinalizado.getPrimeiraLinhaDaUltimaPlanilhaProcessadaNoDia());
	        	
	        	String valorData = arquivoExcel.getValorDaCelulaComoString(urgenciaFinalizado.getPrimeiraLinhaDaUltimaPlanilhaProcessadaNoDia(), urgenciaFinalizado.getColunaDataDaUltimaPlanilhaProcessadaNoDia(), "dd/MM/yyyy");
	        	
	        	LocalDate dataInicial;
	        	LocalDate dataFinal;;
	        	
	        	LocalDate hoje = LocalDate.now();
	        	LocalDate ontem = hoje.minusDays(1);
	        	
	        	try {
	        		
	        		System.out.println("Última data: " + valorData);
	        		dataInicial = LocalDate.parse(valorData, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        		
	        		if(dataInicial.isBefore(ontem))
		        		dataInicial.plusDays(1);
	        			
					
				} catch (Exception e) {
					// TODO: handle exception
					
		        	dataInicial = ontem;
				}
	        
	        	dataFinal = ontem;
	        	
	        	System.out.println("Data inicial: " + dataInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	        	System.out.println("Data final: " + dataFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	        	
	        	for(LocalDate data = dataInicial; !data.isAfter(dataFinal); data = data.plusDays(1))
	        	{
	        		String dataString = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        		System.out.println(dataString);
	        	    
	        		urgenciaFinalizado.obterAgrupamentoFinalizadoUrgencia(driver, ambiente, dataString, pastaBase, pastaDownloads);
	        		urgenciaFichasAssumidas.obterProducaoReguladorUrgencia(driver, ambiente, dataString, pastaBase, pastaDownloads);
	        	}
        	}
        }
        
        driver.close();
    }
    
    public static String acessarModuloAmbulatorial(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String paginaInicial, String login, String senha, String cpf, String rg, String nomeUsuario)
    {
    	String destinoImagem = "C:\\Users\\" + nomeUsuario + "\\Downloads\\imagem.png";
    	InteracaoComSIRESPPaginaInicial.CPF = cpf;
    	InteracaoComSIRESPPaginaInicial.RG = rg;
	    
		XpathModulo = IdentificadoresPaginaWebSIRESPInicio.XPATH_MODULO_AMBULATORIAL.getTextoIdentificador();
    	IdLogin = IdentificadoresPaginaWebSIRESPInicio.ID_TEXTO_LOGIN_AMBULATORIAL.getTextoIdentificador();
    	IdSenha = IdentificadoresPaginaWebSIRESPInicio.ID_TEXTO_SENHA_AMBULATORIAL.getTextoIdentificador();
    	idImagemCaptcha = IdentificadoresPaginaWebSIRESPInicio.ID_IMAGEM_CAPTCHA_AMBULATORIAL.getTextoIdentificador();
    	IdTextoCaptcha = IdentificadoresPaginaWebSIRESPInicio.ID_TEXTO_CAPTCHA_AMBULATORIAL.getTextoIdentificador();
    	IdBotaoEntrar = IdentificadoresPaginaWebSIRESPInicio.ID_BOTAO_ENTRAR_AMBULATORIAL.getTextoIdentificador();
    	
		String retorno = InteracaoComSIRESPPaginaInicial.autenticarNoSistema(driver, paginaWeb, "", destinoImagem, login, senha);
		
		while(!retorno.equals(""))
		{
			driver.get(paginaInicial);
			retorno = InteracaoComSIRESPPaginaInicial.autenticarNoSistema(driver, paginaWeb, "", destinoImagem, login, senha);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InteracaoComSIRESPPaginaInicial.acessarUnidadeSMSCampinas(driver, paginaWeb);
		paginaWeb.voltarAoFramePai(driver);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InteracaoComSIRESPPaginaInicial.validarDocumento(driver, paginaWeb);
		
		return "";
    }
    
    public static String acessarModuloUrgencia(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String paginaInicial, String login, String senha, String cpf, String rg, String nomeUsuario)
    {
    	String destinoImagem = "C:\\Users\\" + nomeUsuario + "\\Downloads\\imagem.png";
    	InteracaoComSIRESPPaginaInicial.CPF = cpf;
    	InteracaoComSIRESPPaginaInicial.RG = rg;
	    
		XpathModulo = IdentificadoresPaginaWebSIRESPInicio.XPATH_MODULO_URGENCIA.getTextoIdentificador();
    	IdLogin = IdentificadoresPaginaWebSIRESPInicio.ID_TEXTO_LOGIN_URGENCIA.getTextoIdentificador();
    	IdSenha = IdentificadoresPaginaWebSIRESPInicio.ID_TEXTO_SENHA_URGENCIA.getTextoIdentificador();
    	idImagemCaptcha = IdentificadoresPaginaWebSIRESPInicio.ID_IMAGEM_CAPTCHA_URGENCIA.getTextoIdentificador();
    	IdTextoCaptcha = IdentificadoresPaginaWebSIRESPInicio.ID_TEXTO_CAPTCHA_URGENCIA.getTextoIdentificador();
    	IdBotaoEntrar = IdentificadoresPaginaWebSIRESPInicio.ID_BOTAO_ENTRAR_URGENCIA.getTextoIdentificador();
    	
		String retorno = InteracaoComSIRESPPaginaInicial.autenticarNoSistema(driver, paginaWeb, "", destinoImagem, login, senha);
		
		while(!retorno.equals(""))
		{
			driver.get(paginaInicial);
			retorno = InteracaoComSIRESPPaginaInicial.autenticarNoSistema(driver, paginaWeb, "", destinoImagem, login, senha);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retorno = InteracaoComSIRESPPaginaInicial.acessarUnidadeCentralRegMunicipal(driver, paginaWeb);
		
		if(retorno.equals(""))
			paginaWeb.voltarAoFramePai(driver);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InteracaoComSIRESPPaginaInicial.validarDocumento(driver, paginaWeb);
		
		return "";
    }
    
    public static String autenticarNoSistema(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String urlInicialImagem, String destino, String login, String senha)
    {
    	paginaWeb.clicarLinkPeloXPath(driver, XpathModulo);
    	
    	paginaWeb.preencherInputText(driver, IdLogin, login);
    	paginaWeb.preencherInputText(driver, IdSenha, senha);
    	
    	paginaWeb.baixarScreenshot(driver, urlInicialImagem, idImagemCaptcha, destino);
    	
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	String textoCaptcha = Utils.obterTextoDeImagem(destino);
    	
    	paginaWeb.preencherInputText(driver, IdTextoCaptcha, textoCaptcha);
    	
    	paginaWeb.clicarBotaoSubmit(driver, IdBotaoEntrar, "id");

    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPInicio.XPATH_BOTAO_OK_CODIGO_SEGURANCA_INVALIDO.getTextoIdentificador()))
    	{
    		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebSIRESPInicio.XPATH_BOTAO_OK_CODIGO_SEGURANCA_INVALIDO.getTextoIdentificador());
    		return IdentificadoresPaginaWebSIRESPInicio.TEXTO_CODIGO_SEGURANCA_INVALIDO.getTextoIdentificador();
    	}
    	
    	return "";
    }
    
    
    public static String validarDocumento(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb)
    {
    	if(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebSIRESPInicio.XPATH_TEXTO_DIGITOS_DOCUMENTOS.getTextoIdentificador()))
    	{
    		String textoLabel = paginaWeb.obterTextoPorXPathDeSpan(driver, IdentificadoresPaginaWebSIRESPInicio.XPATH_TEXTO_DOCUMENTO_SOLICITADO.getTextoIdentificador());
    		String documento = "";
    		
    		if(textoLabel.contains(IdentificadoresPaginaWebSIRESPInicio.TEXTO_RG.getTextoIdentificador()))
    			documento = RG;
    		else if(textoLabel.contains(IdentificadoresPaginaWebSIRESPInicio.TEXTO_CPF.getTextoIdentificador()))
    			documento = CPF;
    		
    		if(!documento.equals(""))
    		{
    			int quantidadeDigitos = IdentificadoresPaginaWebSIRESPInicio.QUANTIDADE_DIGITOS.getIndice();
	    		String digitosDocumento = "";
	    		if(textoLabel.contains(IdentificadoresPaginaWebSIRESPInicio.TEXTO_ULTIMOS.getTextoIdentificador()))
	    			digitosDocumento = documento.substring(documento.length() - quantidadeDigitos, documento.length());
	    		else if(textoLabel.contains(IdentificadoresPaginaWebSIRESPInicio.TEXTO_PRIMEIROS.getTextoIdentificador()))
	    			digitosDocumento = documento.substring(0, quantidadeDigitos);
	    				
	    		if(!digitosDocumento.equals(""))
	    		{
	    			paginaWeb.preencherInputTextPeloXPATH(driver, IdentificadoresPaginaWebSIRESPInicio.XPATH_TEXTO_DIGITOS_DOCUMENTOS.getTextoIdentificador(), digitosDocumento);
	    			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESPInicio.ID_BOTAO_ENTRAR_VALIDAR_DOCUMENTO.getTextoIdentificador(), "id");
	    		}
    		}
    	}
    	
    	return "";
    }
    
    public static String acessarUnidadeSMSCampinas(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb)
    {
    	String valueSMSCampinas = "778_SMS - CAMPINAS";
    	//String value = elementosRadioUnidades.get("5416655 - SMS - CAMPINAS");
		//System.out.println(value);
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
	
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
		boolean unidadeEncontrada = paginaWeb.clicarRadioInputByValue(driver, valueSMSCampinas);
		
		paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_BOTAO_OK_ESCOLHER_UNIDADE.getTextoIdentificador(), "id");
		
		return "";
    }
    
    public static String acessarUnidadeCentralRegMunicipal(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb)
    {
    	String valueSMSCampinas = "9933_CENTRAL REGULAÇÃO MUNICIPAL - CAMPINAS_45";
    	//String value = elementosRadioUnidades.get("5416655 - SMS - CAMPINAS");
		//System.out.println(value);
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
	
		if(paginaWeb.haElementosPorID(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador()))
		{
			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		
			boolean unidadeEncontrada = paginaWeb.clicarRadioInputByValue(driver, valueSMSCampinas);
		
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.NAME_URGENCIA_BOTAO_OK_ESCOLHER_UNIDADE.getTextoIdentificador(), "name");
		}
		else
			return "acesso direto";
		
		return "";
    }
    
    public static boolean isPortOpen(String host, int port) {
    	try (Socket socket = new Socket(host, port)) {

    		return true;

    	} catch (Exception e) {

    		return false;

    	}

    }
}

