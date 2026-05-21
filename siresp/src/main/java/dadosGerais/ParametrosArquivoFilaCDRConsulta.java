package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoFilaCDRConsulta {
		
	INDICE_COLUNA_CODIGO(0, 0, "Código", "String", ""),
	INDICE_COLUNA_NOME(1, 1, "Nome", "String", ""),
	INDICE_COLUNA_TELEFONE(2, 2, "Telefone", "String", ""),
	INDICE_COLUNA_MUNICIPIO(3, 3, "Município", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(4, 4, "Especialidade", "String", ""),
	INDICE_COLUNA_CID(5, 5, "Cid", "String", ""),
	INDICE_COLUNA_TIPO_CONSULTA(6, 6, "Tipo Consulta", "String", ""),
	INDICE_COLUNA_PROFISSIONAL(7, 7, "Profissional", "String", ""),
	INDICE_COLUNA_IDADE_DO_PACIENTE(8, 8, "Idade do Paciente", "String", ""),
	INDICE_COLUNA_MES_ANO_PRETENDIDO(9, 9, "Mês/Ano Pretendido", "String", ""),
	INDICE_COLUNA_TURNO(10, 10, "Turno", "String", ""),
	INDICE_COLUNA_DATA_AGENDA(11, 11, "Data Agenda", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_HORARIO_AGENDA(12, 12, "Horário", "Time", "HH:mm:ss"),
	INDICE_COLUNA_DATA_ENTRADA(13, 13, "Data Entrada", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_STATUS(14, 14, "Status", "String", ""),
	INDICE_COLUNA_FILIPETA(15, 15, "Filipeta", "String", ""),
	INDICE_COLUNA_RET_FILIPETA(16, 16, "Ret. Filipeta", "String", ""),
	INDICE_COLUNA_PRIORIDADE(17, 17, "Prioridade", "String", ""),
	INDICE_COLUNA_ACEITA_TELECONSULTA(18, 18, "Aceita Teleconsulta", "String", ""),
	INDICE_COLUNA_OBSERVACAO(19, 19, "Observação", "String", ""),
	INDICE_COLUNA_OBSERVACAO_STATUS(20, 20, "Observação Status", "String", ""),
	INDICE_COLUNA_ALTERACAO_ESPECIALIDADE_EXAME_DE(21, 21, "Alteração Especialidade/Exame - De", "String", ""),
	INDICE_COLUNA_ALTERACAO_ESPECIALIDADE_EXAME_PARA(22, 22, "Para", "String", ""),
	INDICE_COLUNA_OBSERVACAO_ALTERACAO_ESPECIALIDADE_EXAME(23, 23, "Observação", "String", ""),
	INDICE_COLUNA_USUARIO_ALTERACAO_ESPECIALIDADE_EXAME(24, 24, "Usuário", "String", ""),
	INDICE_COLUNA_DATA_DE_ALTERACAO_ESPECIALIDADE_EXAME(25, 25, "Data de alteração", "String", ""),
	INDICE_COLUNA_ALTERACAO_CID_DE(26, 26, "Alteração CID - De", "String", ""),
	INDICE_COLUNA_ALTERACAO_CID_PARA(27, 27, "Para", "String", ""),
	INDICE_COLUNA_OBSERVACAO_ALTERACAO_CID(28, 28, "Observação", "String", ""),
	INDICE_COLUNA_USUARIO_ALTERACAO_CID(29, 29, "Usuário", "String", ""),
	INDICE_COLUNA_DATA_DE_ALTERACAO_CID(30, 30, "Data de alteração", "String", ""),
	
	LINHA_INICIAL_PLANILHA(31, 1,"Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO(32, 0, "csv", "", ""),
	FORMATO_DATA_CSV(33, 0, "yyyy-MM-dd", "", ""),
	FORMATO_DATA_HORA_CSV(34, 0, "yyyy-MM-dd HH:mm:ss", "", ""),
	FORMATO_HORA_CSV(35, 0, "HH:mm:ss", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoFilaCDRConsulta(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoFilaCDRConsulta> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoFilaCDRConsulta::getIdUnico, Function.identity()));

    public static ParametrosArquivoFilaCDRConsulta poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
