package modelosDados;

public class CensoLeitos
{
	private String paciente;
	private String dataNascimento;
	private String CNS;
	private String datadeInternacao;
	private String descricaoEnfermaria;
	private String descricaoLeito;
	private String especialidade;
	private String sexo;
	private String status;
	private String situacao;
	private String CID1;
	private String CID2;
	private String bloqueado;
	private String motivodoBloqueio;
	private String justificativadoBloqueio;
	private String municipiodeOrigemdoPaciente;
	private String analisedoDERAC;
	private String dataRelatorio;
	private String tipodeLeito3;
	private String motivodoBloqueioRevisto;
	private String unidade;
	private String concatena1;
	private String dia;
	private String tipodeLeito1;
	private String concatena2;
	private String tipodeLeito2;
	
	public String getCNS() {
		return CNS;
	}
	
	public void setCNS(String cNS) {
		CNS = cNS;
	}
	
	public String getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getPaciente() {
		return paciente;
	}
	
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	
	public String getDescricaoEnfermaria() {
		return descricaoEnfermaria;
	}
	
	public void setDescricaoEnfermaria(String descricaoEnfermaria) {
		this.descricaoEnfermaria = descricaoEnfermaria;
	}
	
	public String getDatadeInternacao() {
		return datadeInternacao;
	}
	
	public void setDatadeInternacao(String datadeInternacao) {
		this.datadeInternacao = datadeInternacao;
	}
}
