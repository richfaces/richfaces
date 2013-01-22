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
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

public enum ImageType {
    GIF("gif") {
        @Override
        public BufferedImage createImage(int width, int height) {
            return createBitmaskImage(width, height);
        }
    },
    PNG("png") {
        @Override
        public BufferedImage createImage(int width, int height) {
            return createARGBImage(width, height);
        }
    },
    PNG8("png") {
        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, SAFE_WEB_COLORS_MODEL);
        }
    },
    JPEG("jpeg") {
        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
    };
    /**
     * Default web safe colors color model
     */
    protected static final IndexColorModel SAFE_WEB_COLORS_MODEL;

    // Build web safe 6x6x6 cube color model.
    static {
        byte[] webLevels = { 0, 51, 102, (byte) 153, (byte) 204, (byte) 255 };
        int colorsNumber = webLevels.length * webLevels.length * webLevels.length; /* 216 colors */
        byte[] r = new byte[colorsNumber];
        byte[] g = new byte[colorsNumber];
        byte[] b = new byte[colorsNumber];

        r[0] = 0;
        g[0] = 0;
        b[0] = 0;

        for (int i = 0; i < webLevels.length; i++) {
            for (int j = 0; j < webLevels.length; j++) {
                for (int k = 0; k < webLevels.length; k++) {
                    int colorNum = i * webLevels.length * webLevels.length + j * webLevels.length + k;

                    r[colorNum] = webLevels[i];
                    g[colorNum] = webLevels[j];
                    b[colorNum] = webLevels[k];
                }
            }
        }

        SAFE_WEB_COLORS_MODEL = new IndexColorModel(8, colorsNumber, r, g, b, 0);
    }

    private String formatName;
    private String mimeType;

    private ImageType(String formatName) {
        this.formatName = formatName;
        this.mimeType = "image/" + formatName;
    }

    private static BufferedImage createARGBImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    private static BufferedImage createBitmaskImage(int width, int height) {
        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), true, false,
            Transparency.BITMASK, DataBuffer.TYPE_BYTE);

        WritableRaster raster = colorModel.createCompatibleWritableRaster(width, height);

        return new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
    }

    public abstract BufferedImage createImage(int width, int height);

    public BufferedImage createImage(Dimension dimension) {
        return createImage(dimension.width, dimension.height);
    }

    public String getFormatName() {
        return formatName;
    }

    public String getMimeType() {
        return mimeType;
    }
}