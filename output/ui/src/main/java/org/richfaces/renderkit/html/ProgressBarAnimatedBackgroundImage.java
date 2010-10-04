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
package org.richfaces.renderkit.html;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.renderkit.util.ColorUtils;
import org.richfaces.resource.CacheableResource;
import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DAnimatedUserResource;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 * 
 */
//TODO - add version
@DynamicResource
public class ProgressBarAnimatedBackgroundImage implements Java2DAnimatedUserResource, StateHolderResource, CacheableResource {

    private static final int NUMBER_OF_FRAMES = 12;

    private static final Dimension DIMENSION = new Dimension(24, 48);

    private int frameNumber = 0;

    private Color basicColor;
    
    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }

    public ImageType getImageType() {
        return ImageType.GIF;
    }

    public Dimension getDimension() {
        return DIMENSION;
    }

    public boolean isLooped() {
        return true;
    }

    public int getFrameDelay() {
        return 1000;
    }

    public void startFramesSequence() {
        frameNumber = 0;
    }

    public boolean hasNextFrame() {
        return frameNumber < NUMBER_OF_FRAMES;
    }

    /**
     * Creates a main stage for progress bar background.
     * 
     * @param context
     *            resource context
     * @return a <code>BufferedImage</code> object
     */
    private BufferedImage createMainStage() {
        Color progressbarBackgroundColor = basicColor;
        Color progressbarSpiralColor = ColorUtils.adjustLightness(basicColor, 0.2f);
        
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
    
    public void paint(Graphics2D g2d, Dimension dimension) {
        frameNumber++;

        BufferedImage mainStage = createMainStage();
        BufferedImage frame = mainStage.getSubimage(0, 48 - frameNumber * 2, dimension.width, dimension.height);
        g2d.drawImage(frame, null, null);
        Color progressbarShadowStartColor = ColorUtils.overwriteAlpha(ColorUtils.adjustLightness(basicColor, 0.7f), 0.6f);
        Color progressbarShadowEndColor = ColorUtils.overwriteAlpha(ColorUtils.adjustLightness(basicColor, 0.3f), 0.6f);
        // paint a shadow in the form of semi-transparent gradient
        g2d.setPaint(new GradientPaint(0, 0, progressbarShadowStartColor, 0, 7, progressbarShadowEndColor));
        g2d.fillRect(0, 0, dimension.width, 7);
    }
    
    public boolean isTransient() {
        return false;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        // TODO Auto-generated method stub
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Integer color = skin.getColorParameter(context, Skin.SELECT_CONTROL_COLOR);
        dataOutput.writeInt(color.intValue());
    }
    
    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        basicColor = new Color(dataInput.readInt());
    }

    public boolean isCacheable(FacesContext context) {
        return true;
    }

    public Date getExpires(FacesContext context) {
        return null;
    }

    public int getTimeToLive(FacesContext context) {
        return 0;
    }

    public String getEntityTag(FacesContext context) {
        return null;
    }
}
