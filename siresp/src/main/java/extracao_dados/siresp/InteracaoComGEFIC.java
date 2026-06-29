package extracao_dados.siresp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import org.openqa.selenium.BuildInfo;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.network.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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

/**
 * Para acessar o selenium em uma sessão já existente, o Google Chrome deve ser aberto em modo de depuração
 * "C:\Program Files\Google\Chrome\Application\chrome.exe" --remote-debugging-port=9222 --user-data-dir="C:\chrome-temp"
 *
 */
public class InteracaoComGEFIC 
{
    public static void main( String[] args )
    {
    	
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
    	
    	String nomeUsuario = System.getProperty("user.name");
    	
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
        int escolhaChrome = JOptionPane.showOptionDialog( null, "Deseja abrir o Google Chrome no modo de processamento?", "Google Chrome", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoesChrome, opcoesChrome[0] );
        
        if(escolhaChrome == 0)
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
    	
        driver.get("https://filacirurgica.campinas.sp.gov.br/");
        
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
        	String pastaBase = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta compartilhada", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
        	String pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE).trim();
        	
			String competenciaInicial = JOptionPane.showInputDialog(null, "Insira os dados do mês/ano do início do processamento (MM/yyyy)", "Competência Inicial", JOptionPane.QUESTION_MESSAGE).trim();
			
	        ConsolidadoGEFIC consolidadoGEFIC = new ConsolidadoGEFIC();
	        
	        consolidadoGEFIC.gerarArquivoConsolidadoGEFIC(driver, ambiente, pastaBase, pastaDownloads, false, competenciaInicial);
	        consolidadoGEFIC.gerarArquivoConsolidadoGEFIC(driver, ambiente, pastaBase, pastaDownloads, true, competenciaInicial);
        }
       
        
        driver.quit();
    }
}
