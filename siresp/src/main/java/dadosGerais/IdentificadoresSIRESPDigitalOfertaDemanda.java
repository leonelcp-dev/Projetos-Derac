package dadosGerais;

public enum IdentificadoresSIRESPDigitalOfertaDemanda {

	ID_TELA_INTERNA_FILTRO_UNIDADE(0, "duosystem_corebundle_usuarioacl_unidadeServico"),
	ID_TELA_INTERNA_FILTRO_MODULO(1, "duosystem_corebundle_usuarioacl_sistema"),
	ID_TELA_INTERNA_FILTRO_PERFIL(2, "duosystem_corebundle_usuarioacl_perfil"),
	//ID_TELA_INTERNA_FILTRO_PERFIL(2, "select2-drop-mask"),
	
	ID_TELA_INICIAL_FILTRO_UNIDADE(3, "s2id_duosystem_corebundle_usuarioacl_unidadeServico"),
	ID_TELA_INICIAL_TEXTO_FILTRO_UNIDADE(4, "s2id_autogen3_search"),
	
	ID_TELA_INICIAL_FILTRO_MODULO(5, "s2id_duosystem_corebundle_usuarioacl_sistema"),
	ID_TELA_INICIAL_TEXTO_FILTRO_MODULO(6, "s2id_autogen4_search"),
	
	ID_TELA_INICIAL_FILTRO_PERFIL(7, "s2id_duosystem_corebundle_usuarioacl_perfil"),
	ID_TELA_INICIAL_TEXTO_FILTRO_PERFIL(8, "s2id_autogen5_search"),
	ID_TELA_INICIAL_OPCOES_PERFIL(9, "select2-results-5"),
	
	ID_DIV_CARREGANDO_PAGINA(10, "loading-page"),
	ID_DIV_CARREGANDO_ELEMENTO(11, "loading-element"),
	ID_DIV_MODAL_CADASTRO_EXISTENTE(12, "include-user-modal"),
	
	TITULO_TELA_INTERNA_SPAN_RELATORIOS(12, "Relatórios"),
	
	
	XPATH_RELATORIO_CDR_DETALHADO(12, "//a[@href='/pt_BR/relatorio-fila-detalhado/']"),
	XPATH_RELATORIO_PRODUCAO_EXECUTANTE(12, "//span[normalize-space()='Produção executante']"),
	XPATH_BOTAO_PESQUISAR(12, "//button[normalize-space()='Pesquisar']"),
	XPATH_TABELA_RESULTADOS(12, "//*[@id=\"box-resultados\"]/div/div/div/div/table"),
	XPATH_TABELA_RESULTADOS_NENHUM_RESULTADO(12, "//b[normalize-space()='Nenhum registro encontrado.']"),
	XPATH_OPCAO_UNIDADE_SOLICITANTE(12, "/html/body/div[3]/ul/li/div/option"),
	XPATH_SPAN_MENU_USUARIO(12, "//a[@title='USUÁRIO']//span[contains(text(),'Usuário')]"),
	
	ID_SELECT_TIPO_DE_RECURSO(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_tipoAtendimento"),
	ID_SELECT_TIPO_DE_MARCACAO(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_tipoMarcacao"),
	ID_SELECT_PERFIL_RELATORIO(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_perfilRelatorio"),
	ID_SELECT_TIPO_EMISSAO(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_tipoEmissao"),
	ID_TEXT_DATA_INICIAL(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_dataInicial"),
	ID_TEXT_DATA_FINAL(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_dataFinal"),
	ID_SELECT_VISUALIZACAO(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_visualizacao"),
	ID_SELECT_UNIDADE_SOLICITANTE(12, "duosystem_relatoriobundle_relatorioproducaoexecutante_unidadeServicoSolicitante"),
	
	TEXTO_TIPO_RECURSO_CONSULTA(12, "Consulta"),
	TEXTO_TIPO_RECURSO_EXAMES(12, "Exames"),
	TEXTO_TIPO_RECURSO_PROCEDIMENTOS(12, "Procedimentos"),
	TEXTO_TIPO_MARCACAO_PRIMEIRA_CONSULTA(12, "Primeira Consulta"),
	TEXTO_PERFIL_RELATORIO_RECURSO(12, "Recurso"),
	TEXTO_TIPO_EMISSAO_RESUMIDO(12, "Resumido"),
	TEXTO_VISUALIZACAO_EM_TELA(12, "Em Tela"),
	TEXTO_VINCULO_ESTADUAL(12, "ESTADUAL"),
	TEXTO_SOLICITANTE_SMS_CAMPINAS(12, "SMS - CAMPINAS"),
	TEXTO_OPCAO_UNIDADE_SOLICITANTE_SMS_CAMPINAS(12, "175360 | 5416655 - SMS - CAMPINAS / CAMPINAS"),

	TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE(10, "SELECIONE"),
	TEXTO_FILTRO_MODULO(10, "AMBULATORIAL"),
	TEXTO_FILTRO_PERFIL(11, "EXECUTANTE");
	
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresSIRESPDigitalOfertaDemanda(int indice, String textoIdentificador)
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