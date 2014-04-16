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

package org.richfaces.demo.images;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.richfaces.renderkit.html.BaseGradient;

public class PageFooterGradient extends BaseGradient {
    @Override
    protected void paintGradient(Graphics2D g2d, Dimension dim) {
        Dimension halfHeightDim = new Dimension();
        halfHeightDim.setSize(dim.getWidth(), dim.getHeight() / 2);
        super.paintGradient(g2d, halfHeightDim);
        AffineTransform transform = new AffineTransform(1, 0, 0, -1, 0, getSafeHeight());
        g2d.transform(transform);
        super.paintGradient(g2d, halfHeightDim);
    }
}
