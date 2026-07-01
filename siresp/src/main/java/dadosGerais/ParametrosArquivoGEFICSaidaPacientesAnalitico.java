package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoGEFICSaidaPacientesAnalitico {
		
	
	INDICE_COLUNA_PACIENTE(0, 0, "Paciente", "String", ""),
	INDICE_COLUNA_DATA_SAIDA(1, 1, "Data saída", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_MOTIVO_SAIDA(2, 2, "Motivo saída", "String", ""),
	INDICE_COLUNA_DATA_NASCIMENTO(3, 3, "Data nascimento", "Date", "dd/MM/yyyy"),
	INDICE_COLUNA_IDADE(4, 4, "Idade", "Integer", ""),
	INDICE_COLUNA_ESPECIALIDADE(5, 5, "Especialidade", "String", ""),
	INDICE_COLUNA_SUBESPECIALIDADE(6, 6, "Subespecialidade", "String", ""),
	INDICE_COLUNA_PROCEDIMENTO(7, 7, "Procedimento", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(43, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO_RELATORIO_BAIXADO(44, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoGEFICSaidaPacientesAnalitico(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoGEFICSaidaPacientesAnalitico> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoGEFICSaidaPacientesAnalitico::getIdUnico, Function.identity()));

    public static ParametrosArquivoGEFICSaidaPacientesAnalitico poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
