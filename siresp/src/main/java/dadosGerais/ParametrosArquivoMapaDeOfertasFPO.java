package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoMapaDeOfertasFPO {
		
	INDICE_COLUNA_ESPECIALIDADES(0, 0, "ESPECIALIDADES", "String", ""),
	INDICE_COLUNA_PROCEDIMENTOS(1, 1, "PROCEDIMENTOS (nomenclatura padronizada)", "String", ""),
	INDICE_COLUNA_PROCEDIMENTOS_EXECUTANTE(2, 2, "Procedimentos+Executante", "String", ""),
	INDICE_COLUNA_AGENDA(3, 3, "Agenda", "String", ""),
	INDICE_COLUNA_TIPO(4, 4, "Tipo", "String", ""),
	INDICE_COLUNA_EXECUTANTE(5, 5, "Executante", "String", ""),
	INDICE_COLUNA_FPO(6, 6, "FPO", "Int", ""),
	INDICE_COLUNA_PLANO_DE_TRABALHO(7, 7, "Plano de trabalho (documento SEI)", "String", ""),
	INDICE_COLUNA_MES_DE_REFERENCIA(8, 8, "Mês de Referência", "Date", "MMM/yyyy"),
	
	LINHA_INICIAL_ARQUIVO(9, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),

	VALOR_DINAMICO(10, 0, "#####", "", ""),
	NOME_PLANILHA_CONSOLIDADA(11, 0, "Mapa de ofertas_" + VALOR_DINAMICO.getDescricao(), "", ""),
	
	NOME_PADRAO_ARQUIVO(12, 0, "00. MAPA DE OFERTAS.xlsx", "", ""),
		
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(13, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(14, 0, "xls", "", "");
	
	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoMapaDeOfertasFPO(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoMapaDeOfertasFPO> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoMapaDeOfertasFPO::getIdUnico, Function.identity()));

    public static ParametrosArquivoMapaDeOfertasFPO poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
