package dadosGerais;

public enum IdentificadoresPastasCompartilhadasCDIDR {

	REFERENCIA_PASTAS_AMBULATORIAL(0, "Ambulatorial"),
	REFERENCIA_PASTAS_DEMANDA_REPRIMIDA_CDIDR(1, "Demanda Reprimida CDIDR"),
	
	TEXTO_PASTA_NOVAS_SOLICITACOES_CDR(0, "NOVAS ENTRADAS CDR"),
	
	MASCARA_NOMES_DINAMICOS(12, "######"),
	
	TESTE_PASTA_FILAS_NOMINAIS(0, "Ambulatorial"),
	TESTE_PASTA_ABSENTEISMO(1, "Absenteismo"),
	TESTE_PASTA_FILA_UNICA_PARA_IMPORTACAO(2, "Fila Unica"),
	TESTE_PASTA_ARQUIVOS_PARA_IMPORTACAO(3, "Importacao"),
	TESTE_PASTA_CENSO_DIARIO(4, "Leitos"),
	TESTE_PASTA_LOGINS_SIRESP(5, "Logins SIRESP"),
	TESTE_PASTA_RELATORIO_OFERTA_E_DEMANDA(6, "Oferta e Demanda\\temp"),
	TESTE_PASTA_ARQUIVOS_ORIGINAIS_PRODUCAO(7, "Oferta e Demanda\\Arquivos Originais"),
	TESTE_PASTA_ARQUIVOS_CDR_NOVAS_SOLICITACOES(8, "Oferta e Demanda\\ENTRADAS MENSAIS\\DADOS BRUTOS"),
	TESTE_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA(9, "Oferta e Demanda\\ENTRADAS MENSAIS\\Demanda Reprimida"),
	TESTE_PASTA_ARQUIVOS_NOVAS_SOLICITACOES_CONSOLIDADA(10, "Oferta e Demanda\\ENTRADAS MENSAIS"),
	TESTE_PASTA_ARQUIVOS_REGULADA_NOVAS_SOLICITACOES(11, "Oferta e Demanda\\ENTRADAS MENSAIS\\Regulada - Consolidados Mensais"),
	
	TESTE_CAMINHO_ARQUIVO_NOMENCLATURAS(12, "Oferta e Demanda\\BANCO DE DADOS - 2025.xlsx"),
	
	TESTE_NOME_ARQUIVO_DEMANDA_REPRIMIDA_VAZIO(12, "Demanda Reprimida - VAZIO.xlsx"),
	TESTE_NOME_ARQUIVO_DEMANDA_REPRIMIDA_CONSOLIDADO_DIARIO(13, "Demanda Reprimida (dados coletados em " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ").xlsx"),
	TESTE_NOME_ARQUIVO_UNIDADES_SOLICITANTES(13, "Oferta e Demanda\\unidadesSolicitantes.csv"),
	TESTE_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_CDR(13, "Oferta e Demanda\\ENTRADAS MENSAIS\\BD Demanda Reprimida - CDR.xlsx"),
	TESTE_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_REGULADA(13, "Oferta e Demanda\\ENTRADAS MENSAIS\\BD Demanda Reprimida - Regulada.xlsx"),
	TESTE_NOME_ARQUIVO_OFERTA_DEMANDA(13, "Oferta e Demanda\\temp\\ConsolidadoOfertaEDemanda-temp.xlsx"),
	TESTE_NOME_ARQUIVO_RELACOES_ESPECIALIDADES_BLOQUEIO(13, "Oferta e Demanda\\RelacaoEspecialidadesBloqueio.xlsx"),
	
