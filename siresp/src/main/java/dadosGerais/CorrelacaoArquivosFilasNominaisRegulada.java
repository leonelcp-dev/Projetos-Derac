package dadosGerais;

import java.util.ArrayList;

import modelosDados.CorrelacaoColunasArquivos;

public class CorrelacaoArquivosFilasNominaisRegulada {

	/*
	 * Classe criada para correlacionar a posição das colunas dos arquivos baixados a partir do SIRESP.
	 * Os arquivos de filas nominais para AGENDAMENTO e para SOLICITAÇÃO possuem estruturas ligeiramente distintas. Portanto a ideia aqui é manter a correlação das 
	 * colunas de forma a melhorar o processo de escrita no arquivo final das Filas Nominais de cada uniadade. 
	 */
	
	public ArrayList<CorrelacaoColunasArquivos> obterCorrelacaoEntreArquivos(String consultaOuExame)
	{
		ArrayList<CorrelacaoColunasArquivos> correlacao = new ArrayList<CorrelacaoColunasArquivos>();
		
		if(consultaOuExame.equals("Agendamentos"))
		{
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getIndice(), ParametrosArquivoAgendamentosPendentesRegulada.INDICE_COLUNA_FICHA.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getIndice(), ParametrosArquivoAgendamentosPendentesRegulada.INDICE_COLUNA_CODIGO_PACIENTE.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), ParametrosArquivoAgendamentosPendentesRegulada.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), ParametrosArquivoAgendamentosPendentesRegulada.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getIndice(), ParametrosArquivoAgendamentosPendentesRegulada.INDICE_COLUNA_HIPOTESE.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getFormato()));
		}
		else if(consultaOuExame.equals("Solicitações"))
		{
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getIndice(), ParametrosArquivoSolicitacoesPendentesRegulada.INDICE_COLUNA_FICHA.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_FICHA.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getIndice(), ParametrosArquivoSolicitacoesPendentesRegulada.INDICE_COLUNA_CODIGO_PACIENTE.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_CODIGO_PACIENTE.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), ParametrosArquivoSolicitacoesPendentesRegulada.INDICE_COLUNA_UNIDADE_SOLICITANTE.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_UNIDADE_SOLICITANTE.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), ParametrosArquivoSolicitacoesPendentesRegulada.INDICE_COLUNA_ESPECIALIDADE_EXAME.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_ESPECIALIDADE_EXAME.getFormato()));
			correlacao.add(new CorrelacaoColunasArquivos(ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getIndice(), ParametrosArquivoSolicitacoesPendentesRegulada.INDICE_COLUNA_HIPOTESE.getIndice(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getTipo(), ParametrosArquivoReguladaConsolidado.INDICE_COLUNA_HIPOTESE.getFormato()));
		}
		
		return correlacao;
	}
	
}
