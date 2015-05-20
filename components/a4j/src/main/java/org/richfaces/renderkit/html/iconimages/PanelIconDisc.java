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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import org.richfaces.resource.DynamicUserResource;

@DynamicUserResource
public class PanelIconDisc extends PanelIconBasic {
    @Override
    protected void paintImage(Graphics2D g2d, Color color1, Color color2, Color color3) {

        Dimension dimension = getDimension();
        long dim = Math.round(dimension.getWidth() / 3);

        g2d.setColor(color1);
        g2d.translate(dim, dim);

        g2d.fill(new Ellipse2D.Double(0, 0, dim, dim));

        g2d.setColor(color2);
        g2d.translate(0, 16);

        g2d.fill(new Ellipse2D.Double(0, 0, dim, dim));

        g2d.setColor(color3);
        g2d.translate(0, 16);

        g2d.fill(new Ellipse2D.Double(0, 0, dim, dim));
    }
}
