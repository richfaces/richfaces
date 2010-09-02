/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSObject;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.renderkit.AjaxEventOptions;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.HandlersChain;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.AbstractTogglePanel;
import org.richfaces.component.AbstractTogglePanelItem;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.util.FormUtil;

/**
 * @author akolonitsky
 * @since -4712-01-01
 */
@ResourceDependencies({
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "TogglePanel.js") })
public class TogglePanelRenderer extends DivPanelRenderer {

    public static final String VALUE_POSTFIX = "-value";

    private static final String ON = "on";
    private static final String ITEM_CHANGE = "itemchange";
    private static final String BEFORE_ITEM_CHANGE = "beforeitemchange";

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractTogglePanel panel = (AbstractTogglePanel) component;
        
        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();

        // Don't overwrite the value unless you have to!
        String newValue = requestMap.get(getValueRequestParamName(context, component));
        if (newValue != null) {
            panel.setSubmittedActiveItem(newValue);
        }

        String compClientId = component.getClientId(context);
        String clientId = requestMap.get(compClientId);
        if (clientId != null && clientId.equals(compClientId)) {
            AbstractTogglePanelItem panelItem = panel.getItem(newValue);
            if (panelItem != null) {
                context.getPartialViewContext().getRenderIds().add(panelItem.getClientId(context));
                
                //TODO nick - this should be done on encode, not on decode
                addOnCompleteParam(newValue, panel.getClientId());
            }
        }
    }

    protected static void addOnCompleteParam(String newValue, String panelId) {
        StringBuilder onComplete = new StringBuilder();
        onComplete.append("RichFaces.$('").append(panelId)
                    .append("').onCompleteHandler('").append(newValue).append("');");

        AjaxContext.getCurrentInstance().appendOncomplete(onComplete.toString());
    }

    public static String getValueRequestParamName(FacesContext context, UIComponent component) {
        return component.getClientId(context) + VALUE_POSTFIX;
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent comp) throws IOException {
        FormUtil.throwEnclFormReqExceptionIfNeed(context, comp);

        super.doEncodeBegin(writer, context, comp);
        AbstractTogglePanel panel = (AbstractTogglePanel) comp;

        writer.startElement(HTML.INPUT_ELEM, comp);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HTML.VALUE_ATTRIBUTE, panel.getActiveItem(), "activeItem");
        writer.writeAttribute(HTML.ID_ATTRIBUTE, getValueRequestParamName(context, comp), null);
        writer.writeAttribute(HTML.NAME_ATTRIBUTE, getValueRequestParamName(context, comp), null);
        writer.endElement(HTML.INPUT_ELEM);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return HtmlUtil.concatClasses("rf-tgp", attributeAsString(component, "styleClass"));
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component)
        throws IOException {

        renderChildren(context, component);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.TogglePanel",
            component.getClientId(), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractTogglePanel panel = (AbstractTogglePanel) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("activeItem", panel.getActiveItem());
        options.put("cycledSwitching", panel.isCycledSwitching());
        options.put("items", getChildrenScriptObjects(context, panel));
        options.put("ajax", getAjaxOptions(context, panel));

        addEventOption(context, panel, options, ITEM_CHANGE);
        addEventOption(context, panel, options, BEFORE_ITEM_CHANGE);

        return options;
    }

    public static void addEventOption(FacesContext context, UIComponent component, Map<String, Object> options,
                                String eventName) {

        HandlersChain handlersChain = new HandlersChain(context, component);
        handlersChain.addInlineHandlerFromAttribute(ON + eventName);
        handlersChain.addBehaviors(eventName);
//        handlersChain.addAjaxSubmitFunction();

        String handler = handlersChain.toScript();
        if (handler != null) {
            options.put(ON + eventName,
                new JSFunctionDefinition(JSReference.EVENT).addToBody(handler));
        }
    }

    public static AjaxEventOptions getAjaxOptions(FacesContext context, UIComponent panel) {
        return AjaxRendererUtils.buildEventOptions(context, panel);
    }

    protected List<JSObject> getChildrenScriptObjects(FacesContext context, UIComponent component) {
        List<JSObject> res = new ArrayList<JSObject>(component.getChildCount());
        for (UIComponent child : component.getChildren()) {
            res.add(getChildScriptObject(context, (AbstractTogglePanelItem) child));
        }
        return res;
    }

    private JSObject getChildScriptObject(FacesContext context, AbstractTogglePanelItem child) {
        return ((TogglePanelItemRenderer) child.getRenderer(context))
                            .getScriptObject(context, child);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTogglePanel.class;
    }
}

