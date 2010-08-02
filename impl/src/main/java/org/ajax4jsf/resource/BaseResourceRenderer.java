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

import org.ajax4jsf.Messages;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:07 $
 */
public abstract class BaseResourceRenderer implements ResourceRenderer {
    public void encode(InternetResource resource, FacesContext context, Object data) throws IOException {
        Map<String, Object> attributes = Collections.emptyMap();

        this.encodeBegin(resource, context, data, attributes);
        this.encodeEnd(resource, context, data);
    }

    public void encode(InternetResource resource, FacesContext context, Object data, Map<String, Object> attributes)
        throws IOException {

        this.encodeBegin(resource, context, data, attributes);
        this.encodeEnd(resource, context, data);
    }

    public void encodeBegin(InternetResource resource, FacesContext context, Object data,
                            Map<String, Object> attributes) throws IOException {

        if (null != getTag()) {
            ResponseWriter writer = context.getResponseWriter();

            writer.startElement(getTag(), null);

            String[][] attrs = getCommonAttrs();

            if (null != getHrefAttr()) {
                writer.writeAttribute(getHrefAttr(), resource.getUri(context, data), null);
            }

            if (null != attrs) {
                for (int i = 0; i < attrs.length; i++) {
                    writer.writeAttribute(attrs[i][0], attrs[i][1], null);
                }
            }

            for (Iterator<Entry<String, Object>> iter = attributes.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, Object> attr = iter.next();

                writer.writeAttribute(attr.getKey().toString(), attr.getValue(), null);
            }
        }

        this.customEncode(resource, context, data);
    }

    public void encodeEnd(InternetResource resource, FacesContext context, Object data) throws IOException {
        if (null != getTag()) {
            ResponseWriter writer = context.getResponseWriter();

            writer.endElement(getTag());
        }
    }

    /**
     * Template method for customaize encoding for component. can insert size
     * etc. attributes, child tags etc.
     *
     * @param resource
     * @param context
     * @param component
     * @throws IOException
     */
    protected void customEncode(InternetResource resource, FacesContext context, Object data) throws IOException {

        // TODO if concrete renderer need
    }

    protected abstract String getTag();

    protected abstract String getHrefAttr();

    protected abstract String[][] getCommonAttrs();

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceRenderer#send(org.ajax4jsf.resource.InternetResource,
     *      org.ajax4jsf.resource.ResourceContext)
     */
    public int send(InternetResource base, ResourceContext context) throws IOException {
        InputStream in = base.getResourceAsStream(context);
        OutputStream out = context.getOutputStream();

        if (null == in) {
            String message = Messages.getMessage(Messages.NO_INPUT_STREAM_ERROR, base.getKey());

            throw new IOException(message);
        }

        int total = sendStream(in, out);

        return total;
    }

    /**
     * @param in
     * @param out
     * @return
     * @throws IOException
     */
    protected int sendStream(InputStream in, OutputStream out) throws IOException {
        int total = 0;
        byte[] buffer = new byte[InternetResourceBase.BUFFER_SIZE];
        int length;

        try {
            for (length = in.read(buffer); length > 0; length = in.read(buffer)) {
                out.write(buffer, 0, length);
                total += length;
            }
        } finally {
            in.close();
            out.flush();
            out.close();
        }

        return total;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.resource.ResourceRenderer#requireFacesContext()
     */
    public boolean requireFacesContext() {

        // TODO Auto-generated method stub
        return false;
    }

    public Object getData(InternetResource base, FacesContext context, Object data) {

        // By default, resource dot't provide any data. Can be used in
        // Template-based or other
        // configurable resources ( like weblets configuration ).
        return null;
    }
}
