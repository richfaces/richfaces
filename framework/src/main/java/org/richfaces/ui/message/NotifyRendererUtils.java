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
package org.richfaces.ui.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.ui.message.notify.AbstractNotify;
import org.richfaces.ui.message.notifyMessage.AbstractNotifyMessage;
import org.richfaces.ui.message.notifyMessages.AbstractNotifyMessages;
import org.richfaces.ui.message.notifyStack.AbstractNotifyStack;
import org.richfaces.util.HtmlUtil;
import org.richfaces.util.RendererUtils;
/**
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class NotifyRendererUtils {

    private static final RendererUtils UTILS = RendererUtils.getInstance();

    public static String getStackId(FacesContext facesContext, UIComponent component) {
        NotifyAttributes notify = (NotifyAttributes) component;

        if (null == notify.getStack()) {
            return null;
        }

        UIComponent stack = UTILS.findComponentFor(facesContext.getViewRoot(), notify.getStack());

        if (stack instanceof AbstractNotifyStack) {
            return stack.getClientId();
        } else {
            return null;
        }
    }

    public static void addStackIdOption(Map<String, Object> options, FacesContext facesContext, UIComponent component) {
        String stackId = getStackId(facesContext, component);
        if (stackId != null) {
            options.put("stackId", stackId);
        }
    }

    public static void addFacetOrAttributeAsOption(String name, Map<String, Object> options, FacesContext facesContext,
            UIComponent component) {

        UIComponent facet = component.getFacet(name);
        if (facet != null && facet.isRendered()) {
            ResponseWriter originalResponseWriter = facesContext.getResponseWriter();
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                ResponseWriter newResponseWriter = facesContext.getRenderKit().createResponseWriter(printWriter, null, null);
                facesContext.setResponseWriter(newResponseWriter);
                facet.encodeAll(facesContext);
                printWriter.flush();
                String value = new String(outputStream.toByteArray());
                options.put(name, value);
            } catch (IOException e) {
                throw new FacesException("Can't encode facet '" + name + "' of component '" + component.getClass().getName()
                        + "'", e);
            } finally {
                facesContext.setResponseWriter(originalResponseWriter);
            }
            return;
        }

        Object attribute = component.getAttributes().get(name);
        if (attribute != null) {
            String value = attribute.toString();
            boolean escape = true;
            if (component instanceof AbstractNotify) {
                AbstractNotify notify = (AbstractNotify) component;
                escape = notify.isEscape();
            }
            if (component instanceof AbstractNotifyMessage) {
                AbstractNotifyMessage message = (AbstractNotifyMessage) component;
                escape = message.isEscape();
            }
            if (component instanceof AbstractNotifyMessages) {
                AbstractNotifyMessages messages = (AbstractNotifyMessages) component;
                escape = messages.isEscape();
            }

            if (escape) {
                value = HtmlUtil.escapeHtml(value);
            }
            options.put(name, value);
            return;
        }
    }
}
