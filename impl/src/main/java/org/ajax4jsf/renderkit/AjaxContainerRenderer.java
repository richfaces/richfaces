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

package org.ajax4jsf.renderkit;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.AjaxContainer;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxEvent;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Base renderer for all AJAX - subview components.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/08 15:02:02 $
 */
public class AjaxContainerRenderer extends AjaxChildrenRenderer {
    public static final String AJAX_FLAG_HEADER = "Ajax-Response";
    public static final String AJAX_JSF_SCRIPT = "AJAX.js";
    public static final String AJAX_LOCATION_HEADER = "Location";
    public static final String AJAX_PARAMETER_NAME = "AJAXREQUEST";
    public static final String AJAX_RESULT_GROUP_ATTR = "content";
    public static final String AJAX_RESULT_GROUP_TAG = "meta";
    public static final String AJAX_RESULT_STYLE = "display: none";
    public static final String AJAX_UPDATE_HEADER = "Ajax-Update-Ids";
    public static final String AJAX_VIEW_STATE_ID = "ajax-view-state";
    public static final String AJAX_VIEW_STATE_TAG = "span";
    public static final String JSDOM_SCRIPT = "JSDOM_sarissa.js";
    public static final String SARISSA_SCRIPT = "sarissa.js";
    private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.renderkit.RendererBase#getComponentClass()
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Class getComponentClass() {

        // TODO Auto-generated method stub
        return AjaxContainer.class;
    }

    /**
     * @see javax.faces.component.UIComponent#encodeChildren(javax.faces.context.FacesContext)
     *      Since main function of component - render subset of components, for
     *      update on page,don't delegate this for renderer.
     */
    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

        // Normal request - render all ...
        LOG.debug(Messages.getMessage(Messages.RENDER_CHILDREN_NON_AJAX_INFO));
        renderChildren(context, component);
    }

    /**
     * @param context
     * @param component
     * @throws IOException
     */
    public void encodeAjax(FacesContext context, UIComponent component) throws IOException {
        UIComponent root;

        // Iterate over all childrens, render it if nessesary...
        LOG.debug(Messages.getMessage(Messages.RENDER_CHILDREN_AJAX_INFO));

        AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
        Set<String> ids = ajaxContext.getAjaxAreasToRender();
        Set<String> renderedAreas = ajaxContext.getAjaxRenderedAreas();

        if (((AjaxContainer) component).isRenderRegionOnly()) {
            root = component;
        } else {
            root = context.getViewRoot();
        }

        String path = AjaxRendererUtils.getAbsoluteId(root);

        // if(! (component instanceof NamingContainer)){
        path = path.substring(0, path.lastIndexOf(NamingContainer.SEPARATOR_CHAR) + 1);

        if (isParentRendered(root)) {
            encodeAjaxComponent(context, root, path, ids, renderedAreas);
        }

        // Write information about encoded areas after submission.
        AjaxRendererUtils.encodeAreas(context, component);
    }

    public boolean isParentRendered(UIComponent target) {
        UIComponent component = target;

        while (component != null) {
            if (!component.isRendered()) {
                return false;
            }

            component = component.getParent();
        }

        return true;
    }

    /*
     * always return true, since component must maintain set of rendered
     * components.
     *
     * @see javax.faces.component.UIComponent#getRendersChildren()
     */
    @Override
    public boolean getRendersChildren() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ajax4jsf.renderkit.RendererBase#doDecode(javax.faces.context.FacesContext
     * , javax.faces.component.UIComponent)
     */
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        String clientId = component.getClientId(context);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.DECODE_AJAX_REQUEST_STATUS_INFO, clientId));

            // log.debug(Messages.getMessage(Messages.REQUEST_PARAMETERS_MAP,
            // paramMap.toString()));
        }

        Object ajaxParameter = paramMap.get(AJAX_PARAMETER_NAME);
        AjaxContainer ajaxContainer = (AjaxContainer) component;

        if ((null != ajaxParameter) && ajaxParameter.equals(clientId)) {
            ajaxContainer.setSubmitted(true);

            if (ajaxContainer.isSelfRendered()) {
                AjaxContext.getCurrentInstance(context).setSelfRender(true);
            }

            AjaxEvent event = new AjaxEvent(component);

            component.queueEvent(event);
        } else {
            ajaxContainer.setSubmitted(false);
        }
    }
}
