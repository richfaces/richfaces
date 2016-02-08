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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxActivatorProps;
import org.richfaces.component.attribute.EventsRowProps;
import org.richfaces.component.attribute.IterationProps;
import org.richfaces.component.attribute.RowsProps;
import org.richfaces.component.attribute.SequenceProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.SelectionMode;
import org.richfaces.taglib.ExtendedDataTableHandler;

/**
 * <p> The &lt;rich:extendedDataTable&gt; component builds on the functionality of the &lt;rich:dataTable&gt; component,
 * adding features such as scrolling for the table body (both horizontal and vertical), Ajax loading for vertical
 * scrolling, frozen columns, row selection, and rearranging of columns. It also supports all the basic table features
 * such as sorting, filtering, and paging using the &lt;rich:dataScroller&gt; component. </p>
 *
 * @author Konstantin Mishin
 */
@JsfComponent(type = AbstractExtendedDataTable.COMPONENT_TYPE, family = AbstractExtendedDataTable.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.ExtendedDataTableRenderer"),
        tag = @Tag(name = "extendedDataTable", handlerClass = ExtendedDataTableHandler.class, type = TagType.Facelets))
public abstract class AbstractExtendedDataTable extends UIDataTableBase implements MetaComponentResolver, MetaComponentEncoder, EventsRowProps, RowsProps, StyleProps, StyleClassProps, SequenceProps, IterationProps, AjaxActivatorProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ExtendedDataTable";
    public static final String COMPONENT_FAMILY = UIDataTableBase.COMPONENT_FAMILY;
    public static final String SCROLL = "scroll";
    public static final String SUBMITTED_CLIENT_FIRST = "submittedClientFirst";
    public static final String OLD_CLIENT_FIRST = "oldClientFirst";
    private static final Logger RENDERKIT_LOG = RichfacesLogger.RENDERKIT.getLogger();

    protected enum PropertyKeys {
        clientFirst, clientRows
    }

    /**
     * Determines how many columns should not be vertically scrollable (should be "frozen").
     */
    @Attribute
    public abstract int getFrozenColumns();

    /**
     * Defines selection mode for the table: none, single (only one row can be selected), multiple (Ctrl/Shift keys are used for
     * multi-selection), multipleKeyboardFree (clicks are used for multi-selection). Default value - "multiple"
     */
    @Attribute
    public abstract SelectionMode getSelectionMode();

    /**
     * The client-side script method to be called after the EDT has been initialized, either after a page load, and an ajax update.
     */
    @Attribute(events = @EventName(value = "ready"))
    public abstract String getOnready();

    /**
     * The client-side script method to be called after the selection is changed.
     */
    @Attribute(events = @EventName(value = "selectionchange", defaultEvent = true))
    public abstract String getOnselectionchange();

    /**
     * Determines the order in which the columns should be rendered, left to right.
     * The Strings are the ids of the columns.
     */
    @Attribute
    public abstract String[] getColumnsOrder();

    /**
     * ValueBinding pointing at a property of a String to hold table state
     */
    @Attribute
    public abstract String getTableState();

    /**
     * The client-side script method to be called before the selection is changed.
     */
    @Attribute(events = @EventName("beforeselectionchange"))
    public abstract String getOnbeforeselectionchange();

    /**
     * If "true" a menu for controlling column visibility will be added to the table.
     * Requires the table to have a header. The column names can be customized with name attribute on rich:column.
     * Default value - "false".
     */
    @Attribute(defaultValue = "false")
    public abstract boolean isShowColumnControl();

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (SCROLL.equals(metaComponentId)) {
            Object oldRowKey = getRowKey();

            try {
                setRowKey(facesContext, null);
                return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
            } finally {
                try {
                    setRowKey(facesContext, oldRowKey);
                } catch (Exception e) {
                    RENDERKIT_LOG.error(e.getMessage(), e);
                }
            }
        }

        return super.resolveClientId(facesContext, contextComponent, metaComponentId);
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        if (SCROLL.equals(metaComponentId)) {
            Map<String, Object> attributes = getAttributes();
            Integer submittedClientFirst = (Integer) attributes.remove(SUBMITTED_CLIENT_FIRST);
            if (submittedClientFirst != null) {
                attributes.put(OLD_CLIENT_FIRST, getClientFirst());
                setClientFirst(submittedClientFirst);
            }
        }

        super.encodeMetaComponent(context, metaComponentId);
    }

    protected boolean visitDataChildren(VisitContext visitContext, final VisitCallback callback, boolean visitRows) {
        if (visitContext instanceof ExtendedVisitContext && visitRows) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) visitContext;

            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {
                // TODO nick - call preEncodeBegin(...) and emit PreRenderEvent
                setRowKey(visitContext.getFacesContext(), null);

                VisitResult result;

                result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, SCROLL);

                if (result == VisitResult.ACCEPT) {
                    // TODO nick - visit scroll?
                } else if (result == VisitResult.COMPLETE) {
                    return true;
                }
            }
        }

        return super.visitDataChildren(visitContext, callback, visitRows);
    }

    protected int getActualFirst() {
        return getFirst() + getClientFirst();
    }

    protected int getActualRows() {
        int rows = getClientRows();

        if (rows > 0) {
            int r = getRows();
            if (r > 0 && r < rows) {
                rows = r;
            }
        } else {
            rows = getRows();
        }

        return rows;
    }

    public int getClientFirst() {
        return (Integer) getStateHelper().eval(PropertyKeys.clientFirst, 0);
    }

    public void setClientFirst(int clientFirst) {
        getStateHelper().put(PropertyKeys.clientFirst, clientFirst);
        updateState();
    }

    public void setFirst(int first) {
        super.setFirst(first);
        setClientFirst(0);
    }

    /**
     * Use to switch Extended Data Table to AJAX lazy-loading mode. Specify number of rows rows to be loaded with one request.
     * If this attribute is set to "0", all rows are loaded. (Default value: 0)
     */
    @Attribute(generate = false, defaultValue = "0")
    public int getClientRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.clientRows, 0);
    }

    public void setClientRows(int clientRows) {
        getStateHelper().put(PropertyKeys.clientRows, clientRows);

        updateState();
    }

    @SuppressWarnings("deprecation")
    public void setValueBinding(String name, javax.faces.el.ValueBinding binding) {
        super.setValueBinding(name, binding);

        // TODO nick - clientFirst?
        if ("clientRows".equals(name)) {
            updateState();
        }
    }

    public void setValueExpression(String name, ValueExpression binding) {
        super.setValueExpression(name, binding);

        // TODO nick - clientFirst?
        if ("clientRows".equals(name)) {
            updateState();
        }
    }

    /**
     * The collection of keys for currently selected table rows (generated from data model by rowKeyConverter).
     */
    @Attribute
    public abstract Collection<Object> getSelection();
}
