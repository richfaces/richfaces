/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.demo.jpa;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import org.richfaces.component.SortOrder;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean
@SessionScoped
public class PersonBean {
    private static final class PersonDataModel extends JPADataModel<Person> {
        private PersonDataModel(EntityManager entityManager) {
            super(entityManager, Person.class);
        }

        @Override
        protected Object getId(Person t) {
            return t.getId();
        }
    }

    @ManagedProperty(value = "#{persistenceService}")
    private PersistenceService persistenceService;
    private Map<String, SortOrder> sortOrders = Maps.newHashMapWithExpectedSize(1);
    private Map<String, String> filterValues = Maps.newHashMap();

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Map<String, SortOrder> getSortOrders() {
        return sortOrders;
    }

    public Map<String, String> getFilterValues() {
        return filterValues;
    }

    public SortOrder getSortOrder(String name) {
        SortOrder sortOrder = getSortOrders().get(name);

        if (sortOrder == null) {
            sortOrder = SortOrder.unsorted;
        }

        return sortOrder;
    }

    public void switchSortOrder(String name) {
        SortOrder newSortOrder = null;

        switch (getSortOrder(name)) {
            case unsorted:
                newSortOrder = SortOrder.ascending;
                break;
            case ascending:
                newSortOrder = SortOrder.descending;
                break;
            case descending:
                newSortOrder = SortOrder.ascending;
                break;
            default:
                throw new IllegalStateException();
        }

        sortOrders.clear();
        sortOrders.put(name, newSortOrder);
    }

    public Object getDataModel() {
        return new PersonDataModel(persistenceService.getEntityManager());
    }
}