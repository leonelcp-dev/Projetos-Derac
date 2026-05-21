package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoDemandaReprimidaCDR {
		
	INDICE_COLUNA_TIPO_DE_UNIDADE(0, 0, "Tipo de Unidade", "String", ""),
	INDICE_COLUNA_UNIDADE_DE_SAUDE(1, 1, "Unidade", "String", ""),
	INDICE_COLUNA_CODIGO(2, 2, "Código", "String", ""),
	INDICE_COLUNA_NOME_ABREVIADO(3, 3, "Nome Abreviado", "String", ""),
	INDICE_COLUNA_DATA_ENTRADA(4, 4, "Data Entrada", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_TEMPO_DE_ESPERA_EM_DIAS(5, 5, "Tempo de Espera em Dias", "Int", ""),
	INDICE_COLUNA_PROCEDIMENTO(6, 6, "Procedimento", "String", ""),
	INDICE_COLUNA_NOMENCLATURA_CORRETA(7, 7, "Nomenclatura Correta", "String", ""),
	INDICE_COLUNA_TIPO_DE_AGENDAMENTO(8, 8, "Tipo de Agendamento", "String", ""),
	INDICE_COLUNA_CID(9, 9, "Cid", "String", ""),
	INDICE_COLUNA_TIPO(10, 10, "Tipo", "String", ""),
	INDICE_COLUNA_IDADE_DO_PACIENTE(11, 11, "Idade do Paciente", "String", ""),
	INDICE_COLUNA_STATUS(12, 12, "Status", "String", ""),
	INDICE_COLUNA_PRIORIDADE(13, 13, "Prioridade", "String", ""),
	INDICE_COLUNA_OBSERVACAO(14, 14, "Observação", "String", ""),
	INDICE_COLUNA_OBSERVACAO_STATUS(15, 15, "Observação Status", "String", ""),
	INDICE_COLUNA_TELEFONE(16, 16, "Telefone", "String", ""),
	INDICE_COLUNA_MUNICIPIO(17, 17, "Município", "String", ""),
	
	LINHA_INICIAL_PLANILHA_CDR(18, 1,"Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	LINHA_INICIAL_TABELA_DINAMICA(19, 3,"Ajustado de acordo com o Java, no arquivo é a linha 4", "", ""),
	
	INDICE_COLUNA_CONSOLIDADO_ESPECIALIDADE(20, 0, "Especialidade", "String", ""),
	INDICE_COLUNA_CONSOLIDADO_CONTAGEM_ESPECIALIDADE(21, 1, "Contagem Especialidade", "Int", ""),
	INDICE_COLUNA_CONSOLIDADO_MAX_TEMPO_ESPERA(22, 2, "Max Tempo Espera", "Int", ""),

	NOME_PLANILHA_CDR(23, 0, "CDR", "", ""),
	NOME_PLANILHA_DINAMICA_CDR(24, 0, "DINÂMICA CDR", "", ""),
	NOME_PLANILHA_CONSOLIDADO_CDR(25, 0, "CONSOLIDADO CDR", "", ""),
	
	AREA_PARA_TABELA_DINAMICA_CDR(26, 0, "A1:R", "", ""),
	
	TEXTO_TOTAL_GERAL(27, 0, "Total Geral", "", ""),
	TEXTO_ROTULOS_DE_LINHA(28, 0, "Rótulos de Linha", "", ""),
	
	EXTENSAO_ARQUIVO(29, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoDemandaReprimidaCDR(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoDemandaReprimidaCDR> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoDemandaReprimidaCDR::getIdUnico, Function.identity()));

    public static ParametrosArquivoDemandaReprimidaCDR poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
