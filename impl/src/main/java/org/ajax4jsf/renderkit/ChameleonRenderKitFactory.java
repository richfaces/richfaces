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

import java.util.Iterator;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import org.ajax4jsf.Messages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:50 $
 */
public class ChameleonRenderKitFactory extends RenderKitFactory {

    // private static final String AJAX_BASE_RENDER_KIT_PARAMETER =
    // "AJAX_BASE_RENDERKIT_ID";
    // private static final String AJAX_RENDER_KIT_PARAMETER =
    // "AJAX_RENDER_KIT_ID";
    private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();
    RenderKitFactory defaultFactory;

    /**
     * @param defaultFactory
     */
    public ChameleonRenderKitFactory(RenderKitFactory defaultFactory) {
        if (LOG.isDebugEnabled()) {

            // $NON-NLS-1$
            LOG.debug("ChameleonRenderKitFactory(RenderKitFactory) - Chameleon RenderKit factory instantiated");
        }

        this.defaultFactory = defaultFactory;
    }

    /**
     * @param renderKitId
     * @param renderKit
     */
    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        if (LOG.isDebugEnabled()) {

            // $NON-NLS-1$
            LOG.debug("addRenderKit(String, RenderKit) - Added RenderKit with id - renderKitId=" + renderKitId);
        }

        if (renderKit instanceof ChameleonRenderKit) {

            // ChameleonRenderKit chameleonRenderKit = (ChameleonRenderKit)
            // renderKit;
            // chameleonRenderKit.setConfiguration(ConfigurationFactory.getRendererConfigurationInstance(renderKitId))
            // ;
        }

        defaultFactory.addRenderKit(renderKitId, renderKit);
    }

    /**
     * @param context
     * @param renderKitId
     * @return
     */
    public RenderKit getRenderKit(FacesContext context, String renderKitId) {
        RenderKit renderKit = defaultFactory.getRenderKit(context, renderKitId);

        if (renderKit instanceof ChameleonRenderKit) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.REQUEST_CHAMELEON_RENDER_KIT_INFO, renderKitId));
            }

            String baseRenderKitId = null;

            // TODO - get DefaultRenderKitId from ViewHandler ?
            try {

                // IN JSF-RI verifications, context may be null !
                if (null != context) {
                    baseRenderKitId = context.getApplication().getDefaultRenderKitId();
                } else {
                    ApplicationFactory appFactory =
                        (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

                    baseRenderKitId = appFactory.getApplication().getDefaultRenderKitId();
                }
            } catch (Exception e) {
                LOG.warn(Messages.getMessage(Messages.GET_DEFAULT_RENDER_KIT_ERROR), e);
            }

            if (baseRenderKitId == null) {
                baseRenderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.DEFAULT_RENDER_KIT_INFO, baseRenderKitId));
            }

            ((ChameleonRenderKit) renderKit).setDefaultRenderKit(defaultFactory.getRenderKit(context, baseRenderKitId));
        }

        return renderKit;
    }

    /**
     * @return
     */
    public Iterator<String> getRenderKitIds() {
        return defaultFactory.getRenderKitIds();
    }
}
