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

import static org.richfaces.photoalbum.service.Constants.PHOTOALBUM_FOLDER;
import static org.richfaces.photoalbum.service.Constants.TEMP_DIR;
import static org.richfaces.photoalbum.service.Constants.UPLOAD_FOLDER_PATH_ERROR;
import static org.richfaces.photoalbum.util.FileUtils.copyDirectory;
import static org.richfaces.photoalbum.util.FileUtils.deleteDirectory;
import static org.richfaces.photoalbum.util.FileUtils.joinFiles;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.contexts.ServletLifecycle;


/**
 * Utility class, that perform copying images from ear file to temp folder at startup application
 * @author Andrey Markavtsov
 *
 */

@Name("CopyImagesStuff")
@Scope(ScopeType.APPLICATION)
@Startup
public class CopyImageStuff {

	@Out(scope = ScopeType.APPLICATION)
	private File uploadRoot;
	
	@Out(scope = ScopeType.APPLICATION)
	private String uploadRootPath;
	
	private String imageSrc;
	
	/**
	 * Method, that perform copying images from ear file to temp folder at startup application
	 *
	 */
	@Create
	public void create() throws IOException {
		resolveImageFolder();
		resolveUploadRoot();
		copyImages();
	}
	
	/**
	 * Method, that perform deleting images from temp folder during destroy application
	 *
	 */
	@Destroy
	public void destroy()throws IOException {
		deleteDirectory(uploadRoot, true);
	}
	
	private void resolveImageFolder() throws MalformedURLException {
		final ServletContext servletContext = ServletLifecycle.getServletContext();
		
		if (servletContext != null) {
//			this.imageSrc = getClass().getClassLoader().getResource(IMAGE_FOLDER).getPath();
			this.imageSrc = ServletLifecycle.getServletContext().getRealPath("WEB-INF/classes/Upload");
		} else {
			throw new IllegalStateException(UPLOAD_FOLDER_PATH_ERROR);
		}

	}
	
	private void resolveUploadRoot() throws IOException {
		uploadRoot = new File(joinFiles(
				System.getProperty(TEMP_DIR), PHOTOALBUM_FOLDER));

		if (uploadRoot.exists()) {
			deleteDirectory(uploadRoot, true);
		}

		uploadRoot.mkdir();

		uploadRootPath = uploadRoot.getCanonicalPath();
	}
	
	private void copyImages() {
		try {
			copyDirectory(new File(imageSrc), uploadRoot);
		} catch (IOException e) {
			System.out.println("ERROR on copy '"+imageSrc+"' to '"+uploadRoot+ '\'');
		}
	}
}