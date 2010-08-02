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



package org.ajax4jsf.component;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.ajax4jsf.event.AjaxSource;

/**
 * Extend Ajax-enabled region to support event listeners on Ajax requests.
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:39 $
 *
 */
public interface AjaxContainer extends AjaxContainerBase, AjaxSource {

    /**
     * Is ajax container must re-render all output from page or only it's children ?
     * @return true if all ajax-enabled to re-render araes on page must be checked for output.
     */
    public abstract boolean isRenderRegionOnly();

    public abstract void setRenderRegionOnly(boolean reRenderPage);

    /**
     * Encode AJAX response from this container ( call encode methods for selected components only )
     * @param context
     * @throws IOException
     */
    public void encodeAjax(FacesContext context) throws IOException;
}
