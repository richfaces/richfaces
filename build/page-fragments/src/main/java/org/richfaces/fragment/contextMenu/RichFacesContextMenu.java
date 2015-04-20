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
package org.richfaces.fragment.contextMenu;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author pan
 */
public class RichFacesContextMenu extends AbstractPopupMenu implements PopupMenu,
    AdvancedInteractions<AbstractPopupMenu.AdvancedPopupMenuInteractions> {

    @FindBy(className = "rf-ctx-itm")
    private List<WebElement> menuItemsElements;

    @FindBy(css = "div.rf-ctx-lst")
    private WebElement contextMenuPopup;

    @FindByJQuery("script:last")
    private WebElement script;

    private final AdvancedContextMenuInteractions advancedInteractions = new AdvancedContextMenuInteractions();

    /**
     * Selects an item of the pop-up menu already shown.
     *
     * @param picker the item picker
     */
    public void selectVisibleItem(ChoicePicker picker) {
        WebElement item = picker.pick(advanced().getMenuItemElements());
        if (item == null) {
            throw new IllegalArgumentException("There is no such option to be selected, which satisfied the given rules!");
        }
        item.click();
    }

    public void selectVisibleItem(String header) {
        selectVisibleItem(ChoicePickerHelper.byVisibleText().match(header));
    }

    public void selectVisibleItem(int index) {
        selectVisibleItem(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public AdvancedContextMenuInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedContextMenuInteractions extends AbstractPopupMenu.AdvancedPopupMenuInteractions {

        @Override
        public List<WebElement> getMenuItemElements() {
            return menuItemsElements;
        }

        @Override
        public WebElement getMenuPopup() {
            return contextMenuPopup;
        }

        @Override
        protected WebElement getScriptElement() {
            return script;
        }

        public String getLangAttribute() {
            return getRootElement().getAttribute("lang");
        }
    }
}
