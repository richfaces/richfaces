/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.renderkit.html;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.VersionBean;
import org.richfaces.renderkit.html.images.GradientType;
import org.richfaces.renderkit.html.images.GradientType.BiColor;
import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.resource.VersionedResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 *         created 02.02.2007
 */
@DynamicResource
public class BaseGradient implements Java2DUserResource, StateHolderResource, VersionedResource {

    protected Integer headerBackgroundColor;
    protected Integer headerGradientColor;
    protected GradientType gradientType;

    private int width;
    private int height;
    private int gradientHeight;
    private String baseColor;
    private String gradientColor;
    private boolean horizontal;

    public BaseGradient(int width, int height, int gradientHeight, String baseColor, String gradientColor,
                        boolean horizontal) {
        this.setWidth(width);
        this.setHeight(height);
        this.setGradientHeight(gradientHeight);
        this.setBaseColorParam(baseColor);
        this.setGradientColorParam(gradientColor);
        this.setHorizontal(horizontal);
    }

    public BaseGradient(int width, int height, int gradientHeight) {
        this(width, height, gradientHeight, null, null, false);
    }

    public BaseGradient(int width, int height, int gradientHeight, String baseColor, String gradientColor) {
        this(width, height, gradientHeight, baseColor, gradientColor, false);
    }

    public BaseGradient(int width, int height) {
        this(width, height, height);
    }

    public BaseGradient(int width, int height, String baseColor, String gradientColor) {
        this(width, height, height, baseColor, gradientColor);
    }

    public BaseGradient() {
        this(30, 50, 20);
    }

    public BaseGradient(String baseColor, String gradientColor) {
        this(30, 50, 20, baseColor, gradientColor);
    }

    public BaseGradient(int width, int height, int gradientHeight, boolean horizontal) {
        this(width, height, gradientHeight, null, null, horizontal);
    }

    public BaseGradient(int width, int height, boolean horizontal) {
        this(width, height, horizontal ? width : height, null, null, horizontal);
    }

    public BaseGradient(int width, int height, String baseColor, String gradientColor, boolean horizontal) {
        this(width, height, horizontal ? width : height, baseColor, gradientColor, horizontal);
    }

    public BaseGradient(boolean horizontal) {
        this(30, 50, 20, null, null, horizontal);
    }

    public BaseGradient(String baseColor, String gradientColor, boolean horizontal) {
        this(30, 50, 20, baseColor, gradientColor, horizontal);
    }

    protected void initializeProperties(FacesContext context, Skin skin) {
        
    }
    
    @PostConstructResource
    public final void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        
        String gradientTypeString = null;
        if (gradientTypeString == null || gradientTypeString.length() == 0) {
            gradientTypeString = (String) skin.getParameter(context, Skin.GRADIENT_TYPE);
        }

        this.gradientType = GradientType.getByParameter(gradientTypeString);

        initializeProperties(context, skin);

        this.headerBackgroundColor = skin.getColorParameter(context, baseColor);
        this.headerGradientColor = skin.getColorParameter(context, gradientColor);
    }

    @ResourceParameter(defaultValue = "30")
    public final void setWidth(int width) {
        this.width = width;
    }
    
    @ResourceParameter(defaultValue = "50")
    public final void setHeight(int height) {
        this.height = height;
    }
    
    @ResourceParameter(defaultValue = "20")
    public final void setGradientHeight(int gradientHeight) {
        this.gradientHeight = gradientHeight;
    }
    
    @ResourceParameter(defaultValue = Skin.HEADER_BACKGROUND_COLOR)
    public final void setBaseColorParam(String paramName) {
        this.baseColor = paramName;
    }
    
    @ResourceParameter(defaultValue = Skin.HEADER_GRADIENT_COLOR)
    public final void setGradientColorParam(String paramName) {
        this.gradientColor = paramName;
    }
    
    @ResourceParameter(defaultValue = "false")
    public final void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }
    
    public final void setGradientType(GradientType gradientType) {
        this.gradientType = gradientType;
    }
    
    public Dimension getDimension() {
        return new Dimension(getWidth(), getHeight());
    }

    /**
     * @return the gradientHeight
     */
    protected int getGradientHeight() {
        return gradientHeight;
    }

    protected int getHeight() {
        return height;
    }
    
    protected int getWidth() {
        return width;
    }
    
    /**
     * @return the baseColor
     */
    protected String getBaseColor() {
        return baseColor;
    }

    /**
     * @return the gradientColor
     */
    protected String getGradientColor() {
        return gradientColor;
    }

    /**
     * @return the horizontal
     */
    protected boolean isHorizontal() {
        return horizontal;
    }

    protected void drawGradient(Graphics2D g2d, Shape shape, BiColor colors, int height) {
        if (colors != null) {
            GradientPaint gragient = new GradientPaint(0, 0, colors.getTopColor(), 0, height, colors.getBottomColor());
            g2d.setPaint(gragient);
            g2d.fill(shape);
        }
    }

    public void paint(Graphics2D graphics2d, Dimension dimension) {
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        paintGradient(graphics2d, dimension);
    }

    /**
     * @param g2d
     * @param dim
     */
    protected void paintGradient(Graphics2D g2d, Dimension dim) {
        if ((headerBackgroundColor != null || headerGradientColor != null) && gradientType != null) {
            BiColor biColor = new GradientType.BiColor(headerBackgroundColor, headerGradientColor);

            BiColor firstLayer = gradientType.getFirstLayerColors(biColor);
            BiColor secondLayer = gradientType.getSecondLayerColors(biColor);

            if (horizontal) {
                //x -> y, y -> x
                g2d.transform(new AffineTransform(0, 1, 1, 0, 0, 0));
                dim.setSize(dim.height, dim.width);
            }

            int localGradientHeight = this.gradientHeight;
            if (localGradientHeight < 0) {
                localGradientHeight = dim.height;
            }

            Rectangle2D rect = new Rectangle2D.Float(
                0,
                0,
                dim.width,
                dim.height);

            drawGradient(g2d, rect, firstLayer, localGradientHeight);

            int smallGradientHeight = localGradientHeight / 2;

            rect = new Rectangle2D.Float(
                0,
                0,
                dim.width,
                smallGradientHeight);

            drawGradient(g2d, rect, secondLayer, smallGradientHeight);
        }
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.width = dataInput.readShort();
        this.height = dataInput.readShort();
        this.gradientHeight = dataInput.readShort();
        this.horizontal = dataInput.readBoolean();
        
        this.headerBackgroundColor = dataInput.readInt();
        this.headerGradientColor = dataInput.readInt();
        this.gradientType = GradientType.values()[dataInput.readByte()];
    }
    
    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeShort((short) width);
        dataOutput.writeShort((short) height);
        dataOutput.writeShort((short) gradientHeight);
        dataOutput.writeBoolean(horizontal);
        
        dataOutput.writeInt(this.headerBackgroundColor);
        dataOutput.writeInt(this.headerGradientColor);
        dataOutput.writeByte((byte) this.gradientType.ordinal());
    }

    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }

    public ImageType getImageType() {
        return ImageType.PNG;
    }

    public boolean isTransient() {
        return false;
    }

    public String getVersion() {
        return VersionBean.VERSION.getResourceVersion();
    }

}
