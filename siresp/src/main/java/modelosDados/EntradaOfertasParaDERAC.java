package modelosDados;


public class EntradaOfertasParaDERAC {

	@ExcelColumn(header = "ESPECIALIDADES")
    private String especialidades;

    @ExcelColumn(header = "PROCEDIMENTOS (nomenclatura padronizada)")
    private String procedimentos;

    @ExcelColumn(header = "Procedimentos+Executante")
    private String procedimentosExecutante;
    
    @ExcelColumn(header = "Agenda")
    private String agenda;
    
    @ExcelColumn(header = "Tipo")
    private String tipo;
    
    @ExcelColumn(header = "Executante")
    private String executante;
    
    @ExcelColumn(header = "Oferta prevista")
    private String ofertasParaDERAC;
    
    @ExcelColumn(header = "Plano de trabalho (documento SEI)")
    private String planoDeTrabalho;
    
    @ExcelColumn(header = "Mês de referência")
    private String mesDeReferencia;

	public String getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(String especialidades) {
		this.especialidades = especialidades;
	}

	public String getProcedimentos() {
		return procedimentos;
	}

	public void setProcedimentos(String procedimentos) {
		this.procedimentos = procedimentos;
	}

	public String getProcedimentosExecutante() {
		return procedimentosExecutante;
	}

	public void setProcedimentosExecutante(String procedimentosExecutante) {
		this.procedimentosExecutante = procedimentosExecutante;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}

	public String getOfertasParaDERAC() {
		return ofertasParaDERAC;
	}

	public void setOfertasParaDERAC(String ofertasParaDERAC) {
		this.ofertasParaDERAC = ofertasParaDERAC;
	}

	public String getPlanoDeTrabalho() {
		return planoDeTrabalho;
	}

	public void setPlanoDeTrabalho(String planoDeTrabalho) {
		this.planoDeTrabalho = planoDeTrabalho;
	}

	public String getMesDeReferencia() {
		return mesDeReferencia;
	}

	public void setMesDeReferencia(String mesDeReferencia) {
		this.mesDeReferencia = mesDeReferencia;
	}

}
