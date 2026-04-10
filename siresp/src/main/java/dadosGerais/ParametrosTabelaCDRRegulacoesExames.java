package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaCDRRegulacoesExames {
		
	INDICE_COLUNA_UNIDADE_SOLICITANTE(0, 0, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_GRUPO_DE_COTA(1, 1, "Grupo de Cota", "String", ""),
	INDICE_COLUNA_EXAME(2, 2, "Exame", "String", ""),
	INDICE_COLUNA_CID(3, 3, "CID", "String", ""),
	INDICE_COLUNA_DATA_ENTRADA(4, 4, "Data Entrada", "String", ""),
	INDICE_COLUNA_UNIDADE_EXECUTANTE(5, 5, "Unidade executante", "String", ""),
	INDICE_COLUNA_STATUS(6, 6, "Status", "String", ""),
	INDICE_COLUNA_USUARIO(7, 7, "Usuário", "String", ""),
	INDICE_COLUNA_DATA_HORA(8, 8, "Data/Hora", "String", ""),
	INDICE_COLUNA_ACOES(9, 9, "Ações", "String", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(10, 10, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(11, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(12, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaCDRRegulacoesExames(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaCDRRegulacoesExames> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaCDRRegulacoesExames::getIdUnico, Function.identity()));

    public static ParametrosTabelaCDRRegulacoesExames poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
