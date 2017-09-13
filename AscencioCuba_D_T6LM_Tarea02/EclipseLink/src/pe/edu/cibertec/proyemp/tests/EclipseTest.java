package pe.edu.cibertec.proyemp.tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import pe.edu.cibertec.proyemp.beans.Especialidad;
import pe.edu.cibertec.proyemp.beans.Programador;

public class EclipseTest {

	private EntityManager manager;

	public EclipseTest(EntityManager manager) {
		super();
		this.manager = manager;
	}

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("EclipsePersistence");
		EntityManager manager = factory.createEntityManager();

		EclipseTest test = new EclipseTest(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			test.crearProgramador();
			test.crearProgramadoresCascada();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();
		test.listarProgramadores();
		System.out.println("...done");
	}

	private void listarProgramadores() {
		List<Programador> resulList = manager.createQuery("select p from Programador p", Programador.class).getResultList();
		System.out.println("Nro de programadores: " + resulList.size());
		for(Programador p : resulList){
			System.out.println(p.toString());
		}
	}

	private void crearProgramadoresCascada() {
		Especialidad lp = new Especialidad("C#");
		Date date = new Date();
		Programador p1 = new Programador("Carlos", lp, date);
		Programador p2 = new Programador("Mariano", lp, date);
		Programador p3 = new Programador("Christian", lp, date);
		
		List<Programador> programers =  Arrays.asList(p1,p2,p3);
		
		lp.setProgramadores(programers);
		
		manager.persist(lp);
		
	}

	private void crearProgramador() {
		Especialidad lp = new Especialidad("java");
//		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		manager.persist(lp);
		manager.persist(new Programador("David", lp, date));
		manager.persist(new Programador("Mauricio", lp, date));
		
	}
}