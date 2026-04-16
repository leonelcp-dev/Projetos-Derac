package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoReguladaConsolidado {
	
	INDICE_COLUNA_SOLICITADO_EM(0, 0, "Solicitado em:", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_FICHA(1, 1, "Ficha", "String", ""),
	INDICE_COLUNA_CODIGO_PACIENTE(2, 2, "Código Paciente", "String", ""),
	INDICE_COLUNA_UNIDADE_SOLICITANTE(3, 3, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_TIPO_DE_OFERTA(4, 4, "Tipo de Oferta", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE_EXAME(5, 5, "Especialidade/Exame", "String", ""),
	INDICE_COLUNA_HIPOTESE(6, 6, "Hipótese", "String", ""),
	INDICE_COLUNA_ARQUIVO(7, 7, "Arquivo", "String", ""),
	INDICE_COLUNA_DATA_ARQUIVO(8, 8, "ArqRef", "Date", "dd/MM/yyyy"),

	ARQUIVO_FINAL_LINHA_INICIAL(9, 1,"Ajustado de acordo com o Java, no arquivo é a Linha 2", "", ""),
	ARQUIVO_BAIXADO_LINHA_INICIAL(10, 9,"Ajustado de acordo com o Java, no arquivo é a Linha 10", "", ""),
	
	ARQUIVO_MUNICIPAL_NOME(11, 6,"BD Demanda Reprimida - Regulada.xlsx", "", ""),
	ARQUIVO_MUNICIPAL_VAZIO(12, 6,"ARQUIVO REGULADA VAZIO.xlsx", "", ""),
	
	NOME_PLANILHA_ARQUIVO_DOWNLOAD(13, 8,"demanda_por_recurso_qualitativo", "", ""),
	NOME_PLANILHA_ARQUIVO_FORMATADO(14, 8,"BD - Regulada", "", ""),
	
	EXTENSAO_ARQUIVO_BAIXADO(15, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_FORMATADO(16, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoReguladaConsolidado(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoReguladaConsolidado> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoReguladaConsolidado::getIdUnico, Function.identity()));

    public static ParametrosArquivoReguladaConsolidado poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
