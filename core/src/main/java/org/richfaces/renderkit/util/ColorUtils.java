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
package org.richfaces.renderkit.util;

import java.awt.Color;

/**
 * Utility methods that are useful for color processing.
 *
 * @author carcasser
 */
public final class ColorUtils {
    private ColorUtils() {
    }

    /**
     * Converts the components of a color, as specified by the default RGB model, to an equivalent set of values for hue,
     * saturation, and lightness that are the three components of the HSL model.
     *
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @return an array of three elements containing the hue, saturation, and lightness (in that order), of the color with the
     *         indicated red, green, and blue components.
     */
    public static float[] convertRGBtoHSL(int r, int g, int b) {
        float varR = (r / 255f);
        float varG = (g / 255f);
        float varB = (b / 255f);

        float varMin = Math.min(varR, Math.min(varG, varB)); // Min value of RGB
        float varMax = Math.max(varR, Math.max(varG, varB)); // Max value of RGB
        float delMax = varMax - varMin; // Delta RGB value

        float h = 0;
        float s = 0;
        float l = (varMax + varMin) / 2;

        if (delMax == 0 || l == 0) {
            s = 0;
        } else if (l == 1) {
            s = 1;
        } else if (l <= 0.5) {
            s = delMax / (2 * (1 - l));
        } else if (l > 0.5) {
            s = delMax / (2 * l);
        }

        if (delMax == 0) {
            h = 0;
        } else if (varMax == varR && g >= b) {
            h = 60 * (varG - varB) / delMax + 0;
        } else if (varMax == varR && varG < b) {
            h = 60 * (varG - varB) / delMax + 360;
        } else if (varMax == varG) {
            h = 60 * (varB - varR) / delMax + 120;
        } else if (varMax == varB) {
            h = 60 * (varR - varG) / delMax + 240;
        }

        return new float[] { h, s, l };
    }

    /**
     * Converts the components of a color, as specified by the HSL model, to an equivalent set of values for the default RGB
     * model.
     * <p>
     * The <code>saturation</code> and <code>lightness</code> components should be floating-point values between zero and one
     * (numbers in the range 0.0-1.0). The <code>hue</code> component can be any floating-point number. The floor of this number
     * is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce
     * the hue angle in the HSB color model.
     *
     * @param h the hue component of the color
     * @param s the saturation of the color
     * @param l the lightness of the color
     * @return the RGB value of the color with the indicated hue, saturation, and lightness
     */
    public static Color convertHSLtoRGB(float h, float s, float l) {
        float q;
        if (l < 0.5) {
            q = l * (1 + s);
        } else {
            q = l + s - (l * s);
        }

        float p = 2 * l - q;
        float hNorm = h / 360;

        float tR = hNorm + 1f / 3f;
        float tG = hNorm;
        float tB = hNorm - 1f / 3f;

        float r = tC2C(tR, p, q);
        float g = tC2C(tG, p, q);
        float b = tC2C(tB, p, q);

        return new Color(r, g, b);
    }

    private static float tC2C(float tC, float p, float q) {
        float retVal;

        if (tC < 0) {
            tC += 1;
        }

        if (tC > 1) {
            tC -= 1;
        }

        if ((6 * tC) < 1) {
            retVal = (p + (q - p) * 6 * tC);
        } else if ((2 * tC) < 1) {
            retVal = q;
        } else if ((3 * tC) < 2) {
            retVal = (p + (q - p) * 6 * (2f / 3f - tC));
        } else {
            retVal = p;
        }

        return retVal;
    }

    /**
     * Increases/decreases brightness of the given color by the specified <code>difference</code>.
     * <p>
     * The <code>difference</code> values in the range (-1.0, 1.0): 1.0 - the brightest value; -1.0 - the dimmest value.
     *
     * @param c color to adjust
     * @param difference value to be added to the current brightness
     *
     * @return a new <code>Color</code> instance with increased/decreased brightness by specified <code>difference</code>
     * @throws IllegalArgumentException if difference is outside of the range -1.0 to 1.0, inclusive
     */
    public static Color adjustBrightness(Color c, float difference) {
        if (difference < -1.0 || difference > 1.0) {
            throw new IllegalArgumentException("Difference parameter outside of expected range: "
                + "Difference parameter should be floating-point values between -1 and 1");
        }

        Color retVal = null;
        if (c != null) {
            float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
            float brightness = Math.min(1.0f, Math.max(0.0f, hsb[2] + difference));
            retVal = new Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness));
        }

        return retVal;
    }

    /**
     * Increases/decreases lightness of the given color by the specified <code>difference</code>.
     * <p>
     * The <code>difference</code> values in the range (-1.0, 1.0): 1.0 - the lightest value; -1.0 - on the contrary.
     *
     * @param c color to adjust
     * @param difference value to be added to the current lightness
     *
     * @return a new <code>Color</code> instance with increased/decreased lightness by specified <code>difference</code>
     * @throws IllegalArgumentException if difference is outside of the range -1.0 to 1.0, inclusive
     */
    public static Color adjustLightness(Color c, float difference) {
        if (difference < -1.0 || difference > 1.0) {
            throw new IllegalArgumentException("Difference parameter outside of expected range: "
                + "Difference parameter should be floating-point values between -1 and 1");
        }

        Color retVal = null;
        if (c != null) {
            float[] hsl = convertRGBtoHSL(c.getRed(), c.getGreen(), c.getBlue());
            float lightness = Math.min(1.0f, Math.max(0.0f, hsl[2] + difference));
            retVal = convertHSLtoRGB(hsl[0], hsl[1], lightness);
        }

        return retVal;
    }

    /**
     * Overwrites alpha value for given color.
     *
     * @param c color to overwrite
     * @param alpha a new value of alpha
     * @return a new <code>Color</code> object with a new specified alpha value
     */
    public static Color overwriteAlpha(Color c, float alpha) {
        Color retVal = c;
        if (c != null) {
            retVal = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255 + 0.5));
        }
        return retVal;
    }
}
