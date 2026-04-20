package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoDemandaReprimida {
		
	INDICE_COLUNA_ESPECIALIDADE(0, 0, "Especialidade", "String", ""),
	INDICE_COLUNA_CONTAGEM_ESPECIALIDADE(1, 1, "Contagem Especialidade", "Int", ""),
	INDICE_COLUNA_MAX_TEMPO_ESPERA(2, 2, "Max Tempo Espera", "Int", ""),
	
	LINHA_INICIAL_TABELA_DINAMICA(3, 3,"Ajustado de acordo com o Java, no arquivo é a linha 4", "", ""),
	
	NOME_PLANILHA_DINAMICA_REGULADA(4, 0, "DINÂMICA REGULADA", "", ""),
	NOME_PLANILHA_DINAMICA_CDR(5, 0, "DINÂMICA CDR", "", ""),
	
	TEXTO_TOTAL_GERAL(6, 0, "Total Geral", "", ""),
	TEXTO_ROTULOS_DE_LINHA(7, 0, "Rótulos de Linha", "", ""),
	
	EXTENSAO_ARQUIVO(8, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoDemandaReprimida(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoDemandaReprimida> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoDemandaReprimida::getIdUnico, Function.identity()));

    public static ParametrosArquivoDemandaReprimida poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
