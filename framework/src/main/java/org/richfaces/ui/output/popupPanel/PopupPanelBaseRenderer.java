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

package org.richfaces.ui.output.popupPanel;

import org.richfaces.json.JSONException;
import org.richfaces.json.JSONMap;
import org.richfaces.renderkit.RendererBase;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//TODO nick - JSF have concept of library, it should be used instead of '/' in resource names
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "common/richfaces-base-component.js"),
        @ResourceDependency(library="org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "output/popupPanel/popupPanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "output/popupPanel/popupPanelBorders.js"),
        @ResourceDependency(library = "org.richfaces", name = "output/popupPanel/popupPanelSizer.js"),
        @ResourceDependency(library = "org.richfaces", name = "output/popupPanel/popupPanel.ecss") })
public class PopupPanelBaseRenderer extends RendererBase {
    private static final String CONTROLS_FACET = "controls";
    private static final String HEADER_FACET = "header";
    private static final int SIZE = 10;
    private static final String STATE_OPTION_SUFFIX = "StateOption_";
    private static final String DEFAULT_LEFT = "auto";
    private static final String DEFAULT_TOP = "auto";
    // TODO nick - use enums
    private static final Set<String> ALLOWED_ATTACHMENT_OPTIONS = new HashSet<String>();

    static {
        ALLOWED_ATTACHMENT_OPTIONS.add("body");
        ALLOWED_ATTACHMENT_OPTIONS.add("parent");
        ALLOWED_ATTACHMENT_OPTIONS.add("form");
    }

