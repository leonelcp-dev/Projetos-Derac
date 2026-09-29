package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDIDRLeitos {

	REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR(1, "Leitos"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_LEITOS(9, "Urgencia"),
	TESTE_PASTA_URGENCIA(9, "Urgencia"),
	TESTE_ARQUIVO_CONSOLIDADO_URGENCIA(9, TESTE_PASTA_LEITOS.getTextoIdentificador() + "\\Consolidado Leitos.xlsx"),
	TESTE_ARQUIVO_ENTIDADES(9, TESTE_PASTA_URGENCIA.getTextoIdentificador() + "\\entidadesMonitoramentoLeitos.csv"),
	TESTE_PASTA_ARQUIVOS_CENSO(9, "Leitos"),
	TESTE_ARQUIVO_DE_PARA_ESPECIALIDADES(9,"Urgencia\\deParaEspecialidadeLeitos.csv"),
	TESTE_PASTA_CONSOLIDADO_LEITOS(9, TESTE_PASTA_LEITOS.getTextoIdentificador()),
	TESTE_PASTA_CONSOLIDADO_LEITOS_CDIDR(9, TESTE_PASTA_LEITOS.getTextoIdentificador() + "\\Copia"),
	TESTE_NOME_ARQUIVO_CONSOLIDADO(9, "Consolidado Leitos.xlsx"),

	PROD_PASTA_LEITOS(9, "02. LEITOS"),
	PROD_PASTA_URGENCIA(9, "03. URGENCIA"),
	PROD_ARQUIVO_CONSOLIDADO_URGENCIA(9, PROD_PASTA_LEITOS.getTextoIdentificador() + "\\BANCO DE DADOS\\Base para automatização\\Consolidado Leitos.xlsx"),
	PROD_ARQUIVO_ENTIDADES(9, PROD_PASTA_URGENCIA.getTextoIdentificador() + "\\BANCO DE DADOS\\Base para automatização\\entidadesMonitoramentoLeitos.csv"),
	PROD_PASTA_ARQUIVOS_CENSO(9, "02. LEITOS\\CENSOS SIRESP LEITOS"),
	PROD_ARQUIVO_DE_PARA_ESPECIALIDADES(9,PROD_PASTA_LEITOS.getTextoIdentificador() + "\\BANCO DE DADOS\\Base para automatização\\deParaEspecialidadeLeitos.csv"),
	PROD_PASTA_CONSOLIDADO_LEITOS(9, PROD_PASTA_LEITOS.getTextoIdentificador() + "\\BANCO DE DADOS\\Base para automatização"),
	PROD_PASTA_CONSOLIDADO_LEITOS_CDIDR(9, PROD_PASTA_LEITOS.getTextoIdentificador() + "\\BANCO DE DADOS"),
	PROD_NOME_ARQUIVO_CONSOLIDADO(9, "Consolidado Leitos.xlsx"),

	TESTE(TESTE_PASTA_LEITOS.getTextoIdentificador(), TESTE_PASTA_URGENCIA.getTextoIdentificador(), TESTE_ARQUIVO_CONSOLIDADO_URGENCIA.getTextoIdentificador(), 
			TESTE_ARQUIVO_ENTIDADES.getTextoIdentificador(), TESTE_PASTA_ARQUIVOS_CENSO.getTextoIdentificador(), 
			TESTE_ARQUIVO_DE_PARA_ESPECIALIDADES.getTextoIdentificador(), TESTE_PASTA_CONSOLIDADO_LEITOS.getTextoIdentificador(),
			TESTE_PASTA_CONSOLIDADO_LEITOS_CDIDR.getTextoIdentificador(), TESTE_NOME_ARQUIVO_CONSOLIDADO.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_LEITOS.getTextoIdentificador(), PROD_PASTA_URGENCIA.getTextoIdentificador(), PROD_ARQUIVO_CONSOLIDADO_URGENCIA.getTextoIdentificador(), 
			PROD_ARQUIVO_ENTIDADES.getTextoIdentificador(), PROD_PASTA_ARQUIVOS_CENSO.getTextoIdentificador(), 
			PROD_ARQUIVO_DE_PARA_ESPECIALIDADES.getTextoIdentificador(), PROD_PASTA_CONSOLIDADO_LEITOS.getTextoIdentificador(),
			PROD_PASTA_CONSOLIDADO_LEITOS_CDIDR.getTextoIdentificador(), PROD_NOME_ARQUIVO_CONSOLIDADO.getTextoIdentificador());
	
	private String pastaLeitos;
	private String pastaUrgencia;
	private String arquivoConsolidadoUrgencia;
	private String arquivoEntidades;
	private String pastaArquivosCenso;
	private String arquivoDeParaEspecialidades;
	private String pastaConsolidadoLeitos;
	private String pastaConsolidadoLeitosCDIDR;
	private String nomeArquivoConsolidado;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDIDRLeitos(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDIDRLeitos(String pastaLeitos, String pastaUrgencia, String arquivoConsolidadoUrgencia, String arquivoEntidades, String pastaArquivosCenso, String arquivoDeParaEspecialidades, String pastaConsolidadoLeitos, String pastaConsolidadoLeitosCDIDR, String nomeArquivoConsolidado)
	{
		this.pastaLeitos = pastaLeitos;
		this.pastaUrgencia= pastaUrgencia; 
		this.arquivoConsolidadoUrgencia = arquivoConsolidadoUrgencia;
		this.arquivoEntidades = arquivoEntidades;
		this.pastaArquivosCenso = pastaArquivosCenso;
		this.arquivoDeParaEspecialidades = arquivoDeParaEspecialidades;
		this.pastaConsolidadoLeitos = pastaConsolidadoLeitos;
		this.pastaConsolidadoLeitosCDIDR = pastaConsolidadoLeitosCDIDR;
		this.nomeArquivoConsolidado = nomeArquivoConsolidado;
		
		System.out.println(this.pastaConsolidadoLeitosCDIDR + " - " + pastaConsolidadoLeitosCDIDR);
		System.out.println(getPastaConsolidadoLeitosCDIDR());
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

	public String getPastaLeitos() {
		return pastaLeitos;
	}

	public void setPastaLeitos(String pastaLeitos) {
		this.pastaLeitos = pastaLeitos;
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

	public String getPastaUrgencia() {
		return pastaUrgencia;
	}

	public void setPastaUrgencia(String pastaUrgencia) {
		this.pastaUrgencia = pastaUrgencia;
	}

	public String getPastaConsolidadoLeitos() {
		return pastaConsolidadoLeitos;
	}

	public void setPastaConsolidadoLeitos(String pastaConsolidadoLeitos) {
		this.pastaConsolidadoLeitos = pastaConsolidadoLeitos;
	}

	public String getPastaConsolidadoLeitosCDIDR() {
		return pastaConsolidadoLeitosCDIDR;
	}

	public void setPastaConsolidadoLeitosCDIDR(String pastaConsolidadoLeitosCDIDR) {
		this.pastaConsolidadoLeitosCDIDR = pastaConsolidadoLeitosCDIDR;
	}

	public String getNomeArquivoConsolidado() {
		return nomeArquivoConsolidado;
	}

	public void setNomeArquivoConsolidado(String nomeArquivoConsolidado) {
		this.nomeArquivoConsolidado = nomeArquivoConsolidado;
	}
}
