package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDRA {

	REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDRA(2, "Demanda Reprimida CDRA"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA(9, "Oferta e Demanda\\ENTRADAS MENSAIS\\Demanda Reprimida CDRA"),
	TESTE_PASTA_RELATORIO_OFERTA_DEMANDA(9, "Oferta e Demanda\\ENTRADAS MENSAIS\\Relatorio Oferta Demanda"),

	PROD_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA(9, "historico demanda reprimida"),
	PROD_PASTA_RELATORIO_OFERTA_DEMANDA(10, "Relatorio oferta e demanda"),

	TESTE(TESTE_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA.getTextoIdentificador(), TESTE_PASTA_RELATORIO_OFERTA_DEMANDA.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA.getTextoIdentificador(), PROD_PASTA_RELATORIO_OFERTA_DEMANDA.getTextoIdentificador());
	
	private String arquivosDemandaReprimida;
	private String pastaRelatorioOfertaDemanda;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDRA(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDRA(String arquivosDemandaReprimida, String pastaRelatorioOfertaDemanda)
	{
		this.arquivosDemandaReprimida = arquivosDemandaReprimida;
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

	public String getArquivosDemandaReprimida() {
		return arquivosDemandaReprimida;
	}

	public void setArquivosDemandaReprimida(String arquivosDemandaReprimida) {
		this.arquivosDemandaReprimida = arquivosDemandaReprimida;
	}

	public String getPastaRelatorioOfertaDemanda() {
		return pastaRelatorioOfertaDemanda;
	}

	public void setPastaRelatorioOfertaDemanda(String pastaRelatorioOfertaDemanda) {
		this.pastaRelatorioOfertaDemanda = pastaRelatorioOfertaDemanda;
	}
}
