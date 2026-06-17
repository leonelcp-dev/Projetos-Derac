package modelosDados;

public class IntervalosUrgencia {
	
	private String textoIntervalo;
	private int indiceTabela;
	private int inicioIntervalo;
	private int finalIntervalo;
	private int quantidade;
	
	public IntervalosUrgencia(String textoIntervalo, int indiceIntervalo, int inicioIntervalo, int finalIntervalo)
	{
		this.setTextoIntervalo(textoIntervalo);
		this.setIndiceTabela(indiceIntervalo);
		this.setInicioIntervalo(inicioIntervalo);
		this.setFinalIntervalo(finalIntervalo);
		
		quantidade = 0;
	}

	public String getTextoIntervalo() {
		return textoIntervalo;
	}

	public void setTextoIntervalo(String textoIntervalo) {
		this.textoIntervalo = textoIntervalo;
	}

	public int getIndiceTabela() {
		return indiceTabela;
	}

	public void setIndiceTabela(int indiceTabela) {
		this.indiceTabela = indiceTabela;
	}

	public int getInicioIntervalo() {
		return inicioIntervalo;
	}

	public void setInicioIntervalo(int inicioIntervalo) {
		this.inicioIntervalo = inicioIntervalo;
	}

	public int getFinalIntervalo() {
		return finalIntervalo;
	}

	public void setFinalIntervalo(int finalIntervalo) {
		this.finalIntervalo = finalIntervalo;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public void incrementarQuantidade()
	{
		quantidade++;
	}

}
