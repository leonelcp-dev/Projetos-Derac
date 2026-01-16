package modelosDados;

public class CelulaExcel {
	
	private int linha;
	private int coluna;
	private Object valor;
	private String tipo;
	
	public CelulaExcel(int linha, int coluna, Object valor, String tipo)
	{
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setValor(valor);	
		this.setTipo(tipo);
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
