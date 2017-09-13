package pe.edu.cibertec.proyemp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TB_ESPECIALIDAD")
public class Especialidad {

	@Id
	@GeneratedValue
	@Column(name = "ESP_ID")
	private Integer idEspecialidad;

	@Column(name = "ESP_DESCRIPCION", length = 200, nullable = false)
	private String descripcion;

	@OneToMany(mappedBy = "especialidad", cascade = CascadeType.PERSIST)
	private List<Programador> programadores = new ArrayList<Programador>();

	public Especialidad() {
		super();
	}

	public Especialidad(String nombre) {
		super();
		this.descripcion = nombre;
	}

	public Integer getIdprogramador() {
		return idEspecialidad;
	}

	public void setIdprogramador(Integer idprogramador) {
		this.idEspecialidad = idprogramador;
	}

	public String getNombre() {
		return descripcion;
	}

	public void setNombre(String nombre) {
		this.descripcion = nombre;
	}

	public List<Programador> getProgramadores() {
		return programadores;
	}

	public void setProgramadores(List<Programador> programadores) {
		this.programadores = programadores;
	}

}
