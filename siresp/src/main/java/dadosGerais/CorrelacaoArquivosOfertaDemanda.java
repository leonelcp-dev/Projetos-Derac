package dadosGerais;

import java.util.ArrayList;

import modelosDados.CorrelacaoColunasArquivos;
import modelosDados.CorrelacaoColunasOfertasDemandas;

public class CorrelacaoArquivosOfertaDemanda {

	public ArrayList<CorrelacaoColunasOfertasDemandas> obterCorrelacaoEntreArquivos(String consultaOuExame, String vinculo)
	{
		ArrayList<CorrelacaoColunasOfertasDemandas> correlacoes = new ArrayList<CorrelacaoColunasOfertasDemandas>();
		
		CorrelacaoColunasOfertasDemandas correlacao;
		
		if(vinculo.equals(IdentificadoresPaginaWebSIRESP.TEXTO_VINCULO_ESTADUAL.getTextoIdentificador()))
		{
			if(consultaOuExame.equals("Exame"))
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_GRUPO_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosTabelaProducaoExecutanteExames.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getFormato()));
				
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
			else if(consultaOuExame.equals("Consulta"))
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosTabelaProducaoExecutanteConsultas.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getFormato()));
				
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
			if(consultaOuExame.equals("Exame"))
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_GRUPO_DE_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_OFERTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getFormato()));
				
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getIndice(), ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_TOTAL.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getFormato()));
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_COTA_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_COTA_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_BOLSAO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_BOLSAO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_EXTRA_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_EXTRA_EXTERNO.getIndice());
				correlacoes.add(correlacao);
								
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_AUSENTE_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_AUSENTE_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
			
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE_CALCULADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_AGENDAMENTO_TOTAL.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_INTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_PRESENTE_EXTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_INTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_EXTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_INTERNO.getIndice());
				correlacao.getColunasSubtracao().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_EXTERNO.getIndice());				
				correlacoes.add(correlacao);
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DESISTENCIA_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_DISPENSADO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
				
				correlacao = new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_DISPENSADO.getFormato());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_INTERNO.getIndice());
				correlacao.getColunaSIRESP().add(ParametrosTabelaProducaoConsolidadoExames.INDICE_COLUNA_ATENDIMENTO_NAO_INFORMADO_EXTERNO.getIndice());
				correlacoes.add(correlacao);
			}
			else if(consultaOuExame.equals("Consulta"))
			{
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ESPECIALIDADE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ESPECIALIDADE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_OFERTA_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_OFERTA_TOTAL.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_TOTAL_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_TOTAL.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_COTA_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_COTA.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_BOLSAO_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_BOLSAO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_NAO_DISTRIBUIDO_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_EXTRA.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_AGENDAMENTO_EXTRA_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_AGENDAMENTOS_NAO_DISTRIBUIDO.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_PRESENCIAL_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_PRESENTE.getFormato()));
				correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getIndice(), ParametrosTabelaProducaoConsolidadoConsultas.INDICE_COLUNA_ATENDIMENTO_AUSENTE_PRIMEIRA_CONSULTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getTipo(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_ATENDIMENTOS_AUSENTE.getFormato()));
				
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
