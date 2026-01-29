package dadosGerais;

import java.util.ArrayList;

import modelosDados.MesFormatado;

public class MesesFormatados {
	
	private ArrayList<MesFormatado> meses;
	
	public MesesFormatados()
	{
		meses = new ArrayList();
		
		meses.add(new MesFormatado("01", "JANEIRO", "JANEIRO"));
		meses.add(new MesFormatado("02", "FEVEREIRO", "FEVEREIRO"));
		meses.add(new MesFormatado("03", "MARÇO", "MARCO"));
		meses.add(new MesFormatado("04", "ABRIL", "ABRIL"));
		meses.add(new MesFormatado("05", "MAIO", "MAIO"));
		meses.add(new MesFormatado("06", "JUNHO", "JUNHO"));
		meses.add(new MesFormatado("07", "JULHO", "JULHO"));
		meses.add(new MesFormatado("08", "AGOSTO", "AGOSTO"));
		meses.add(new MesFormatado("09", "SETEMBRO", "SETEMBRO"));
		meses.add(new MesFormatado("10", "OUTUBRO", "OUTUBRO"));
		meses.add(new MesFormatado("11", "NOVEMBRO", "NOVEMBRO"));
		meses.add(new MesFormatado("12", "DEZEMBRO", "DEZEMBRO"));
	}

	public ArrayList<MesFormatado> getMeses() {
		return meses;
	}

	public void setMeses(ArrayList<MesFormatado> meses) {
		this.meses = meses;
	}
	
	

}