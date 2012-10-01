package org.richfaces.photoalbum.service;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/*
 * This class serves to provide an EntityManager to all classes that inject it.
 */
public class Resources {
    @SuppressWarnings("unused")
    @Produces
    @PersistenceContext
    private EntityManager em;
}
