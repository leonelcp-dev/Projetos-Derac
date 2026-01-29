package interacao_externa;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
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
					
					System.out.println(opcao.getText() + " - " + itemMenu);
					
					if (opcao.getText().trim().equals(itemMenu))
					{
						opcao.click();
						menu = opcao;
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
	
	public boolean limparInputTextByName(WebDriver driverPagina, String name)
	{

		try
		{		
			WebElement item = driverPagina.findElement(By.name(name));
	
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
	
	public boolean preencherInputTextByName(WebDriver driverPagina, String name, String texto)
	{

		try
		{		
			WebElement item = driverPagina.findElement(By.name(name));
	
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
	
	public boolean elementoEstaVisivel(WebDriver driverPagina, String id)
	{
		boolean visivel;
		
		try
		{	
			WebElement elemento = driverPagina.findElement(By.id(id));
			
			visivel = elemento.isDisplayed();
			
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
	

    public enum OpenStrategy {
        HOVER, // abre submenu passando o mouse
        CLICK  // abre submenu clicando no item do nível
    }

/**
     * Clica em um caminho de menu UL/LI de N níveis dentro de um iframe.
     *
     * @param iframeIdOrName id/name do iframe (pode ser null se não houver iframe)
     * @param rootMenuId id do UL/LI/NAV raiz do menu
     * @param sequencia Lista sequencial dos rótulos (ex: ["Produtos","Eletrônicos","TVs"])
     * @param openStrategy como o submenu abre nos níveis intermediários (HOVER ou CLICK)
     * @return true se conseguiu clicar o último item
     */
    public boolean clicarMenuUL(WebDriver driverPagina, int timeoutSegundos, String iframeIdOrName, String rootMenuId, List<String> sequencia, OpenStrategy openStrategy) {
   

        WebDriverWait wait;
        Actions actions;

        wait = new WebDriverWait(driverPagina, Duration.ofSeconds(timeoutSegundos));
        actions = new Actions(driverPagina);

        if (sequencia == null || sequencia.isEmpty()) {
            throw new IllegalArgumentException("A sequência de menus não pode ser vazia.");
        }

        try {
            // 1) Entrar no iframe (se houver)
            switchToFrameIfNeeded(wait, iframeIdOrName);

            // 2) Localizar o container raiz do menu
            WebElement container = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.id(rootMenuId)));

            // 3) Percorrer níveis
            for (int i = 0; i < sequencia.size(); i++) {
                String label = sequencia.get(i);
                boolean isLast = (i == sequencia.size() - 1);

                // 3.1) Encontrar o item do nível atual por texto
                WebElement item = findMenuItem(wait, container, label);

                // 3.2) Garantir visibilidade/posição
                scrollIntoView(driverPagina, item);

                if (!isLast) {
                    // 3.3) Abrir submenu do nível (hover ou clique)
                    openSubmenu(driverPagina, wait, actions, item, openStrategy);

                    // 3.4) Atualizar container para o submenu visível
                    container = waitForVisibleSubmenu(wait, item);
                } else {
                    // 3.5) Clique final com resiliência
                    clickReliably(driverPagina, wait, item);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("[MenuNavigator] Falha: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        } 
    }

    /* ====================== Utilitários ====================== */

    private void switchToFrameIfNeeded(WebDriverWait wait, String iframeIdOrName) {
        if (iframeIdOrName == null || iframeIdOrName.isBlank()) return;
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeIdOrName));
    }

    /**
     * Tenta achar um item do menu (LI ou A) cujo texto visível corresponda ao label (case-insensitive).
     * Procura primeiro <a>, depois cai no <li> se necessário.
     */
    private WebElement findMenuItem(WebDriverWait wait, WebElement container, String label) {
        String normalizedLabel = normalize(label);

        // Procurar <a> com texto
        List<WebElement> links = container.findElements(By.cssSelector("li a, a"));
        Optional<WebElement> exactLink = links.stream()
                .filter(el -> normalize(el.getText()).equals(normalizedLabel))
                .findFirst();

        if (exactLink.isPresent()) {
            wait.until(ExpectedConditions.visibilityOf(exactLink.get()));
            return exactLink.get();
        }

        // Procurar <li> cujo texto total corresponda
        List<WebElement> lis = container.findElements(By.tagName("li"));
        Optional<WebElement> exactLi = lis.stream()
                .filter(el -> normalize(el.getText()).equals(normalizedLabel))
                .findFirst();

        if (exactLi.isPresent()) {
            wait.until(ExpectedConditions.visibilityOf(exactLi.get()));
            return exactLi.get();
        }

        throw new NoSuchElementException("Item de menu não encontrado: '" + label + "'");
    }

    private void openSubmenu(WebDriver driverPagina, WebDriverWait wait, Actions actions, WebElement item, OpenStrategy strategy) {
        try {
            if (strategy == OpenStrategy.HOVER) {
                actions.moveToElement(item).pause(Duration.ofMillis(200)).perform();
            } else {
                clickReliably(driverPagina, wait, item);
            }
        } catch (MoveTargetOutOfBoundsException e) {
            // Tenta centralizar e repetir
            scrollIntoView(driverPagina, item);
            if (strategy == OpenStrategy.HOVER) {
                actions.moveToElement(item).pause(Duration.ofMillis(200)).perform();
            } else {
                clickReliably(driverPagina, wait, item);
            }
        }
    }

    /**
     * Espera o <ul> do submenu ficar visível. Primeiro tenta descendente direto,
     * depois irmão imediato (padrão comum).
     */
    private WebElement waitForVisibleSubmenu(WebDriverWait wait, WebElement parentItem) {
        // 1) Descendente
        By childUl = By.xpath(".//ul[not(contains(@style,'display: none')) and not(contains(@hidden,'true'))]");
        try {
            List<WebElement> visibles = wait.until(
                    ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentItem, childUl));
            if (!visibles.isEmpty()) return visibles.get(0);
        } catch (TimeoutException ignored) { }

        // 2) Irmão imediato
        try {
            WebElement sibling = parentItem.findElement(By.xpath("following-sibling::ul[1]"));
            wait.until(ExpectedConditions.visibilityOf(sibling));
            return sibling;
        } catch (NoSuchElementException | TimeoutException ignored) { }

        // 3) Fallback: procurar um UL aberto próximo na hierarquia
        WebElement anyVisibleUl = closestVisibleUl(parentItem);
        if (anyVisibleUl != null) return anyVisibleUl;

        throw new TimeoutException("Submenu não ficou visível para o item: " + safeText(parentItem));
    }

    private WebElement closestVisibleUl(WebElement from) {
        WebElement scope = from;
        for (int i = 0; i < 3; i++) {
            try {
                WebElement ul = scope.findElement(By.xpath(".//ul[not(contains(@style,'display: none'))]"));
                if (ul.isDisplayed()) return ul;
            } catch (NoSuchElementException ignored) { }
            try {
                scope = scope.findElement(By.xpath(".."));
            } catch (NoSuchElementException e) {
                break;
            }
        }
        return null;
    }

    private void clickReliably(WebDriver driverPagina, WebDriverWait wait, WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el));
            el.click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            scrollIntoView(driverPagina, el);
            // Tenta novamente o clique nativo
            try {
                wait.until(ExpectedConditions.elementToBeClickable(el));
                el.click();
            } catch (Exception ex) {
                // Fallback: clique via JavaScript
                ((JavascriptExecutor) driverPagina).executeScript("arguments[0].click();", el);
            }
        } catch (StaleElementReferenceException e) {
            // DOM mudou; tenta reobter por texto dentro do container ancestral
            String label = normalize(el.getText());
            WebElement ancestor = el.findElement(By.xpath("ancestor-or-self::*[self::ul or self::nav or self::li][1]"));
            WebElement refreshed = findMenuItem(wait, ancestor, label);
            scrollIntoView(driverPagina, refreshed);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(refreshed));
                refreshed.click();
            } catch (Exception ex) {
                ((JavascriptExecutor) driverPagina).executeScript("arguments[0].click();", refreshed);
            }
        }
    }

    private void scrollIntoView(WebDriver driverPagina, WebElement el) {
        ((JavascriptExecutor) driverPagina).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
    }

    private String normalize(String s) {
        if (s == null) return "";
        // Normaliza espaços e case; pode adaptar para remover acentos se necessário
        return s.replace('\u00A0',' ') // NBSP
                .trim()
                .replaceAll("\\s+", " ");
               // .toLowerCase(Locale.ROOT);
    }

    private String safeText(WebElement el) {
        try { return el.getText(); } catch (Exception e) { return "<sem texto>"; }
    }

	
}
