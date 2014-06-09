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
package org.richfaces.demo.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.model.UploadedFile;
import org.richfaces.ui.input.fileUpload.FileUploadEvent;

/**
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
@ManagedBean(name = "richFileUploadBean2")
@ViewScoped
public class RichFileUploadBean implements Serializable {

    private static final long serialVersionUID = -1L;
    private List<UploadedFile> files;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        files = new ArrayList<UploadedFile>();
    }

    public List<UploadedFile> getItems() {
        return files;
    }

    public void listener(FileUploadEvent event) {
        UploadedFile file = event.getUploadedFile();

        if (file != null) {
            files.add(file);
        }
    }

    public void listener2(FileUploadEvent event) {
        UploadedFile file = event.getUploadedFile();

        if (file != null) {
            files.add(file);
        }
    }

    public String clearUploadedData() {
        files.clear();
        return null;
    }

    public boolean isRenderButton() {
        return files.size() > 0;
    }
}
