package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoOfertaDemanda {
		
	INDICE_COLUNA_UNIDADE(0, 1, "Unidade", "String", ""),
	INDICE_COLUNA_CNES(1, 2, "CNES", "String", ""),
	INDICE_COLUNA_VINCULO(2, 3, "Vínculo", "String", ""),
	INDICE_COLUNA_COMPETENCIA(3, 4, "Competência", "Date mes/ano", "mmm/yyyy"),
	INDICE_COLUNA_TIPO_OFERTA(4, 5, "Tipo de Oferta", "String", ""),
	INDICE_COLUNA_PROCEDIMENTOS(5, 6, "Procedimentos (Padronizado)", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(6, 7, "Especialidade", "String", ""),
	INDICE_COLUNA_CLASSIFICACAO(7, 8, "Classificação", "String", ""),
	INDICE_COLUNA_OFERTAS_PREVISTAS(8, 9, "Ofertas Previstas", "Int", ""),
	INDICE_COLUNA_NOVAS_SOLICITACOES_MENSAIS(9, 10, "Novas Solicitações (mensais)", "Int", ""),
	INDICE_COLUNA_OFERTA_DISPONIVEL(10, 11, "Oferta Disponível", "Int", ""),
	INDICE_COLUNA_OFERTA_SIRESP(11, 12, "Oferta SIRESP", "Int", ""),
	INDICE_COLUNA_OFERTA_BLOQUEADA(12, 13,"Oferta Bloqueada", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_TOTAL(13, 14,"Agendamentos Total", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_COTA(14, 15,"Agendamentos Cota", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_BOLSAO(15, 16,"Agendamentos Bolsão", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO(16, 17,"Agendamentos Não Distribuídos", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_EXTRA(17, 18,"Agendamentos Extras", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_PRESENTE(18, 19,"Atendimentos Total", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_AUSENTE(19, 20,"Atendimentos Ausentes", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO(20, 21,"Atendimentos Ausentes Calculado", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA(21, 22,"Atendimentos Desistencia", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_DISPENSADO(22, 23,"Atendimentos Dispensado", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO(23, 24,"Atendimentos Não Informado", "Int", ""),
	INDICE_COLUNA_CALCULOS_AGENDAMENTO(24, 25,"Cálculos Agendamentos", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_PERDA_PRIMARIA(25, 26,"Cálculos Perda Primária", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_ATENDIDO(26, 27,"Cálculos Atendido", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_AUSENTE(27, 28,"Cálculos Ausente", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_DESISTENCIA(28, 29,"Cálculos Desistência", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_DISPENSADO(29, 30,"Cálculo Dispensado", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_NAO_INFORMADO(30, 31,"Cálculo Não Informado", "Porcentagem", ""),
	INDICE_COLUNA_DEMANDA_REPRIMIDA_DO_DIA(31, 32,"Demanda Reprimida", "Int", ""),
	INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA(32, 33,"Cálculos Tempo de Espera", "Int", ""),
	INDICE_COLUNA_MAIOR_TEMPO_DE_ESPERA_EM_DIAS(33, 34,"Maior tempo de espera em dias", "Int", ""),
	INDICE_COLUNA_RECEPCAO_FECHADA(34, 35,"Recepção Fechada", "String", ""),
	INDICE_COLUNA_OBSERVACAO(35, 36,"Observação", "String", ""),
	INDICE_COLUNA_DIFERENCA_DE_OFERTA(36, 36,"Atual", "Int", ""),
	INDICE_COLUNA_DIFERENCA_DE_OFERTA_ANTERIOR(37, 37,"Anterior", "Int", ""),
	INDICE_COLUNA_DIFERENCA_DE_OFERTA_ANTERIOR_2(38, 38,"Anterior 2", "Int", ""),
	
	LINHA_INICIAL_ARQUIVO(39, 10, "Ajustado de acordo com o Java, no arquivo é a linha 11", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(40, 0, "Ofertas", "", ""),
	
	TEXTO_ERRO_SELECIONAR_GRUPO_DE_COTA(41, 0, "Erro ao selecionar o grupo de cota", "", ""),
	
	INDICE_COLUNA_DATA_PROCESSAMENTO(42, 2, "Ajustado de acordo com o Java, no arquivo é a coluna 3 (C)", "", ""),
	INDICE_LINHA_DATA_PROCESSAMENTO(43, 7, "Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),
	
	NOME_ARQUIVO_CONSOLIDADO(44, 0, "ConsolidadoOfertaEDemanda.xlsx", "", ""),
	NOME_ARQUIVO_CONSOLIDADO_EM_PROCESSAMENTO(45, 0, "ConsolidadoOfertaEDemanda-temp.xlsx", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(46, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(47, 0, "xls", "", "");
	

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
