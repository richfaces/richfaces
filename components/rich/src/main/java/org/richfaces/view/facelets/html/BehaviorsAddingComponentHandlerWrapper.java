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
package org.richfaces.view.facelets.html;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;

/**
 * @author Nick Belaevski
 */
public class BehaviorsAddingComponentHandlerWrapper extends ComponentHandler implements FacesWrapper<ComponentHandler> {
    private ComponentHandler componentHandler;

    public BehaviorsAddingComponentHandlerWrapper(ComponentHandler componentHandler) {
        super(componentHandler.getComponentConfig());
        this.componentHandler = componentHandler;
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        componentHandler.apply(ctx, parent);
    }

    public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
        componentHandler.applyNextHandler(ctx, c);
    }

    public TagAttribute getBinding() {
        return componentHandler.getBinding();
    }

    public Tag getTag() {
        return componentHandler.getTag();
    }

    public TagAttribute getTagAttribute(String localName) {
        // workaround for MyFaces
        if (componentHandler == null) {
            return getComponentConfig().getTag().getAttributes().get(localName);
        }

        return componentHandler.getTagAttribute(localName);
    }

    public String getTagId() {
        return componentHandler.getTagId();
    }

    public boolean equals(Object obj) {
        return componentHandler.equals(obj);
    }

    public int hashCode() {
        return componentHandler.hashCode();
    }

    public String toString() {
        return componentHandler.toString();
    }

    public boolean isDisabled(FaceletContext ctx) {
        return componentHandler.isDisabled(ctx);
    }

    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        componentHandler.onComponentCreated(ctx, c, parent);
    }

    public void onComponentPopulated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        FacesContext facesContext = ctx.getFacesContext();
        BehaviorStack behaviorStack = BehaviorStack.getBehaviorStack(facesContext, false);

        if (behaviorStack != null && !behaviorStack.isEmpty()) {
            if (c instanceof ClientBehaviorHolder) {
                ClientBehaviorHolder behaviorHolder = (ClientBehaviorHolder) c;

                behaviorStack.addBehaviors(facesContext, behaviorHolder);
            }
        }

        componentHandler.onComponentPopulated(ctx, c, parent);
    }

    public void setAttributes(FaceletContext ctx, Object instance) {
        componentHandler.setAttributes(ctx, instance);
    }

    public ComponentHandler getWrapped() {
        return componentHandler;
    }
}
