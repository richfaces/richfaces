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
package org.richfaces.resource;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.ajax4jsf.util.HtmlColor;
import org.richfaces.renderkit.util.HtmlDimensions;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 * 
 */
public class Java2DUserResourceWrapperImpl extends BaseResourceWrapper<Java2DUserResource> 
    implements Java2DUserResourceWrapper {

    public Java2DUserResourceWrapperImpl(Java2DUserResource resourceObject) {
        super(resourceObject);
    }

    public InputStream getInputStream() throws IOException {
        Java2DUserResource j2DUserResource = getWrapped();
        Dimension dimension = j2DUserResource.getDimension();
        int width = dimension.width;
        int height = dimension.height;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageType imageType = j2DUserResource.getImageType();
        
        if ((width > 0) && (height > 0)) {
            BufferedImage image = imageType.createImage(width, height);
            Graphics2D g2d = image.createGraphics();

            try {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                j2DUserResource.paint(g2d, dimension);
            } finally {
                g2d.dispose();
            }

            try {
                ImageIO.write(image, imageType.getFormatName(), baos);
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {

                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }

    public String getContentType() {
        return getWrapped().getImageType().getMimeType();
    }

    protected String getValueParameter(FacesContext context, String name) {
        SkinFactory skinFactory = SkinFactory.getInstance();

        Skin skin = skinFactory.getSkin(context);
        String value = (String) skin.getParameter(context, name);

        if (value == null || value.length() == 0) {
            skin = skinFactory.getDefaultSkin(context);
            value = (String) skin.getParameter(context, name);
        }

        return value;
    }

    protected Integer getColorValueParameter(FacesContext context, String name, boolean useDefault) {
        Skin skin;
        if (useDefault) {
            skin = SkinFactory.getInstance().getDefaultSkin(context);
        } else {
            skin = SkinFactory.getInstance().getSkin(context);
        }

        return decodeColor((String) skin.getParameter(context, name));
    }

    protected Integer getHeight(FacesContext context, String heightParamName) {
        SkinFactory skinFactory = SkinFactory.getInstance();
        Skin skin = skinFactory.getSkin(context);

        String height = (String) skin.getParameter(context, heightParamName);
        if (height == null || height.length() == 0) {
            skin = skinFactory.getDefaultSkin(context);
            height = (String) skin.getParameter(context, heightParamName);
        }

        if (height != null && height.length() != 0) {
            return Integer.valueOf(HtmlDimensions.decode(height).intValue());
        } else {
            return Integer.valueOf(16);
        }
    }

    protected Integer decodeColor(String value) {
        if (value != null && value.length() != 0) {
            return Integer.valueOf(HtmlColor.decode(value).getRGB());
        } else {
            return null;
        }
    }

    @Override
    protected Map<String, String> getWrappedResourceResponseHeaders() {
        return getWrapped().getResponseHeaders();
    }
    
    @Override
    protected Date getLastModified(FacesContext context) {
        return getWrapped().getLastModified();
    }
}
