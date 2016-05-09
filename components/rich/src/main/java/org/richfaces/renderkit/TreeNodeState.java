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
    expanded("rf-tr-nd-exp", "rf-trn-hnd-exp", "rf-trn-ico-exp") {
        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public boolean isDifferentThan(TreeNodeState anotherState) {
            return anotherState != expandedNoChildren && super.isDifferentThan(anotherState);
        }
    },
    expandedNoChildren("rf-tr-nd-exp rf-tr-nd-exp-nc", "rf-trn-hnd-lf", "rf-trn-ico-exp") {
        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public boolean isDifferentThan(TreeNodeState anotherState) {
            return anotherState != expanded && super.isDifferentThan(anotherState);
        }
    },
    collapsed("rf-tr-nd-colps", "rf-trn-hnd-colps", "rf-trn-ico-colps") {
        @Override
        public boolean isLeaf() {
            return false;
        }
    },
    leaf("rf-tr-nd-lf", "rf-trn-hnd-lf", "rf-trn-ico-lf") {
        @Override
        public boolean isLeaf() {
            return true;
        }
    };
    private String nodeClass;
    private String handleClass;
    private String iconClass;
    private String customIconClass;

    TreeNodeState(String nodeClass, String defaultHandleClass, String iconClass) {
        this.nodeClass = nodeClass;
        this.handleClass = HtmlUtil.concatClasses(defaultHandleClass, "rf-trn-hnd");
        this.iconClass = HtmlUtil.concatClasses(iconClass, "rf-trn-ico");
        this.customIconClass = HtmlUtil.concatClasses(this.iconClass, "rf-trn-ico-cst");
    }

    public abstract boolean isLeaf();

    public boolean isDifferentThan(TreeNodeState anotherState) {
        return anotherState != this;
    }

    public String getNodeClass() {
        return nodeClass;
    }

    public String getHandleClass() {
        return handleClass;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getCustomIconClass() {
        return customIconClass;
    }
}