package dadosGerais;

public enum IdentificadoresPaginaWebSIRESPInicio {

	ID_FRAME_MENU(0, "site"),
	ID_FRAME_COMPONENTES(0, "principal"),
	ID_FRAME_DISTRIBUICAO_COTAS_TABELA_RESULTADOS(0, "frame1"),
	ID_MENU(0, "example"),
	
	ID_TEXTO_LOGIN_AMBULATORIAL(0, "usuario_4"),
	ID_TEXTO_SENHA_AMBULATORIAL(0, "senha_4"),
	ID_TEXTO_CAPTCHA_AMBULATORIAL(0, "txt_captcha_4"),
	ID_IMAGEM_CAPTCHA_AMBULATORIAL(0, "captcha_4"),
	ID_BOTAO_ENTRAR_AMBULATORIAL(0, "btn_entrar_4"),
	ID_TEXTO_DIGITOS_DOCUMENTOS(0, "digito_doc"),
	ID_BOTAO_ENTRAR_VALIDAR_DOCUMENTO(0, "btn_entrar"),
	
	TEXTO_ULTIMOS(0, "últimos"),
	TEXTO_PRIMEIROS(0, "primeiros"),
	TEXTO_RG(0, "RG"),
	TEXTO_CPF(0, "CPF"),
	TEXTO_CODIGO_SEGURANCA_INVALIDO(0, "O código de segurança não é válido"),
	
	QUANTIDADE_DIGITOS(3, "QUANTIDADE_DIGITOS"),
	
	XPATH_TEXTO_DOCUMENTO_SOLICITADO(0, "/html/body/form/table/tbody/tr[3]/td/table[2]/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr/td[1]/label"),
	XPATH_MODULO_AMBULATORIAL(0, "/html/body/div/div[4]/div/div/div[2]/div[1]/a"),
	XPATH_BOTAO_OK_CODIGO_SEGURANCA_INVALIDO(0, "/html/body/div[2]/div/div[6]/button[1]"),
	XPATH_BOTAO_FECHAR(0, "/html/body/form/table/tbody/tr[2]/td/table/tbody/tr/td/table[1]/tbody/tr/td[5]/a"),
	XPATH_UNIDADE_AUTENTICADA(0, "/html/body/form/table/tbody/tr[1]/td/table/tbody/tr[2]/td[1]/table/tbody/tr/td[5]"),
	XPATH_CAIXA_OPCOES_UNIDADES(0, "/html/body/form/table/tbody"),
	XPATH_TEXTO_DIGITOS_DOCUMENTOS(0, "/html/body/form/table/tbody/tr[3]/td/table[2]/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td[3]/table/tbody/tr/td/table/tbody/tr/td[1]/input[1]");
	
	
		
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPaginaWebSIRESPInicio(int indice, String textoIdentificador)
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
