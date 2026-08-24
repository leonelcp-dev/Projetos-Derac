package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoGEFICFilasRelatorio {
		
	
	INDICE_COLUNA_INICIAIS_USUARIO(0, 0, "Iniciais da usuária", "String", ""),
	INDICE_COLUNA_PACIENTE(1, 1, "Paciente", "String", ""),
	INDICE_COLUNA_IDADE(2, 2, "Idade", "String", ""),
	INDICE_COLUNA_PRIODIZACAO(3, 3, "Priorização", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(4, 4, "Especialidade", "String", ""),
	INDICE_COLUNA_SUBESPECIALIDADE(5, 5, "Subesecialidade", "String", ""),
	INDICE_COLUNA_PROCEDIMENTO(6, 6, "Procedimento", "String", ""),
	INDICE_COLUNA_CID(7, 7, "CID", "String", ""),
	INDICE_COLUNA_DATA_INDICACAO(8, 8, "Data indicação", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_DATA_INSERCAO(9, 9, "Data de inserção", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_DATA_EXECUCAO(10, 10, "Data de execução", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_DATA_SAIDA(11, 11, "Data de saída", "Date mes/ano", "dd/MM/yyyy"),
	INDICE_COLUNA_ESTABELECIMENTO(12, 12, "Estabelecimento", "String", ""),
	INDICE_COLUNA_SITUACAO(13, 13, "Situação", "String", ""),
	INDICE_COLUNA_CIDADE(14, 14, "Cidade", "String", ""),
	INDICE_COLUNA_TEMPO_ESPERA(15, 15, "Tempo de espera", "Integer", ""),
	INDICE_COLUNA_TEMPO_MEDIO_ESPERA(16, 16, "Tempo médio de espera", "Integer", ""),
	INDICE_COLUNA_TEMPO_MAXIMO_ESPERA(17, 17, "Tempo máximo de espera", "Integer", ""),
	INDICE_COLUNA_OBSERVACOES(18, 18, "Observações", "String", ""),
	
	NOME_PLANILHA(42, 1, "Fila Nominal", "", ""),
	
	LINHA_INICIAL_ARQUIVO(43, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO_RELATORIO_BAIXADO(44, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoGEFICFilasRelatorio(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoGEFICFilasRelatorio> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoGEFICFilasRelatorio::getIdUnico, Function.identity()));

    public static ParametrosArquivoGEFICFilasRelatorio poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
