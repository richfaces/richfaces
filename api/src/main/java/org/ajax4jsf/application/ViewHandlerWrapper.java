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



package org.ajax4jsf.application;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Base wrapper for {@link javax.faces.application.ViewHandler} . By default, delegate all
 * method calls to wrapped handler.
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:12 $
 *
 */

//TODO remove
public class ViewHandlerWrapper extends ViewHandler {
    private static final String HANDLERS = "org.ajax4jsf.VIEW_HANDLERS";
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();

    /**
     * Wrapped ViewHandler
     */
    protected ViewHandler handler;

    /**
     * @param handler - to wrap.
     */
    public ViewHandlerWrapper(ViewHandler handler) {
        this.handler = handler;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#calculateLocale(javax.faces.context.FacesContext)
     */
    public Locale calculateLocale(FacesContext context) {
        return handler.calculateLocale(context);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)
     */
    public String calculateRenderKitId(FacesContext context) {
        return handler.calculateRenderKitId(context);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#createView(javax.faces.context.FacesContext, java.lang.String)
     */
    public UIViewRoot createView(FacesContext context, String viewId) {
        return handler.createView(context, viewId);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#getActionURL(javax.faces.context.FacesContext, java.lang.String)
     */
    public String getActionURL(FacesContext context, String url) {
        return handler.getActionURL(context, url);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext, java.lang.String)
     */
    public String getResourceURL(FacesContext context, String url) {
        return handler.getResourceURL(context, url);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#renderView(javax.faces.context.FacesContext,
     * javax.faces.component.UIViewRoot)
     */
    public void renderView(FacesContext context, UIViewRoot root) throws IOException, FacesException {
        handler.renderView(context, root);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#restoreView(javax.faces.context.FacesContext, java.lang.String)
     */
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return handler.restoreView(context, viewId);
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#writeState(javax.faces.context.FacesContext)
     */
    public void writeState(FacesContext context) throws IOException {
        handler.writeState(context);
    }

    /**
     * @return Returns the handler.
     */
    protected ViewHandler getHandler() {
        return handler;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.application.ViewHandler#initView(javax.faces.context.FacesContext)
     */
    public void initView(FacesContext context) throws FacesException {
        handler.initView(context);
    }

    public String calculateCharacterEncoding(FacesContext context) {

        // TODO Auto-generated method stub
        return handler.calculateCharacterEncoding(context);
    }

    /**
     * <p>Fill view-handlers chain for alternate handlers.</p>
     * <p><em>NOTE:</em> Calls to this method should be synchronized externally since 3.3.0 version</p>
     *
     * @param context
     */
    public void fillChain(FacesContext context) {
        String handlers = context.getExternalContext().getInitParameter(HANDLERS);

        if (null != handlers) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String[] classes = handlers.split(",");

            for (int i = 0; i < classes.length; i++) {
                String handlerClass = classes[i];

                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getMessage(Messages.CREATE_ALTERNATE_HANDLER, handlerClass));
                }

                try {
                    Class<?> clazz = classLoader.loadClass(handlerClass);

                    try {
                        Constructor<?> constructor = clazz.getConstructor(new Class[] {ViewHandler.class});

                        handler = (ViewHandler) constructor.newInstance(new Object[] {handler});
                    } catch (NoSuchMethodException e) {

                        // No constructor with parent class - create simple instance
                        if (LOG.isWarnEnabled()) {
                            LOG.warn(Messages.getMessage(Messages.ALTERNATE_HANDLER_CONSTRUCTOR_WARNING));
                        }

                        handler = (ViewHandler) clazz.newInstance();
                    }
                } catch (Exception e) {
                    LOG.error(Messages.getMessage(Messages.VIEW_HANDLER_INSTANTIATION_ERROR), e);
                }
            }
        }
    }
}
