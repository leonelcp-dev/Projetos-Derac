package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoGEFICEntradaPacientes {
		
	
	INDICE_COLUNA_MES_ANO_INDICACAO(0, 0, "Mês/Ano Indicação", "String", ""),
	INDICE_COLUNA_ESTABELECIMENTO(1, 1, "Estabelecimento", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(2, 2, "Especialidade", "String", ""),
	INDICE_COLUNA_SUBESPECIALIDADE(3, 3, "Subespecialidade", "String", ""),
	INDICE_COLUNA_QTDE_ENTRADA(4, 4, "Qtde Entrada", "Integer", ""),
	
	LINHA_INICIAL_ARQUIVO(43, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO_RELATORIO_BAIXADO(44, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoGEFICEntradaPacientes(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoGEFICEntradaPacientes> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoGEFICEntradaPacientes::getIdUnico, Function.identity()));

    public static ParametrosArquivoGEFICEntradaPacientes poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
