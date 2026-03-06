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
		private ArrayList<PerfilSIRESP> perfis;
		
		public Usuario()
		{
			setPerfis(new ArrayList<PerfilSIRESP>());
		}

		public UsuarioSIRESP getUsuario() {
			return usuario;
		}

		public void setUsuario(UsuarioSIRESP usuarios) {
			this.usuario = usuarios;
		}

		public ArrayList<PerfilSIRESP> getPerfis() {
			return perfis;
		}

		public void setPerfis(ArrayList<PerfilSIRESP> perfis) {
			this.perfis = perfis;
		}
	}
}
