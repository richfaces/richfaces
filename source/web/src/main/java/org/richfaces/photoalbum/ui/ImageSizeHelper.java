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
package org.richfaces.photoalbum.ui;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.util.ImageDimension;

/**
 * 
 * Convenience UI class for image resizing
 *
 * @author Andrey Markhel
 */
@Name("imageSizeHelper")
@Scope(ScopeType.PAGE)
public class ImageSizeHelper {

	int value = Constants.DEFAULT_IMAGE_SIZEVALUE;
	
	ImageDimension currentDimension = ImageDimension.getInstance(Constants.DEFAULT_IMAGE_SIZEVALUE);
	
	public ImageDimension getCurrentDimension() {
		return currentDimension;
	}

	public int getValue() {
		return value;
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