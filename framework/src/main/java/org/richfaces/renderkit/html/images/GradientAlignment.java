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
package org.richfaces.renderkit.html.images;

import static java.lang.Math.max;

import java.util.Locale;

/**
 * Created 22.04.2008
 *
 * @author Nick Belaevski
 * @since 3.2
 */
public enum GradientAlignment {
    top {
        @Override
        public int getBottomRectangleHeight(int imageHeight, int gradientHeight) {
            return max(imageHeight - gradientHeight, 0);
        }

        @Override
        public int getTopRectangleHeight(int imageHeight, int gradientHeight) {
            return 0;
        }
    },
    middle {
        @Override
        public int getBottomRectangleHeight(int imageHeight, int gradientHeight) {
            return max((imageHeight - gradientHeight) / 2, 0);
        }

        @Override
        public int getTopRectangleHeight(int imageHeight, int gradientHeight) {
            return getBottomRectangleHeight(imageHeight, gradientHeight);
        }
    },
    bottom {
        @Override
        public int getBottomRectangleHeight(int imageHeight, int gradientHeight) {
            return 0;
        }

        @Override
        public int getTopRectangleHeight(int imageHeight, int gradientHeight) {
            return max(imageHeight - gradientHeight, 0);
        }
    };

    public static final GradientAlignment getByParameter(String string) {
        if ((string == null) || (string.length() == 0)) {
            return middle;
        }

        return GradientAlignment.valueOf(string.toLowerCase(Locale.US));
    }

    public abstract int getTopRectangleHeight(int imageHeight, int gradientHeight);

    public abstract int getBottomRectangleHeight(int imageHeight, int gradientHeight);
}
