package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoLeitosPlanilhaMonitoramento {
		
	INDICE_COLUNA_UNIDADE(0, 1, "Unidade", "String", ""),
	INDICE_COLUNA_DATA(1, 2, "Data", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_ESPECIALIDADE(2, 3, "Especialidade", "Int", ""),
	INDICE_COLUNA_TOTAL_DISPONIVEL(3, 4,"Total Disponível", "Int", ""),
	INDICE_COLUNA_RESERVA_INTERNA(4, 5, "Reserva Interna", "Int", ""),
	INDICE_COLUNA_TOTAL_OCUPADO(5, 6, "Total Ocupado", "Int", ""),
	INDICE_COLUNA_REGULAR_OCUPADO(6, 7, "Regular", "Int", ""),
	INDICE_COLUNA_EXTRA_OCUPADO(7, 8, "Extra", "Int", ""),
	INDICE_COLUNA_INTERNO_OCUPADO(8, 9, "Interno", "Int", ""),
	INDICE_COLUNA_TOTAL_BLOQUEADO(9, 10, "Total Bloqueado", "Int", ""),
	INDICE_COLUNA_ISOLAMENTO_BLOQUEADO(10, 11, "Isolamento", "Int", ""),
	INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO(11, 12, "Aguardando Paciente", "Int", ""),
	INDICE_COLUNA_OUTROS_BLOQUEADO(12, 13, "Outros", "Int", ""),
	INDICE_COLUNA_LEITOS_VAGOS(13, 14, "Vagos", "Int", ""),
	INDICE_COLUNA_TAXA_DE_OCUPACAO(14, 15, "Taxa de Ocupação", "Porcentagem", ""),
	
	LINHA_INICIAL_ARQUIVO(15, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_MONITORAMENTO(16, 0, "Monitoramento de Leitos", "", ""),
	
	DIVISOR_UNIDADE_ESPECIALIDADE(17, 0, "#$%&", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(18, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(19, 0, "xls", "", "");
	

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
