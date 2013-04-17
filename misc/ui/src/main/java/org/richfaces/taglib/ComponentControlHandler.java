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
package org.richfaces.taglib;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletHandler;

import org.richfaces.component.AbstractParameter;
import org.richfaces.component.UIHashParameter;
import org.richfaces.component.behavior.ComponentControlBehavior;
import org.richfaces.view.facelets.html.CustomBehaviorHandler;

/**
 * @author Anton Belevich
 *
 */
public class ComponentControlHandler extends CustomBehaviorHandler {
    public ComponentControlHandler(BehaviorConfig config) {
        super(config);
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        super.apply(ctx, parent);
        processNestedTags(ctx, parent);
    }

    private void processNestedTags(FaceletContext ctx, UIComponent parent) throws IOException {
        if (nextHandler instanceof CompositeFaceletHandler) {
            FaceletHandler[] children = ((CompositeFaceletHandler) nextHandler).getHandlers();
            for (FaceletHandler handler : children) {
                processNextHandler(ctx, handler, parent);
            }
        } else {
            processNextHandler(ctx, nextHandler, parent);
        }
    }

    private void processNextHandler(FaceletContext ctx, FaceletHandler handler, UIComponent parent) throws IOException {
        if (handler instanceof ComponentHandler) {
            ComponentHandler componentHandler = (ComponentHandler) handler;

            ComponentConfig componentConfig = componentHandler.getComponentConfig();
            String componentType = componentConfig.getComponentType();

            if (isUIParameter(componentType)) {
                FacesContext facesContext = ctx.getFacesContext();

                UIComponent component = (UIComponent) facesContext.getApplication().createComponent(componentType);
                componentHandler.setAttributes(ctx, component);

                if (parent instanceof ClientBehaviorHolder) {
                    ClientBehaviorHolder clientBehaviorHolder = ((ClientBehaviorHolder) parent);
                    Map<String, List<ClientBehavior>> clientBehaviors = clientBehaviorHolder.getClientBehaviors();

                    String eventName = getEventName();

                    if (eventName == null) {
                        eventName = clientBehaviorHolder.getDefaultEventName();
                    }

                    if (eventName != null) {
                        List<ClientBehavior> eventClientBehaviors = clientBehaviors.get(eventName);
                        for (ClientBehavior clientBehavior : eventClientBehaviors) {
                            if (clientBehavior instanceof ComponentControlBehavior) {
                                ((ComponentControlBehavior) clientBehavior).getChildren().add(component);
                            }
                        }
                    }
                }

                componentHandler.applyNextHandler(ctx, component);
            }
        }
    }

    private boolean isUIParameter(String type) {
        return (UIParameter.COMPONENT_TYPE.equals(type) || UIHashParameter.COMPONENT_TYPE.equals(type) || AbstractParameter.COMPONENT_TYPE
            .equals(type));
    }

    @Override
    public boolean isWrapping() {
        return false;
    }
}
