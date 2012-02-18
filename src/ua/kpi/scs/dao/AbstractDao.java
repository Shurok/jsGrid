package ua.kpi.scs.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractDao {
	protected EntityManager em;

	@PersistenceContext
	public void setEntityManager(final EntityManager em) {
		this.em = em;
	}
}
