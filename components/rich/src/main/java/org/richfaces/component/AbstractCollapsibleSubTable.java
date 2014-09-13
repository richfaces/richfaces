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

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.richfaces.StateHolderArray;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.EventsRowProps;
import org.richfaces.component.attribute.IterationProps;
import org.richfaces.component.attribute.RowsProps;
import org.richfaces.component.attribute.SequenceProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.component.attribute.TableStyleProps;
import org.richfaces.event.CollapsibleSubTableToggleEvent;
import org.richfaces.event.CollapsibleSubTableToggleListener;
import org.richfaces.taglib.CollapsibleSubTableHandler;

/**
 * <p>
 * The &lt;rich:collapsibleSubTable&gt; component acts as a child element to a &lt;rich:dataTable&gt; component. The
 * &lt;rich:collapsibleSubTable&gt; component iterates through the child collections in the currently iterated object to create
 * master-detail tables.
 * </p>
 *
 * @author Anton Belevich
 */
@JsfComponent(type = AbstractCollapsibleSubTable.COMPONENT_TYPE,
        family = AbstractCollapsibleSubTable.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.CollapsibleSubTableRenderer"),
        tag = @Tag(name = "collapsibleSubTable", handlerClass = CollapsibleSubTableHandler.class, type = TagType.Facelets))
public abstract class AbstractCollapsibleSubTable extends UIDataTableBase implements Column, Expandable, EventsRowProps,
    RowsProps, StyleProps, SequenceProps, IterationProps, TableStyleProps {
    public static final String COMPONENT_TYPE = "org.richfaces.CollapsibleSubTable";
    public static final String COMPONENT_FAMILY = UIDataTableBase.COMPONENT_FAMILY;
    public static final String MODE_AJAX = "ajax";
    public static final String MODE_SERVER = "server";
    public static final String MODE_CLIENT = "client";
    public static final int EXPANDED_STATE = 1;
    public static final int COLLAPSED_STATE = 0;

    enum PropertyKeys {
        expanded
    }

    /**
     * Determines the state of sub table: true (expanded), false (collapsed)
     */
    @Attribute
    public boolean isExpanded() {
        return (Boolean) getStateHelper().eval(PropertyKeys.expanded, true);
    }

    public void setExpanded(boolean expanded) {
        getStateHelper().put(PropertyKeys.expanded, expanded);
    }

    /**
     * Specifies what mode will be used to expand the sub table: client (default), ajax, server, none (can't be expanded)
     */
    @Attribute
    public abstract String getExpandMode();

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof CollapsibleSubTableToggleEvent) {
            CollapsibleSubTableToggleEvent toggleEvent = (CollapsibleSubTableToggleEvent) event;
            boolean newValue = toggleEvent.isExpanded();

            getStateHelper().put(PropertyKeys.expanded, newValue);

            FacesContext facesContext = getFacesContext();
            ELContext elContext = facesContext.getELContext();

            ValueExpression valueExpression = getValueExpression(PropertyKeys.expanded.toString());
            if (valueExpression != null && !valueExpression.isReadOnly(elContext)) {
                valueExpression.setValue(elContext, newValue);
            }

            if (getFacesContext().getPartialViewContext().isAjaxRequest()) {
                String render = resolveClientId(facesContext, this, BODY);

                getFacesContext().getPartialViewContext().getRenderIds().add(render);

                String togglerId = toggleEvent.getTogglerId();
                if (togglerId != null) {
                    getFacesContext().getPartialViewContext().getRenderIds().add(togglerId);
                }
            }
        }
        super.broadcast(event);
    }

    public boolean isBreakBefore() {
        return true;
    }

    public void setBreakBefore(boolean newBreakBefore) {
        throw new IllegalStateException("Property 'breakBefore' for subtable is read-only");
    }

    public String getSortExpression() {
        // SubTable is not sortable element.
        return null;
    }

    public void setSortExpression(String sortExpression) {
        throw new IllegalArgumentException("subtable is not sortable element");
    }

    public void addCollapsibleSubTableToggleListener(CollapsibleSubTableToggleListener listener) {
        addFacesListener(listener);
    }

    public void removeCollapsibleSubTableToggleListener(CollapsibleSubTableToggleListener listener) {
        removeFacesListener(listener);
    }

    public CollapsibleSubTableToggleListener[] getCollapsibleSubTableToggleListener() {
        return (CollapsibleSubTableToggleListener[]) getFacesListeners(CollapsibleSubTableToggleListener.class);
    }

    public void setIterationState(Object stateObject) {
        StateHolderArray stateHolderList = (StateHolderArray) stateObject;

        if (stateHolderList != null && !stateHolderList.isEmpty()) {
            super.setIterationState(stateHolderList.get(0));
            getStateHelper().put(PropertyKeys.expanded, (Boolean) stateHolderList.get(1));
        } else {
            super.setIterationState(null);
            getStateHelper().put(PropertyKeys.expanded, null);
        }
    }

    public Object getIterationState() {
        StateHolderArray holderList = new StateHolderArray();

        holderList.add(super.getIterationState());
        holderList.add(getStateHelper().get(PropertyKeys.expanded));

        return holderList;
    }

    public String getSortingAndFilteringRenderTargetId(FacesContext facesContext) {
        return getClientId(facesContext) + "@" + BODY;
    }
}