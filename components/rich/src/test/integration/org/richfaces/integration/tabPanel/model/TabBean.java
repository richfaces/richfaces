/**
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
 **/
package org.richfaces.integration.tabPanel.model;

import javax.inject.Named;
import java.io.Serializable;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class TabBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tabId;
    private String tabName;
    private String tabHeader;
    private String tabContentText;
    private boolean closable;

    public TabBean(String tabId, String tabName, String tabHeader, String tabContentText) {
        this.tabId = tabId;
        this.tabName = tabName;
        this.tabHeader = tabHeader;
        this.tabContentText = tabContentText;

        // default is closable
        this.closable = true;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTabHeader() {
        return tabHeader;
    }

    public void setTabHeader(String tabHeader) {
        this.tabHeader = tabHeader;
    }

    public String getTabContentText() {
        return tabContentText;
    }

    public void setTabContentText(String tabContentText) {
        this.tabContentText = tabContentText;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    @Override
    public String toString() {
        return tabId;
    }
}