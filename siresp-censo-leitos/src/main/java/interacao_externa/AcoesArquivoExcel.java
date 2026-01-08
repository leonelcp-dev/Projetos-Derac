package interacao_externa;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

public class AcoesArquivoExcel {

	private String nomeDoAquivo;
	private Workbook arquivo;
	private Sheet planilhaAtiva;
	private int primeiraLinhaVazia;
	
	public AcoesArquivoExcel(String nomeDoArquivo)
	{
		this.setNomeDoAquivo(nomeDoArquivo);
		
		try
		{
			FileInputStream fis = new FileInputStream(nomeDoArquivo);
            arquivo = new XSSFWorkbook(fis); 
            abrirPlanilha(0);

        } catch (Exception e) {
        	arquivo = null;
            e.printStackTrace();
        }

	}
	
	public boolean isAberto()
	{
		if(arquivo == null)
			return false;
		else
			return true;
	}
	
	public void abrirPlanilha(int planilha)
	{
		planilhaAtiva = arquivo.getSheetAt(planilha);
		
		primeiraLinhaVazia = planilhaAtiva.getLastRowNum();
	}

	public String getNomeDoAquivo() {
		return nomeDoAquivo;
	}

	public void setNomeDoAquivo(String nomeDoAquivo) {
		this.nomeDoAquivo = nomeDoAquivo;
	}

	public int getPrimeiraLinhaVazia() {
		return primeiraLinhaVazia;
	}

	public void setPrimeiraLinhaVazia(int primeiraLinhaVazia) {
		this.primeiraLinhaVazia = primeiraLinhaVazia;
	}
	
	public String getValorDaCeula(int Linha, int Coluna)
	{
		Row linha = planilhaAtiva.getRow(Linha);
		Cell celula = linha.getCell(Coluna);
		
		return celula.getStringCellValue();
	}
}
