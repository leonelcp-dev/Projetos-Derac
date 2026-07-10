package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoGEFICMotivoOutros {
		
	
	INDICE_COLUNA_UNIDADE(0, 0, "Unidade", "String", ""),
	INDICE_COLUNA_COMPETENCIA(1, 1, "Competência", "Date mes/ano", "MMM/yyyy"),
	INDICE_COLUNA_PACIENTE(2, 2, "Paciente", "String", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(3, 3, "Data de Nascimento", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_IDADE(4, 4, "Idade", "Int", ""),
	INDICE_COLUNA_DATA_SAIDA(5, 5, "Data de Saída", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_MOTIVO_SAIDA(6, 6, "Motivo da Saída", "String", ""),
	INDICE_COLUNA_PROCEDIMENTO(7, 7, "Procedimento", "String", ""),
	INDICE_COLUNA_OBSERVACAO(8, 8, "Observação", "String", ""),
	
	NOME_PLANILHA_OUTROS(42, 1, "MOTIVO OUTROS", "", ""),
	
	LINHA_INICIAL_ARQUIVO(43, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO_RELATORIO_BAIXADO(44, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoGEFICMotivoOutros(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoGEFICMotivoOutros> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoGEFICMotivoOutros::getIdUnico, Function.identity()));

    public static ParametrosArquivoGEFICMotivoOutros poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
