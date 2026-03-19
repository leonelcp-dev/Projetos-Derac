package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoAbsenteismoConsolidado {
		
	INDICE_COLUNA_TIPO(0, 2, "Tipo", "String", ""),
	INDICE_COLUNA_DATA_AGENDA(1, 3, "Data Agenda", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_HORA_AGENDA(2, 4, "Hora", "Time", "HH:mm"),
	INDICE_COLUNA_ESPECIALIDADE(3, 5, "Especialidade", "String", ""),
	INDICE_COLUNA_CODIGO(4, 6, "Código", "Int", ""),
	//INDICE_COLUNA_USUARIO(5, 6, "Usuário", "String", ""),
	//INDICE_COLUNA_DATA_NASCIMENTO(6, 7, "Nasc.", "Date", "dd/MM/yyyy"),
	//INDICE_COLUNA_TELEFONE(7, 8, "Telefone", "String", ""),
	INDICE_COLUNA_ATENDIDO(8, 7, "Atendido Presencial", "String", ""),
	INDICE_COLUNA_AUSENTE(9, 8, "Aus", "String", ""),
	INDICE_COLUNA_DISPENSADO(10, 9, "Disp", "String", ""),
	INDICE_COLUNA_DESISTENCIA(11, 10, "Des", "String", ""),
	INDICE_COLUNA_EXECUTANTE(12, 11, "Executante", "String", ""),
	LINHA_INICIAL_ARQUIVO_CONSOLIDADO(13, 18,"Ajustado de acordo com o Java, no arquivo é a linha 19", "", ""),
	COLUNA_INICIAL_ARQUIVO_CONSOLIDADO(14, 1,"Ajustado de acordo com o Java, no arquivo é a coluna B (2)", "", ""),
	LINHA_MES_DE_REFERENCIA(15, 8,"Ajustado de acordo com o Java, no arquivo é a linha 9", "", ""),
	COLUNA_MES_DE_REFERENCIA(16, 4,"Ajustado de acordo com o Java, no arquivo é a coluna E (5)", "", ""),
	LINHA_TEXTO_MES_DE_REFERENCIA(17, 16,"Ajustado de acordo com o Java, no arquivo é a linha 17", "", ""),
	COLUNA_TEXTO_MES_DE_REFERENCIA(18, 2,"Ajustado de acordo com o Java, no arquivo é a coluna C (3)", "", ""),
	LINHA_NOME_UNIDADE(19, 8,"Ajustado de acordo com o Java, no arquivo é a linha 9", "", ""),
	COLUNA_NOME_UNIDADE(20, 7,"Ajustado de acordo com o Java, no arquivo é a coluna H (8)", "", ""),
	NOME_PLANILHA_GRAFICO(21, 8, "Série Histórica", "", ""),
	INDICE_DESENHO_GRAFICO_PLANILHA(22, 0, "Índice de Figuras Gráficas da planilha Série Histórica", "", ""),
	LINHA_TOTAL_CONSULTAS_AGENDADAS(23, 12,"Ajustado de acordo com o Java, no arquivo é a linha 13", "", ""),
	COLUNA_TOTAL_CONSULTAS_AGENDADAS(24, 4,"Ajustado de acordo com o Java, no arquivo é a coluna E (5)", "", ""),
	LINHA_TOTAL_FALTAS_CONSULTAS(25, 13,"Ajustado de acordo com o Java, no arquivo é a linha 14", "", ""),
	COLUNA_TOTAL_FALTAS_CONSULTAS(26, 4,"Ajustado de acordo com o Java, no arquivo é a coluna E (5)", "", ""),
	LINHA_TOTAL_EXAMES_AGENDADOS(27, 12,"Ajustado de acordo com o Java, no arquivo é a linha 13", "", ""),
	COLUNA_TOTAL_EXAMES_AGENDADOS(28, 6,"Ajustado de acordo com o Java, no arquivo é a coluna G (7)", "", ""),
	LINHA_TOTAL_FALTAS_EXAMES(29, 13,"Ajustado de acordo com o Java, no arquivo é a linha 14", "", ""),
	COLUNA_TOTAL_FALTAS_EXAMES(30, 6,"Ajustado de acordo com o Java, no arquivo é a coluna G (7)", "", ""),
	LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS(31, 15,"Ajustado de acordo com o Java, no arquivo é a linha 16", "", ""),
	COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_CONSULTAS(32, 4,"Ajustado de acordo com o Java, no arquivo é a coluna E (5)", "", ""),
	LINHA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES(33, 15,"Ajustado de acordo com o Java, no arquivo é a linha 16", "", ""),
	COLUNA_MEDIA_MUNICIPAL_ABSENTEISMO_EXAMES(34, 6,"Ajustado de acordo com o Java, no arquivo é a coluna G (7)", "", ""),

	TEXTO_DINAMICO_PARA_SUBSTITUICAO(35, 0, "####", "", ""),
	
	ARQUIVO_MUNICIPAL_NOME(36, 6,"01 - COMPILADO MUNICIPAL " + TEXTO_DINAMICO_PARA_SUBSTITUICAO.getDescricao() + ".xlsx", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_UNIDADE(37, 1,"Ajustado de acordo com o Java, no arquivo é a coluna B (2)", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_CONSULTA_UNIDADE(38, 4,"Ajustado de acordo com o Java, no arquivo é a coluna E (5)", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_EXAME_UNIDADE(39, 5,"Ajustado de acordo com o Java, no arquivo é a coluna F (6)", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_CONSULTA_MUNICIPAL(40, 6,"Ajustado de acordo com o Java, no arquivo é a coluna G (7)", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_ABSENTEISMO_EXAME_MUNICIPAL(41, 7,"Ajustado de acordo com o Java, no arquivo é a coluna H (8)", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_DISTRITO(42, 2,"Ajustado de acordo com o Java, no arquivo é a coluna C (3)", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_MES_REFERENCIA(43, 3,"Ajustado de acordo com o Java, no arquivo é a coluna D (4)", "", ""),
	
	ARQUIVO_MUNICIPAL_LINHA_INICIAL_UNIDADES(44, 8,"Ajustado de acordo com o Java, no arquivo é a Linha 9", "", ""),
	
	NOME_PLANILHA_MMA(45, 8,"MMA", "", ""),
	ARQUIVO_MUNICIPAL_LINHA_INICIAL_PLANILHA_MMA(46, 4,"Ajustado de acordo com o Java, no arquivo é a Linha 5", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_CONSULTAS_PLANILHA_MMA(47, 2,"Ajustado de acordo com o Java, no arquivo é a coluna C (3)", "", ""),
	ARQUIVO_MUNICIPAL_COLUNA_EXAMES_PLANILHA_MMA(48, 3,"Ajustado de acordo coxm o Java, no arquivo é a coluna D (4)", "", ""),
	
	EXTENSAO_ARQUIVO_ABSENTEISMO(49, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoAbsenteismoConsolidado(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoAbsenteismoConsolidado> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoAbsenteismoConsolidado::getIdUnico, Function.identity()));

    public static ParametrosArquivoAbsenteismoConsolidado poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
