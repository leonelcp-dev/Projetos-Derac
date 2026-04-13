package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoDistribuicaoFilaCentralReg {
		
	INDICE_COLUNA_TIPO(0, 0, "Tipo", "String", ""),
	INDICE_COLUNA_CODIGO(1, 1, "Código", "String", ""),
	INDICE_COLUNA_NOME(2, 2, "Nome", "String", ""),
	INDICE_COLUNA_TELEFONE(3, 3, "Telefone", "String", ""),
	INDICE_COLUNA_MUNICIPIO(4, 4, "Município", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(5, 5, "Especialidade", "String", ""),
	INDICE_COLUNA_CID(6, 6, "CID", "String", ""),
	INDICE_COLUNA_TIPO_CONSULTA(7, 7, "Tipo Consulta", "String", ""),
	INDICE_COLUNA_PROFISSIONAL(8, 8, "Profissional", "String", ""),
	INDICE_COLUNA_IDADE_DO_PACIENTE(9, 9, "Idade do Paciente", "String", ""),
	INDICE_COLUNA_MES_ANO_PRETENDIDO(10, 10, "Mes/Ano Pretendido", "String", ""),
	INDICE_COLUNA_TURNO(11, 11, "Turno", "String", ""),
	INDICE_COLUNA_DATA_AGENDA(12, 12, "Data Agenda", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_HORARIO(13, 13, "Horário", "Time", "HH:mm"),
	INDICE_COLUNA_DATA_ENTRADA(14, 14, "Data Entrada", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_STATUS(15, 15, "Status", "String", ""),
	INDICE_COLUNA_FILIPETA(16, 16, "Filipeta", "String", ""),
	INDICE_COLUNA_RET_FILIPETA(17, 17, "Filipeta", "String", ""),
	INDICE_COLUNA_PRIORIDADE(18, 18, "Prioridade", "String", ""),
	INDICE_COLUNA_ACEITA_TELECONSULTA(19, 19, "Aceita Teleconsulta", "String", ""),
	INDICE_COLUNA_OBSERVACAO(20, 20, "Observação", "String", ""),
	INDICE_COLUNA_OBSERVACAO_STATUS(21, 21, "Observação Status", "String", ""),
	INDICE_COLUNA_ALTERACAO_ESPECIALIDADE_EXAME_DE(22, 22, "Alteração Especialidade/Exame - De", "String", ""),
	INDICE_COLUNA_ALTERACAO_ESPECIALIDADE_EXAME_PARA(23, 23, "Alteração Especialidade/Exame - Para", "String", ""),
	INDICE_COLUNA_OBSERVACAO_2(24, 24, "Observação", "String", ""),
	INDICE_COLUNA_USUARIO_2(25, 25, "Usuário", "String", ""),
	INDICE_COLUNA_DATA_DE_ALTERACAO(26, 26, "Data de Alteração", "String", ""),
	INDICE_COLUNA_ALTERACAO_CID_DE(27, 27, "Alteração CID - De", "String", ""),
	INDICE_COLUNA_ALTERACAO_CID_PARA(28, 28, "Alteração CID - Para", "String", ""),
	INDICE_COLUNA_OBSERVACAO_3(29, 29, "Observação 3", "String", ""),
	INDICE_COLUNA_USUARIO_3(30, 30, "Usuário", "String", ""),
	INDICE_COLUNA_DATA_DE_ALTERACAO_3(31, 31, "Data de Alteração 3", "String", ""),
	INDICE_COLUNA_OBSERVACAO_AUTOMATIZACAO(32, 32, "Observação Automatização", "String", ""),
	
	LINHA_INICIAL_ARQUIVO_UNIDADE(33, 1,"Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO(34, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoDistribuicaoFilaCentralReg(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoDistribuicaoFilaCentralReg> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoDistribuicaoFilaCentralReg::getIdUnico, Function.identity()));

    public static ParametrosArquivoDistribuicaoFilaCentralReg poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
