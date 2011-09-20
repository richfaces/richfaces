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
package org.richfaces.renderkit;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractNotifyStack;
import org.richfaces.component.NotifyAttributes;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
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
}
