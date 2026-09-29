package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaFichasAssumidasUrgencia {
		
	INDICE_COLUNA_MEDICO_REGULADOR(0, 0, "Médico Regulador", "String", ""),
	INDICE_COLUNA_CASOS_ASSUMIDOS(1, 1, "Casos Assumidos", "Int", ""),
	INDICE_COLUNA_CASOS_ENCAMINHADOS(2, 2, "Casos Encaminhados", "Int", ""),
	INDICE_COLUNA_CASOS_REGULADOS_QUANTIDADE(3, 3, "Casos Regulados", "Int", ""),
	INDICE_COLUNA_CASOS_REGULADOS_PORCENTAGEM(4, 3, "Casos Regulados", "Double", ""),
	INDICE_COLUNA_CASOS_PENDENTES_QUANTIDADE(5, 3, "Casos Pendentes", "Int", ""),
	INDICE_COLUNA_CASOS_PENDENTES_PORCENTAGEM(6, 3, "Casos Pendentes", "Double", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(7, 11, "Quantidade Esperada de Colunas", "", ""),
	LINHA_INICIAL_TABELA(8, 3, "Primeira linha de resultados", "", ""),
	
	TEXTO_TOTAL(9, 0, "Total", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(10, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(11, 0, "xls", "", "");
	
	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaFichasAssumidasUrgencia(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaFichasAssumidasUrgencia> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaFichasAssumidasUrgencia::getIdUnico, Function.identity()));

    public static ParametrosTabelaFichasAssumidasUrgencia poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