    public void renderHeaderFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet(context, component, HEADER_FACET);
    }

    public void renderControlsFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet(context, component, CONTROLS_FACET);
    }

    private void renderFacet(FacesContext context, UIComponent component, String facet) throws IOException {
        UIComponent headerFacet = component.getFacet(facet);
        headerFacet.encodeAll(context);
    }

    @SuppressWarnings("unchecked")
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);

        AbstractPopupPanel panel = (AbstractPopupPanel) component;
        ExternalContext exCtx = context.getExternalContext();
        Map<String, String> rqMap = exCtx.getRequestParameterMap();
        Object panelOpenState = rqMap.get(panel.getClientId(context) + "OpenedState");

        if (panel.isKeepVisualState()) {
            if (null != panelOpenState) {
                // Bug https://jira.jboss.org/jira/browse/RF-2466
                // Incorrect old:
                // panel.setShowWhenRendered(Boolean.parseBoolean((String) clnId));
                // ShowWhenRendered can be settled separately with modal panel "showWhenRendered" attribute
                // so we should combine ShowWhenRendered || KeepVisualState && (OpenedState==TRUE) against rewriting
                boolean show = panel.isShow() || Boolean.parseBoolean((String) panelOpenState);
                panel.setShow(show);

                Map<String, Object> visualOptions = (Map<String, Object>) getHandledVisualOptions(panel);
                Iterator<Entry<String, String>> it = rqMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    int suffixPos = entry.getKey().toString().indexOf(STATE_OPTION_SUFFIX);
                    if (-1 != suffixPos) {
                        String key = entry.getKey().toString().substring(suffixPos + STATE_OPTION_SUFFIX.length());
                        visualOptions.put(key, entry.getValue());
                    }
                }
            }
        }
    }

    protected Class getComponentClass() {
        return AbstractPopupPanel.class;
    }

    public void checkOptions(FacesContext context, UIComponent component) {
        AbstractPopupPanel panel = (AbstractPopupPanel) component;
        if (panel.isAutosized() && panel.isResizeable()) {
            throw new IllegalArgumentException("Autosized modal panel can't be resizeable.");
        }

        String domElementAttachment = panel.getDomElementAttachment();
        if (domElementAttachment != null && domElementAttachment.trim().length() != 0) {
            if (!ALLOWED_ATTACHMENT_OPTIONS.contains(domElementAttachment)) {
                throw new IllegalArgumentException("Value '" + domElementAttachment
                    + "' of domElementAttachment attribute is illegal. " + "Allowed values are: " + ALLOWED_ATTACHMENT_OPTIONS);
            }
        }

        if (panel.getMinHeight() != -1) {
            if (panel.getMinHeight() < SIZE) {
                throw new FacesException("Attribute minHeight should be greater then 10px");
            }
        }

        if (panel.getMinWidth() != -1) {
            if (panel.getMinWidth() < SIZE) {
                throw new FacesException("Attribute minWidth should be greater then 10px");
            }
        }
    }

    public boolean getRendersChildren() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public String buildShowScript(FacesContext context, UIComponent component) {
        AbstractPopupPanel panel = (AbstractPopupPanel) component;
        StringBuilder result = new StringBuilder();

        // Bug https://jira.jboss.org/jira/browse/RF-2466
        // We are already processed KeepVisualState and current open state in
        // doDecode, so no need to check panel.isKeepVisualState() here.
        if (panel.isShow()) {
            result.append("RichFaces.ui.PopupPanel.showPopupPanel('" + panel.getClientId(context) + "', {");

            // TODO nick - use ScriptUtils.toScript
            Iterator<Map.Entry<String, Object>> it = ((Map<String, Object>) getHandledVisualOptions(panel)).entrySet()
                .iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();

                result.append(entry.getKey() + ": '" + entry.getValue() + "'");
                if (it.hasNext()) {
                    result.append(", ");
                }
            }

            result.append("});");
        }
        if (result.length() > 0) {
            return result.toString();
        }
        return null;
    }

    public String getStyleIfTrimmed(UIComponent panel) {
        if (panel.getAttributes().get("trimOverlayedElements").equals(Boolean.TRUE)) {
            return "position: relative; z-index : 0";
        }
        return "";
    }

    public String getContainerStyle(UIComponent panel) {
        StringBuilder res = new StringBuilder();
        Map<String, Object> attrs = panel.getAttributes();
        res.append("position: ").append(((Boolean) attrs.get("followByScroll")) ? "fixed" : "absolute").append("; ");

        Integer zindex = (Integer) attrs.get("zindex");
        if (zindex != null) {
            res.append("z-index:").append(zindex).append("; ");
        }

        String style = (String) attrs.get("style");
        if (style != null && style.length() > 0) {
            res.append(style);
        }

        return res.toString();
    }

    private Object buildEventFunction(Object eventFunction) {
        if (eventFunction != null && eventFunction.toString().length() > 0) {
            return "new Function(\"" + eventFunction.toString() + "\");";
        }
        return null;
    }

    public Map<String, Object> getHandledVisualOptions(UIComponent component) {
        AbstractPopupPanel panel = (AbstractPopupPanel) component;
        String options = panel.getVisualOptions();
        Map<String, Object> result;
        result = prepareVisualOptions(options, panel);

        if (null == result) {
            result = new HashMap<String, Object>();
        }
        return result;
    }

    private Map<String, Object> prepareVisualOptions(Object value, AbstractPopupPanel panel) {
        if (null == value) {
            return new HashMap<String, Object>();
        } else if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else if (value instanceof String) {
            String s = (String) value;
            if (!s.startsWith("{")) {
                s = "{" + s + "}";
            }
            try {
                return new HashMap<String, Object>(new JSONMap(s));
            } catch (JSONException e) {
                throw new FacesException(e);
            }
        } else {
            throw new FacesException("Attribute visualOptions of component ["
                + panel.getClientId(FacesContext.getCurrentInstance())
                + "] must be instance of Map or String, but its type is " + value.getClass().getSimpleName());
        }
    }

    protected String getLeftOrDefault(UIComponent component) {
        String leftProperty = ((AbstractPopupPanel) component).getLeft();
        if (leftProperty == null || leftProperty.length() == 0) {
            leftProperty = DEFAULT_LEFT;
        }
        return leftProperty;
    }

    protected String getTopOrDefault(UIComponent component) {
        String topProperty = ((AbstractPopupPanel) component).getTop();
        if (topProperty == null || topProperty.length() == 0) {
            topProperty = DEFAULT_TOP;
        }
        return topProperty;
    }
}
