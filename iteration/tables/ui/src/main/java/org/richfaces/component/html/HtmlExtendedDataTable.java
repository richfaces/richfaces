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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.faces.component.behavior.ClientBehaviorHolder;

import org.richfaces.component.UIExtendedDataTable;

public class HtmlExtendedDataTable extends UIExtendedDataTable implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.ExtendedDataTable";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(
        "selectionchange", "beforeselectionchange"));

    private static enum PropertyKeys {
        style, styleClass, frozenColumns
    }

    public HtmlExtendedDataTable() {
        setRendererType("org.richfaces.ExtendedDataTableRenderer");
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, "");
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, "");
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public Integer getFrozenColumns() {
        return (Integer) getStateHelper().eval(PropertyKeys.frozenColumns, 0);
    }

    public void setFrozenColumns(Integer frozenColumns) {
        getStateHelper().put(PropertyKeys.frozenColumns, frozenColumns);
    }

    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    public String getDefaultEventName() {
        return "selectionchange";
    }
}
