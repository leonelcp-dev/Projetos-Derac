package modelosDados;

public class EntidadeAbsenteismo {
	
	private String cnes;
	private String unidade;
	private String distrito;
	private String nomeUnidadeSIRESP;
	private String nomeArquivoAbsenteismo;
	private String caminhoCompletoArquivoBaixadoXLS;
	private String caminhoCompletoArquivoBaixadoXLSX;
	private String arquivoBaixadoXLS;
	private String arquivoBaixadoXLSX;
	
	public EntidadeAbsenteismo(String cnes, String unidade, String distrito, String nomeUnidadeSIRESP, String nomeArquivoAbsenteismo) {
		this.cnes = cnes;
		this.unidade = unidade;
		this.distrito = distrito;
		this.nomeUnidadeSIRESP = nomeUnidadeSIRESP;
		this.nomeArquivoAbsenteismo = nomeArquivoAbsenteismo;
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

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getNomeUnidadeSIRESP() {
		return nomeUnidadeSIRESP;
	}

	public void setNomeUnidadeSIRESP(String nomeUnidadeSIRESP) {
		this.nomeUnidadeSIRESP = nomeUnidadeSIRESP;
	}

	public String getNomeArquivoAbsenteismo() {
		return nomeArquivoAbsenteismo;
	}

	public void setNomeArquivoAbsenteismo(String nomeArquivoAbsenteismo) {
		this.nomeArquivoAbsenteismo = nomeArquivoAbsenteismo;
	}

	public String getCaminhoCompletoArquivoBaixadoXLS() {
		return caminhoCompletoArquivoBaixadoXLS;
	}

	public void setCaminhoCompletoArquivoBaixadoXLS(String caminhoCompletoArquivoBaixadoXLS) {
		this.caminhoCompletoArquivoBaixadoXLS = caminhoCompletoArquivoBaixadoXLS;
	}

	public String getArquivoBaixadoXLS() {
		return arquivoBaixadoXLS;
	}

	public void setArquivoBaixadoXLS(String arquivoBaixadoXLS) {
		this.arquivoBaixadoXLS = arquivoBaixadoXLS;
	}

	public String getCaminhoCompletoArquivoBaixadoXLSX() {
		return caminhoCompletoArquivoBaixadoXLSX;
	}

	public void setCaminhoCompletoArquivoBaixadoXLSX(String caminhoCompletoArquivoBaixadoXLSX) {
		this.caminhoCompletoArquivoBaixadoXLSX = caminhoCompletoArquivoBaixadoXLSX;
	}

	public String getArquivoBaixadoXLSX() {
		return arquivoBaixadoXLSX;
	}

	public void setArquivoBaixadoXLSX(String arquivoBaixadoXLSX) {
		this.arquivoBaixadoXLSX = arquivoBaixadoXLSX;
	}
	
	

}
