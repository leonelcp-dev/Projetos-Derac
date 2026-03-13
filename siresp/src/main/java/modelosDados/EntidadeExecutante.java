package modelosDados;

public class EntidadeExecutante {
	
	private String cnes;
	private String vinculo;
	private String unidade;
	
	public EntidadeExecutante(String cnes, String vinculo, String unidade)
	{
		this.cnes = cnes;
		this.vinculo = vinculo;
		this.unidade = unidade;
	}
	
	public String getVinculo() {
		return vinculo;
	}
	
	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
	}

	public String getCnes() {
		return cnes;
	}

	public void setCnes(String cnes) {
		this.cnes = cnes;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	
	

}
