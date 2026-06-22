package modelosDados;

public class UrgenciaAguardandoDetalhado 
{
	@ExcelColumn(header = "Data", pattern = "dd/MM/yyyy")
	private String data;
	
	private String dataOrdenacao;
	
	@ExcelColumn(header = "Horário Extração", pattern = "HH:mm:ss")
	private String horarioExtracao;
	
	@ExcelColumn(header = "Solicitante")
	private String solicitante;
	
	@ExcelColumn(header = "Recurso")
	private String recurso;
	
	@ExcelColumn(header = "Ficha")
	private String ficha;
	
	@ExcelColumn(header = "Horas de Espera")
	private String horasDeEspera;
	
	@ExcelColumn(header = "Quantidade")
	private String quantidade;
	
	private int horasDeEsperaOrdenacao;
	
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

	public String getHorarioExtracao() {
		return horarioExtracao;
	}

	public void setHorarioExtracao(String horarioExtracao) {
		this.horarioExtracao = horarioExtracao;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}

	public String getHorasDeEspera() {
		return horasDeEspera;
	}

	public void setHorasDeEspera(String horasDeEspera) {
		this.horasDeEspera = horasDeEspera;
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

	public int getHorasDeEsperaOrdenacao() {
		return horasDeEsperaOrdenacao;
	}

	public void setHorasDeEsperaOrdenacao(int horasDeEsperaOrdenacao) {
		this.horasDeEsperaOrdenacao = horasDeEsperaOrdenacao;
	}
}
