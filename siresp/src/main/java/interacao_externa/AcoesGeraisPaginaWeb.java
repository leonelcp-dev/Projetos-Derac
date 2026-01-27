package interacao_externa;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import modelosDados.ElementoSelecao;

public class AcoesGeraisPaginaWeb {

	public boolean trocarFrame(WebDriver driverPagina, String iframe)
	{
		driverPagina.switchTo().frame(iframe);
		
		return true;
	}
	
	public boolean voltarAoTopoDaPagina(WebDriver driverPagina)
	{
		driverPagina.switchTo().defaultContent();
		
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
					{
						opcao.click();
						menu = opcao;
						break;
					}
				}
			}
			
			return true;
		}catch(Exception e) {
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
	
	public boolean selecionarItemSelectPeloValue(WebDriver driverPagina, String id, String valueASelecionar)
	{

		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			Select select = new Select(item);
			select.selectByValue(valueASelecionar);
			
		}catch(Exception e) {
			System.out.println(e.toString());
			return false;
		}
				
		return true;
	}
	
	public boolean removerSelecaoItemSelectPeloValue(WebDriver driverPagina, String id, String valueASelecionar)
	{

		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			Select select = new Select(item);
			select.deselectByValue(valueASelecionar);
			
		}catch(Exception e) {
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
		WebElement botao = null;
		try
		{	
			
			if(idOrName.equals("name"))
				botao = driverPagina.findElement(By.name(id));
			else if(idOrName.equals("id"))
				botao = driverPagina.findElement(By.id(id));
	
			botao.click();
			
		}catch(Exception e) {
			System.out.println(e.toString());
			
			aguardarElementosSobrepostos(driverPagina, id, "");
			
			return false;
		}
		
		return true;
	}
	
	public boolean clicarBotaoSubmitComValue(WebDriver driverPagina, String id, String idOrName, String value)
	{
		WebElement botao = null;
		try
		{	
			
			if(idOrName.equals("name"))
				botao = driverPagina.findElement(By.name(id));
			else if(idOrName.equals("id"))
				botao = driverPagina.findElement(By.id(id));
	
			botao.click();
			
		}catch(Exception e) {
			System.out.println(e.toString());
			
			aguardarElementosSobrepostos(driverPagina, id, value);
			
			return false;
		}
		
		return true;
	}
	
	public boolean clicarElementoPeloId(WebDriver driverPagina, String id)
	{
		try
		{	
			WebElement elemento = null;
			
			elemento = driverPagina.findElement(By.id(id));
	
			elemento.click();
			
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
	
	
	public ArrayList<ElementoSelecao> getListaDeOpcoesRadioPorName(WebDriver driverPagina, String name)
	{
		ArrayList<ElementoSelecao> radioButtons = new ArrayList<ElementoSelecao>();
		List<WebElement> elementos;
		
		try
		{		
			elementos = driverPagina.findElements(By.name(name));
			
			for(WebElement elemento : elementos)
			{
				String value = elemento.getAttribute("value");
				
				WebElement label = driverPagina.findElement(By.xpath("//label[input[@value='" + value + "']]"));
				String texto = label.getText();
				
				radioButtons.add(new ElementoSelecao(texto, value));
			}

		}catch(Error e) {
			System.out.println(e.toString());
			return null;
		}
		
		return radioButtons;
	}
	
	
	public boolean clicarRadioInputByValue(WebDriver driverPagina, String value)
	{
		try
		{	
			WebElement radio = driverPagina.findElement(By.xpath("//input[@type='radio' and @value='" + value + "']"));
			//WebElement radio = driverPagina.findElement(By.cssSelector("input[type='radio'][value$='" + value + "']") );
	
			radio.click();
			
		}catch(Error e) {
			System.out.println(e.toString());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<ElementoSelecao> obterItensDeUmSelect(WebDriver driverPagina, String id)
	{
		ArrayList<ElementoSelecao> opcoes = new ArrayList<ElementoSelecao>();
		
		try
		{		
			WebElement item = driverPagina.findElement(By.id(id));
	
			Select select = new Select(item);
			List<WebElement> options = select.getOptions();
			
			for(WebElement option : options)
				opcoes.add(new ElementoSelecao(option.getText(), option.getAttribute("value")));
			
		}catch(Error e) {
			System.out.println(e.toString());
			return null;
		}
				
		return opcoes;
	}
	
	public boolean divEstaVisivel(WebDriver driverPagina, String id)
	{
		boolean visivel;
		
		try
		{	
			WebElement div = driverPagina.findElement(By.id(id));
			
			visivel = div.isDisplayed();
			
		}catch(Error e) {
			System.out.println(e.toString());
			return false;
		}
		
		return visivel;
	}
	
	private void aguardarElementosSobrepostos(WebDriver driverPagina, String id, String value)
	{

		/* Opção 1 */
//		WebDriverWait wait = new WebDriverWait(driverPagina, Duration.ofSeconds(10));
//		
//		// Exemplo: backdrops típicos
//		By overlay = By.cssSelector(".modal-backdrop, .backdrop, .loading-mask, .cdk-overlay-backdrop");
//		
//		// Aguarde overlay sumir/invisível
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
//		
//		// Agora clique de forma normal
//		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
//		btn.click();
		

		
		/* Opção 2 */
		WebDriverWait wait = new WebDriverWait(driverPagina, Duration.ofSeconds(15));
		
		// 1) espere qualquer overlay sumir
		wait.until(ExpectedConditions.invisibilityOfElementLocated(
		    By.cssSelector(".modal-backdrop, .overlay, .cdk-overlay-backdrop, .loading")
		));
		
		// 2) desça até o botão
		
		List<WebElement> elements = driverPagina.findElements(By.id(id));
		
		for(WebElement btn : elements)
		{
			System.out.println("botão: " + btn.getAccessibleName() + "|" + btn.getAttribute("value"));
			if(btn.getAttribute("value").equals(value))
			{
				((JavascriptExecutor) driverPagina).executeScript(
				    "arguments[0].scrollIntoView({block: 'center'});", btn);
				
				// 3) clique via JS (ignora sobreposição física)
				((JavascriptExecutor) driverPagina).executeScript("arguments[0].click();", btn);
			}
		}



		/* Opção 3 */

//		By locator = By.cssSelector(".modal.show .acoes [id='" + id + "']");
//		
//		WebDriverWait wait = new WebDriverWait(driverPagina, Duration.ofSeconds(20));
//		
//		// 1) (Opcional, mas recomendado) — espere overlays comuns sumirem
//		wait.until(d ->
//		    d.findElements(By.cssSelector(
//		        ".modal-backdrop, .cdk-overlay-backdrop, .loading-mask, .overlay, .spinner, .toast"))
//		     .stream()
//		     .noneMatch(WebElement::isDisplayed)
//		);
//		
//		// 2) garanta presença e visibilidade no DOM
//		WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//		btn = wait.until(ExpectedConditions.visibilityOf(btn));
//		
//		// 3) traga para o centro do viewport (evita header fixo e overlay parcial)
//		((JavascriptExecutor) driverPagina).executeScript(
//		    "arguments[0].scrollIntoView({block:'center', inline:'center'});", btn
//		);
//		
//		// 4) tente clique “normal”; se falhar, use fallbacks
//		try {
//		    wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
//		} catch (Exception e1) {
//		    try {
//		        // Fallback 1: Actions (simula mouse real)
//		        new Actions(driverPagina).moveToElement(btn).click().perform();
//		    } catch (Exception e2) {
//		        // Fallback 2: JS click (ignora overlay/animacoes)
//		        ((JavascriptExecutor) driverPagina).executeScript("arguments[0].click();", btn);
//		    }
//		}

		
	}
	
}
