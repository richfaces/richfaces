/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 */
public final class DataComponentsContextUtil {
    private static final String MODEL_RESET_ATTRIBUTE_SUFFIX = "#" + DataComponentsContextUtil.class.getName();

    private DataComponentsContextUtil() {
    }

    private static String getAttributeName(FacesContext context, UIComponent component) {
        return component.getClientId(context) + MODEL_RESET_ATTRIBUTE_SUFFIX;
    }

    public static void resetDataModelOncePerPhase(FacesContext context, UIComponent component) {
        String attributeName = getAttributeName(context, component);
        if (context.getCurrentPhaseId() != context.getAttributes().put(attributeName, context.getCurrentPhaseId())) {
            resetDataModel(context, component);
        }
    }

    private static void resetDataModel(FacesContext context, UIComponent component) {
        if (component instanceof UIDataAdaptor) {
            ((UIDataAdaptor) component).resetDataModel();
        } else if (component instanceof UIData) {
            // hack to reset cached data model
            component.setValueExpression("value", component.getValueExpression("value"));
        } else {
            throw new IllegalArgumentException(component.toString());
        }
    }
}
