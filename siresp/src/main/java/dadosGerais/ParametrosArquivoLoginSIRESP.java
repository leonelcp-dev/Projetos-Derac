package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoLoginSIRESP {
		
	INDICE_COLUNA_INDICE(0, 0, "Indice", "", ""),
	INDICE_COLUNA_UNIDADE(0, 1, "Unidade", "", ""),
	INDICE_COLUNA_NOME_COMPLETO(0, 2, "Nome Completo", "", ""),
	INDICE_COLUNA_EMAIL(0, 3, "E-mail do colaborador", "", ""),
	INDICE_COLUNA_RG(0, 4, "RG", "", ""),
	INDICE_COLUNA_CPF(0, 5, "CPF", "", ""),
	INDICE_COLUNA_TELEFONE(0, 6, "Número de telefone do colaborador", "", ""),
	INDICE_COLUNA_SENHA_PROVISORIA(0, 7, "Senha provisória", "", ""),
	INDICE_COLUNA_MODULO(0, 8, "Módulo", "", ""),
	INDICE_COLUNA_PERFIL(0, 9, "Perfil do Login", "", ""),
	INDICE_COLUNA_HORARIO(0, 10, "Horário", "", ""),
	INDICE_COLUNA_LOGIN(0, 11, "Login", "", ""),
	INDICE_COLUNA_EXECUTADO(0, 12, "Executado", "", ""),
	INDICE_COLUNA_METODO(0, 13, "Método", "", ""),
	INDICE_COLUNA_OBSERVACAO(0, 14, "Observação", "", ""),
	INDICE_COLUNA_STATUS(0, 15, "Status", "", ""),
	EXTENSAO_ARQUIVO_LOGIN(0, 0, "xlsx", "", ""),
	TEXTO_CONFIRMACAO_EXECUTADO(0, 0, "ok", "", ""),
	TEXTO_METODO_EXECUTADO(0, 0, "Automático", "", ""),
	TEXTO_HORARIO(0, 0, "Sem Restrição", "", ""),
	TEXTO_SENHA_PROVISORIA(0, 0, "Mudar@123", "", ""),
	TEXTO_SENHA_PROVISORIA_NAO_SE_APLICA(0, 0, "-----", "", ""),
	TEXTO_PERFIL_JA_VINCULADO(0, 0, "Perfil já vinculado", "", ""),
	NOME_PLANILHA_LOGINS(0, 0, "Perfis", "", ""),
	NOME_PLANILHA_PROCESSADOS(0, 0, "Processados", "", ""),
	TEXTO_ERRO_AO_CADASTRAR_USUARIO(0, 0, "Erro ao cadastrar usuário", "", ""),
	TEXTO_UNIDADE_CONCLUIDA(0, 0, "Unidade concluída com sucesso", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoLoginSIRESP(int idUnico, int indice, String descricao, String tipo, String formato)
	{
		this.setIdUnico(idUnico);
		this.setIndice(indice);
		this.setDescricao(descricao);
		this.setTipo(tipo);
		this.setFormato(formato);
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getIdUnico() {
		return idUnico;
	}

	public void setIdUnico(int idUnico) {
		this.idUnico = idUnico;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}	
	
 
}
