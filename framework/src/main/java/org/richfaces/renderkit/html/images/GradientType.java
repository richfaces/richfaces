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

import java.awt.Color;
import java.util.Locale;

/**
 * @author Nick Belaevski mailto:nbelaevski@exadel.com created 16.04.2008
 */
public enum GradientType {
    glass {
        @Override
        public BiColor getFirstLayerColors(BiColor biColor) {
            Color bottomColor = biColor.getBottomColor();

            if (bottomColor != null) {
                float[] hsb = Color.RGBtoHSB(bottomColor.getRed(), bottomColor.getGreen(), bottomColor.getBlue(), null);

                hsb[2] = 0.2f * hsb[2] + 0.80f; // (hsb[2] * 2) / 10 + 80;
                hsb[1] = 0.5f * hsb[1];

                Color topColor = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));

                return new BiColor(bottomColor, topColor);
            } else {
                return null;
            }
        }

        @Override
        public BiColor getSecondLayerColors(BiColor biColor) {
            if (biColor.getBottomColor() != null) {
                return WHITE;
            } else {
                return null;
            }
        }
    },
    plastic {
        @Override
        public BiColor getFirstLayerColors(BiColor biColor) {
            Color bottomColor = biColor.getBottomColor();

            if (bottomColor != null) {
                float[] hsb = Color.RGBtoHSB(bottomColor.getRed(), bottomColor.getGreen(), bottomColor.getBlue(), null);

                hsb[2] = 0.25f * hsb[2] + 0.75f; // (100 - hsb[2]) * 0.75f + hsb[2];
                hsb[1] = 0.75f * hsb[1];

                Color topColor = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));

                return new BiColor(topColor, bottomColor);
            } else {
                return null;
            }
        }

        @Override
        public BiColor getSecondLayerColors(BiColor biColor) {
            if (biColor.getBottomColor() != null) {
                return WHITE;
            } else {
                return null;
            }
        }
    },
    plain {
        @Override
        public BiColor getFirstLayerColors(BiColor biColor) {
            if ((biColor.getBottomColor() != null) && (biColor.getTopColor() != null)) {
                return biColor;
            } else {
                return null;
            }
        }

        @Override
        public BiColor getSecondLayerColors(BiColor biColor) {
            return null;
        }
    };
    private static final BiColor WHITE = new BiColor(new Color(0xff, 0xff, 0xff, (int) (0.65f * 0xff)), new Color(0xff, 0xff,
        0xff, (int) (0.20f * 0xff)));

    public static final GradientType getByParameter(String string) {
        if ((string == null) || (string.length() == 0)) {
            return plain;
        }

        return GradientType.valueOf(string.toLowerCase(Locale.US));
    }

    public abstract BiColor getFirstLayerColors(BiColor biColor);

    public abstract BiColor getSecondLayerColors(BiColor biColor);

    public static class BiColor {
        private Color bottomColor;
        private Color topColor;

        public BiColor(Color topColor, Color bottomColor) {
            super();
            this.topColor = topColor;
            this.bottomColor = bottomColor;
        }

        public BiColor(Integer topColor, Integer bottomColor) {
            super();
            this.topColor = (topColor != null) ? new Color(topColor.intValue()) : null;
            this.bottomColor = (bottomColor != null) ? new Color(bottomColor.intValue()) : null;
        }

        public Color getTopColor() {
            return topColor;
        }

        public Color getBottomColor() {
            return bottomColor;
        }

        @Override
        public String toString() {
            return "BiColor: [" + topColor + " -> " + bottomColor + "]";
        }
    }
}
