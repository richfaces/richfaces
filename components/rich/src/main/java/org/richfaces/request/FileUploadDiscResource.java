/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * @author Nick Belaevski
 */
final class FileUploadDiscResource extends FileUploadResource {
    private File file;
    private FileOutputStream fos;

    FileUploadDiscResource(String name, String uploadLocation) {
        super(name, uploadLocation);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public void write(String fileName) throws IOException {
        boolean writeResult = file.renameTo(getOutputFile(fileName));

        if (!writeResult) {
            throw new IOException(MessageFormat.format("Write to disc resource {0} failed", fileName));
        }
    }

    @Override
    public void delete() throws IOException {
        complete();

        if (file == null || !file.exists()) {
            return;
        }

        boolean deleteResult = file.delete();

        if (!deleteResult) {
            throw new IOException("File delete failed");
        }
    }

    public void create() throws IOException {
        file = File.createTempFile("richfaces_uploaded_file_", null, getOutputFile(null));
        file.deleteOnExit();
        fos = new FileOutputStream(file);
    }

    public void handle(byte[] bytes, int length) throws IOException {
        fos.write(bytes, 0, length);
    }

    public void complete() {
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            // Swallow
        }
        fos = null;
    }
}
