package dominiosSIRESP;

import modelosDados.ExcelColumn;

public class StatusAgendamentoSIRESP {

	@ExcelColumn(header = "ID_STATUS")
    private String idStatus;

    @ExcelColumn(header = "STATUS")
    private String status;

	public String getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(String idStatus) {
		this.idStatus = idStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


    
}
