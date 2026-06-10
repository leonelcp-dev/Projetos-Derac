package extracao_dados.siresp;

import modulos.LeitosUrgencia;
import modulos.OfertaDemandaDeAcessoR1;

public class Testador {
	
	public static void main( String[] args )
    {
//		OfertaDemandaDeAcessoR1 oferta = new OfertaDemandaDeAcessoR1("C:\\Users\\PMC514991-2\\Documents", "TESTE");
//		
//		oferta.ordenarPlanilhaDeOfertas();
//		oferta.ordenarPlanilhaDeDemandas();
		
		LeitosUrgencia leitosUrgencia = new LeitosUrgencia();
		
		leitosUrgencia.consolidarDadosDeLeitos("TESTE");
	}


}
