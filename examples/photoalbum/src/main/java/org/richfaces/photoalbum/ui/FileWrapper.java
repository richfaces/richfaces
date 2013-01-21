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
package org.richfaces.photoalbum.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Named;

import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.ImageEvent;
import org.richfaces.photoalbum.event.SimpleEvent;

@Named
@ApplicationScoped
public class FileWrapper implements Serializable {

    private static final long serialVersionUID = -1767281809514660171L;

    private boolean complete = false;

    private List<Image> files = new ArrayList<Image>();

    private List<ErrorImage> errorFiles = new ArrayList<ErrorImage>();

    class ErrorImage {
        private Image image;
        private String errorDescription;

        ErrorImage(Image i, String description) {
            image = i;
            errorDescription = description;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public void setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
        }
    }

    public int getSize() {
        return getFiles().size();
    }

    public List<Image> getFiles() {
        return files;
    }

    public void setFiles(List<Image> files) {
        this.files = files;
    }

    public void removeImage(@Observes @EventType(Events.IMAGE_DRAGGED_EVENT) ImageEvent ie) {
        files.remove(ie.getImage());
    }

    public void clear(@Observes @EventType(Events.CLEAR_FILE_UPLOAD_EVENT) SimpleEvent se) {
        files.clear();
        errorFiles.clear();
        complete = false;
    }

    public void onFileUploadError(Image image, String error) {
        ErrorImage e = new ErrorImage(image, error);
        errorFiles.add(e);
    }

    public Image getErrorImage(ErrorImage e) {
        return e.getImage();
    }

    public String getErrorDescription(ErrorImage e) {
        return e.getErrorDescription();
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public List<ErrorImage> getErrorFiles() {
        return errorFiles;
    }

    public void setErrorFiles(List<ErrorImage> errorFiles) {
        this.errorFiles = errorFiles;
    }
}