	TESTE_PASTA_ARQUIVOS_OFERTAS_PARA_DERAC(13, "Oferta e Demanda\\FPO"),
	TESTE_NOME_ARQUIVO_UNIDADES_EXECUTANTES(13, "Oferta e Demanda\\unidadesExecutantes.csv"),
	TESTE_NOME_ARQUIVO_DE_PARA_ESPECIALIDADES(13, "Oferta e Demanda\\ENTRADAS MENSAIS\\Demanda Reprimida\\de-para Especialidades.xlsx"),
	TESTE_PASTA_ARQUIVOS_PARA_AUTOMATIZACAO(13, "Oferta e Demanda\\Arquivos Vazios"),
	TESTE_NOME_ARQUIVO_UNIDADES_DEMANDA_REPRIMIDA(13, "Ambulatorial\\entidadesParaDemandaReprimida.csv"),
	TESTE_PASTA_RELATORIO_OFERTA_E_DEMANDA_PARA_CDIDR(13, "Oferta e Demanda"),
	
	

	PROD_PASTA_FILAS_NOMINAIS(0, "FILAS NOMINAIS - UNIDADES"),
	PROD_PASTA_ABSENTEISMO(1, "03 - ABSENTEÍSMO POR UNIDADE SOLICITANTE"),
	PROD_PASTA_FILA_UNICA_PARA_IMPORTACAO(2, "Fila Unica"),
	PROD_PASTA_ARQUIVOS_PARA_IMPORTACAO(3, "Importacao"),
	PROD_PASTA_CENSO_DIARIO(4, "Leitos"),
	PROD_PASTA_LOGINS_SIRESP(5, "Logins SIRESP"),
	PROD_PASTA_RELATORIO_OFERTA_E_DEMANDA(6, "01 - BANCO DE DADOS\\Base para Automatizacao\\Oferta e Demanda"),
	PROD_PASTA_ARQUIVOS_ORIGINAIS_PRODUCAO(7, "01 - BANCO DE DADOS\\Oferta e Demanda\\Arquivos Originais"),
	PROD_PASTA_ARQUIVOS_CDR_NOVAS_SOLICITACOES(8, "06 - ENTRADAS MENSAIS DE SOLICITAÇÕES\\Dados CDR"),
	PROD_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA(9, ""),
	PROD_PASTA_ARQUIVOS_NOVAS_SOLICITACOES_CONSOLIDADA(10, "06 - ENTRADAS MENSAIS DE SOLICITAÇÕES"),
	PROD_PASTA_ARQUIVOS_REGULADA_NOVAS_SOLICITACOES(11, "06 - ENTRADAS MENSAIS DE SOLICITAÇÕES\\Dados Regulada"),
	
	PROD_CAMINHO_ARQUIVO_NOMENCLATURAS(12, "01 - BANCO DE DADOS\\Nomenclaturas\\BANCO DE DADOS.xlsx"),
	
	PROD_NOME_ARQUIVO_DEMANDA_REPRIMIDA_VAZIO(12, "Demanda Reprimida - VAZIO.xlsx"),
	PROD_NOME_ARQUIVO_DEMANDA_REPRIMIDA_CONSOLIDADO_DIARIO(13, "Demanda Reprimida (dados coletados em " + MASCARA_NOMES_DINAMICOS.getTextoIdentificador() + ").xlsx"),
	PROD_NOME_ARQUIVO_UNIDADES_SOLICITANTES(13, "01 - BANCO DE DADOS\\Base para Automatizacao\\Oferta e Demanda\\unidadesSolicitantes.csv"),
	PROD_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_CDR(13, "06 - ENTRADAS MENSAIS DE SOLICITAÇÕES\\BD Demanda Reprimida - CDR.xlsx"),
	PROD_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_REGULADA(13, "06 - ENTRADAS MENSAIS DE SOLICITAÇÕES\\BD Demanda Reprimida - Regulada.xlsx"),
	PROD_NOME_ARQUIVO_OFERTA_DEMANDA(13, "01 - BANCO DE DADOS\\Base para Automatizacao\\Oferta e Demanda\\ConsolidadoOfertaEDemanda-temp.xlsx"),
	PROD_NOME_ARQUIVO_RELACOES_ESPECIALIDADES_BLOQUEIO(13, "01 - BANCO DE DADOS\\Base para Automatizacao\\Oferta e Demanda\\RelacaoEspecialidadesBloqueio.xlsx"),
	
