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
package org.richfaces.integration.ui.tabPanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@Named
@SessionScoped
public class TabPanelBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tabIdLast = 3;
    private List<TabBean> tabBeans = new ArrayList<TabBean>();

    @PostConstruct
    public void init() {
        String idBase = "tab" + ++tabIdLast;
        tabBeans.add(new TabBean(idBase, idBase, idBase + " header", "Content of dynamicaly created " + idBase));
        idBase = "tab" + ++tabIdLast;
        tabBeans.add(new TabBean(idBase, idBase, idBase + " header", "Content of dynamicaly created " + idBase));
        idBase = "tab" + ++tabIdLast;
        tabBeans.add(new TabBean(idBase, idBase, idBase + " header", "Content of dynamicaly created " + idBase));
    }

    public List<TabBean> getTabBeans() {
        return tabBeans;
    }

    public void generateNewTab() {
        String idBase = "tab" + ++tabIdLast;
        tabBeans.add(new TabBean(idBase, idBase, idBase + " header", "Content of dynamicaly created " + idBase));
    }

    public void removeTab() throws Exception {
        String tabIdToRemove = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("removeTabId");

        TabBean currentTab = getTabById(tabIdToRemove);

        if (currentTab != null) {
            tabBeans.remove(currentTab);
        } else {
            throw new Exception("Tab Id parameter is null");
        }
    }

    private TabBean getTabById(String tabId) {
        for (TabBean currentTab : tabBeans) {
            if (currentTab.getTabId().equals(tabId)) {
                return currentTab;
            }
        }
        return null;
    }
}
