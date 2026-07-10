package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaFilasRegistrosCanceladosGEFIC {
		
	INDICE_COLUNA_POSICAO(0, 0, "Posição", "String", ""),
	INDICE_COLUNA_PACIENTE(1, 1, "Paciente", "String", ""),
	INDICE_COLUNA_CPF(2, 2, "CPF", "String", ""),
	INDICE_COLUNA_IDADE(3, 3, "Idade", "Int", ""),
	INDICE_COLUNA_PRIORIZACAO(4, 4, "Priorização", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(5, 5, "Especialidade", "String", ""),
	INDICE_COLUNA_SUBESPECIALIDADE(6, 6, "Subespecialidade", "String", ""),
	INDICE_COLUNA_PROCEDIMENTO(7, 7, "Procedimento", "String", ""),
	INDICE_COLUNA_DATA_INDICACAO(8, 8, "Data indicação", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_DATA_SAIDA(9, 9, "Data saída", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_SITUACAO(10, 10, "Situação", "String", ""),
	INDICE_COLUNA_ACOES(11, 11, "Ações", "", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(12, 12, "Quantidade Esperada de Colunas", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaFilasRegistrosCanceladosGEFIC(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaFilasRegistrosCanceladosGEFIC> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaFilasRegistrosCanceladosGEFIC::getIdUnico, Function.identity()));

    public static ParametrosTabelaFilasRegistrosCanceladosGEFIC poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