	PROD_PASTA_ARQUIVOS_OFERTAS_PARA_DERAC(13, "01 - BANCO DE DADOS\\Ofertas Previstas"),
	PROD_NOME_ARQUIVO_UNIDADES_EXECUTANTES(13, "01 - BANCO DE DADOS\\Base para Automatizacao\\Oferta e Demanda\\unidadesExecutantes.csv"),
	PROD_NOME_ARQUIVO_DE_PARA_ESPECIALIDADES(13, "01 - BANCO DE DADOS\\Base para Automatizacao\\Oferta e Demanda\\de-para Especialidades.xlsx"),
	PROD_PASTA_ARQUIVOS_PARA_AUTOMATIZACAO(13, "01 - BANCO DE DADOS\\Base para Automatizacao\\Oferta e Demanda"),
	PROD_NOME_ARQUIVO_UNIDADES_DEMANDA_REPRIMIDA(13, "01 - BANCO DE DADOS\\Base para Automatizacao\\Demanda Reprimida\\entidadesParaDemandaReprimida.csv"),
	PROD_PASTA_RELATORIO_OFERTA_E_DEMANDA_PARA_CDIDR(13, "01 - BANCO DE DADOS\\Oferta e Demanda"),
	
	TESTE(TESTE_PASTA_FILAS_NOMINAIS.getTextoIdentificador(),
			TESTE_PASTA_ABSENTEISMO.getTextoIdentificador(),
			TESTE_PASTA_FILA_UNICA_PARA_IMPORTACAO.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_PARA_IMPORTACAO.getTextoIdentificador(),
			TESTE_PASTA_CENSO_DIARIO.getTextoIdentificador(),
			TESTE_PASTA_LOGINS_SIRESP.getTextoIdentificador(),
			TESTE_PASTA_RELATORIO_OFERTA_E_DEMANDA.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_ORIGINAIS_PRODUCAO.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_CDR_NOVAS_SOLICITACOES.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_NOVAS_SOLICITACOES_CONSOLIDADA.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_REGULADA_NOVAS_SOLICITACOES.getTextoIdentificador(),
			TESTE_CAMINHO_ARQUIVO_NOMENCLATURAS.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_DEMANDA_REPRIMIDA_VAZIO.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_DEMANDA_REPRIMIDA_CONSOLIDADO_DIARIO.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_UNIDADES_SOLICITANTES.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_CDR.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_REGULADA.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_OFERTA_DEMANDA.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_RELACOES_ESPECIALIDADES_BLOQUEIO.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_OFERTAS_PARA_DERAC.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_UNIDADES_EXECUTANTES.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_DE_PARA_ESPECIALIDADES.getTextoIdentificador(),
			TESTE_PASTA_ARQUIVOS_PARA_AUTOMATIZACAO.getTextoIdentificador(),
			TESTE_NOME_ARQUIVO_UNIDADES_DEMANDA_REPRIMIDA.getTextoIdentificador(),
			TESTE_PASTA_RELATORIO_OFERTA_E_DEMANDA_PARA_CDIDR.getTextoIdentificador()),
	
