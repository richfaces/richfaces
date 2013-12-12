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

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RichFacesPanelMenuItem implements PanelMenuItem {

    @FindBy(css = "td[class*=rf-][class*=-itm-lbl]")
    private WebElement label;
    @FindBy(css = "td[class*=rf-][class*=-itm-ico]")
    private WebElement leftIcon;
    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico]")
    private WebElement rightIcon;
    @FindBy(css = "td[class*=rf-][class*=-itm-ico] img")
    private WebElement leftIconImg;
    @FindBy(css = "td[class*=rf-][class*=-itm-exp-ico] img")
    private WebElement rightIconImg;

    @Root
    private WebElement root;

    private final AdvancedPanelMenuItemInteractions advancedInteractions = new AdvancedPanelMenuItemInteractions();

    @Override
    public void select() {
        root.click();
    }

    public AdvancedPanelMenuItemInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedPanelMenuItemInteractions {

        public WebElement getLeftIconElement() {
            return leftIcon;
        }

        public WebElement getRightIconElement() {
            return rightIcon;
        }

        public WebElement getRightIconImgElement() {
            return rightIconImg;
        }

        public WebElement getLeftIconImgElement() {
            return leftIconImg;
        }

        public boolean isSelected() {
            return root.getAttribute("class").contains("-sel");
        }

        public WebElement getRootElement() {
            return root;
        }

        public boolean isDisabled() {
            return root.getAttribute("class").contains("-dis");
        }

        public boolean isTransparent(WebElement icon) {
            return icon.getAttribute("class").contains("-transparent");
        }
    }
}
