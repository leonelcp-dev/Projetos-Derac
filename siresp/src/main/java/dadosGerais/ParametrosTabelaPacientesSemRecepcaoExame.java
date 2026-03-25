package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosTabelaPacientesSemRecepcaoExame {
		
	INDICE_COLUNA_CONTROLE_CONSULTA(0, 0, "Controle Consulta", "String", ""),
	INDICE_COLUNA_DATA_AGENDA(1, 1, "Data da Agenda", "Date", "dd-MM-yyyy"),
	INDICE_COLUNA_HORA_INICIAL(2, 2, "Hora Inicial", "hh:mm", ""),
	INDICE_COLUNA_COD_PACIENTE(3, 3, "Cod. Paciente", "Int", ""),
	INDICE_COLUNA_PRONTUARIO(4, 4, "Prontuário", "String", ""),
	INDICE_COLUNA_NOME(5, 5, "Nome", "String", ""),
	INDICE_COLUNA_GRUPO_DE_COTA(6, 6, "Grupo de Cota", "String", ""),
	INDICE_COLUNA_EXAME(7, 7, "Exame", "String", ""),
	INDICE_COLUNA_NOME_PROFISSIONAL(8, 8, "Nome Profissional", "String", ""),
		
	QUANTIDADE_ESPERADA_DE_COLUNAS(9, 9, "Quantidade Esperada de Colunas", "", ""),
	
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA(10, 0, "xlsx", "", ""),
	EXTENSAO_ARQUIVO_OFERTA_DEMANDA_BAIXADO(11, 0, "xls", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosTabelaPacientesSemRecepcaoExame(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosTabelaPacientesSemRecepcaoExame> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosTabelaPacientesSemRecepcaoExame::getIdUnico, Function.identity()));

    public static ParametrosTabelaPacientesSemRecepcaoExame poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
