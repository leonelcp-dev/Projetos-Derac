package interacao_externa;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class AcoesGeraisPaginaWeb {

	public boolean trocarFrame(WebDriver driverPagina, String iframe)
	{
		driverPagina.switchTo().frame(iframe);
		
		return true;
	}
	
	public boolean clicarMenuUL(WebDriver driverPagina, String iframe, String idInicial, List<String> SequenciaMenus)
	{
		try
		{
		
			driverPagina.switchTo().frame(iframe);
			
			WebElement menu = driverPagina.findElement(By.id(idInicial));
	
			for(String itemMenu : SequenciaMenus)
			{
				List<WebElement> opcoes = menu.findElements(By.tagName("li"));
		
				for (WebElement opcao : opcoes) { 
					if (opcao.getText().trim().equals(itemMenu))
						opcao.click();
				}
			}
			
			return true;
		}catch(Error e) {
			System.out.println(e.toString());
			return false;
		}
		
	}
	
	
	public boolean selecionarItemSelect(WebDriver driverPagina, String id, String textoASelecionar)
	{

		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			Select select = new Select(item);
			select.selectByVisibleText(textoASelecionar);
			
		}catch(Error e) {
			System.out.println(e.toString());
			return false;
		}
				
		return true;
	}
	
	public String obterValorSelecionadoDoSelect(WebDriver driverPagina, String id)
	{
		String valor;

		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			Select select = new Select(item);
			valor = select.getFirstSelectedOption().getText();
			
		}catch(Error e) {
			System.out.println(e.toString());
			return "";
		}
				
		return valor;
	}
	
	public boolean limparInputText(WebDriver driverPagina, String id)
	{

		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			item.clear();
			
		}catch(Error e) {
			System.out.println(e.toString());
			return false;
		}
				
		return true;
	}
	
	public boolean preencherInputText(WebDriver driverPagina, String id, String texto)
	{

		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			item.sendKeys(texto);
			
		}catch(Error e) {
			System.out.println(e.toString());
			return false;
		}
				
		return true;
	}
	
	public String obterTextoInputText(WebDriver driverPagina, String id)
	{
		String valor;
		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			valor = item.getAttribute("value");
			
		}catch(Error e) {
			System.out.println(e.toString());
			return "";
		}
				
		return valor;
	}
	public boolean clicarBotaoSubmit(WebDriver driverPagina, String id, String idOrName)
	{
		try
		{	
			WebElement botao = null;
			if(idOrName.equals("name"))
				botao = driverPagina.findElement(By.name(id));
			else if(idOrName.equals("id"))
				botao = driverPagina.findElement(By.id(id));
	
			botao.click();
			
		}catch(Error e) {
			System.out.println(e.toString());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<ArrayList<String>> obterTable(WebDriver driverPagina, String id)
	{
		ArrayList<ArrayList<String>> tabela = new ArrayList();
		
		try
		{		
			WebElement table = driverPagina.findElement(By.id(id));
	

			List<WebElement> linhas = table.findElements(By.tagName("tr"));

            // Percorrer as linhas e imprimir o conteúdo das células
            for (WebElement tr : linhas) {
            	
            	ArrayList<String> linha = new ArrayList();
            	
                List<WebElement> colunas = tr.findElements(By.tagName("td"));
                for (WebElement coluna : colunas) {
                    
                	linha.add(coluna.getText());
                }
                                
                tabela.add(linha);
            }

			
		}catch(Exception e) {
			System.out.println(e.toString());
			System.out.println("Chegou aqui");
			return null;
		}
		
		return tabela;
	}
	
	public List<WebElement> obterSubmits(WebDriver driverPagina, String className)
	{
		List<WebElement> submits;
		
		try
		{		
			submits = driverPagina.findElements(By.className(className));

		}catch(Error e) {
			System.out.println(e.toString());
			return null;
		}
		
		return submits;
	}
	
}
