package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas {
		
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
	INDICE_COLUNA_DATA_AGENDA(12, 12, "Data Entrada", "Date", "yyyy-MM-dd"),
	INDICE_COLUNA_HORARIO(13, 13, "Horário", "Time", "HH:mm:ss"),
	INDICE_COLUNA_STATUS(14, 14, "Status", "String", ""),
	INDICE_COLUNA_PRIORIDADE(15, 15, "Prioridade", "String", ""),
	INDICE_COLUNA_OBSERVACAO(16, 16, "Observação", "String", ""),
	INDICE_COLUNA_OBSERVACAO_STATUS(17, 17, "Observação Status", "String", ""),
	INDICE_COLUNA_TELEFONE(18, 18, "Telefone", "String", ""),
	INDICE_COLUNA_MUNICIPIO(19, 19, "Município", "String", ""),
	
	LINHA_INICIAL_PLANILHA_CDR(20, 1,"Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	LINHA_INICIAL_TABELA_DINAMICA(21, 3,"Ajustado de acordo com o Java, no arquivo é a linha 4", "", ""),
	
	INDICE_COLUNA_CONSOLIDADO_ESPECIALIDADE(22, 0, "Especialidade", "String", ""),
	INDICE_COLUNA_CONSOLIDADO_CONTAGEM_ESPECIALIDADE(23, 1, "Contagem Especialidade", "Int", ""),
	INDICE_COLUNA_CONSOLIDADO_MAX_TEMPO_ESPERA(24, 2, "Max Tempo Espera", "Int", ""),

	NOME_PLANILHA_FILIPETAS_NAO_IMPRESSAS(25, 0, "FILIPETAS NÃO IMPRESSAS", "", ""),
	
	TEXTO_STATUS_AGENDADO(28, 0, "Agendado", "", ""),
	TEXTO_ROTULOS_DE_LINHA(29, 0, "Rótulos de Linha", "", ""),
	
	EXTENSAO_ARQUIVO(30, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas::getIdUnico, Function.identity()));

    public static ParametrosArquivoDemandaReprimidaFilipetasNaoImpressas poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
