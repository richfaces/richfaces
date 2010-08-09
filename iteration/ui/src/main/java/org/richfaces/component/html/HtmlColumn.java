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

package org.richfaces.component.html;

import org.richfaces.component.UIColumn;

public class HtmlColumn extends UIColumn {

    public static final String COMPONENT_TYPE = "org.richfaces.Column";

    public static final String COMPONENT_FAMILY = "org.richfaces.Column";
    
    enum PropertyKeys {
        breakBefore, rowspan, colspan, selfSorted, sortBy
    }
    

    public int getColspan() {
        return (Integer) getStateHelper().eval(PropertyKeys.colspan, Integer.MIN_VALUE);
    }

    public void setColspan(int colspan) {
        getStateHelper().put(PropertyKeys.colspan, Integer.valueOf(colspan));
    }

    public int getRowspan() {
        return (Integer) getStateHelper().eval(PropertyKeys.rowspan, Integer.MIN_VALUE);
    }

    public void setRowspan(int rowspan) {
        getStateHelper().put(PropertyKeys.rowspan, Integer.valueOf(rowspan));
    }

    public boolean isSelfSorted() {
        return (Boolean) getStateHelper().eval(PropertyKeys.selfSorted, true);
    }

    public void setSelfSorted(boolean selfSorted) {
        getStateHelper().put(PropertyKeys.selfSorted, selfSorted);
    }

    public Object getSortBy() {
        return getStateHelper().eval(PropertyKeys.sortBy);
    }

    public void setSortBy(Object sortBy) {
        getStateHelper().put(PropertyKeys.sortBy, sortBy);
    }

    public boolean isBreakBefore() {
        return (Boolean) getStateHelper().eval(PropertyKeys.breakBefore, false);
    }

    public void setBreakBefore(boolean breakBefore) {
        getStateHelper().put(PropertyKeys.breakBefore, breakBefore);
    }
}
