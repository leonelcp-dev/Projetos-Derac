package tratamentoDeArquivos;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Pasta {

	private File pasta;
	private boolean existe;
	
	public Pasta(String caminhoDaPasta, boolean criar)
	{
		pasta = new File(caminhoDaPasta);
		
		if(!pasta.exists() && criar)
		{
			setExiste(true);
			pasta.mkdir();
		}
		else
			setExiste(false);
	}

	public File getPasta() {
		return pasta;
	}

	public void setPasta(File pasta) {
		this.pasta = pasta;
	}

	public boolean isExiste() {
		return existe;
	}

	public void setExiste(boolean existe) {
		this.existe = existe;
	}
	
	public String arquivoRecentementeModificado()
	{
		File[] arquivos = pasta.listFiles();
		String arquivo;
		
		if(arquivos != null)
		{
			Arrays.sort(arquivos, Comparator.comparingLong(File::lastModified).reversed());
			
			arquivo = arquivos[0].getName();
		}else {
			return "";
		}
		
		return arquivo;
	}
	
}
