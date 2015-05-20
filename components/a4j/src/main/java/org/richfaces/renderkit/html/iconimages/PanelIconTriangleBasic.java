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
package org.richfaces.renderkit.html.iconimages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/**
 * @author Anton Belevich
 */
public abstract class PanelIconTriangleBasic extends PanelIconBasic {
    protected void paintImage(Graphics2D g2d, Color color1, Color color2, Color color3) {

        GeneralPath path = new GeneralPath();

        Dimension dimension = getDimension();
        g2d.scale(dimension.getWidth() / 128, dimension.getWidth() / 128);

        draw(path, g2d);

        g2d.setColor(color1);
        g2d.fill(path);

        g2d.setColor(color2);
        g2d.translate(0, 128);
        g2d.fill(path);

        g2d.setColor(color3);
        g2d.translate(0, 128);
        g2d.fill(path);
    }

    abstract void draw(GeneralPath path, Graphics2D g2d);
}
