package modelosDados;

import java.util.ArrayList;

public class CorrelacaoColunasOfertasDemandas {
	
	private int colunaConsolidado;
	private ArrayList<Integer> colunaSIRESP;
	private ArrayList<Integer> colunasSubtracao;
	private ArrayList<Integer> colunasDivisao;
	private String tipo;
	private String formato;
	
	public CorrelacaoColunasOfertasDemandas(int colunaConsolidado, String tipo, String formato)
	{
		this.colunaConsolidado = colunaConsolidado;
		this.colunaSIRESP = new ArrayList<Integer>();
		this.colunasSubtracao = new ArrayList<Integer>();
		this.colunasDivisao = new ArrayList<Integer>();
		this.tipo = tipo;
		this.formato = formato;
	}
	
	public CorrelacaoColunasOfertasDemandas(int colunaConsolidado, ArrayList<Integer> colunaSIRESPSoma, String tipo, String formato)
	{
		this.colunaConsolidado = colunaConsolidado;
		this.colunaSIRESP = colunaSIRESPSoma;
		this.colunasSubtracao = new ArrayList<Integer>();
		this.colunasDivisao = new ArrayList<Integer>();
		this.tipo = tipo;
		this.formato = formato;
	}
	
	public CorrelacaoColunasOfertasDemandas(int colunaConsolidado, ArrayList<Integer> colunaSIRESPSoma, ArrayList<Integer> colunaSIRESPSubtracao, ArrayList<Integer> colunaSIRESPDivisao, String tipo, String formato)
	{
		this.colunaConsolidado = colunaConsolidado;
		this.colunaSIRESP = colunaSIRESPSoma;
		this.colunasSubtracao = colunaSIRESPSubtracao;
		this.colunasDivisao = colunaSIRESPDivisao;
		this.tipo = tipo;
		this.formato = formato;
	}
	
	public CorrelacaoColunasOfertasDemandas(int colunaConsolidado, int colunaSIRESP, String tipo, String formato)
	{
		this.colunaConsolidado = colunaConsolidado;
		this.colunaSIRESP = new ArrayList<Integer>();
		this.colunaSIRESP.add(colunaSIRESP);
		this.colunasSubtracao = new ArrayList<Integer>();
		this.colunasDivisao = new ArrayList<Integer>();
		this.tipo = tipo;
		this.formato = formato;
	}
	
	public int getColunaConsolidado() {
		return colunaConsolidado;
	}
	
	public void setColunaConsolidado(int colunaConsolidado) {
		this.colunaConsolidado = colunaConsolidado;
	}

	public ArrayList<Integer> getColunaSIRESP() {
		return colunaSIRESP;
	}

	public void setColunaSIRESP(ArrayList<Integer> colunaSIRESP) {
		this.colunaSIRESP = colunaSIRESP;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public ArrayList<Integer> getColunasSubtracao() {
		return colunasSubtracao;
	}

	public void setColunasSubtracao(ArrayList<Integer> colunasSubtracao) {
		this.colunasSubtracao = colunasSubtracao;
	}

	public ArrayList<Integer> getColunasDivisao() {
		return colunasDivisao;
	}

	public void setColunasDivisao(ArrayList<Integer> colunasDivisao) {
		this.colunasDivisao = colunasDivisao;
	}

}
