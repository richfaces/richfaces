/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.faces.context.FacesContext;

import org.richfaces.resource.AbstractJava2DUserResource;
import org.richfaces.resource.DynamicUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 *
 */
@DynamicUserResource
public class AutocompleteGradient extends AbstractJava2DUserResource implements StateHolderResource {
    private static final Dimension DIMENSION = new Dimension(18, 8);
    private String topColorParam;
    private String bottomColorParam;
    private Integer topColor;
    private Integer bottomColor;

    public AutocompleteGradient() {
        super(DIMENSION);
    }

    @PostConstructResource
    public void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);

        topColor = skin.getColorParameter(context, topColorParam);
        bottomColor = skin.getColorParameter(context, bottomColorParam);
    }

    public void paint(Graphics2D graphics2d) {
        if (topColor == null || bottomColor == null) {
            return;
        }
        Dimension dimension = getDimension();

        GradientPaint paint = new GradientPaint(0, 0, new Color(topColor), 0, dimension.height, new Color(bottomColor));
        graphics2d.setPaint(paint);
        graphics2d.fill(new Rectangle(dimension));
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        if (topColor != null && bottomColor != null) {
            dataOutput.writeBoolean(true);
            dataOutput.writeInt(topColor);
            dataOutput.writeInt(bottomColor);
        } else {
            dataOutput.writeBoolean(false);
        }
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        if (dataInput.readBoolean()) {
            topColor = dataInput.readInt();
            bottomColor = dataInput.readInt();
        } else {
            topColor = null;
            bottomColor = null;
        }
    }

    public boolean isTransient() {
        return false;
    }

    @ResourceParameter(defaultValue = Skin.HEADER_GRADIENT_COLOR)
    public void setTopColorParam(String topColorSkinParameter) {
        this.topColorParam = topColorSkinParameter;
    }

    @ResourceParameter(defaultValue = Skin.HEADER_BACKGROUND_COLOR)
    public void setBottomColorParam(String bottomColorSkinParameter) {
        this.bottomColorParam = bottomColorSkinParameter;
    }
}
