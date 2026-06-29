package modelosDados;

public class UrgenciaProducaoRegulador 
{
	@ExcelColumn(header = "Data", pattern = "dd/MM/yyyy")
	private String data;
	
	private String dataOrdenacao;
	
	@ExcelColumn(header = "Regulador")
	private String regulador;
	
	@ExcelColumn(header = "Executante")
	private String executante;
	
	@ExcelColumn(header = "Recurso")
	private String recurso;
	
	@ExcelColumn(header = "Ficha")
	private String ficha;
	
	@ExcelColumn(header = "Quantidade")
	private String quantidade;
	
	private int linhaExcel;
	
	private boolean linhaUtilizada;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getFicha() {
		return ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	
	public boolean isLinhaUtilizada()
	{
		return linhaUtilizada;
	}
	
	public void setLinhaUtilizada(boolean linhaUtilizada)
	{
		this.linhaUtilizada = linhaUtilizada;
	}

	public String getDataOrdenacao() {
		return dataOrdenacao;
	}

	public void setDataOrdenacao(String dataOrdenacao) {
		this.dataOrdenacao = dataOrdenacao;
	}

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}

	public String getRegulador() {
		return regulador;
	}

	public void setRegulador(String regulador) {
		this.regulador = regulador;
	}
}
