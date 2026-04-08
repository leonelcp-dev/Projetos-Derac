package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoSolicitacoesPendentesRegulada {
	
	INDICE_COLUNA_SOLICITADO_EM(0, 0, "Solicitado em:", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_MUNICIPIO(1, 1, "Município", "String", ""),
	INDICE_COLUNA_FICHA(2, 2, "Ficha", "String", ""),
	INDICE_COLUNA_CODIGO_PACIENTE(3, 3, "Código Paciente", "String", ""),
	INDICE_COLUNA_PACIENTE(4, 4, "Paciente", "String", ""),
	INDICE_COLUNA_UNIDADE_SOLICITANTE(5, 5, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_NOME_FICHA(6, 6, "Nome Ficha", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE_EXAME(7, 7, "Especialidade/Exame", "String", ""),
	INDICE_COLUNA_ASSUMIDO_EM(8, 8, "Assumido em:", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_HIPOTESE(9, 9, "Hipótese", "String", ""),

	ARQUIVO_FINAL_LINHA_INICIAL(10, 1,"Ajustado de acordo com o Java, no arquivo é a Linha 2", "", ""),
	ARQUIVO_BAIXADO_LINHA_INICIAL(11, 9,"Ajustado de acordo com o Java, no arquivo é a Linha 10", "", ""),
	
	NOME_PLANILHA_ARQUIVO_DOWNLOAD_CONSULTA(12, 8,"CONSULTA - SOLICITACOES PENDENT", "", ""),
	NOME_PLANILHA_ARQUIVO_DOWNLOAD_EXAME(13, 8,"EXAME - SOLICITACOES PENDENTES", "", ""),
	NOME_PLANILHA_ARQUIVO_FORMATADO(14, 8,"BD - CDR", "", ""),
	
	EXTENSAO_ARQUIVO_BAIXADO(15, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_FORMATADO(16, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoSolicitacoesPendentesRegulada(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoSolicitacoesPendentesRegulada> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoSolicitacoesPendentesRegulada::getIdUnico, Function.identity()));

    public static ParametrosArquivoSolicitacoesPendentesRegulada poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
