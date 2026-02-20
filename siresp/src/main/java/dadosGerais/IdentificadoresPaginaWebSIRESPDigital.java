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
	
	TITULO_TELA_INTERNA_SPAN_USUARIO(12, "Usuário"),
	TITULO_TELA_INTERNA_BOTAO_PESQUISAR(13, "Pesquisar"),
	
	ID_TABELA_TELA_INTERNA_PESQUISA_USUARIO(13, "data-table-usuario"),
	ID_TEXT_TELA_INTERNA_FILTRO_LOGIN(14, "duosystem_corebundle_usuario_login"),
	
	CLASS_NAME_TELA_INTERNA_PAGINACAO(15, "pagination"),
	
	TEXTO_FILTRO_MODULO(10, "AMBULATORIAL"),
	TEXTO_FILTRO_PERFIL(11, "GESTOR DE ACESSOS");
	
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