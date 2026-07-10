package dadosGerais;

public enum IdentificadoresPaginaWebGEFIC {

	ID_LOGIN(0, "username"),
	ID_PASSWORD(0, "password"),
	ID_LOTACAO_ACESSO_TABELA(0, "estabelecimentos"),
	
	MASCARA_VALOR_DINAMICO(0, "#####"),
	MASCARA_COLUNA_ACAO_DINAMICO(0, "@@@@@"),
	
	XPATH_LOTACAO_ACESSO_LINHA(0, "/html/body/div[2]/div/div/div/div/div/div[1]/div[2]/div[2]/table/tbody/tr[" + MASCARA_VALOR_DINAMICO.getTextoIdentificador() + "]/td/p"),
	XPATH_MENU_PRINCIPAL(0, "/html/body/div[1]/nav/ul"),
	XPATH_AGUARDANDO(0, "/html/body/div[7]/div"),
	XPATH_AGUARDANDO_SAIDA(0, "/html/body/div[6]/div"),
	
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_DIV_RODAPE_TABELA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[2]/table/tfoot/tr/th/div[1]"),
	CLASS_NAME_RELATORIO_QUANTIDADE_PACIENTES_LINHA_RODAPE_TABELA(0, ".row.no-gutters"),
	CLASS_NAME_FILAS_PROXIMA_PAGINA_HABILITADO(0, ".page-item.page-next"),
	CLASS_NAME_FILAS_PROXIMA_PAGINA_DESABILITADO(0, ".page-item.page-next.disabled"),
	
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_ESTABELECIMENTO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div/div/span/span[1]/span/span[1]/span"),
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_TEXT_ESTABELECIMENTO(0, "/html/body/span/span/span[1]/input"),
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_UL_ESTABELECIMENTOS(0, "/html/body/span/span/span[2]/ul"),
	
	XPATH_RELATORIO_TRANSFERENCIAS_DATA_INICIAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[2]"),
	XPATH_RELATORIO_TRANSFERENCIAS_DATA_FINAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[4]"),
	
	XPATH_RELATORIO_ENTRADAS_DATA_INDICACAO_INICIAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[2]"),
	XPAHT_RELATORIO_ENTRADAS_DATA_INDICACAO_FINAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[4]"),
	
	XPATH_RELATORIO_ENTRADAS_DATA_INICIAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[3]/div/div/input[2]"),
	XPAHT_RELATORIO_ENTRADAS_DATA_FINAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[3]/div/div/input[4]"),
	
	XPATH_RELATORIO_SAIDAS_ESTABELECIMENTO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[1]/div/span/span[1]/span/span[1]"),
	XPATH_RELATORIO_SAIDAS_TEXT_ESTABELECIMENTO(0, "/html/body/span/span/span[1]/input"),
	XPATH_RELATORIO_SAIDAS_UL_ESTABELECIMENTO(0, "/html/body/span/span/span[2]/ul"),
	XPATH_RELATORIO_SAIDAS_PRIMEIRA_LI_ESTABELECIMENTO(0, "/html/body/span/span/span[2]/ul/li"),
	XPATH_RELATORIO_SAIDAS_REMOVER_ESTABELECIMENTO_SELECIONADO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[1]/div/span/span[1]/span/span[1]/span"),
	
