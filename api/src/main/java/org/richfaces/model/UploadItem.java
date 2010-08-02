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



package org.richfaces.model;

import java.io.File;
import java.io.Serializable;

/**
 * Class provides object holder for file uploaded.
 * Instance of this type will be returned by UploadEvent after appropriate listener called after uploading has been
 * completed.
 *
 * @author "Andrey Markavtsov"
 *
 */
public class UploadItem implements Serializable {

    /**
     * Serial id
     */
    private static final long serialVersionUID = -111723029745124147L;

    /** File byte content */
    private byte[] bytes;

    /** Content type */
    private String contentType;

    /** java.io.File instance */
    private File file;

    /** Users file name */
    private String fileName;

    /** File size */
    private int fileSize;

    /**
     * Constructor for the UploadItem
     */
    public UploadItem(String fileName, int fileSize, String contentType, Object file) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;

        if (null != file) {
            if (file.getClass().isAssignableFrom(File.class)) {
                this.file = (File) file;
            } else if (file.getClass().isAssignableFrom(byte[].class)) {
                this.bytes = (byte[]) file;
            }
        }
    }

    /**
     * Return true if file is holding as java.io.File type.
     * If true getFile method should be invoked to get file uploaded.
     * In another case getData method should be invoked to get file's bytes.
     * @return
     */
    public boolean isTempFile() {
        return null != file;
    }

    /**
     * This method should called only in case of TRUE value returned by {@link #isTempFile()} method.
     * Otherwise null value will be returned by this method.
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * This method should called only in case of FALSE value returned by {@link #isTempFile()} method.
     * Otherwise null value will be returned by this method.
     * @return the bytes
     * @throws Exception
     */
    public byte[] getData() {
        return bytes;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @return the fileSize
     */
    public int getFileSize() {
        return fileSize;
    }
}
