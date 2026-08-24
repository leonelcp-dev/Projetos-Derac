package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoConsolidadoGEFIC {
		
	INDICE_COLUNA_INICIAL_RELATORIOS(0, 1, "Coluna ajustada de acordo com a coluna B do arquivo", "", ""),
	INDICE_LINHA_INICIAL_RELATORIOS(1, 4, "Ajustado de acordo com a linha 5 do arquivo", "", ""),
	INDICE_LINHA_INICIAL_ENTRADA_SAIDA(2, 5, "Ajustado de acordo com a linha 5 do arquivo", "", ""),
	
	MASCARA_ANO_DINAMICO(3, 0, "######", "", ""),
	
	NOME_PLANILHA_GERAL(16, 0, "GERAL", "", ""),
	NOME_PLANILHA_ENTRADA_SAIDA(17, 0, "ENTRADA_SAIDA", "", ""),
	NOME_PLANILHA_CIRURGIAS_REALIZADAS(18, 0, "CIRURGIAS REALIZADAS", "", ""),
	NOME_PLANILHA_CIRURGIAS_CANCELADAS(19, 0, "CIRURGIAS CANCELADAS", "", ""),
	NOME_PLANILHA_OPM_ENTREGUES(20, 0, "OPM ENTREGUES", "", ""),
	NOME_PLANILHA_OPM_CANCELADAS(21, 0, "OPM CANCELADAS", "", ""),
	NOME_PLANILHA_NAO_CONFORMIDADES(22, 0, "NÃO CONFORMIDADES", "", ""),
	
	TEXTO_TOTAL(23, 0, "TOTAL", "", ""),
	
	TEXTO_SERVICO(24, 0, "SERVIÇO", "", ""),
	TEXTO_SOMAR(25, 0, "SOMAR", "", ""),
	TEXTO_SUBTRAIR(26, 0, "SUBTRAIR", "", ""),
	TEXTO_CIRURGIA_REALIZADA(27, 0, "Realizou a cirurgia", "", ""),
	TEXTO_OPM_ENTREGUE(28, 0, "Entrega realizada", "", ""),
	
	TEXTO_FINAL_RELATORIO(29, 0, "Final do relatório", "", ""),
	
	TEXTO_CIRURGIA_ELETIVA(30, 0, "CIRURGIA ELETIVA", "", ""),
	TEXTO_OPM(31, 0, "OPM", "", ""),
	
	TEXTO_SIM(32, 0, "SIM", "", ""),
	TEXTO_NAO(33, 0, "NÃO", "", ""),
	
	EXTENSAO_ARQUIVO(34, 0, "xlsx", "", ""),
	
	NOME_ARQUIVO_CONSOLIDADO_GEFIC_ELETIVAS(35, 0, "GEFIC " + MASCARA_ANO_DINAMICO.getDescricao() + ".xlsx", "", ""),
	NOME_ARQUIVO_CONSOLIDADO_GEFIC_OPM(36, 0, "GEFIC OPM " + MASCARA_ANO_DINAMICO.getDescricao() + ".xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoConsolidadoGEFIC(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoConsolidadoGEFIC> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoConsolidadoGEFIC::getIdUnico, Function.identity()));

    public static ParametrosArquivoConsolidadoGEFIC poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
