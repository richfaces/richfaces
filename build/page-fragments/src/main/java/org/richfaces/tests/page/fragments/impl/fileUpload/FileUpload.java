/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.page.fragments.impl.fileUpload;

import java.io.File;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface FileUpload {

    /**
     * Adds the given <code>file</code> in to the list of to be uploaded files.
     *
     * @param file to be uploaded
     * @return <code>true</code> if the file was added successfully, <code>false</code> otherwise
     */
    boolean addFile(File file);

    /**
     * Clears all to be uploaded or already uploaded files from the list.
     *
     * @return back the fileUpload object
     */
    FileUpload clearAll();

    /**
     * Uploads all to be uploaded files.
     *
     * @return back the fileUpload object
     */
    FileUpload upload();
}
