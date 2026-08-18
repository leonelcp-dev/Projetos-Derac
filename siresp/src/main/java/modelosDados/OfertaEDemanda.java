package modelosDados;


public class OfertaEDemanda {

    @ExcelColumn(header = "Unidade")
    private String unidade;
    
    @ExcelColumn(header = "CNES")
    private String cnes;

    @ExcelColumn(header = "Vínculo")
    private String vinculo;

    @ExcelColumn(header = "Competência", pattern = "mmm/yyyy")
    private String competencia;
    
    private String competenciaOrdenacao;

    @ExcelColumn(header = "Tipo de oferta")
    private String tipoDeOferta;

    @ExcelColumn(header = "Procedimentos (padronizado)")
    private String procedimento;

    @ExcelColumn(header = "Nomenclatura SIRESP")
    private String especialidade;

    @ExcelColumn(header = "Classificação")
    private String classificacao;

    @ExcelColumn(header = "Ofertas Previstas")
    private String ofertasPrevistas;
    
    @ExcelColumn(header = "Novas Solicitações (mensais)")
    private String novasSolicitacoes;

    @ExcelColumn(header = "Oferta Disponível")
    private String ofertaDisponivel;

    @ExcelColumn(header = "Oferta SIRESP")
    private String ofertasSIRESP;
    
    @ExcelColumn(header = "Oferta Bloqueada")
    private String ofertaBloqueada;
    
    @ExcelColumn(header = "Agendamento Total")
    private String agendamentoTotal;   
    
    @ExcelColumn(header = "Agendamento Cota")
    private String agendamentoCota;

    @ExcelColumn(header = "Agendamento Bolsao")
    private String agendamentoBolsao;
    
   	@ExcelColumn(header = "Agendamento Não Distribuída")
    private String agendamentoNaoDistribuido;
    
    @ExcelColumn(header = "Agendamento Extra")
    private String agendamentoExtra;
    
    @ExcelColumn(header = "Recepção Atendido")
    private String recepcaoAtendido;
    
   	@ExcelColumn(header = "Recepção Ausente")
    private String recepcaoAusente;
    
    @ExcelColumn(header = "Recepção Ausente Calculado")
    private String recepcaoAusenteCalculado;
    
    @ExcelColumn(header = "Recepção Desistência")
    private String recepcaoDesistencia;
    
   	@ExcelColumn(header = "Recepção Dispensado")
    private String recepcaoDispensado;
    
    @ExcelColumn(header = "Recepção Não Informado")
    private String recepcaoNaoInformado;
    
    @ExcelColumn(header = "Taxa de Agendamento")
    private String taxaAgendamento;
    
    private String perdaPrimaria;
    
    @ExcelColumn(header = "Taxa de Perda Primária")
    private String taxaPerdaPrimaria;
    
    @ExcelColumn(header = "Taxa Atendido")
    private String taxaAtendido;
    
   	@ExcelColumn(header = "Taxa Ausente")
    private String taxaAusente;
    
    @ExcelColumn(header = "Taxa Desistência")
    private String taxaDesistencia;
    
    @ExcelColumn(header = "Taxa Dispensado")
    private String taxaDispensado;
    
    @ExcelColumn(header = "Taxa Não Informado")
    private String taxaNaoInformado;
    
   	@ExcelColumn(header = "Demanda Reprimida do dia")
    private String demandaReprimida;
    
    @ExcelColumn(header = "Tempo de Espera")
    private String tempoDeEspera;
    
    @ExcelColumn(header = "Recepção Fechada")
    private String recepcaoFechada;
    
    @ExcelColumn(header = "Maior tempo de espera em dias")
    private String maisVelhoNaFila;
    
    private String observacao;

    @ExcelColumn(header = "Atual")
    private String diferencaDeOferta;
    
    @ExcelColumn(header = "Anterior")
    private String diferencaDeOfertaAnterior;
    
    @ExcelColumn(header = "Anterior 2")
    private String diferencaDeOfertaAnterior2;
    
    private int linhaExcel;

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	
	public String getCNES() {
		return cnes;
	}

	public void setCNES(String cnes) {
		this.cnes = cnes;
	}

	public String getVinculo() {
		return vinculo;
	}

	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public String getTipoDeOferta() {
		return tipoDeOferta;
	}

