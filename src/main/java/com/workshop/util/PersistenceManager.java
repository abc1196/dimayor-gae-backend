package com.workshop.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {

	private static EntityManagerFactory entityManagerFactory = null;

	public static EntityManager getEntityManager() {

		if (entityManagerFactory == null)
			entityManagerFactory = Persistence.createEntityManagerFactory("teams");

		return entityManagerFactory.createEntityManager();
	}
	
}
