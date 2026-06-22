package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoUrgenciaPlanilhaFormaResolucao {
		
	INDICE_COLUNA_DATA(0, 1, "Data", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_FORMA_DE_RESOLUCAO(1, 2, "Forma de Resolução", "String", ""),
	INDICE_COLUNA_SOLICITANTE(2, 3, "Solicitante", "String", ""),
	INDICE_COLUNA_EXECUTANTE(3, 4,"Executante", "String", ""),
	INDICE_COLUNA_LOCAL_DE_REGULACAO(4, 5, "Local de Regulação", "String", ""),
	INDICE_COLUNA_RECURSO(5, 6, "Recurso", "String", ""),
	INDICE_COLUNA_FICHA(6, 7, "Ficha", "String", ""),
	INDICE_COLUNA_QUANTIDADE(7, 8, "Quantidade", "Int", ""),
	
	LINHA_INICIAL_ARQUIVO(21, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_MONITORAMENTO(22, 0, "Forma de Resolução", "", ""),
	
	DIVISOR_CAMPOS(23, 0, "####", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(25, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(26, 0, "xls", "", ""),
	
	INDICE_COLUNA_DATA_PROCESSAMENTO(27, 2, "Ajustado de acordo com o Java, no arquivo é a coluna 3 (C)", "", ""),
	INDICE_LINHA_DATA_PROCESSAMENTO(28, 7, "Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),
	TEXTO_EXECUTANTE_VAZIO(29, 0, "VAZIO", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoUrgenciaPlanilhaFormaResolucao(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoUrgenciaPlanilhaFormaResolucao> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoUrgenciaPlanilhaFormaResolucao::getIdUnico, Function.identity()));

    public static ParametrosArquivoUrgenciaPlanilhaFormaResolucao poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
