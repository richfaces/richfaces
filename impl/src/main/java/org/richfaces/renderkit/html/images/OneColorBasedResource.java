/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;

import org.ajax4jsf.resource.Java2Dresource;
import org.ajax4jsf.resource.ResourceContext;
import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;

@DynamicResource
public abstract class OneColorBasedResource extends Java2Dresource {

    private Dimension dimension;

    private String basicColorParamName;

    private Color basicColor;

    public OneColorBasedResource(int width, int height, final String basicColorParamName) {
        super(ImageType.GIF);
        this.basicColorParamName = basicColorParamName;
        this.dimension = new Dimension(width, height);
        
    }

    /**
     * @see Java2Dresource#isCacheable(ResourceContext)
     */
    public boolean isCacheable(ResourceContext ctx) {
        return true;
    }

    /**
     * Gets value of basicColor field.
     * @return value of basicColor field
     */
    public Color getBasicColor() {
        return basicColor;
    }
}
