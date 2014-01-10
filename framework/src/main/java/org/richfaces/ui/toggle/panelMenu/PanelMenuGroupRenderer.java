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
package org.richfaces.ui.toggle.panelMenu;

import com.google.common.base.Strings;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.javascript.JSObject;
import org.richfaces.ui.common.DivPanelRenderer;
import org.richfaces.ui.common.TableIconsRendererHelper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.richfaces.ui.common.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.DIV_ELEM;
import static org.richfaces.ui.common.HtmlConstants.ID_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.INPUT_ELEM;
import static org.richfaces.ui.common.HtmlConstants.INPUT_TYPE_HIDDEN;
import static org.richfaces.ui.common.HtmlConstants.NAME_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.TYPE_ATTR;
import static org.richfaces.ui.common.HtmlConstants.VALUE_ATTRIBUTE;
import static org.richfaces.ui.toggle.togglePanel.TogglePanelRenderer.addEventOption;
import static org.richfaces.ui.toggle.togglePanel.TogglePanelRenderer.getAjaxOptions;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
@JsfRenderer(type = "org.richfaces.ui.PanelMenuGroupRenderer", family = AbstractPanelMenuGroup.COMPONENT_FAMILY)
public class PanelMenuGroupRenderer extends DivPanelRenderer {
    public static final String COLLAPSE = "collapse";
    public static final String EXPAND = "expand";
    public static final String SWITCH = "switch";
    public static final String BEFORE_COLLAPSE = "beforecollapse";
    public static final String BEFORE_EXPAND = "beforeexpand";
    public static final String BEFORE_SWITCH = "beforeswitch";
    public static final String BEFORE_SELECT = "beforeselect";
    public static final String SELECT = "select";
    private static final String CSS_CLASS_PREFIX = "rf-pm-gr";
    // TODO nick - shouldn't this be rf-pm-gr-top?
    private static final String TOP_CSS_CLASS_PREFIX = "rf-pm-top-gr";
    private static final String DEFAULT_EXPAND_EVENT = "click";
    private static final String DEFAULT_COLLAPSE_EVENT = "click";
    private final TableIconsRendererHelper<AbstractPanelMenuGroup> headerRenderer = new PanelMenuGroupHeaderRenderer(
        CSS_CLASS_PREFIX);
    private final TableIconsRendererHelper<AbstractPanelMenuGroup> topHeaderRenderer = new PanelMenuGroupHeaderRenderer(
        TOP_CSS_CLASS_PREFIX);

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractPanelMenuGroup menuGroup = (AbstractPanelMenuGroup) component;

        if (menuGroup.isDisabled()) {
            return;
        }

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        // Don't overwrite the value unless you have to!
        // TODO! nick - ":expanded" suffix is not necessary
        String newValue = requestMap.get(component.getClientId(context) + ":expanded");
        if (newValue != null) {
            menuGroup.setSubmittedExpanded(newValue);
        }

        String clientId = component.getClientId(context);
        if (requestMap.get(clientId) != null) {
            new ActionEvent(component).queue();
            context.getPartialViewContext().getRenderIds().add(clientId);
        }
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        AbstractPanelMenuGroup menuGroup = (AbstractPanelMenuGroup) component;

        writer.startElement(INPUT_ELEM, component);

        // TODO nick - there is no need to encode this input - group state can be extracted from class
        final String expanded = component.getClientId(context) + ":expanded";
        writer.writeAttribute(ID_ATTRIBUTE, expanded, null);
        writer.writeAttribute(NAME_ATTRIBUTE, expanded, null);
        writer.writeAttribute(TYPE_ATTR, INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(VALUE_ATTRIBUTE, String.valueOf(menuGroup.getState()), null);
        writer.endElement(INPUT_ELEM);

        encodeHeader(writer, context, menuGroup);
        encodeContentBegin(writer, context, menuGroup);
    }

    private void encodeHeader(ResponseWriter writer, FacesContext context, AbstractPanelMenuGroup menuGroup) throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(ID_ATTRIBUTE, menuGroup.getClientId(context) + ":hdr", null);
        writer.writeAttribute(
            CLASS_ATTRIBUTE,
            concatClasses(
                getCssClass(menuGroup, "-hdr"),
                "rf-pm-hdr-" + (menuGroup.getState() ? "exp" : "colps"),
                (menuGroup.getPanelMenu().isBubbleSelection()
                    && menuGroup.hasActiveItem(menuGroup, menuGroup.getPanelMenu().getActiveItem()) ? getCssClass(menuGroup,
                    "-sel") : ""),
                PanelMenuItemRenderer.isParentPanelMenuDisabled(menuGroup) || menuGroup.isDisabled() ? getCssClass(menuGroup,
                    "-hdr-dis") : null), null);

