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
package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.Java2Dresource;
import org.ajax4jsf.util.HtmlColor;
import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;

/**
 * implementation of the default CANCEL icon renderer
 *
 * @author Anton Belevich
 * @since 3.2.0
 */
@DynamicResource
public class CancelControlIcon extends Java2Dresource {

    protected static final String ALTERNATE_COLOR = "#ED6161";

    private static final Dimension DIMENSIONS = new Dimension(11, 11);

    protected Integer iconColor;
    protected Integer iconBorderColor;

    public CancelControlIcon() {
        super(ImageType.GIF);
    }

    @Override
    public Dimension getDimension() {
        return DIMENSIONS;
    }

    @Override
    public void readState(FacesContext context, DataInput stream) throws IOException {
        super.readState(context, stream);

        this.iconColor = stream.readInt();
        this.iconBorderColor = stream.readInt();
    }

    public void writeState(FacesContext context, DataOutput stream) throws IOException {
        super.writeState(context, stream);

        stream.writeInt(this.iconColor);
        stream.writeInt(this.iconBorderColor);
    }

    @Override
    protected void paint(Graphics2D g2d, Dimension dimension) {

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        Color iconColour = new Color(iconColor);
        Color iconBorder = new Color(iconBorderColor);
        g2d.setColor(iconColour);

        Color altenateColor = HtmlColor.decode(ALTERNATE_COLOR);
        GradientPaint gradient = new GradientPaint(2, 3, altenateColor, 3, 9, iconColour);
        g2d.setPaint(gradient);

        // draw cross
        g2d.drawLine(2, 3, 7, 8);
        g2d.drawLine(3, 3, 7, 7);
        g2d.drawLine(3, 2, 8, 7);

        g2d.drawLine(2, 7, 7, 2);
        g2d.drawLine(3, 7, 7, 3);
        g2d.drawLine(3, 8, 8, 3);

        //draw border
        g2d.setColor(iconBorder);
        g2d.drawLine(1, 3, 3, 5);
        g2d.drawLine(3, 5, 1, 7);
        g2d.drawLine(1, 7, 3, 9);
        g2d.drawLine(3, 9, 5, 7);
        g2d.drawLine(5, 7, 7, 9);
        g2d.drawLine(7, 9, 9, 7);
        g2d.drawLine(9, 7, 7, 5);
        g2d.drawLine(7, 5, 9, 3);
        g2d.drawLine(9, 3, 7, 1);
        g2d.drawLine(7, 1, 5, 3);
        g2d.drawLine(5, 3, 3, 1);
        g2d.drawLine(3, 1, 1, 3);


    }
}

