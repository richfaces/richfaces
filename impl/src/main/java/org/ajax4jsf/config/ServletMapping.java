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

package org.ajax4jsf.config;

class ServletMapping {
    private final String servletName;
    private final String urlPattern;

    /**
     * @param servletName
     * @param urlPattern
     */
    public ServletMapping(String servletName, String urlPattern) {
        super();
        this.servletName = servletName;
        this.urlPattern = urlPattern;
    }

    public String getServletName() {
        return servletName;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((servletName == null) ? 0 : servletName.hashCode());

        return result;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        ServletMapping other = (ServletMapping) obj;

        if (servletName == null) {
            if (other.servletName != null) {
                return false;
            }
        } else if (!servletName.equals(other.servletName)) {
            return false;
        }

        return true;
    }
}
