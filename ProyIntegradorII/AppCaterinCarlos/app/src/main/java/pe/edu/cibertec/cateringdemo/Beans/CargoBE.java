package pe.edu.cibertec.cateringdemo.Beans;

public class CargoBE {
	
	private int id_cargo;
	private String str_cargo;
	private int id_estado;
	
	public CargoBE(){}

	public int getId_cargo() {
		return id_cargo;
	}

	public void setId_cargo(int id_cargo) {
		this.id_cargo = id_cargo;
	}

	public String getStr_cargo() {
		return str_cargo;
	}

	public void setStr_cargo(String str_cargo) {
		this.str_cargo = str_cargo;
	}

	public int getId_estado() {
		return id_estado;
	}

	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}

	@Override
	public String toString() {
		return  str_cargo;
	}
}
