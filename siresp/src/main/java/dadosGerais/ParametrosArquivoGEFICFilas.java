package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoGEFICFilas {
		
	
	INDICE_COLUNA_POSICAO(0, 0, "Posição", "Integer", ""),
	INDICE_COLUNA_PACIENTE(1, 1, "Paciente", "String", ""),
	INDICE_COLUNA_CPF(2, 2, "CPF", "String", ""),
	INDICE_COLUNA_TELEFONE(3, 3, "Telefone", "Int", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(4, 4, "Data nascimento", "Date", "MMM/yyyy"),
	INDICE_COLUNA_IDADE(5, 5, "Idade", "String", ""),
	INDICE_COLUNA_PRIODIZACAO(6, 6, "Priorização", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(7, 7, "Especialidade", "String", ""),
	INDICE_COLUNA_SUBESPECIALIDADE(8, 8, "Subesecialidade", "String", ""),
	INDICE_COLUNA_PROCEDIMENTO(9, 9, "Procedimento", "String", ""),
	INDICE_COLUNA_CID(10, 10, "CID", "String", ""),
	INDICE_COLUNA_DATA_INDICACAO(11, 11, "Data indicação", "Date", "MMM/yyyy"),
	INDICE_COLUNA_DATA_INSERCAO(12, 12, "Data de inserção", "Date", "MMM/yyyy"),
	INDICE_COLUNA_DATA_EXECUCAO(13, 13, "Data de execução", "Date", "MMM/yyyy"),
	INDICE_COLUNA_DATA_SAIDA(14, 14, "Data de saída", "Date mes/ano", "MMM/yyyy"),
	INDICE_COLUNA_ESTABELECIMENTO(15, 15, "Estabelecimento", "String", ""),
	INDICE_COLUNA_SITUACAO(16, 16, "Situação", "String", ""),
	INDICE_COLUNA_CIDADE(17, 17, "Cidade", "String", ""),
	INDICE_COLUNA_TELEFONE_ADICIONAL_1(18, 18, "Telefone Adicional 1", "String", ""),
	INDICE_COLUNA_TELEFONE_ADICIONAL_2(19, 19, "Telefone Adicional 2", "String", ""),
	INDICE_COLUNA_UNIDADE_BASICA_REFERENCIA(20, 20, "Unidade Básica de Referência", "String", ""),
	INDICE_COLUNA_UNIDADE_SOLICITANTE(21, 21, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_TEMPO_ESPERA(22, 22, "Tempo de espera", "Integer", ""),
	INDICE_COLUNA_TEMPO_MEDIO_ESPERA(23, 23, "Tempo médio de espera", "Integer", ""),
	INDICE_COLUNA_TEMPO_MAXIMO_ESPERA(24, 24, "Tempo máximo de espera", "Integer", ""),
	INDICE_COLUNA_OBSERVACOES(25, 25, "Observações", "String", ""),
	
	MASCARA_DATA_DOWNLOAD(30, 0, "######", "", ""),
	MASCARA_STATUS(31, 0, "@@@@@@", "", ""),
	NOME_ARQUIVO_FILA_GERAL(32, 0, "GEFIC - FILA GERAL - " + MASCARA_DATA_DOWNLOAD.getDescricao() + " - " + MASCARA_STATUS.getDescricao() + ".xlsx", "", ""),
	NOME_ARQUIVO_FILA_OPM(33, 0, "GEFIC - FILA OPM - " + MASCARA_DATA_DOWNLOAD.getDescricao() + " - " + MASCARA_STATUS.getDescricao() + ".xlsx", "", ""),
	NOME_ARQUIVO_PACIENTE_POR_ESPECIALIDADE(34, 0, "GEFIC - PACIENTE POR ESPECIALIDADE - " + MASCARA_DATA_DOWNLOAD.getDescricao() + ".xlsx", "", ""),
	NOME_ARQUIVO_PACIENTE_POR_ESPECIALIDADE_OPM(35, 0, "GEFIC - PACIENTE POR ESPECIALIDADE OPM - " + MASCARA_DATA_DOWNLOAD.getDescricao() + ".xlsx", "", ""),
	NOME_ARQUIVO_TEMPO_ESPERA_POR_PROCEDIMENTO(36, 0, "GEFIC - TEMPO DE ESPERA POR PROCEDIMENTO - " + MASCARA_DATA_DOWNLOAD.getDescricao() + ".xlsx", "", ""),
	NOME_ARQUIVO_TEMPO_ESPERA_POR_PROCEDIMENTO_OPM(37, 0, "GEFIC - TEMPO DE ESPERA POR PROCEDIMENTO OPM - " + MASCARA_DATA_DOWNLOAD.getDescricao() + ".xlsx", "", ""),
	
	NOME_PLANILHA_OUTROS(42, 1, "MOTIVO OUTROS", "", ""),
	
	LINHA_INICIAL_ARQUIVO(43, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO_RELATORIO_BAIXADO(44, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoGEFICFilas(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoGEFICFilas> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoGEFICFilas::getIdUnico, Function.identity()));

    public static ParametrosArquivoGEFICFilas poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
