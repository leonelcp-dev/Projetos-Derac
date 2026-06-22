package modelosDados;


public class DadosAcumuladosVagaZero {

    private int total;
    private int vagaZero;
    private int encaminhadoParaReferenciaPactuada;
    private int encaminhadoParaAvaliacaoDeComplexidadeAdequada;
    private int encaminhadoAutomaticamenteParaReferenciaPactuada;

    public DadosAcumuladosVagaZero()
    {
         total = 0;
         vagaZero = 0;
         encaminhadoParaReferenciaPactuada = 0;
         encaminhadoParaAvaliacaoDeComplexidadeAdequada = 0;
         encaminhadoAutomaticamenteParaReferenciaPactuada = 0;
    }
    
    public void incrementarTotal()
    {
    	total++;
    }
    
    public void incrementarVagaZero()
    {
    	vagaZero++;
    }
    
    public void incrementarEncaminhadoParaReferenciaPactuada()
    {
    	encaminhadoParaReferenciaPactuada++;
    }
    
    public void incrementarEncaminhadoParaAvaliacaoDeComplexidadeAdequada()
    {
    	encaminhadoParaAvaliacaoDeComplexidadeAdequada++;
    }
    
    public void incrementarEncaminhadoAutomaticamenteParaReferenciaPactuada()
    {
    	encaminhadoAutomaticamenteParaReferenciaPactuada++;
    }
    
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getEncaminhadoParaReferenciaPactuada() {
		return encaminhadoParaReferenciaPactuada;
	}

	public void setEncaminhadoParaReferenciaPactuada(int encaminhadoParaReferenciaPactuada) {
		this.encaminhadoParaReferenciaPactuada = encaminhadoParaReferenciaPactuada;
	}

	public int getEncaminhadoParaAvaliacaoDeComplexidadeAdequada() {
		return encaminhadoParaAvaliacaoDeComplexidadeAdequada;
	}

	public void setEncaminhadoParaAvaliacaoDeComplexidadeAdequada(int encaminhadoParaAvaliacaoDeComplexidadeAdequada) {
		this.encaminhadoParaAvaliacaoDeComplexidadeAdequada = encaminhadoParaAvaliacaoDeComplexidadeAdequada;
	}

	public int getEncaminhadoAutomaticamenteParaReferenciaPactuada() {
		return encaminhadoAutomaticamenteParaReferenciaPactuada;
	}

	public void setEncaminhadoAutomaticamenteParaReferenciaPactuada(int encaminhadoAutomaticamenteParaReferenciaPactuada) {
		this.encaminhadoAutomaticamenteParaReferenciaPactuada = encaminhadoAutomaticamenteParaReferenciaPactuada;
	}
	
	public int getVagaZero() {
		return vagaZero;
	}

	public void setVagaZero(int vagaZero) {
		this.vagaZero = vagaZero;
	}


}
