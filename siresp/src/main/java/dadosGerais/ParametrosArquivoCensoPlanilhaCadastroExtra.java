package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoCensoPlanilhaCadastroExtra {
		
	INDICE_COLUNA_UNIDADE(0, 0, "Unidade", "", ""),
	INDICE_COLUNA_DESCRICAO_ENFERMARIA(1, 1, "Descrição Enfermaria", "String", ""),
	INDICE_COLUNA_DESCRICAO_LEITO(2, 2, "Descrição Leito", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(3, 3, "Especialidade", "String", ""),
	INDICE_COLUNA_PACTUADO(4, 4, "Pactuado", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(15, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	NOME_PLANILHA(16, 0, "EXTRAS CADASTRADOS", "", ""),
	
	TEXTO_STATUS_LEITO_ATIVO(17, 0, "ativo", "", ""),
	TEXTO_STATUS_LEITO_INATIVO(18, 0, "i n a t i v o", "", ""),
	TEXTO_NAO_PACTUADO(19, 0, "NÃO", "", ""),
	TEXTO_PACTUADO(20, 0, "SIM", "", ""),
	
	EXTENSAO_ARQUIVO(21, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoCensoPlanilhaCadastroExtra(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoCensoPlanilhaCadastroExtra> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoCensoPlanilhaCadastroExtra::getIdUnico, Function.identity()));

    public static ParametrosArquivoCensoPlanilhaCadastroExtra poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
