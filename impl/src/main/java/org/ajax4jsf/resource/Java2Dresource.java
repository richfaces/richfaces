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

package org.ajax4jsf.resource;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.ajax4jsf.util.HtmlColor;
import org.ajax4jsf.util.HtmlDimensions;
import org.richfaces.resource.AbstractCacheableResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/01 15:31:57 $
 */
public abstract class Java2Dresource extends AbstractCacheableResource implements StateHolderResource {
    private static final String SKIN_MARKER = "Skin.";
    private ImageType imageType;

    public Java2Dresource(ImageType imageType) {
        super();
        this.imageType = imageType;
    }

    /**
     * Primary calculation of image dimensions - used when HTML code is
     * generated to render IMG's width and height Subclasses should override
     * this method to provide correct sizes of rendered images
     *
     * @return dimensions of the image to be displayed on page
     */
    public abstract Dimension getDimension();

    /**
     * Template method for create image as Applet-like paint.
     *
     * @param graphics2D -
     *                   graphics to paint.
     */
    protected void paint(Graphics2D graphics2D, Dimension dimension) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.resource.AbstractBaseResource#getInputStream()
     */
    @Override
    public InputStream getInputStream() throws IOException {
        Dimension dimension = getDimension();
        int width = dimension.width;
        int height = dimension.height;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if ((width > 0) && (height > 0)) {
            BufferedImage image = imageType.createImage(width, height);
            Graphics2D g2d = image.createGraphics();

            try {
                paint(g2d, dimension);
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

    @Override
    public String getContentType() {
        return imageType.getMimeType();
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

    protected String encodeSkinParameter(String param){
        if(param.startsWith(SKIN_MARKER)){
            String name = param.substring(SKIN_MARKER.length());
            return getValueParameter(FacesContext.getCurrentInstance(), name );
        }
        return param;
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

    public void readState(FacesContext context, DataInput dataInput) throws IOException {}

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {}

    public boolean isTransient() {
        return false;
    }
}
