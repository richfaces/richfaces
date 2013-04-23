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

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@SessionScoped
@Named
public class IterationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] array = {"3","6","4","8","2","1","5","7","9","0" };
    private String[] columnsOrder = {"column2", "column1", "column3"};
    private List<String> values = Arrays.asList(array);
    private String selectedValue;
    private List<String> data;


    public IterationBean() {
    }

    public List<String> getValues() {
        return values;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public void show() {
        data = values;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String[] getColumnsOrder() {
        return columnsOrder;
    }

    public String getColumnsOrderString() {
        StringBuilder sb = new StringBuilder();
        for (String order : columnsOrder) {
            sb.append(order);
        }
        return sb.toString();
    }

    public void setColumnsOrder(String columnsOrder) {
        this.columnsOrder = columnsOrder.split(",");
    }
}
