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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ManagedBean
@SessionScoped
public class TabPanelBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tabIdLast = 2;
    private List<TabBean> tabBeans = new ArrayList<TabBean>();

    @PostConstruct
    public void init() {
        int id = tabIdLast;
        while (id < tabIdLast + 3) {
            id = ++id;
            createTab(id);
        }
        tabIdLast = id;
    }

    public List<TabBean> getTabBeans() {
        return tabBeans;
    }

    public void createTab(int id) {
        String idBase = "tab" + id;
        tabBeans.add(new TabBean(idBase, idBase, idBase + " header", "content of tab " + id));
    }

    public void generateNewTab() {
        createTab(++tabIdLast);
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
