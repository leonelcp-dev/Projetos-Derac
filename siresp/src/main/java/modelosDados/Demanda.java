package modelosDados;


public class Demanda {

    @ExcelColumn(header = "Competência", pattern = "mmm/yyyy")
    private String competencia;

    @ExcelColumn(header = "Procedimentos (padronizado)")
    private String procedimento;

    @ExcelColumn(header = "Novas Solicitações")
    private String novasSolicitacoes;

    @ExcelColumn(header = "Oferta Total")
    private String ofertaTotal;

   	@ExcelColumn(header = "Demanda Reprimida")
    private String demandaReprimida;
    
    @ExcelColumn(header = "Tempo de Espera")
    private String tempoDeEspera;
    
    @ExcelColumn(header = "Mais Velho na Fila (em dias)")
    private String maisVelhoNaFila;
    
    private int linhaExcel;

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public String getNovasSolicitacoes() {
		return novasSolicitacoes;
	}

	public void setNovasSolicitacoes(String novasSolicitacoes) {
		this.novasSolicitacoes = novasSolicitacoes;
	}

	public String getDemandaReprimida() {
		return demandaReprimida;
	}

	public void setDemandaReprimida(String demandaReprimida) {
		this.demandaReprimida = demandaReprimida;
	}
	
	public String getOfertaTotal() {
		return ofertaTotal;
	}

	public void setOfertaTotal(String ofertaTotal) {
		this.ofertaTotal = ofertaTotal;
	}

	public String getTempoDeEspera() {
		return tempoDeEspera;
	}

	public void setTempoDeEspera(String tempoDeEspera) {
		this.tempoDeEspera = tempoDeEspera;
	}
	
	public String getMaisVelhoNaFila() {
		return maisVelhoNaFila;
	}

	public void setMaisVelhoNaFila(String maisVelhoNaFila) {
		this.maisVelhoNaFila = maisVelhoNaFila;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}

}
