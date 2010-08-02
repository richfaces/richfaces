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

package org.ajax4jsf.resource.cached;

/**
 * @author shura
 */
public class ResourceDataBean extends ResourceBean {

    /**
     *
     */
    private static final long serialVersionUID = -6486715556040103424L;
    private Object data;

    public ResourceDataBean(String key, Object data) {
        super(key);
        this.data = data;
    }

    /*
     *  (non-Javadoc)
     * @see com.exadel.vcp.resource.ResourceBean#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((null != obj) && (obj instanceof ResourceBean)) {
            ResourceBean bean = (ResourceBean) obj;

            return getKey().equals(bean.getKey()) && data.equals(bean.getData());
        }

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see com.exadel.vcp.resource.ResourceBean#getData()
     */
    public Object getData() {
        return data;
    }

    /*
     *  (non-Javadoc)
     * @see com.exadel.vcp.resource.ResourceBean#hashCode()
     */
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();

        result = result * prime + data.hashCode();

        return result;
    }
}
