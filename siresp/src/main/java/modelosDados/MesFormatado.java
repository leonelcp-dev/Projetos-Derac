package modelosDados;

public class MesFormatado {
	
	private String mesNumero;
	private String mesDescricao;
	private String mesDescricaoSemAcentuacao;
	
	public MesFormatado(String mesNumero, String mesDescricao, String mesDescricaoSemAcentuacao)
	{
		this.mesNumero = mesNumero;
		this.mesDescricao = mesDescricao;
		this.mesDescricaoSemAcentuacao = mesDescricaoSemAcentuacao;
	}
	
	public String getMesNumero() {
		return mesNumero;
	}
	
	public void setMesNumero(String mesNumero) {
		this.mesNumero = mesNumero;
	}
	
	public String getMesDescricao() {
		return mesDescricao;
	}
	
	public void setMesDescricao(String mesDescricao) {
		this.mesDescricao = mesDescricao;
	}

	public String getMesDescricaoSemAcentuacao() {
		return mesDescricaoSemAcentuacao;
	}

	public void setMesDescricaoSemAcentuacao(String mesDescricaoSemAcentuacao) {
		this.mesDescricaoSemAcentuacao = mesDescricaoSemAcentuacao;
	}

}
