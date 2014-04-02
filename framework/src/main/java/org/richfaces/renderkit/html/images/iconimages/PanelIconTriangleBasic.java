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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/**
 * @author Anton Belevich
 */
public abstract class PanelIconTriangleBasic extends PanelIconBasic {
    protected void paintImage(Graphics2D g2d, Color color) {

        GeneralPath path = new GeneralPath();

        Dimension dimension = getDimension();
        g2d.scale(dimension.getHeight() / 128, dimension.getHeight() / 128);

        draw(path, g2d);

        g2d.setColor(color);
        g2d.fill(path);
    }

    abstract void draw(GeneralPath path, Graphics2D g2d);
}
