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
package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

import org.richfaces.model.UploadItem;

public class UploadEvent extends FacesEvent {

    private static final long serialVersionUID = -7645197191376210068L;
    private UploadItem uploadItem = null;

    public UploadEvent(UIComponent component, UploadItem uploadItem) {
        super(component);
        this.uploadItem = uploadItem;
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof FileUploadListener;
    }

    public void processListener(FacesListener listener) {
        ((FileUploadListener) listener).processUpload(this);
    }

    /**
     * Returns UploadItem instance. Returns first element of list of UploadItems in case of multiple upload.
     * 
     * @return the uploadItem
     */
    public UploadItem getUploadItem() {
        return uploadItem;
    }
}
