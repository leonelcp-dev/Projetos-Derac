package dadosGerais;

import java.util.ArrayList;

import modelosDados.MesFormatado;

public class MesesFormatados {
	
	private ArrayList<MesFormatado> meses;
	
	public MesesFormatados()
	{
		meses = new ArrayList();
		
		meses.add(new MesFormatado("01", "JANEIRO"));
		meses.add(new MesFormatado("02", "FEVEREIRO"));
		meses.add(new MesFormatado("03", "MARÇO"));
		meses.add(new MesFormatado("04", "ABRIL"));
		meses.add(new MesFormatado("05", "MAIO"));
		meses.add(new MesFormatado("06", "JUNHO"));
		meses.add(new MesFormatado("07", "JULHO"));
		meses.add(new MesFormatado("08", "AGOSTO"));
		meses.add(new MesFormatado("09", "SETEMBRO"));
		meses.add(new MesFormatado("10", "OUTUBRO"));
		meses.add(new MesFormatado("11", "NOVEMBRO"));
		meses.add(new MesFormatado("12", "DEZEMBRO"));
	}

	public ArrayList<MesFormatado> getMeses() {
		return meses;
	}

	public void setMeses(ArrayList<MesFormatado> meses) {
		this.meses = meses;
	}
	
	

}