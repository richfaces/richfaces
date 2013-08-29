/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.drag.dropTarget;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.attribute.AjaxProps;
import org.richfaces.ui.attribute.BypassProps;

/**
 * <p>
 *     The &lt;r:dropTarget&gt; component can be added to a component so that the component can accept dragged items.
 *     The dragged items must be defined with a compatible drop type for the &lt;r:dragSource&gt; component.
 * </p>
 * @author abelevich
 */
@JsfComponent(type = AbstractDropTarget.COMPONENT_TYPE, family = AbstractDropTarget.COMPONENT_FAMILY, renderer = @JsfRenderer(type = "org.richfaces.DropTargetRenderer"), attributes = {
        "immediate-prop.xml", "dropListener-props.xml" }, tag = @Tag(name = "dropTarget", handlerClass = DropHandler.class, type = TagType.Facelets))
public abstract class AbstractDropTarget extends UIComponentBase implements AjaxProps, BypassProps {
    public static final String COMPONENT_TYPE = "org.richfaces.DropTarget";
    public static final String COMPONENT_FAMILY = "org.richfaces.DropTarget";

    /**
     * Data to be processed after a drop event
     */
    @Attribute
    public abstract Object getDropValue();

    @Attribute
    public abstract boolean isImmediate();

    @Attribute
    public abstract boolean isBypassUpdates();

    @Attribute
    public abstract Object getExecute();

    @Attribute
    public abstract Object getRender();

    /**
     * A list of drag zones types, which elements are accepted by a drop zone
     */
    @Attribute
    public abstract Object getAcceptedTypes();

    @Attribute
    public abstract boolean isLimitRender();

    @Attribute
    public abstract Object getData();

    @Attribute
    public abstract String getStatus();

    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();

    @Attribute(events = @EventName("begin"))
    public abstract String getOnbegin();

    public void addDropListener(DropListener listener) {
        addFacesListener(listener);
    }

    public DropListener[] getDropListeners() {
        return (DropListener[]) getFacesListeners(DropListener.class);
    }

    public void removeDropListener(DropListener listener) {
        removeFacesListener(listener);
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if (event instanceof DropEvent) {

            if (isImmediate()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else if (isBypassUpdates()) {
                event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
            } else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
        }
        super.queueEvent(event);
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event instanceof DropEvent && (isBypassUpdates() || isImmediate())) {
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
}