	public void setTipoDeOferta(String tipoDeOferta) {
		this.tipoDeOferta = tipoDeOferta;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getOfertasPrevistas() {
		return ofertasPrevistas;
	}

	public void setOfertasPrevistas(String ofertasPrevistas) {
		this.ofertasPrevistas = ofertasPrevistas;
	}

	public String getNovasSolicitacoes() {
		return novasSolicitacoes;
	}

	public void setNovasSolicitacoes(String novasSolicitacoes) {
		this.novasSolicitacoes = novasSolicitacoes;
	}

	public String getOfertaBloqueada() {
		return ofertaBloqueada;
	}

	public void setOfertaBloqueada(String ofertaBloqueada) {
		this.ofertaBloqueada = ofertaBloqueada;
	}

	public String getOfertaDisponivel() {
		return ofertaDisponivel;
	}

	public void setOfertaDisponivel(String ofertaDisponivel) {
		this.ofertaDisponivel = ofertaDisponivel;
	}

	public String getAgendamentoCota() {
		return agendamentoCota;
	}

	public void setAgendamentoCota(String agendamentoCota) {
		this.agendamentoCota = agendamentoCota;
	}

	public String getAgendamentoTotal() {
		return agendamentoTotal;
	}

	public void setAgendamentoTotal(String agendamentoTotal) {
		this.agendamentoTotal = agendamentoTotal;
	}

	public String getAgendamentoBolsao() {
		return agendamentoBolsao;
	}

	public void setAgendamentoBolsao(String agendamentoBolsao) {
		this.agendamentoBolsao = agendamentoBolsao;
	}

	public String getAgendamentoNaoDistribuido() {
		return agendamentoNaoDistribuido;
	}

	public void setAgendamentoNaoDistribuido(String agendamentoNaoDistribuido) {
		this.agendamentoNaoDistribuido = agendamentoNaoDistribuido;
	}

	public String getAgendamentoExtra() {
		return agendamentoExtra;
	}

	public void setAgendamentoExtra(String agendamentoExtra) {
		this.agendamentoExtra = agendamentoExtra;
	}

	public String getRecepcaoAtendido() {
		return recepcaoAtendido;
	}

	public void setRecepcaoAtendido(String recepcaoAtendido) {
		this.recepcaoAtendido = recepcaoAtendido;
	}

	public String getRecepcaoAusente() {
		return recepcaoAusente;
	}

	public void setRecepcaoAusente(String recepcaoAusente) {
		this.recepcaoAusente = recepcaoAusente;
	}

	public String getRecepcaoAusenteCalculado() {
		return recepcaoAusenteCalculado;
	}

	public void setRecepcaoAusenteCalculado(String recepcaoAusenteCalculado) {
		this.recepcaoAusenteCalculado = recepcaoAusenteCalculado;
	}

	public String getRecepcaoDesistencia() {
		return recepcaoDesistencia;
	}

	public void setRecepcaoDesistencia(String recepcaoDesistencia) {
		this.recepcaoDesistencia = recepcaoDesistencia;
	}

	public String getRecepcaoDispensado() {
		return recepcaoDispensado;
	}

	public void setRecepcaoDispensado(String recepcaoDispensado) {
		this.recepcaoDispensado = recepcaoDispensado;
	}

	public String getRecepcaoNaoInformado() {
		return recepcaoNaoInformado;
	}

	public void setRecepcaoNaoInformado(String recepcaoNaoInformado) {
		this.recepcaoNaoInformado = recepcaoNaoInformado;
	}

	public String getTaxaAtendido() {
		return taxaAtendido;
	}

	public void setTaxaAtendido(String taxaAtendido) {
		this.taxaAtendido = taxaAtendido;
	}

	public String getTaxaAusente() {
		return taxaAusente;
	}

	public void setTaxaAusente(String taxaAusente) {
		this.taxaAusente = taxaAusente;
	}

	public String getTaxaDesistencia() {
		return taxaDesistencia;
	}

	public void setTaxaDesistencia(String taxaDesistencia) {
		this.taxaDesistencia = taxaDesistencia;
	}

	public String getTaxaDispensado() {
		return taxaDispensado;
	}

	public void setTaxaDispensado(String taxaDispensado) {
		this.taxaDispensado = taxaDispensado;
	}
	
	public String getTaxaNaoInformado() {
		return taxaNaoInformado;
	}

	public void setTaxaNaoInformado(String taxaNaoInformado) {
		this.taxaNaoInformado = taxaNaoInformado;
	}

	public String getDemandaReprimida() {
		return demandaReprimida;
	}

	public void setDemandaReprimida(String demandaReprimida) {
		this.demandaReprimida = demandaReprimida;
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

	public String getRecepcaoFechada() {
		return recepcaoFechada;
	}

	public void setRecepcaoFechada(String recepcaoFechada) {
		this.recepcaoFechada = recepcaoFechada;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public int getLinhaExcel() {
		return linhaExcel;
	}

	public void setLinhaExcel(int linhaExcel) {
		this.linhaExcel = linhaExcel;
	}

	public String getDiferencaDeOferta() {
		return diferencaDeOferta;
	}

	public void setDiferencaDeOferta(String diferencaDeOferta) {
		this.diferencaDeOferta = diferencaDeOferta;
	}

	public String getCompetenciaOrdenacao() {
		return competenciaOrdenacao;
	}

	public void setCompetenciaOrdenacao(String competenciaOrdenacao) {
		this.competenciaOrdenacao = competenciaOrdenacao;
	}

	public String getOfertasSIRESP() {
		return ofertasSIRESP;
	}

	public void setOfertasSIRESP(String ofertasSIRESP) {
		this.ofertasSIRESP = ofertasSIRESP;
	}

	public String getTaxaAgendamento() {
		return taxaAgendamento;
	}

	public void setTaxaAgendamento(String taxaAgendamento) {
		this.taxaAgendamento = taxaAgendamento;
	}

	public String getTaxaPerdaPrimaria() {
		return taxaPerdaPrimaria;
	}

	public void setTaxaPerdaPrimaria(String taxaPerdaPrimaria) {
		this.taxaPerdaPrimaria = taxaPerdaPrimaria;
	}

	public String getPerdaPrimaria() {
		return perdaPrimaria;
	}

	public void setPerdaPrimaria(String perdaPrimaria) {
		this.perdaPrimaria = perdaPrimaria;
	}

	public String getDiferencaDeOfertaAnterior() {
		return diferencaDeOfertaAnterior;
	}

	public void setDiferencaDeOfertaAnterior(String diferencaDeOfertaAnterior) {
		this.diferencaDeOfertaAnterior = diferencaDeOfertaAnterior;
	}

	public String getDiferencaDeOfertaAnterior2() {
		return diferencaDeOfertaAnterior2;
	}

	public void setDiferencaDeOfertaAnterior2(String diferencaDeOfertaAnterior2) {
		this.diferencaDeOfertaAnterior2 = diferencaDeOfertaAnterior2;
	}

}
