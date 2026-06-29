package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoLeitosPlanilhaMonitoramento {
		
	INDICE_COLUNA_UNIDADE(0, 1, "Unidade", "String", ""),
	INDICE_COLUNA_DATA(1, 2, "Data", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_ESPECIALIDADE(2, 3, "Especialidade", "Int", ""),
	INDICE_COLUNA_ENFERMARIA(3, 4, "Enfermaria", "String", ""),
	INDICE_COLUNA_TOTAL_DISPONIVEL(4, 5,"Total Disponível", "Int", ""),
	INDICE_COLUNA_RESERVA_INTERNA(5, 6, "Reserva Interna", "Int", ""),
	INDICE_COLUNA_USO_NAO_CONVENIADO(6, 7, "Uso Não Conveniado", "Int", ""),
	INDICE_COLUNA_TOTAL_OCUPADO(7, 8, "Total Ocupado", "Int", ""),
	INDICE_COLUNA_REGULAR_OCUPADO(8, 9, "Regular", "Int", ""),
	INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO(9, 10, "Extra Pactuado", "Int", ""),
	INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO(10, 11, "Extra Não Pactuado", "Int", ""),
	INDICE_COLUNA_INTERNO_OCUPADO(11, 12, "Interno", "Int", ""),
	INDICE_COLUNA_NAO_CONVENIADO_OCUPADO(12, 13, "Interno", "Int", ""),
	INDICE_COLUNA_TOTAL_BLOQUEADO(13, 14, "Total Bloqueado", "Int", ""),
	INDICE_COLUNA_ISOLAMENTO_BLOQUEADO(14, 15, "Isolamento", "Int", ""),
	INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO(15, 16, "Aguardando Paciente", "Int", ""),
	INDICE_COLUNA_OUTROS_BLOQUEADO(16, 17, "Outros", "Int", ""),
	INDICE_COLUNA_LEITOS_VAGOS(17, 18, "Vagos", "Int", ""),
	INDICE_COLUNA_TAXA_DE_OCUPACAO(18, 19, "Taxa de Ocupação", "Porcentagem", ""),
	
	LINHA_INICIAL_ARQUIVO(19, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_MONITORAMENTO(20, 0, "Monitoramento de Leitos", "", ""),
	
	DIVISOR_UNIDADE_ESPECIALIDADE(21, 0, "####", "", ""),
	DIVISOR_ESPECIALIDADE_ENFERMARIA(22, 0, "@@@@", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(23, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(24, 0, "xls", "", ""),
	
	INDICE_COLUNA_DATA_PROCESSAMENTO(25, 2, "Ajustado de acordo com o Java, no arquivo é a coluna 3 (C)", "", ""),
	INDICE_LINHA_DATA_PROCESSAMENTO(26, 7, "Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),;
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoLeitosPlanilhaMonitoramento(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoLeitosPlanilhaMonitoramento> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoLeitosPlanilhaMonitoramento::getIdUnico, Function.identity()));

    public static ParametrosArquivoLeitosPlanilhaMonitoramento poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
