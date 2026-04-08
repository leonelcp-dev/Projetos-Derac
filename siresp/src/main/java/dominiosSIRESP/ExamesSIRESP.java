package dominiosSIRESP;

import modelosDados.ExcelColumn;

public class ExamesSIRESP {

	@ExcelColumn(header = "ID_EXAME")
    private String idExame;

    @ExcelColumn(header = "COD_EXAME")
    private String codExame;
    
    @ExcelColumn(header = "NOME_EXAME")
    private String nomeExame;
    
    @ExcelColumn(header = "ID_SUBGRUPO")
    private String idSubGrupo;
    
    @ExcelColumn(header = "TIPO_TABELA")
    private String tipoTabela;
    
    @ExcelColumn(header = "TIPO")
    private String tipo;

	public String getIdExame() {
		return idExame;
	}

	public void setIdExame(String idExame) {
		this.idExame = idExame;
	}

	public String getCodExame() {
		return codExame;
	}

	public void setCodExame(String codExame) {
		this.codExame = codExame;
	}

	public String getNomeExame() {
		return nomeExame;
	}

	public void setNomeExame(String nomeExame) {
		this.nomeExame = nomeExame;
	}

	public String getIdSubGrupo() {
		return idSubGrupo;
	}

	public void setIdSubGrupo(String idSubGrupo) {
		this.idSubGrupo = idSubGrupo;
	}

	public String getTipoTabela() {
		return tipoTabela;
	}

	public void setTipoTabela(String tipoTabela) {
		this.tipoTabela = tipoTabela;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
    
}
