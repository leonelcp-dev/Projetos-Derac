package dadosGerais;

import java.util.ArrayList;

import modelosDados.MesFormatado;

public class MesesFormatados {
	
	private ArrayList<MesFormatado> meses;
	
	public MesesFormatados()
	{
		meses = new ArrayList();
		
		meses.add(new MesFormatado("01", "JANEIRO", "JANEIRO", "Janeiro"));
		meses.add(new MesFormatado("02", "FEVEREIRO", "FEVEREIRO", "Fevereiro"));
		meses.add(new MesFormatado("03", "MARÇO", "MARCO", "Março"));
		meses.add(new MesFormatado("04", "ABRIL", "ABRIL", "Abril"));
		meses.add(new MesFormatado("05", "MAIO", "MAIO", "Maio"));
		meses.add(new MesFormatado("06", "JUNHO", "JUNHO", "Junho"));
		meses.add(new MesFormatado("07", "JULHO", "JULHO", "Julho"));
		meses.add(new MesFormatado("08", "AGOSTO", "AGOSTO", "Agosto"));
		meses.add(new MesFormatado("09", "SETEMBRO", "SETEMBRO", "Setembro"));
		meses.add(new MesFormatado("10", "OUTUBRO", "OUTUBRO", "Outubro"));
		meses.add(new MesFormatado("11", "NOVEMBRO", "NOVEMBRO", "Novembro"));
		meses.add(new MesFormatado("12", "DEZEMBRO", "DEZEMBRO", "Dezembro"));
	}

	public ArrayList<MesFormatado> getMeses() {
		return meses;
	}

	public void setMeses(ArrayList<MesFormatado> meses) {
		this.meses = meses;
	}
	
	

}