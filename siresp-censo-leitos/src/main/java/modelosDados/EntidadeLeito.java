package modelosDados;

public class EntidadeLeito {

	private String nomePasta;
	private String nomeSIRESP;
	private String caminhoCompletoArquivoBaixadoXLS;
	private String caminhoCompletoArquivoBaixadoXLSX;
	private String arquivoBaixadoXLS;
	private String arquivoBaixadoXLSX;
	
	public EntidadeLeito(String nomePasta, String nomeSIRESP) {
		this.nomePasta = nomePasta;
		this.nomeSIRESP = nomeSIRESP;
	}
	
	public String getNomePasta() {
		return nomePasta;
	}
	public void setNomePasta(String nomePasta) {
		this.nomePasta = nomePasta;
	}
	public String getNomeSIRESP() {
		return nomeSIRESP;
	}
	public void setNomeSIRESP(String nomeSIRESP) {
		this.nomeSIRESP = nomeSIRESP;
	}

	public String getCaminhoCompletoArquivoBaixadoXLS() {
		return caminhoCompletoArquivoBaixadoXLS;
	}

	public void setCaminhoCompletoArquivoBaixadoXLS(String caminhoCompletoArquivoBaixadoXLS) {
		this.caminhoCompletoArquivoBaixadoXLS = caminhoCompletoArquivoBaixadoXLS;
	}

	public String getCaminhoCompletoArquivoBaixadoXLSX() {
		return caminhoCompletoArquivoBaixadoXLSX;
	}

	public void setCaminhoCompletoArquivoBaixadoXLSX(String caminhoCompletoArquivoBaixadoXLSX) {
		this.caminhoCompletoArquivoBaixadoXLSX = caminhoCompletoArquivoBaixadoXLSX;
	}

	public String getArquivoBaixadoXLS() {
		return arquivoBaixadoXLS;
	}

	public void setArquivoBaixadoXLS(String arquivoBaixadoXLS) {
		this.arquivoBaixadoXLS = arquivoBaixadoXLS;
	}

	public String getArquivoBaixadoXLSX() {
		return arquivoBaixadoXLSX;
	}

	public void setArquivoBaixadoXLSX(String arquivoBaixadoXLSX) {
		this.arquivoBaixadoXLSX = arquivoBaixadoXLSX;
	}
	
}
