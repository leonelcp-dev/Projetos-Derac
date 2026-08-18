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
    	
        driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
        
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
        	UrgenciaAguardando urgenciaAguardando = new UrgenciaAguardando();
        	urgenciaAguardando.obterAgrupamentoDeEsperaUrgencia(driver, ambiente);
        }
        
        else if(escolha == 11)
        {
        	UrgenciaFinalizado urgenciaFinalizado = new UrgenciaFinalizado();
        	
        	String pastaBase = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta compartilhada", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
    		String pastaDownloads = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde os downloads são salvos", "Pasta de Download", JOptionPane.QUESTION_MESSAGE).trim();
        	//urgenciaFinalizado.obterAgrupamentoDeEsperaUrgencia(driver, ambiente, null, pastaBase, pastaDownloads);
        	
        	LocalDate dataInicial = LocalDate.of(2026, 7, 0);
        	LocalDate dataFinal = LocalDate.of(2026, 7, 30);
        	
        	for(LocalDate data = dataInicial; !data.isAfter(dataFinal); data = data.plusDays(1))
        	{
        		String dataString = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        		System.out.println(dataString);
        	    
        		urgenciaFinalizado.obterAgrupamentoDeEsperaUrgencia(driver, ambiente, dataString, pastaBase, pastaDownloads);
        	}
        }
        
        driver.quit();
    }
}
