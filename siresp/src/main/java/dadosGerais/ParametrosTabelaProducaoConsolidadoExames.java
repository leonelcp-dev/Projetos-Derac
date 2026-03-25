package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaProducaoConsolidadoExames {
		
	INDICE_COLUNA_GRUPO_DE_COTA(0, 0, "Grupo de Cota", "String", ""),
	INDICE_COLUNA_OFERTA(1, 1, "Oferta Total", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL(2, 2, "Agendado Total", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL_INTERNO(3, 3, "Agendamento Total Interno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL_EXTERNO(4, 4, "Agendamento Total Externo", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_TOTAL_DESPERDICIO(5, 5, "Agendamento desperdíciio", "Double", ""),
	INDICE_COLUNA_AGENDAMENTO_COTA_INTERNO(6, 6, "Atendamento Cota Interno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_COTA_EXTERNO(7, 7, "Agendamento Cota Externo", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_BOLSAO_INTERNO(8, 8, "Agendamento Bolsão Interno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_BOLSAO_EXTERNO(9, 9, "Agendamento Bolsão Interconsulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_INTERNO(10, 10, "Agendamento Não Distribuído Interno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_EXTERNO(11, 11, "Agendamento Não Distribuído Interconsulta", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_EXTRA_INTERNO(12, 12, "Agendamento Extra Interno", "Int", ""),
	INDICE_COLUNA_AGENDAMENTO_EXTRA_EXTERNO(13, 13, "Agendamento Extra Externo", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_PRESENTE_INTERNO(14, 14, "Presente Interno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_PRESENTE_EXTERNO(15, 15, "Presente Externo", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_AUSENTE_INTERNO(16, 16, "Ausente Interno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_AUSENTE_EXTERNO(17, 17, "Ausente Externo", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_INTERNO(18, 18, "Desistência Interno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_EXTERNO(19, 19, "Desistência Externo", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DISPENSADO_INTERNO(20, 20, "Dispensado Interno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_DISPENSADO_EXTERNO(21, 21, "Dispensado Externo", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_INTERNO(22, 22, "Não informado Interno", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_EXTERNO(23, 23, "Não Informado Externo", "Int", ""),
	INDICE_COLUNA_ATENDIMENTO_ABSENTEISMO_INTERNO(24, 24, "Absenteismo Interno", "Double", ""),
	INDICE_COLUNA_ATENDIMENTO_ABSENTEISMO_EXTERNO(25, 25, "Absenteismo Externo", "Double", ""),
	
	QUANTIDADE_ESPERADA_DE_COLUNAS(26, 26, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(27, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(28, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaProducaoConsolidadoExames(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaProducaoConsolidadoExames> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaProducaoConsolidadoExames::getIdUnico, Function.identity()));

    public static ParametrosTabelaProducaoConsolidadoExames poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
