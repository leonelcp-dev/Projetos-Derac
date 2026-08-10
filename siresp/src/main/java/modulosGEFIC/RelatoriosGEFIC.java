package modulosGEFIC;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import dadosGerais.IdentificadoresPaginaWebGEFIC;
import interacao_externa.AcoesGeraisPaginaWeb;

public class RelatoriosGEFIC
{

	public String baixarFilaNominalCompleta(WebDriver driver, boolean separarPorStatus, boolean ehOPM)
	{
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		ArrayList<String> opcoes = new ArrayList<String>();
		
		if(ehOPM)
		{
			opcoes.add("Filas OPM");
		}
		else
		{
			opcoes.add("Filas");
		}
		
		ArrayList<String> listaStatus = new ArrayList<String>();
		if(separarPorStatus)
		{
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_AGUARDANDO_NA_FILA.getTextoIdentificador());
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_PROCEDIMENTO_REALIZADO.getTextoIdentificador());
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_REGISTRO_CANCELADO.getTextoIdentificador());
		}
		else
		{
			listaStatus.add(IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_TODOS.getTextoIdentificador());
		}
			
		paginaWeb.clicarMenuULPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_MENU_PRINCIPAL.getTextoIdentificador(), opcoes);
		
		while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO.getTextoIdentificador()));
		
		selecionarTodosCampos(driver, paginaWeb);
		
		for(String status : listaStatus)
		{
			paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_FILTRO_STATUS.getTextoIdentificador());
			
			paginaWeb.selecionarItemSelectULLIPeloTextoVisivelDeUmaLinhaPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_FILTRO_STATUS_UL.getTextoIdentificador(), status);
			
			while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO_FILAS.getTextoIdentificador()));
			
			paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebGEFIC.ID_FILAS_BOTAO_EXCEL.getTextoIdentificador(), "id");
			
			while(paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresPaginaWebGEFIC.XPATH_AGUARDANDO_FILAS.getTextoIdentificador()));
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		
		return "";
	}
	
	private String selecionarTodosCampos(WebDriver driver, AcoesGeraisPaginaWeb paginaWeb)
	{
		paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresPaginaWebGEFIC.XPATH_FILAS_BOTAO_MAIS_COLUNAS.getTextoIdentificador());
		
		for(int indice = 1; indice <= IdentificadoresPaginaWebGEFIC.XPATH_FILAS_CHECK_BOX_ACOES.getIndice(); indice++)
		{
			String xPathCheckBox = IdentificadoresPaginaWebGEFIC.XPATH_FILAS_CHECK_BOX_DINAMICO.getTextoIdentificador().replaceAll(IdentificadoresPaginaWebGEFIC.MASCARA_VALOR_DINAMICO.getTextoIdentificador(), String.valueOf(indice));
			
			if(!paginaWeb.elementoEstaSelecionadoPeloXPATH(driver, xPathCheckBox))
				paginaWeb.clicarLinkPeloXPath(driver, xPathCheckBox);
			
		}
		
		return "";
	}
	
}
