/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.ajax;

import java.io.IOException;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.view.ActionSource2AttachedObjectHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

import org.richfaces.ui.common.TagHandlerUtils;

/**
 * @author Nick Belaevski
 *
 */
public class ActionListenerHandler extends TagHandler implements ActionSource2AttachedObjectHandler {
    private TagAttribute binding;
    private String listenerType;
    private TagAttribute listenerMethod;

    public ActionListenerHandler(TagConfig config) {
        super(config);

        this.binding = this.getAttribute("binding");

        TagAttribute type = this.getAttribute("type");
        if (type != null) {
            if (!type.isLiteral()) {
                throw new TagAttributeException(type, "Must be a literal class name of type ActionListener");
            } else {
                // test it out
                try {
                    TagHandlerUtils.loadClass(type.getValue(), ActionListener.class);
                } catch (ClassNotFoundException e) {
                    throw new TagAttributeException(type, "Couldn't qualify ActionListener", e);
                } catch (ClassCastException e) {
                    throw new TagAttributeException(type, "Qualified class is not ActionListener", e);
                }
            }

            this.listenerType = type.getValue();
        } else {
            this.listenerType = null;
        }

        this.listenerMethod = this.getAttribute("listener");

        if (this.listenerMethod != null && this.binding != null) {
            throw new TagException(this.tag, "Attributes 'listener' and 'binding' cannot be used simultaneously");
        }

        if (this.listenerMethod != null && this.listenerType != null) {
            throw new TagException(this.tag, "Attributes 'listener' and 'type' cannot be used simultaneously");
        }
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        if (!(parent instanceof ActionSource)) {
            throw new TagException(this.tag, "Parent is not of type ActionSource, type is: " + parent);
        }

        ActionSource as = (ActionSource) parent;

        FaceletContext ctx = (FaceletContext) context.getAttributes().get(TagHandlerUtils.FACELET_CONTEXT_KEY);

        if (this.listenerMethod != null) {
            MethodExpression listenerMethodExpression = this.listenerMethod.getMethodExpression(ctx, Void.TYPE,
                new Class<?>[] { ActionEvent.class });

            as.addActionListener(new MethodExpressionActionListener(listenerMethodExpression));
        } else {
            ValueExpression b = null;
            if (this.binding != null) {
                b = this.binding.getValueExpression(ctx, ActionListener.class);
            }
            ActionListener listener = new LazyActionListener(this.listenerType, b);
            as.addActionListener(listener);
        }
    }

    public String getFor() {
        String result = null;
        TagAttribute attr = this.getAttribute("for");

        if (null != attr) {
            result = attr.getValue();
        }

        return result;
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        if (null == parent || !(ComponentHandler.isNew(parent))) {
            return;
        }

        if (UIComponent.isCompositeComponent(parent)) {
            if (null == getFor()) {
                throw new TagException(this.tag,
                    "actionListener tags nested within composite components must have a non-null 'for' attribute");
            }

            TagHandlerUtils.getOrCreateRetargetableHandlersList(parent).add(this);
        } else {
            applyAttachedObject(ctx.getFacesContext(), parent);
        }
    }
}
