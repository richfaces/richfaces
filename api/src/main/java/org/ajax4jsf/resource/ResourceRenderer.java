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



package org.ajax4jsf.resource;

import java.io.IOException;

import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * "Brige" for concrete resource types - images, scripts, styles.
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:07 $
 *
 */
public interface ResourceRenderer {

    /**
     * @return content type ( image/jpeg , text/javascript etc. ) for given type.
     */
    String getContentType();

    /**
     * Encode concrete HTML element for resource.
     * @param context
     * @param data
     * @throws IOException
     */
    void encode(InternetResource resource, FacesContext context, Object data) throws IOException;

    /**
     * Encode concrete HTML element for resource.
     * @param context
     * @param data
     * @throws IOException
     */
    void encode(InternetResource resource, FacesContext context, Object data, Map<String, Object> attributes)
        throws IOException;

    void encodeBegin(InternetResource base, FacesContext context, Object data, Map<String, Object> attributes)
        throws IOException;

    void encodeEnd(InternetResource base, FacesContext context, Object data) throws IOException;

    /**
     * @return true, if resource must be rendered in faces request cicle.
     */
    public boolean requireFacesContext();

    /**
     * Send incapsulated resource to client by {@link ResourceContext} .
     * Perform any conversions, nessesary for this resources ( template interpretations, JavaScript conversions etc )
     * @param context
     * @return TODO
     */
    public int send(InternetResource base, ResourceContext context) throws IOException;

    /**
     * Get data to build URI for given resource. Can be any Serialisable object,
     * @param base TODO
     * @return data for Store in URI/ Cache key.
     */
    public Object getData(InternetResource base, FacesContext context, Object data);
}
