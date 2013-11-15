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

package org.richfaces.ui.misc.focus;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.javascript.JSLiteral;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.services.ServiceTracker;
import org.richfaces.util.RendererUtils;

public class FocusManagerImpl implements FocusManager {

    private static final String SCRIPT = "RichFaces.jQuery(document.getElementById('%s')).focus();";
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();

    @Override
    public void focus(String componentId) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context == null) {
            throw new IllegalStateException(FocusManager.class.getSimpleName()
                    + " can't be used without FacesContext available");
        }

        if (componentId == null) {
            setContextAttribute(context, null);
        } else {
            UIComponent currentComponent = UIComponent.getCurrentComponent(context);
            if (currentComponent == null) {
                currentComponent = context.getViewRoot();
            }
            currentComponent.findComponent(componentId);
            UIComponent component = RendererUtils.getInstance().findComponentFor(currentComponent, componentId);

            if (component == null) {
                String message = FocusManager.class.getSimpleName() + ": Component with ID '" + componentId + "' was not found";
                LOG.warn(message);
                context.addMessage(null, new FacesMessage(message));
            } else {
                String clientId = component.getClientId(context);

                setContextAttribute(context, clientId);

                JavaScriptService javaScriptService = ServiceTracker.getService(context, JavaScriptService.class);
                javaScriptService.addPageReadyScript(context, new JSLiteral(String.format(SCRIPT, clientId)));
            }
        }
    }

    private void setContextAttribute(FacesContext context, String clientId) {
        context.getAttributes().put(FocusManager.FOCUS_CONTEXT_ATTRIBUTE, clientId);
    }
}

