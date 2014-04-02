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
import org.richfaces.resource.ImageType;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

import javax.faces.context.FacesContext;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@DynamicUserResource
public class InputNumberSliderBarHandleBackgroundImage extends AbstractJava2DUserResource implements StateHolderResource {

    private static final Dimension DIMENSION = new Dimension(24, 48);
    private Integer basicColor;
    private String colorParam;

    public InputNumberSliderBarHandleBackgroundImage() {
        super(ImageType.GIF, DIMENSION);
    }

    @PostConstructResource
    public void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);
        basicColor = skin.getColorParameter(context, colorParam);
        if (basicColor == null) {
            basicColor = defaultSkin.getColorParameter(context, colorParam);
        }
    }

    /**
     * Creates a main stage for progress bar background.
     *
     * @param context resource context
     * @return a <code>BufferedImage</code> object
     */
    private BufferedImage createMainStage() {
        Color progressbarBackgroundColor = new Color(basicColor);
        Color progressbarSpiralColor = ColorUtils.adjustLightness(progressbarBackgroundColor, 0.2f);

        Dimension dimension = getDimension();
        BufferedImage retVal = getImageType().createImage(dimension.width, dimension.height * 2);
        Graphics g = retVal.getGraphics();
        try {
            g.setColor(progressbarBackgroundColor);
            g.fillRect(0, 0, dimension.width, dimension.height * 2);
            g.setColor(progressbarSpiralColor);
            for (int k : new int[] { -24, 0, 24, 48, 72 }) {
                g.fillPolygon(new int[] { 0, 24, 24, 0 }, new int[] { 24 + k, k, 12 + k, 36 + k }, 4);
            }
        } finally {
            if (g != null) {
                g.dispose();
            }
        }

        return retVal;
    }

    public void paint(Graphics2D g2d) {
        Dimension dimension = getDimension();

        BufferedImage mainStage = createMainStage();
        g2d.drawImage(mainStage, null, null);
        Color progressbarBackgroundColor = new Color(basicColor);
        Color progressbarShadowStartColor = ColorUtils.overwriteAlpha(
                ColorUtils.adjustLightness(progressbarBackgroundColor, 0.7f), 0.6f);
        Color progressbarShadowEndColor = ColorUtils.overwriteAlpha(
                ColorUtils.adjustLightness(progressbarBackgroundColor, 0.3f), 0.6f);
        // paint a shadow in the form of semi-transparent gradient
        g2d.setPaint(new GradientPaint(0, 0, progressbarShadowStartColor, 0, 7, progressbarShadowEndColor));
        g2d.fillRect(0, 0, dimension.width, 7);
    }

    public boolean isTransient() {
        return false;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(basicColor);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        basicColor = dataInput.readInt();
    }

    @ResourceParameter(defaultValue = Skin.SELECT_CONTROL_COLOR)
    public void setColorParam(String colorParam) {
        this.colorParam = colorParam;
    }
}
