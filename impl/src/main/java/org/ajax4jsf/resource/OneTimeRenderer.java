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

import org.ajax4jsf.Messages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Base class for resources, rendered only one time in page ( JavaScript, CSS )
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:02 $
 */
public abstract class OneTimeRenderer extends BaseResourceRenderer {
    private static final Logger LOG = RichfacesLogger.RESOURCE.getLogger();

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#encodeBegin(org.ajax4jsf.resource.InternetResource,
     * javax.faces.context.FacesContext, java.lang.Object, java.util.Map)
     */
    @Override
    public void encodeBegin(InternetResource resource, FacesContext context, Object data,
                            Map<String, Object> attributes) throws IOException {

        if (!isRendered(resource, context)) {
            super.encodeBegin(resource, context, data, attributes);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.SKIP_ENCODE_BEGIN_HTML_INFO_2, resource.getKey()));
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.BaseResourceRenderer#encodeEnd(org.ajax4jsf.resource.InternetResource,
     * javax.faces.context.FacesContext, java.lang.Object)
     *
     */
    @Override
    public void encodeEnd(InternetResource resource, FacesContext context, Object data) throws IOException {

        // TODO Auto-generated method stub
        if (!isRendered(resource, context)) {
            super.encodeEnd(resource, context, data);
            context.getExternalContext().getRequestMap().put(resource.getKey(), Boolean.TRUE);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getMessage(Messages.MARK_AS_RENDERED_INFO, resource.getKey()));
            }
        } else if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.SKIP_ENCODE_END_HTML_INFO_2, resource.getKey()));
        }
    }

    /**
     * Detect for resource rendered status. For cacheable, show key parameter in request Map.
     *
     * @param resource
     * @param context
     * @return - true, if resource already rendered.
     */
    private boolean isRendered(InternetResource resource, FacesContext context) {
        return null != context.getExternalContext().getRequestMap().get(resource.getKey()); // resource.isCacheable() &&
    }
}
