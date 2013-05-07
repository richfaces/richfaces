/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 *
 */
package org.richfaces.example;

import javax.faces.context.FacesContext;

/**
 * @author leo
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class PageDescriptionBean implements Comparable<PageDescriptionBean> {
    private final String _path;
    private final String _title;

    public PageDescriptionBean(String _path, String _title) {
        this._path = _path;
        this._title = _title;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return _path;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return _title;
    }

    public String navigate() {
        return getPath();
    }

    public String getUrl() {
        FacesContext context = FacesContext.getCurrentInstance();
        String actionURL = context.getApplication().getViewHandler().getActionURL(context, getPath());
        return context.getExternalContext().encodeActionURL(actionURL);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(PageDescriptionBean o) {
        // compare paths
        return getPath().compareToIgnoreCase(o.getPath());
    }
}
