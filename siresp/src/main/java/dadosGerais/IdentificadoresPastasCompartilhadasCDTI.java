package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDTI {

	REFERENCIA_PASTAS_CDTI(2, "CDTI"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_ARQUIVOS_LEITOS_URGENCIA(9, "Urgencia\\CopiaCDTI"),
	TESTE_PASTA_RELATORIO_OFERTA_DEMANDA(9, "Oferta e Demanda\\ENTRADAS MENSAIS\\Relatorio Oferta Demanda"),

	PROD_PASTA_ARQUIVOS_LEITOS_URGENCIA(9, "Leitos e Urgencia"),
	PROD_PASTA_RELATORIO_OFERTA_DEMANDA(10, "Relatorio Produção"),

	TESTE(TESTE_PASTA_ARQUIVOS_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_PASTA_RELATORIO_OFERTA_DEMANDA.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_ARQUIVOS_LEITOS_URGENCIA.getTextoIdentificador(), PROD_PASTA_RELATORIO_OFERTA_DEMANDA.getTextoIdentificador());
	
	private String pastaLeitosUrgencia;
	private String pastaRelatorioOfertaDemanda;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDTI(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDTI(String pastaLeitosUrgencia, String pastaRelatorioOfertaDemanda)
	{
		this.pastaLeitosUrgencia = pastaLeitosUrgencia;
		this.pastaRelatorioOfertaDemanda = pastaRelatorioOfertaDemanda;
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

	public String getPastaRelatorioOfertaDemanda() {
		return pastaRelatorioOfertaDemanda;
	}

	public void setPastaRelatorioOfertaDemanda(String pastaRelatorioOfertaDemanda) {
		this.pastaRelatorioOfertaDemanda = pastaRelatorioOfertaDemanda;
	}
}
