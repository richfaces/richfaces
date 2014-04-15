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
package org.richfaces.resource;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.ajax4jsf.io.ByteBuffer;
import org.ajax4jsf.io.FastBufferInputStream;
import org.ajax4jsf.io.FastBufferOutputStream;
import org.ajax4jsf.util.HtmlColor;
import org.richfaces.util.HtmlDimensions;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 */
public class Java2DUserResourceWrapperImpl extends BaseResourceWrapper<Java2DUserResource> {
    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();

    public Java2DUserResourceWrapperImpl(Java2DUserResource resourceObject, boolean cacheable, boolean versioned) {
        super(resourceObject, cacheable, versioned);
    }

    public InputStream getInputStream() throws IOException {
        FastBufferOutputStream fbos = new FastBufferOutputStream();

        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(fbos);
        try {
            paintAndWrite(imageOutputStream);
        } finally {
            if (imageOutputStream != null) {
                try {
                    imageOutputStream.close();
                } catch (IOException e) {
                    LOGGER.debug(e.getMessage(), e);
                }

                try {
                    fbos.close();
                } catch (IOException e) {
                    // Swallow
                }
            }
        }
        ByteBuffer buffer = fbos.getFirstBuffer();
        buffer.compact();

        return new FastBufferInputStream(buffer);
    }

    protected void write(BufferedImage image, String formatName, ImageOutputStream imageOutputStream) throws IOException {
        ImageIO.write(image, formatName, imageOutputStream);
    }

    public String getContentType() {
        return getWrapped().getImageType().getMimeType();
    }

    protected String getValueParameter(FacesContext context, String name) {
        SkinFactory skinFactory = SkinFactory.getInstance(context);

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
            skin = SkinFactory.getInstance(context).getDefaultSkin(context);
        } else {
            skin = SkinFactory.getInstance(context).getSkin(context);
        }

        return decodeColor((String) skin.getParameter(context, name));
    }

    protected Integer getHeight(FacesContext context, String heightParamName) {
        SkinFactory skinFactory = SkinFactory.getInstance(context);
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

    protected void paintAndWrite(ImageOutputStream outputStream) throws IOException {
        Java2DUserResource resource = getWrapped();
        ImageType imageType = resource.getImageType();

        BufferedImage image = imageType.createImage(resource.getDimension());
        Graphics2D g2d = null;
        try {
            g2d = createGraphics(image);
            resource.paint(g2d);
            ImageIO.write(image, imageType.getFormatName(), outputStream);
        } finally {
            if (g2d != null) {
                g2d.dispose();
            }
        }
    }

    protected Graphics2D createGraphics(BufferedImage image) {
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g2d;
    }
}
