package modelosDados;


public class DeParaEspecialidadesPadronizadas {

	@ExcelColumn(header = "De")
    private String de;

    @ExcelColumn(header = "Para")
    private String para;
    
    public String getDe()
    {
    	return de;
    }
    
    public void setDe(String de)
    {
    	this.de = de;
    }
    
    public String getPara()
    {
    	return para;
    }
    
    public void setPara(String para)
    {
    	this.para = para;
    }
}
