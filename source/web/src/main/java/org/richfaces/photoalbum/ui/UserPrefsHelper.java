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
package org.richfaces.photoalbum.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.richfaces.photoalbum.domain.Sex;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IUserAction;

/**
 * Convenience UI class for userPrefs page
 *
 * @author Andrey Markhel
 */

@Named
@RequestScoped
public class UserPrefsHelper implements Serializable {
    private static final long serialVersionUID = -1767281809514660171L;
    @Inject
    IUserAction userAction;

    // @In(required=false, scope=ScopeType.CONVERSATION) @Out(required=false, scope=ScopeType.CONVERSATION)
    @Inject
    private File avatarData;

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
        //avatarData = new File(file.getInputStream());
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