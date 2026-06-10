package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoDemandaReprimidaRegulada {
		
	INDICE_COLUNA_SOLICITADO_EM(0, 0, "Solicitado em:", "String", ""),
	INDICE_COLUNA_TEMPO_DE_ESPERA_EM_DIAS(1, 1, "Tempo de Espera em Dias", "Int", ""),
	INDICE_COLUNA_FICHA(2, 2, "Ficha", "String", ""),
	INDICE_COLUNA_NOME_PACIENTE(3, 3, "Nome Paciente", "String", ""),
	INDICE_COLUNA_CODIGO(4, 4, "Código", "String", ""),
	INDICE_COLUNA_NOME_ABREVIADO(5, 5, "Nome Abreviado", "String", ""),
	INDICE_COLUNA_UNIDADE_TIPO(6, 6, "UNIDADE TIPO", "String", ""),
	INDICE_COLUNA_UNIDADE_SOLICITANTE(7, 7, "Unidade Solicitante", "Int", ""),
	INDICE_COLUNA_NOME_FICHA(8, 8, "Nome Ficha", "String", ""),
	INDICE_COLUNA_GRUPO_DE_COTAS_E_ESPECIALIDADES_NOMENCLATURA(9, 9, "GRUPO DE COTAS E ESPECIALIDADES.NOMENCLATURA", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE_EXAME(10, 10, "Especialidade/Exame", "String", ""),
	INDICE_COLUNA_HIPOTESE(11, 11, "Hipótese", "String", ""),
	
	LINHA_INICIAL_PLANILHA_REGULADA(18, 1,"Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	LINHA_INICIAL_TABELA_DINAMICA(19, 3,"Ajustado de acordo com o Java, no arquivo é a linha 4", "", ""),
	
	INDICE_COLUNA_CONSOLIDADO_ESPECIALIDADE(20, 0, "Especialidade", "String", ""),
	INDICE_COLUNA_CONSOLIDADO_CONTAGEM_ESPECIALIDADE(21, 1, "Contagem Especialidade", "Int", ""),
	INDICE_COLUNA_CONSOLIDADO_MAX_TEMPO_ESPERA(22, 2, "Max Tempo Espera", "Int", ""),

	NOME_PLANILHA_REGULADA(23, 0, "REGULADA", "", ""),
	NOME_PLANILHA_DINAMICA_REGULADA(24, 0, "DINÂMICA REGULADA", "", ""),
	NOME_PLANILHA_CONSOLIDADO_REGULADA(25, 0, "CONSOLIDADO REGULADA", "", ""),
	
	AREA_PARA_TABELA_DINAMICA_REGULADA(26, 0, "A1:L", "", ""),
	
	TEXTO_TOTAL_GERAL(27, 0, "Total Geral", "", ""),
	TEXTO_ROTULOS_DE_LINHA(28, 0, "Rótulos de Linha", "", ""),
	
	EXTENSAO_ARQUIVO(29, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoDemandaReprimidaRegulada(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoDemandaReprimidaRegulada> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoDemandaReprimidaRegulada::getIdUnico, Function.identity()));

    public static ParametrosArquivoDemandaReprimidaRegulada poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
