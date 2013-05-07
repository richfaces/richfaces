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

package org.richfaces.demo;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean(name = "skinBean")
@SessionScoped
public class SkinBean implements Serializable {
    private static final long serialVersionUID = 2744605279708632184L;
    private SelectItem[] skinSetItems = { new SelectItem("blueSky"), new SelectItem("classic"), new SelectItem("deepMarine"),
            new SelectItem("DEFAULT"), new SelectItem("emeraldTown"), new SelectItem("japanCherry"), new SelectItem("NULL"),
            new SelectItem("plain"), new SelectItem("ruby"), new SelectItem("wine") };
    private String skin = "classic";
    private boolean enableElementsSkinning = true;
    private boolean enableClassesSkinning = false;

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public boolean isEnableElementsSkinning() {
        return enableElementsSkinning;
    }

    public void setEnableElementsSkinning(boolean enableElementsSkinning) {
        this.enableElementsSkinning = enableElementsSkinning;
    }

    public boolean isEnableClassesSkinning() {
        return enableClassesSkinning;
    }

    public void setEnableClassesSkinning(boolean enableClassesSkinning) {
        this.enableClassesSkinning = enableClassesSkinning;
    }

    public SelectItem[] getSkinSetItems() {
        return skinSetItems;
    }
}
