package modelosDados;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;

public class LinhaCensoLeitos 
{
	private ArrayList<String> linha;
	private WebElement submit;
	
	public ArrayList<String> getLinha() {
		return linha;
	}
	
	public void setLinha(ArrayList<String> linha) {
		this.linha = linha;
	}
	
	public WebElement getSubmit() {
		return submit;
	}
	
	public void setSubmit(WebElement submit) {
		this.submit = submit;
	}
}
