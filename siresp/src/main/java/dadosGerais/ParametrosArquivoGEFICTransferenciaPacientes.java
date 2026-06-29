package dadosGerais;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParametrosArquivoGEFICTransferenciaPacientes {
		
	INDICE_COLUNA_PACIENTE(0, 0, "Paciente", "String", ""),
	INDICE_COLUNA_CPF(1, 1, "Cpf", "String", ""),
	INDICE_COLUNA_DATA_TRANSFERENCIA(2, 2, "Data Transferência", "String", ""),
	INDICE_COLUNA_ESPECIALIDADE(3, 3, "Especialidade", "String", ""),
	INDICE_COLUNA_SUBESPECIALIDADE(4, 4, "Subespecialidade", "String", ""),
	INDICE_COLUNA_PROCEDIMENTO(5, 5, "Procedimento", "String", ""),
	INDICE_COLUNA_ESTABELECIMENTO_ORIGEM(6, 6, "Estabelecimento Origem", "String", ""),
	INDICE_COLUNA_ESTABELECIMENTO_DESTINO(7, 7, "Estabelecimento Destino", "String", ""),
	
	LINHA_INICIAL_ARQUIVO(43, 1, "Ajustado de acordo com o Java, no arquivo é a linha 2", "", ""),
	
	EXTENSAO_ARQUIVO_RELATORIO_BAIXADO(44, 0, "xlsx", "", "");
	

	private int idUnico;
	private int indice;
	private String descricao;
	private String tipo;
	private String formato;
			
	ParametrosArquivoGEFICTransferenciaPacientes(int idUnico, int indice, String descricao, String tipo, String formato)
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
	
    private static final Map<Integer, ParametrosArquivoGEFICTransferenciaPacientes> POR_ID_UNICO =
        Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(ParametrosArquivoGEFICTransferenciaPacientes::getIdUnico, Function.identity()));

    public static ParametrosArquivoGEFICTransferenciaPacientes poIdUnico(int idUnico) {
        return POR_ID_UNICO.get(idUnico); // pode retornar null se não existir
    }

}
