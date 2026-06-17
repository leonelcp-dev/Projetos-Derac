package modelosDados;

public class UrgenciaAguardandoAgrupado 
{
	@ExcelColumn(header = "Data", pattern = "dd/MM/yyyy")
	private String data;
	
	@ExcelColumn(header = "Horário Extração", pattern = "HH:mm:ss")
	private String horarioExtracao;
	
	@ExcelColumn(header = "Solicitante")
	private String solicitante;
	
	@ExcelColumn(header = "Recurso")
	private String recurso;
	
	@ExcelColumn(header = "Ficha")
	private String ficha;
	
	@ExcelColumn(header = "Total Geral")
	private String totalGeral;
	
	@ExcelColumn(header = "0-6 horas")
	private String periodo_0_6_horas;
	
	@ExcelColumn(header = "6-12 horas")
	private String periodo_6_12_horas;
	
	@ExcelColumn(header = "12-24 horas")
	private String periodo_12_24_horas;
	
	@ExcelColumn(header = "24-48 horas")
	private String periodo_24_48_horas;
	
	@ExcelColumn(header = "2-3 dias")
	private String periodo_2_3_dias;
	
	@ExcelColumn(header = "3-5 dias")
	private String periodo_3_5_dias;
	
	@ExcelColumn(header = "5-7 dias")
	private String periodo_5_7_dias;
	
	@ExcelColumn(header = "7-10 dias")
	private String periodo_7_10_dias;
	
	@ExcelColumn(header = "10-13 dias")
	private String periodo_10_13_dias;
	
	@ExcelColumn(header = "13-15 dias")
	private String periodo_13_15_dias;
	
	@ExcelColumn(header = "15-17 dias")
	private String periodo_15_17_dias;
	
	@ExcelColumn(header = "17-20 dias")
	private String periodo_17_20_dias;
	
	@ExcelColumn(header = "20-25 dias")
	private String periodo_20_25_dias;
	
	@ExcelColumn(header = "25-30 dias")
	private String periodo_25_30_dias;
	
	@ExcelColumn(header = "30 dias acima")
	private String periodo_30_dias_acima;
	
	private int linhaExcel;
	
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

	public String getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(String totalGeral) {
		this.totalGeral = totalGeral;
	}

	public String getPeriodo_0_6_horas() {
		return periodo_0_6_horas;
	}

	public void setPeriodo_0_6_horas(String periodo_0_6_horas) {
		this.periodo_0_6_horas = periodo_0_6_horas;
	}

	public String getPeriodo_2_3_dias() {
		return periodo_2_3_dias;
	}

	public void setPeriodo_2_3_dias(String periodo_2_3_dias) {
		this.periodo_2_3_dias = periodo_2_3_dias;
	}

	public String getPeriodo_12_24_horas() {
		return periodo_12_24_horas;
	}

	public void setPeriodo_12_24_horas(String periodo_12_24_horas) {
		this.periodo_12_24_horas = periodo_12_24_horas;
	}

	public String getPeriodo_24_48_horas() {
		return periodo_24_48_horas;
	}

	public void setPeriodo_24_48_horas(String periodo_24_48_horas) {
		this.periodo_24_48_horas = periodo_24_48_horas;
	}

	public String getPeriodo_6_12_horas() {
		return periodo_6_12_horas;
	}

	public void setPeriodo_6_12_horas(String periodo_6_12_horas) {
		this.periodo_6_12_horas = periodo_6_12_horas;
	}

	public String getPeriodo_3_5_dias() {
		return periodo_3_5_dias;
	}

	public void setPeriodo_3_5_dias(String periodo_3_5_dias) {
		this.periodo_3_5_dias = periodo_3_5_dias;
	}

	public String getPeriodo_25_30_dias() {
		return periodo_25_30_dias;
	}

	public void setPeriodo_25_30_dias(String periodo_25_30_dias) {
		this.periodo_25_30_dias = periodo_25_30_dias;
	}

	public String getPeriodo_13_15_dias() {
		return periodo_13_15_dias;
	}

	public void setPeriodo_13_15_dias(String periodo_13_15_dias) {
		this.periodo_13_15_dias = periodo_13_15_dias;
	}

	public String getPeriodo_5_7_dias() {
		return periodo_5_7_dias;
	}

	public void setPeriodo_5_7_dias(String periodo_5_7_dias) {
		this.periodo_5_7_dias = periodo_5_7_dias;
	}

	public String getPeriodo_10_13_dias() {
		return periodo_10_13_dias;
	}

	public void setPeriodo_10_13_dias(String periodo_10_13_dias) {
		this.periodo_10_13_dias = periodo_10_13_dias;
	}

	public String getPeriodo_7_10_dias() {
		return periodo_7_10_dias;
	}

	public void setPeriodo_7_10_dias(String periodo_7_10_dias) {
		this.periodo_7_10_dias = periodo_7_10_dias;
	}

	public String getPeriodo_15_17_dias() {
		return periodo_15_17_dias;
	}

	public void setPeriodo_15_17_dias(String periodo_15_17_dias) {
		this.periodo_15_17_dias = periodo_15_17_dias;
	}

	public String getPeriodo_20_25_dias() {
		return periodo_20_25_dias;
	}

	public void setPeriodo_20_25_dias(String periodo_20_25_dias) {
		this.periodo_20_25_dias = periodo_20_25_dias;
	}

	public String getPeriodo_17_20_dias() {
		return periodo_17_20_dias;
	}

	public void setPeriodo_17_20_dias(String periodo_17_20_dias) {
		this.periodo_17_20_dias = periodo_17_20_dias;
	}

	public String getPeriodo_30_dias_acima() {
		return periodo_30_dias_acima;
	}

	public void setPeriodo_30_dias_acima(String periodo_30_dias_acima) {
		this.periodo_30_dias_acima = periodo_30_dias_acima;
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
	
}
