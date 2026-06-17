package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaUrgenciaSolicitacoesPendentes {
		
	INDICE_COLUNA_PRIORIDADE(0, 0, "Prioridade", "String", ""),
	INDICE_COLUNA_NR_FICHA(1, 1, "Nr. FICHA", "String", ""),
	INDICE_COLUNA_SOLICITACAO_EM(2, 2, "Solicitação em:", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_SOLICITANTE(3, 3, "Solicitante", "String", ""),
	INDICE_COLUNA_PACIENTE(4, 4, "Paciente", "String", ""),
	INDICE_COLUNA_RESPONSAVEL(5, 5, "Responsável", "String", ""),
	INDICE_COLUNA_ASSUMIDO_EM(6, 6, "Assumido em:", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_FICHA(7, 7, "Ficha", "String", ""),
	INDICE_COLUNA_RECURSO(8, 8, "Recurso", "String", ""),
	INDICE_COLUNA_MUNICIPIO(9, 9, "Município", "String", ""),
	INDICE_COLUNA_DISPONIVEL_PARA_REGULACAO(10, 10, "Disponível para Regulação", "DateTime", "dd/MM/yyyy HH:mm:ss"),
	INDICE_COLUNA_HIPOTESE(11, 11, "Hipótese", "String", ""),
	INDICE_COLUNA_ACAO(12, 12, "Ação", "String", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(13, 9, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(14, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(15, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaUrgenciaSolicitacoesPendentes(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaUrgenciaSolicitacoesPendentes> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaUrgenciaSolicitacoesPendentes::getIdUnico, Function.identity()));

    public static ParametrosTabelaUrgenciaSolicitacoesPendentes poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
