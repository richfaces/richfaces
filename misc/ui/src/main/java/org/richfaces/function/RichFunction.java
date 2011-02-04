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

package org.richfaces.function;

import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.cdk.annotations.Function;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * Created 20.03.2008
 * @author Nick Belaevski
 * @since 3.2
 */

public final class RichFunction {

    /**
     * 
     */
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    //EasyMock requires at least protected access for the interface for calls to be delegated to
    protected static interface ComponentLocator {

        UIComponent findComponent(FacesContext facesContext, UIComponent contextComponent, String id);

    }

    private static ComponentLocator locator = new ComponentLocator() {

        public UIComponent findComponent(FacesContext context, UIComponent contextComponent, String id) {
            return RENDERER_UTILS.findComponentFor(context, contextComponent, id);
        }
    };

    private RichFunction() {
        //utility class constructor
    }

    //used by unit tests
    static void setComponentLocator(ComponentLocator mockLocator) {
        locator = mockLocator;
    }

    private static UIComponent findComponent(FacesContext context, String id) {
        if (id != null) {
            UIComponent contextComponent = UIComponent.getCurrentComponent(context);
            if (contextComponent == null) {
                contextComponent = context.getViewRoot();
            }

            UIComponent component = locator.findComponent(context, contextComponent, id);

            if (component != null) {
                return component;
            }
        }

        return null;
    }

    @Function
    public static String clientId(String id) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = findComponent(context, id);
        return component != null ? component.getClientId(context) : null;
    }

    @Function
    public static String component(String id) {
        String clientId = clientId(id);
        if (clientId != null) {
            //TODO nick - what if jQuery.RichFaces doesn't exist?
            return "RichFaces.$('" + clientId + "')";
        }

        return null;
    }

    @Function
    public static String element(String id) {
        String clientId = clientId(id);
        if (clientId != null) {
            return "document.getElementById('" + clientId + "')";
        }

        return null;
    }

    @Function
    public static UIComponent findComponent(String id) {
        return findComponent(FacesContext.getCurrentInstance(), id);
    }

    /**
     * @since 3.3.1
     * @param rolesObject
     * @return
     */
    @Function
    public static boolean isUserInRole(Object rolesObject) {
        //TODO nick - AjaxRendererUtils split text by commas and whitespace, what is the right variant?
        Set<String> rolesSet = CoreAjaxRendererUtils.asIdsSet(rolesObject);
        if (rolesSet != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            for (String role : rolesSet) {
                if (externalContext.isUserInRole(role)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    @Function
    public static String toScript(Object o) {
        return ScriptUtils.toScript(o);
    }
}
