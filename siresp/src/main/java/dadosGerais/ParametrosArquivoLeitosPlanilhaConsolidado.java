package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoLeitosPlanilhaConsolidado {
		
	INDICE_COLUNA_DATA(0, 1, "Data", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_ESPECIALIDADE(1, 2, "Especialidade", "Int", ""),
	INDICE_COLUNA_TOTAL_DISPONIVEL(2, 3,"Total Disponível", "Int", ""),
	INDICE_COLUNA_RESERVA_INTERNA(3, 4, "Reserva Interna", "Int", ""),
	INDICE_COLUNA_TOTAL_OCUPADO(4, 5, "Total Ocupado", "Int", ""),
	INDICE_COLUNA_REGULAR_OCUPADO(5, 6, "Regular", "Int", ""),
	INDICE_COLUNA_EXTRA_OCUPADO(6, 7, "Extra", "Int", ""),
	INDICE_COLUNA_INTERNO_OCUPADO(7, 8, "Interno", "Int", ""),
	INDICE_COLUNA_TOTAL_BLOQUEADO(8, 9, "Total Bloqueado", "Int", ""),
	INDICE_COLUNA_ISOLAMENTO_BLOQUEADO(9, 10, "Isolamento", "Int", ""),
	INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO(10, 11, "Aguardando Paciente", "Int", ""),
	INDICE_COLUNA_OUTROS_BLOQUEADO(11, 12, "Outros", "Int", ""),
	INDICE_COLUNA_LEITOS_VAGOS(12, 13, "Vagos", "Int", ""),
	INDICE_COLUNA_TAXA_DE_OCUPACAO(13, 14, "Taxa de Ocupação", "Porcentagem", ""),
	
	LINHA_INICIAL_ARQUIVO(15, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(16, 0, "Consolidado Leitos", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(17, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(18, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoLeitosPlanilhaConsolidado(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoLeitosPlanilhaConsolidado> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoLeitosPlanilhaConsolidado::getIdUnico, Function.identity()));

    public static ParametrosArquivoLeitosPlanilhaConsolidado poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
