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
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractToolbar;
import org.richfaces.component.AbstractToolbarGroup;
import org.richfaces.renderkit.HtmlConstants;

@JsfRenderer(type = ToolbarGroupRenderer.RENDERER_TYPE, family = AbstractToolbar.COMPONENT_FAMILY)
public class ToolbarGroupRenderer extends ToolbarRendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.ToolbarGroupRenderer";

    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractToolbarGroup.class;
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractToolbarGroup toolbarGroup = (AbstractToolbarGroup) component;
        List<UIComponent> renderedChildren = toolbarGroup.getRenderedChildren();
        if (renderedChildren.size() <= 0) {
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        renderChild(facesContext, toolbarGroup, writer, renderedChildren.get(0));
        for (int i = 1; i < renderedChildren.size(); i++) {
            insertSeparatorIfNeed(facesContext, toolbarGroup, writer);
            renderChild(facesContext, toolbarGroup, writer, renderedChildren.get(i));
        }
    }

    private void renderChild(FacesContext facesContext, AbstractToolbarGroup toolbarGroup, ResponseWriter writer,
            UIComponent child) throws IOException {
        writer.startElement(HtmlConstants.TD_ELEM, toolbarGroup);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, encodeClientItemID(child), null);
        writeClassValue(toolbarGroup, writer);
        writeStyleValue(toolbarGroup, writer);
        child.encodeAll(facesContext);
        writer.endElement(HtmlConstants.TD_ELEM);
    }

    private void writeStyleValue(AbstractToolbarGroup toolbarGroup, ResponseWriter writer) throws IOException {
        String style = getStringAttribute(toolbarGroup, HtmlConstants.STYLE_ATTRIBUTE);
        String itemStyle = getStringAttribute(getParentToolBar(toolbarGroup), "itemStyle");

        String value = concatStyles(itemStyle, style);

        if (isPropertyRendered(value)) {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, value, null);
        }
    }

    private void writeClassValue(AbstractToolbarGroup toolbarGroup, ResponseWriter writer) throws IOException {
        String styleClass = getStringAttribute(toolbarGroup, HtmlConstants.STYLE_CLASS_ATTR);
        AbstractToolbar toolbar = getParentToolBar(toolbarGroup);
        String itemClass = null;
        
        if(toolbar != null) {
            itemClass = getStringAttribute(toolbar, "itemClass");
        }
        
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, concatClasses("rf-tb-itm", itemClass, styleClass), null);
    }

    private String getStringAttribute(UIComponent toolbarGroup, String attribute) {
        String value = (String) toolbarGroup.getAttributes().get(attribute);
        return null == value ? "" : value;
    }
    
    public AbstractToolbar getParentToolBar(UIComponent component) {
        return (component instanceof AbstractToolbarGroup) ? ((AbstractToolbarGroup) component).getToolBar() : null;
    }    

}