	PRODUCAO(PROD_PASTA_FILAS_NOMINAIS.getTextoIdentificador(),
			PROD_PASTA_ABSENTEISMO.getTextoIdentificador(),
			PROD_PASTA_FILA_UNICA_PARA_IMPORTACAO.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_PARA_IMPORTACAO.getTextoIdentificador(),
			PROD_PASTA_CENSO_DIARIO.getTextoIdentificador(),
			PROD_PASTA_LOGINS_SIRESP.getTextoIdentificador(),
			PROD_PASTA_RELATORIO_OFERTA_E_DEMANDA.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_ORIGINAIS_PRODUCAO.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_CDR_NOVAS_SOLICITACOES.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_DEMANDA_REPRIMIDA.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_NOVAS_SOLICITACOES_CONSOLIDADA.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_REGULADA_NOVAS_SOLICITACOES.getTextoIdentificador(),
			PROD_CAMINHO_ARQUIVO_NOMENCLATURAS.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_DEMANDA_REPRIMIDA_VAZIO.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_DEMANDA_REPRIMIDA_CONSOLIDADO_DIARIO.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_UNIDADES_SOLICITANTES.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_CDR.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_BD_CONSOLIDADO_NOVAS_SOLICITACOES_REGULADA.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_OFERTA_DEMANDA.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_RELACOES_ESPECIALIDADES_BLOQUEIO.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_OFERTAS_PARA_DERAC.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_UNIDADES_EXECUTANTES.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_DE_PARA_ESPECIALIDADES.getTextoIdentificador(),
			PROD_PASTA_ARQUIVOS_PARA_AUTOMATIZACAO.getTextoIdentificador(),
			PROD_NOME_ARQUIVO_UNIDADES_DEMANDA_REPRIMIDA.getTextoIdentificador(),
			PROD_PASTA_RELATORIO_OFERTA_E_DEMANDA_PARA_CDIDR.getTextoIdentificador());
	
	
	
	private String pastaFilasNominais;
	private String pastaAbsenteismo;
	private String pastaFilaUnica;
	private String pastaArquivosParaImportacao;
	private String pastaCensoDiario;
	private String pastaParaLoginsSiresp;
	private String pastaRelatorioOfertaEDemanda;
	private String pastaArquivosOriginaisProducao;
	private String arquivosCDRNovasSolicitacoes;
	private String arquivosDemandaReprimida;
	private String arquivosNovasSolicitacoesConsolidada;
	private String arquivosReguladasNovasSolicitacoes;
	private String caminhoArquivoNomenclaturas;
	private String nomeArquivoDemandaReprimidaVazio;
	private String nomeArquivoDemandaReprimidaConsolidadoDiario;
	private String nomeArquivoUnidadesSolicitantes;
	private String nomeArquivoBDConsolidadoNovasSolicitacoesCDR;
	private String nomeArquivoBDConsolidadoNovasSolicitacoesRegulada;
	private String nomeArquivoOfertaDemanda;
	private String nomeArquivoRelacoesEspecialidadesBloqueio;
	private String pastaArquivosOfertasParaDERAC;
	private String nomeArquivoUnidadesExecutantes;
	private String nomeArquivoDeParaEspecialidades;
	private String pastaArquivosParaAutomatizacao;
	private String nomeArquivoUnidadesDemandaReprimida;
	private String pastaRelatorioOfertaEDemandaCDIDR;
	
	private int indice;
	private String textoIdentificador;
	
	IdentificadoresPastasCompartilhadasCDIDR(int indice, String textoIdentificador)
	{
		this.setIndice(indice);
		this.textoIdentificador = textoIdentificador;
	}
	
