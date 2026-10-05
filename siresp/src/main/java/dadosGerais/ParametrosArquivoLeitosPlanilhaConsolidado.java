package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoLeitosPlanilhaConsolidado {
		
	INDICE_COLUNA_DATA(0, 1, "Data", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_ESPECIALIDADE(1, 2, "Especialidade", "String", ""),
	INDICE_COLUNA_ENFERMARIA(2, 3, "Enfermaria", "String", ""),
	INDICE_COLUNA_TOTAL_DISPONIVEL(3, 4,"Total Disponível", "Int", ""),
	INDICE_COLUNA_RESERVA_INTERNA(4, 5, "Reserva Interna", "Int", ""),
	INDICE_COLUNA_USO_NAO_CONVENIADO(5, 6, "Uso Não Conveniado", "Int", ""),
	INDICE_COLUNA_TOTAL_OCUPADO(6, 7, "Total Ocupado", "Int", ""),
	INDICE_COLUNA_REGULAR_OCUPADO(7, 8, "Regular", "Int", ""),
	INDICE_COLUNA_EXTRA_PACTUADO_OCUPADO(8, 9, "Extra Pactuado", "Int", ""),
	INDICE_COLUNA_EXTRA_NAO_PACTUADO_OCUPADO(9, 10, "Extra Não Pactuado", "Int", ""),
	INDICE_COLUNA_INTERNO_OCUPADO(10, 11, "Interno", "Int", ""),
	INDICE_COLUNA_NAO_CONVENIADO_OCUPADO(11, 12, "Não Conveniado", "Int", ""),
	INDICE_COLUNA_TOTAL_BLOQUEADO(12, 13, "Total Bloqueado", "Int", ""),
	INDICE_COLUNA_ISOLAMENTO_BLOQUEADO(13, 14, "Isolamento", "Int", ""),
	INDICE_COLUNA_AGUARDANDO_PACIENTE_BLOQUEADO(14, 15, "Aguardando Paciente", "Int", ""),
	INDICE_COLUNA_SAZONALIDADE_BLOQUEADO(15, 16, "Sazonalidade", "Int", ""),
	INDICE_COLUNA_RESERVA_INTERNA_BLOQUEADO(16, 17, "Reserva Interna", "Int", ""),
	INDICE_COLUNA_MANUTENCAO_BLOQUEADO(17, 18, "Manutenção", "Int", ""),
	INDICE_COLUNA_ADMINISTRATIVO_BLOQUEADO(18, 19, "Administrativo", "Int", ""),
	INDICE_COLUNA_OUTROS_BLOQUEADO(19, 20, "Outros", "Int", ""),
	INDICE_COLUNA_LEITOS_VAGOS(20, 21, "Vagos", "Int", ""),
	INDICE_COLUNA_TAXA_DE_OCUPACAO(21, 22, "Taxa de Ocupação", "Porcentagem", ""),
	
	LINHA_INICIAL_ARQUIVO(22, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_CONSOLIDADA(23, 0, "Consolidado Leitos", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(24, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(25, 0, "xls", "", ""),
	INDICE_COLUNA_DATA_PROCESSAMENTO(26, 2, "Ajustado de acordo com o Java, no arquivo é a coluna 3 (C)", "", ""),
	INDICE_LINHA_DATA_PROCESSAMENTO(27, 7, "Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),
	TEXTO_NAO_CADASTRADO(31, 8, "***NÃO CADASTRADO***", "", "");
	

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
