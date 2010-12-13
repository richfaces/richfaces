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
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.PanelToggleEvent;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public abstract class AbstractPanelMenuGroup extends UIPanelMenuItem {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuGroup";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuGroup";
    private Boolean submittedExpanded;

    private enum PropertyKeys {
        valid,
        immediate
    }

    protected AbstractPanelMenuGroup() {
        setRendererType("org.richfaces.PanelMenuGroup");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void processDecodes(FacesContext context) {
        super.processDecodes(context);

        //TODO nick - is component immediate==true always?
        executeValidate(context);
    }

    public void validate(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Submitted value == null means "the component was not submitted at all".
        Boolean expanded = getSubmittedExpanded();
        if (expanded == null) {
            return;
        }

        Boolean previous = (Boolean) getValue();
        setExpanded(expanded);
        setSubmittedExpanded(null);
        if (previous != null && !previous.equals(expanded)) {
            queueEvent(new PanelToggleEvent(this, previous));
        }
    }

    private void executeValidate(FacesContext context) {
        try {
            validate(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }

        //TODO nick - isValid()/setValid() is not called anywhere
        if (!isValid()) {
            context.validationFailed();
            context.renderResponse();
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



    public Boolean getSubmittedExpanded() {
        return this.submittedExpanded;
    }

    public void setSubmittedExpanded(Object submittedValue) {
        this.submittedExpanded = Boolean.parseBoolean(String.valueOf(submittedValue));
    }

    public boolean isValid() {
        return (Boolean) getStateHelper().eval(PropertyKeys.valid, true);
    }

    public void setValid(boolean valid) {
        getStateHelper().put(PropertyKeys.valid, valid);
    }

    public boolean isExpanded() {
        return getValue() == null ? false : (Boolean) getValue();
    }

    public void setExpanded(boolean expanded) {
        setValue(expanded);
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("expanded".equals(name)) {
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




    public abstract boolean isExpandSingle();

    public abstract String getCollapseEvent();

    public abstract String getExpandEvent();

    public abstract boolean isBubbleSelection();

    //TODO nick - this should be MethodExpression
    public abstract String getChangeExpandListener();
}
