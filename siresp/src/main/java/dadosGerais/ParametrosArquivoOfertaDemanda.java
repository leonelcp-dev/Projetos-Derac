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
	INDICE_COLUNA_FPO(7, 8, "FPO", "Int", ""),
	INDICE_COLUNA_NOVAS_SOLICITACOES(8, 9, "Novas Solicitações", "Int", ""),
	INDICE_COLUNA_OFERTA_TOTAL(9, 10, "Oferta Total", "Int", ""),
	INDICE_COLUNA_OFERTA_BLOQUEADA(10, 11,"Oferta Bloqueada", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_TOTAL(11, 12,"Agendamentos Total", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_COTA(12, 13,"Agendamentos Cota", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_BOLSAO(13, 14,"Agendamentos Bolsão", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO(14, 15,"Agendamentos Não Distribuídos", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS_EXTRA(15, 16,"Agendamentos Extras", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_PRESENTE(16, 17,"Atendimentos Total", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_AUSENTE(17, 18,"Atendimentos Ausentes", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO(18, 19,"Atendimentos Ausentes Calculado", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA(19, 20,"Atendimentos Desistencia", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_DISPENSADO(20, 21,"Atendimentos Dispensado", "Int", ""),
	INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO(21, 22,"Atendimentos Não Informado", "Int", ""),
	INDICE_COLUNA_CALCULOS_ATENDIDO(22, 23,"Cálculos Atendido", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_AUSENTE(23, 24,"Cálculos Ausente", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_DESISTENCIA(24, 25,"Cálculos Desistência", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_DISPENSADO(25, 26,"Cálculo Dispensado", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_NAO_INFORMADO(26, 27,"Cálculo Não Informado", "Porcentagem", ""),
	INDICE_COLUNA_DEMANDA_REPRIMIDA(27, 28,"Demanda Reprimida", "Int", ""),
	INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA(28, 29,"Cálculos Tempo de Espera", "Double", ""),
	INDICE_COLUNA_RECEPCAO_FECHADA(29, 30,"Recepção Fechada", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(30, 10, "Ajustado de acordo com o Java, no arquivo é a linha 11", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(31, 0, "Ofertas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(32, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(33, 0, "xls", "", "");
	

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
