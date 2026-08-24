package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDTI {

	REFERENCIA_PASTAS_CDTI(2, "CDTI"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA(9, ""),
	TESTE_PASTA_RELATORIO_OFERTA_DEMANDA(9, "Oferta e Demanda\\ENTRADAS MENSAIS\\Relatorio Oferta Demanda"),

	PROD_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA(9, "historico demanda reprimida"),
	PROD_PASTA_RELATORIO_OFERTA_DEMANDA(10, "Relatorio Produção"),

	TESTE(TESTE_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA.getTextoIdentificador(), TESTE_PASTA_RELATORIO_OFERTA_DEMANDA.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA.getTextoIdentificador(), PROD_PASTA_RELATORIO_OFERTA_DEMANDA.getTextoIdentificador());
	
	private String arquivosDemandaReprimida;
	private String pastaRelatorioOfertaDemanda;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDTI(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDTI(String arquivosDemandaReprimida, String pastaRelatorioOfertaDemanda)
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
