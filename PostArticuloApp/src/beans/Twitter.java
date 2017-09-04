package beans;

public class Twitter implements RedSocial {

	@Override
	public void post(Articulo articulo) {
		System.out.println("posteando con Twitter");
	}

}
