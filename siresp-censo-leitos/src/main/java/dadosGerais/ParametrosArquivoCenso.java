package dadosGerais;

public enum ParametrosArquivoCenso {
		
	INDICE_COLUNA_PACIENTE(0, "Paciente"),
	INDICE_COLUNA_DATA_NASCIMENTO(1, "Data Nascimento"),
	INDICE_COLUNA_CNS(2, "CNS"),
	INDICE_COLUNA_DATA_DE_INTERNACAO(3, "Data de Internação"),
	INDICE_COLUNA_DESCRICAO_ENFERMARIA(4, "Descrição Enfermaria"),
	INDICE_COLUNA_DESCRICAO_LEITO(5, "Descrição Leito"),
	INDICE_COLUNA_ESPECIALIDADE(6, "Especialidade"),
	INDICE_COLUNA_SEXO(7, "Sexo"),
	INDICE_COLUNA_STATUS(8, "Status"),
	INDICE_COLUNA_SITUACAO(9, "Situação"),
	INDICE_COLUNA_CID_1(10, "CID 1"),
	INDICE_COLUNA_CID_2(11, "CID 2"),
	INDICE_COLUNA_BLOQUEADO(12, "Bloqueado"),
	INDICE_COLUNA_MOTIVO_DO_BLOQUEIO(13, "Motivo do Bloqueio"),
	INDICE_COLUNA_JUSTIFICATIVA_DO_BLOQUEIO(14, "Justificativa do Bloqueio"),
	INDICE_COLUNA_MUNICIPIO_DE_ORIGEM_DO_PACIENTE(15, "Município de Origem do Paciente"),
	INDICE_COLUNA_ANALISE_DO_DERAC(16, "Análise do DERAC"),
	INDICE_COLUNA_DATA_RELATORIO(17, "Data Relatorio"),
	INDICE_COLUNA_TIPO_DE_LEITO_3(18, "Tipo de Leito 3"),
	INDICE_COLUNA_MOTIVO_DO_BLOQUEIO_REVISTO(19, "Motivo do Bloqueio Revisto"),
	INDICE_COLUNA_UNIDADE(20, "Unidade"),
	INDICE_COLUNA_CONCATENA_1(21, "Concatena 1"),
	INDICE_COLUNA_DIA(22, "Dia"),
	INDICE_COLUNA_TIPO_DE_LEITO_1(23, "Tipo de Leito 1"),
	INDICE_COLUNA_CONCATENA_2(24, "Concatena 2"),
	INDICE_COLUNA_TIPO_DE_LEITO_2(25, "Tipo de Leito 2"),
	TEXTO_CONFIRMA_BLOQUEIO(-1, "SIM"),
	LINHA_INICIAL_ARQUIVO(8,"Ajustado de acordo com o Java, no arquivo é a linha 9");

	private int indice;
	private String descricao;
			
	ParametrosArquivoCenso(int indice, String descricao)
	{
		this.setIndice(indice);
		this.setDescricao(descricao);
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
	
}
