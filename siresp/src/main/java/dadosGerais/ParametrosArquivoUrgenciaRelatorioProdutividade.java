package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoUrgenciaRelatorioProdutividade {
		
	
	INDICE_COLUNA_NUMERO_FICHA(0, 0, "N° ficha", "String", ""),
	INDICE_COLUNA_TIPO_DE_FICHA(1, 1, "Tipo de Ficha", "String", ""),
	INDICE_COLUNA_TIPO_SOLICITACAO(2, 2, "Tipo Solicitação", "String", ""),
	INDICE_COLUNA_CLASSIFICACAO_DE_RISCO(3, 3, "Classificação de Risco", "String", ""),
	INDICE_COLUNA_DATA_HORA_SOLICITACAO(4, 4,"Data/Hora Solicitação", "Date/Time", "dd/MM/yyyy HH:mm"),
	INDICE_COLUNA_DATA_HORA_FINALIZACAO(5, 5, "Data/Hora Finalização", "Date/Time", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_TEMPO_RESOLUCAO(6, 6, "Tempo Resolução", "String", ""),
	INDICE_COLUNA_JUDICIAL(7, 7, "Judicial", "String", ""),
	INDICE_COLUNA_MUNICIPIO_PACIENTE(8, 8, "Município Paciente", "String", ""),
	INDICE_COLUNA_CODIGO_PACIENTE(9, 9, "Código Paciente", "String", ""),
	INDICE_COLUNA_CNS(10, 10, "CNS", "String", ""),
	INDICE_COLUNA_NOME_DO_PACIENTE(11, 11, "Nome do Paciente", "String", ""),
	INDICE_COLUNA_SEXO(12, 12, "Sexo", "String", ""),
	INDICE_COLUNA_IDADE(13, 13, "Idade", "int", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(14, 14, "Data Nascimento", "String", ""),
	INDICE_COLUNA_TARM_PAD(15, 15, "TARM/PAD", "String", ""),
	INDICE_COLUNA_REGULADOR_INICIAL(16, 16, "Regulador Inicial", "String", ""),
	INDICE_COLUNA_REGULADOR_FINAl(17, 17, "Regulador Final", "String", ""),
	INDICE_COLUNA_TRANSP(18, 18, "Transp.", "String", ""),
	INDICE_COLUNA_FORMA_DE_RESOLUÇÃO(19, 19, "Forma de Resolução", "String", ""),
	INDICE_COLUNA_MEDICO_RECEPTOR(20, 20, "Medico Receptor", "String", ""),
	INDICE_COLUNA_CID_1(21, 21, "CID 1", "String", ""),
	INDICE_COLUNA_DESCRICAO_CID_1(22, 22, "Descrição CID 1", "String", ""),
	INDICE_COLUNA_CID_2(23, 23, "CID 2", "String", ""),
	INDICE_COLUNA_DESCRICAO_CID_2(24, 24, "Descrição CID 2", "String", ""),
	INDICE_COLUNA_TIPO_DE_RECURSO_SOLICITADO_1(25, 25, "Tipo de Recurso Solicitado 1", "String", ""),
	INDICE_COLUNA_RECURSO_SOLICITADO_1(26, 26, "Recurso Solicitado 1", "String", ""),
	INDICE_COLUNA_RRAS_SOLICITANTE(27, 27, "RRAS", "String", ""),
	INDICE_COLUNA_COMPLEXO_SOLICITANTE(28, 28, "Complexo Solicitante", "String", ""),
	INDICE_COLUNA_DRS_SOLICITANTE(29, 29, "DRS Solicitante", "String", ""),
	INDICE_COLUNA_REGIAO_DE_SAUDE_SOLICITANTE(30, 30, "Região de Saúde Solicitante", "String", ""),
	INDICE_COLUNA_MUNICIPIO_SOLICITANTE(31, 31, "Municipio Solicitante", "String", ""),
	INDICE_COLUNA_CNES_SOLICITANTE(32, 32, "CNES Solicitante", "String", ""),
	INDICE_COLUNA_UNIDADE_SOLICITANTE(33, 33, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_RAAS_EXECUTANTE(34, 34, "RAAS", "String", ""),
	INDICE_COLUNA_COMPLEXO_EXECUTANTE(35, 35, "Complexo Executante", "String", ""),
	INDICE_COLUNA_DRS_EXECUTANTE(36, 36, "DRS Executante", "String", ""),
	INDICE_COLUNA_REGIAO_DE_SAUDE_EXECUTANTE(37, 37, "Região de Saúde Executante", "String", ""),
	INDICE_COLUNA_MUNICIPIO_EXECUTANTE(38, 38, "Municipio Executante", "String", ""),
	INDICE_COLUNA_CNES_EXECUTANTE(39, 39, "CNES Executante", "String", ""),
	INDICE_COLUNA_UNIDADE_EXECUTANTE(40, 40, "Unidade Executante", "String", ""),
	INDICE_COLUNA_TIPO_DE_FECHAMENTO(41, 41, "Tipo de Fechamento", "String", ""),
	INDICE_COLUNA_LOCAL_REGULACAO(42, 42, "Local Regulação", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(43, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO_RELATORIO_BAIXADO(44, 0, "csv", "", ""),
	
	TEXTO_CENTRAL_MUNICIPAL_REGULACAO_CAMPINAS(45, 0, "CENTRAL REGULAÇÃO MUNICIPAL - CAMPINAS", "", ""),
	TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_PARA_REFERENCIA_PACTUADA(46, 0, "ENCAMINHADO PARA REFERÊNCIA PACTUADA", "", ""),
	TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_PARA_AVALIACAO_NA_REFERENCIA_DE_COMPLEXIDADE_ADEQUADA(47, 0, "ENCAMINHADO PARA AVALIAÇÃO NA REFERÊNCIA DE COMPLEXIDADE ADEQUADA", "", ""),
	TEXTO_FORMA_RESOLUCAO_ENCAMINHADO_AUTOMATICAMENTE_PAR_REFERENCIA_PACTUADA(48, 0, "ENCAMINHADO AUTOMATICAMENTE PARA REFERÊNCIA PACTUADA", "", ""),
	TEXTO_FORMA_RESOLUCAO_VAGA_ZERO(49, 0, "VAGA ZERO", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoUrgenciaRelatorioProdutividade(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoUrgenciaRelatorioProdutividade> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoUrgenciaRelatorioProdutividade::getIdUnico, Function.identity()));

    public static ParametrosArquivoUrgenciaRelatorioProdutividade poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
