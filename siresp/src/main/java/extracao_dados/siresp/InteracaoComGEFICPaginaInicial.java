package extracao_dados.siresp;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.sun.jna.platform.win32.Shell32Util;
import com.sun.jna.platform.win32.ShlObj;

import dadosGerais.IdentificadoresPaginaWebGEFIC;
import dadosGerais.IdentificadoresPaginaWebSIRESPInicio;
import interacao_externa.AcoesGeraisPaginaWeb;
import modelosDados.UrgenciaAguardandoDetalhado;
import modulos.AbrirGoogleChrome;
import modulos.Absenteismo;
import modulos.CadastroUsuarioSIRESPDigital;
import modulos.CensoLeitos;
import modulos.FilaNominalAgendamentosPendentes;
import modulos.FilaNominalCDRNaoRegulada;
import modulos.FilaNominalSolicitacoesPendentes;
import modulos.LoginsSirespDigital;
import modulos.OfertaDemandaDeAcesso;
import modulos.OfertaDemandaDeAcessoR1;
import modulos.RemoverDaFilaCentralReg;
import modulos.RemoverDuplicadoDeFila;
import modulos.UrgenciaAguardando;
import modulos.UrgenciaFinalizado;
import modulosGEFIC.ConsolidadoGEFIC;
import modulosGEFIC.FilasNominaisGEFIC;
import modulosGEFIC.RelatoriosGEFIC;
import utils.Utils;

/**
 * Para acessar o selenium em uma sessão já existente, o Google Chrome deve ser aberto em modo de depuração
 * "C:\Program Files\Google\Chrome\Application\chrome.exe" --remote-debugging-port=9222 --user-data-dir="C:\chrome-temp"
 *
 */
public class InteracaoComGEFICPaginaInicial 
{
	private static String IdLogin;
	private static String IdSenha;
	private static String IdBotaoEntrar;
	
