package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaAgendaHorarioConsultas {
		
	INDICE_COLUNA_CHECK_LIST(0, 0, "", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(1, 1, "Especialidade", "String", ""),
	INDICE_COLUNA_REGULADA(2, 2, "Regulada", "String", ""),
	INDICE_COLUNA_HORARIOS(3, 3, "Horarios", "Int", ""),
	INDICE_COLUNA_EXTRA(4, 4, "Extra", "Int", ""),
	INDICE_COLUNA_LIVRE(5, 5, "Livre", "Int", ""),
	INDICE_COLUNA_AGENDADO(6, 6, "Agendado", "Int", ""),
	INDICE_COLUNA_DESBLOQUEADO(7, 7, "Desbloqueado", "Int", ""),
	INDICE_COLUNA_BLOQUEADO(8, 8, "Bloqueado", "Int", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(9, 9, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(10, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(11, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaAgendaHorarioConsultas(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaAgendaHorarioConsultas> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaAgendaHorarioConsultas::getIdUnico, Function.identity()));

    public static ParametrosTabelaAgendaHorarioConsultas poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
