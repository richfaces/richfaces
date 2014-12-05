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
package org.richfaces.renderkit;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.component.AbstractPush;
import org.richfaces.resource.PushResource;

/**
 * @author Nick Belaevski
 *
 */
public class PushRendererBase extends RendererBase {
    private static final String PUSH_URL_ENCODED_ATTRIBUTE = PushRendererBase.class.getName();

    protected String getPushResourceUrl(FacesContext context) {
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        Resource pushResource = resourceHandler.createResource(PushResource.class.getName());

        return pushResource.getRequestPath();
    }

    protected String getPushHandlerUrl(FacesContext context) {
        PushContext pushContext = ServiceTracker.getService(PushContextFactory.class).getPushContext();

        return pushContext.getPushHandlerUrl();
    }

    protected boolean shouldEncodePushUrl(FacesContext context) {
        Map<Object, Object> attributes = context.getAttributes();

        if (attributes.get(PUSH_URL_ENCODED_ATTRIBUTE) == null) {
            attributes.put(PUSH_URL_ENCODED_ATTRIBUTE, Boolean.TRUE);
            return true;
        }

        return false;
    }

    protected String getOptionsString(FacesContext context, UIComponent component) {
        AbstractPush push = (AbstractPush) component;

        Map<String, Object> options = new HashMap<String, Object>(2);

        options.put("address", push.getAddress());
        options.put("dataHandler", push.getOndataavailable());
        options.put("errorHandler", push.getOnerror());

        return ScriptUtils.toScript(options);
    }
}
