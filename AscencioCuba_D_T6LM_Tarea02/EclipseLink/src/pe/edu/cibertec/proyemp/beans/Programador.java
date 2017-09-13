package pe.edu.cibertec.proyemp.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_PROGRAMADOR")
public class Programador {
	@Id
	@GeneratedValue
	@Column(name = "PRO_ID")
	private Integer idprogramador;

	@Column(name = "PRO_NOMBRE", length = 200, nullable = false)
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "ESP_ID")
	private Especialidad especialidad;

	@Column(name = "PRO_FEC_REG")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaRegistro;

	public Programador(String nombre, Especialidad especialidad,Date fechaRegistro) {
		super();
		this.nombre = nombre;
		this.especialidad = especialidad;
		this.fechaRegistro = fechaRegistro;
	}

	public Programador() {
		super();
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Integer getIdprogramador() {
		return idprogramador;
	}

	public void setIdprogramador(Integer idprogramador) {
		this.idprogramador = idprogramador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	@Override
	public String toString() {
		return "Programador [idprogramador=" + idprogramador + ", nombre=" + nombre + ", especialidad=" + especialidad.getNombre()
				+ ", fechaRegistro=" +  fechaRegistro + "]";
	}
	
	

}