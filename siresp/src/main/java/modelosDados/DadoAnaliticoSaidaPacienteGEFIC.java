package modelosDados;

public class DadoAnaliticoSaidaPacienteGEFIC {
	
	private String siglaEstabelecimento;
	
	@ExcelColumn(header = "Unidade")
	private String estabelecimento;
	
	@ExcelColumn(header = "Competência", pattern = "mmm/yyyy")
	private String competencia;
	
	@ExcelColumn(header = "Paciente")
	private String paciente;
	
	@ExcelColumn(header = "Data de Saída", pattern = "dd/mm/yyyy")
	private String dataSaida;
	
	@ExcelColumn(header = "Motivo da Saída")
	private String motivoSaida;
	
	@ExcelColumn(header = "Data de Nascimento", pattern = "dd/mm/yyyy")
	private String dataNascimento;
	
	@ExcelColumn(header = "Idade")
	private String idade;
	private String especialidade;
	private String subespecialidade;
	
	@ExcelColumn(header = "Procedimento")
	private String procedimento;
	
	@ExcelColumn(header = "Observação")
	private String Observacao;
	
	private String competenciaOrdenacao;
	private String dataSaidaOrdenacao;
	
	public String getPaciente() {
		return paciente;
	}
	
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getIdade() {
		return idade;
	}

	public void setIdade(String idade) {
		this.idade = idade;
	}

	public String getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(String dataSaida) {
		this.dataSaida = dataSaida;
	}

	public String getMotivoSaida() {
		return motivoSaida;
	}

	public void setMotivoSaida(String motivoSaida) {
		this.motivoSaida = motivoSaida;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getSubespecialidade() {
		return subespecialidade;
	}

	public void setSubespecialidade(String subespecialidade) {
		this.subespecialidade = subespecialidade;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public String getSiglaEstabelecimento() {
		return siglaEstabelecimento;
	}

	public void setSiglaEstabelecimento(String siglaEstabelecimento) {
		this.siglaEstabelecimento = siglaEstabelecimento;
	}

	public String getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public String getObservacao() {
		return Observacao;
	}

	public void setObservacao(String observacao) {
		Observacao = observacao;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public String getCompetenciaOrdenacao() {
		return competenciaOrdenacao;
	}

	public void setCompetenciaOrdenacao(String competenciaOrdenacao) {
		this.competenciaOrdenacao = competenciaOrdenacao;
	}

	public String getDataSaidaOrdenacao() {
		return dataSaidaOrdenacao;
	}

	public void setDataSaidaOrdenacao(String dataSaidaOrdenacao) {
		this.dataSaidaOrdenacao = dataSaidaOrdenacao;
	}


}
