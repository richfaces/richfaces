/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import org.richfaces.component.util.HtmlUtil;

public enum TreeNodeState {
    expanded("rf-tr-nd-exp", "rf-trn-hnd-exp", "rf-trn-hnd-exp rf-trn-hnd-exp-cst", "rf-trn-ico-nd") {
        @Override
        public boolean isLeaf() {
            return false;
        }
    }, 
    collapsed("rf-tr-nd-colps", "rf-trn-hnd-colps", "rf-trn-hnd-colps rf-trn-hnd-colps-cst", "rf-trn-ico-nd") {
        @Override
        public boolean isLeaf() {
            return false;
        }
    }, 
    leaf("rf-tr-nd-lf", "rf-trn-hnd-lf", "rf-trn-hnd-lf rf-trn-hnd-lf-cst", "rf-trn-ico-lf") {
        @Override
        public boolean isLeaf() {
            return true;
        }
    };

    private String nodeClass;

    private String defaultHandleClass;
    
    private String customHandleClass;

    private String iconClass;
    
    private TreeNodeState(String nodeClass, String defaultHandleClass, String customHandleClass, String iconClass) {
        this.nodeClass = nodeClass;
        this.defaultHandleClass = HtmlUtil.concatClasses(defaultHandleClass, "rf-trn-hnd");
        this.customHandleClass = HtmlUtil.concatClasses(customHandleClass, "rf-trn-hnd");
        this.iconClass = iconClass;
    }

    public abstract boolean isLeaf();
    
    public String getNodeClass() {
        return nodeClass;
    }

    public String getDefaultHandleClass() {
        return defaultHandleClass;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getCustomHandleClass() {
        return customHandleClass;
    }
}