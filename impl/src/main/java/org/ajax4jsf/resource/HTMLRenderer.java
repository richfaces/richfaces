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
import java.io.IOException;
import java.io.InputStream;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:03 $
 */
public class HTMLRenderer extends BaseResourceRenderer {

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#getTag()
     */
    @Override
    protected String getTag() {

        // TODO Auto-generated method stub
        return "span";
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#getHrefAttr()
     */
    @Override
    protected String getHrefAttr() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#getCommonAttrs()
     */
    @Override
    protected String[][] getCommonAttrs() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceRenderer#getContentType()
     */
    public String getContentType() {

        // TODO Auto-generated method stub
        return "text/html";
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#customEncode(org.ajax4jsf.resource.InternetResource,
     * javax.faces.context.FacesContext, java.lang.Object)
     */
    @Override
    protected void customEncode(InternetResource resource, FacesContext context, Object data) throws IOException {

        // Encode style in page - read from resource.
        ResourceContext resourceContext = new FacesResourceContext(context);
        InputStream in = resource.getResourceAsStream(resourceContext);
        StringBuffer buff = new StringBuffer();
        int input;

        while ((input = in.read()) > 0) {
            buff.append((char) input);
        }

        in.close();

        // MyFaces & RI have different beahvior for style element, in RI best use writeComment ?
        // TODO - detect page content type ?
        context.getResponseWriter().writeText(buff, resource.getKey());
    }
}
