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

package org.richfaces.demo.tables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.demo.tables.model.slides.Picture;

/**
 * @author Ilya Shaikovsky
 */
@ManagedBean
@ViewScoped
public class SlidesBean implements Serializable {
    private static final long serialVersionUID = -6498592143189891265L;
    private static final String FILE_EXT = ".jpg";
    private static final int FILES_COUNT = 9;
    private static final String PATH_PREFIX = "/images/nature/";
    private static final String PIC_NAME = "pic";
    private List<Picture> pictures;

    public SlidesBean() {
        pictures = new ArrayList<Picture>();
        for (int i = 1; i <= FILES_COUNT; i++) {
            pictures.add(new Picture(PATH_PREFIX + PIC_NAME + i + FILE_EXT, PIC_NAME + i));
        }
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
