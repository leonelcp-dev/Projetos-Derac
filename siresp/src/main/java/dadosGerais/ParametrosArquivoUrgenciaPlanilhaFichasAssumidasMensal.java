package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal {
		
	INDICE_COLUNA_COMPETENCIA(0, 1, "Competência", "Date", "mmm/yyyy"),
	INDICE_COLUNA_PERIODO(1, 2, "Período", "String", ""),
	INDICE_COLUNA_REGULADOR(2, 3, "Regulador", "String", ""),
	INDICE_COLUNA_ASSUMIDOS(3, 4, "Casos Assumidos", "Int", ""),
	INDICE_COLUNA_ENCAMINHADOS(4, 5, "Casos Encaminhados", "Int", ""),
	INDICE_COLUNA_REGULADOS(5, 6, "Casos Regulados", "Int", ""),
	INDICE_COLUNA_PENDENTES(6, 7, "Casos Pendentes", "Int", ""),
	
	LINHA_INICIAL_ARQUIVO(21, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_MONITORAMENTO(22, 0, "Fichas Assumidas Mensal", "", ""),
	
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
			
	ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal::getIdUnico, Function.identity()));

    public static ParametrosArquivoUrgenciaPlanilhaFichasAssumidasMensal poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
