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

package org.richfaces.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.richfaces.application.ServiceTracker;
import org.richfaces.component.PushEventTracker;
import org.richfaces.component.PushListenersManager;

/**
 * @author Nick Belaevski
 * @since 4.0
 */

//TODO make this a singleton
@DynamicResource
public class PushResource extends Resource {

    public PushResource() {
        setResourceName(getClass().getName());
    }
    
    public InputStream getInputStream() throws IOException {
        return null;
    }

    public Map<String, String> getResponseHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String pushId = externalContext.getRequestParameterMap().get("id");

        if (pushId != null && pushId.length() != 0) {
            PushListenersManager manager = PushListenersManager.getInstance(facesContext);
            PushEventTracker eventTracker = manager.getListener(pushId);

            if (eventTracker != null) {
                if (eventTracker.pollStatus()) {
                    headers.put("Ajax-Push-Status", "READY");
                }
            }
        }

        return headers;
    }

    public String getContentType() {
        return null;
    }

    @Override
    public String getRequestPath() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceCodec resourceCodec = ServiceTracker.getService(ResourceCodec.class);
        
        String requestPath = resourceCodec.encodeResourceRequestPath(facesContext, null, getResourceName(), null, null);
        return resourceCodec.encodeJSFMapping(facesContext, requestPath);
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return true;
    }
}
