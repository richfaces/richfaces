/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import com.google.common.base.Strings;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.event.PreRenderParentListener;
import org.richfaces.renderkit.PlaceholderRendererBase;

/**
 * Adds placeholder capability to HTML input and textarea elements. A placeholder typically appears as light gray text within an
 * input or textarea element whenever the element is empty and does not have focus. This provides a hint to the user as to what
 * the input or textarea element is used for, or the type of input that is required.
 */
@JsfComponent(tag = @Tag(name = "placeholder", type = TagType.Facelets), renderer = @JsfRenderer(family = AbstractPlaceholder.COMPONENT_FAMILY, type = PlaceholderRendererBase.RENDERER_TYPE), attributes = { "javax.faces.component.ValueHolder.xml" })
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractPlaceholder extends UIOutput implements StyleClassProps {
    // ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Placeholder";

    public static final String COMPONENT_TYPE = "org.richfaces.Placeholder";

    @Attribute(required = true)
    public abstract Object getValue();

    @Attribute(hidden = true)
    public abstract Converter getConverter();

    /**
     * The jQuery selector used to filter which child DOM elements will be a placeholder attached to.
     */
    @Attribute
    public abstract String getSelector();

    /**
     * Registers component for processing before its parent component when it has empty selector - this logic is used as
     * workaround for text components which does not render children (RF-12589).
     */
    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {

        if (event.getSource() == this) {
            if (event instanceof PostAddToViewEvent) {

                // only for nested usage (workaround for RF-12589)
                if (this.getSelector() == null || this.getSelector().isEmpty()) {
                    UIComponent component = ((PostAddToViewEvent) event).getComponent();
                    UIComponent parent = component.getParent();

                    new PlaceholderParentPreRenderListener(parent, component);
                }
            }
        }

        super.processEvent(event);
    }

    /**
     * Renders component before its parent.
     */
    private static class PlaceholderParentPreRenderListener extends PreRenderParentListener {

        private static final long serialVersionUID = 1870218106060075543L;

        public PlaceholderParentPreRenderListener(UIComponent parent, UIComponent component) {
            super(parent, component);
        }

        @Override
        protected void preRenderParent(FacesContext facesContext, UIComponent component) {
            AbstractPlaceholder placeholder = (AbstractPlaceholder) component;
            UIComponent parent = component.getParent();
            PlaceholderRendererBase renderer = (PlaceholderRendererBase) placeholder.getRenderer(facesContext);

            String placeHolderStyleClass = (String) component.getAttributes().get("styleClass");
            if (! Strings.isNullOrEmpty(placeHolderStyleClass)) {
                    parent.getAttributes().put("placeHolderStyleClass", placeHolderStyleClass);
            }
            if (parent instanceof InplaceComponent) {
                if (placeholder.isRendered() && placeholder.getValue() != null) {
                    // backup defaultLabel attribute
                    ValueExpression originalExpression = parent.getValueExpression("defaultLabel");
                    if (originalExpression != null) {
                        parent.getAttributes().put("originalDefaultLabel", originalExpression);
                    } else if (((InplaceComponent) parent).getDefaultLabel() != null) {
                        parent.getAttributes().put("originalDefaultLabel", ((InplaceComponent) parent).getDefaultLabel());
                    }

                    String text = renderer.getConvertedValue(facesContext, placeholder);
                    ((InplaceComponent) parent).setDefaultLabel(text);
                } else {
                    Object defaultLabel = parent.getAttributes().get("originalDefaultLabel");
                    ((InplaceComponent) parent).setDefaultLabel(null);
                    if (defaultLabel instanceof ValueExpression) {
                        parent.setValueExpression("defaultLabel", (ValueExpression) defaultLabel);
                    } else if (defaultLabel != null) {
                        ((InplaceComponent) parent).setDefaultLabel((String) defaultLabel);
                    }
                }
            } else {
                try {
                    if (component.isRendered()) {
                        renderer.doEncodeEnd(facesContext.getResponseWriter(), facesContext, component);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Rendering of placeholder before its parent has failed", e);
                }
            }
        }
    }
}
