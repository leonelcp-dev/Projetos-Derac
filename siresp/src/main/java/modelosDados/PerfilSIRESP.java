package modelosDados;

public class PerfilSIRESP {
	
	private String modulo;
	private String perfil;
	private String comRestricaoDeHorario;
	private int linhaExcel;
	
	public PerfilSIRESP(String modulo, String perfil, String comRestricaoDeHorario, int linhaExcel)
	{
		this.modulo = modulo;
		this.perfil = perfil;
		this.comRestricaoDeHorario = comRestricaoDeHorario;
		this.setLinhaExcel(linhaExcel);
	}
	
	public String getModulo() {
		return modulo;
	}
	
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getComRestricaoDeHorario() {
		return comRestricaoDeHorario;
	}

	public void setComRestricaoDeHorario(String comRestricaoDeHorario) {
		this.comRestricaoDeHorario = comRestricaoDeHorario;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}
	
	

}
