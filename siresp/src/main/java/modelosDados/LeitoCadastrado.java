package modelosDados;

public class LeitoCadastrado 
{
	private String unidade;
	private String descricaoEnfermaria;
	private String descricaoLeito;
	private String atividade;
	private String especialidade;
	private String status;
	private String contabilizaNaTaxaDeOcupacao;
	
	public String getAtividade() {
		return atividade;
	}
	
	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getDescricaoEnfermaria() {
		return descricaoEnfermaria;
	}

	public void setDescricaoEnfermaria(String descricaoEnfermaria) {
		this.descricaoEnfermaria = descricaoEnfermaria;
	}

	public String getDescricaoLeito() {
		return descricaoLeito;
	}

	public void setDescricaoLeito(String descricaoLeito) {
		this.descricaoLeito = descricaoLeito;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getContabilizaNaTaxaDeOcupacao() {
		return contabilizaNaTaxaDeOcupacao;
	}

	public void setContabilizaNaTaxaDeOcupacao(String contabilizaNaTaxaDeOcupacao) {
		this.contabilizaNaTaxaDeOcupacao = contabilizaNaTaxaDeOcupacao;
	}
}
