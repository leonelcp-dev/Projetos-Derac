package modelosDados;


public class AgendamentosPendentesRegulada {

	

	@ExcelColumn(header = "Solicitado em:")
    private String solicitadoEm;

    @ExcelColumn(header = "Município")
    private String municipio;

    @ExcelColumn(header = "Ficha")
    private String ficha;
    
    @ExcelColumn(header = "Código Paciente")
    private String codigoPaciente;
    
    @ExcelColumn(header = "Paciente")
    private String paciente;
    
    @ExcelColumn(header = "Unidade Solicitante")
    private String unidadeSolicitante;
    
    @ExcelColumn(header = "Nome Ficha")
    private String nomeFicha;
    
    @ExcelColumn(header = "Especialidade/Exame")
    private String especialidadeExame;
    
    @ExcelColumn(header = "Assumido em:")
    private String assumidoEm;
    
    @ExcelColumn(header = "Hipótese")
    private String hipotese;

	public String getSolicitadoEm() {
		return solicitadoEm;
	}

	public void setSolicitadoEm(String solicitadoEm) {
		this.solicitadoEm = solicitadoEm;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
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

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getUnidadeSolicitante() {
		return unidadeSolicitante;
	}

	public void setUnidadeSolicitante(String unidadeSolicitante) {
		this.unidadeSolicitante = unidadeSolicitante;
	}

	public String getNomeFicha() {
		return nomeFicha;
	}

	public void setNomeFicha(String nomeFicha) {
		this.nomeFicha = nomeFicha;
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

	public String getAssumidoEm() {
		return assumidoEm;
	}

	public void setAssumidoEm(String assumidoEm) {
		this.assumidoEm = assumidoEm;
	}

}
