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
import static org.richfaces.renderkit.HtmlConstants.DIV_ELEM;
import static org.richfaces.renderkit.HtmlConstants.ID_ATTRIBUTE;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.application.ServiceTracker;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractTab;
import org.richfaces.component.AbstractTabPanel;
import org.richfaces.component.AbstractTogglePanelItemInterface;
import org.richfaces.component.ComponentIterators;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.component.VisitChildrenRejectable;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.util.AjaxRendererUtils;

import com.google.common.base.Predicate;

/**
 * @author akolonitsky
 * @since 2010-08-24
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "togglePanelItem.js"),
        @ResourceDependency(library = "org.richfaces", name = "tab.js") })
@JsfRenderer(type = "org.richfaces.TabRenderer", family = AbstractTab.COMPONENT_FAMILY)
public class TabRenderer extends TogglePanelItemRenderer {
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {

        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();

        AbstractTab tab = (AbstractTab) component;
        String compClientId = component.getClientId(context);

        AbstractTabPanel parentTabPanel = getParentTabPanel(tab);

        PartialViewContext pvc = context.getPartialViewContext();

        if (pvc.isPartialRequest()) {
            // encode the tabPanel headers when the tab is encoded
            String headerMetaComponentId = parentTabPanel.getClientId(context)
                + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + AbstractTabPanel.HEADER_META_COMPONENT;
            pvc.getRenderIds().add(headerMetaComponentId);
        }

        if (requestMap.get(compClientId) != null) {
            if (parentTabPanel.isImmediate()) {
                tab.setImmediate(true);
            }
            new ActionEvent(tab).queue();
        }
    }

    @Override
    protected void doEncodeItemBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        super.doEncodeItemBegin(writer, context, component);
        encodeContentBegin(context, component, writer);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-tab", attributeAsString(component, "styleClass"));
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        AbstractTab tab = (AbstractTab) component;

        if (tab.shouldVisitChildren() && !tab.isDisabled()) {
            super.doEncodeChildren(writer, context, tab);
        }
    }

    @Override
    protected void doEncodeItemEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeContentEnd(component, writer);

        jsService().addScript(context, getScriptObject(context, component));

        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (((VisitChildrenRejectable) component).shouldVisitChildren()) {
            doEncodeItemEnd(writer, context, component);
        } else {
            encodePlaceHolder(context, component);
            jsService().addScript(context, getScriptObject(context, component));
        }
    }

    private JavaScriptService jsService() {
        return ServiceTracker.getService(JavaScriptService.class);
    }

    private void encodeContentBegin(FacesContext context, UIComponent component, ResponseWriter writer) throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses("rf-tab-cnt", attributeAsString(component, "contentClass")), null);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":content", null);
    }

    private void encodeContentEnd(UIComponent component, ResponseWriter responseWriter) throws IOException {
        responseWriter.endElement(DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.Tab", component.getClientId(context), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> res = super.getScriptObjectOptions(context, component);
        res.put("disabled", ((AbstractTab) component).isDisabled());
        res.put("enter", ((AbstractTogglePanelItemInterface) component).getOnenter());
        res.put("leave", ((AbstractTogglePanelItemInterface) component).getOnleave());
        res.put("ajax", AjaxRendererUtils.buildEventOptions(context, component));
        return res;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTab.class;
    }

    private static AbstractTabPanel getParentTabPanel(AbstractTab menuItem) {
        return (AbstractTabPanel) ComponentIterators.getParent(menuItem, new Predicate<UIComponent>() {
            public boolean apply(UIComponent component) {
                return component instanceof AbstractTabPanel;
            }
        });
    }
}
