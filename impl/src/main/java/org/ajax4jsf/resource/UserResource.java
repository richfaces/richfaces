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

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shura
 */
public class UserResource extends InternetResourceBase {
    private String contentType;

    /**
     *
     */
    public UserResource(boolean cacheable, boolean session, String mime) {
        super();
        setCacheable(cacheable);
        setSessionAware(session);
        setContentType(mime);
    }

    /**
     * @return Returns the contentType.
     */
    public String getContentType(ResourceContext resourceContext) {
        return contentType;
    }

    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.InternetResourceBase#getDataToStore(javax.faces.context.FacesContext, java.lang.Object)
     */
    public Object getDataToStore(FacesContext context, Object data) {
        UriData dataToStore = null;

        if (data instanceof ResourceComponent2) {
            ResourceComponent2 resource = (ResourceComponent2) data;

            dataToStore = new UriData();
            dataToStore.value = resource.getValue();
            dataToStore.createContent = UIComponentBase.saveAttachedState(context,
                resource.getCreateContentExpression());

            if (data instanceof UIComponent) {
                UIComponent component = (UIComponent) data;
                ValueExpression expires = component.getValueExpression("expires");

                if (null != expires) {
                    dataToStore.expires = UIComponentBase.saveAttachedState(context, expires);
                }

                ValueExpression lastModified = component.getValueExpression("lastModified");

                if (null != lastModified) {
                    dataToStore.modified = UIComponentBase.saveAttachedState(context, lastModified);
                }
            }
        }

        return dataToStore;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.InternetResourceBase#send(org.ajax4jsf.resource.ResourceContext)
     */
    public void send(ResourceContext context) throws IOException {
        UriData data = (UriData) restoreData(context);
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if ((null != data) && (null != facesContext)) {

            // Send headers
            ELContext elContext = facesContext.getELContext();

//          if(data.expires != null){
//              ValueExpression binding = (ValueExpression) UIComponentBase.restoreAttachedState(facesContext,data.expires);
//              Date expires = (Date) binding.getValue(elContext);
//              context.setDateHeader("Expires",expires.getTime());
//          }
//          if(data.modified != null){
//              ValueExpression binding = (ValueExpression) UIComponentBase.restoreAttachedState(facesContext,data.modified);
//              Date modified = (Date) binding.getValue(elContext);
//              context.setDateHeader("Last-Modified",modified.getTime());
//          }
            // Send content
            OutputStream out = context.getOutputStream();
            MethodExpression send = (MethodExpression) UIComponentBase.restoreAttachedState(facesContext,
                data.createContent);

            send.invoke(elContext, new Object[]{out, data.value});
        }
    }

    @Override
    public Date getLastModified(ResourceContext resourceContext) {
        UriData data = (UriData) restoreData(resourceContext);
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if ((null != data) && (null != facesContext)) {

            // Send headers
            ELContext elContext = facesContext.getELContext();

            if (data.modified != null) {
                ValueExpression binding = (ValueExpression) UIComponentBase.restoreAttachedState(facesContext,
                    data.modified);
                Date modified = (Date) binding.getValue(elContext);

                if (null != modified) {
                    return modified;
                }
            }
        }

        return super.getLastModified(resourceContext);
    }

    @Override
    public long getExpired(ResourceContext resourceContext) {
        UriData data = (UriData) restoreData(resourceContext);
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if ((null != data) && (null != facesContext)) {

            // Send headers
            ELContext elContext = facesContext.getELContext();

            if (data.expires != null) {
                ValueExpression binding = (ValueExpression) UIComponentBase.restoreAttachedState(facesContext,
                    data.expires);
                Date expires = (Date) binding.getValue(elContext);

                if (null != expires) {
                    return expires.getTime() - System.currentTimeMillis();
                }
            }
        }

        return super.getExpired(resourceContext);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.InternetResourceBase#requireFacesContext()
     */
    public boolean requireFacesContext() {

        // TODO Auto-generated method stub
        return true;
    }

    public static class UriData implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1258987L;
        private Object createContent;
        private Object expires;
        private Object modified;
        private Object value;

        public Object getCreateContent() {
            return createContent;
        }

        public void setCreateContent(Object createContent) {
            this.createContent = createContent;
        }

        public Object getExpires() {
            return expires;
        }

        public void setExpires(Object expires) {
            this.expires = expires;
        }

        public Object getModified() {
            return modified;
        }

        public void setModified(Object modified) {
            this.modified = modified;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
