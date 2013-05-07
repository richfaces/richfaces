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
package org.richfaces.ui.common;

import java.io.IOException;
import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

/**
 * @author akolonitsky
 * @since Aug 31, 2010
 */
public abstract class EventListenerHandler extends TagHandler implements AttachedObjectHandler {
    protected final TagAttribute binding;
    protected final String listenerType;

    public abstract static class LazyEventListener<L extends FacesListener> implements FacesListener, Serializable {
        private static final long serialVersionUID = 1L;
        protected final String type;
        protected final ValueExpression binding;

        protected LazyEventListener(String type, ValueExpression binding) {
            this.type = type;
            this.binding = binding;
        }

        public void processEvent(FacesEvent event) throws AbortProcessingException {

            FacesContext faces = FacesContext.getCurrentInstance();
            if (faces == null) {
                return;
            }

            L instance = null;
            if (this.binding != null) {
                instance = (L) binding.getValue(faces.getELContext());
            }
            if (instance == null && this.type != null) {
                try {
                    instance = (L) TagHandlerUtils.loadClass(this.type, Object.class).newInstance();
                } catch (Exception e) {
                    throw new AbortProcessingException("Couldn't Lazily instantiate EventListener", e);
                }
                if (this.binding != null) {
                    binding.setValue(faces.getELContext(), instance);
                }
            }

            if (instance != null) {
                event.processListener(instance);
            }
        }
    }

    public EventListenerHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        TagAttribute type = this.getAttribute("type");
        if (type != null) {
            if (type.isLiteral()) {
                try {
                    TagHandlerUtils.loadClass(type.getValue(), Object.class);
                } catch (ClassNotFoundException e) {
                    throw new TagAttributeException(type, "Couldn't qualify EventListener", e);
                }
            } else {
                throw new TagAttributeException(type, "Must be a literal class name of type EventListener");
            }
            this.listenerType = type.getValue();
        } else {
            this.listenerType = null;
        }
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {

        // only process if it's been created
        if (parent == null || !ComponentHandler.isNew(parent)) {
            return;
        }

        if (isEventSource(parent)) {
            applyAttachedObject(ctx.getFacesContext(), parent);
        } else if (UIComponent.isCompositeComponent(parent)) {
            // Allow the composite component to know about the target component.
            TagHandlerUtils.getOrCreateRetargetableHandlersList(parent).add(this);
        } else {
            throw new TagException(this.tag, "Parent does not match event source requirements: " + parent);
        }
    }

    public String getFor() {
        TagAttribute attr = this.getAttribute("for");
        return attr == null ? null : attr.getValue();
    }

    public abstract boolean isEventSource(UIComponent comp);
}
