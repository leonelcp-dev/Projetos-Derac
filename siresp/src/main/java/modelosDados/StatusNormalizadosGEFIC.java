package modelosDados;

public class StatusNormalizadosGEFIC 
{
	private String dataOrdenacao;
	
	@ExcelColumn(header = "Status")
	private String status;
	
	@ExcelColumn(header = "Módulo")
	private String modulo;
	
	@ExcelColumn(header = "Normalizado")
	private String normalizado;
	
	@ExcelColumn(header = "Realizado")
	private String realizado;
	
	private int horasDeEsperaOrdenacao;
	
	private int linhaExcel;
	
	private boolean linhaUtilizada;

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

	public int getHorasDeEsperaOrdenacao() {
		return horasDeEsperaOrdenacao;
	}

	public void setHorasDeEsperaOrdenacao(int horasDeEsperaOrdenacao) {
		this.horasDeEsperaOrdenacao = horasDeEsperaOrdenacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getNormalizado() {
		return normalizado;
	}

	public void setNormalizado(String normalizado) {
		this.normalizado = normalizado;
	}

	public String getRealizado() {
		return realizado;
	}

	public void setRealizado(String realizado) {
		this.realizado = realizado;
	}
}
