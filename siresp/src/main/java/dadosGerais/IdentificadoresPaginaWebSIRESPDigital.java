package dadosGerais;

public enum IdentificadoresPaginaWebSIRESPDigital {

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
	
	TITULO_TELA_INTERNA_SPAN_USUARIO(12, "Usuário"),
	TITULO_TELA_INTERNA_BOTAO_PESQUISAR(13, "Pesquisar"),
	
	ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO(13, "data-table-usuario"),
	ID_TEXT_TELA_INTERNA_FILTRO_LOGIN(14, "duosystem_corebundle_usuario_login"),
	ID_SELECT_TELA_INTERNA_FITLRO_STATUS(15, "duosystem_corebundle_usuario_flagAtivo"),
	
	CLASS_NAME_TELA_INTERNA_PAGINACAO(15, "pagination"),
	
	//XPATH_BOTAO_EDITAR_LINHA_USUARIO(16, ""),
	//XPATH_BOTAO_DROP_DOWN_LINHA_USUARIO(17, "//button[@class='btn btn-sm btn-default dropdown-toggle']"),

	XPATH_BOTAO_DROP_DOWN_LINHA_USUARIO(17, ".//button[contains(concat(' ', normalize-space(@class), ' '), ' btn ') and contains(concat(' ', normalize-space(@class), ' '), ' btn-sm ') and contains(concat(' ', normalize-space(@class), ' '), ' btn-default ') and contains(concat(' ', normalize-space(@class), ' '), ' dropdown-toggle ')]"),

	//XPATH_BOTAO_EDITAR_PERFIL_USUARIO(18, "//a[@class='perfil']"),
	XPATH_BOTAO_EDITAR_PERFIL_USUARIO(18, ".//a[contains(concat(' ', normalize-space(@class), ' '), ' perfil ')]"),
	XPATH_BOTAO_NOVO_USUARIO(19, "//a[@class='btn btn-primary']"),
	XPATH_BOTAO_SIM_EDITAR_CADASTRO_EXISTENTE(20, "//button[@type='button'][normalize-space()='Sim']"),
	
	ID_TEXT_USUARIO_NOME(20, "duosystem_corebundle_usuario_nome"),
	ID_TEXT_USUARIO_EMAIL(21, "duosystem_corebundle_usuario_email"),
	ID_TEXT_USUARIO_RG(22, "duosystem_corebundle_usuario_rg"),
	ID_TEXT_USUARIO_CPF(23, "duosystem_corebundle_usuario_cpf"),
	ID_TEXT_USUARIO_CELULAR(24, "duosystem_corebundle_usuario_celular"),
	ID_TEXT_USUARIO_SENHA(25, "duosystem_corebundle_usuario_senha"),
	ID_TEXT_USUARIO_CONFIRMAR_SENHA(26, "duosystem_corebundle_usuario_confirmarSenha"),
	ID_TEXT_USUARIO_DATA_DE_CRIACAO(26, "duosystem_corebundle_usuario_dataCadastro"),
	ID_SELECT_USUARIO_ATIVO(26, "duosystem_corebundle_usuario_flagAtivo"),
	ID_TEXT_PESQUISAR_USUARIO_POR_LOGIN(27, "duosystem_corebundle_usuario_login"),

	ID_BOTAO_ADICIONAR_PERFIL(27, "adicionar"),
	ID_BOTAO_CADASTRAR_PERFIS(27, "cadastrarPerfisChecked"),
	ID_BOTAO_CADASTRAR_USUARIO(27, "btnAdicionar"),
	ID_BOTAO_ATUALIZAR_USUARIO(27, "btnAdicionar"),
	ID_BOTAO_INCLUIR_USUARIO(27, "btnAdicionar"),
	XPATH_BOTAO_VOLTAR_NOVO_USUARIO(28, "//a[@class='btn btn-default']"),
	XPATH_BOTAO_VOLTAR_NOVO_PERFIL_USUARIO(28, "//a[@class='btn btn-default']"),
	XPATH_BOTAO_VOLTAR_EDICAO_USUARIO(28, "//a[normalize-space()='Voltar']"),
	XPATH_ALERTA_ERRO_INCLUIR_USUARIO(28, "//div[@class='alert alert-info']"),
	
