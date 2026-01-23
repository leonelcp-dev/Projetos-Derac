package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoCenso {
		
	INDICE_COLUNA_PACIENTE(0, 0, "Paciente", "String", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(1, 1, "Data Nascimento", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_CNS(2, 2, "CNS", "Int", ""),
	INDICE_COLUNA_DATA_DE_INTERNACAO(3, 3, "Data de Internação", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_DESCRICAO_ENFERMARIA(4, 4, "Descrição Enfermaria", "String", ""),
	INDICE_COLUNA_DESCRICAO_LEITO(5, 5, "Descrição Leito", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(6, 6, "Especialidade", "String", ""),
	INDICE_COLUNA_SEXO(7, 7, "Sexo", "String", ""),
	INDICE_COLUNA_STATUS(8, 8, "Status", "String", ""),
	INDICE_COLUNA_SITUACAO(9, 9, "Situação", "String", ""),
	INDICE_COLUNA_CID_1(10, 10, "CID 1", "String", ""),
	INDICE_COLUNA_CID_2(11, 11, "CID 2", "String", ""),
	INDICE_COLUNA_BLOQUEADO(12, 12, "Bloqueado", "String", ""),
	INDICE_COLUNA_MOTIVO_DO_BLOQUEIO(13, 13, "Motivo do Bloqueio", "String", ""),
	INDICE_COLUNA_JUSTIFICATIVA_DO_BLOQUEIO(14, 14, "Justificativa do Bloqueio", "String", ""),
	INDICE_COLUNA_MUNICIPIO_DE_ORIGEM_DO_PACIENTE(15, 15, "Município de Origem do Paciente", "String", ""),
	INDICE_COLUNA_ANALISE_DO_DERAC(16, 16, "Análise do DERAC", "String", ""),
	INDICE_COLUNA_DATA_RELATORIO(17, 17, "Data Relatorio", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_TIPO_DE_LEITO_3(18, 18, "Tipo de Leito 3", "Formula", ""),
	INDICE_COLUNA_MOTIVO_DO_BLOQUEIO_REVISTO(19, 19, "Motivo do Bloqueio Revisto", "Formula", ""),
	INDICE_COLUNA_UNIDADE(20, 20, "Unidade", "String", ""),
	INDICE_COLUNA_CONCATENA_1(21, 21, "Concatena 1", "Formula", ""),
	INDICE_COLUNA_DIA(22, 22, "Dia", "Formula", ""),
	INDICE_COLUNA_TIPO_DE_LEITO_1(23, 23, "Tipo de Leito 1", "Formula", ""),
	INDICE_COLUNA_CONCATENA_2(24, 24, "Concatena 2", "Formula", ""),
	INDICE_COLUNA_TIPO_DE_LEITO_2(25, 25, "Tipo de Leito 2", "Formula", ""),
	TEXTO_CONFIRMA_BLOQUEIO(26, -1, "SIM", "", ""),
	LINHA_INICIAL_ARQUIVO_SIRESP(27, 8,"Ajustado de acordo com o Java, no arquivo é a linha 9", "", ""),
	LINHA_INICIAL_ARQUIVO_CENSO(28, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
//	COLUNA_DATA_HORA_RELATORIO_CENSO_DIARIO_SEM_FORMATACAO(29, 14, "Ajustado de acordo com o Java, no arquivo é a coluna 15", "", ""),
//	LINHA_DATA_HORA_RELATORIO_CENSO_DIARIO_SEM_FORMATACAO(30, 0, "Ajustado de acordo com o Java, no arquivo é a linha 1", "", ""),
	COLUNA_DATA_HORA_RELATORIO_CENSO_DIARIO_FORMATADO(31, 8, "Ajustado de acordo com o Java, no arquivo é a coluna 9", "", ""),
	LINHA_DATA_HORA_RELATORIO_CENSO_DIARIO_FORMATADO(32, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	NOME_PLANILHA_CENSO(33, 0, "CENSOS", "", ""),
	EXTENSAO_ARQUIVO_CENSO(34, 0, "xls", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoCenso(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoCenso> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoCenso::getIdUnico, Function.identity()));

    public static ParametrosArquivoCenso poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
