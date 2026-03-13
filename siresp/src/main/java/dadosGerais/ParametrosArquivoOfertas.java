package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoOfertas {
		
	
	INDICE_COLUNA_UNIDADE(0, 0, "Unidade", "", ""),
	INDICE_COLUNA_TIPO_RECURSO(0, 1, "Tipo de Recurso", "", ""),
	INDICE_COLUNA_RECURSO(0, 2, "Recurso", "", ""),
	INDICE_COLUNA_OFERTA(0, 3, "Oferta", "", ""),
	INDICE_COLUNA_AGENDADO(0, 4, "Agendado", "", ""),
	INDICE_COLUNA_REALIZADO(0, 5, "Realizado", "", ""),
	NOME_PLANILHA_OFERTAS(0, 0, "Ofertas", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoOfertas(int idUnico, int indice, String descricao, String tipo, String formato)
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
