package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoOfertaDemanda {
		
	INDICE_COLUNA_UNIDADE(0, 1, "Unidade", "String", ""),
	INDICE_COLUNA_VINCULO(1, 2, "Vínculo", "String", ""),
	INDICE_COLUNA_COMPETENCIA(2, 3, "Competência", "Date mes/ano", "mmm/yyyy"),
	INDICE_COLUNA_TIPO_OFERTA(3, 4, "Tipo de Oferta", "String", ""),
	INDICE_COLUNA_PROCEDIMENTOS(4, 5, "Procedimentos (Padronizado)", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(5, 6, "Especialidade", "String", ""),
	INDICE_COLUNA_CLASSIFICACAO(6, 7, "Classificação", "String", ""),
	INDICE_COLUNA_OFERTAS_PREVISTAS(7, 8, "Ofertas Previstas", "Int", ""),
	INDICE_COLUNA_NOVAS_SOLICITACOES_MENSAIS(8, 9, "Novas Solicitações (mensais)", "Int", ""),
	INDICE_COLUNA_OFERTA_DISPONIVEL(9, 10, "Oferta Disponível", "Int", ""),
	INDICE_COLUNA_OFERTA_SIRESP(10, 11, "Oferta SIRESP", "Int", ""),
	INDICE_COLUNA_OFERTA_BLOQUEADA(11, 12,"Oferta Bloqueada", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_TOTAL(12, 13,"Agendamentos Total", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_COTA(13, 14,"Agendamentos Cota", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_BOLSAO(14, 15,"Agendamentos Bolsão", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO(15, 16,"Agendamentos Não Distribuídos", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_EXTRA(16, 17,"Agendamentos Extras", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_PRESENTE(17, 18,"Atendimentos Total", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_AUSENTE(18, 19,"Atendimentos Ausentes", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO(19, 20,"Atendimentos Ausentes Calculado", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA(20, 21,"Atendimentos Desistencia", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_DISPENSADO(21, 22,"Atendimentos Dispensado", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO(22, 23,"Atendimentos Não Informado", "Int", ""),
	INDICE_COLUNA_CALCULOS_AGENDAMENTO(23, 24,"Cálculos Agendamentos", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_PERDA_PRIMARIA(24, 25,"Cálculos Perda Primária", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_ATENDIDO(25, 26,"Cálculos Atendido", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_AUSENTE(26, 27,"Cálculos Ausente", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_DESISTENCIA(27, 28,"Cálculos Desistência", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_DISPENSADO(28, 29,"Cálculo Dispensado", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_NAO_INFORMADO(29, 30,"Cálculo Não Informado", "Porcentagem", ""),
	INDICE_COLUNA_DEMANDA_REPRIMIDA_DO_DIA(30, 31,"Demanda Reprimida", "Int", ""),
	INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA(31, 32,"Cálculos Tempo de Espera", "Int", ""),
	INDICE_COLUNA_MAIOR_TEMPO_DE_ESPERA_EM_DIAS(32, 33,"Maior tempo de espera em dias", "Int", ""),
	INDICE_COLUNA_RECEPCAO_FECHADA(33, 34,"Recepção Fechada", "String", ""),
	INDICE_COLUNA_OBSERVACAO(34, 35,"Recepção Fechada", "String", ""),
	INDICE_COLUNA_DIFERENCA_DE_OFERTA(35, 35,"Diferença de oferta", "Int", ""),
	
	LINHA_INICIAL_ARQUIVO(36, 10, "Ajustado de acordo com o Java, no arquivo é a linha 11", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(37, 0, "Ofertas", "", ""),
	
	TEXTO_ERRO_SELECIONAR_GRUPO_DE_COTA(38, 0, "Erro ao selecionar o grupo de cota", "", ""),
	
	INDICE_COLUNA_DATA_PROCESSAMENTO(39, 2, "Ajustado de acordo com o Java, no arquivo é a coluna 3 (C)", "", ""),
	INDICE_LINHA_DATA_PROCESSAMENTO(40, 7, "Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),
	
	NOME_ARQUIVO_CONSOLIDADO(41, 0, "ConsolidadoOfertaEDemanda.xlsx", "", ""),
	NOME_ARQUIVO_CONSOLIDADO_EM_PROCESSAMENTO(42, 0, "ConsolidadoOfertaEDemanda-temp.xlsx", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(43, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(44, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoOfertaDemanda(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoOfertaDemanda> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoOfertaDemanda::getIdUnico, Function.identity()));

    public static ParametrosArquivoOfertaDemanda poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
