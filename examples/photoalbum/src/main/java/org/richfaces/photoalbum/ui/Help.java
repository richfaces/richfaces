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
package org.richfaces.photoalbum.ui;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.richfaces.photoalbum.util.Environment;

/**
 * Convenience UI class for application help system
 *
 * @author Andrey Markhel
 */

@Named
@RequestScoped
public class Help {

    private String page = "/includes/help/stuff.xhtml";

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Convenience method to show specified page with help info in modal panel
     *
     * @param src - page to show
     */
    public void navigateTo(String src) {
        this.setPage(src);
    }

    /**
     * Convenience method to determine is there need to render application help system.
     *
     * @param src - page to show
     */
    public boolean isShowHelp() {
        return Environment.isShowHelp();
    }
}
