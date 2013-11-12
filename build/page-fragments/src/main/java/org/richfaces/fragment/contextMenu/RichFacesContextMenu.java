/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.fragment.contextMenu;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesContextMenu extends AbstractPopupMenu implements PopupMenu {

    @FindBy(className = "rf-ctx-itm")
    private List<WebElement> menuItemsElements;

    @FindBy(css = "div.rf-ctx-lst")
    private WebElement contextMenuPopup;

    @FindByJQuery("script:last")
    private WebElement script;

    private final AdvancedContextMenuInteractions advancedInteractions = new AdvancedContextMenuInteractions();

    @Override
    public AdvancedContextMenuInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    protected List<WebElement> getMenuItemElementsInternal() {
        return menuItemsElements;
    }

    @Override
    protected WebElement getMenuPopupInternal() {
        return contextMenuPopup;
    }

    @Override
    protected WebElement getScriptElement() {
        return script;
    }

    public class AdvancedContextMenuInteractions extends AbstractPopupMenu.AdvancedPopupMenuInteractions {

        public String getLangAttribute() {
            return getRootElement().getAttribute("lang");
        }
    }
}