/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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
package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractOutputPanel;
import org.richfaces.component.OutputPanelLayout;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:27 $
 */
@JsfRenderer(type = "org.richfaces.OutputPanelRenderer", family = AbstractOutputPanel.COMPONENT_FAMILY)
public class AjaxOutputPanelRenderer extends RendererBase {
    private static final String[] STYLE_ATTRIBUTES = new String[] { "style", "class" };

    private boolean hasNoneLayout(UIComponent component) {
        // TODO - A1 won't support 'none' layout
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        //
        renderChildren(context, component);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.renderkit.RendererBase#getComponentClass()
     */
    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractOutputPanel.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.renderkit.RendererBase#doEncodeBegin(javax.faces.context.ResponseWriter,
     * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        AbstractOutputPanel panel = (AbstractOutputPanel) component;
        if (!hasNoneLayout(component)) {
            writer.startElement(getTag(panel), panel);
            getUtils().encodeId(context, component);
            getUtils().encodePassThru(context, component, null);
            getUtils().encodeAttributesFromArray(context, component, STYLE_ATTRIBUTES);
        }
    }

    /**
     * @param panel
     * @return
     */
    private String getTag(AbstractOutputPanel panel) {
        Object layout = panel.getAttributes().get("layout");
        return OutputPanelLayout.block.equals(layout) ? HtmlConstants.DIV_ELEM : HtmlConstants.SPAN_ELEM;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.renderkit.RendererBase#doEncodeEnd(javax.faces.context.ResponseWriter,
     * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractOutputPanel panel = (AbstractOutputPanel) component;
        if (!hasNoneLayout(component)) {
            writer.endElement(getTag(panel));
        }
        if (panel.isKeepTransient()) {
            markNoTransient(component);
        }
    }

    /**
     * Set "transient" flag to false for component and all its children ( recursive ).
     *
     * @param component
     */
    private void markNoTransient(UIComponent component) {
        for (Iterator<UIComponent> iter = component.getFacetsAndChildren(); iter.hasNext();) {
            UIComponent element = iter.next();
            // RF-12295: UIInstructions can be set to non-transient in Mojarra which causes an exception on postback
            // because non-transient components require a zero-parameter to be restored with;
            // (no issue in MyFaces as UIInstructions.setTransient() is an empty method)
            if (!"com.sun.faces.facelets.compiler.UIInstructions".equals(element.getClass().getName())) {
                markNoTransient(element);
                element.setTransient(false);
            }
        }
    }
}
