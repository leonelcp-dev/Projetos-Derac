package extracao_dados.siresp;

import java.awt.image.WritableRenderedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import interacao_externa.AcoesArquivoExcel;
import modelosDados.CelulaExcel;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadesFilaCentralReg;
import modelosDados.UsuarioFilaCentralReg;
import modulos.DemandaReprimida;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class ConsolidacaoDemandaReprimida {

	public static void main( String[] args )
    {
		DemandaReprimida demanda = new DemandaReprimida();
		
		demanda.montarDemandaReprimidaDiaria(args[0]);
	}

}
