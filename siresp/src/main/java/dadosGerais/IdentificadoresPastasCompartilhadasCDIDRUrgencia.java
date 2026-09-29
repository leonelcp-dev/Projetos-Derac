package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDIDRUrgencia {

	REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR(1, "Urgencia"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_LEITOS_URGENCIA(9, "Urgencia"),
	TESTE_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Consolidado Urgencia.xlsx"),
	TESTE_ARQUIVO_ENTIDADES(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\entidadesMonitoramentoLeitos.csv"),
	TESTE_PASTA_ARQUIVOS_CENSO(9, "Leitos"),
	TESTE_PASTA_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador()),
	TESTE_PASTA_CONSOLIDADO_URGENCIA_CDIDR(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Copia"),
	TESTE_PASTA_CONSOLIDADO_LEITOS_CDIDR(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Copia"),
	TESTE_NOME_ARQUIVO_CONSOLIDADO(9, "Consolidado Urgencia.xlsx"),

	PROD_PASTA_LEITOS_URGENCIA(9, "03. URGENCIA"),
	PROD_ARQUIVO_CONSOLIDADO_URGENCIA(9, PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\BANCO DE DADOS\\Base para automatização\\Consolidado Urgencia.xlsx"),
	PROD_ARQUIVO_ENTIDADES(9, PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\BANCO DE DADOS\\Base para automatização\\entidadesMonitoramentoLeitos.csv"),
	PROD_PASTA_ARQUIVOS_CENSO(9, "Leitos"),
	PROD_PASTA_CONSOLIDADO_URGENCIA(9, PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\BANCO DE DADOS\\Base para automatização"),
	PROD_PASTA_CONSOLIDADO_URGENCIA_CDIDR(9, PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\BANCO DE DADOS"),
	PROD_PASTA_CONSOLIDADO_LEITOS_CDIDR(9, "\\02. LEITOS\\BANCO DE DADOS"),
	PROD_NOME_ARQUIVO_CONSOLIDADO(9, "Consolidado Urgencia.xlsx"),

	TESTE(TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_ARQUIVO_CONSOLIDADO_URGENCIA.getTextoIdentificador(), 
			TESTE_ARQUIVO_ENTIDADES.getTextoIdentificador(), TESTE_PASTA_ARQUIVOS_CENSO.getTextoIdentificador(), 
			TESTE_PASTA_CONSOLIDADO_URGENCIA.getTextoIdentificador(), TESTE_PASTA_CONSOLIDADO_URGENCIA_CDIDR.getTextoIdentificador(), 
			TESTE_NOME_ARQUIVO_CONSOLIDADO.getTextoIdentificador(), TESTE_PASTA_CONSOLIDADO_LEITOS_CDIDR.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), PROD_ARQUIVO_CONSOLIDADO_URGENCIA.getTextoIdentificador(), 
			PROD_ARQUIVO_ENTIDADES.getTextoIdentificador(), PROD_PASTA_ARQUIVOS_CENSO.getTextoIdentificador(), 
			PROD_PASTA_CONSOLIDADO_URGENCIA.getTextoIdentificador(), PROD_PASTA_CONSOLIDADO_URGENCIA_CDIDR.getTextoIdentificador(), 
			PROD_NOME_ARQUIVO_CONSOLIDADO.getTextoIdentificador(), PROD_PASTA_CONSOLIDADO_LEITOS_CDIDR.getTextoIdentificador());
	
	private String pastaLeitosUrgencia;
	private String arquivoConsolidadoUrgencia;
	private String arquivoEntidades;
	private String pastaArquivosCenso;
	private String pastaConsolidadoUrgencia;
	private String pastaConsolidadoUrgenciaCDIDR;
	private String pastaConsolidadoLeitosCDIDR;
	private String nomeArquivoConsolidado;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDIDRUrgencia(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDIDRUrgencia(String pastaLeitosUrgencia, String arquivoConsolidadoUrgencia, String arquivoEntidades, String pastaArquivosCenso, String pastaConsolidadoUrgencia, String pastaConsolidadoUrgenciaCDIDR, String nomeArquivoConsolidado, String pastaConsolidadoLeitosCDIDR)
	{
		this.pastaLeitosUrgencia = pastaLeitosUrgencia;
		this.arquivoConsolidadoUrgencia = arquivoConsolidadoUrgencia;
		this.arquivoEntidades = arquivoEntidades;
		this.pastaArquivosCenso = pastaArquivosCenso;
		this.pastaConsolidadoUrgencia = pastaConsolidadoUrgencia;
		this.pastaConsolidadoUrgenciaCDIDR = pastaConsolidadoUrgenciaCDIDR;
		this.nomeArquivoConsolidado = nomeArquivoConsolidado;
		this.pastaConsolidadoLeitosCDIDR = pastaConsolidadoLeitosCDIDR;
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

	public String getPastaConsolidadoUrgencia() {
		return pastaConsolidadoUrgencia;
	}

	public void setPastaConsolidadoUrgencia(String pastaConsolidadoUrgencia) {
		this.pastaConsolidadoUrgencia = pastaConsolidadoUrgencia;
	}

	public String getPastaConsolidadoUrgenciaCDIDR() {
		return pastaConsolidadoUrgenciaCDIDR;
	}

	public void setPastaConsolidadoUrgenciaCDIDR(String pastaConsolidadoUrgenciaCDIDR) {
		this.pastaConsolidadoUrgenciaCDIDR = pastaConsolidadoUrgenciaCDIDR;
	}

	public String getNomeArquivoConsolidado() {
		return nomeArquivoConsolidado;
	}

	public void setNomeArquivoConsolidado(String nomeArquivoConsolidado) {
		this.nomeArquivoConsolidado = nomeArquivoConsolidado;
	}

	public String getPastaConsolidadoLeitosCDIDR() {
		return pastaConsolidadoLeitosCDIDR;
	}

	public void setPastaConsolidadoLeitosCDIDR(String pastaConsolidadoLeitosCDIDR) {
		this.pastaConsolidadoLeitosCDIDR = pastaConsolidadoLeitosCDIDR;
	}
}
