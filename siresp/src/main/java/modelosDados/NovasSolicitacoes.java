package modelosDados;


public class NovasSolicitacoes {

	@ExcelColumn(header = "Tipo Solicitação")
    private String tipoSolicitacao;

    @ExcelColumn(header = "Especialidade/Exame")
    private String especialidadeExame;
    
    @ExcelColumn(header = "CID")
    private String cid;
    
    @ExcelColumn(header = "Unidades Campinas")
    private String unidadesCampinas;
    
    @ExcelColumn(header = "Mês Inclusão")
    private String mesInclusao;
    
    @ExcelColumn(header = "Ano Inclusão")
    private String anoInclusao;
    
    @ExcelColumn(header = "Novas Solicitações")
    private String novasSolicitacoes;
    
    private int qtdeSolicitacoes;
    private int linhaExcel;

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

	public String getUnidadesCampinas() {
		return unidadesCampinas;
	}

	public void setUnidadesCampinas(String unidadesCampinas) {
		this.unidadesCampinas = unidadesCampinas;
	}

	public String getMesInclusao() {
		return mesInclusao;
	}

	public void setMesInclusao(String mesInclusao) {
		this.mesInclusao = mesInclusao;
	}
	
	public String getAnoInclusao() {
		return anoInclusao;
	}

	public void setAnoInclusao(String anoInclusao) {
		this.anoInclusao = anoInclusao;
	}
	
	public String getNovasSolicitacoes() {
		return novasSolicitacoes;
	}

	public void setNovasSolicitacoes(String novasSolicitacoes) {
		this.novasSolicitacoes = novasSolicitacoes;
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
