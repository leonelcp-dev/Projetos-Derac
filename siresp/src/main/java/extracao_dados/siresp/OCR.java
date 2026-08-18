package extracao_dados.siresp;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;

public class OCR {

	public static void main(String[] args) throws Exception {

		ITesseract tesseract = new Tesseract();
	// Pasta onde ficam os arquivos .traineddata
		tesseract.setDatapath("C:\\Users\\PMC514991-2\\Documents\\SIRESP\\Dicionarios OCR");
		tesseract.setLanguage("por");
		String texto = tesseract.doOCR(new File("C:\\Users\\PMC514991-2\\Pictures\\Screenshots\\imagem.png"));
		System.out.println(texto);

	}

}
