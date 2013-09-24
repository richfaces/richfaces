/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 **/
package org.richfaces.component.extendedDataTable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.richfaces.model.SortOrder;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@RequestScoped
@Named
public class IterationBuiltInBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] array = {"3","6","4","8","2","1","5","7","9","0" };
    private List<String> values = Arrays.asList(array);

    private SortOrder sortOrder;// = SortOrder.ascending;
    private SortOrder sortOrder2;// = SortOrder.ascending;
    private Long filterValue = 10L;


    public IterationBuiltInBean() {
    }

    public List<String> getValues() {
        return values;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public SortOrder getSortOrder2() {
        return sortOrder2;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(Long filterValue) {
        this.filterValue = filterValue;
    }

}
