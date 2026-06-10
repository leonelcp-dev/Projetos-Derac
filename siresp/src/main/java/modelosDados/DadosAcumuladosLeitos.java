package modelosDados;


public class DadosAcumuladosLeitos {

    private String especialidade;
    private int totalDisponivel;
    private int reservaInterna;
    private int totalOcupado;
    private int regularOcupado;
    private int extraOcupado;
    private int internoOcupado;
    private int totalBloqueado;
    private int bloqueadoIsolamento;
    private int bloqueadoAguardandoPaciente;
    private int bloqueadoOutros;   
    private int leitosVagos;

    public DadosAcumuladosLeitos()
    {
         totalDisponivel = 0;
         reservaInterna = 0;
         totalOcupado = 0;
         regularOcupado = 0;
         extraOcupado = 0;
         internoOcupado = 0;
         totalBloqueado = 0;
         bloqueadoIsolamento = 0;
         bloqueadoAguardandoPaciente = 0;
         bloqueadoOutros = 0;   
         leitosVagos = 0;
    }
    
    public void incrementarTotalDisponivel()
    {
    	totalDisponivel++;
    }
    
    public void incrementarReservaInterna()
    {
    	reservaInterna++;
    }
    
    public void incrementarTotalOcupado()
    {
    	totalOcupado++;
    }
    
    public void incrementarRegularOcupado()
    {
    	regularOcupado++;
    }
    
    public void incrementarExtraOcupado()
    {
    	extraOcupado++;
    }
    
    public void incrementarInternoOcupado()
    {
    	internoOcupado++;
    }
    
    public void incrementarTotalBloqueado()
    {
    	totalBloqueado++;
    }
    
    public void incrementarBloqueadoPorIsolamento()
    {
    	bloqueadoIsolamento++;
    }
    
    public void incrementarBloqueadoAguardandoPaciente()
    {
    	bloqueadoAguardandoPaciente++;
    }
    
    public void incrementarBloqueadoOutros()
    {
    	bloqueadoOutros++;
    }
    
    public void incrementarLeitosVagos()
    {
    	leitosVagos++;
    }
    
	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public int getTotalDisponivel() {
		return totalDisponivel;
	}

	public void setTotalDisponivel(int totalDisponivel) {
		this.totalDisponivel = totalDisponivel;
	}

	public int getTotalOcupado() {
		return totalOcupado;
	}

	public void setTotalOcupado(int totalOcupado) {
		this.totalOcupado = totalOcupado;
	}

	public int getRegularOcupado() {
		return regularOcupado;
	}

	public void setRegularOcupado(int regularOcupado) {
		this.regularOcupado = regularOcupado;
	}

	public int getExtraOcupado() {
		return extraOcupado;
	}

	public void setExtraOcupado(int extraOcupado) {
		this.extraOcupado = extraOcupado;
	}

	public int getTotalBloqueado() {
		return totalBloqueado;
	}

	public void setTotalBloqueado(int totalBloqueado) {
		this.totalBloqueado = totalBloqueado;
	}

	public int getBloqueadoIsolamento() {
		return bloqueadoIsolamento;
	}

	public void setBloqueadoIsolamento(int bloqueadoIsolamento) {
		this.bloqueadoIsolamento = bloqueadoIsolamento;
	}

	public int getBloqueadoAguardandoPaciente() {
		return bloqueadoAguardandoPaciente;
	}

	public void setBloqueadoAguardandoPaciente(int bloqueadoAguardandoPaciente) {
		this.bloqueadoAguardandoPaciente = bloqueadoAguardandoPaciente;
	}

	public int getBloqueadoOutros() {
		return bloqueadoOutros;
	}

	public void setBloqueadoOutros(int bloqueadoOutros) {
		this.bloqueadoOutros = bloqueadoOutros;
	}

	public int getLeitosVagos() {
		return leitosVagos;
	}

	public void setLeitosVagos(int leitosVagos) {
		this.leitosVagos = leitosVagos;
	}

	public int getReservaInterna() {
		return reservaInterna;
	}

	public void setReservaInterna(int reservaInterna) {
		this.reservaInterna = reservaInterna;
	}

	public int getInternoOcupado() {
		return internoOcupado;
	}

	public void setInternoOcupado(int internoOcupado) {
		this.internoOcupado = internoOcupado;
	}

}
