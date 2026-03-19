package modelosDados;

import dadosGerais.ParametrosArquivoAbsenteismoConsolidado;

public class EntidadeAbsenteismo {
	
	private String cnes;
	private String unidade;
	private String distrito;
	private String nomeUnidadeSIRESP;
	private String nomePadraoAbsenteismo;
	private String nomeArquivoAbsenteismo;
	private String nomeParaGrafico;
	private String caminhoCompletoArquivoBaixadoXLS;
	private String caminhoCompletoArquivoBaixadoXLSX;
	private String arquivoBaixadoXLS;
	private String arquivoBaixadoXLSX;
	private int quantidadeConsultasAgendadas;
	private int quantidadeFaltasConsultas;
	private int quantidadeExamesAgendados;
	private int quantidadeFaltasExames;
	private double absenteismoEmConsultas;
	private double absenteismoEmExames;
	
	public EntidadeAbsenteismo(String cnes, String unidade, String distrito, String nomeUnidadeSIRESP, String nomePadraoAbsenteismo, String nomeParaGrafico, String anoCompetencia) {
		this.cnes = cnes;
		this.unidade = unidade;
		this.distrito = distrito;
		this.nomeUnidadeSIRESP = nomeUnidadeSIRESP;
		this.nomePadraoAbsenteismo  = nomePadraoAbsenteismo;
		this.nomeArquivoAbsenteismo = nomePadraoAbsenteismo;
		if(!anoCompetencia.equals(""))
		{
			nomeArquivoAbsenteismo += " - " + anoCompetencia;
		}
		nomeArquivoAbsenteismo += "." + ParametrosArquivoAbsenteismoConsolidado.EXTENSAO_ARQUIVO_ABSENTEISMO.getDescricao();
		
		this.nomeParaGrafico = nomeParaGrafico;
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

	public String getNomeParaGrafico() {
		return nomeParaGrafico;
	}

	public void setNomeParaGrafico(String nomeParaGrafico) {
		this.nomeParaGrafico = nomeParaGrafico;
	}

	public String getNomePadraoAbsenteismo() {
		return nomePadraoAbsenteismo;
	}

	public void setNomePadraoAbsenteismo(String nomePadraoAbsenteismo) {
		this.nomePadraoAbsenteismo = nomePadraoAbsenteismo;
	}

	public int getQuantidadeConsultasAgendadas() {
		return quantidadeConsultasAgendadas;
	}

	public void setQuantidadeConsultasAgendadas(int quantidadeConsultasAgendadas) {
		this.quantidadeConsultasAgendadas = quantidadeConsultasAgendadas;
	}

	public int getQuantidadeFaltasConsultas() {
		return quantidadeFaltasConsultas;
	}

	public void setQuantidadeFaltasConsultas(int quantidadeFaltasConsultas) {
		this.quantidadeFaltasConsultas = quantidadeFaltasConsultas;
	}

	public int getQuantidadeExamesAgendados() {
		return quantidadeExamesAgendados;
	}

	public void setQuantidadeExamesAgendados(int quantidadeExamesAgendados) {
		this.quantidadeExamesAgendados = quantidadeExamesAgendados;
	}

	public int getQuantidadeFaltasExames() {
		return quantidadeFaltasExames;
	}

	public void setQuantidadeFaltasExames(int quantidadeFaltasExames) {
		this.quantidadeFaltasExames = quantidadeFaltasExames;
	}

	public double getAbsenteismoEmConsultas() {
		if(quantidadeConsultasAgendadas <= 0)
			return -1;
		return 1.0 * quantidadeFaltasConsultas / quantidadeConsultasAgendadas;
	}

	public void setAbsenteismoEmConsultas(double absenteismoEmConsultas) {
		this.absenteismoEmConsultas = absenteismoEmConsultas;
	}

	public double getAbsenteismoEmExames() {
		if(quantidadeExamesAgendados <= 0)
			return -1;
		return 1.0 * quantidadeFaltasExames / quantidadeExamesAgendados;
	}

	public void setAbsenteismoEmExames(double absenteismoEmExames) {
		this.absenteismoEmExames = absenteismoEmExames;
	}
	
	

}
