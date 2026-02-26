package modelosDados;

import java.util.ArrayList;

public class PerfisUsuariosSIRESP {

	private String unidade;
	private ArrayList<Usuario> usuarios;
	
	public PerfisUsuariosSIRESP()
	{
		usuarios = new ArrayList<Usuario>();
	}
	
	public String getUnidade() {
		return unidade;
	}
	
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public void novoUsuario()
	{
		usuarios.add(new Usuario());
	}
	
	
	public class Usuario
	{
		private UsuarioSIRESP usuario;
		private ArrayList<String> perfis;
		
		public Usuario()
		{
			setPerfis(new ArrayList<String>());
		}

		public UsuarioSIRESP getUsuario() {
			return usuario;
		}

		public void setUsuario(UsuarioSIRESP usuarios) {
			this.usuario = usuarios;
		}

		public ArrayList<String> getPerfis() {
			return perfis;
		}

		public void setPerfis(ArrayList<String> perfis) {
			this.perfis = perfis;
		}
	}
}