    public static void main( String[] args )
    {
    	String nomeUsuario = System.getProperty("user.name");
    	
    	String pastaInicial = System.getProperty("user.home");
    	
    	System.out.println(nomeUsuario);
    	System.out.println(pastaInicial);
		HashMap<String, String> mapaDeAcessos = new HashMap<String, String>();
    	
    	try {
    		String documentos = Shell32Util.getFolderPath(ShlObj.CSIDL_MYDOCUMENTS);
    		
			Reader reader = null;
			
			reader = new InputStreamReader(new FileInputStream(documentos + "\\SIRESP\\parametros_acesso.csv"), StandardCharsets.ISO_8859_1);
				
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
		
    	String usuarioComum = mapaDeAcessos.get("USUARIO_GEFIC");
    	String senhaUsuarioComum = mapaDeAcessos.get("SENHA_GEFIC");
    	
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
    	
    	String paginaInicial = "https://filacirurgica.campinas.sp.gov.br/";
    	
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
        	AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
        	
        	String retorno;
        	
        	driver.get(paginaInicial);
        	retorno = InteracaoComGEFICPaginaInicial.acessarGEFIC(driver, paginaWeb, paginaInicial, usuarioComum, senhaUsuarioComum);
        	if(retorno.equals(""))
        	{
        		String pastaBase = "C:\\Users\\" + nomeUsuario;
            	String pastaDownloads = "C:\\Users\\" + nomeUsuario + "\\Downloads";
	        	
	        	String opcoesSimNao[] = {"Sim", "Não"};
	        	int obterStatusAtualDaFila = JOptionPane.showOptionDialog(null, "Favor informar se será atualizada a planilha GERAL do arquivo consolidado.", "Tipo de Execução", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoesSimNao, null);
	        	
	        	boolean executarStatus;
	        	if(obterStatusAtualDaFila == 0)
	        		executarStatus = true;
	        	else
	        		executarStatus = false;
	        		
				String competenciaInicial = JOptionPane.showInputDialog(null, "Insira os dados do mês/ano do início do processamento (MM/yyyy)", "Competência Inicial", JOptionPane.QUESTION_MESSAGE).trim();
				
		        ConsolidadoGEFIC consolidadoGEFIC = new ConsolidadoGEFIC();
		        
		        consolidadoGEFIC.gerarArquivoConsolidadoGEFIC(driver, ambiente, pastaBase, pastaDownloads, false, competenciaInicial, executarStatus);
		        //consolidadoGEFIC.gerarArquivoConsolidadoGEFIC(driver, ambiente, pastaBase, pastaDownloads, true, competenciaInicial, executarStatus);
        	}
        }
        else if(escolha == 1)
        {
        	AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
        	
        	String retorno;
        	
        	driver.get(paginaInicial);
        	retorno = InteracaoComGEFICPaginaInicial.acessarGEFIC(driver, paginaWeb, paginaInicial, usuarioComum, senhaUsuarioComum);
        	if(retorno.equals(""))
        	{
        		String pastaBase = "C:\\Users\\" + nomeUsuario;
            	String pastaDownloads = "C:\\Users\\" + nomeUsuario + "\\Downloads";
            	
            	LocalDate dataExecucao = LocalDate.now();
	        	
	        	RelatoriosGEFIC relatorioGEFIC = new RelatoriosGEFIC(pastaBase, pastaDownloads, ambiente);
	        	
	        	relatorioGEFIC.baixarFilaNominalCompleta(driver, true, false, dataExecucao);
	        	relatorioGEFIC.baixarFilaNominalCompleta(driver, true, true, dataExecucao);
	        	
	        	relatorioGEFIC.baixarQuantidadeDePacientesPorServico(driver, false, dataExecucao);
	        	relatorioGEFIC.baixarQuantidadeDePacientesPorServico(driver, true, dataExecucao);
	        	
	        	relatorioGEFIC.baixarTempoMedioDeEspera(driver, false, dataExecucao);
	        	relatorioGEFIC.baixarTempoMedioDeEspera(driver, true, dataExecucao);
	        	
	        	FilasNominaisGEFIC filaNominalGEFIC = new FilasNominaisGEFIC("C:\\Users\\" + nomeUsuario, ambiente, false);
	        	
	        	String dataExecucaoString = dataExecucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        	String competenciaString = dataExecucao.format(DateTimeFormatter.ofPattern("MM/yyyy"));
	    		
	    		filaNominalGEFIC.gerarFilasNominaisPorStatus(competenciaString, dataExecucaoString, IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_PROCEDIMENTO_REALIZADO.getTextoIdentificador(), false);
	    		filaNominalGEFIC.gerarFilasNominaisPorStatus(competenciaString, dataExecucaoString, IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_ENTREGA_REALIZADA.getTextoIdentificador(), true);
        	}
        }
       
        
        driver.close();
    }
    
    public static String acessarGEFIC(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String paginaInicial, String login, String senha)
    {
    	IdLogin = IdentificadoresPaginaWebGEFIC.ID_LOGIN_USUARIO.getTextoIdentificador();
    	IdSenha = IdentificadoresPaginaWebGEFIC.ID_LOGIN_SENHA.getTextoIdentificador();
    	IdBotaoEntrar = IdentificadoresPaginaWebGEFIC.ID_LOGIN_BOTAO_ENTRAR.getTextoIdentificador();
    	
		String retorno = InteracaoComGEFICPaginaInicial.autenticarNoSistema(driver, paginaWeb, login, senha);
		
		while(!retorno.equals(""))
		{
			driver.get(paginaInicial);
			retorno = InteracaoComGEFICPaginaInicial.autenticarNoSistema(driver, paginaWeb, login, senha);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
    }
    
    public static String autenticarNoSistema(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb, String login, String senha)
    {
    	paginaWeb.preencherInputText(driver, IdLogin, login);
    	paginaWeb.preencherInputText(driver, IdSenha, senha);
    	
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	paginaWeb.clicarBotaoSubmit(driver, IdBotaoEntrar, "id");

    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_ESCOLHER_LOTACAO.getTextoIdentificador()))
    	{
    		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_ESCOLHER_LOTACAO.getTextoIdentificador());
    		return "";
    	}
    	
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
