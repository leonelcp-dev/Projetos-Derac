package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoConsolidadoGEFIC {
		
	INDICE_COLUNA_INICIAL_RELATORIOS(0, 1, "Coluna ajustada de acordo com a coluna B do arquivo", "", ""),
	INDICE_LINHA_INICIAL_RELATORIOS(1, 4, "Ajustado de acordo com a linha 5 do arquivo", "", ""),
	
	NOME_PLANILHA_GERAL(16, 0, "GERAL", "", ""),
	
	TEXTO_TOTAL(17, 0, "TOTAL", "", ""),
	
	EXTENSAO_ARQUIVO(18, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoConsolidadoGEFIC(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoConsolidadoGEFIC> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoConsolidadoGEFIC::getIdUnico, Function.identity()));

    public static ParametrosArquivoConsolidadoGEFIC poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
