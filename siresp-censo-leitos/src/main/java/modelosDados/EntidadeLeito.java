package modelosDados;

public class EntidadeLeito {

	private String nomePasta;
	private String nomeSIRESP;
	private String arquivoBaixado;
	
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

	public String getArquivoBaixado() {
		return arquivoBaixado;
	}

	public void setArquivoBaixado(String arquivoBaixado) {
		this.arquivoBaixado = arquivoBaixado;
	}
	
}
