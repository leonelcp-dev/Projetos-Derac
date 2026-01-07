package interacao_pagina_web;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class AcoesGeraisPaginaWeb {

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
	
}
