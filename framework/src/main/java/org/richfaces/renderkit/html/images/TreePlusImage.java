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

import org.richfaces.resource.AbstractJava2DUserResource;
import org.richfaces.resource.DynamicUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

import javax.faces.context.FacesContext;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Nick Belaevski
 *
 */
@DynamicUserResource
public class TreePlusImage extends AbstractJava2DUserResource implements StateHolderResource {
    private static final Dimension DIMENSION = new Dimension(16, 16);
    protected Integer generalColorValue;
    protected Integer controlColorValue;
    protected Integer trimColorValue;

    public TreePlusImage() {
        super(DIMENSION);
    }

    @PostConstructResource
    public void init() {
        generalColorValue = getColorParameter(Skin.GENERAL_TEXT_COLOR);
        controlColorValue = getColorParameter(Skin.CONTROL_BACKGROUND_COLOR);
        trimColorValue = getColorParameter(Skin.TRIM_COLOR);
    }

    protected Integer getColorParameter(String property) {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);

        Integer value = skin.getColorParameter(context, property);
        if (value == null) {
            value = defaultSkin.getColorParameter(context, property);
        }
        return value;
    }

    protected void drawFrame(Graphics2D g2d) {
        Color trimColor = new Color(trimColorValue);
        Color controlColor = new Color(controlColorValue);

        g2d.setColor(trimColor);
        g2d.drawRect(3, 3, 8, 8);

        Rectangle2D rect = new Rectangle2D.Float(4, 4, 7, 7);
        GradientPaint gragient = new GradientPaint(4, 4, controlColor, 13, 13, trimColor);
        g2d.setPaint(gragient);
        g2d.fill(rect);
    }

    public void paint(Graphics2D g2d) {
        drawFrame(g2d);

        drawHorizontalCrossLine(g2d);
        drawVerticalCrossLine(g2d);
    }

    protected void drawVerticalCrossLine(Graphics2D g2d) {
        g2d.setColor(new Color(generalColorValue));
        // vertical cross line
        g2d.drawLine(7, 5, 7, 9);
    }

    protected void drawHorizontalCrossLine(Graphics2D g2d) {
        g2d.setColor(new Color(generalColorValue));
        // horizontal cross line
        g2d.drawLine(5, 7, 9, 7);
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(generalColorValue);
        dataOutput.writeInt(controlColorValue);
        dataOutput.writeInt(trimColorValue);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        generalColorValue = dataInput.readInt();
        controlColorValue = dataInput.readInt();
        trimColorValue = dataInput.readInt();
    }

    public boolean isTransient() {
        return false;
    }
}
