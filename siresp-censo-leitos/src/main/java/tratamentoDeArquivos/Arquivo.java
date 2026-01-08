package tratamentoDeArquivos;

import java.io.File;

public class Arquivo {
	
	private File arquivo;
	private String caminho;
	private String nomeDoArquivo;
	
	public Arquivo(String caminho, String nomeDoArquivo)
	{
		this.caminho = caminho;
		this.nomeDoArquivo = nomeDoArquivo;
		
		arquivo = new File(caminho + "\\" + nomeDoArquivo);
		
	}
	
	public boolean renomear(String novoNome)
	{
		arquivo.renameTo(new File(caminho + "\\" + novoNome));
	
		this.nomeDoArquivo = novoNome;
		return true;
	}

	public File getArquivo() {
		return arquivo;
	}

	public String getCaminhoCompleto() {
		return caminho + "\\" + nomeDoArquivo;
	}
	
	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}
	
	public String getNomeDoArquivo()
	{
		return nomeDoArquivo;
	}
	

}