	XPATH_FILAS_BOTAO_FILTROS(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[1]/div/div/div[1]/button"),
	XPATH_FILAS_FILTRO_STATUS(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[1]/div/div/div[2]/span/span[1]/span/span[1]"),
	XPATH_FILAS_FILTRO_STATUS_UL(0, "/html/body/span/span/span[2]/ul"),
	XPATH_FILAS_FILTRO_NOME_PACIENTE(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[1]/div[1]/div[1]/div/input"),
	XPATH_FILAS_BOTAO_MAIS_COLUNAS(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/button"),
	
	XPATH_FILAS_CHECK_BOX_DINAMICO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[" + MASCARA_VALOR_DINAMICO.getTextoIdentificador() + "]/input"),
	XPATH_FILAS_CHECK_BOX_POSICAO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[1]/input"),
	XPATH_FILAS_CHECK_BOX_TELEFONE(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[2]/input"),
	XPATH_FILAS_CHECK_BOX_DATA_NASCIMENTO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[3]/input"),
	XPATH_FILAS_CHECK_BOX_IDADE(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[4]/input"),
	XPATH_FILAS_CHECK_BOX_PRIORIZACAO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[5]/input"),
	XPATH_FILAS_CHECK_BOX_ESPECIALIDADE(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[6]/input"),
	XPATH_FILAS_CHECK_BOX_SUBESPECIALIDADE(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[7]/input"),
	XPATH_FILAS_CHECK_BOX_PROCEDIMENTO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[8]/input"),
	XPATH_FILAS_CHECK_BOX_CID(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[9]/input"),
	XPATH_FILAS_CHECK_BOX_DATA_INDICACAO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[10]/input"),
	XPATH_FILAS_CHECK_BOX_DATA_INSERCAO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[11]/input"),
	XPATH_FILAS_CHECK_BOX_DATA_EXECUCAO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[12]/input"),
	XPATH_FILAS_CHECK_BOX_DATA_SAIDA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[13]/input"),
	XPATH_FILAS_CHECK_BOX_ESTABELECIMENTO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[14]/input"),
	XPATH_FILAS_CHECK_BOX_SITUACAO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[15]/input"),
	XPATH_FILAS_CHECK_BOX_CIDADE(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[16]/input"),
	XPATH_FILAS_CHECK_BOX_TELEFONE_ADICIONAL_1(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[17]/input"),
	XPATH_FILAS_CHECK_BOX_TELEFONE_ADICIONAL_2(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[18]/input"),
	XPATH_FILAS_CHECK_BOX_UNIDADE_BASICA_REFERENCIA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[19]/input"),
	XPATH_FILAS_CHECK_BOX_UNIDADE_SOLICITANTE(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[20]/input"),
	XPATH_FILAS_CHECK_BOX_TEMPO_DE_ESPERA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[21]/input"),
	XPATH_FILAS_CHECK_BOX_TEMPO_MEDIO_DE_ESPERA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[22]/input"),
	XPATH_FILAS_CHECK_BOX_TEMPO_MAXIMO_DE_ESPERA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[23]/input"),
	XPATH_FILAS_CHECK_BOX_OBSERVACOES(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[24]/input"),
	XPATH_FILAS_CHECK_BOX_ACOES(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[1]/div[2]/div/div/label[25]/input"),
	
	XPATH_FILAS_TABELA_RESULTADOS(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[2]/div[2]/table"),
	XPATH_FILAS_BOTAO_ACAO_DINAMICO(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[2]/div[2]/table/tbody/tr" + MASCARA_VALOR_DINAMICO.getTextoIdentificador() + "/td[" + MASCARA_COLUNA_ACAO_DINAMICO.getTextoIdentificador() + "]/div/button"),
	XPATH_FILAS_PROXIMA_PAGINA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/div[3]/div[2]/ul/li[9]/a"),
	
	ARIA_LABEL_FILAS_PROXIMA_PAGINA(0, "próxima página"),
	
	XPATH_FILAS_TEXT_AREA_OBSERVACAO_HISTORICO(0, "/html/body/div[1]/div/div[2]/div[1]/div[1]/div[2]/div/div[2]/div[1]/div/div/div/form/div[2]/div/div/textarea"),
	ID_FILAS_TEXT_AREA_OBSERVACAO_HISTORICO(0, "obs_original"),
	
	CLASS_RELATORIO_SAIDAS_REMOVER_ESTABELECIMENTO_SELECIONADO(0, "select2-selection__clear"),
	
	
	XPATH_RELATORIO_SAIDAS_DATA_INICIAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[2]"),
	XPATH_RELATORIO_SAIDAS_DATA_FINAL(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div/input[4]"),
	
	
	ID_RELATORIO_SAIDA_PACIENTES_SELECT_ESTABELECIMENTOS(0, "select2-estabelecimento-gt-results"),
	
	ID_RELATORIO_SAIDA_PACIENTES_SPAN_ESTABELECIMENTO(0, "select2-estabelecimento-ub-container"),
	
	ID_RELATORIO_QUANTIDADE_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_RELATORIO_SAIDA_PACIENTES_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_FILAS_BOTAO_PESQUISAR(0, "pesquisar"),
	ID_FILAS_BOTAO_LIMPAR_FILTRO(0, "limpar"),
	ID_RELATORIO_TRANSFERENCIA_PACIENTES_BOTAO_EXCEL(0, "excel"),
	ID_RELATORIO_ENTRADA_PACIENTES_BOTAO_EXCEL(0, "excel"),
	ID_RELATORIO_SAIDA_PACIENTES_BOTAO_EXCEL(0, "excel"),
	
	ID_RELATORIO_SAIDA_PACIENTES_OPCAO_ANALITICO(0, "analitico"),
	
	XPATH_RELATORIO_QUANTIDADE_PACIENTES_RODAPE_TABELA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[2]/table/tfoot"),
	
	XPATH_RELATORIO_TRANSFERENCIA_PACIENTES_TABELA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div[2]/table"),
	
	XPATH_RELATORIO_TRANSFERENCIA_PROXIMA_PAGINA(0, "/html/body/div[1]/div/div[2]/div[2]/div/div/div[2]/div[1]/div[3]/div[2]/ul/li[9]/a"),
	
	NAME_LOGIN_BOTAO_ENTRAR(0, "login"),
	
	TEXTO_UNIDADE_REGULACAO(0, "Regulacao"),
	TEXTO_STATUS_REGISTRO_CANCELADO(0, "Registros cancelados");
	
	
		
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
