package tratamentoDeArquivos;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class Arquivo {
	
	private String caminho;
	private String nomeDoArquivo;
	private Path arquivo;
	
	public Arquivo(String caminho, String nomeDoArquivo)
	{
		this.caminho = caminho;
		this.nomeDoArquivo = nomeDoArquivo;
		
		try
		{
			arquivo = Paths.get(caminho + "\\" + nomeDoArquivo);
		}
		catch(Exception e)
		{
			arquivo = null;
		}
	}
	
	public boolean mover(String caminhoDoDestino)
	{
		try
		{
			Path destino = Paths.get(caminhoDoDestino);
			
			Files.move(arquivo, destino, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean apagar()
	{
		try
		{
		
			Files.delete(arquivo);
		}catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	public boolean CopiarArquivo(String caminhoDoDestino)
	{
        Path destino = Paths.get(caminhoDoDestino);

        try {
			Files.copy(arquivo, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        
        return true;
	}

	
	public boolean renomear(String novoNome)
	{
		try
		{
			Path destino = Paths.get(caminho + "\\" + novoNome);
			
			Files.move(arquivo, destino, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	
		arquivo = Paths.get(caminho + "\\" + novoNome);
		this.nomeDoArquivo = novoNome;
		return true;
	}

	public Path getArquivo() {
		return arquivo;
	}

	public String getCaminhoCompleto() {
		return caminho + "\\" + nomeDoArquivo;
	}
	
	public void setArquivo(Path arquivo) {
		this.arquivo = arquivo;
	}
	
	public String getNomeDoArquivo()
	{
		return nomeDoArquivo;
	}
	
	public boolean existe()
	{
		if(arquivo == null)
			return false;
		
		return Files.exists(arquivo);
	}
	

}
