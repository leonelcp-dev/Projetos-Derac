package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaProducaoExecutanteConsultas {
		
	INDICE_COLUNA_ESPECIALIDADE(0, 0, "Especialidade", "String", ""),
	INDICE_COLUNA_OFERTA_TOTAL(1, 1, "Oferta Total", "Int", ""),
	INDICE_COLUNA_OFERTA_NAO_DISTRIBUIDA(2, 2, "Não Distribuído", "Int", ""),
	INDICE_COLUNA_AGENDADO_COTA(3, 3, "Agendado Cota", "Int", ""),
	INDICE_COLUNA_AGENDADO_EXTRA(4, 4, "Agendado Extra", "Int", ""),
	INDICE_COLUNA_AGENDADO_PRIMEIRA_CONSULTA_POR_BOLSAO(5, 5, "Agendado Primeira Consulta de Bolsão", "Int", ""),
	INDICE_COLUNA_ATENDIDO_TOTAL(6, 6, "Atendido Total", "Int", ""),
	INDICE_COLUNA_ATENDIDO_PORCENTAGEM(7, 7, "Atendido Porcentagem", "Double", ""),
	INDICE_COLUNA_ATENDIDO_PRESENCIAL(8, 8, "Atendido Presencial", "Int", ""),
	INDICE_COLUNA_ATENDIDO_PRESENCIAL_PORCENTAGEM(9, 9, "Atendido Presencial Porcentagem", "Double", ""),
	INDICE_COLUNA_ATENDIDO_TELECONSULTA(10, 10,"Teleconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIDO_TELECONSULTA_PORCENTAGEM(11, 11,"Teleconsulta Porcentagem", "Double", ""),
	INDICE_COLUNA_AUSENTE(12, 12,"Ausente", "Int", ""),
	INDICE_COLUNA_AUSENTE_PORCENTAGEM(13, 13,"Ausente Porcentagem", "Double", ""),
	INDICE_COLUNA_DESISTENTE(14, 14,"Desistente", "Int", ""),
	INDICE_COLUNA_DESISTENTE_PORCENTAGEM(15, 15,"Desistente Porcentagem", "Double", ""),
	INDICE_COLUNA_DISPENSADO(16, 16,"Dispensado", "Int", ""),
	INDICE_COLUNA_DISPENSADO_PORCENTAGEM(17, 17,"Dispensado Porcentagem", "Double", ""),
	INDICE_COLUNA_NAO_INFORMADO(18, 18,"Não Informado", "Int", ""),
	INDICE_COLUNA_NAO_INFORMADO_PORCENTAGEM(19, 19,"Não Informado Porcentagem", "Double", ""),
	INDICE_COLUNA_ABSENTEISMO(20, 20,"Absenteismo", "Int", ""),
	INDICE_COLUNA_ABSENTEISMO_PORCENTAGEM(21, 21,"Absenteismo Porcentagem", "Double", ""),
	INDICE_COLUNA_ALTA(22, 22,"Alta", "Int", ""),
	INDICE_COLUNA_ALTA_PORCENTAGEM(23, 23,"Alta", "Porcentagem", ""),
	
	QUANTIDADE_ESPERADA_DE_COLUNAS(24, 24, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(30, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(31, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaProducaoExecutanteConsultas(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaProducaoExecutanteConsultas> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaProducaoExecutanteConsultas::getIdUnico, Function.identity()));

    public static ParametrosTabelaProducaoExecutanteConsultas poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
