package extracao_dados.siresp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.network.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import interacao_externa.AcoesGeraisPaginaWeb;
import modulos.CensoLeitos;

/**
 * Para acessar o selenium em uma sessão já existente, o Google Chrome deve ser aberto em modo de depuração
 * "C:\Program Files\Google\Chrome\Application\chrome.exe" --remote-debugging-port=9222 --user-data-dir="C:\Users\PMC514991-2\AppData\Local\Google\Chrome\User Data"
 *
 */
public class BaixarCensoSIRESP 
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
    	
		 System.setProperty("webdriver.chrome.driver", "C:\\Users\\PMC514991-2\\Documents\\chromedriver-win64\\chromedriver.exe"); 
		 ChromeOptions options = new ChromeOptions(); 
		 options.addArguments("user-data-dir=C:\\chrome-temp"); 
		 options.addArguments("profile-directory=Default"); // ou "Profile 1" 
		 options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		     		
    	WebDriver driver = new ChromeDriver(options);
        	
        driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
        
       
        System.out.println("Page Title: " + driver.getTitle());
        
        CensoLeitos censo = new CensoLeitos();
        censo.executarCenso(driver);
        
        driver.quit();
    }
}
