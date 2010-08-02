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

import org.ajax4jsf.resource.util.URLToStreamHelper;

import java.io.InputStream;

/**
 * Class stoped all resources in application classpath.
 * TODO - set last midified & expired values.
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:00 $
 */
public class JarResource extends InternetResourceBase {

    /**
     * Default place for all static resources in Jar.
     * to avoid access to any class in application, only allowed subpackages for
     * META-INF/resources
     */
    public static final String RESOURCES_ROOT = "META-INF/resources";
    private String path;

    public JarResource() {
    }

    public JarResource(String path) {
        this.path = path;
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.resource.InternetResourceBase#getResourceAsStream(javax.faces.context.FacesContext,
     * java.lang.Object)
     */
    public InputStream getResourceAsStream(ResourceContext context) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        return URLToStreamHelper.urlToStreamSafe(loader.getResource(path));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
