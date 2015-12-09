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
import org.richfaces.renderkit.util.RendererUtils;
import org.richfaces.util.Sets;

/**
 * Created 20.03.2008
 *
 * @author Nick Belaevski
 * @since 3.2
 */
public final class RichFunction {
    /**
     *
     */
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    // EasyMock requires at least protected access for the interface for calls to be delegated to
    protected interface ComponentLocator {
        UIComponent findComponent(FacesContext facesContext, UIComponent contextComponent, String id);
    }

    private static ComponentLocator locator = new ComponentLocator() {
        public UIComponent findComponent(FacesContext context, UIComponent contextComponent, String id) {
            return RENDERER_UTILS.findComponentFor(contextComponent, id);
        }
    };

    private RichFunction() {
        // utility class constructor
    }

    // used by unit tests
    static void setComponentLocator(ComponentLocator mockLocator) {
        locator = mockLocator;
    }

    public static UIComponent findComponent(FacesContext context, String id) {
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

    /**
     * The rich:clientId('id') function returns the client identifier related to the passed component identifier ('id'). If the
     * specified component identifier is not found, null is returned instead.
     */
    @Function
    public static String clientId(String id) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = findComponent(context, id);
        return component != null ? component.getClientId(context) : null;
    }

    /**
     * The rich:component('id') function is equivalent to the RichFaces.component('clientId') code. It returns the client object
     * instance based on the passed server-side component identifier ('id'). If the specified component identifier is not found,
     * null is returned instead. The function can be used to get an object from a component to call a JavaScript API function
     * without using the <rich:componentControl> component.
     */
    @Function
    public static String component(String id) {
        String clientId = clientId(id);
        if (clientId != null) {
            // TODO nick - what if jQuery.RichFaces doesn't exist?
            return "RichFaces.component('" + clientId + "')";
        }

        return null;
    }

    /**
     * <p>The rich:element('id') function is a shortcut for the equivalent document.getElementById(#{rich:clientId('id')}) code. It
     * returns the element from the client, based on the passed server-side component identifier. If the specified component
     * identifier is not found, null is returned instead.</p>
     */
    @Function
    public static String element(String id) {
        String clientId = clientId(id);
        if (clientId != null) {
            return "document.getElementById('" + clientId + "')";
        }

        return null;
    }

    /**
     * The rich:jQuerySelector('id') function will perform nearly the same function as rich:clientId('id') but will transform
     * the resulting id into a jQuery id selector which means that it will add a "#" character at the beginning and escape all
     * reserved characters in CSS selectors.
     */
    @Function
    public static String jQuerySelector(String id) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent component = findComponent(facesContext, id);
        if (component != null) {
            return jQuerySelector(facesContext, component);
        }

        return null;
    }

    /**
     * Utility method which finds component's jQuery selector based on component's clientId.
     */
    public static String jQuerySelector(FacesContext facesContext, UIComponent component) {
        if (facesContext == null) {
            throw new IllegalArgumentException("facesContext can't be null");
        }
        if (component == null) {
            throw new IllegalArgumentException("component can't be null");
        }
        String clientId = component.getClientId(facesContext);
        return "#" + ScriptUtils.escapeCSSMetachars(ScriptUtils.escapeCSSMetachars(clientId));
    }

    /**
     * <p>The rich:jQuery('id') function is a shortcut for the equivalent jQuery(#{rich:element('id')}) code. It returns the
     * jQuery object from the client, based on the passed server-side component identifier. If the specified component
     * identifier is not found, empty jQuery object is returned instead.</p>
     *
     * <p>This function is for use in EL.  Refer to the &lt;rich:jQuery&gt; component for access to the jQuery library as a
     * facelet tag.</p>
     */
    @Function
    public static String jQuery(String id) {
        String element = element(id);

        if (element != null) {
            return "RichFaces.jQuery(" + element + ")";
        }

        return "RichFaces.jQuery()";
    }

    /**
     * The rich:findComponent('id') function returns the a UIComponent instance of the passed component identifier. If the
     * specified component identifier is not found, null is returned instead.
     */
    @Function
    public static UIComponent findComponent(String id) {
        return findComponent(FacesContext.getCurrentInstance(), id);
    }

    /**
     * <p>
     * The rich:isUserInRole(Object) function checks whether the logged-in user belongs to a certain user role, such as being an
     * administrator. User roles are defined in the web.xml settings file.
     * </p>
     *
     * @since 3.3.1
     */
    @Function
    public static boolean isUserInRole(Object rolesObject) {
        // TODO nick - AjaxRendererUtils split text by commas and whitespace, what is the right variant?
        Set<String> rolesSet = Sets.asSet(rolesObject);
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

    /**
     * Convert any Java Object to JavaScript representation, converting types properly, e.g.:
     *
     * <ul>
     * <li><tt>Java primitives</tt></li>
     * <li><tt>Arrays: toScript(new int[] { 1, 2, 3 }) -&gt; [1, 2, 3]</tt></li>
     * <li><tt>Collections (sets, lists): toScript(Arrays.asList(new int[] { 1, 2, 3 })) -&gt; [1, 2, 3]</tt></li>
     * <li><tt>Maps: toScript((Map&lt;String, String&gt;)map) -&gt; {\"a\":\"foo\",\"b\":\"bar\",\"c\":\"baz\"}</tt></li>
     * <li>
     * <tt>Beans / Objects: toScript(new Bean[] { new Bean(1, true, "bar") }) -&gt; [{\"bool\":true,\"foo\":\"bar\",\"integer\":1}]</tt>
     * </li>
     *
     * <li><tt>Dates and Timezones</tt></li>
     *
     * <li><tt>Combinations of above</tt></li>
     * </ul>
     *
     * This function delegates to org.richfaces.javascript.ScriptUtils#toScript(Object)
     */
    @Function
    public static String toScript(Object o) {
        return ScriptUtils.toScript(o);
    }
}
