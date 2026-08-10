package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDIDRGEFIC {

	REFERENCIA_PASTAS_MONITORAMENTO_LEITOS_CDIDR(1, "Demanda Reprimida CDIDR"),
	
	TEXTO_IDENTIFICADOR_CIRURGIA_ELETIVA(0, "CIRURGIA ELETIVA"),
	TEXTO_IDENTIFICADOR_ELETIVAS(0, "ELETIVAS"),
	TEXTO_IDENTIFICADOR_OPM(0, "OPM"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_RELATORIOS_GEFIC(9, "GEFIC"),
	TESTE_ARQUIVO_CONSOLIDADO_GEFIC_CIRURGIA_ELETIVA(9, TESTE_PASTA_RELATORIOS_GEFIC.getTextoIdentificador() + "\\Consolidado\\GEFIC " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ".xlsx"),
	TESTE_ARQUIVO_CONSOLIDADO_GEFIC_OPM(9, TESTE_PASTA_RELATORIOS_GEFIC.getTextoIdentificador() + "\\Consolidado\\GEFIC OPM " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ".xlsx"),
	TESTE_ARQUIVO_ENTIDADES(9,"GEFIC\\unidadesGEFIC.csv"),
	TESTE_ARQUIVO_STATUS_NORMALIZADO(9,"GEFIC\\StatusNormalizados.xlsx"),
	TESTE_ARQUIVO_MONITORAMENTO_NAO_CONFORMIDADES(9, TESTE_PASTA_RELATORIOS_GEFIC.getTextoIdentificador() + "\\Consolidado\\MONITORAMENTO " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ".xlsx"),

	PROD_PASTA_RELATORIOS_GEFIC(9, "Urgencia"),
	PROD_ARQUIVO_CONSOLIDADO_GEFIC_CIRURGIA_ELETIVA(9, PROD_PASTA_RELATORIOS_GEFIC.getTextoIdentificador() + "\\Consolidado\\GEFIC OPM " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ".xlsx"),
	PROD_ARQUIVO_CONSOLIDADO_GEFIC_OPM(9, PROD_PASTA_RELATORIOS_GEFIC.getTextoIdentificador() + "\\Consolidado\\GEFIC OPM " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ".xlsx"),
	PROD_ARQUIVO_ENTIDADES(9, "GEFIC\\unidadesGEFIC.csv"),
	PROD_ARQUIVO_STATUS_NORMALIZADO(9,"GEFIC\\StatusNormalizados.xlsx"),
	PROD_ARQUIVO_MONITORAMENTO_NAO_CONFORMIDADES(9, PROD_PASTA_RELATORIOS_GEFIC.getTextoIdentificador() + "\\Consolidado\\MONITORAMENTO " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ".xlsx"),

	TESTE(TESTE_PASTA_RELATORIOS_GEFIC.getTextoIdentificador(), TESTE_ARQUIVO_ENTIDADES.getTextoIdentificador(), 
			TESTE_ARQUIVO_CONSOLIDADO_GEFIC_CIRURGIA_ELETIVA.getTextoIdentificador(), TESTE_ARQUIVO_CONSOLIDADO_GEFIC_OPM.getTextoIdentificador(),
			TESTE_ARQUIVO_STATUS_NORMALIZADO.getTextoIdentificador(), TESTE_ARQUIVO_MONITORAMENTO_NAO_CONFORMIDADES.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_RELATORIOS_GEFIC.getTextoIdentificador(), PROD_ARQUIVO_ENTIDADES.getTextoIdentificador(), 
			PROD_ARQUIVO_CONSOLIDADO_GEFIC_CIRURGIA_ELETIVA.getTextoIdentificador(), PROD_ARQUIVO_CONSOLIDADO_GEFIC_OPM.getTextoIdentificador(), 
			PROD_ARQUIVO_STATUS_NORMALIZADO.getTextoIdentificador(), PROD_ARQUIVO_MONITORAMENTO_NAO_CONFORMIDADES.getTextoIdentificador());
	
	private String pastaDadosGEFIC;
	private String arquivoConsolidadoGEFICCirurgiasEletivas;
	private String arquivoConsolidadoGEFICOPM;
	private String arquivoEntidades;
	private String arquivoStatusNormalizados;
	private String arquivoMonitoramentoNaoConformidades;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDIDRGEFIC(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDIDRGEFIC(String pastaDadosGEFIC, String arquivoEntidades, String arquivoConsolidadoGEFICCirurgiasEletivas, 
			String arquivoConsolidadoGEFICOPM, String arquivoStatusNormalizados, String arquivoMonitoramentoNaoConformidades)
	{
		this.pastaDadosGEFIC = pastaDadosGEFIC;
		this.arquivoConsolidadoGEFICCirurgiasEletivas = arquivoConsolidadoGEFICCirurgiasEletivas;
		this.arquivoEntidades = arquivoEntidades;
		this.arquivoConsolidadoGEFICOPM = arquivoConsolidadoGEFICOPM;
		this.arquivoStatusNormalizados = arquivoStatusNormalizados;
		this.arquivoMonitoramentoNaoConformidades = arquivoMonitoramentoNaoConformidades;
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

	public String getPastaDadosGEFIC() {
		return pastaDadosGEFIC;
	}

	public void setPastaDadosGEFIC(String pastaDadosGEFIC) {
		this.pastaDadosGEFIC = pastaDadosGEFIC;
	}

	public String getArquivoConsolidadoGEFICCirurgiasEletivas() {
		return arquivoConsolidadoGEFICCirurgiasEletivas;
	}

	public void setArquivoConsolidadoGEFICCirurgiasEletivas(String arquivoConsolidadoGEFICCirurgiasEletivas) {
		this.arquivoConsolidadoGEFICCirurgiasEletivas = arquivoConsolidadoGEFICCirurgiasEletivas;
	}

	public String getArquivoEntidades() {
		return arquivoEntidades;
	}

	public void setArquivoEntidades(String arquivoEntidades) {
		this.arquivoEntidades = arquivoEntidades;
	}

	public String getArquivoConsolidadoGEFICOPM() {
		return arquivoConsolidadoGEFICOPM;
	}

	public void setArquivoConsolidadoGEFICOPM(String arquivoConsolidadoGEFICOPM) {
		this.arquivoConsolidadoGEFICOPM = arquivoConsolidadoGEFICOPM;
	}

	public String getArquivoStatusNormalizados() {
		return arquivoStatusNormalizados;
	}

	public void setArquivoStatusNormalizados(String arquivoStatusNormalizados) {
		this.arquivoStatusNormalizados = arquivoStatusNormalizados;
	}

	public String getArquivoMonitoramentoNaoConformidades() {
		return arquivoMonitoramentoNaoConformidades;
	}

	public void setArquivoMonitoramentoNaoConformidades(String arquivoMonitoramentoNaoConformidades) {
		this.arquivoMonitoramentoNaoConformidades = arquivoMonitoramentoNaoConformidades;
	}
}
