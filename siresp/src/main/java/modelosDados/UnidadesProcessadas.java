package modelosDados;


public class UnidadesProcessadas {

    @ExcelColumn(header = "Unidade")
    private String unidade;

    @ExcelColumn(header = "Status")
    private String status;


    public void setUnidade(String unidade)
    {
    	this.unidade = unidade;
    }
    
    public String getUnidade()
    {
    	return unidade;
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
