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

import static org.richfaces.renderkit.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.TBODY_ELEMENT;
import static org.richfaces.renderkit.HtmlConstants.TD_ELEM;
import static org.richfaces.renderkit.HtmlConstants.TR_ELEMENT;
import static org.richfaces.renderkit.html.TogglePanelRenderer.addEventOption;
import static org.richfaces.renderkit.html.TogglePanelRenderer.getAjaxOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractPanelMenu;
import org.richfaces.component.AbstractPanelMenuItem;
import org.richfaces.component.ComponentIterators;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.renderkit.util.PanelIcons;
import org.richfaces.renderkit.util.PanelIcons.State;

import com.google.common.base.Predicate;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
@JsfRenderer(type = "org.richfaces.PanelMenuItemRenderer", family = AbstractPanelMenuItem.COMPONENT_FAMILY)
public class PanelMenuItemRenderer extends DivPanelRenderer {
    public static final String SELECT = "select";
    public static final String BEFORE_SELECT = "beforeselect";
    private static final String CSS_CLASS_PREFIX = "rf-pm-itm";
    private static final String TOP_CSS_CLASS_PREFIX = "rf-pm-top-itm";
    private static final ParentPanelMenuPredicate PARENT_PANEL_MENU_PREDICATE = new ParentPanelMenuPredicate();

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        AbstractPanelMenuItem menuItem = (AbstractPanelMenuItem) component;
        String compClientId = component.getClientId(context);
        if (requestMap.get(compClientId) != null) {
            if (menuItem.isDisabled() || menuItem.isParentDisabled()) {
                return;
            }
            AbstractPanelMenu parentPanelMenu = getParentPanelMenu(menuItem);

            if (parentPanelMenu.isImmediate()) {
                menuItem.setImmediate(true);
            }
            new ActionEvent(menuItem).queue();

            context.getPartialViewContext().getRenderIds().add(component.getClientId(context));
        }
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        AbstractPanelMenuItem menuItem = (AbstractPanelMenuItem) component;
        encodeHeaderGroupBegin(writer, context, menuItem, getCssClass(menuItem, ""));
    }

    private void encodeHeaderGroupBegin(ResponseWriter writer, FacesContext context, AbstractPanelMenuItem menuItem,
        String classPrefix) throws IOException {
        writer.startElement("table", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, classPrefix + "-gr", null);
        writer.startElement(TBODY_ELEMENT, null);
        writer.startElement(TR_ELEMENT, null);

        encodeHeaderGroupLeftIcon(writer, context, menuItem, classPrefix);

        writer.startElement(TD_ELEM, null);
        writer.writeAttribute(CLASS_ATTRIBUTE, classPrefix + "-lbl", null);

        String label = menuItem.getLabel();
        if (label != null) {
            writer.writeText(label, null);
        }
    }

    private void encodeHeaderGroupEnd(ResponseWriter writer, FacesContext context, AbstractPanelMenuItem menuItem,
        String classPrefix) throws IOException {
        writer.endElement(TD_ELEM);

        encodeHeaderGroupRightIcon(writer, context, menuItem, classPrefix);

        writer.endElement(TR_ELEMENT);
        writer.endElement(TBODY_ELEMENT);
        writer.endElement("table");
    }

    private PanelIcons.State getState(AbstractPanelMenuItem item) {
        return PanelMenuItemRenderer.isParentPanelMenuDisabled(item) || item.isDisabled() ? State.commonDisabled : State.common;
    }

    private void encodeHeaderGroupRightIcon(ResponseWriter writer, FacesContext context, AbstractPanelMenuItem menuItem,
        String classPrefix) throws IOException {
        String icon = PanelMenuItemRenderer.isParentPanelMenuDisabled(menuItem) || menuItem.isDisabled() ? menuItem
            .getRightDisabledIcon() : menuItem.getRightIcon();
        String cssClasses = concatClasses(classPrefix + "-exp-ico", menuItem.getRightIconClass());

        if (icon == null || icon.trim().length() == 0) {
            icon = PanelIcons.transparent.toString();
        }
        encodeTdIcon(writer, context, cssClasses, icon, getState(menuItem));
    }

    private void encodeHeaderGroupLeftIcon(ResponseWriter writer, FacesContext context, AbstractPanelMenuItem menuItem,
        String classPrefix) throws IOException {
        String icon = PanelMenuItemRenderer.isParentPanelMenuDisabled(menuItem) || menuItem.isDisabled() ? menuItem
            .getLeftDisabledIcon() : menuItem.getLeftIcon();
        String cssClasses = concatClasses(classPrefix + "-ico", menuItem.getLeftIconClass());

        if (icon == null || icon.trim().length() == 0) {
            icon = PanelIcons.transparent.toString();
        }
        encodeTdIcon(writer, context, cssClasses, icon, getState(menuItem));
    }

    private boolean isIconRendered(String attrIconValue) {
        if (attrIconValue != null && attrIconValue.trim().length() > 0 && !PanelIcons.none.toString().equals(attrIconValue)) {
            return true;
        }
        return false;
    }

    // TODO nick - the same as in PanelMenuGroupRenderer
    public void encodeTdIcon(ResponseWriter writer, FacesContext context, String classPrefix, String attrIconValue,
        PanelIcons.State state) throws IOException {
        if (!isIconRendered(attrIconValue)) {
            return;
        }

        writer.startElement(TD_ELEM, null);
        try {
            PanelIcons icon = PanelIcons.valueOf(attrIconValue);
            writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses(classPrefix, state.getCssClass(icon)), null);
        } catch (IllegalArgumentException e) {
            writer.writeAttribute(CLASS_ATTRIBUTE, classPrefix, null);
            if (attrIconValue != null && attrIconValue.trim().length() != 0) {
                writer.startElement(HtmlConstants.IMG_ELEMENT, null);
                writer.writeAttribute(HtmlConstants.ALT_ATTRIBUTE, "", null);
                writer.writeURIAttribute(HtmlConstants.SRC_ATTRIBUTE, RenderKitUtils.getResourceURL(attrIconValue, context),
                    null);
                writer.endElement(HtmlConstants.IMG_ELEMENT);
            }
        }

        writer.endElement(TD_ELEM);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        AbstractPanelMenuItem menuItem = (AbstractPanelMenuItem) component;
        return concatClasses(
            getCssClass(menuItem, ""),
            attributeAsString(component, "styleClass"),
            PanelMenuItemRenderer.isParentPanelMenuDisabled(menuItem) || menuItem.isDisabled() ? getCssClass(menuItem, "-dis")
                : "",
            (menuItem.isActiveItem() ? getCssClass(menuItem, "-sel") : ""),
            PanelMenuItemRenderer.isParentPanelMenuDisabled(menuItem) || menuItem.isDisabled() ? attributeAsString(component,
                "disabledClass") : "");
    }

    public String getCssClass(AbstractPanelMenuItem item, String postfix) {
        return (item.isTopItem() ? TOP_CSS_CLASS_PREFIX : CSS_CLASS_PREFIX) + postfix;
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.PanelMenuItem", component.getClientId(context), getScriptObjectOptions(context,
            component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractPanelMenuItem panelMenuItem = (AbstractPanelMenuItem) component;

        Map<String, Object> options = new HashMap<String, Object>();
        // TODO nick - ajax options should not be rendered in client mode
        options.put("ajax", getAjaxOptions(context, panelMenuItem));
        options.put("disabled", PanelMenuItemRenderer.isParentPanelMenuDisabled(panelMenuItem) || panelMenuItem.isDisabled());
        options.put("mode", panelMenuItem.getMode());
        options.put("name", panelMenuItem.getName());
        options.put("selectable", panelMenuItem.getSelectable());
        options.put("unselectable", panelMenuItem.getUnselectable());
        options.put("stylePrefix", getCssClass(panelMenuItem, ""));

        addEventOption(context, panelMenuItem, options, SELECT);
        addEventOption(context, panelMenuItem, options, BEFORE_SELECT);

        return options;
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractPanelMenuItem menuItem = (AbstractPanelMenuItem) component;
        encodeHeaderGroupEnd(writer, context, menuItem, getCssClass(menuItem, ""));

        super.doEncodeEnd(writer, context, component);

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        if (requestMap.get(component.getClientId(context)) != null) {
            if (context.getPartialViewContext().isPartialRequest()) {
                addOnCompleteParam(context, component.getClientId(context));
            }
        }
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractPanelMenuItem.class;
    }

    private static AbstractPanelMenu getParentPanelMenu(AbstractPanelMenuItem menuItem) {
        return (AbstractPanelMenu) ComponentIterators.getParent(menuItem, PARENT_PANEL_MENU_PREDICATE);
    }

    static boolean isParentPanelMenuDisabled(AbstractPanelMenuItem menuItem) {
        AbstractPanelMenu parentPanelMenu = (AbstractPanelMenu) ComponentIterators.getParent(menuItem,
            PARENT_PANEL_MENU_PREDICATE);
        if (parentPanelMenu != null) {
            return parentPanelMenu.isDisabled();
        }
        return false;
    }

    private static class ParentPanelMenuPredicate implements Predicate<UIComponent> {
        public boolean apply(UIComponent comp) {
            return comp instanceof AbstractPanelMenu;
        }
    }
}
