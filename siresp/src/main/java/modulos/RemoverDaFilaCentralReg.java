package modulos;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoFilasNominais;
import dominiosSIRESP.EspecialidadesSIRESP;
import dominiosSIRESP.ExamesSIRESP;
import dominiosSIRESP.StatusAgendamentoSIRESP;
import dominiosSIRESP.UnidadeSIRESP;
import extracao_dados.siresp.CriarImportacaoFilaCentralReg;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import modelosDados.CelulaExcel;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeLeito;
import modelosDados.EntidadesFilaCentralReg;
import modelosDados.LinhaCensoLeitos;
import modelosDados.UsuarioFilaCentralReg;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class RemoverDaFilaCentralReg {
	
	private int mesCompetencia;
	private int anoCompetencia;
	private String pastaDestinoArquivos;
	private String pastaDownloads;
	private MesesFormatados meses;	
	private DateTimeFormatter formatoDataPasta;
	LocalDate dataProcessamento;
	String dataFormatadaPasta;

	public String remvoerRegistrosCentralReg(WebDriver driver)
	{			
		formatoDataPasta = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
		
		ArrayList<String> opcoes = new ArrayList<>();
		opcoes.add("Agendamento");
		opcoes.add("Cadastro Demanda por Recurso");
		
		
		driver.get("https://www.siresp.saude.sp.gov.br/principal.php");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());
		paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
		ArrayList<ElementoSelecao> listaUnidadeRadio = paginaWeb.getListaDeOpcoesRadioPorName(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_RADIO_UNIDADES.getTextoIdentificador());
		
		HashMap<String, String> elementosRadioUnidades = new HashMap<String, String>();
		
		for(ElementoSelecao elemento : listaUnidadeRadio)
		{
			String cnes = elemento.getText().substring(0, 7);
			String value = elemento.getValue();
			
			int posicaoPerfilDeAcesso = elemento.getText().indexOf(" - Administrador Unidade Reg");
			
			if(posicaoPerfilDeAcesso > 0)
			{
				String composicaoCNESNomeUnidade = elemento.getText().substring(0, posicaoPerfilDeAcesso);
				elementosRadioUnidades.put(composicaoCNESNomeUnidade, value);
				
				//System.out.println("CNES: " + cnes + "| Value: " + value + "| Composição: " + composicaoCNESNomeUnidade + "|");
			}
			else
			{
				posicaoPerfilDeAcesso = elemento.getText().indexOf(" - Agendador Reg");
				
				if(posicaoPerfilDeAcesso > 0)
				{
					String composicaoCNESNomeUnidade = elemento.getText().substring(0, posicaoPerfilDeAcesso);
					elementosRadioUnidades.put(composicaoCNESNomeUnidade, value);
				}
			}

		}
		
		EntidadeCDRNaoRegulada centralReg = new EntidadeCDRNaoRegulada("5733944", "CENTRAL REG DE CAMPINAS", "DIVERSOS", "CENTRAL REG DE CAMPINAS");
		
		String value = elementosRadioUnidades.get(centralReg.getCNES() + " - " + centralReg.getNomeUnidadeSIRESP());
		//System.out.println(value);
		
		if(value != null)
		{
			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador());		
		
			boolean visivel;
			do
			{

				paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
				
				boolean unidadeEncontrada = paginaWeb.clicarRadioInputByValue(driver, value);
				
				paginaWeb.clicarBotaoSubmit(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_BOTAO_OK_ESCOLHER_UNIDADE.getTextoIdentificador(), "id");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//buscando arquivos e baixando
				paginaWeb.voltarAoTopoDaPagina(driver);
			

				//visivel = paginaWeb.clicarMenuUL(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes);
			
				visivel = paginaWeb.clicarMenuUL(driver, 1, IdentificadoresPaginaWebSIRESP.ID_FRAME_MENU.getTextoIdentificador(), IdentificadoresPaginaWebSIRESP.ID_MENU.getTextoIdentificador(), opcoes, OpenStrategy.HOVER);
			}while(!visivel);
			
			paginaWeb.trocarFrame(driver, IdentificadoresPaginaWebSIRESP.ID_FRAME_COMPONENTES.getTextoIdentificador());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			paginaWeb.clicarElementoPeloId(driver, IdentificadoresPaginaWebSIRESP.ID_AMBULATORIAL_CDR_ABA_LISTAR.getTextoIdentificador());
			
		}
		else
			System.out.println("Unidade não encontrada: " + centralReg.getCNES() + " - " + centralReg.getUnidade() + "(" + centralReg.getDistrito() + ")");

		
		JOptionPane.showMessageDialog(null, "Processamento concluído com sucesso!");
		
		return "";	
	}
	
	private ArrayList<EntidadesFilaCentralReg> lerEntidades(String nomeArquivo)
	{
		ArrayList<EntidadesFilaCentralReg> entidades = new ArrayList();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
                String unidade = registro.get("Unidade");
                String distrito = registro.get("Distrito");
                String nomeSIRESP = registro.get("Nome SIRESP");
                
                entidades.add(new EntidadesFilaCentralReg(unidade, distrito, "", nomeSIRESP, ".xlsx"));
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
}
