package modelosDados;

import dadosGerais.ParametrosArquivoAbsenteismoConsolidado;

public class EntidadeExecutanteR1 {
	
	private String cnes;
	private String vinculo;
	private String executante;
	private String nomeUnidadeSIRESP;
	private String nomeOfertasParaDERAC;
	private String caminhoCompletoArquivoBaixadoXLS;
	private String caminhoCompletoArquivoBaixadoXLSX;
	private String arquivoBaixadoXLS;
	private String arquivoBaixadoXLSX;
	
	public EntidadeExecutanteR1(String cnes, String vinculo, String executante, String nomeUnidadeSIRESP, String nomeOfertasParaDERAC) {
		this.cnes = cnes;
		this.vinculo = vinculo;
		this.executante = executante;
		this.nomeUnidadeSIRESP = nomeUnidadeSIRESP;
		this.nomeOfertasParaDERAC = nomeOfertasParaDERAC;
	}
	
	public String getCNES() {
		return cnes;
	}
	
	public void setCNES(String cnes) {
		this.cnes = cnes;
	}

//	public String getUnidade() {
//		return vinculo;
//	}
//
//	public void setUnidade(String unidade) {
//		this.vinculo = unidade;
//	}

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String distrito) {
		this.executante = distrito;
	}

	public String getNomeUnidadeSIRESP() {
		return nomeUnidadeSIRESP;
	}

	public void setNomeUnidadeSIRESP(String nomeUnidadeSIRESP) {
		this.nomeUnidadeSIRESP = nomeUnidadeSIRESP;
	}

	public String getVinculo() {
		return vinculo;
	}

	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
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

	public String getNomeOfertasParaDERAC() {
		return nomeOfertasParaDERAC;
	}

	public void setNomeOfertasParaDERAC(String nomeOfertasParaDERAC) {
		this.nomeOfertasParaDERAC = nomeOfertasParaDERAC;
	}
	
	

}
