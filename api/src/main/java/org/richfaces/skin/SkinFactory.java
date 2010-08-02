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

package org.richfaces.skin;

import javax.faces.context.FacesContext;

import org.richfaces.application.ServiceTracker;

/**
 * Base factory class ( implement Singleton design pattern ). Produce self
 * instance to build current skin configuration. At present, realised as lazy
 * creation factory. TODO - select point to initialize.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:59:43 $
 */
public abstract class SkinFactory {

    /**
     * Initialize skin factory. TODO - make call from init() method of any
     * servlet or custom faces element method ??? If exist resource
     * META-INF/services/org.richfaces.skin.SkinFactory , create
     * instance of class by name from first line of this file. If such class
     * have constructor with {@link SkinFactory} parameter, instantiate it with
     * instance of default factory ( as usual in JSF ). If any error occurs in
     * instantiate custom factory, return default.
     */
    @Deprecated
    public static final SkinFactory getInstance() {
        return getInstance(FacesContext.getCurrentInstance());
    }

    public static final SkinFactory getInstance(FacesContext context) {
        return ServiceTracker.getService(context, SkinFactory.class);
    }

    /**
     * Get default {@link Skin} implementation.
     *
     * @param context
     * @return
     */
    public abstract Skin getDefaultSkin(FacesContext context);

    /**
     * Get current {@link Skin} implementation.
     *
     * @param context
     * @return
     */
    public abstract Skin getSkin(FacesContext context);

    /**
     * Get base {@link Skin} implementation
     *
     * @param facesContext
     * @return
     */
    public abstract Skin getBaseSkin(FacesContext facesContext);

    /**
     * @param facesContext
     * @param name
     * @return
     */
    public abstract Theme getTheme(FacesContext facesContext, String name);

    public abstract Skin getSkin(FacesContext context, String name);

}
