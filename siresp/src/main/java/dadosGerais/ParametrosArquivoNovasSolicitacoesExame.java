package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoNovasSolicitacoesExame {
	
	INDICE_COLUNA_UNIDADE_SOLICITANTE(0, 0, "Unidade Solicitante", "String", ""),
	INDICE_COLUNA_COD_PACIENTE(1, 1, "Cod Paciente", "String", ""),
	INDICE_COLUNA_PACIENTE(2, 2, "Paciente", "String", ""),
	INDICE_COLUNA_IDADE(3, 3, "Idade", "Int", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(4, 4, "Data Nascimento", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_TIPO(5, 5, "Tipo", "String", ""),
	INDICE_COLUNA_TIPO_EXAME(6, 6, "Tipo Exame", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE_EXAME(7, 7, "Especialidade/Exame", "String", ""),
	INDICE_COLUNA_CID(8, 8, "CID", "String", ""),
	INDICE_COLUNA_DATA_INCLUSAO(9, 9, "Data Inclusão", "DateTime", "dd/MM/yyyy hh:mm"),
	INDICE_COLUNA_DATA_SAIDA(10, 10, "Data Saída", "DateTime", "dd/MM/yyyy hh:mm"),
	INDICE_COLUNA_TOTAL_DIAS_AGUARDANDO(11, 11, "Total dias Aguardando", "String", ""),
	INDICE_COLUNA_PROFISSIONAL(12, 12, "Profissional", "String", ""),
	INDICE_COLUNA_UNIDADE_INDICADA_PARA_AGENDAMENTO(13, 13, "Unidade Indicada para Agendamento", "String", ""),

	ARQUIVO_FINAL_LINHA_INICIAL(14, 1,"Ajustado de acordo com o Java, no arquivo é a Linha 2", "", ""),
	ARQUIVO_BAIXADO_LINHA_INICIAL(15, 8,"Ajustado de acordo com o Java, no arquivo é a Linha 9", "", ""),
	
	NOME_PLANILHA(16, 8,"demanda_por_recurso_qualitativo", "", ""),
	
	EXTENSAO_ARQUIVO_BAIXADO(17, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_FORMATADO(18, 0, "xlsx", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoNovasSolicitacoesExame(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoNovasSolicitacoesExame> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoNovasSolicitacoesExame::getIdUnico, Function.identity()));

    public static ParametrosArquivoNovasSolicitacoesExame poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
