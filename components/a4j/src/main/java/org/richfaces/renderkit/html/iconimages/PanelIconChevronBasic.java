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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * @author Alex.Kolonitsky
 */
public abstract class PanelIconChevronBasic extends PanelIconBasic {
    private static final int BUFFER_IMAGE_SIZE_WIDTH = 128;
    private static final int BUFFER_IMAGE_SIZE_HEIGHT = 384;

    @Override
    protected void paintImage(Graphics2D graphics2d, Color color1, Color color2, Color color3) {

        BufferedImage bufferedImage = new BufferedImage(BUFFER_IMAGE_SIZE_WIDTH, BUFFER_IMAGE_SIZE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        Dimension dimension = getDimension();
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g2d.translate(28, 28);
        g2d.setColor(color1);

        GeneralPath path = new GeneralPath();
        draw(path);

        g2d.fill(path);
        if (this instanceof PanelIconChevron || this instanceof PanelIconChevronLeft) {
            g2d.translate(24, 0);
        } else {
            g2d.translate(0, 24);
        }
        g2d.fill(path);

        g2d.setColor(color2);
        g2d.translate(0, 104);
        if (this instanceof PanelIconChevron || this instanceof PanelIconChevronLeft) {
            g2d.translate(-24, 24);
        }

        g2d.fill(path);
        if (this instanceof PanelIconChevron || this instanceof PanelIconChevronLeft) {
            g2d.translate(24, 0);
        } else {
            g2d.translate(0, 24);
        }
        g2d.fill(path);

        g2d.setColor(color3);
        g2d.translate(0, 104);
        if (this instanceof PanelIconChevron || this instanceof PanelIconChevronLeft) {
            g2d.translate(-24, 24);
        }

        g2d.fill(path);
        if (this instanceof PanelIconChevron || this instanceof PanelIconChevronLeft) {
            g2d.translate(24, 0);
        } else {
            g2d.translate(0, 24);
        }
        g2d.fill(path);

        AffineTransform transform = AffineTransform.getScaleInstance(dimension.getWidth() / BUFFER_IMAGE_SIZE_WIDTH,
            dimension.getHeight() / BUFFER_IMAGE_SIZE_HEIGHT);
        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        graphics2d.drawImage(bufferedImage, transformOp, 0, 0);
        g2d.dispose();
    }

    abstract void draw(GeneralPath draw);
}
