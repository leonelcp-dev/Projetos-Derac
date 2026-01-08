package dadosGerais;

public enum IdentificadoresPaginaWebSIRESP {

	ID_FRAME_MENU(0, "site"),
	ID_FRAME_COMPONENTES(0, "principal"),
	ID_MENU(0, "example"),
	ID_LEITOS_FILTRO_UNIDADE(0, "FLT_CBO_UNIDADE"),
	ID_LEITOS_FILTRO_ESPECIALIDADE(0, "FLT_CBO_SERVICO"),
	ID_LEITOS_FILTRO_DESCRICAO(0, "FLT_DESC_LEITO"),
	ID_LEITOS_FILTRO_STATUS(0, "FLT_CBO_ATIVIDADE"),
	ID_LEITOS_FILTRO_SEXO(0, "FLT_CBO_SEXO"),
	ID_LEITOS_BOTAO_BUSCAR(0, "btnBuscar"),
	ID_TABELA_CENSO(0, "tab-listagem"),
	NUM_COLUNA_TABELA_CENSO_DESCRICAO_ENFERMARIA(4, "Descrição Enfermaria"),
	NAME_LEITOS_BOTAO_DOWNLOAD(0, "btn_excel"),
	NAME_LEITOS_BOTAO_BUSCAR(0, "btn_acao");
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPaginaWebSIRESP(int indice, String textoIdentificador)
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
