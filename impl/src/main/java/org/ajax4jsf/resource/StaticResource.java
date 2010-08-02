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

import javax.faces.context.FacesContext;
import java.io.InputStream;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: nick_belaevski $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/01/11 16:52:14 $
 */
public class StaticResource extends InternetResourceBase {
    private String path;

    // private byte[] resourceData = null;

    /**
     *
     */
    public StaticResource() {
        super();
    }

    public StaticResource(String path) {
        this.path = path;
        setCacheable(true);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.InternetResourceBase#getResourceAsStream(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent)
     */
    @Override
    public InputStream getResourceAsStream(ResourceContext context) {
        return context.getResourceAsStream(path);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.InternetResourceBase#isCacheable()
     */
    @Override
    public boolean isCacheable(ResourceContext resourceContext) {
        return true;
    }

    @Override
    public String getUri(FacesContext context, Object data) {

        // perform all encodings, suitable for JSF specification.
        String src = context.getApplication().getViewHandler().getResourceURL(context, path);

        return context.getExternalContext().encodeResourceURL(src);
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
