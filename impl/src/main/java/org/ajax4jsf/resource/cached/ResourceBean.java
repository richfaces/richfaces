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

import java.io.Serializable;

/**
 * @author shura
 */
public class ResourceBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2830008963777271324L;
    private String key;

    public ResourceBean(String key) {
        super();
        this.key = key;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    public Object getData() {
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((null != obj) && (obj instanceof ResourceBean)) {
            ResourceBean bean = (ResourceBean) obj;

            return key.equals(bean.getKey()) && (bean.getData() == null);
        }

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        // TODO Auto-generated method stub
        return key.hashCode();
    }
}
