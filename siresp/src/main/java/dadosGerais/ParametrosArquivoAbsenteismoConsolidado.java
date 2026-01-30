package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoAbsenteismoConsolidado {
		
	INDICE_COLUNA_TIPO(0, 1, "Tipo", "String", ""),
	INDICE_COLUNA_DATA_AGENDA(1, 2, "Data Agenda", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_HORA_AGENDA(2, 3, "Hora", "DateTime", "hh:mm"),
	INDICE_COLUNA_ESPECIALIDADE(3, 4, "Especialidade", "String", ""),
	INDICE_COLUNA_CODIGO(4, 5, "Código", "Int", ""),
	INDICE_COLUNA_USUARIO(5, 6, "Usuário", "String", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(6, 7, "Nasc.", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_TELEFONE(7, 8, "Telefone", "String", ""),
	INDICE_COLUNA_ATENDIDO(8, 9, "Atendido Presencial", "String", ""),
	INDICE_COLUNA_AUSENTE(9, 10, "Aus", "String", ""),
	INDICE_COLUNA_DISPENSADO(10, 11, "Disp", "String", ""),
	INDICE_COLUNA_DESISTENCIA(11, 12, "Des", "String", ""),
	INDICE_COLUNA_EXECUTANTE(12, 13, "Executante", "String", ""),
	LINHA_INICIAL_ARQUIVO_CONSOLIDADO(13, 18,"Ajustado de acordo com o Java, no arquivo é a linha 19", "", ""),
	COLUNA_INICIAL_ARQUIVO_CONSOLIDADO(14, 1,"Ajustado de acordo com o Java, no arquivo é a coluna B (2)", "", ""),
	LINHA_MES_DE_REFERENCIA(15, 8,"Ajustado de acordo com o Java, no arquivo é a linha 9", "", ""),
	COLUNA_MES_DE_REFERENCIA(16, 4,"Ajustado de acordo com o Java, no arquivo é a coluna E (5)", "", ""),
	LINHA_TEXTO_MES_DE_REFERENCIA(17, 16,"Ajustado de acordo com o Java, no arquivo é a linha 17", "", ""),
	COLUNA_TEXTO_MES_DE_REFERENCIA(18, 2,"Ajustado de acordo com o Java, no arquivo é a coluna C (3)", "", ""),
	EXTENSAO_ARQUIVO_ABSENTEISMO(19, 0, "xlsx", "", "");

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
