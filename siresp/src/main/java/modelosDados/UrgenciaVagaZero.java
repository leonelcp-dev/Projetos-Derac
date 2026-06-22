package modelosDados;

public class UrgenciaVagaZero 
{
	@ExcelColumn(header = "Data", pattern = "dd/MM/yyyy")
	private String data;
	
	private String dataOrdenacao;
	
	@ExcelColumn(header = "Executante")
	private String executante;
	
	@ExcelColumn(header = "Recurso")
	private String recurso;
	
	@ExcelColumn(header = "Ficha")
	private String ficha;
	
	@ExcelColumn(header = "Total")
	private String total;
	
	@ExcelColumn(header = "Vaga Zero")
	private String vagaZero;
	
	@ExcelColumn(header = "Encaminhado para referência pactuada")
	private String encaminhadoParaReferenciaPactuada;
	
	@ExcelColumn(header = "Encaminhado para avaliação na referência de complexidade adequada")
	private String encaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada;
	
	@ExcelColumn(header = "Encaminhado automaticamente para referência pactuada")
	private String encaminhadoAutomaticamenteParaReferenciaPactuada;
	
	private int horasDeEsperaOrdenacao;
	
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

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getVagaZero() {
		return vagaZero;
	}

	public void setVagaZero(String vagaZero) {
		this.vagaZero = vagaZero;
	}

	public String getEncaminhadoParaReferenciaPactuada() {
		return encaminhadoParaReferenciaPactuada;
	}

	public void setEncaminhadoParaReferenciaPactuada(String encaminhadoParaReferenciaPactuada) {
		this.encaminhadoParaReferenciaPactuada = encaminhadoParaReferenciaPactuada;
	}

	public String getEncaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada() {
		return encaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada;
	}

	public void setEncaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada(String encaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada) {
		this.encaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada = encaminhadoParaAvaliacaoNaReferenciaDeComplexidadeAdequada;
	}

	public String getEncaminhadoAutomaticamenteParaReferenciaPactuada() {
		return encaminhadoAutomaticamenteParaReferenciaPactuada;
	}

	public void setEncaminhadoAutomaticamenteParaReferenciaPactuada(String encaminhadoAutomaticamenteParaReferenciaPactuada) {
		this.encaminhadoAutomaticamenteParaReferenciaPactuada = encaminhadoAutomaticamenteParaReferenciaPactuada;
	}
}
