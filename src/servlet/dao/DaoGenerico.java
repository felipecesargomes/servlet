package servlet.dao;

import javax.persistence.EntityManager;

import servlet.util.JpaUtil;

public class DaoGenerico<T> {

	private EntityManager em = JpaUtil.pegarConexao();
	
	public void cadastrar(T t) {
		em.getTransaction().begin();
		em.persist(t);
		em.getTransaction().commit();
	}
	
}
