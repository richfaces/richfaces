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

import org.ajax4jsf.resource.ResourceContext;

import java.awt.*;

public class InputErrorIcon extends OneColorBasedResource {

    public InputErrorIcon() {
        super(6, 11, "warningColor");
    }

    /**
     * @see org.ajax4jsf.resource.Java2Dresource#paint(ResourceContext, Graphics2D)
     */
    protected void paint(ResourceContext context, Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBasicColor());
        g2d.fillRect(3, 2, 2, 6);
        g2d.fillRect(3, 9, 2, 2);
    }

    @Override
    public Dimension getDimension() {
        // TODO Auto-generated method stub
        return null;
    }

}
