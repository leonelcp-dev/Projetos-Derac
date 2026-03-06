package modelosDados;


public class UsuarioSIRESP {

    @ExcelColumn(header = "Indice")
    private String prioridade;

    @ExcelColumn(header = "Unidade")
    private String unidade;

    @ExcelColumn(header = "Nome Completo")
    private String nomeCompleto;

    @ExcelColumn(header = "CPF", digitsOnly = true)
    private String cpf;

    @ExcelColumn(header = "RG", digitsOnly = true)
    private String rg;

    @ExcelColumn(header = "E-mail do colaborador")
    private String email;

    @ExcelColumn(header = "Número de telefone do colaborador", digitsOnly = true)
    private String telefone;

    @ExcelColumn(header = "Senha provisória")
    private String senhaProvisoria;

    @ExcelColumn(header = "Perfil do Login")
    private String perfil;

    @ExcelColumn(header = "Horário")
    private String horarioAcesso;

    // Nome de usuário que será criado
    @ExcelColumn(header = "Login")
    private String criacaoLogin;
    
    @ExcelColumn(header = "Módulo")
    private String modulo;   
    
    @ExcelColumn(header = "Executado")
    private String executado;

    @ExcelColumn(header = "Observação")
    private String observacao;
    
//    @ExcelColumn(header = "Data de Criação")
    private String dataDeCriacao;
    
    @ExcelColumn(header = "Status")
    private String status;
    
    public void setPrioridade(String prioridade)
    {
    	this.prioridade = prioridade;
    }
    
    public String getPrioridade()
    {
    	return prioridade;
    }

    
    public void setUnidade(String unidade)
    {
    	this.unidade = unidade;
    }
    
    public String getUnidade()
    {
    	return unidade;
    }
    
    public void setNomeCompleto(String nomeCompleto)
    {
    	this.nomeCompleto = nomeCompleto;
    }
    
    public String getNomeCompleto()
    {
    	return nomeCompleto;
    }

    public void setCPF(String cpf)
    {
    	this.cpf = cpf;
    }
    
    public String getCPF()
    {
    	return cpf;
    }
    
    public void setRG(String rg)
    {
    	this.rg = rg;
    }
    
    public String getRG()
    {
    	return rg;
    }

    public void setEmail(String email)
    {
    	this.email = email;
    }
    
    public String getEmail()
    {
    	return email;
    }
   
    public void setTelefone(String telefone)
    {
    	this.telefone = telefone;
    }
    
    public String getTelefone()
    {
    	return telefone;
    }
    
    public void setSenhaProvisoria(String senhaProvisoria)
    {
    	this.senhaProvisoria = senhaProvisoria;
    }
    
    public String getSenhaProvisoria()
    {
    	return senhaProvisoria;
    }

    public void setPerfil(String perfil)
    {
    	this.perfil = perfil;
    }
    
    public String getPerfil()
    {
    	return perfil;
    }

    public void setHorarioDeAcessoAoPortal(String horarioAcesso)
    {
    	this.horarioAcesso = horarioAcesso;
    }
    
    public String getHorarioDeAcessoAoPortal()
    {
    	return horarioAcesso;
    }
    
    public void setLogin(String criacaoLogin)
    {
    	this.criacaoLogin = criacaoLogin;
    }
    
    public String getLogin()
    {
    	return criacaoLogin;
    }
    
    public void setModulo(String modulo)
    {
    	this.modulo = modulo;
    }
    
    public String getModulo()
    {
    	return modulo;
    }
    
    public void setExecutado(String executado)
    {
    	this.executado = executado;
    }
    
    public String getExecutado()
    {
    	return executado;
    }

    public void setObservacao(String observacao)
    {
    	this.observacao = observacao;
    }
    
    public String getObservacao()
    {
    	return observacao;
    }
    
    public void setDataDeCriacao(String dataDeCriacao)
    {
    	this.dataDeCriacao = dataDeCriacao;
    }
    
    public String getDataDeCriacao()
    {
    	return dataDeCriacao;
    }
    
    public void setStatus(String status)
    {
    	this.status = status;
    }
    
    public String getStatus()
    {
    	return status;
    }

}
