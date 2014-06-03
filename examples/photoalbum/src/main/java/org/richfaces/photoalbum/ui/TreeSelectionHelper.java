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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.richfaces.component.UITree;

@Named
@SessionScoped
public class TreeSelectionHelper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Map<String, UITree> treeMap = new HashMap<String, UITree>();

    private static final String predefined = "PreDefinedTree";
    private static final String user = "userTree";
    private static final String event = "eventTree";

    private String lastTreeId = "";

    public TreeSelectionHelper() {
        treeMap.put("PreDefinedTree", new UITree());
        treeMap.put("userTree", new UITree());
        treeMap.put("eventTree", new UITree());
    }

    public UITree getPredefinedTree() {
        return treeMap.get(predefined);
    }

    public void setPredefinedTree(UITree predefinedTree) {
        treeMap.put(predefined, predefinedTree);
    }

    public UITree getUserTree() {
        return treeMap.get(user);
    }

    public void setUserTree(UITree userTree) {
        treeMap.put(user, userTree);
    }

    public UITree getEventTree() {
        return treeMap.get(event);
    }

    public void setEventTree(UITree eventTree) {
        treeMap.put(event, eventTree);
    }

    public void unselectOtherTrees(String id) {
        if (lastTreeId.equals(id)) {
            return;
        }
        for (Entry<String, UITree> e : treeMap.entrySet()) {
            if (e.getValue() != null && !e.getKey().equals(id)) {
                e.getValue().setSelection(null);
            }
        }
        lastTreeId = id;
    }
}
