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

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for default applikation render kit. Only can append any
 * custom renderers , all other methods delegate to default render kit.
 *
 * @author shura
 * @see javax.faces.render.RenderKit
 */
public class ChameleonRenderKitImpl extends RenderKit implements ChameleonRenderKit {
    public static final String RENDER_KIT_ID = "AJAX_HTML_BASIC";
    private RenderKit defaultRenderKit = null;
    private Map<String, Renderer> renderers;

    public ChameleonRenderKitImpl() {
        renderers = new HashMap<String, Renderer>();
    }

    private String key(String componentFamily, String rendererType) {
        return componentFamily + "." + rendererType;
    }

    /**
     * @param family
     * @param rendererType
     * @param renderer
     */
    public void addRenderer(String family, String rendererType, Renderer renderer) {
        renderers.put(key(family, rendererType), renderer);
    }

    /**
     * @param writer
     * @param contentTypeList
     * @param characterEncoding
     * @return
     */
    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
        return getDefaultRenderKit().createResponseWriter(writer, contentTypeList, characterEncoding);
    }

    /**
     * @param family
     * @param rendererType
     * @return
     */
    public Renderer getRenderer(String family, String rendererType) {
        Renderer renderer = (Renderer) renderers.get(key(family, rendererType));

        if (renderer != null) {
            return renderer;
        }

        return getDefaultRenderKit().getRenderer(family, rendererType);
    }

    /**
     * @return
     */
    public ResponseStateManager getResponseStateManager() {
        return getDefaultRenderKit().getResponseStateManager();
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.render.RenderKit#createResponseStream(java.io.OutputStream)
     */
    public ResponseStream createResponseStream(OutputStream out) {
        return getDefaultRenderKit().createResponseStream(out);
    }

    /**
     * TODO - create own implementation for @see javax.faces.component.UIViewRoot ,
     * and get default renderKitId as value, setted at creation time.
     * in this case, we can substitute concrete renderKit even if it pointed
     * as value for <f:view> tag on 1.2 specification.
     *
     * @return Returns the defaultRenderer.
     */
    protected RenderKit getDefaultRenderKit() {
        if (defaultRenderKit == null) {
            String defaultRenderkitId = null;
            FacesContext context = null;
            RenderKitFactory rdf = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

            try {
                context = FacesContext.getCurrentInstance();

                // IN JSF-RI verifications, context may be null !
                if (null != context) {
                    defaultRenderkitId = context.getApplication().getDefaultRenderKitId();
                } else {
                    ApplicationFactory appFactory =
                        (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

                    defaultRenderkitId = appFactory.getApplication().getDefaultRenderKitId();
                }
            } catch (Exception e) {

                // TODO: handle exception
            }

            if (defaultRenderkitId == null) {
                defaultRenderkitId = RenderKitFactory.HTML_BASIC_RENDER_KIT;
            }

            defaultRenderKit = rdf.getRenderKit(context, defaultRenderkitId);
        }

        return defaultRenderKit;
    }

    public void setDefaultRenderKit(RenderKit renderKit) {

        // TODO Auto-generated method stub
        this.defaultRenderKit = renderKit;
    }
}
