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
package org.richfaces.photoalbum.ui;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.ImageDimension;

/**
 *
 * Convenience UI class for image resizing
 *
 * @author Andrey Markhel
 */

@Named
@ApplicationScoped
public class ImageSizeHelper {

    int value = Constants.DEFAULT_IMAGE_SIZEVALUE;

    ImageDimension currentDimension = ImageDimension.getInstance(Constants.DEFAULT_IMAGE_SIZEVALUE);

    public ImageDimension getCurrentDimension() {
        return currentDimension;
    }

    public int getValue() {
        return this.value;
    }

    /**
     *
     * Convenience method invoked after user want to change image dimensions
     *
     * @param value - new image dimension value
     */
    public void setValue(int value) {
        currentDimension = ImageDimension.getInstance(value);
        this.value = currentDimension.getX();
    }
}