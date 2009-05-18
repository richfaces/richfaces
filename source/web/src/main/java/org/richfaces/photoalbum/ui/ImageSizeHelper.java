/**
 * 
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