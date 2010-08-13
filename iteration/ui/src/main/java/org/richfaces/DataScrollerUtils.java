/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.richfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;

import org.richfaces.component.AbstractDataScroller;

public final class DataScrollerUtils {

    protected enum PropertyKeys {
        rowCount, rows
    }
    
    private DataScrollerUtils(){
    }
    
    
    public static int getPageCount(UIComponent data, int rowCount, int rows) {
        int pageCount;
        if (rows > 0) {
            pageCount = rows <= 0 ? 1 : rowCount / rows;
            if (rowCount % rows > 0) {
                pageCount++;
            }
            if (pageCount == 0) {
                pageCount = 1;
            }
        } else {
            rows = 1;
            pageCount = 1;
        }
        return pageCount;
    }

    public static int getRows(UIComponent component) {
        int row = (Integer) component.getAttributes().get("rows");
        if (row == 0) {
            row = getRowCount(component);
        }

        return row;
    }

    public static int getRowCount(UIComponent component) {
        return (Integer) eval(PropertyKeys.rowCount, component, 0);
    }

    protected static Object eval(Serializable key, UIComponent component, Object defaultValue) {
        String name = key.toString();
        Object retObject = component.getAttributes().get(name);
        return retObject != null ? retObject : defaultValue;
    }

    public static UIComponent findParentContainer(UIComponent component) {
        UIComponent parent = component.getParent();
        if (!(component instanceof NamingContainer)) {
            findParentContainer(parent);
        }
        return parent;
    }

    public static List<AbstractDataScroller> findDataScrollers(UIComponent dataTable) {
        List<AbstractDataScroller> datascrollers = new ArrayList<AbstractDataScroller>();
        Map<String, UIComponent> facets = dataTable.getFacets();
        Set<Entry<String, UIComponent>> entries = facets.entrySet();

        for (Entry<String, UIComponent> entry : entries) {
            findBelow(entry.getValue(), datascrollers);
        }

        UIComponent parent = findParentContainer(dataTable);
        if (parent != null) {
            findBelow(parent, datascrollers);
        }

        return datascrollers;
    }

    protected static void findBelow(UIComponent component, List<AbstractDataScroller> result) {
        if ((component instanceof AbstractDataScroller) && component.isRendered()) {
            result.add((AbstractDataScroller) component);

        } else {
            for (UIComponent child : component.getChildren()) {
                if (!(child instanceof NamingContainer)) {
                    findBelow(child, result);
                }
            }
        }

    }
}
