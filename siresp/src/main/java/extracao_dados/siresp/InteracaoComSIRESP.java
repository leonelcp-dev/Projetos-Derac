package extracao_dados.siresp;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.network.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import interacao_externa.AcoesGeraisPaginaWeb;
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
    	
    	int escolha = -1;
    	int maximoEscolha = 8;
    	boolean abrirMenuInicial = false;
    	
    	if(args.length == 0)
    	{
    		abrirMenuInicial = true;
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
        	//absenteismo.verificarAbsenteismo(driver, null, null);
        	absenteismo.verificarAbsenteismo(driver, "12/2024", "12/2024");
        	//absenteismo.parametrizarArquivosVazios(driver);
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
        	ofertaDemanda.calcularOfertaEDemanda(driver);
        }
        
        driver.quit();
    }
}
