package modelosDados;

public class CorrelacaoColunasArquivosAbsenteismo {
	
	private int colunaConsolidado;
	private int colunaSIRESP;
	private String tipo;
	private String formato;
	
	public CorrelacaoColunasArquivosAbsenteismo(int colunaConsolidado, int colunaSIRESP, String tipo, String formato)
	{
		this.colunaConsolidado = colunaConsolidado;
		this.colunaSIRESP = colunaSIRESP;
		this.tipo = tipo;
		this.formato = formato;
	}
	
	public int getColunaConsolidado() {
		return colunaConsolidado;
	}
	
	public void setColunaConsolidado(int colunaConsolidado) {
		this.colunaConsolidado = colunaConsolidado;
	}

	public int getColunaSIRESP() {
		return colunaSIRESP;
	}

	public void setColunaSIRESP(int colunaSIRESP) {
		this.colunaSIRESP = colunaSIRESP;
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

}
