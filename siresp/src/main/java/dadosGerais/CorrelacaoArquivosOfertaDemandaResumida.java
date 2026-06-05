package dadosGerais;

import java.util.ArrayList;

import modelosDados.CorrelacaoColunasArquivos;
import modelosDados.CorrelacaoColunasOfertasDemandas;

public class CorrelacaoArquivosOfertaDemandaResumida {

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
	
	public ArrayList<CorrelacaoColunasOfertasDemandas> obterCorrelacaoEntreArquivos()
	{
		ArrayList<CorrelacaoColunasOfertasDemandas> correlacoes = new ArrayList<CorrelacaoColunasOfertasDemandas>();
		
		CorrelacaoColunasOfertasDemandas correlacao;
		
		correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_UNIDADE.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_UNIDADE.getIndice(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_UNIDADE.getTipo(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_UNIDADE.getFormato()));
		correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_VINCULO.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_VINCULO.getIndice(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_VINCULO.getTipo(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_VINCULO.getFormato()));
		correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_COMPETENCIA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_COMPETENCIA.getIndice(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_COMPETENCIA.getTipo(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_VINCULO.getFormato()));
		correlacoes.add(new CorrelacaoColunasOfertasDemandas(ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_TIPO_OFERTA.getIndice(), ParametrosArquivoOfertaDemanda.INDICE_COLUNA_TIPO_OFERTA.getIndice(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_TIPO_OFERTA.getTipo(), ParametrosArquivoOfertaDemandaResumida.INDICE_COLUNA_VINCULO.getFormato()));
		
		
//		INDICE_COLUNA_PROCEDIMENTOS(4, 5, "Procedimentos (Padronizado)", "String", ""),
//		INDICE_COLUNA_ESPECIALIDADE(5, 6, "Especialidade", "String", ""),
//		INDICE_COLUNA_OFERTAS_PREVISTAS(7, 7, "Ofertas Previstas", "Int", ""),
//		INDICE_COLUNA_NOVAS_SOLICITACOES_MENSAIS(8, 8, "Novas Solicitações (mensais)", "Int", ""),
//		INDICE_COLUNA_OFERTA_DISPONIVEL(9, 9, "Oferta Disponível", "Int", ""),
//		INDICE_COLUNA_AGENDAMENTOS_TOTAL(11, 10,"Agendamentos Total", "Int", ""),
//		INDICE_COLUNA_ATENDIMENTOS_PRESENTE(16, 11,"Atendimentos Total", "Int", ""),
//		INDICE_COLUNA_ATENDIMENTOS_AUSENTE(17, 12,"Atendimentos Ausentes", "Int", ""),
//		INDICE_COLUNA_ATENDIMENTOS_DESISTENCIA(19, 13,"Atendimentos Desistencia", "Int", ""),
//		INDICE_COLUNA_ATENDIMENTOS_DISPENSADO(20, 14,"Atendimentos Dispensado", "Int", ""),
//		INDICE_COLUNA_ATENDIMENTOS_NAO_INFORMADO(21, 15,"Atendimentos Não Informado", "Int", ""),
//		INDICE_COLUNA_CALCULOS_ATENDIDO(22, 16,"Cálculos Atendido", "Porcentagem", ""),
//		INDICE_COLUNA_CALCULOS_AUSENTE(23, 17,"Cálculos Ausente", "Porcentagem", ""),
//		INDICE_COLUNA_CALCULOS_DESISTENCIA(24, 18,"Cálculos Desistência", "Porcentagem", ""),
//		INDICE_COLUNA_CALCULOS_DISPENSADO(25, 19,"Cálculo Dispensado", "Porcentagem", ""),
//		INDICE_COLUNA_CALCULOS_NAO_INFORMADO(26, 20,"Cálculo Não Informado", "Porcentagem", ""),
//		INDICE_COLUNA_DEMANDA_REPRIMIDA_DO_DIA(27, 21,"Demanda Reprimida", "Int", ""),
//		INDICE_COLUNA_CALCULOS_TEMPO_DE_ESPERA(28, 22,"Cálculos Tempo de Espera", "Int", ""),
//		INDICE_COLUNA_MAIOR_TEMPO_DE_ESPERA_EM_DIAS(29, 23,"Maior tempo de espera em dias", "Int", ""),

		return correlacoes;
	}
	
}
