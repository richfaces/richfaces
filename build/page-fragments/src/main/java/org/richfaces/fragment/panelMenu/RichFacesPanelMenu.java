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

package org.richfaces.fragment.panelMenu;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RichFacesPanelMenu extends AbstractPanelMenu {

    @FindByJQuery(".rf-pm-top-gr,.rf-pm-gr")
    private List<WebElement> menuGroups;
    @FindByJQuery(".rf-pm-top-itm,.rf-pm-itm")
    private List<WebElement> menuItems;

    private AdvancedPanelMenuInteractions advancedInteractions = new AdvancedPanelMenuInteractions();

    @Root
    private WebElement root;

    @Override
    public List<WebElement> getMenuItems() {
        return menuItems;
    }

    @Override
    public List<WebElement> getMenuGroups() {
        return menuGroups;
    }

    @Override
    public AdvancedPanelMenuInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedPanelMenuInteractions extends AbstractPanelMenu.AdvancedAbstractPanelMenuInteractions {

        public List<WebElement> getMenuGroupElements() {
            return menuGroups;
        }

        public List<WebElement> getMenuItemElements() {
            return menuItems;
        }

        public List<WebElement> getAllSelectedItems() {
            return root.findElements(By.cssSelector("div[class*=rf-pm][class*=-itm-sel]"));
        }

        public List<WebElement> getAllSelectedGroups() {
            return root.findElements(By.cssSelector("div[class*=rf-pm][class*=-gr-sel]"));
        }

        public List<WebElement> getAllDisabledGroups() {
            return root.findElements(By.cssSelector("div[class*=rf-pm-][class*=-gr-dis]"));
        }

        public List<WebElement> getAllDisabledItems() {
            return root.findElements(By.cssSelector("div[class*=rf-pm-][class*=-itm-dis]"));
        }

        public List<WebElement> getAllExpandedGroups() {
            return root.findElements(By.cssSelector("div.rf-pm-hdr-exp"));
        }

        public WebElement getRootElement() {
            return root;
        }
    }
}
