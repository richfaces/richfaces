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

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 *         created 20.11.2006
 */
public class MimeRenderer extends BaseResourceRenderer {
    private String contentType = null;

    public MimeRenderer() {
    }

    public MimeRenderer(String contentType) {
        super();
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    protected String getTag() {
        return null;
    }

    @Override
    protected String[][] getCommonAttrs() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getHrefAttr() {

        // TODO Auto-generated method stub
        return null;
    }
}
