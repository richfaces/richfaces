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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.event.ToggleEvent;
import org.richfaces.event.ToggleListener;


/**
 * @author Anton Belevich
 *
 */
@JsfComponent(
    type = AbstractSubTable.COMPONENT_TYPE,
    family = AbstractSubTable.COMPONENT_FAMILY, 
    generate = "org.richfaces.component.UISubTable",
    renderer = @JsfRenderer(type = "org.richfaces.SubTableRenderer"),
    tag = @Tag(name = "subTable", handler = "org.richfaces.taglib.SubTableHandler", type = TagType.Facelets)
)
public abstract class AbstractSubTable extends UIDataTableBase implements Column, Expandable {
    
    public static final String COMPONENT_TYPE = "org.richfaces.SubTable";

    public static final String COMPONENT_FAMILY = UIDataTableBase.COMPONENT_FAMILY;

    public static final String MODE_AJAX = "ajax"; 
    
    public static final String MODE_SERVER = "server";
    
    public static final String MODE_CLIENT = "client";
    
    public static final int EXPAND_STATE = 1;
    
    public static final int COLLAPSE_STATE = 0;


    enum PropertyKeys {
        expanded
    }
            
    @Attribute(defaultValue = "true")
    public abstract boolean isExpanded();
    
    @Attribute(defaultValue = MODE_CLIENT)
    public abstract String getExpandMode();

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof ToggleEvent) {
            ToggleEvent toggleEvent = (ToggleEvent) event;
            boolean newValue = toggleEvent.isExpanded();
            getStateHelper().put(PropertyKeys.expanded, newValue);
            
            FacesContext facesContext = getFacesContext();
            ELContext elContext = facesContext.getELContext();

            ValueExpression valueExpression = getValueExpression(PropertyKeys.expanded.toString());
            if (valueExpression != null && !valueExpression.isReadOnly(elContext)) {
                valueExpression.setValue(elContext, newValue);
            }
            
            if(getFacesContext().getPartialViewContext().isAjaxRequest()) {
                String render = resolveClientId(facesContext, this, BODY);
                
                getFacesContext().getPartialViewContext().getRenderIds().add(render);
                
                String togglerId = toggleEvent.getTogglerId();
                if(togglerId != null) {
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
    
    public void addToggleListener(ToggleListener listener) {
        addFacesListener(listener);
    }
    
    public void removeToggleListener(ToggleListener listener) {
        removeFacesListener(listener);
    }

    public ToggleListener[] getToggleListeners() {
        return (ToggleListener[]) getFacesListeners(ToggleListener.class);
    }
    
    public void setIterationState(Object stateObject) {
        Object[] state = (Object[]) stateObject;
        if (state != null) {
            super.setIterationState(state[0]);
            getStateHelper().put(PropertyKeys.expanded, state[1]);
        } else {
            super.setIterationState(null);
            getStateHelper().put(PropertyKeys.expanded, null);
        }
    }
    
    public Object getIterationState() {
        Object [] state = new Object[2];
        state[0] = super.getIterationState();
        state[1] = getStateHelper().get(PropertyKeys.expanded); 
        return state;
    }
}


