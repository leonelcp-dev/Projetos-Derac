package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoAgendamentosPendentesRegulada {
	
	INDICE_COLUNA_SOLICITADO_EM(0, 0, "Solicitado em:", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_FICHA(1, 1, "Ficha", "String", ""),
	INDICE_COLUNA_CODIGO_PACIENTE(2, 2, "Código Paciente", "String", ""),
	INDICE_COLUNA_PACIENTE(3, 3, "Código Paciente", "String", ""),
	INDICE_COLUNA_UNIDADE_SOLICITANTE(4, 4, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_NOME_FICHA(5, 5, "Nome Ficha", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE_EXAME(6, 6, "Especialidade/Exame", "String", ""),
	INDICE_COLUNA_HIPOTESE(7, 7, "Hipótese", "String", ""),

	ARQUIVO_FINAL_LINHA_INICIAL(8, 1,"Ajustado de acordo com o Java, no arquivo é a Linha 2", "", ""),
	ARQUIVO_BAIXADO_LINHA_INICIAL(9, 9,"Ajustado de acordo com o Java, no arquivo é a Linha 10", "", ""),
	
	LINHA_CABECALHO(10, 5, "Ajustado de acordo com o Java, no arquivo é a Linha 5", "", ""),
	NOME_PLANILHA_ARQUIVO_DOWNLOAD_CONSULTA(11, 8,"CONSULTA - AGENDAMENTOS PENDENT", "", ""),
	NOME_PLANILHA_ARQUIVO_DOWNLOAD_EXAME(12, 8,"EXAME - AGENDAMENTOS PENDENTES", "", ""),
	NOME_PLANILHA_ARQUIVO_FORMATADO(13, 8,"BD - CDR", "", ""),
	
	EXTENSAO_ARQUIVO_BAIXADO(14, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_FORMATADO(15, 0, "xlsx", "", ""),
	
	TEXTO_TIPO_ARQUIVO_CONSULTA(16, 0, "AGENDAMENTO CONSULTA", "", ""),
	TEXTO_TIPO_ARQUIVO_EXAME(17, 0, "AGENDAMENTO EXAME", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoAgendamentosPendentesRegulada(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoAgendamentosPendentesRegulada> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoAgendamentosPendentesRegulada::getIdUnico, Function.identity()));

    public static ParametrosArquivoAgendamentosPendentesRegulada poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
