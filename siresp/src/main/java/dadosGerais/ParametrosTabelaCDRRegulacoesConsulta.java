package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaCDRRegulacoesConsulta {
		
	INDICE_COLUNA_UNIDADE_SOLICITANTE(0, 0, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(1, 1, "Especialidade", "String", ""),
	INDICE_COLUNA_CID(2, 2, "CID", "String", ""),
	INDICE_COLUNA_PROFISSIONAL(3, 3, "Profissional", "String", ""),
	INDICE_COLUNA_TIPO(4, 4, "Tipo", "String", ""),
	INDICE_COLUNA_DATA_ENTRADA(5, 5, "Data Entrada", "String", ""),
	INDICE_COLUNA_UNIDADE_EXECUTANTE(6, 6, "Unidade executante", "String", ""),
	INDICE_COLUNA_STATUS(7, 7, "Status", "String", ""),
	INDICE_COLUNA_USUARIO(8, 8, "Usuário", "String", ""),
	INDICE_COLUNA_DATA_HORA(9, 9, "Data/Hora", "String", ""),
	INDICE_COLUNA_ACOES(10, 10, "Ações", "String", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(11, 11, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(12, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(13, 0, "xls", "", "");
	
	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaCDRRegulacoesConsulta(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaCDRRegulacoesConsulta> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaCDRRegulacoesConsulta::getIdUnico, Function.identity()));

    public static ParametrosTabelaCDRRegulacoesConsulta poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
