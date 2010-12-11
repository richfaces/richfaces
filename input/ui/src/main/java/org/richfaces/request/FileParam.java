/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.richfaces.exception.FileUploadException;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.UploadedFile;

/**
 * @author Konstantin Mishin
 * 
 */
class FileParam implements Param, UploadedFile {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private String name;
    private String contentType;
    private OutputStream outputStream;
    private File file;
    private byte[] data;
    
    public FileParam(String name, String contentType, boolean createTempFiles, String tempFilesDirectory) {
        if (createTempFiles) {
            try {
                File dir = null;
                if (tempFilesDirectory != null) {
                    dir = new File(tempFilesDirectory);
                }
                file = File.createTempFile("richfaces_uploaded_file_", null, dir);
                file.deleteOnExit();
                outputStream = new FileOutputStream(file);
            } catch (IOException ex) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                throw new FileUploadException("Could not create temporary file");
            }
        } else {
            outputStream = new ByteArrayOutputStream();
        }
        this.name = name;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        long size = -1;
        if (data != null) {
            size = data.length;
        } else {
            size = file.length();
        }
        return size;
    }

    public byte[] getData() {
        byte[] result = null;
        if (data != null) {
            result = data;
        } else {
            long fileLength = file.length();
            if (fileLength > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("File content is too long to be allocated as byte[]");
            } else {
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                    byte[] fileData = new byte[(int) fileLength];
                    int totalRead = 0;
                    int read;
                    do {
                        read = inputStream.read(fileData, totalRead, fileData.length - totalRead);
                        totalRead += read;
                    } while (read > 0);
                    result = fileData;
                } catch (IOException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return result;
    }

    public InputStream getInputStream() {
        InputStream stream = null;
        if (data != null) {
            stream = new ByteArrayInputStream(data);
        } else {
            try {
                stream = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return stream;
    }

    public void complete() throws IOException {
        if (outputStream instanceof ByteArrayOutputStream) {
            data = ((ByteArrayOutputStream) outputStream).toByteArray();
        } else {
            try {
                outputStream.close();
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        outputStream = null;
    }

    public void clear() {
        if (data != null) {
            data = null;
        } else {
            file.delete();
        }
    }

    public void handle(byte[] bytes, int length) throws IOException {
        outputStream.write(bytes, 0, length);
        outputStream.flush();
    }
}