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

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.richfaces.component.UITree;

@Named
@SessionScoped
public class TreeSelectionHelper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final String[] treeIds = {"overForm:PreDefinedTree", "overForm:userTree", "overForm:eventTree"};

    private String lastTreeId = "";

    private UITree getTree(String id) {
        return (UITree) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
    }

    public void unselectOtherTrees(String id) {
        if (lastTreeId.equals(id)) {
            return;
        }
        for (String treeId : treeIds) {
            if (!treeId.equals(id)) {
                getTree(treeId).setSelection(null);
            }
        }
        lastTreeId = id;
    }
}
