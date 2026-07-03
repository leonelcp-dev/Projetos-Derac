package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDIDRLeitos {

	REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR(1, "Demanda Reprimida CDIDR"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_LEITOS_URGENCIA(9, "Urgencia"),
	TESTE_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Consolidado Leitos.xlsx"),
	TESTE_ARQUIVO_ENTIDADES(9,"Urgencia\\entidadesMonitoramentoLeitos.csv"),
	TESTE_PASTA_ARQUIVOS_CENSO(9, "Leitos"),
	TESTE_ARQUIVO_DE_PARA_ESPECIALIDADES(9,"Urgencia\\deParaEspecialidadeLeitos.csv"),

	PROD_PASTA_LEITOS_URGENCIA(9, "Urgencia"),
	PROD_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador() + "\\Consolidado Leitos.xlsx"),
	PROD_ARQUIVO_ENTIDADES(9, "Urgencia\\entidadesMonitoramentoLeitos.csv"),
	PROD_PASTA_ARQUIVOS_CENSO(9, "Leitos"),
	PROD_ARQUIVO_DE_PARA_ESPECIALIDADES(9,"Urgencia\\deParaEspecialidadeLeitos.csv"),

	TESTE(TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_ARQUIVO_CONSOLIDADO_URGENCIA.getTextoIdentificador(), 
			TESTE_ARQUIVO_ENTIDADES.getTextoIdentificador(), TESTE_PASTA_ARQUIVOS_CENSO.getTextoIdentificador(), 
			TESTE_ARQUIVO_DE_PARA_ESPECIALIDADES.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), TESTE_PASTA_LEITOS_URGENCIA.getTextoIdentificador(), 
			PROD_ARQUIVO_ENTIDADES.getTextoIdentificador(), PROD_PASTA_ARQUIVOS_CENSO.getTextoIdentificador(), 
			PROD_ARQUIVO_DE_PARA_ESPECIALIDADES.getTextoIdentificador());
	
	private String pastaLeitosUrgencia;
	private String arquivoConsolidadoUrgencia;
	private String arquivoEntidades;
	private String pastaArquivosCenso;
	private String arquivoDeParaEspecialidades;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDIDRLeitos(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDIDRLeitos(String pastaLeitosUrgencia, String arquivoConsolidadoUrgencia, String arquivoEntidades, String pastaArquivosCenso, String arquivoDeParaEspecialidades)
	{
		this.pastaLeitosUrgencia = pastaLeitosUrgencia;
		this.arquivoConsolidadoUrgencia = arquivoConsolidadoUrgencia;
		this.arquivoEntidades = arquivoEntidades;
		this.pastaArquivosCenso = pastaArquivosCenso;
		this.arquivoDeParaEspecialidades = arquivoDeParaEspecialidades;
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

	public String getArquivoDeParaEspecialidades() {
		return arquivoDeParaEspecialidades;
	}

	public void setArquivoDeParaEspecialidades(String arquivoDeParaEspecialidades) {
		this.arquivoDeParaEspecialidades = arquivoDeParaEspecialidades;
	}
}
