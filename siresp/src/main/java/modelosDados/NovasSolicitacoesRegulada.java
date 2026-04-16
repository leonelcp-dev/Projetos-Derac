package modelosDados;


public class NovasSolicitacoesRegulada {

	@ExcelColumn(header = "Solicitado em:")
    private String solicitadoEm;

    @ExcelColumn(header = "Ficha")
    private String ficha;
    
    @ExcelColumn(header = "Código Paciente")
    private String codigoPaciente;
    
    @ExcelColumn(header = "Unidade Solicitante")
    private String unidadeSolicitante;
    
    @ExcelColumn(header = "Tipo de Oferta")
    private String tipoDeOferta;
    
    @ExcelColumn(header = "Especialidade/Exame")
    private String especialidadeExame;
    
    @ExcelColumn(header = "Hipótese")
    private String hipotese;
    
    @ExcelColumn(header = "Arquivo")
    private String arquivo;
    
    private int qtdeSolicitacoes;
    private int linhaExcel;
    
	public String getSolicitadoEm() {
		return solicitadoEm;
	}
	
	public void setSolicitadoEm(String solicitadoEm) {
		this.solicitadoEm = solicitadoEm;
	}

	public String getFicha() {
		return ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getUnidadeSolicitante() {
		return unidadeSolicitante;
	}

	public void setUnidadeSolicitante(String unidadeSolicitante) {
		this.unidadeSolicitante = unidadeSolicitante;
	}

	public String getTipoDeOferta() {
		return tipoDeOferta;
	}

	public void setTipoDeOferta(String tipoDeOferta) {
		this.tipoDeOferta = tipoDeOferta;
	}

	public String getEspecialidadeExame() {
		return especialidadeExame;
	}

	public void setEspecialidadeExame(String especialidadeExame) {
		this.especialidadeExame = especialidadeExame;
	}

	public String getHipotese() {
		return hipotese;
	}

	public void setHipotese(String hipotese) {
		this.hipotese = hipotese;
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public int getQtdeSolicitacoes() {
		return qtdeSolicitacoes;
	}

	public void setQtdeSolicitacoes(int qtdeSolicitacoes) {
		this.qtdeSolicitacoes = qtdeSolicitacoes;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}
    



}
