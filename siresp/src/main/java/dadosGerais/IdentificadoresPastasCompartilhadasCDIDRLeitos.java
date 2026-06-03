package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDIDRLeitos {

	REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDRA(2, "Demanda Reprimida CDRA"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_LEITOS_URGENCIA(9, "Urgencia"),
	TESTE_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Consolidado Urgencia.xlsx"),

	PROD_PASTA_LEITOS_URGENCIA(9, "Urgencia"),
	PROD_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Consolidado Urgencia.xlsx"),

	TESTE(TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_ARQUIVO_CONSOLIDADO_URGENCIA.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador());
	
	private String pastaLeitosUrgencia;
	private String arquivoConsolidadoUrgencia;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDIDRLeitos(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDIDRLeitos(String pastaLeitosUrgencia, String arquivoConsolidadoUrgencia)
	{
		this.pastaLeitosUrgencia = pastaLeitosUrgencia;
		this.arquivoConsolidadoUrgencia = arquivoConsolidadoUrgencia;
	}

	public String getTextoIdentificador() {
		return textoIdentificador;
	}

	public void setTextoIdentificador(String textoIdentificador) {
		this.textoIdentificador = textoIdentificador;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public String getPastaLeitosUrgencia() {
		return pastaLeitosUrgencia;
	}

	public void setPastaLeitosUrgencia(String pastaLeitosUrgencia) {
		this.pastaLeitosUrgencia = pastaLeitosUrgencia;
	}

	public String getArquivoConsolidadoUrgencia() {
		return arquivoConsolidadoUrgencia;
	}

	public void setArquivoConsolidadoUrgencia(String arquivoConsolidadoUrgencia) {
		this.arquivoConsolidadoUrgencia = arquivoConsolidadoUrgencia;
	}
}
