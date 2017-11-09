package pe.edu.cibertec.cateringdemo.Beans;

public class ListaClientesBE {
	
	private String id_cliente;
	private String nombre;
	private String apelldio;
	private String correo;
	private String tipoCliente;
	private String indicaAutoriza;
	private String estado;
	private String fechaRegistro;
	
	public ListaClientesBE(){}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApelldio() {
		return apelldio;
	}

	public void setApelldio(String apelldio) {
		this.apelldio = apelldio;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getIndicaAutoriza() {
		return indicaAutoriza;
	}

	public void setIndicaAutoriza(String indicaAutoriza) {
		this.indicaAutoriza = indicaAutoriza;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	

}
