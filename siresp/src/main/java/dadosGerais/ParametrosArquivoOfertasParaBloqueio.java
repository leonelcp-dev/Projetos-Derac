package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoOfertasParaBloqueio {
		
	INDICE_COLUNA_UNIDADE(0, 0, "Unidade", "String", ""),
	INDICE_COLUNA_TIPO_OFERTA(1, 1, "Tipo de Oferta", "String", ""),
	INDICE_COLUNA_GRUPO(2, 2, "Grupo", "String", ""),
	INDICE_COLUNA_EQUIPAMENTO(3, 3, "Equipamento", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(4, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(5, 0, "Relacoes", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(6, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(7, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoOfertasParaBloqueio(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoOfertasParaBloqueio> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoOfertasParaBloqueio::getIdUnico, Function.identity()));

    public static ParametrosArquivoOfertasParaBloqueio poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
