package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoOfertaPlanilhaDemanda {
		
	INDICE_COLUNA_PROCEDIMENTOS(0, 1, "Procedimentos (Padronizado)", "String", ""),
	INDICE_COLUNA_TIPO_DE_OFERTA(1, 2, "Tipo de Oferta", "String", ""),
	INDICE_COLUNA_COMPETENCIA(2, 3, "Competência", "Date mes/ano", "mmm/yyyy"),
	INDICE_COLUNA_NOVAS_SOLICITACOES(3, 4, "Novas Solicitações", "Int", ""),
	INDICE_COLUNA_DEMANDA_REPRIMIDA(4, 5,"Demanda Reprimida", "Int", ""),
	INDICE_COLUNA_OFERTA_TOTAL(5, 6, "Oferta Disponível", "Int", ""),
	INDICE_COLUNA_AGENDAMENTOS(6, 7, "Agendamentos", "Int", ""),
	INDICE_COLUNA_PERDA_PRIMARIA(7, 8, "Perda Primária", "Int", ""),
	INDICE_COLUNA_TAXA_PERDA_PRIMARIA(8, 9, "Taxa de Perda Primária", "Porcentagem", ""),
	INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA(9, 10,"Cálculos Tempo de Espera", "Int", ""),
	INDICE_COLUNA_MAIS_VELHO_NA_FILA(10, 11,"Mais Velho na Fila", "Int", ""),
	INDICE_COLUNA_OFERTA_ATIVA(11, 12,"Oferta ativa", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(12, 10, "Ajustado de acordo com o Java, no arquivo é a linha 11", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(13, 0, "Demandas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(14, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(15, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoOfertaPlanilhaDemanda(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoOfertaPlanilhaDemanda> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoOfertaPlanilhaDemanda::getIdUnico, Function.identity()));

    public static ParametrosArquivoOfertaPlanilhaDemanda poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
