package modelosDados;

public class EntidadeLeito {

	private String nomePasta;
	private String nomeSIRESP;
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

	public String getArquivoBaixadoXLS() {
		return arquivoBaixadoXLS;
	}

	public void setArquivoBaixadoXLS(String arquivoBaixado) {
		this.arquivoBaixadoXLS = arquivoBaixado;
	}

	public String getArquivoBaixadoXLSX() {
		return arquivoBaixadoXLSX;
	}

	public void setArquivoBaixadoXLSX(String arquivoBaixadoXLSX) {
		this.arquivoBaixadoXLSX = arquivoBaixadoXLSX;
	}
	
}
