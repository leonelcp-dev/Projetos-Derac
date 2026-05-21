package modulos;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import dadosGerais.CorrelacaoArquivosAbsenteismo;
import dadosGerais.IdentificadoresPaginaWebSIRESP;
import dadosGerais.IdentificadoresSIRESPDigitalOfertaDemanda;
import dadosGerais.MesesFormatados;
import dadosGerais.ParametrosArquivoAbsenteismoConsolidado;
import dadosGerais.ParametrosArquivoAbsenteismoConsultaBaixado;
import dadosGerais.ParametrosArquivoAbsenteismoExameBaixado;
import dadosGerais.ParametrosArquivoCenso;
import dadosGerais.ParametrosArquivoFilasNominais;
import dadosGerais.ParametrosArquivoLoginSIRESP;
import dadosGerais.ParametrosArquivoOfertas;
import interacao_externa.AcoesArquivoExcel;
import interacao_externa.AcoesGeraisPaginaWeb;
import interacao_externa.AcoesGeraisPaginaWeb.OpenStrategy;
import interacao_externa.ConversaoHMTL_XLSX;
import interacao_externa.ExcelBinder;
import modelosDados.CelulaExcel;
import modelosDados.CorrelacaoColunasArquivos;
import modelosDados.ElementoSelecao;
import modelosDados.EntidadeAbsenteismo;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadeExecutante;
import modelosDados.EntidadeLeito;
import modelosDados.LinhaCensoLeitos;
import modelosDados.MesFormatado;
import modelosDados.PerfilSIRESP;
import modelosDados.PerfisUsuariosSIRESP;
import modelosDados.PerfisUsuariosSIRESP.Usuario;
import modelosDados.UsuarioSIRESP;
import modelosDados.UsuariosVinculadosSIRESP;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class OfertaDemandaDeAcesso {
	
	private String pastaComDados;
	private String dataInicialDeAnalise;
	private String dataFinalDeAnalise;
	
	
	public String calcularOfertaEDemanda(WebDriver driver)
	{			
		
		AcoesGeraisPaginaWeb paginaWeb = new AcoesGeraisPaginaWeb();
				
		driver.get("https://digital.siresp.saude.sp.gov.br/pt_BR/usuario-acl/");
		
		int multiplicadorTempo = 4;
		
		ArrayList<String> tiposDeRecursos = new ArrayList<String>();
		tiposDeRecursos.add(IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_TIPO_RECURSO_CONSULTA.getTextoIdentificador());
		tiposDeRecursos.add(IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_TIPO_RECURSO_EXAMES.getTextoIdentificador());
		tiposDeRecursos.add(IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_TIPO_RECURSO_PROCEDIMENTOS.getTextoIdentificador());
		
		pastaComDados = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta onde se encontram os dados dos logins a serem criados", "Pasta", JOptionPane.QUESTION_MESSAGE).trim();
		dataInicialDeAnalise = JOptionPane.showInputDialog(null, "Insira a data inicial da análise", "Data Inicial", JOptionPane.QUESTION_MESSAGE).trim();
		dataFinalDeAnalise = JOptionPane.showInputDialog(null, "Insira a data final da análise", "Data Final", JOptionPane.QUESTION_MESSAGE).trim();
		
		//definindo entidades para o censo de leitos
		ArrayList<EntidadeExecutante> executantes = lerEntidades(pastaComDados + "\\unidadesExecutantes.csv");
		
		AcoesArquivoExcel arquivoExcelResultante = new AcoesArquivoExcel(pastaComDados + "\\ConsolidadoOfertaEDemanda.xlsx", 0);
		
		
		String unidadeAtual = "";
		
		String usuarioAtual = "";
		String loginAtual = "";
		
		
	
		for(EntidadeExecutante executante : executantes)
		{
			
			String ErroAoAcessarUnidade = "";
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_UNIDADE_VALOR_SELECIONE.getTextoIdentificador());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador(), executante.getUnidade());
			while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
			
			if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador()).equals(executante.getUnidade()))
			{
				System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_UNIDADE.getTextoIdentificador()) + " - " + executante);
				ErroAoAcessarUnidade = "Erro";
			}
			else
			{
				if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador()))
				{
					paginaWeb.clicarElementoPeloId(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INICIAL_FILTRO_MODULO.getTextoIdentificador());
					paginaWeb.digitarEmInputText(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INICIAL_TEXTO_FILTRO_MODULO.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_MODULO.getTextoIdentificador());
				}

				int tentativas = 0;
				boolean selecionado;
				do
				{
					try {
						Thread.sleep(multiplicadorTempo * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_MODULO.getTextoIdentificador());
					tentativas++;
					
				}while(tentativas <= 5 && !selecionado);
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador()).equals(IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_MODULO.getTextoIdentificador()))
				{
					System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_MODULO.getTextoIdentificador()) + " - " + IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_MODULO.getTextoIdentificador());
					ErroAoAcessarUnidade = "Erro";
				}
				else
				{
					if(paginaWeb.elementoEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INICIAL_FILTRO_PERFIL.getTextoIdentificador()))
						paginaWeb.escolherEmSelect2(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INICIAL_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INICIAL_TEXTO_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INICIAL_OPCOES_PERFIL.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
					else
						paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
					
					tentativas = 0;
					do
					{
						try {
							Thread.sleep(multiplicadorTempo * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						selecionado = paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
						tentativas++;
					}while(tentativas <= 5 && !selecionado);
						
					
					while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					
					System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()) + " - " + IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_PERFIL.getTextoIdentificador());
					
					if(!paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()).toUpperCase().equals(IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_PERFIL.getTextoIdentificador().toUpperCase()))
					{
						System.out.println(paginaWeb.obterValorSelecionadoDoSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TELA_INTERNA_FILTRO_PERFIL.getTextoIdentificador()) + " - " + IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_FILTRO_PERFIL.getTextoIdentificador().toUpperCase());
						ErroAoAcessarUnidade = "Erro";
					}
				}
				
			}
			
			if(ErroAoAcessarUnidade.equals(""))
			{
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				paginaWeb.clicarSpanPeloTitulo(driver, IdentificadoresSIRESPDigitalOfertaDemanda.TITULO_TELA_INTERNA_SPAN_RELATORIOS.getTextoIdentificador());
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresSIRESPDigitalOfertaDemanda.XPATH_RELATORIO_PRODUCAO_EXECUTANTE.getTextoIdentificador());
				while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
				
				for(String tipoDeRecurso : tiposDeRecursos)
				{
					paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_SELECT_TIPO_DE_RECURSO.getTextoIdentificador(), tipoDeRecurso);
					
					if(tipoDeRecurso.equals(IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_TIPO_RECURSO_CONSULTA.getTextoIdentificador()))
						paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_SELECT_TIPO_DE_MARCACAO.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_TIPO_MARCACAO_PRIMEIRA_CONSULTA.getTextoIdentificador());
					
					paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_SELECT_PERFIL_RELATORIO.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_PERFIL_RELATORIO_RECURSO.getTextoIdentificador());
					paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_SELECT_TIPO_EMISSAO.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_TIPO_EMISSAO_RESUMIDO.getTextoIdentificador());
					
					if(executante.getVinculo().equals(IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_VINCULO_ESTADUAL.getTextoIdentificador()))
					{
						paginaWeb.digitarEmInputText(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_SELECT_UNIDADE_SOLICITANTE.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_SOLICITANTE_SMS_CAMPINAS.getTextoIdentificador());
						paginaWeb.selecionarItemSelectULLIPeloDataValueDeUmOption(driver, IdentificadoresSIRESPDigitalOfertaDemanda.XPATH_OPCAO_UNIDADE_SOLICITANTE.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_OPCAO_UNIDADE_SOLICITANTE_SMS_CAMPINAS.getTextoIdentificador());
					}
					
					paginaWeb.digitarEmInputText(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TEXT_DATA_INICIAL.getTextoIdentificador(), dataInicialDeAnalise);
					paginaWeb.digitarEmInputText(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_TEXT_DATA_FINAL.getTextoIdentificador(), dataFinalDeAnalise);
					
					paginaWeb.selecionarItemSelect(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_SELECT_VISUALIZACAO.getTextoIdentificador(), IdentificadoresSIRESPDigitalOfertaDemanda.TEXTO_VISUALIZACAO_EM_TELA.getTextoIdentificador());
					
					paginaWeb.clicarLinkPeloXPath(driver, IdentificadoresSIRESPDigitalOfertaDemanda.XPATH_BOTAO_PESQUISAR.getTextoIdentificador());
					while(paginaWeb.divEstaVisivel(driver, IdentificadoresSIRESPDigitalOfertaDemanda.ID_DIV_CARREGANDO_PAGINA.getTextoIdentificador()));
					
					if(!paginaWeb.elementoEstaVisivelPeloXPATH(driver, IdentificadoresSIRESPDigitalOfertaDemanda.XPATH_TABELA_RESULTADOS_NENHUM_RESULTADO.getTextoIdentificador()))
					{
						ArrayList<ArrayList<String>> tabelaResultados = paginaWeb.obterTablePeloXPath(driver, IdentificadoresSIRESPDigitalOfertaDemanda.XPATH_TABELA_RESULTADOS.getTextoIdentificador());
						
						arquivoExcelResultante.abrirPlanilha(ParametrosArquivoOfertas.NOME_PLANILHA_OFERTAS.getDescricao(), 0);
						int numeroDaLinha = arquivoExcelResultante.getUltimaLinhaPreenchida() + 1;
						ArrayList<CelulaExcel> celulas = new ArrayList<CelulaExcel>();
						
						System.out.println("Tabela :" + tabelaResultados.size());
						
						for(ArrayList<String> linha : tabelaResultados)
						{
							System.out.println("Linha :" + linha.size());
							
							if(linha.size() > 1)
							{
								celulas.add(new CelulaExcel(numeroDaLinha, ParametrosArquivoOfertas.INDICE_COLUNA_UNIDADE.getIndice(), executante.getUnidade(), "String"));
								celulas.add(new CelulaExcel(numeroDaLinha, ParametrosArquivoOfertas.INDICE_COLUNA_TIPO_RECURSO.getIndice(), tipoDeRecurso, "String"));
								celulas.add(new CelulaExcel(numeroDaLinha, ParametrosArquivoOfertas.INDICE_COLUNA_RECURSO.getIndice(), linha.get(0), "String"));
								celulas.add(new CelulaExcel(numeroDaLinha, ParametrosArquivoOfertas.INDICE_COLUNA_OFERTA.getIndice(), Integer.parseInt(linha.get(1)), "Integer"));
								celulas.add(new CelulaExcel(numeroDaLinha, ParametrosArquivoOfertas.INDICE_COLUNA_AGENDADO.getIndice(), Integer.parseInt(linha.get(2)), "Integer"));
								celulas.add(new CelulaExcel(numeroDaLinha, ParametrosArquivoOfertas.INDICE_COLUNA_REALIZADO.getIndice(), Integer.parseInt(linha.get(3)), "Integer"));
								
								numeroDaLinha++;
							}
						}
						
						arquivoExcelResultante.gravarDadosEmCelula(ParametrosArquivoOfertas.NOME_PLANILHA_OFERTAS.getDescricao(), celulas, false, false, 0, null);
					}
				}
			}
		}
		
		return "";
	}
	
	private ArrayList<EntidadeExecutante> lerEntidades(String nomeArquivo)
	{
		ArrayList<EntidadeExecutante> entidades = new ArrayList<EntidadeExecutante>();
		
        try (Reader reader = new FileReader(nomeArquivo);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").setHeader().setSkipHeaderRecord(true).build())) {

            // Itera sobre cada linha do arquivo CSV
            for (CSVRecord registro : csvParser) {
                // Acessa os valores pelos nomes dos cabeçalhos
            	String cnes = registro.get("CNES");
            	String vinculo = registro.get("VINCULO");
                String unidade = registro.get("EXECUTANTE");
               
                entidades.add(new EntidadeExecutante(cnes, vinculo, unidade));
            }
            
            return entidades;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
}
