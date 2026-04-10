package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaCDRResultadosConsulta {
		
	INDICE_COLUNA_CHECK_LIST(0, 0, "", "String", ""),
	INDICE_COLUNA_ACAO(1, 1, "", "String", ""),
	INDICE_COLUNA_CODIGO(2, 2, "Código", "String", ""),
	INDICE_COLUNA_NOME(3, 3, "Nome", "String", ""),
	INDICE_COLUNA_TELEFONE(4, 4, "Telefone", "String", ""),
	INDICE_COLUNA_MUNICIPIO(5, 5, "Município", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(6, 6, "Especialidade", "String", ""),
	INDICE_COLUNA_CID(7, 7, "CID", "String", ""),
	INDICE_COLUNA_TIPO_CONSULTA(8, 8, "Tipo Consulta", "String", ""),
	INDICE_COLUNA_PROFISSIONAL(9, 9, "Profissional", "String", ""),
	INDICE_COLUNA_IDADE_DO_PACIENTE(10, 10, "Idade do Paciente", "String", ""),
	INDICE_COLUNA_MES_ANO_PRETENDIDO(11, 11, "Mês/Ano Pretendido", "String", ""),
	INDICE_COLUNA_TURNO(12, 12, "Turno", "String", ""),
	INDICE_COLUNA_DATA_AGENDA(13, 13, "Data agenda", "String", ""),
	INDICE_COLUNA_HORARIO(14, 14, "Horário", "String", ""),
	INDICE_COLUNA_DATA_ENTRADA(15, 15, "Data Entrada", "String", ""),
	INDICE_COLUNA_STATUS(16, 16, "Status", "String", ""),
	INDICE_COLUNA_FILIPETA(17, 17, "Filipeta", "String", ""),
	INDICE_COLUNA_RET_FILIPETA(18, 18, "Ret. Filipeta", "String", ""),
	INDICE_COLUNA_PRIORIDADE(19, 19, "Prioridade", "String", ""),
	INDICE_COLUNA_ACEITA_TELECONSULTA(20, 20, "Prioridade", "String", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(21, 21, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(22, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(23, 0, "xls", "", "");
	
	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaCDRResultadosConsulta(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaCDRResultadosConsulta> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaCDRResultadosConsulta::getIdUnico, Function.identity()));

    public static ParametrosTabelaCDRResultadosConsulta poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
