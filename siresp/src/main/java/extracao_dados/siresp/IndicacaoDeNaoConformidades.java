package extracao_dados.siresp;

import java.awt.image.WritableRenderedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import interacao_externa.AcoesArquivoExcel;
import modelosDados.CelulaExcel;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadesFilaCentralReg;
import modelosDados.UsuarioFilaCentralReg;
import modulos.DemandaReprimida;
import modulosGEFIC.NaoConformidadesGEFIC;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class IndicacaoDeNaoConformidades {

	public static void main( String[] args )
    {
		String pastaBase = JOptionPane.showInputDialog(null, "Insira o caminho completo da pasta compartilhada", "Pasta de Destino dos Arquivos", JOptionPane.QUESTION_MESSAGE).trim();
    	
		String competenciaInicial = JOptionPane.showInputDialog(null, "Insira os dados do mês/ano da verificação de não conformidades (MM/yyyy)", "Competência Inicial", JOptionPane.QUESTION_MESSAGE).trim();
		
		NaoConformidadesGEFIC naoConformidades = new NaoConformidadesGEFIC(pastaBase, args[0]);
		
		//sobrescrever
		//naoConformidades.identificarNaoConformidades(false, competenciaInicial, true);
		
		//não sobrescrever
		naoConformidades.identificarNaoConformidades(false, competenciaInicial, false);
	}
}
