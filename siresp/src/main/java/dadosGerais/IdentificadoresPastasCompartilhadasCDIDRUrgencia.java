package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDIDRUrgencia {

	REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR(1, "Demanda Reprimida CDIDR"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_LEITOS_URGENCIA(9, "Urgencia"),
	TESTE_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Consolidado Urgencia.xlsx"),
	TESTE_ARQUIVO_ENTIDADES(9,"Urgencia\\entidadesMonitoramentoLeitos.csv"),
	TESTE_PASTA_ARQUIVOS_CENSO(9, "Leitos"),

	PROD_PASTA_LEITOS_URGENCIA(9, "Urgencia"),
	PROD_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Consolidado Urgencia.xlsx"),
	PROD_ARQUIVO_ENTIDADES(9, "Urgencia\\entidadesMonitoramentoLeitos.csv"),
	PROD_PASTA_ARQUIVOS_CENSO(9, "Leitos"),

	TESTE(TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_ARQUIVO_CONSOLIDADO_URGENCIA.getTextoIdentificador(), 
			TESTE_ARQUIVO_ENTIDADES.getTextoIdentificador(), TESTE_PASTA_ARQUIVOS_CENSO.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), 
			PROD_ARQUIVO_ENTIDADES.getTextoIdentificador(), PROD_PASTA_ARQUIVOS_CENSO.getTextoIdentificador());
	
	private String pastaLeitosUrgencia;
	private String arquivoConsolidadoUrgencia;
	private String arquivoEntidades;
	private String pastaArquivosCenso;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDIDRUrgencia(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDIDRUrgencia(String pastaLeitosUrgencia, String arquivoConsolidadoUrgencia, String arquivoEntidades, String pastaArquivosCenso)
	{
		this.pastaLeitosUrgencia = pastaLeitosUrgencia;
		this.arquivoConsolidadoUrgencia = arquivoConsolidadoUrgencia;
		this.arquivoEntidades = arquivoEntidades;
		this.pastaArquivosCenso = pastaArquivosCenso;
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

	public String getArquivoEntidades() {
		return arquivoEntidades;
	}

	public void setArquivoEntidades(String arquivoEntidades) {
		this.arquivoEntidades = arquivoEntidades;
	}

	public String getPastaArquivosCenso() {
		return pastaArquivosCenso;
	}

	public void setPastaArquivosCenso(String pastaArquivosCenso) {
		this.pastaArquivosCenso = pastaArquivosCenso;
	}
}
