package modelosDados;


public class NomenclaturaPadronizada {

    @ExcelColumn(header = "INSERÇÃO")
    private String insercao;

    @ExcelColumn(header = "NOMENCLATURA")
    private String nomenclatura;

    @ExcelColumn(header = "FLUXO")
    private String fluxo;
    
    @ExcelColumn(header = "TIPO DE OFERTA")
    private String tipoDeOferta;

    public String getInsercao() {
		return insercao;
	}

	public void setInsercao(String insercao) {
		this.insercao = insercao;
	}

	public String getNomenclatura() {
		return nomenclatura;
	}

	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}
	
	public String getFluxo() {
		return fluxo;
	}

	public void setFluxo(String fluxo) {
		this.fluxo = fluxo;
	}

	public String getTipoDeOferta() {
		return tipoDeOferta;
	}

	public void setTipoDeOferta(String tipoDeOferta) {
		this.tipoDeOferta = tipoDeOferta;
	}
	
}