        (menuGroup.isTopItem() ? topHeaderRenderer : headerRenderer).encodeHeader(writer, context, menuGroup);

        writer.endElement(DIV_ELEM);
    }

    public String getCssClass(AbstractPanelMenuItem item, String postfix) {
        return (item.isTopItem() ? TOP_CSS_CLASS_PREFIX : CSS_CLASS_PREFIX) + postfix;
    }

    private void encodeContentBegin(ResponseWriter writer, FacesContext context, AbstractPanelMenuGroup menuGroup)
        throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(ID_ATTRIBUTE, menuGroup.getClientId(context) + ":cnt", null);
        writer.writeAttribute(CLASS_ATTRIBUTE,
            concatClasses(getCssClass(menuGroup, "-cnt"), menuGroup.getState() ? "rf-pm-exp" : "rf-pm-colps"), null);

        writeJavaScript(writer, context, menuGroup);
    }

    private void encodeContentEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.endElement(DIV_ELEM);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        AbstractPanelMenuItem menuItem = (AbstractPanelMenuItem) component;

        return concatClasses(
            getCssClass(menuItem, ""),
            attributeAsString(component, "styleClass"),
            PanelMenuItemRenderer.isParentPanelMenuDisabled(menuItem) || menuItem.isDisabled() ? getCssClass(menuItem, "-dis")
                : "",
            PanelMenuItemRenderer.isParentPanelMenuDisabled(menuItem) || menuItem.isDisabled() ? attributeAsString(component,
                "disabledClass") : "");
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.PanelMenuGroup", component.getClientId(context), getScriptObjectOptions(context,
            component));
    }

    private String getExpandEvent(AbstractPanelMenuGroup group) {
        String expandEvent = group.getExpandEvent();
        if (Strings.isNullOrEmpty(expandEvent)) {
            expandEvent = group.getPanelMenu().getExpandEvent();
        }
        if (Strings.isNullOrEmpty(expandEvent)) {
            expandEvent = DEFAULT_EXPAND_EVENT;
        }
        return expandEvent;
    }

    private String getCollapseEvent(AbstractPanelMenuGroup group) {
        String collapseEvent = group.getCollapseEvent();
        if (Strings.isNullOrEmpty(collapseEvent)) {
            collapseEvent = group.getPanelMenu().getCollapseEvent();
        }
        if (Strings.isNullOrEmpty(collapseEvent)) {
            collapseEvent = DEFAULT_COLLAPSE_EVENT;
        }
        return collapseEvent;
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractPanelMenuGroup panelMenuGroup = (AbstractPanelMenuGroup) component;

        Map<String, Object> options = new HashMap<String, Object>();
        // TODO nick - non-default values should not be rendered
        options.put("ajax", getAjaxOptions(context, panelMenuGroup));
        options.put("name", panelMenuGroup.getName());
        options.put("mode", panelMenuGroup.getMode());
        options.put("disabled", PanelMenuItemRenderer.isParentPanelMenuDisabled(panelMenuGroup) || panelMenuGroup.isDisabled());
        options.put("expandEvent", getExpandEvent(panelMenuGroup));
        options.put("collapseEvent", getCollapseEvent(panelMenuGroup));
        options.put("expanded", panelMenuGroup.getState());
        options.put("selectable", panelMenuGroup.getSelectable());
        options.put("unselectable", panelMenuGroup.getUnselectable());
        options.put("stylePrefix", getCssClass(panelMenuGroup, ""));

        addEventOption(context, panelMenuGroup, options, COLLAPSE);
        addEventOption(context, panelMenuGroup, options, EXPAND);
        addEventOption(context, panelMenuGroup, options, SWITCH);
        addEventOption(context, panelMenuGroup, options, BEFORE_COLLAPSE);
        addEventOption(context, panelMenuGroup, options, BEFORE_EXPAND);
        addEventOption(context, panelMenuGroup, options, BEFORE_SWITCH);
        addEventOption(context, panelMenuGroup, options, BEFORE_SELECT);
        addEventOption(context, panelMenuGroup, options, SELECT);

        return options;
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(writer, context, component);

        writer.endElement(DIV_ELEM);

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(context);
        if (requestMap.get(clientId) != null) {
            addOnCompleteParam(context, clientId);
        }
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractPanelMenuGroup.class;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        AbstractPanelMenuGroup group = (AbstractPanelMenuGroup) component;

        boolean isClientMode = group.getMode() == PanelMenuMode.client;

        if (isClientMode || group.getState()) {
            renderChildren(context, component);
        }
    }
}
