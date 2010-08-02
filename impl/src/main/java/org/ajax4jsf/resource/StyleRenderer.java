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

/**
 * Render style element with content in page. Warning - not use for head link element,
 * it must be rendered separate.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:56:58 $
 */
public class StyleRenderer extends OneTimeRenderer {

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#getTag()
     */
    protected String getTag() {
        return "link";
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#getHrefAttr()
     */
    protected String getHrefAttr() {
        return "href";
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#getCommonAttrs()
     */
    protected String[][] getCommonAttrs() {
        return new String[][]{
            {"type", "text/css"}, {"rel", "stylesheet"}
        };
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.ResourceRenderer#getContentType()
     */
    public String getContentType() {

        // TODO use configurable encoding ?
        return "text/css";
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#customEncode(org.ajax4jsf.resource.InternetResource, javax.faces.context.FacesContext, java.lang.Object)
     */

    /*
     * protected void customEncode(InternetResource resource, FacesContext context, Object data) throws IOException {
     *   // Encode style in page - read from resource.
     *   ResourceContext resourceContext = new FacesResourceContext(context);
     *   InputStream in = resource.getResourceAsStream(resourceContext);
     *   StringBuffer buff = new StringBuffer();
     *   int input;
     *   while((input = in.read())>0){
     *       buff.append((char)input);
     *   }
     *   in.close();
     *   //  MyFaces & RI have different beahvior for style element, in RI best use writeComment ?
     *   // TODO - detect page content type ?
     *   context.getResponseWriter().writeText(buff,resource.getKey());
     * }
     */
}
