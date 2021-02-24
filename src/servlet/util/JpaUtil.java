package servlet.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

	private static EntityManager em=null;
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("c_jpa");
	
	static {
		pegarConexao();
	}
	
	public static EntityManager pegarConexao() {
		if(em==null) {
			em = emf.createEntityManager();
		}
		return em;
	}
	
}