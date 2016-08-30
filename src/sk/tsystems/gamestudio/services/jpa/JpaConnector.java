package sk.tsystems.gamestudio.services.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

abstract class JpaConnector implements AutoCloseable {
	// TODO non thread safe?? !!!
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	private static int connCount = 0; 
	JpaConnector() {
		super();
		connCount++; // increment connections to JPA, prevent closing on work
		System.err.printf("*** info: HIBERNATE connection count: %d <-------\n", connCount);
	}


	private static EntityManagerFactory getFactory() {
		if (factory == null || !factory.isOpen()) {
			factory = Persistence.createEntityManagerFactory("hibernatePersistenceUnit");
		}
		return factory;
	}

	synchronized EntityManager getEntityManager() {
		if (entityManager == null || !entityManager.isOpen()) {
			entityManager = getFactory().createEntityManager();
		}
		return entityManager;
	}


	synchronized void beginTransaction() {
		getEntityManager().getTransaction().begin();
	}

	synchronized void commitTransaction() {
		getEntityManager().getTransaction().commit();
	}

	private static void closeEntityManager() {
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
	}

	private static void closeEntityManagerFactory() {
		if (factory != null && factory.isOpen()) {
			factory.close();
		}
	}
	
	@Override
	synchronized public void close() throws Exception {
		System.err.printf("*** info: HIBERNATE connection count: %d <-------\n", connCount-1);
		if(--connCount>0)
			return;
		closeEntityManager();
		closeEntityManagerFactory();
	}
	
}
