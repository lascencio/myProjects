package beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PostMain {
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		Usuario usuario = (Usuario)context.getBean("usuario");
		
		//Usuario usuario = new Usuario();
		Articulo articulo = new Articulo();
		usuario.post(articulo);
	}
}
