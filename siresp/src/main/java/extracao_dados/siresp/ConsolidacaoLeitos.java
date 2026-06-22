package extracao_dados.siresp;

import modulos.LeitosUrgencia;
import modulos.OfertaDemandaDeAcessoR1;
import modulos.UrgenciaAguardando;

public class ConsolidacaoLeitos {
	
	public static void main( String[] args )
    {
//		OfertaDemandaDeAcessoR1 oferta = new OfertaDemandaDeAcessoR1("C:\\Users\\PMC514991-2\\Documents", "TESTE");
//		
//		oferta.ordenarPlanilhaDeOfertas();
//		oferta.ordenarPlanilhaDeDemandas();
		
		LeitosUrgencia leitosUrgencia = new LeitosUrgencia();
		

Runtime rt = Runtime.getRuntime();

System.out.println("Max memory: " + rt.maxMemory() / (1024 * 1024) + " MB");
System.out.println("Total memory: " + rt.totalMemory() / (1024 * 1024) + " MB");
System.out.println("Free memory: " + rt.freeMemory() / (1024 * 1024) + " MB");

		
	leitosUrgencia.consolidarDadosDeLeitos("TESTE");
	}


}
