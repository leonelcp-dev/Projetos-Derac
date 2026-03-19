package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaProducaoConsolidadoConsultas {
		
	INDICE_COLUNA_ESPECIALIDADE(0, 0, "Especialidade", "String", ""),
	INDICE_COLUNA_OFERTA_TOTAL(1, 1, "Oferta Total", "Int", ""),
	INDICE_COLUNA_OFERTA_PRIMEIRA_CONSULTA(2, 2, "Oferta Primeira Consulta", "Int", ""),
	INDICE_COLUNA_OFERTA_RETORNO(3, 3, "Oferta Retorno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL(4, 4, "Agendado Total", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL_PRIMEIRA_CONSULTA(5, 5, "Agendamento Total Primeira Consulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL_RETORNO(6, 6, "Agendamento Total Retorno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL_INTERCONSULTA(7, 7, "Agendamento Total Interconsulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL_DESPERDICIO(8, 8, "Agendamento desperdíciio", "Double", ""),
	INDICE_COLUNA_AGENDAMENTO_COTA_PRIMEIRA_CONSULTA(9, 9, "Atendamento Cota Primeira Consulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_COTA_RETORNO(10, 10, "Agendamento Cota Retorno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_COTA_INTERCONSULTA(11, 11, "Agendamento Cota Interconsulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_BOLSAO_PRIMEIRA_CONSULTA(12, 12, "Agendamento Bolsão Primeira Consulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_BOLSAO_INTERCONSULTA(13, 13, "Agendamento Bolsão Interconsulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_PRIMEIRA_CONSULTA(14, 14, "Agendamento Não Distribuído Primeira Consulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_INTERCONSULTA(15, 15, "Agendamento Não Distribuído Interconsulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_EXTRA_PRIMEIRA_CONSULTA(16, 16, "Agendamento Extra Primeira Consulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_EXTRA_RETORNO(17, 17, "Agendamento Extra Retorno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_EXTRA_INTERCONSULTA(18, 18, "Agendamento Extra Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_PRESENCIAL_PRIMEIRA_CONSULTA(19, 19, "Atendimento presencial Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_PRESENCIAL_RETORNO(20, 20, "Atendimento Presencial Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_PRESENCIAL_INTERCONSULTA(21, 21, "Atendiemnto Presencial Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_TELEATENDIMENTO_PRIMEIRA_CONSULTA(22, 22, "Atendimento Teleatendimento Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_TELEATENDIMENTO_RETORNO(23, 23, "Atendimento Teleatendimento Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_TELEATENDIMENTO_INTERCONSULTA(24, 24, "Atendimento Teleatendimento Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_TOTAL_PRIMEIRA_CONSULTA(25, 25, "Atendimento Total Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_TOTAL_RETORNO(26, 26, "Atendimento Total Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_TOTAL_INTERCONSULTA(27, 27, "Atendimento Total Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_AUSENTE_PRIMEIRA_CONSULTA(28, 28, "Ausente Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_AUSENTE_RETORNO(29, 29, "Ausente Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_AUSENTE_INTERCONSULTA(30, 30, "Ausente Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_PRIMEIRA_CONSULTA(31, 31, "Desistência Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_RETORNO(32, 32, "Desistência Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DESISTENCIA__INTERCONSULTA(33, 33, "Desistência Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DISPENSADO_PRIMEIRA_CONSULTA(34, 34, "Dispensado Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DISPENSADO_RETORNO(35, 35, "Dispensado Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DISPENSADO_INTERCONSULTA(36, 36, "Dispensado Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_PRIMEIRA_CONSULTA(37, 37, "Não informado Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_RETORNO(38, 38, "Não Informado Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_INTERCONSULTA(39, 39, "Não Informado Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_ALTA_PRIMEIRA_CONSULTA(40, 40, "Alta Primeira Consulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_ALTA_RETORNO(41, 41, "Alta Retorno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_ALTA_INTERCONSULTA(42, 42, "Alta Interconsulta", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_ABSENTEISMO_PRIMEIRA_CONSULTA(43, 43, "Absenteismo Primeira Consulta", "Double", ""),
	INDICE_COLUNA_ATENDIMENTO_ABSENTEISMO_RETORNO(44, 44, "Absenteismo Retorno", "Double", ""),
	INDICE_COLUNA_ATENDIMENTO_ABSENTEISMO_INTERCONSULTA(45, 45, "Absenteismo Interconsulta", "Double", ""),
	
	QUANTIDADE_ESPERADA_DE_COLUNAS(46, 46, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(47, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(48, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaProducaoConsolidadoConsultas(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaProducaoConsolidadoConsultas> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaProducaoConsolidadoConsultas::getIdUnico, Function.identity()));

    public static ParametrosTabelaProducaoConsolidadoConsultas poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
