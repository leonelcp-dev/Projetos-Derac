package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoNomenclaturas {
		
	INDICE_COLUNA_INSERCAO(0, 0, "INSERÇÃO", "String", ""),
	INDICE_COLUNA_NOMENCLATURA(1, 1, "NOMENCLATURA", "String", ""),
	INDICE_COLUNA_FLUXO(2, 2, "FLUXO", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(3, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(4, 0, "GRUPO DE COTAS E ESPECIALIDADES", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(5, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(6, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoNomenclaturas(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoNomenclaturas> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoNomenclaturas::getIdUnico, Function.identity()));

    public static ParametrosArquivoNomenclaturas poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
