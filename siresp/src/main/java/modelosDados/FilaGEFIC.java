package modelosDados;

public class FilaGEFIC 
{
	@ExcelColumn(header = "Posição")
	private String posicao;
	
	@ExcelColumn(header = "Paciente")
	private String paciente;
	
	@ExcelColumn(header = "CPF")
	private String CPF;
	
	@ExcelColumn(header = "Telefone")
	private String telefone;
	
	@ExcelColumn(header = "Data nascimento")
	private String dataNascimento;
	
	@ExcelColumn(header = "Idade")
	private String idade;
	
	@ExcelColumn(header = "Priorização")
	private String priorizacao;
	
	@ExcelColumn(header = "Especialidade")
	private String especialidade;
	
	@ExcelColumn(header = "Subespecialidade")
	private String subespecialidade;
	
	@ExcelColumn(header = "Procedimento")
	private String procedimento;
	
	@ExcelColumn(header = "CID")
	private String CID;
	
	@ExcelColumn(header = "Data indicação", pattern="dd/MM/yyyy")
	private String dataIndicacao;
	
	@ExcelColumn(header = "Data de inserção", pattern="dd/MM/yyyy")
	private String dataInsercao;
	
	@ExcelColumn(header = "Data de execução", pattern="dd/MM/yyyy")
	private String dataExecucao;
	
	@ExcelColumn(header = "Data de saída", pattern="dd/MM/yyyy")
	private String dataSaida;
	
	@ExcelColumn(header = "Estabelecimento")
	private String estabelecimento;
	
	@ExcelColumn(header = "Situação")
	private String situacao;
	
	@ExcelColumn(header = "Cidade")
	private String cidade;
	
	@ExcelColumn(header = "Telefone Adicional 1")
	private String telefoneAdicional1;
	
	@ExcelColumn(header = "Telefone Adicional 2")
	private String telefoneAdicional2;
	
	@ExcelColumn(header = "Unidade Básica de Referência")
	private String unidadeBasicaReferencia;
	
	@ExcelColumn(header = "Unidade Solicitante")
	private String unidadeSolicitante;
	
	@ExcelColumn(header = "Tempo de Espera")
	private String tempoEspera;
	
	@ExcelColumn(header = "Tempo médio de Espera")
	private String tempoMedioEspera;
	
	@ExcelColumn(header = "Tempo máximo de Espera")
	private String tempoMaximoEspera;
	
	@ExcelColumn(header = "Observações")
	private String observacoes;
	
	public String getPosicao() {
	return posicao;
	}

	public void setPosicao(String posicao) {
		this.posicao = posicao;
	}
	
	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	
	public String getCPF() {
		return CPF;
	}

	public void setCPF(String CPF) {
		this.CPF = CPF;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getDatanascimento() {
		return dataNascimento;
	}

	public void setDatanascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getIdade() {
		return idade;
	}

	public void setIdade(String idade) {
		this.idade = idade;
	}
	
	public String getPriorizacao() {
		return priorizacao;
	}

	public void setPriorizacao(String priorizacao) {
		this.priorizacao = priorizacao;
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
	
	public String getCID() {
		return CID;
	}

	public void setCID(String CID) {
		this.CID = CID;
	}
	
	public String getDataindicacao() {
		return dataIndicacao;
	}

	public void setDataindicacao(String dataIndicacao) {
		this.dataIndicacao = dataIndicacao;
	}
	
	public String getDatadeinsercao() {
		return dataInsercao;
	}

	public void setDatadeinsercao(String dataInsercao) {
		this.dataInsercao = dataInsercao;
	}
	
	public String getDatadeexecucao() {
		return dataExecucao;
	}

	public void setDatadeexecucao(String dataExecucao) {
		this.dataExecucao = dataExecucao;
	}
	
	public String getDatadesaida() {
		return dataSaida;
	}

	public void setDatadesaida(String dataSaida) {
		this.dataSaida = dataSaida;
	}
	
	public String getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}
	
	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	public String getTelefoneAdicional1() {
		return telefoneAdicional1;
	}

	public void setTelefoneAdicional1(String telefoneAdicional1) {
		this.telefoneAdicional1 = telefoneAdicional1;
	}
	
	public String getTelefoneAdicional2() {
		return telefoneAdicional2;
	}

	public void setTelefoneAdicional2(String telefoneAdicional2) {
		this.telefoneAdicional2 = telefoneAdicional2;
	}
	
	public String getUnidadeBasicadeReferencia() {
		return unidadeBasicaReferencia;
	}

	public void setUnidadeBasicadeReferencia(String unidadeBasicaReferencia) {
		this.unidadeBasicaReferencia = unidadeBasicaReferencia;
	}
	
	public String getUnidadeSolicitante() {
		return unidadeSolicitante;
	}

	public void setUnidadeSolicitante(String unidadeSolicitante) {
		this.unidadeSolicitante = unidadeSolicitante;
	}
	
	public String getTempodeEspera() {
		return tempoEspera;
	}

	public void setTempodeEspera(String tempoEspera) {
		this.tempoEspera = tempoEspera;
	}
	
	public String getTempomediodeEspera() {
		return tempoMedioEspera;
	}

	public void setTempomediodeEspera(String tempoMedioEspera) {
		this.tempoMedioEspera = tempoMedioEspera;
	}
	
	public String getTempomaximodeEspera() {
		return tempoMaximoEspera;
	}

	public void setTempomaximodeEspera(String tempoMaximoEspera) {
		this.tempoMaximoEspera = tempoMaximoEspera;
	}
	
	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
}
