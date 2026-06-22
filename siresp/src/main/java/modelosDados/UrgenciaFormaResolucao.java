package modelosDados;

public class UrgenciaFormaResolucao 
{
	@ExcelColumn(header = "Data", pattern = "dd/MM/yyyy")
	private String data;
	
	private String dataOrdenacao;
	
	@ExcelColumn(header = "Forma de Resolução")
	private String formaDeResolucao;
	
	@ExcelColumn(header = "Solicitante")
	private String solicitante;
	
	@ExcelColumn(header = "Local de Regulação")
	private String localDeRegulacao;
	
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
	
	public String getSolicitante() {
		return solicitante;
	}
	
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

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

	public String getFormaDeResolucao() {
		return formaDeResolucao;
	}

	public void setFormaDeResolucao(String formaDeResolucao) {
		this.formaDeResolucao = formaDeResolucao;
	}

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}

	public String getLocalDeRegulacao() {
		return localDeRegulacao;
	}

	public void setLocalDeRegulacao(String localDeRegulacao) {
		this.localDeRegulacao = localDeRegulacao;
	}
}
