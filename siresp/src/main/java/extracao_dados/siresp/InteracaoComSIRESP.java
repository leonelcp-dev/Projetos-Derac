package extracao_dados.siresp;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.network.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import interacao_externa.AcoesGeraisPaginaWeb;
import modulos.AbrirGoogleChrome;
import modulos.Absenteismo;
import modulos.CensoLeitos;
import modulos.FilaNominalAgendamentosPendentes;
import modulos.FilaNominalCDRNaoRegulada;
import modulos.FilaNominalSolicitacoesPendentes;

/**
 * Para acessar o selenium em uma sessão já existente, o Google Chrome deve ser aberto em modo de depuração
 * "C:\Program Files\Google\Chrome\Application\chrome.exe" --remote-debugging-port=9222 --user-data-dir="C:\chrome-temp"
 *
 */
public class InteracaoComSIRESP 
{
    public static void main( String[] args )
    {
    	
    	//ChromeOptions options = new ChromeOptions();
    	
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
				chrome.abrir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe"); 
		ChromeOptions options = new ChromeOptions(); 
		options.addArguments("user-data-dir=C:\\chrome-temp"); 
		options.addArguments("profile-directory=Default"); // ou "Profile 1" 
		options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");

    	WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
       
        System.out.println("Page Title: " + driver.getTitle());
        
        String[] opcoes = {
        		"Censo diário de Leitos"
        		, "Filas Nominais CDR (Não Regulada)"
        		, "Filas Nominais Regulada (Agendamentos)"
        		, "Filas Nominais Regulada (Solicitacoes)"
        		, "Absenteísmo"
        		}; 
        int escolha = JOptionPane.showOptionDialog( null, "Escolha uma opção:", "Caixa de Seleção", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0] );
        
        if(escolha == 0)
        {
	        CensoLeitos censo = new CensoLeitos();
	        censo.executarCenso(driver);
        }
        else if(escolha == 1)
        {
        	FilaNominalCDRNaoRegulada filaNominalCDR = new FilaNominalCDRNaoRegulada();
        	filaNominalCDR.baixarFilaCDR(driver);
        }
        else if(escolha == 2)
        {
        	FilaNominalAgendamentosPendentes filaNominalRegulada = new FilaNominalAgendamentosPendentes();
        	filaNominalRegulada.baixarFilaAgendamentosPendentes(driver);
        }
        else if(escolha == 3)
        {
        	FilaNominalSolicitacoesPendentes filaNominalRegulada = new FilaNominalSolicitacoesPendentes();
        	filaNominalRegulada.baixarFilaAgendamentosPendentes(driver);
        }
        else if(escolha == 4)
        {
        	Absenteismo absenteismo = new Absenteismo();
        	absenteismo.verificarAbsenteismo(driver);
        }
        
        driver.quit();
    }
}
