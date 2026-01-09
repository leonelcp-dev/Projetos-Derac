package modelosDados;

public class CelulaExcel {
	
	private int linha;
	private int coluna;
	private String valor;
	
	public CelulaExcel(int linha, int coluna, String valor)
	{
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setValor(valor);	
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	
}
