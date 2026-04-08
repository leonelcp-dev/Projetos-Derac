package dominiosSIRESP;

import modelosDados.ExcelColumn;

public class EspecialidadesSIRESP {

	@ExcelColumn(header = "ID_ESPECIALIDADE")
    private String idEspecialidade;

    @ExcelColumn(header = "ID_SUBGRUPO")
    private String idSubGrupo;
    
    @ExcelColumn(header = "NOME_ESPECIALIDADE")
    private String nomeEspecialidade;

	public String getIdEspecialidade() {
		return idEspecialidade;
	}

	public void setIdEspecialidade(String idEspecialidade) {
		this.idEspecialidade = idEspecialidade;
	}

	public String getIdSubGrupo() {
		return idSubGrupo;
	}

	public void setIdSubGrupo(String idSubGrupo) {
		this.idSubGrupo = idSubGrupo;
	}

	public String getNomeEspecialidade() {
		return nomeEspecialidade;
	}

	public void setNomeEspecialidade(String nomeEspecialidade) {
		this.nomeEspecialidade = nomeEspecialidade;
	}


    
}
