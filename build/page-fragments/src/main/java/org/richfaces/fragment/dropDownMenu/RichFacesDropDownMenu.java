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
package org.richfaces.fragment.dropDownMenu;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.contextMenu.AbstractPopupMenu;
import org.richfaces.fragment.contextMenu.PopupMenu;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesDropDownMenu extends AbstractPopupMenu implements PopupMenu {

    @FindBy(className = "rf-ddm-itm")
    private List<WebElement> menuItemsElements;

    @FindByJQuery(".rf-ddm-lst:eq(0)")
    private WebElement dropDownMenuPopup;

    @FindByJQuery(".rf-ddm-lbl script:last")
    private WebElement script;

    @FindByJQuery(".rf-ddm-lbl:eq(0)")
    private WebElement topLvlElement;

    private final AdvancedDropDownMenuInteractions advancedInteractions = new AdvancedDropDownMenuInteractions();

    @Override
    public AdvancedDropDownMenuInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedDropDownMenuInteractions extends AbstractPopupMenu.AdvancedPopupMenuInteractions {

        @Override
        public WebElement getMenuPopup() {
            return dropDownMenuPopup;
        }

        @Override
        public List<WebElement> getMenuItemElements() {
            return menuItemsElements;
        }

        @Override
        protected WebElement getScriptElement() {
            return script;
        }

        private final Event DEFAULT_INVOKE_EVENT = Event.MOUSEOVER;
        private Event invokeEvent = DEFAULT_INVOKE_EVENT;

        public String getLangAttribute() {
            return getTopLevelElement().getAttribute("lang");
        }

        public WebElement getTopLevelElement() {
            return topLvlElement;
        }

        @Override
        protected Event getDefaultShowEvent() {
            return DEFAULT_INVOKE_EVENT;
        }

        @Override
        public void setShowEvent(Event showEvent) {
            this.invokeEvent = showEvent;
        }

        @Override
        protected Event getShowEvent() {
            return invokeEvent;
        }

    }
}
