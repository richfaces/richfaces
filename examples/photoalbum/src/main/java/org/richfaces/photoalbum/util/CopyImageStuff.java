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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * Utility class, that perform copying images from ear file to temp folder at startup application
 *
 * @author Andrey Markavtsov
 *
 */

@Startup
@ApplicationScoped
public class CopyImageStuff {

    // @Out(scope = ScopeType.APPLICATION)
    @Produces
    private File uploadRoot;

    // @Out(scope = ScopeType.APPLICATION)
    @SuppressWarnings("unused")
    @Produces
    private String uploadRootPath;

    private String imageSrc;

    /**
     * Method, that perform copying images from ear file to temp folder at startup application
     *
     */
    @PostConstruct
    public void create() throws IOException {
        resolveImageFolder();
        resolveUploadRoot();
        copyImages();
    }

    /**
     * Method, that perform deleting images from temp folder during destroy application
     *
     */
    @PreDestroy
    public void destroy() throws IOException {
        deleteDirectory(uploadRoot, true);
    }

    private void resolveImageFolder() throws MalformedURLException {
        final ServletContext servletContext = ((HttpSession) (FacesContext.getCurrentInstance().getExternalContext()
            .getSession(false))).getServletContext();

        if (servletContext != null) {
            // this.imageSrc = getClass().getClassLoader().getResource(IMAGE_FOLDER).getPath();
            this.imageSrc = servletContext.getRealPath("WEB-INF/classes/Upload");
        } else {
            throw new IllegalStateException(UPLOAD_FOLDER_PATH_ERROR);
        }

    }

    private void resolveUploadRoot() throws IOException {
        uploadRoot = new File(joinFiles(System.getProperty(TEMP_DIR), PHOTOALBUM_FOLDER));

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
            System.out.println("ERROR on copy '" + imageSrc + "' to '" + uploadRoot + '\'');
        }
    }
}