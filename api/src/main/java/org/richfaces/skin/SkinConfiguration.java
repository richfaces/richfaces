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

/**
 * Fine tuning parameters for components configuration.
 * For any skin, calculation of concrete component parameners done by special "configuration",
 * pointed as special skin parameter ( or by default configuration ).
 * Work like "transformation" of limited set Skin parameters to fine-grained individual component
 * parameters.
 * @author shura
 *
 */
public interface SkinConfiguration {

    /**
     * Get value for configuration parameter. If parameter set as EL-expression,
     * calculate it value.
     *
     * @param context -
     *            {@link FacesContext } for current request.
     * @param name
     *            name of paremeter.
     * @return value of parameter in config, or null
     */
    public Object getParameter(FacesContext context, String name);

    /**
     * Get value for skin parameter, for build extensible ( mandatory/fine tuning ) with three-stage checks :
     * <ul>
     * <li>first, check value for parameter with given name. If value not null, return it</li>
     * <li>second, got parameter value from skin by skinName key. If not null, return it.</li>
     * <li>if both above parameters is null, return default value.</li>
     * </ul>
     * @param context - Current JSF context.
     * @param name - name of parameter.
     * @param skinName - name of according ( default ) parameter i Skin for same purpose. may be <code>null</code>.
     * @param defaultValue - default parameter value.
     * @return - value of parameter.
     */
    public Object getParameter(FacesContext context, String name, String skinName, Object defaultValue);

    /**
     * Test for present parameter for given name.
     * @param name of parameter to test
     * @return true if parameter present in configuration.
     */
    public boolean containsParameter(String name);
}
