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

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.richfaces.component.SortOrder;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 */
@ManagedBean
@SessionScoped
public class PersonBean implements Serializable {
    private static final long serialVersionUID = -5156711102367948040L;

    private static final class PersonDataModel extends JPADataModel<Person> {
        private PersonDataModel(EntityManager entityManager) {
            super(entityManager, Person.class);
        }

        @Override
        protected Object getId(Person t) {
            return t.getId();
        }
    }

    private Map<String, SortOrder> sortOrders = Maps.newHashMapWithExpectedSize(1);
    private Map<String, String> filterValues = Maps.newHashMap();
    private String sortProperty;

    public PersonBean() {
        sortOrders.put("name", SortOrder.unsorted);
        sortOrders.put("surname", SortOrder.unsorted);
        sortOrders.put("email", SortOrder.unsorted);
    }

    private EntityManager lookupEntityManager() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PersistenceService persistenceService = facesContext.getApplication().evaluateExpressionGet(facesContext,
                "#{persistenceService}", PersistenceService.class);
        return persistenceService.getEntityManager();
    }

    public Map<String, SortOrder> getSortOrders() {
        return sortOrders;
    }

    public Map<String, String> getFilterValues() {
        return filterValues;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public void setSortProperty(String sortPropety) {
        this.sortProperty = sortPropety;
    }

    public void toggleSort() {
        for (Entry<String, SortOrder> entry : sortOrders.entrySet()) {
            SortOrder newOrder;

            if (entry.getKey().equals(sortProperty)) {
                if (entry.getValue() == SortOrder.ascending) {
                    newOrder = SortOrder.descending;
                } else {
                    newOrder = SortOrder.ascending;
                }
            } else {
                newOrder = SortOrder.unsorted;
            }

            entry.setValue(newOrder);
        }
    }

    public Object getDataModel() {
        return new PersonDataModel(lookupEntityManager());
    }
}