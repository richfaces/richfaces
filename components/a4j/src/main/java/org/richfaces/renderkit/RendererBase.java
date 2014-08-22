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
package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.ajax4jsf.Messages;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.util.RendererUtils;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * Base Renderer for all chameleon Skin's and components. At most, make all common procedures and realise concrete work in
 * "template" methods.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:51 $
 */
public abstract class RendererBase extends Renderer {
    private static final Logger LOG = RichfacesLogger.RENDERKIT.getLogger();
    private static final RendererUtils UTILS = RendererUtils.getInstance();
    private SkinFactory skinFactory = null;

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public final void decode(FacesContext context, UIComponent component) {

        // Test for correct parameters.
        checkNull(context, component, "decode");

        if (!getComponentClass().isInstance(component)) {
            throw new IllegalArgumentException(Messages.getMessage(Messages.COMPONENT_CLASS_ERROR, "", getComponentClass()
                .getName()));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.START_DECODING_COMPONENT_INFO, component.getClientId(context), component
                .getClass().getName()));
        }

        preDecode(context, component);

        // TODO - create set od common decoders ( UIInput, ActionSource etc. ) for process decoding.
        if (component.isRendered()) {
            String behaviorEventName = RenderKitUtils.decodeBehaviors(context, component);
            if (behaviorEventName != null) {
                queueComponentEventForBehaviorEvent(context, component, behaviorEventName);
            }

            doDecode(context, component);
        }
    }

    protected void queueComponentEventForBehaviorEvent(FacesContext context, UIComponent component, String eventName) {

    }

    protected void preDecode(FacesContext context, UIComponent component) {
    }

    protected void preEncodeBegin(FacesContext context, UIComponent component) throws IOException {
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public final void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        // Test for correct parameters.
        checkForCorrectParams(context, component, "encodeBegin");

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.START_ENCODING_COMPONENT_INFO, component.getClientId(context), component
                .getClass().getName()));
        }

        preEncodeBegin(context, component);

        if (component.isRendered()) {
            ResponseWriter writer = context.getResponseWriter();

            doEncodeBegin(writer, context, component);
        }
    }

    private void checkForCorrectParams(FacesContext context, UIComponent component, String exceptionMessageParam) {
        checkNull(context, component, exceptionMessageParam);

        if (!getComponentClass().isInstance(component)) {
            throw new IllegalArgumentException(Messages.getMessage(Messages.COMPONENT_CLASS_ERROR,
                component.getClientId(context), getComponentClass().getName()));
        }
    }

    private void checkNull(Object context, Object component, String exceptionMessageParam) {
        if (context == null) {
            throw new NullPointerException(Messages.getMessage(Messages.CONTEXT_NULL_ERROR, exceptionMessageParam));
        }

        if (component == null) {
            throw new NullPointerException(Messages.getMessage(Messages.COMPONENT_NULL_ERROR, exceptionMessageParam));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public final void encodeChildren(FacesContext context, UIComponent component) throws IOException {

        // Test for correct parameters.
        checkForCorrectParams(context, component, "encodeBegin");

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.CHILDREN_ENCODING_COMPONENT_INFO, component.getClientId(context), component
                .getClass().getName()));
        }

        if (component.isRendered()) {
            ResponseWriter writer = context.getResponseWriter();

            doEncodeChildren(writer, context, component);
        }
    }

    public boolean isAlreadyRendered(UIComponent component) {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public final void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        // Test for correct parameters.
        checkForCorrectParams(context, component, "encodeEnd");

        if (component.isRendered() && !isAlreadyRendered(component)) {
            ResponseWriter writer = context.getResponseWriter();

            doEncodeEnd(writer, context, component);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.FINISH_ENCODING_COMPONENT_INFO, component.getClientId(context), component
                .getClass().getName()));
        }
    }

    // ==========================================================
    // Protected common methods - for all Renderer's

    /**
     * Calculate current Skin for rendering.
     *
     * @param context - current {@link FacesContext }
     * @return
     */
    protected Skin getSkin(FacesContext context) {
        if (skinFactory == null) {
            skinFactory = SkinFactory.getInstance(context);
        }

        return skinFactory.getSkin(context);
    }

    /**
     * Due to big number of common utility methods, base renderer divide to 2 classes - renderer and utils. since use static
     * methods of utility class breack object paradigm, we use getter for concrete util instance. Developer can override any
     * utility metod in 2 stages : 1) Create subclass of {@link RendererUtils} and override utility method. 2) Override this
     * method for return instance of such subclass.
     *
     * @return Returns the utils.
     */
    public RendererUtils getUtils() {
        return UTILS;
    }

    /**
     * Get base component slass , targetted for this renderer. Used for check arguments in decode/encode.
     *
     * @return
     */
    protected Class<? extends UIComponent> getComponentClass() {
        // TODO - do we need this function?
        return UIComponent.class;
    }

    /**
     * Template method for custom decoding of concrete renderer. All parameters checking if performed in original {@see
     * decode(FacesContext, UIComponent) }
     * method.
     *
     * @param context
     * @param component
     */
    protected void doDecode(FacesContext context, UIComponent component) {
    }

    /**
     * Template method for custom start encoding of concrete renderer. All parameters checking and writer is performed in
     * original {@link encodeBegin(FacesContext, UIComponent) } method.
     *
     * @param writer
     * @param context
     * @param component
     */
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
    }

    /**
     * @param writer
     * @param context
     * @param component
     */
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        // Hook method, must be overriden in renderers with special children processing
    }

    /**
     * Template method for custom finish encoding of concrete renderer. All parameters checking and writer is performed in
     * original {@link encodeEnd(FacesContext, UIComponent) } method.
     *
     * @param writer
     * @param context
     * @param component
     * @throws IOException
     */
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
    }

    /**
     * Render all children for given component.
     *
     * @param facesContext
     * @param component
     * @throws IOException
     */
    public void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                child.encodeAll(facesContext);
            }
        }
    }

    public String concatClasses(Object... objects) {
        return HtmlUtil.concatClasses(objects);
    }

    public String concatStyles(Object... objects) {
        return HtmlUtil.concatStyles(objects);
    }

    public String getResourcePath(FacesContext context, String library, String resourceName) {
        return RenderKitUtils.getResourcePath(context, library, resourceName);
    }
}
