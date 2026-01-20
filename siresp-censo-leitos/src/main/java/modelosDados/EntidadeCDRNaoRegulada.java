package modelosDados;

public class EntidadeCDRNaoRegulada {
	
	private String cnes;
	private String unidade;
	private String distrito;
	private String caminhoCompletoArquivoBaixadoCSV;
	private String arquivoBaixadoCSV;
	
	public EntidadeCDRNaoRegulada(String cnes, String unidade, String distrito) {
		this.cnes = cnes;
		this.unidade = unidade;
		this.distrito = distrito;
	}
	
	public String getCNES() {
		return cnes;
	}
	
	public void setCNES(String cnes) {
		this.cnes = cnes;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getCaminhoCompletoArquivoBaixadoCSV() {
		return caminhoCompletoArquivoBaixadoCSV;
	}

	public void setCaminhoCompletoArquivoBaixadoCSV(String caminhoCompletoArquivoBaixadoCSV) {
		this.caminhoCompletoArquivoBaixadoCSV = caminhoCompletoArquivoBaixadoCSV;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getArquivoBaixadoCSV() {
		return arquivoBaixadoCSV;
	}

	public void setArquivoBaixadoCSV(String arquivoBaixadoCSV) {
		this.arquivoBaixadoCSV = arquivoBaixadoCSV;
	}
	
	

}
