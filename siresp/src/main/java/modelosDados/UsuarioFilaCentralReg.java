package modelosDados;

public class UsuarioFilaCentralReg {

	@ExcelColumn(header = "Tipo")
	public String exameOuConsulta;
	
	@ExcelColumn(header = "Código")
	public String codigo;
	
	@ExcelColumn(header = "Nome")
	public String nome;
	
	@ExcelColumn(header = "Telefone")
	public String telefone;
	
	@ExcelColumn(header = "Município")
	public String municipio;
	
	@ExcelColumn(header = "Especialidade")
	public String especialidade;
	
	@ExcelColumn(header = "Cid")
	public String cid;
	
	@ExcelColumn(header = "Tipo Consulta")
	public String tipo;
	
	@ExcelColumn(header = "Profissional")
	public String profissional;
	
	@ExcelColumn(header = "Idade do Paciente")
	public String idade;
	
	@ExcelColumn(header = "Mês/Ano Pretendido")
	public String mesAnoPretendido;
	
	@ExcelColumn(header = "Turno")
	public String turno;
	
	@ExcelColumn(header = "Data Agenda")
	public String dataAgenda;
	
	@ExcelColumn(header = "Horário")
	public String Horario;
		
	@ExcelColumn(header = "Data Entrada")
	public String dataEntrada;
	
	@ExcelColumn(header = "Status")
	public String status;
	
	@ExcelColumn(header = "Filipeta")
	public String filipeta;
	
	@ExcelColumn(header = "Ret. Filipeta")
	public String retFilipeta;
	
	@ExcelColumn(header = "Prioridade")
	public String prioridade;
	
	@ExcelColumn(header = "Aceita Teleconsulta")
	public String aceitaTeleconsulta;
	
	@ExcelColumn(header = "Observação")
	public String observacao;
	
	@ExcelColumn(header = "Observação Status")
	public String observacaoStatus;
	
	@ExcelColumn(header = "Alteração Especialidade/Exame - De")
	public String alteracaoEspecialidadeExameDe;
	
	@ExcelColumn(header = "Alteração Especialidade/Exame - Para")
	public String alteracaoEspecialidadeExamePara;
	
	@ExcelColumn(header = "Observação 2")
	public String observacao2;
	
	@ExcelColumn(header = "Usuário 2")
	public String usuario2;
	
	@ExcelColumn(header = "Data de alteração")
	public String dataDeAlteracao;
	
	@ExcelColumn(header = "Alteração CID - De")
	public String alteracaoCIDDe;
	
	@ExcelColumn(header = "Alteração CID - Para")
	public String alteracaoCIDPara;
	
	@ExcelColumn(header = "Observação 3")
	public String observacao3;
	
	@ExcelColumn(header = "Usuário 3")
	public String usuario3;
	
	@ExcelColumn(header = "Data de alteração 3")
	public String dataAlteracao3;
	
	@ExcelColumn(header = "Observação Automatização")
	public String observacaoAutomatizacao;
	
	
}
