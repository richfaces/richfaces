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

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenu.AdvancedPanelMenuInteractions;

public class RichFacesPanelMenu extends AbstractPanelMenu {

    @FindBy(css = ".rf-pm-top-gr,.rf-pm-gr")
    private List<WebElement> menuGroups;
    @FindBy(css = ".rf-pm-top-itm,.rf-pm-itm")
    private List<WebElement> menuItems;

    @FindBy(css = "div[class*=rf-pm-][class*=-itm-dis]")
    private List<WebElement> allDisabledItems;
    @FindBy(css = "div[class*=rf-pm-][class*=-gr-dis]")
    private List<WebElement> allDisabledGroups;
    @FindBy(css = "div[class*=rf-pm][class*=-itm-sel]")
    private List<WebElement> allSelectedItems;
    @FindBy(css = "div[class*=rf-pm][class*=-gr-sel]")
    private List<WebElement> allSelectedGroups;
    @FindBy(css = "div.rf-pm-hdr-exp")
    private List<WebElement> allExpandedGroups;

    private final AdvancedPanelMenuInteractions advancedInteractions = new AdvancedPanelMenuInteractions();

    @Root
    private WebElement root;

    @Override
    public List<WebElement> getMenuItems() {
        return Collections.unmodifiableList(menuItems);
    }

    @Override
    public List<WebElement> getMenuGroups() {
        return Collections.unmodifiableList(menuGroups);
    }

    @Override
    public AdvancedPanelMenuInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedPanelMenuInteractions extends AbstractPanelMenu.AdvancedAbstractPanelMenuInteractions
        implements VisibleComponentInteractions {

        public List<WebElement> getMenuGroupElements() {
            return getMenuGroups();
        }

        public List<WebElement> getMenuItemElements() {
            return getMenuItems();
        }

        public List<WebElement> getAllSelectedItems() {
            return Collections.unmodifiableList(allSelectedItems);
        }

        public List<WebElement> getAllSelectedGroups() {
            return Collections.unmodifiableList(allSelectedGroups);
        }

        public List<WebElement> getAllDisabledGroups() {
            return Collections.unmodifiableList(allDisabledGroups);
        }

        public List<WebElement> getAllDisabledItems() {
            return Collections.unmodifiableList(allDisabledItems);
        }

        public List<WebElement> getAllExpandedGroups() {
            return Collections.unmodifiableList(allExpandedGroups);
        }

        @Override
        public WebElement getRootElement() {
            return root;
        }
    }
}
