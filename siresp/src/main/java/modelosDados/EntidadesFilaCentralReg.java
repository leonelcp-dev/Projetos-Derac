package modelosDados;

import java.util.ArrayList;

public class EntidadesFilaCentralReg {

	private String Unidade;
	private String NomeArquivo;
	private String Distrito;
	private String nomeSIRESP;
	private ArrayList<UsuarioFilaCentralReg> pacientes;
	private ArrayList<String> outrosNomes;
	
	public EntidadesFilaCentralReg(String unidade, String distrito, String escritoComo)
	{
		this.Unidade = unidade;
		this.Distrito = distrito;
		this.NomeArquivo = unidade.replaceAll("[\\\\/:*?\"<>|]", "_") + ".xlsx";
		setPacientes(new ArrayList<UsuarioFilaCentralReg>());
		
		outrosNomes = new ArrayList<String>();
		if(!escritoComo.equals(""))
		{
			String[] nomes = escritoComo.split(";");
			
			for(String nome : nomes)
				outrosNomes.add(nome);
		}
			
	}
	
	public String getNomeArquivo() {
		return NomeArquivo;
	}
	
	public void setNomeArquivo(String nomeArquivo) {
		NomeArquivo = nomeArquivo;
	}

	public String getUnidade() {
		return Unidade;
	}

	public void setUnidade(String unidade) {
		Unidade = unidade;
	}

	public String getDistrito() {
		return Distrito;
	}

	public void setDistrito(String distrito) {
		Distrito = distrito;
	}

	public ArrayList<UsuarioFilaCentralReg> getPacientes() {
		return pacientes;
	}

	public void setPacientes(ArrayList<UsuarioFilaCentralReg> pacientes) {
		this.pacientes = pacientes;
	}

	public ArrayList<String> getOutrosNomes() {
		return outrosNomes;
	}

	public void setOutrosNomes(ArrayList<String> outrosNomes) {
		this.outrosNomes = outrosNomes;
	}

	public String getNomeSIRESP() {
		return nomeSIRESP;
	}

	public void setNomeSIRESP(String nomeSIRESP) {
		this.nomeSIRESP = nomeSIRESP;
	}
	
}
