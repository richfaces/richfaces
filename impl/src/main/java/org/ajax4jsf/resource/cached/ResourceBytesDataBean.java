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

import java.util.Arrays;

/**
 * @author shura
 */
public class ResourceBytesDataBean extends ResourceBean {

    /**
     *
     */
    private static final long serialVersionUID = -3012554202964229624L;
    private byte[] data;

    ResourceBytesDataBean(String key, byte[] data) {
        super(key);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    /*
     *  (non-Javadoc)
     * @see com.exadel.vcp.resource.ResourceBean#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((null != obj) && (obj instanceof ResourceBytesDataBean)) {
            ResourceBytesDataBean bean = (ResourceBytesDataBean) obj;
            byte[] beanData = (byte[]) bean.getData();

            return getKey().equals(bean.getKey()) && Arrays.equals(data, beanData);
        }

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see com.exadel.vcp.resource.ResourceBean#hashCode()
     */
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();

        result = prime * result + Arrays.hashCode(data);

        return result;
    }
}
