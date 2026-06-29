package dadosGerais;

public enum IdentificadoresPaginaWebGEFIC {

	ID_LOGIN(0, "username"),
	ID_PASSWORD(0, "password"),
	ID_LOTACAO_ACESSO_TABELA(0, "estabelecimentos"),
	
	MASCARA_VALOR_DINAMICO(0, "#####"),
	
	XPATH_LOTACAO_ACESSO_LINHA(0, "/html/body/div[2]/div/div/div/div/div/div[1]/div[2]/div[2]/table/tbody/tr[" + MASCARA_VALOR_DINAMICO.getTextoIdentificador() + "]/td/p"),
	XPATH_MENU_PRINCIPAL(0, "/html/body/div[1]/nav/ul"),
	XPATH_AGUARDANDO(0, "/html/body/div[7]/div"),
	
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_DIV_RODAPE_TABELA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[2]/table/tfoot/tr/th/div[1]"),
	CLASS_NAME_RELATORIO_QUANTIDADE_PACIENTES_LINHA_RODAPE_TABELA(0, ".row.no-gutters"),
	
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_ESTABELECIMENTO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div/div/span/span[1]/span/span[1]/span"),
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_TEXT_ESTABELECIMENTO(0, "/html/body/span/span/span[1]/input"),
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_UL_ESTABELECIMENTOS(0, "/html/body/span/span/span[2]/ul"),
	
	XPATH_RELATORIO_TRANSFERENCIAS_DATA_INICIAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[2]"),
	XPATH_RELATORIO_TRANSFERENCIAS_DATA_FINAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[4]"),
	
	XPATH_RELATORIO_ENTRADAS_DATA_INICIAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[2]"),
	XPAHT_RELATORIO_ENTRADAS_DATA_FINAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[4]"),
	
	XPATH_RELATORIO_SAIDAS_DATA_INICIAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[2]"),
	XPATH_RELATORIO_SAIDAS_DATA_FINAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[4]"),
	
	
	ID_RELATORIO_QUANTIDADE_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_RELATORIO_SAIDA_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_EXCEL(0, "excel"),
	ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_EXCEL(0, "excel"),
	ID_RELATORIO_SAIDA_PACIENTES_BOTAO_EXCEL(0, "excel"),
	
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_RODAPE_TABELA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[2]/table/tfoot"),
	
	XPATH_RELATORIO_TRANSFERENCIA_PACIENTES_TABELA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[2]/table"),
	
	XPATH_RELATORIO_TRANSFERENCIA_PROXIMA_PAGINA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[3]/div[2]/ul/li[9]/a"),
	
	NAME_LOGIN_BOTAO_ENTRAR(0, "login"),
	
	TEXTO_UNIDADE_REGULACAO(0, "Regulacao");
	
	
		
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPaginaWebGEFIC(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
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
}
