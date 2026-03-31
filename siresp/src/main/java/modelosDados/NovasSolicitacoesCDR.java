package modelosDados;


public class NovasSolicitacoesCDR {

	@ExcelColumn(header = "Cod Paciente")
    private String codPaciente;

    @ExcelColumn(header = "Tipo Solicitação")
    private String tipoSolicitacao;

    @ExcelColumn(header = "Especialidade/Exame")
    private String especialidadeExame;
    
    @ExcelColumn(header = "CID")
    private String cid;
    
    @ExcelColumn(header = "Data Inclusão")
    private String dataInclusao;
    
    @ExcelColumn(header = "Unidades Campinas")
    private String unidadesCampinas;
    
    @ExcelColumn(header = "Ano Inclusão")
    private String anoInclusao;

	public String getCodPaciente() {
		return codPaciente;
	}

	public void setCodPaciente(String codPaciente) {
		this.codPaciente = codPaciente;
	}

	public String getTipoSolicitacao() {
		return tipoSolicitacao;
	}

	public void setTipoSolicitacao(String tipoSolicitacao) {
		this.tipoSolicitacao = tipoSolicitacao;
	}

	public String getEspecialidadeExame() {
		return especialidadeExame;
	}

	public void setEspecialidadeExame(String especialidadeExame) {
		this.especialidadeExame = especialidadeExame;
	}

	public String getCID() {
		return cid;
	}

	public void setCID(String cid) {
		this.cid = cid;
	}

	public String getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getUnidadesCampinas() {
		return unidadesCampinas;
	}

	public void setUnidadesCampinas(String unidadesCampinas) {
		this.unidadesCampinas = unidadesCampinas;
	}

	public String getAnoInclusao() {
		return anoInclusao;
	}

	public void setAnoInclusao(String anoInclusao) {
		this.anoInclusao = anoInclusao;
	}


}
