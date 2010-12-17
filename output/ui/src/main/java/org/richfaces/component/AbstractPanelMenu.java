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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.PanelMenuMode;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public abstract class AbstractPanelMenu extends UIOutput implements ItemChangeSource {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenu";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenu";

    private String submittedActiveItem;

    private enum PropertyKeys {
        immediate
    }

    protected AbstractPanelMenu() {
        setRendererType("org.richfaces.PanelMenu");
    }

    @Override
    public void processDecodes(FacesContext context) {
        super.processDecodes(context);

        //TODO nick - is component immediate = true only?
        //TODO nick - processValue should be executed in context of component, i.e. when 'component' EL variable is set

        processValue(context);
    }

    private void processValue(FacesContext context) {
        try {
            if (context == null) {
                throw new NullPointerException();
            }

            // Submitted value == null means "the component was not submitted at all".
            String activeItem = getSubmittedActiveItem();
            if (activeItem == null) {
                return;
            }

            String previous = (String) getValue();
            setActiveItem(activeItem);
            setSubmittedActiveItem(null);

            if (previous == null || !previous.equalsIgnoreCase(activeItem)) {
                queueEvent(new ItemChangeEvent(this, previous, activeItem));
            }
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if ((event instanceof ItemChangeEvent) && (event.getComponent() == this)) {
            setEventPhase(event);
        }

        super.queueEvent(event);
    }

    protected void setEventPhase(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else if (isBypassUpdates()) {
            event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
        } else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        //TODO nick - check for (isBypassUpdates() || isImmediate()) can be removed
        if (event instanceof ItemChangeEvent && (isBypassUpdates() || isImmediate())) {
            getFacesContext().renderResponse();
        }
    }



    public String getSubmittedActiveItem() {
        return this.submittedActiveItem;
    }

    public void setSubmittedActiveItem(String submittedValue) {
        this.submittedActiveItem = String.valueOf(submittedValue);
    }

    public String getActiveItem() {
        return (String) getValue();
    }

    //TODO nick - where is EL-expression updated?
    public void setActiveItem(String value) {
        setValue(value);
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("activeItem".equals(name)) {
            super.setValueExpression("value", binding);
        } else {
            super.setValueExpression(name, binding);
        }
    }

    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public abstract boolean isDisabled();

    public abstract String getExpandEvent();

    public abstract String getCollapseEvent();

    public abstract PanelMenuMode getGroupMode();

    public abstract boolean isExpandSingle();

    public abstract PanelMenuMode getItemMode();

    public abstract boolean isBubbleSelection();

    public abstract String getItemChangeListener();

    public abstract boolean isBypassUpdates();

    public abstract boolean isLimitRender();

    public abstract Object getData();

    public abstract String getStatus();

    public abstract Object getExecute();

    public abstract Object getRender();

    public AbstractPanelMenuItem getItem(String itemName) {
        if (itemName == null) {
            throw new IllegalArgumentException("Icon name can't be null");
        }

        return getItem(itemName, this);
    }

    private static AbstractPanelMenuItem getItem(String itemName, UIComponent comp) {
        if (comp instanceof AbstractPanelMenu) {
            return null;
        }

        if (comp instanceof AbstractPanelMenuItem
                && itemName.equals(((AbstractPanelMenuItem) comp).getName())) {
            return (AbstractPanelMenuItem) comp;
        }

        for (UIComponent item : comp.getChildren()) {
            AbstractPanelMenuItem resItem = getItem(itemName, item);
            if (resItem != null) {
                return resItem;
            }
        }

        return null;
    }

    // ------------------------------------------------ Event Processing Methods

    public void addItemChangeListener(ItemChangeListener listener) {
        addFacesListener(listener);
    }

    public ItemChangeListener[] getItemChangeListeners() {
        return (ItemChangeListener[]) getFacesListeners(ItemChangeListener.class);
    }

    public void removeItemChangeListener(ItemChangeListener listener) {
        removeFacesListener(listener);
    }
}
