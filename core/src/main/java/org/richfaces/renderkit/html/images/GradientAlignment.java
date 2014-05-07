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

import java.util.Locale;

import static java.lang.Math.max;

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
