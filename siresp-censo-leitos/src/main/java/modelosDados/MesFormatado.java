package modelosDados;

public class MesFormatado {
	
	private String mesNumero;
	private String mesDescricao;
	
	public MesFormatado(String mesNumero, String mesDescricao)
	{
		this.mesNumero = mesNumero;
		this.mesDescricao = mesDescricao;
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

}
