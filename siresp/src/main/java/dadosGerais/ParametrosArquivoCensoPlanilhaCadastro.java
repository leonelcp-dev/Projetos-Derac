package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoCensoPlanilhaCadastro {
		
	INDICE_COLUNA_UNIDADE(0, 0, "Unidade", "", ""),
	INDICE_COLUNA_DESCRICAO_ENFERMARIA(1, 1, "Descrição Enfermaria", "String", ""),
	INDICE_COLUNA_DESCRICAO_LEITO(2, 2, "Descrição Leito", "String", ""),
	INDICE_COLUNA_ATIVIDADE(3, 3, "Atividade", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(4, 4, "Especialidade", "String", ""),
	INDICE_COLUNA_CONCATENAR(5, 5, "Concatenar", "String", ""),
	INDICE_COLUNA_ATIVO(6, 6, "Atividade", "String", ""),
	INDICE_COLUNA_CONTABILIZA_NA_TAXA_DE_OCUPACAO(7, 7, "Contabiliza na Taxa de Ocupação", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(15, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	NOME_PLANILHA(16, 0, "", "", ""),
	
	TEXTO_STATUS_LEITO_ATIVO(17, 0, "ativo", "", ""),
	TEXTO_STATUS_LEITO_INATIVO(18, 0, "i n a t i v o", "", ""),
	TEXTO_NAO_CONSIDERADO_TAXA_OCUPACAO(19, 0, "NÃO", "", ""),
	
	EXTENSAO_ARQUIVO(20, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoCensoPlanilhaCadastro(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoCensoPlanilhaCadastro> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoCensoPlanilhaCadastro::getIdUnico, Function.identity()));

    public static ParametrosArquivoCensoPlanilhaCadastro poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