	IdentificadoresPastasCompartilhadasCDIDR(String pastaFilasNominais, String pastaAbsenteismo, String pastaFilaUnica, String pastaArquivosParaImportacao, 
										String pastaCensoDiario, String pastaParaLoginsSiresp, String pastaRelatorioOfertaEDemanda, 
										String pastaArquivosOriginaisProducao, String arquivosCDRNovasSolicitacoes, String arquivosDemandaReprimida, 
										String arquivosNovasSolicitacoesConsolidada, String arquivosReguladasNovasSolicitacoes, String caminhoArquivoNomenclaturas, 
										String nomeArquivoDemandaReprimidaVazio, String nomeArquivoDemandaReprimidaConsolidadoDiario, 
										String nomeArquivoUnidadesSolicitantes, String nomeArquivoBDConsolidadoNovasSolicitacoesCDR,
										String nomeArquivoBDConsolidadoNovasSolicitacoesRegulada, String nomeArquivoOfertaDemanda, 
										String nomeArquivoRelacoesEspecialidadesBloqueio, String pastaArquivosOfertasParaDERAC, String nomeArquivoUnidadesExecutantes,
										String nomeArquivoDeParaEspecialidades, String pastaArquivosParaAutomatizacao, String nomeArquivoUnidadesDemandaReprimida,
										String pastaRelatorioOfertaEDemandaCDIDR)
	{
		this.pastaFilasNominais = pastaFilasNominais;
		this.pastaAbsenteismo = pastaAbsenteismo;
		this.pastaFilaUnica = pastaFilaUnica;
		this.pastaArquivosParaImportacao = pastaArquivosParaImportacao;
		this.pastaCensoDiario = pastaCensoDiario;
		this.pastaParaLoginsSiresp = pastaParaLoginsSiresp;
		this.pastaRelatorioOfertaEDemanda = pastaRelatorioOfertaEDemanda;
		this.pastaArquivosOriginaisProducao = pastaArquivosOriginaisProducao;
		this.arquivosCDRNovasSolicitacoes = arquivosCDRNovasSolicitacoes;
		this.arquivosDemandaReprimida = arquivosDemandaReprimida;
		this.arquivosNovasSolicitacoesConsolidada = arquivosNovasSolicitacoesConsolidada;
		this.arquivosReguladasNovasSolicitacoes = arquivosReguladasNovasSolicitacoes;
		this.caminhoArquivoNomenclaturas = caminhoArquivoNomenclaturas;
		this.nomeArquivoDemandaReprimidaVazio = nomeArquivoDemandaReprimidaVazio;
		this.nomeArquivoDemandaReprimidaConsolidadoDiario = nomeArquivoDemandaReprimidaConsolidadoDiario;
		this.nomeArquivoUnidadesSolicitantes = nomeArquivoUnidadesSolicitantes;
		this.nomeArquivoBDConsolidadoNovasSolicitacoesCDR = nomeArquivoBDConsolidadoNovasSolicitacoesCDR;
		this.nomeArquivoBDConsolidadoNovasSolicitacoesRegulada = nomeArquivoBDConsolidadoNovasSolicitacoesRegulada;
		this.nomeArquivoOfertaDemanda = nomeArquivoOfertaDemanda;
		this.nomeArquivoRelacoesEspecialidadesBloqueio = nomeArquivoRelacoesEspecialidadesBloqueio;
		this.pastaArquivosOfertasParaDERAC = pastaArquivosOfertasParaDERAC;
		this.nomeArquivoUnidadesExecutantes = nomeArquivoUnidadesExecutantes;
		this.nomeArquivoDeParaEspecialidades = nomeArquivoDeParaEspecialidades;
		this.pastaArquivosParaAutomatizacao = pastaArquivosParaAutomatizacao;
		this.nomeArquivoUnidadesDemandaReprimida = nomeArquivoUnidadesDemandaReprimida;
		this.pastaRelatorioOfertaEDemandaCDIDR = pastaRelatorioOfertaEDemandaCDIDR;
	}

	public String getTextoIdentificador() {
		return textoIdentificador;
	}

