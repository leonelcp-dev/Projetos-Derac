package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaProducaoExecutanteExames {
		
	INDICE_COLUNA_GRUPO_COTA(0, 0, "Especialidade", "String", ""),
	INDICE_COLUNA_OFERTA_TOTAL(1, 1, "Oferta Total", "Int", ""),
	INDICE_COLUNA_AGENDADO_COTA(2, 2, "Agendado Cota", "Int", ""),
	INDICE_COLUNA_AGENDADO_EXTRA(3, 3, "Agendado Extra", "Int", ""),
	INDICE_COLUNA_REALIZADO(4, 4, "Realizado", "Int", ""),
	INDICE_COLUNA_PRESENTE(5, 5, "Presente", "Int", ""),
	INDICE_COLUNA_PRESENTE_PORCENTAGEM(6, 6, "Presente", "Double", ""),
	INDICE_COLUNA_AUSENTE(7, 7,"Ausente", "Int", ""),
	INDICE_COLUNA_AUSENTE_PORCENTAGEM(8, 8,"Ausente Porcentagem", "Double", ""),
	INDICE_COLUNA_DESISTENTE(9, 9,"Desistente", "Int", ""),
	INDICE_COLUNA_DESISTENTE_PORCENTAGEM(10, 10,"Desistente Porcentagem", "Double", ""),
	INDICE_COLUNA_DISPENSADO(11, 11,"Dispensado", "Int", ""),
	INDICE_COLUNA_DISPENSADO_PORCENTAGEM(12, 12,"Dispensado Porcentagem", "Double", ""),
	INDICE_COLUNA_NAO_INFORMADO(13, 13,"Não Informado", "Int", ""),
	INDICE_COLUNA_NAO_INFORMADO_PORCENTAGEM(14, 14,"Não Informado Porcentagem", "Double", ""),
	INDICE_COLUNA_ABSENTEISMO(15, 15,"Absenteismo", "Int", ""),
	INDICE_COLUNA_ABSENTEISMO_PORCENTAGEM(16, 16,"Absenteismo Porcentagem", "Double", ""),
	INDICE_COLUNA_ALTA(17, 17,"Alta", "Int", ""),
	INDICE_COLUNA_ALTA_PORCENTAGEM(18, 18,"Alta", "Porcentagem", ""),
	
	QUANTIDADE_ESPERADA_DE_COLUNAS(19, 19, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(30, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(31, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaProducaoExecutanteExames(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaProducaoExecutanteExames> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaProducaoExecutanteExames::getIdUnico, Function.identity()));

    public static ParametrosTabelaProducaoExecutanteExames poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
