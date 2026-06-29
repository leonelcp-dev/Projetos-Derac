package modelosDados;

public class UrgenciaProducaoReguladorMensal 
{
	@ExcelColumn(header = "Competência", pattern = "mmm/yyyy")
	private String competencia;
	
	private String competenciaOrdenacao;
	
	@ExcelColumn(header = "Regulador")
	private String regulador;
	
	@ExcelColumn(header = "Quantidade")
	private String quantidade;
	
	private int linhaExcel;
	
	private boolean linhaUtilizada;
	
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

	public String getRegulador() {
		return regulador;
	}

	public void setRegulador(String regulador) {
		this.regulador = regulador;
	}

	public String getCompetenciaOrdenacao() {
		return competenciaOrdenacao;
	}

	public void setCompetenciaOrdenacao(String competenciaOrdenacao) {
		this.competenciaOrdenacao = competenciaOrdenacao;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}
}
