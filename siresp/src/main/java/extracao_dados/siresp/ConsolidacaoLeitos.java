package extracao_dados.siresp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		
		Runtime rt = Runtime.getRuntime();
		
		System.out.println("Max memory: " + rt.maxMemory() / (1024 * 1024) + " MB");
		System.out.println("Total memory: " + rt.totalMemory() / (1024 * 1024) + " MB");
		System.out.println("Free memory: " + rt.freeMemory() / (1024 * 1024) + " MB");

    	String nomeUsuario = System.getProperty("user.name");
		String pastaBase = "C:\\Users\\" + nomeUsuario;
		
		if(args.length == 0)
		{
			LeitosUrgencia leitosUrgencia = new LeitosUrgencia();
			leitosUrgencia.consolidarDadosDeLeitos("TESTE", false);
		}
		else if(args.length == 1)
		{
			LeitosUrgencia leitosUrgencia = new LeitosUrgencia();
			leitosUrgencia.consolidarDadosDeLeitos(args[0], false);
		}
		else if(args.length == 2)
		{
			if(args[1].equals("SIM"))
			{
				LeitosUrgencia leitosUrgencia = new LeitosUrgencia(pastaBase, args[0], LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				leitosUrgencia.consolidarDadosDeLeitos(args[0], true);
			}
			else
			{
				LeitosUrgencia leitosUrgencia = new LeitosUrgencia();
				leitosUrgencia.consolidarDadosDeLeitos(args[0], false);
			}
		}
		else if(args.length == 3)
		{
			
		}
			
	}


}
