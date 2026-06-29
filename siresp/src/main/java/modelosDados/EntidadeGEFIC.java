package modelosDados;

public class EntidadeGEFIC {
	
	private String siglaConsolidado;
	private String nomeGEFIC;
	private String tipoUnidade;
	
	public EntidadeGEFIC(String siglaConsolidado, String nomeGEFIC, String tipoUnidade)
	{
		this.siglaConsolidado = siglaConsolidado;
		this.nomeGEFIC = nomeGEFIC;
		this.tipoUnidade = tipoUnidade;
	}
	
	public String getNomeGEFIC() {
		return nomeGEFIC;
	}
	
	public void setNomeGEFIC(String nomeGEFIC) {
		this.nomeGEFIC = nomeGEFIC;
	}

	public String getSiglaSolicitante() {
		return siglaConsolidado;
	}

	public void setSiglaSolicitante(String siglaConsolidado) {
		this.siglaConsolidado = siglaConsolidado;
	}

	public String getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(String tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}
	
	

}
