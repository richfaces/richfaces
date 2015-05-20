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
package org.richfaces.renderkit.html.iconimages;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.richfaces.resource.DynamicUserResource;

@DynamicUserResource
public class PanelIconGrid extends PanelIconBasic {
    @Override
    protected void paintImage(Graphics2D g2d, Color color1, Color color2, Color color3) {

        Rectangle2D.Float path = new Rectangle2D.Float();
        Dimension dimension = getDimension();

        g2d.setStroke(new BasicStroke(16, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
        g2d.scale(dimension.getWidth() / 128, dimension.getWidth() / 128);
        g2d.translate(40, 40);

        path.setRect(0, 0, 40, 40);

        Color bcolor = new Color(1f, 1f, 1f, 0f);
        g2d.setBackground(bcolor);

        g2d.setColor(color1);
        g2d.fill(path);
        g2d.clearRect(16, 0, 8, 40);
        g2d.clearRect(0, 16, 40, 8);

        g2d.setColor(color2);
        g2d.translate(0, 128);
        g2d.fill(path);
        g2d.clearRect(16, 0, 8, 40);
        g2d.clearRect(0, 16, 40, 8);

        g2d.setColor(color3);
        g2d.translate(0, 128);
        g2d.fill(path);
        g2d.clearRect(16, 0, 8, 40);
        g2d.clearRect(0, 16, 40, 8);
    }
}
