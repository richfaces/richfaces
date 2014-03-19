/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.demo.arrangeablemodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class PersistenceService {
    private static final Logger LOGGER = Logger.getLogger(PersistenceService.class.getName());
    private EntityManagerFactory entityManagerFactory;

    public EntityManager getEntityManager() {
        Map<Object, Object> attributes = FacesContext.getCurrentInstance().getAttributes();

        EntityManager manager = (EntityManager) attributes.get(PersistenceService.class);

        if (manager == null) {
            manager = entityManagerFactory.createEntityManager();
            attributes.put(PersistenceService.class, manager);
            manager.getTransaction().begin();
        }

        return manager;
    }

    void closeEntityManager() {
        Map<Object, Object> attributes = FacesContext.getCurrentInstance().getAttributes();

        EntityManager entityManager = (EntityManager) attributes.remove(PersistenceService.class);

        if (entityManager != null) {
            try {
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                try {
                    entityManager.getTransaction().rollback();
                } catch (Exception e1) {
                    LOGGER.log(Level.SEVERE, e1.getMessage(), e1);
                }
            } finally {
                entityManager.close();
            }
        }
    }

    @PostConstruct
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("richfaces-showcase", new Properties());

        EntityManager em = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            for (Person person : parseTestData()) {
                em.persist(person);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private List<Person> parseTestData() throws Exception {
        InputStream dataStream = null;
        try {
            dataStream = PersistenceService.class.getResourceAsStream("data.xml");
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Node node = documentBuilder.parse(dataStream).getDocumentElement();

            List<Person> persons = Lists.newArrayList();

            for (Node personNode = node.getFirstChild(); personNode != null; personNode = personNode.getNextSibling()) {
                if (personNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Person person = new Person();
                persons.add(person);

                for (Node personDataNode = personNode.getFirstChild(); personDataNode != null; personDataNode = personDataNode
                        .getNextSibling()) {
                    if (personDataNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    String nodeName = personDataNode.getNodeName();
                    String text = personDataNode.getTextContent();
                    if ("name".equals(nodeName)) {
                        person.setName(text);
                    } else if ("surname".equals(nodeName)) {
                        person.setSurname(text);
                    } else if ("email".equals(nodeName)) {
                        person.setEmail(text);
                    }
                }
            }

            return persons;
        } finally {
            try {
                dataStream.close();
            }
            catch (IOException e){
                // Swallow
            }
        }
    }

    @PreDestroy
    public void destroy() {
        entityManagerFactory.close();
        entityManagerFactory = null;
    }
}
