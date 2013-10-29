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

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.list.ListItem;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface FileUploadItem extends ListItem {

    /**
     * Finds out whether this item representing a file is already uploaded.
     *
     * @return <code>true</code> if this file item is already uploaded, <code>false</code> otherwise
     */
    boolean isUploaded();

    /**
     * Clears/deletes the log of already uploaded/to be uploaded file from the list of the files of the already uploaded files/to be uploaded files.
     *
     * @return
     */
    WebElement getClearOrDeleteElement();

    /**
     * Gets the name of the this file.
     *
     * @return file name
     */
    String getFilename();

    WebElement getFilenameElement();

    String getState();

    WebElement getStateElement();

    void remove();
}
