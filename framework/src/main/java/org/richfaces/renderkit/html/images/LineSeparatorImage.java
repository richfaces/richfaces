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

package org.richfaces.renderkit.html.images;

import org.richfaces.resource.DynamicUserResource;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

@DynamicUserResource
public class LineSeparatorImage extends ToolbarSeparatorImage {
    public Dimension getDimension() {
        return calculateDimension();
    }

    public void paint(Graphics2D g2d) {
        Dimension dimensions = calculateDimension();
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(this.getHeaderBackgroundColor()));
        g2d.fillRect(-1, -1, dimensions.width + 2, dimensions.height + 2);
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.drawLine(1, -1, 1, dimensions.height + 2);
    }

    private Dimension calculateDimension() {
        int h = this.getSeparatorHeight();
        int w = 2;
        return new Dimension(w, h);
    }
}
