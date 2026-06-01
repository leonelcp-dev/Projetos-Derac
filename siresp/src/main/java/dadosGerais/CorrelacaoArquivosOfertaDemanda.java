package dadosGerais;

import java.util.ArrayList;

import modelosDados.CorrelacaoColunasArquivos;
import modelosDados.CorrelacaoColunasOfertasDemandas;

public class CorrelacaoArquivosOfertaDemanda {

	/*
	 * Classe criada para correlacionar a posição das colunas dos tabelas acessadas a partir do SIRESP.
	 * As tabelas de produtividade são distintas para EXAME e para CONSULTA 
	 * 
	 *  Para acessar tabelas de entidades estaduais, acessar o menu
	 *  	Relatório > Produtividade > P01 - Produção Executante
	 *  
	 *  Para acessar tabelas das demais entidades, acessar o menu
	 *  	Relatório > Produtividade > P06 - Consolidado
	 *  
	 *  
	 *  Problema a resolver, algumas colunas do arquivo final é a composição de soma ou subtração de colunas da tabela encontrada no sistema.
	 *  
	 */
	
	public ArrayList<CorrelacaoColunasOfertasDemandas> obterCorrelacaoEntreArquivos(String consultaOuExame, String vinculo)
	{
		ArrayList<CorrelacaoColunasOfertasDemandas> correlacoes = new ArrayList<CorrelacaoColunasOfertasDemandas>();
		
		CorrelacaoColunasOfertasDemandas correlacao;
		
		if(vinculo.equals(IdentificadoresPaginaWebSIRESP.TEXTO_VINCULO_ESTADUAL.getTextoIdentificador()))
		{
			if(consultaOuExame.equals("Exame")) //Exame em unidade estadual
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_GRUPO_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getFormato()));
				
				//Agendamentos Total = Agendado Cota + Agendado Extra
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_AGENDADO_COTA.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_AGENDADO_EXTRA.getIndice());
				correlacoes.add(correlacao);
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_AGENDADO_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getIndice(), null, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getIndice(), null, ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_AGENDADO_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_PRESENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_AUSENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getFormato()));
				
				//Ausente Calculado = (Agendado Cota + Agendado Extra) - (Presente + Dispensado + Desistente)
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_AGENDADO_COTA.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_AGENDADO_EXTRA.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_PRESENTE.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_DISPENSADO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_DESISTENTE.getIndice());
				correlacoes.add(correlacao);
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_DESISTENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_DISPENSADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_NAO_INFORMADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getFormato()));
			}
			else if(consultaOuExame.equals("Consulta")) //Consulta em unidade estadual
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getFormato()));
				
				// Agendamento Total = Agendado Cota + Agendado Extra + Agendado Primeira Consulta
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_COTA.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_EXTRA.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_PRIMEIRA_CONSULTA_POR_BOLSAO.getIndice());
				correlacoes.add(correlacao);
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_PRIMEIRA_CONSULTA_POR_BOLSAO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_OFERTA_NAO_DISTRIBUIDA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_ATENDIDO_PRESENCIAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AUSENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getFormato()));
				
				//Ausente Calculado = (Agendado Cota + Agendado Extra + Agendado Primeira Consulta) - (Atendido Presencial + Dispensado + Desistente)
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_COTA.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_EXTRA.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_AGENDADO_PRIMEIRA_CONSULTA_POR_BOLSAO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_ATENDIDO_PRESENCIAL.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_DISPENSADO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_DESISTENTE.getIndice());
				correlacoes.add(correlacao);
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_DESISTENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_DISPENSADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_NAO_INFORMADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getFormato()));
			}
		}
		else
		{
			if(consultaOuExame.equals("Exame")) //exame em unidades não estaduais
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_GRUPO_DE_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getIndice(), ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_OFERTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getFormato()));
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getIndice(), ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getFormato()));
				
				//Agendamentos Cota = Agendamento Cota Interno + Agendamento Cota Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_COTA_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_COTA_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				//Agendamento Bolsao = Agendamento Bolsao Interno + Agendamento Bolsao Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_BOLSAO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_BOLSAO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				//Agendamento Nao Distribuido = Agendamento Nao Distribuido Interno + Agendamento Nao Distribuido Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				//Agendamento Extra = Agendamento Extra Interno + Agendamento Extra Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_EXTRA_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_EXTRA_EXTERNO.getIndice());
				correlacoes.add(correlacao);
								
				//Atendimentos Presente = Atendimentos Presente Interno + Atendimentos Presente Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				//Atendimento Ausente = Atendimento Ausente Interno + Atendimento Ausente Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_AUSENTE_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_AUSENTE_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				//Ausente Calculado = Agendamento Total - (Atendimento Presente Interno + Atendimento Presente Externo + Atendimento Dispensado Interno +
				//										   Atendimento Dispensado Externo + Atendimento Desistencia Interno + Atendimento Desistencia Externo)
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_TOTAL.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_INTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_EXTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_INTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_EXTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_INTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_EXTERNO.getIndice());				
				correlacoes.add(correlacao);
				
				//Desistencia = Atendimento Desistencia Interno + Atendimento Desistencia Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_EXTERNO.getIndice());
				correlacoes.add(correlacao);

				//Dispensado = Atendimento Dispensado Interno + Atendimento Dispensado Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				//Nao Informado = Atendimento Nao Informado Interno + Atendimento Nao Informado Externo
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
			}
			else if(consultaOuExame.equals("Consulta"))
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_OFERTA_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_DISPONIVEL.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_TOTAL_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_COTA_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_BOLSAO_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_EXTRA_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_PRESENCIAL_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_AUSENTE_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getFormato()));
				
				//Ausente Calculado = Agendamento Total Primeira Consulta - (Atendimento Presencial Primeira Consulta + Atendimento Dispensado Primeira Consulta +
				//															 Atendimento Desistencia Primeira Consulta)
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_TOTAL_PRIMEIRA_CONSULTA.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_PRESENCIAL_PRIMEIRA_CONSULTA.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_PRIMEIRA_CONSULTA.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_PRIMEIRA_CONSULTA.getIndice());
				correlacoes.add(correlacao);
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getFormato()));
			}
		}
		
		return correlacoes;
	}
	
}
