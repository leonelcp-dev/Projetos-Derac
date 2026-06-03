package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoCensoPlanilhaOrient {
		
	INDICE_COLUNA_REFERENCIA_DADOS(0, 2, "Dados", "", ""),
	INDICE_COLUNA_INICIO_VIGENCIA(1, 2, "Início", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_FINAL_VIGENCIA(1, 3, "Fim", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_PLANILHA_RELATORIO(2, 4, "Relatório", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(15, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA(16, 0, "Orient", "", ""),
	
	EXTENSAO_ARQUIVO(17, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoCensoPlanilhaOrient(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoCensoPlanilhaOrient> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoCensoPlanilhaOrient::getIdUnico, Function.identity()));

    public static ParametrosArquivoCensoPlanilhaOrient poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
