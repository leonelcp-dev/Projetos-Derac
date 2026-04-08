package dominiosSIRESP;

import modelosDados.ExcelColumn;

public class UnidadeSIRESP {

	@ExcelColumn(header = "COD_UNIDADE")
    private String codUnidade;

    @ExcelColumn(header = "CNES")
    private String cnes;
    
    @ExcelColumn(header = "UNIDADE_FANTASIA")
    private String unidadeFantasia;
    
    @ExcelColumn(header = "RAZAO_SOCIAL")
    private String razaoSocial;
    
    @ExcelColumn(header = "ENDERECO")
    private String endereco;
    
    @ExcelColumn(header = "ENDERECO_NUMERO")
    private String enderecoNumero;
    
    @ExcelColumn(header = "ENDERECO_COMPLEMENTO")
    private String enderecoComplemento;
    
    @ExcelColumn(header = "BAIRRO")
    private String bairro;
    
    @ExcelColumn(header = "MUNICIPIOO")
    private String municipio;
    
    @ExcelColumn(header = "CODMUNGEST")
    private String codMunGest;
    
    @ExcelColumn(header = "UF")
    private String uf;
    
    @ExcelColumn(header = "TEL_DDD")
    private String tel_ddd;
    
    @ExcelColumn(header = "TEL_1")
    private String tel_1;

    private int quantidadeImportar;
    private int quantidadeNaoImportar;
    
	public String getCodUnidade() {
		return codUnidade;
	}
	
	public void setCodUnidade(String codUnidade) {
		this.codUnidade = codUnidade;
	}
	
	public String getCnes() {
		return cnes;
	}
	
	public void setCnes(String cnes) {
		this.cnes = cnes;
	}
	
	public String getUnidadeFantasia() {
		return unidadeFantasia;
	}
	
	public void setUnidadeFantasia(String unidadeFantasia) {
		this.unidadeFantasia = unidadeFantasia;
	}
	
	public String getRazaoSocial() {
		return razaoSocial;
	}
	
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public String getEnderecoNumero() {
		return enderecoNumero;
	}
	
	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}
	
	public String getEnderecoComplemento() {
		return enderecoComplemento;
	}
	
	public void setEnderecoComplemento(String enderecoComplemento) {
		this.enderecoComplemento = enderecoComplemento;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public String getMunicipio() {
		return municipio;
	}
	
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	public String getCodMunGest() {
		return codMunGest;
	}
	
	public void setCodMunGest(String codMunGest) {
		this.codMunGest = codMunGest;
	}
	
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	public String getTel_ddd() {
		return tel_ddd;
	}
	
	public void setTel_ddd(String tel_ddd) {
		this.tel_ddd = tel_ddd;
	}
	
	public String getTel_1() {
		return tel_1;
	}
	
	public void setTel_1(String tel_1) {
		this.tel_1 = tel_1;
	}
	
	public int getQuantidadeImportar() {
		return quantidadeImportar;
	}
	
	public void setQuantidadeImportar(int quantidadeImportar) {
		this.quantidadeImportar = quantidadeImportar;
	}
	
	public int getQuantidadeNaoImportar() {
		return quantidadeNaoImportar;
	}
	
	public void setQuantidadeNaoImportar(int quantidadeNaoImportar) {
		this.quantidadeNaoImportar = quantidadeNaoImportar;
	}


}
