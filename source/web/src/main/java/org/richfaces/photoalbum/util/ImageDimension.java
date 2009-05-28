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
package org.richfaces.photoalbum.util;
/**
 * Convenience UI class for represent image object in different dimensions. Each image have 5 thumbnails with different
 * size, background style, file prefix, css class and  background image. CurrentDimension field will be used to determine rendering parameters used during page rendering process
 *
 * @author Andrey Markhel
 */
import org.richfaces.photoalbum.service.Constants;

public enum ImageDimension {

	SIZE_80(80), SIZE_120(120), SIZE_160(160), SIZE_200(200), SIZE_MEDIUM(600), ORIGINAL(0);

	final static String CSS_CLASS = "preview_box_photo_";
	final static String FILE_POSTFIX = "_small";
	final static String IMAGE_BG = "/img/shell/frame_photo_%1$d.png";
	final static String IMAGE_BG_STYLE = "width: %1$dpx; height: %1$dpx";
	
	int x;
	String bgStyle;
	String cssClass;
	String imageBgSrc;
	String filePostfix;

	private ImageDimension(int x) {
		this.x = x;
		this.bgStyle = String.format(IMAGE_BG_STYLE, x + 20);
		cssClass = CSS_CLASS + x;
		imageBgSrc = String.format(IMAGE_BG, (x == 160) ? 200 : x);
		if(x == 600){
			filePostfix = "_medium";
		}else if(x == 0){
			filePostfix = "";
		}else{
			filePostfix = FILE_POSTFIX + x;
		}
	}

	public int getX() {
		return x;
	}

	public String getCssClass() {
		return cssClass;
	}

	public String getImageBg() {
		return imageBgSrc;
	}
	
	public String getImageBgStyle() {
		return bgStyle;
	}

	public String getFilePostfix() {
		return filePostfix;
	}
	
	public static ImageDimension getInstance(int x) {
		ImageDimension [] all = values();
		for (int i=0; i<all.length; i++) {
			if (all[i].x == x) {
				return all[i];
			}
		}
		return values()[Constants.DEFAULT_IMAGE_SIZEVALUE];
	}
}