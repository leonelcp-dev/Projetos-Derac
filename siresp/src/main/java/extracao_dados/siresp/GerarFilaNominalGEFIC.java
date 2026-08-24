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

import dadosGerais.IdentificadoresPaginaWebGEFIC;
import interacao_externa.AcoesArquivoExcel;
import modelosDados.CelulaExcel;
import modelosDados.EntidadeCDRNaoRegulada;
import modelosDados.EntidadesFilaCentralReg;
import modelosDados.UsuarioFilaCentralReg;
import modulos.DemandaReprimida;
import modulosGEFIC.FilasNominaisGEFIC;
import tratamentoDeArquivos.Arquivo;
import tratamentoDeArquivos.Pasta;

public class GerarFilaNominalGEFIC {

	public static void main( String[] args )
    {
		FilasNominaisGEFIC filaNominalGEFIC = new FilasNominaisGEFIC("C:\\Users\\PMC514991-2", "TESTE", false);
		
		filaNominalGEFIC.gerarFilasNominaisPorStatus("07/2026", "14/08/2026", IdentificadoresPaginaWebGEFIC.TEXTO_STATUS_PROCEDIMENTO_REALIZADO.getTextoIdentificador(), false);
	}
}
