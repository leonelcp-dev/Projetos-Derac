package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoUrgenciaPlanilhaVagaZero {
		
	INDICE_COLUNA_DATA(0, 1, "Data", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_EXECUTANTE(1, 2, "Executante", "String", ""),
	INDICE_COLUNA_RECURSO(2, 3, "Recurso", "String", ""),
	INDICE_COLUNA_FICHA(3, 4,"Ficha", "String", ""),
	INDICE_COLUNA_TOTAL(4, 5, "Horas de Espera", "Int", ""),
	INDICE_COLUNA_VAGA_ZERO(5, 6, "Quantidade", "Int", ""),
	INDICE_COLUNA_ENCAMINHADO_PARA_REFERENCIA_PACTUADA(6, 7, "Encaminhado para referência pactuada", "Int", ""),
	INDICE_COLUNA_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA(7, 8, "Encaminhado para avaliação na referência de Complexidade Adequada", "Int", ""),
	INDICE_COLUNA_ENCAMINHADO_AUTOMATICAMENTE_PARA_REFERENCIA_PACTUADA(8, 9, "Encaminhado Automaticamente para referência pactuada", "Int", ""),
	
	LINHA_INICIAL_ARQUIVO(21, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_MONITORAMENTO(22, 0, "Vaga Zero", "", ""),
	
	DIVISOR_CAMPOS(23, 0, "####", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(25, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(26, 0, "xls", "", ""),
	
	INDICE_COLUNA_DATA_PROCESSAMENTO(27, 2, "Ajustado de acordo com o Java, no arquivo é a coluna 3 (C)", "", ""),
	INDICE_LINHA_DATA_PROCESSAMENTO(28, 7, "Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),;
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoUrgenciaPlanilhaVagaZero(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoUrgenciaPlanilhaVagaZero> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoUrgenciaPlanilhaVagaZero::getIdUnico, Function.identity()));

    public static ParametrosArquivoUrgenciaPlanilhaVagaZero poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
