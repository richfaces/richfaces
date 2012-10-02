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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.manager.FileManager;
import org.richfaces.photoalbum.service.Constants;

@Named
@RequestScoped
public class ImageLoader implements Serializable {

    private static final long serialVersionUID = -1572789608594870285L;

    @Inject
    FileManager fileManager;

    public void paintImage(OutputStream out, Object data) throws IOException {
        if (null == data) {
            return;
        }
        File imageResource = fileManager.getFileByPath(data.toString());
        paintImageToBrowser(out, imageResource);
    }

    public void paintAvatarImage(OutputStream out, Object data) throws IOException {
        if (null == data) {
            return;
        }
        File imageResource = fileManager.getFileByPath(data.toString());
        if (imageResource == null || !imageResource.exists()) {
            imageResource = fileManager.getFileByAbsolutePath(data.toString());
        }
        paintImageToBrowser(out, imageResource);
    }

    private void paintImageToBrowser(OutputStream out, File imageResource) throws IOException {

        if (imageResource != null && imageResource.exists()) {

            byte[] toWrite = new byte[Constants.DEFAULT_BUFFER_SIZE];

            FileInputStream in = new FileInputStream(imageResource);

            try {
                while (in.read(toWrite) != -1) {
                    out.write(toWrite);
                }
            } finally {
                in.close();
            }

        } else {
            String suffix = excludeFilePrefix(imageResource.getPath());
            paintImage(out, fileManager.transformPath(Constants.DEFAULT_ORIGINAL_PICTURE, suffix));
            return;
        }
    }

    private String excludeFilePrefix(String path) {
        final int begin = path.lastIndexOf("_");
        final int end = path.lastIndexOf(Constants.DOT);
        return path.substring(begin, end);
    }
}