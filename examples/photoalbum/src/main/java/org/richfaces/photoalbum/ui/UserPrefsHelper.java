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
package org.richfaces.photoalbum.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.richfaces.photoalbum.model.Sex;
import org.richfaces.photoalbum.util.Constants;

/**
 * Convenience UI class for userPrefs page
 *
 * @author Andrey Markhel
 */

@Named
@SessionScoped
public class UserPrefsHelper implements Serializable {
    private static final long serialVersionUID = -1767281809514660171L;

    private File avatarData;

    private boolean edit = false;

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        if (!edit) {
            this.avatarData = null;
        }
        this.edit = edit;
    }

    static final SelectItem[] sexs = new SelectItem[] { new SelectItem(Sex.MALE, Constants.MALE),
            new SelectItem(Sex.FEMALE, Constants.FEMALE) };

    public SelectItem[] getSexs() {
        return sexs;
    }

    /**
     * Convenience method invoked after user add avatar and outject avatar to conversation
     *
     * param event - upload event
     */
    public void uploadAvatar(FileUploadEvent event) {
        UploadedFile file = event.getUploadedFile();
        try {
            File f = new File((file.getName() + "avatar"));

            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(file.getData());
            fos.flush();
            fos.close();

            avatarData = f;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public File getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(File avatarData) {
        this.avatarData = avatarData;
    }
}