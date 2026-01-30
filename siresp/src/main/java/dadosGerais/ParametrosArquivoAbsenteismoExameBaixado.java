package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoAbsenteismoExameBaixado {
		
	INDICE_COLUNA_DATA_AGENDA(0, 0, "Data Agenda", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_HORA_AGENDA(1, 1, "Data Nascimento", "DateTime", "hh:mm"),
	INDICE_COLUNA_EXAME(2, 2, "Exame", "String", ""),
	INDICE_COLUNA_CODIGO(3, 3, "Código", "Int", ""),
	INDICE_COLUNA_PACIENTE(4, 4, "Paciente", "String", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(5, 5, "Nasc.", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_TELEFONE(6, 6, "Telefone", "String", ""),
	INDICE_COLUNA_MUNICIPIO(7, 7, "Município", "String", ""),
	INDICE_COLUNA_DATA_HORA_AGENDAMENTO(8, 8, "Date", "Date", "dd/MM/yyyy hh:mm"),
	INDICE_COLUNA_ACAO_JUDICIAL(9, 9, "Ação Judicial", "String", ""),
	INDICE_COLUNA_ATENDIDO(10, 10, "Atendido", "String", ""),
	INDICE_COLUNA_AUSENTE(11, 11, "Aus", "String", ""),
	INDICE_COLUNA_DISPENSADO(12, 12, "Disp", "String", ""),
	INDICE_COLUNA_DESISTENCIA(13, 13, "Des", "String", ""),
	INDICE_COLUNA_ALTA(14, 14, "Alta", "String", ""),
	INDICE_COLUNA_MOTIVO(15, 15, "Motivo", "String", ""),
	INDICE_COLUNA_MOTIVO_ALTA(16, 16, "Motivo Alta", "String", ""),
	INDICE_COLUNA_USUARIO(17, 17, "Usuário", "String", ""),
	INDICE_COLUNA_EXECUTANTE(18, 18, "Executante", "String", ""),
	LINHA_INICIAL_ARQUIVO_SIRESP(19, 7,"Ajustado de acordo com o Java, no arquivo é a linha 8", "", ""),
	COLUNA_INICIAL_ARQUIVO_SIRESP(20, 0,"Ajustado de acordo com o Java, no arquivo é a coluna A (1)", "", ""),
	EXTENSAO_ARQUIVO_ABSENTEISMO(21, 0, "xls", "", "");

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoAbsenteismoExameBaixado(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoAbsenteismoExameBaixado> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoAbsenteismoExameBaixado::getIdUnico, Function.identity()));

    public static ParametrosArquivoAbsenteismoExameBaixado poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