	public void setTextoIdentificador(String textoIdentificador) {
		this.textoIdentificador = textoIdentificador;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public String getPastaFilasNominais() {
		return pastaFilasNominais;
	}

	public void setPastaFilasNominais(String pastaFilasNominais) {
		this.pastaFilasNominais = pastaFilasNominais;
	}

	public String getPastaFilaUnica() {
		return pastaFilaUnica;
	}

	public void setPastaFilaUnica(String pastaFilaUnica) {
		this.pastaFilaUnica = pastaFilaUnica;
	}

	public String getPastaAbsenteismo() {
		return pastaAbsenteismo;
	}

	public void setPastaAbsenteismo(String pastaAbsenteismo) {
		this.pastaAbsenteismo = pastaAbsenteismo;
	}

	public String getPastaArquivosParaImportacao() {
		return pastaArquivosParaImportacao;
	}

	public void setPastaArquivosParaImportacao(String pastaArquivosParaImportacao) {
		this.pastaArquivosParaImportacao = pastaArquivosParaImportacao;
	}

	public String getPastaCensoDiario() {
		return pastaCensoDiario;
	}

	public void setPastaCensoDiario(String pastaCensoDiario) {
		this.pastaCensoDiario = pastaCensoDiario;
	}

	public String getPastaRelatorioOfertaEDemanda() {
		return pastaRelatorioOfertaEDemanda;
	}

	public void setPastaRelatorioOfertaEDemanda(String pastaRelatorioOfertaEDemanda) {
		this.pastaRelatorioOfertaEDemanda = pastaRelatorioOfertaEDemanda;
	}

	public String getPastaParaLoginsSiresp() {
		return pastaParaLoginsSiresp;
	}

	public void setPastaParaLoginsSiresp(String pastaParaLoginsSiresp) {
		this.pastaParaLoginsSiresp = pastaParaLoginsSiresp;
	}

	public String getPastaArquivosOriginaisProducao() {
		return pastaArquivosOriginaisProducao;
	}

	public void setPastaArquivosOriginaisProducao(String pastaArquivosOriginaisProducao) {
		this.pastaArquivosOriginaisProducao = pastaArquivosOriginaisProducao;
	}

	public String getArquivosCDRNovasSolicitacoes() {
		return arquivosCDRNovasSolicitacoes;
	}

	public void setArquivosCDRNovasSolicitacoes(String arquivosCDRNovasSolicitacoes) {
		this.arquivosCDRNovasSolicitacoes = arquivosCDRNovasSolicitacoes;
	}

	public String getArquivosDemandaReprimida() {
		return arquivosDemandaReprimida;
	}

	public void setArquivosDemandaReprimida(String arquivosDemandaReprimida) {
		this.arquivosDemandaReprimida = arquivosDemandaReprimida;
	}

	public String getArquivosNovasSolicitacoesConsolidada() {
		return arquivosNovasSolicitacoesConsolidada;
	}

	public void setArquivosNovasSolicitacoesConsolidada(String arquivosNovasSolicitacoesConsolidada) {
		this.arquivosNovasSolicitacoesConsolidada = arquivosNovasSolicitacoesConsolidada;
	}

	public String getArquivosReguladasNovasSolicitacoes() {
		return arquivosReguladasNovasSolicitacoes;
	}

	public void setArquivosReguladasNovasSolicitacoes(String arquivosReguladasNovasSolicitacoes) {
		this.arquivosReguladasNovasSolicitacoes = arquivosReguladasNovasSolicitacoes;
	}

	public String getCaminhoArquivoNomenclaturas() {
		return caminhoArquivoNomenclaturas;
	}

	public void setCaminhoArquivoNomenclaturas(String caminhoArquivoNomenclaturas) {
		this.caminhoArquivoNomenclaturas = caminhoArquivoNomenclaturas;
	}

	public String getNomeArquivoDemandaReprimidaVazio() {
		return nomeArquivoDemandaReprimidaVazio;
	}

	public void setNomeArquivoDemandaReprimidaVazio(String nomeArquivoDemandaReprimidaVazio) {
		this.nomeArquivoDemandaReprimidaVazio = nomeArquivoDemandaReprimidaVazio;
	}

	public String getNomeArquivoDemandaReprimidaConsolidadoDiario() {
		return nomeArquivoDemandaReprimidaConsolidadoDiario;
	}

	public void setNomeArquivoDemandaReprimidaConsolidadoDiario(String nomeArquivoDemandaReprimidaConsolidadoDiario) {
		this.nomeArquivoDemandaReprimidaConsolidadoDiario = nomeArquivoDemandaReprimidaConsolidadoDiario;
	}
	
	public String getNomeArquivoUnidadesSolicitantes() {
		return nomeArquivoUnidadesSolicitantes;
	}

	public void setNomeArquivoUnidadesSolicitantes(String nomeArquivoUnidadesSolicitantes) {
		this.nomeArquivoUnidadesSolicitantes = nomeArquivoUnidadesSolicitantes;
	}

	public String getNomeArquivoBDConsolidadoNovasSolicitacoesCDR() {
		return nomeArquivoBDConsolidadoNovasSolicitacoesCDR;
	}

	public void setNomeArquivoBDConsolidadoNovasSolicitacoesCDR(String nomeArquivoBDConsolidadoNovasSolicitacoesCDR) {
		this.nomeArquivoBDConsolidadoNovasSolicitacoesCDR = nomeArquivoBDConsolidadoNovasSolicitacoesCDR;
	}

	public String getNomeArquivoBDConsolidadoNovasSolicitacoesRegulada() {
		return nomeArquivoBDConsolidadoNovasSolicitacoesRegulada;
	}

	public void setNomeArquivoBDConsolidadoNovasSolicitacoesRegulada(
			String nomeArquivoBDConsolidadoNovasSolicitacoesRegulada) {
		this.nomeArquivoBDConsolidadoNovasSolicitacoesRegulada = nomeArquivoBDConsolidadoNovasSolicitacoesRegulada;
	}

	public String getNomeArquivoOfertaDemanda() {
		return nomeArquivoOfertaDemanda;
	}

	public void setNomeArquivoOfertaDemanda(String nomeArquivoOfertaDemanda) {
		this.nomeArquivoOfertaDemanda = nomeArquivoOfertaDemanda;
	}

	public String getNomeArquivoRelacoesEspecialidadesBloqueio() {
		return nomeArquivoRelacoesEspecialidadesBloqueio;
	}

	public void setNomeArquivoRelacoesEspecialidadesBloqueio(String nomeArquivoRelacoesEspecialidadesBloqueio) {
		this.nomeArquivoRelacoesEspecialidadesBloqueio = nomeArquivoRelacoesEspecialidadesBloqueio;
	}

	public String getPastaArquivosOfertasParaDERAC() {
		return pastaArquivosOfertasParaDERAC;
	}

	public void setPastaArquivosOfertasParaDERAC(String pastaArquivosOfertasParaDERAC) {
		this.pastaArquivosOfertasParaDERAC = pastaArquivosOfertasParaDERAC;
	}

	public String getNomeArquivoUnidadesExecutantes() {
		return nomeArquivoUnidadesExecutantes;
	}

	public void setNomeArquivoUnidadesExecutantes(String nomeArquivoUnidadesExecutantes) {
		this.nomeArquivoUnidadesExecutantes = nomeArquivoUnidadesExecutantes;
	}

	public String getNomeArquivoDeParaEspecialidades() {
		return nomeArquivoDeParaEspecialidades;
	}

	public void setNomeArquivoDeParaEspecialidades(String nomeArquivoDeParaEspecialidades) {
		this.nomeArquivoDeParaEspecialidades = nomeArquivoDeParaEspecialidades;
	}

	public String getPastaArquivosParaAutomatizacao() {
		return pastaArquivosParaAutomatizacao;
	}

	public void setPastaArquivosParaAutomatizacao(String pastaArquivosParaAutomatizacao) {
		this.pastaArquivosParaAutomatizacao = pastaArquivosParaAutomatizacao;
	}

	public String getNomeArquivoUnidadesDemandaReprimida() {
		return nomeArquivoUnidadesDemandaReprimida;
	}

	public void setNomeArquivoUnidadesDemandaReprimida(String nomeArquivoUnidadesDemandaReprimida) {
		this.nomeArquivoUnidadesDemandaReprimida = nomeArquivoUnidadesDemandaReprimida;
	}

	public String getPastaRelatorioOfertaEDemandaCDIDR() {
		return pastaRelatorioOfertaEDemandaCDIDR;
	}

	public void setPastaRelatorioOfertaEDemandaCDIDR(String pastaRelatorioOfertaEDemandaCDIDR) {
		this.pastaRelatorioOfertaEDemandaCDIDR = pastaRelatorioOfertaEDemandaCDIDR;
	}
}
