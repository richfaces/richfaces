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
package org.richfaces.renderkit.html.images.iconimages;

import org.richfaces.resource.DynamicUserResource;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

@DynamicUserResource
public class PanelIconGrid extends PanelIconBasic {
    @Override
    protected void paintImage(Graphics2D g2d, Color color) {

        Rectangle2D.Float path = new Rectangle2D.Float();
        Dimension dimension = getDimension();

        g2d.setStroke(new BasicStroke(16, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
        g2d.scale(dimension.getHeight() / 128, dimension.getHeight() / 128);
        g2d.translate(40, 40);

        path.setRect(0, 0, 40, 40);

        g2d.setColor(color);

        Color bcolor = new Color(1f, 1f, 1f, 0f);

        g2d.setBackground(bcolor);
        g2d.fill(path);
        g2d.clearRect(16, 0, 8, 40);
        g2d.clearRect(0, 16, 40, 8);
    }
}
