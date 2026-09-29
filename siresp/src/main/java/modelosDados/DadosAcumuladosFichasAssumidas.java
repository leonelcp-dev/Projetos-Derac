package modelosDados;


public class DadosAcumuladosFichasAssumidas {

    private int assumidos;
    private int encaminhados;
    private int regulados;
    private int pendentes;

    public DadosAcumuladosFichasAssumidas()
    {
		assumidos = 0;
		encaminhados = 0;
		regulados = 0;
		pendentes = 0;
    }

	public int getAssumidos() {
		return assumidos;
	}

	public void setAssumidos(int assumidos) {
		this.assumidos = assumidos;
	}

	public int getEncaminhados() {
		return encaminhados;
	}

	public void setEncaminhados(int encaminhados) {
		this.encaminhados = encaminhados;
	}

	public int getPendentes() {
		return pendentes;
	}

	public void setPendentes(int pendentes) {
		this.pendentes = pendentes;
	}

	public int getRegulados() {
		return regulados;
	}

	public void setRegulados(int regulados) {
		this.regulados = regulados;
	}
    
 


}
