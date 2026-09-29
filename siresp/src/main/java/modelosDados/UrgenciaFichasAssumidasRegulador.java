package modelosDados;

public class UrgenciaFichasAssumidasRegulador 
{
	@ExcelColumn(header = "Data", pattern = "dd/MM/yyyy")
	private String data;
	
	private String dataOrdenacao;

	@ExcelColumn(header = "Período")
	private String periodo;
	
	@ExcelColumn(header = "Regulador")
	private String regulador;
	
	@ExcelColumn(header = "Casos Assumidos")
	private String assumidos;
	
	@ExcelColumn(header = "Casos Encaminhados")
	private String encaminhados;
	
	@ExcelColumn(header = "Casos Regulados")
	private String regulados;
	
	@ExcelColumn(header = "Casos Pendentes")
	private String pendentes;
	
	private int linhaExcel;
	
	private boolean linhaUtilizada;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
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

	public String getRegulador() {
		return regulador;
	}

	public void setRegulador(String regulador) {
		this.regulador = regulador;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getAssumidos() {
		return assumidos;
	}

	public void setAssumidos(String assumidos) {
		this.assumidos = assumidos;
	}

	public String getEncaminhados() {
		return encaminhados;
	}

	public void setEncaminhados(String encaminhados) {
		this.encaminhados = encaminhados;
	}

	public String getRegulados() {
		return regulados;
	}

	public void setRegulados(String regulados) {
		this.regulados = regulados;
	}

	public String getPendentes() {
		return pendentes;
	}

	public void setPendentes(String pendentes) {
		this.pendentes = pendentes;
	}
}
