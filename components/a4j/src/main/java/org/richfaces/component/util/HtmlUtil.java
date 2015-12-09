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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.renderkit.util.HtmlDimensions;
import org.richfaces.renderkit.util.RendererUtils;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com created 09.02.2007
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public final class HtmlUtil {
    public static final Pattern ID_SELECTOR_PATTERN = Pattern
            .compile("#((?:-[A-Za-z_-]|[A-Za-z_]|\\\\[^A-F0-9U]|\\\\[A-F0-9]{1,6}\\s?|\\\\U[0-9A-F]{2}(?:A[1-9A-F]|[B-F][0-9A-F]))(?:\\\\[A-F0-9]{1,6}\\s?|[A-Za-z0-9_-]|\\\\:)*)");
    private static final String ORG_AJAX4JSF = "org.ajax4jsf.";
    private static final String ORG_RICHFACES = "org.richfaces.";
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    private HtmlUtil() {
    }

    public static String addToSize(String declaration, String delta) {
        Double doubleDelta = HtmlDimensions.decode(delta);
        Double decoded = HtmlDimensions.decode(declaration);

        return HtmlDimensions.formatPx(new Double(decoded.doubleValue() + doubleDelta.doubleValue()));
    }

    private static String escapeReplacement(String s) {
        return s.replaceAll("(\\\\|\\$)", "\\\\$1");
    }

    /**
     * <p>
     * Expands ID selectors in given CSS selector from componentId to clientId.
     * </p>
     *
     * <p>
     * e.g. <code>#componentId</code> turns out into <code>#form:componentId</code>
     * </p>
     *
     * <p>
     * For expansion, {@link RendererUtils#findComponentFor(FacesContext, UIComponent, String)} is used.
     * </p>
     *
     * @param selector the CSS selector
     * @param component the root component for finding other components in tree
     * @param context the {@link FacesContext}
     * @return The expanded CSS selector where ID selectors are expanded from componentIds to clientIds
     */
    public static String expandIdSelector(String selector, UIComponent component, FacesContext context) {
        Matcher matcher = ID_SELECTOR_PATTERN.matcher(selector);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {

            // make new id selector here using matcher.group(1)
            String unescaped = matcher.group(1).replaceAll("\\\\:", ":");
            UIComponent target = RENDERER_UTILS.findComponentFor(component, unescaped);

            if (target != null) {
                matcher.appendReplacement(sb,
                        escapeReplacement("#" + ScriptUtils.escapeCSSMetachars(target.getClientId(context))));
            }
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    public static String idsToIdSelector(String ids) {
        StringBuffer buffer = new StringBuffer();

        if (ids != null) {
            String[] idString = ids.split("\\s*,\\s*");

            for (int i = 0; i < idString.length; i++) {
                if (i > 0) {
                    buffer.append(",");
                }

                idString[i] = idString[i].replaceAll(":", "\\\\:");
                buffer.append("#").append(idString[i]);
            }
        }

        return buffer.toString();
    }

    public static boolean shouldWriteId(UIComponent component) {
        String rendererType = component.getRendererType();
        String id = component.getId();

        if ((id != null) && !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
            return true;
        }

        if ((rendererType != null) && (rendererType.startsWith(ORG_AJAX4JSF) || rendererType.startsWith(ORG_RICHFACES))) {
            return true;
        }

        return false;
    }

    private static String concat(char separator, Object... objects) {
        StringBuilder result = new StringBuilder();

        for (Object o : objects) {
            String s = (String) o;
            if (!Strings.isNullOrEmpty(s)) {
                if (result.length() != 0) {
                    result.append(separator);
                }

                result.append(s.trim());
            }
        }

        return result.toString();
    }

    public static String concatClasses(Object... classes) {
        return concat(' ', classes);
    }

    public static String concatStyles(Object... styles) {
        return concat(';', styles);
    }

    public static String escapeHtml(String html) {
        return html.replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#39;");
    }
}
