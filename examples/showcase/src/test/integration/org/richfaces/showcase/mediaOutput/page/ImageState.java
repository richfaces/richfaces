/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.showcase.mediaOutput.page;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public enum ImageState {

    STATE0(Color.LEFT_RED, Color.RIGHT_DARK_BLUE,Color.TEXT_GREEN),
    STATE1(Color.LEFT_GREEN, Color.RIGHT_YELLOW, Color.TEXT_RED),
    STATE2(Color.LEFT_BLUE, Color.RIGHT_RED, Color.TEXT_DARK_BLUE);

    public static int RED_INDEX = 0;

    private Color leftColor;
    private Color rightColor;
    private Color textColor;

    private ImageState(Color leftColor, Color rightColor, Color textColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        this.textColor = textColor;
    }

    public Color getLeftColor() {
        return leftColor;
    }

    public Color getRightColor() {
        return rightColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public enum Color {
        LEFT_BLUE(ImgUsagePage.INDEX_BLUE, -15993869, "Blue"),
        LEFT_GREEN(ImgUsagePage.INDEX_GREEN, -15991296, "Green"),
        LEFT_RED(ImgUsagePage.INDEX_RED, -851957, "Red"),
        RIGHT_DARK_BLUE(ImgUsagePage.INDEX_DARK_BLUE, -16449286, "Dark Blue"),
        RIGHT_RED(ImgUsagePage.INDEX_RED, -391931, "Red"),
        RIGHT_YELLOW(ImgUsagePage.INDEX_YELLOW, -327936, "Yellow"),
        TEXT_DARK_BLUE(ImgUsagePage.INDEX_DARK_BLUE, -16776962, "Dark Blue"),
        TEXT_GREEN(ImgUsagePage.INDEX_GREEN, -16712192, "Green"),
        TEXT_RED(ImgUsagePage.INDEX_RED, -131072, "Red");

        private int index;
        private long value;
        private String name;

        private Color(int index, long value, String name) {
            this.index = index;
            this.value = value;
            this.name = name;
        }
        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }
        public long getValue() {
            return value;
        }

    }

}
