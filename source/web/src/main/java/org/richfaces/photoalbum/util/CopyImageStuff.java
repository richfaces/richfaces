/**
 * 
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