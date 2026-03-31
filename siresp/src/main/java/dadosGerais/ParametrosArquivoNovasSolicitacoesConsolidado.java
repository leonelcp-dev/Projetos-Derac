package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoNovasSolicitacoesConsolidado {
	
	INDICE_COLUNA_COD_PACIENTE(0, 0, "Cod Paciente", "String", ""),
	INDICE_COLUNA_TIPO_SOLICITACAO(1, 1, "Tipo Solicitação", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE_EXAME(2, 2, "Especialidade/Exame", "String", ""),
	INDICE_COLUNA_CID(3, 3, "CID", "String", ""),
	INDICE_COLUNA_DATA_INCLUSAO(4, 4, "Data Inclusão", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_UNIDADES_CAMPINAS(5, 5, "Unidades Campinas", "String", ""),
	INDICE_COLUNA_ANO_INCLUSAO(6, 6, "Ano Inclusão", "Int", ""),

	ARQUIVO_MUNICIPAL_NOME(7, 6,"BD Demanda Reprimida - CDR.xlsx", "", ""),
	ARQUIVO_MUNICIPAL_VAZIO(7, 6,"ARQUIVO MENSAL VAZIO.xlsx", "", ""),
	
	ARQUIVO_MUNICIPAL_LINHA_INICIAL(8, 1,"Ajustado de acordo com o Java, no arquivo é a Linha 2", "", ""),
	ARQUIVO_MUNICIPAL_VAZIO_LINHA_INICIAL(8, 1,"Ajustado de acordo com o Java, no arquivo é a Linha 2", "", ""),
	
	NOME_PLANILHA_BD_CDR(9, 8,"BD - CDR", "", ""),
	NOME_PLANILHA_DINAMICA(10, 8,"Dinamica BD - CDR", "", ""),
	
	EXTENSAO_ARQUIVO_BAIXADO(12, 0, "xls", "", ""),
	EXTENSAO_ARQUIVO_CONSOLIDADO(13, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoNovasSolicitacoesConsolidado(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoNovasSolicitacoesConsolidado> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoNovasSolicitacoesConsolidado::getIdUnico, Function.identity()));

    public static ParametrosArquivoNovasSolicitacoesConsolidado poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
