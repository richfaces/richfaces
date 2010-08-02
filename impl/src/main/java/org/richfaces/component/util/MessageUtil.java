/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.component.util;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Nick - mailto:nbelaevski@exadel.com
 *         created 06.02.2007
 */
public final class MessageUtil {
    private static final boolean IS_12;

    static {
        boolean is12;

        try {
            Application.class.getMethod("getExpressionFactory", null);
            is12 = true;
        } catch (NoSuchMethodException e) {
            is12 = false;
        }

        IS_12 = is12;
    }

    private MessageUtil() {
    }

    public static Object getLabel(FacesContext context, UIComponent component) {
        Object o = null;

        if (IS_12) {
            o = component.getAttributes().get("label");

            if ((o == null) || ((o instanceof String) && ((String) o).length() == 0)) {
                ValueBinding ex = component.getValueBinding("label");

                if (ex != null) {
                    o = ex.getValue(context);
                }
            }
        }

        if (o == null) {
            o = component.getClientId(context);
        }

        return o;
    }

    public static FacesMessage getMessage(FacesContext context, String messageId, Object[] parameters) {
        return AbstractMessageUtil.getMessage(context, messageId, parameters, FacesMessage.FACES_MESSAGES);
    }
}
