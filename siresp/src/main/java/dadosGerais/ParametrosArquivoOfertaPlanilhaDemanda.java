package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoOfertaPlanilhaDemanda {
		
	INDICE_COLUNA_PROCEDIMENTOS(0, 1, "Procedimentos (Padronizado)", "String", ""),
	INDICE_COLUNA_COMPETENCIA(1, 2, "Competência", "Date mes/ano", "mmm/yyyy"),
	INDICE_COLUNA_NOVAS_SOLICITACOES(2, 3, "Novas Solicitações", "Int", ""),
	INDICE_COLUNA_DEMANDA_REPRIMIDA(3, 4,"Demanda Reprimida", "Int", ""),
	INDICE_COLUNA_OFERTA_TOTAL(4, 5, "Oferta Total", "Int", ""),
	INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA(5, 6,"Cálculos Tempo de Espera", "Double", ""),
	INDICE_COLUNA_MAIS_VELHO_NA_FILA(6, 7,"Mais Velho na Fila", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(8, 10, "Ajustado de acordo com o Java, no arquivo é a linha 11", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(9, 0, "Demandas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(11, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(12, 0, "xls", "", "");
	

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
