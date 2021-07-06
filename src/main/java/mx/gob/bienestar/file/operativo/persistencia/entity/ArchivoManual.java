package mx.gob.bienestar.file.operativo.persistencia.entity;

public class ArchivoManual {

	private String name;
	private String fileName;
	private String extend;
	private String guid;

	private int idOperativo;
	private String operativo;
	private String fechaInicio;
	private String fechaFin;

	private String fechaCorte;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.guid  = java.util.UUID.randomUUID().toString();
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getGuid() { 
		return guid;
	}

	public int getIdOperativo() {
		return idOperativo;
	}

	public void setIdOperativo(int idOperativo) {
		this.idOperativo = idOperativo;
	}

	public String getOperativo() {
		return operativo;
	}

	public void setOperativo(String operativo) {
		this.operativo = operativo;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

}
