package modelosDados;


public class MonitoramentoLeitos {

    @ExcelColumn(header = "Unidade")
    private String unidade;

    @ExcelColumn(header = "Data", pattern = "dd/MM/yyyy")
    private String dataExtracao;
    
    private String dataExtracaoOrdenacao;

    @ExcelColumn(header = "Especialidade")
    private String especialidade;
    
    @ExcelColumn(header = "Enfermaria")
    private String enfermaria;    

    @ExcelColumn(header = "Total Disponível")
    private String totalDisponivel;

    @ExcelColumn(header = "Uso Interno")
    private String reservaInterna;
    
    @ExcelColumn(header = "Uso Não Conveniado")
    private String usoNaoConveniado;
    
    @ExcelColumn(header = "Total Ocupado")
    private String totalOcupado;

    @ExcelColumn(header = "Regular")
    private String regularOcupado;

    @ExcelColumn(header = "Extra Pactuado")
    private String extraPactuadoOcupado;
    
    @ExcelColumn(header = "Extra Não Pactuado")
    private String extraNaoPactuadoOcupado;

    @ExcelColumn(header = "Interno")
    private String internoOcupado;
    
    @ExcelColumn(header = "Não Conveniado")
    private String naoConveniadoOcupado;
    
    @ExcelColumn(header = "Total Bloqueado")
    private String totalBloqueado;

    @ExcelColumn(header = "Isolamento")
    private String bloqueadoIsolamento;

    @ExcelColumn(header = "Aguardando Paciente")
    private String bloqueadoAguardandoPaciente;
    
    @ExcelColumn(header = "Outros")
    private String bloqueadoOutros;   
    
    @ExcelColumn(header = "Vagos")
    private String leitosVagos;

    @ExcelColumn(header = "Taxa de Ocupação")
    private String taxaDeOcupacao;
    
    private int linhaExcel;

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getDataExtracao() {
		return dataExtracao;
	}

	public void setDataExtracao(String dataExtracao) {
		this.dataExtracao = dataExtracao;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getTotalDisponivel() {
		return totalDisponivel;
	}

	public void setTotalDisponivel(String totalDisponivel) {
		this.totalDisponivel = totalDisponivel;
	}

	public String getTotalOcupado() {
		return totalOcupado;
	}

	public void setTotalOcupado(String totalOcupado) {
		this.totalOcupado = totalOcupado;
	}

	public String getRegularOcupado() {
		return regularOcupado;
	}

	public void setRegularOcupado(String regularOcupado) {
		this.regularOcupado = regularOcupado;
	}

	public String getExtraPactuadoOcupado() {
		return extraPactuadoOcupado;
	}

	public void setExtraPactuadoOcupado(String extraPactuadoOcupado) {
		this.extraPactuadoOcupado = extraPactuadoOcupado;
	}

	public String getTotalBloqueado() {
		return totalBloqueado;
	}

	public void setTotalBloqueado(String totalBloqueado) {
		this.totalBloqueado = totalBloqueado;
	}

	public String getBloqueadoIsolamento() {
		return bloqueadoIsolamento;
	}

	public void setBloqueadoIsolamento(String bloqueadoIsolamento) {
		this.bloqueadoIsolamento = bloqueadoIsolamento;
	}

	public String getBloqueadoAguardandoPaciente() {
		return bloqueadoAguardandoPaciente;
	}

	public void setBloqueadoAguardandoPaciente(String bloqueadoAguardandoPaciente) {
		this.bloqueadoAguardandoPaciente = bloqueadoAguardandoPaciente;
	}

	public String getBloqueadoOutros() {
		return bloqueadoOutros;
	}

	public void setBloqueadoOutros(String bloqueadoOutros) {
		this.bloqueadoOutros = bloqueadoOutros;
	}

	public String getLeitosVagos() {
		return leitosVagos;
	}

	public void setLeitosVagos(String leitosVagos) {
		this.leitosVagos = leitosVagos;
	}

	public String getTaxaDeOcupacao() {
		return taxaDeOcupacao;
	}

	public void setTaxaDeOcupacao(String taxaDeOcupacao) {
		this.taxaDeOcupacao = taxaDeOcupacao;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}

	public String getReservaInterna() {
		return reservaInterna;
	}

	public void setReservaInterna(String reservaInterna) {
		this.reservaInterna = reservaInterna;
	}

	public String getInternoOcupado() {
		return internoOcupado;
	}

	public void setInternoOcupado(String internoOcupado) {
		this.internoOcupado = internoOcupado;
	}

	public String getEnfermaria() {
		return enfermaria;
	}

	public void setEnfermaria(String enfermaria) {
		this.enfermaria = enfermaria;
	}

	public String getExtraNaoPactuadoOcupado() {
		return extraNaoPactuadoOcupado;
	}

	public void setExtraNaoPactuadoOcupado(String extraNaoPactuadoOcupado) {
		this.extraNaoPactuadoOcupado = extraNaoPactuadoOcupado;
	}

	public String getDataExtracaoOrdenacao() {
		return dataExtracaoOrdenacao;
	}

	public void setDataExtracaoOrdenacao(String dataExtracaoOrdenacao) {
		this.dataExtracaoOrdenacao = dataExtracaoOrdenacao;
	}

	public String getUsoNaoConveniado() {
		return usoNaoConveniado;
	}

	public void setUsoNaoConveniado(String usoNaoConveniado) {
		this.usoNaoConveniado = usoNaoConveniado;
	}
	
	public String getNaoConveniadoOcupado() {
		return naoConveniadoOcupado;
	}

	public void setNaoConveniadoOcupado(String naoConveniadoOcupado) {
		this.naoConveniadoOcupado = naoConveniadoOcupado;
	}

}