	MASCARA_PARA_ITENS_DINAMICOS(29, "###"),
	SEGUNDA_MASCARA_PARA_ITENS_DINAMICOS(29, "@@@"),
	
	ID_DINAMICO_MODULO_LOGIN(29, "duosystem_corebundle_usuario_usuarioUnidadeServicoSistemaPerfis_" + MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador() + "_sistema"),
	ID_DINAMICO_PERFIL_LOGIN(29, "duosystem_corebundle_usuario_usuarioUnidadeServicoSistemaPerfis_" + MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador() + "_perfil"),
	ID_DINAMICO_HORARIO_LOGIN(29, "duosystem_corebundle_usuario_usuarioUnidadeServicoSistemaPerfis_" + MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador() + "_horario"),
	
	ID_DIV_DINAMICA_ACESSOS_MODULO(29, "2"),
	ID_DIV_DINAMICA_ACESSOS_PERFIL(29, "3"),
	
	XPATH_DINAMICO_LINHA_PERFIL(29, "/html/body/div[1]/div[1]/section[2]/div[1]/form[1]/div[1]/fieldset[1]/div[1]/div[" + MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador() + "]/div[2]/div[" + SEGUNDA_MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador() + "]/div[1]/span[1]/ul[1]"),
	COMPLEMENTO_XPATH_DIV_LINHA_PERFIL_MODULO(23, "//div[2]//div[2]//div[1]//span[1]//ul[1]"),
	COMPLEMENTO_XPATH_DIV_LINHA_PERFIL_PERFIL(23, "//div[3]//div[2]//div[1]//span[1]//ul[1]"),
	//XPATH_DINAMICO_LINHA_PERFIL(29, "//div[@id='email-fields-list-row']/div[" + MASCARA_PARA_ITENS_DINAMICOS.getTextoIdentificador() + "]/div[1]"),
	XPATH_BOTAO_EDITAR_USUARIO(29, ".//a[normalize-space(.)='Editar' and contains(concat(' ', normalize-space(@class), ' '), ' btn ') and contains(concat(' ', normalize-space(@class), ' '), ' btn-sm ') and contains(concat(' ', normalize-space(@class), ' '), ' btn-default ')]"),
	
	CLASS_NAME_LINHA_PERFIL(29, "row tag"),
	XPATH_CLASS_NAME_LINHA_PERFIL_VISIVEL(29, "//*[contains(concat(' ', normalize-space(@class), ' '), ' row ') and contains(concat(' ', normalize-space(@class), ' '), ' tag ') and not(contains(concat(' ', normalize-space(@class), ' '), ' hide '))]"),
	CLASS_NAME_LINHA_PERFIL_OCULTA(29, "row tag hide"),
	XPATH_CLASS_NAME_TODAS_LINHAS(29, "//*[contains(concat(' ', normalize-space(@class), ' '), ' row ') and contains(concat(' ', normalize-space(@class), ' '), ' tag ')]"),
	
	
	PREFIXO_ID_LINHA_PERFIL(29, "row"),
	
	ERRO_AO_ACESSAR_UNIDADE(29, "Não foi possível acessar a Unidade"),
	ERRO_AO_ACESSAR_MODULO(29, "Não foi possível acessar o módulo Ambulatorial"),
	ERRO_AO_ACESSAR_PERFIL(29, "Não há perfil Gestor de Acessos"),
	
	TEXTO_SEM_RESTRICAO_DE_HORARIO(10, "Não"),
	
	TEXTO_FILTRO_STATUS_VALOR_ATIVO(10, "ATIVO"),
	TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE(10, "SELECIONE"),
	TEXTO_FILTRO_MODULO(10, "AMBULATORIAL"),
	TEXTO_FILTRO_PERFIL(11, "Gestor de acessos");
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPaginaWebSIRESPDigital(int indice, String textoIdentificador)
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