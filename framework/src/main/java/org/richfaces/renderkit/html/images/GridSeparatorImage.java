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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

@DynamicUserResource
public class GridSeparatorImage extends ToolbarSeparatorImage {
    public Dimension getDimension() {
        return calculateDimension();
    }

    public void paint(Graphics2D g2d) {
        Dimension dimensions = calculateDimension();

        BufferedImage texture = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        Graphics2D txG2d = texture.createGraphics();
        txG2d.setColor(new Color(this.getHeaderBackgroundColor()));
        txG2d.fillRect(0, 0, 2, 2);
        txG2d.setColor(new Color(255, 255, 255, 150));
        txG2d.fillRect(0, 0, 1, 1);
        txG2d.dispose();
        g2d.setPaint(new TexturePaint(texture, new Rectangle(1, 1, 3, 3)));
        g2d.fillRect(0, 0, dimensions.width, dimensions.height);
    }

    private Dimension calculateDimension() {
        int h = (int) (this.getSeparatorHeight() * 0.8);
        h = h - h % 3;
        int w = 9;
        return new Dimension(w, h);
    }
}