package beans;

public class Usuario {
	RedSocial redSocial;

	public void setRedSocial(RedSocial redSocial) {
		this.redSocial = redSocial;
	}

	public void post(Articulo articulo){
		redSocial.post(articulo);
	}
}
