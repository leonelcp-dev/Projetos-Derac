package modelosDados;


public class RelacaoOfertasEmBloqueio {

    @ExcelColumn(header = "Unidade")
    private String unidade;

    @ExcelColumn(header = "Tipo de Oferta")
    private String tipoDeOferta;

    @ExcelColumn(header = "Grupo")
    private String grupo;

    @ExcelColumn(header = "Equipamento")
    private String equipamento;

    public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	public String getTipoDeOferta() {
		return tipoDeOferta;
	}

	public void setTipoDeOferta(String tipoDeOferta) {
		this.tipoDeOferta = tipoDeOferta;
	}
	
	public String getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}

}
