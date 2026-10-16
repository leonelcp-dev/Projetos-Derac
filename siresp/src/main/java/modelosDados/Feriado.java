package modelosDados;


public class Feriado {

	@ExcelColumn(header = "Data")
    private String data;

    @ExcelColumn(header = "Feriado")
    private String feriado;
    
    public String getData()
    {
    	return data;
    }
    
    public void setData(String data)
    {
    	this.data = data;
    }
    
    public String getFeriado()
    {
    	return feriado;
    }
    
    public void setPara(String feriado)
    {
    	this.feriado = feriado;
    }
}
