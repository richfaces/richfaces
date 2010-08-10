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

package org.richfaces.view.facelets.html;

import org.richfaces.component.AbstractTogglePanel;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.view.EditableValueHolderAttachedObjectHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;
import java.io.Serializable;

/**
 *
 * @author akolonitsky
 * @version 1.0
 */
public final class ItemChangeListenerHandler extends TagHandler implements EditableValueHolderAttachedObjectHandler {

    private static class LazyItemChangeListener implements ItemChangeListener, Serializable {

        private static final long serialVersionUID = 1L;

        private final String type;

        private final ValueExpression binding;

        LazyItemChangeListener(String type, ValueExpression binding) {
            this.type = type;
            this.binding = binding;
        }

        public void processItemChange(ItemChangeEvent event)
            throws AbortProcessingException {
            
            FacesContext faces = FacesContext.getCurrentInstance();
            if (faces == null) {
                return;
            }

            ItemChangeListener instance = null;
            if (this.binding != null) {
                instance = (ItemChangeListener) binding.getValue(faces.getELContext());
            }
            if (instance == null && this.type != null) {
                try {
                    instance = (ItemChangeListener) forName(this.type).newInstance();
                } catch (Exception e) {
                    throw new AbortProcessingException("Couldn't Lazily instantiate ItemChangeListener", e);
                }
                if (this.binding != null) {
                    binding.setValue(faces.getELContext(), instance);
                }
            }
            if (instance != null) {
                instance.processItemChange(event);
            }
        }
    }

    private final TagAttribute binding;

    private final String listenerType;

    public ItemChangeListenerHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        TagAttribute type = this.getAttribute("type");
        if (type != null) {
            if (type.isLiteral()) {
                try {
                    forName(type.getValue());
                } catch (ClassNotFoundException e) {
                    throw new TagAttributeException(type, "Couldn't qualify ItemChangeListener", e);
                }
            } else {
                throw new TagAttributeException(type, "Must be a literal class name of type ItemChangeListener");
            }
            this.listenerType = type.getValue();
        } else {
            this.listenerType = null;
        }
    }

    public void apply(FaceletContext ctx, UIComponent parent) {

        // only process if it's been created
        if (parent == null || !ComponentHandler.isNew(parent)) {
            return;
        }

        if (parent instanceof AbstractTogglePanel) {
            applyAttachedObject(ctx.getFacesContext(), parent);
        } else if (UIComponent.isCompositeComponent(parent)) {
            // Allow the composite component to know about the target component.
            TagHandlerUtils.getOrCreateRetargetableHandlersList(parent).add(this);
        } else {
            throw new TagException(this.tag, "Parent is not of type AbstractTogglePanel, type is: " + parent);
        }
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        ValueExpression valueExpr = null;
        if (this.binding != null) {
            FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
            valueExpr = this.binding.getValueExpression(ctx, ItemChangeListener.class);
        }

        ItemChangeSource evh = (ItemChangeSource) parent;
        evh.addItemChangeListener(new LazyItemChangeListener(this.listenerType, valueExpr));
    }

    public String getFor() {
        TagAttribute attr = this.getAttribute("for");
        return attr == null ? null : attr.getValue();
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        if (null == name || "".equals(name)) {
            return null;
        }
        
        return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
    }
}

