package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado {
		
	INDICE_COLUNA_DATA(0, 1, "Data", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_HORARIO_EXTRACAO(1, 2, "Data", "Time", "HH:mm:ss"),
	INDICE_COLUNA_SOLICITANTE(2, 3, "Solicitante", "String", ""),
	INDICE_COLUNA_RECURSO(3, 4, "Recurso", "String", ""),
	INDICE_COLUNA_FICHA(4, 5,"Ficha", "String", ""),
	INDICE_COLUNA_TOTAL_GERAL(5, 6, "Total Geral", "Int", ""),
	INDICE_COLUNA_0_6_HORAS(6, 7, "0-6 horas", "Int", ""),
	INDICE_COLUNA_6_12_HORAS(7, 8, "6-12 horas", "Int", ""),
	INDICE_COLUNA_12_24_HORAS(8, 9, "12-24 horas", "Int", ""),
	INDICE_COLUNA_24_48_HORAS(9, 10, "24_48 horas", "Int", ""),
	INDICE_COLUNA_2_3_DIAS(10, 11, "2-3 dias", "Int", ""),
	INDICE_COLUNA_3_5_DIAS(11, 12, "3-5 dias", "Int", ""),
	INDICE_COLUNA_5_7_DIAS(12, 13, "5-7 dias", "Int", ""),
	INDICE_COLUNA_7_10_DIAS(13, 14, "7-10 dias", "Int", ""),
	INDICE_COLUNA_10_13_DIAS(14, 15, "10-13 dias", "Int", ""),
	INDICE_COLUNA_13_15_DIAS(15, 16, "13-15 dias", "Int", ""),
	INDICE_COLUNA_15_17_DIAS(16, 17, "15-17 dias", "Int", ""),
	INDICE_COLUNA_17_20_DIAS(17, 18, "17-20 dias", "Int", ""),
	INDICE_COLUNA_20_25_DIAS(18, 19, "20-25 dias", "Int", ""),
	INDICE_COLUNA_25_30_DIAS(19, 20, "25-30 dias", "Int", ""),
	INDICE_COLUNA_30_DIAS_ACIMA(20, 21, "30 dias acima", "Int", ""),
	
	LINHA_INICIAL_ARQUIVO(21, 11, "Ajustado de acordo com o Java, no arquivo é a linha 12", "", ""),
	
	NOME_PLANILHA_MONITORAMENTO(22, 0, "Monitoramento de Leitos", "", ""),
	
	DIVISOR_UNIDADE_ESPECIALIDADE(23, 0, "####", "", ""),
	DIVISOR_ESPECIALIDADE_ENFERMARIA(24, 0, "@@@@", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(25, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(26, 0, "xls", "", ""),
	
	INDICE_COLUNA_DATA_PROCESSAMENTO(27, 2, "Ajustado de acordo com o Java, no arquivo é a coluna 3 (C)", "", ""),
	INDICE_LINHA_DATA_PROCESSAMENTO(28, 7, "Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),;
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado::getIdUnico, Function.identity()));

    public static ParametrosArquivoUrgenciaPlanilhaAguardandoAgrupado poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
