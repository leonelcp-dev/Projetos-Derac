package modelosDados;

public class AnaliseNaoConformidades {

	private String executante;
	private String naoConformidades;
	private String analise;
	private int linhaExcel;
	
	public String getNaoConformidades() 
	{
		return naoConformidades;
	}
	
	public void setNaoConformidades(String naoConformidades) 
	{
		this.naoConformidades = naoConformidades;
	}

	public String getAnalise() {
		return analise;
	}

	public void setAnalise(String analise) {
		this.analise = analise;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}
	
	
	
}
