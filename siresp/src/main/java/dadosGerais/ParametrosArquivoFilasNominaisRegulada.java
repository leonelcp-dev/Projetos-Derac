package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoFilasNominaisRegulada {
		
	
	EXTENSAO_ARQUIVO_REGULADA_AGENDAMENTO(0, 0, "xls", "", ""),
	EXTENSAO_ARQUIVO_REGULADA_SOLICITACOES(1, 0, "xls", "", ""),
	EXTENSAO_ARQUIVO_CDR(2, 0, "csv", "", ""),
	PREFIXO_NOME_ARQUIVO_REGULADA_AGENDAMENTO(3, 0, "AGENDAMENTO PENDENTE", "", ""),
	PREFIXO_NOME_ARQUIVO_REGULADA_SOLICITACOES(4, 0, "SOLICITACOES PENDENTES", "", ""),
	TEXTO_REGULADA(5, 0, "REGULADA", "", ""),
	EXTENSAO_ARQUIVO_XLSX(6, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_XLS(7, 0, "xls", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoFilasNominaisRegulada(int idUnico, int indice, String descricao, String tipo, String formato)
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
