package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoGEFICNaoConformidades {
		
	
	INDICE_COLUNA_NUMERO_SOLICITACAO(0, 0, "Numero Solicitação", "String", ""),
	INDICE_COLUNA_DATA_FINALIZACAO(1, 1, "Data Finalização", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_NOME(2, 2, "Nome", "String", ""),
	INDICE_COLUNA_EXECUTANTE(3, 3, "Executante", "String", ""),
	INDICE_COLUNA_PROCEDIMENTO_CIRURGICO(4, 4, "Procedimento Cirurgico", "String", ""),
	INDICE_COLUNA_DATA_INSERCAO_GEFIC(5, 5, "Data Inserção GEFIC", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_DATA_SAIDA_GEFIC(6, 6, "Data Saída GEFIC", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_DATA_EXECUCAO(7, 7, "Data Execução", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_NAO_CONFORMIDADE(8, 8, "Não Conformidade", "String", ""),
	INDICE_COLUNA_ALERTA(9, 9, "Alerta", "String", ""),
	INDICE_COLUNA_COMENTARIOS_ANALISE_GEFIC(10, 10, "Comentários Análise GEFIC", "String", ""),
	INDICE_COLUNA_OUTRAS_INFORMACOES(11, 11, "Outras Informações", "String", ""),
	
	TEXTO_SEPARADOR_NAO_CONFORMIDADES(20, 0, " / ", "", ""),
	TEXTO_NAO_INSERIDO(21, 0, "NÃO INSERIDO", "", ""),
	TEXTO_INSERCAO_APOS_CIRURGIA(22, 0, "INSERÇÃO APÓS A CIRURGIA", "", ""), 
	TEXTO_CODIGO_DE_PROCEDIMENTO_INCORRETO(23, 0, "CÓDIGO DE PROCEDIMENTO INCORRETO", "", ""),
	TEXTO_SEM_ATUALIZCAO_DE_STATUS(24, 0, "SEM ATUALIZAÇÃO DE STATUS", "", ""),
	TEXTO_SAIDA_APOS_7_DIAS(25, 7, "SAÍDA APÓS 7 DIAS", "", ""), 
	TEXTO_INSERCAO_ERRADA(26, 0, "INSERÇÃO ERRADA", "", ""),
	TEXTO_FICHA_SIRESP_INSERIDA_APOS_CIRURGIA(27, 0, "FICHA SIRESP INSERIDA APÓS A CIRURGIA", "", ""),

	LINHA_INICIAL_ARQUIVO(30, 1, "Correspondente a linha 2 do Excel", "", ""),
	TEXTO_DESCONSIDERAR(31, 0, "DESCONSIDERAR", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoGEFICNaoConformidades(int idUnico, int indice, String descricao, String tipo, String formato)
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
