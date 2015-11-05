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
package org.richfaces.component;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.RowColumnStyleProps;
import org.richfaces.renderkit.RowHolderBase;

/**
 * <p>The &lt;rich:columnGroup&gt; component combines multiple columns in a single row to organize complex parts of a
 * table. The resulting effect is similar to using the breakRowBefore attribute of the &lt;rich:column&gt; component,
 * but is clearer and easier to follow in the source code.</p>
 */
@JsfComponent(type = AbstractColumnGroup.COMPONENT_TYPE, family = AbstractColumnGroup.COMPONENT_FAMILY, renderer = @JsfRenderer(type = "org.richfaces.ColumnGroupRenderer"), tag = @Tag(name = "columnGroup"))
public abstract class AbstractColumnGroup extends UIPanel implements Row, Column, RowColumnStyleProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ColumnGroup";
    public static final String COMPONENT_FAMILY = "org.richfaces.ColumnGroup";

    public Iterator<UIComponent> columns() {
        return new DataTableColumnsIterator(this);
    }

    public void setBreakBefore(boolean newBreakBefore) {
        throw new IllegalStateException("Property 'breakBefore' for subtable is read-only");
    }

    public void setRowKey(FacesContext context, Object rowKey) {
        // columnGroup doesn't have data model
    }

    public void walk(FacesContext context, DataVisitor visitor, Object argument) {
        if (!(argument instanceof RowHolderBase)) {
            return;
        }

        // TODO nick - implement in the proper way
        visitor.process(context, null, argument);
    }

    enum Properties {
        columnClasses,
        rowClasses
    }

    @Attribute(hidden = true)
    public String getColumnClasses() {
        return (String) getStateHelper().eval(Properties.columnClasses, getParentTable().getColumnClasses());
    }

    @Attribute(hidden = true)
    public String getRowClasses() {
        return (String) getStateHelper().eval(Properties.rowClasses, getParentTable().getRowClasses());
    }

    public RowColumnStyleProps getParentTable() {
        return ComponentIterators.getParent(this, UIDataTableBase.class);
    }
}
