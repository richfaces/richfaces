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

import static java.lang.String.format;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.WebElementUtils;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.AdvancedInteractions;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesContextMenu extends AbstractPopupMenu implements PopupMenu, AdvancedInteractions<AbstractPopupMenu.AdvancedPopupMenuInteractions> {

    private static final String MENU_SELECTOR_TEMPLATE = "div[id='%s_list'].rf-ctx-lst";
    private final AdvancedContextMenuInteractions advancedInteractions = new AdvancedContextMenuInteractions();

    @ArquillianResource
    private WebDriver browser;

    @FindByJQuery("script:last")
    private WebElement script;

    @Override
    public AdvancedContextMenuInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedContextMenuInteractions extends AbstractPopupMenu.AdvancedPopupMenuInteractions {

        private String menuId;
        private MenuPopup popupFragment;

        public String getLangAttribute() {
            return getPopupFragment().getPopupElement().getAttribute("lang");
        }

        protected String getMenuId() {
            if (menuId == null) {
                menuId = getRootElement().getAttribute("id");
            }
            return menuId;
        }

        @Override
        public List<WebElement> getMenuItemElements() {
            return getPopupFragment().getMenuItemsElements();
        }

        @Override
        public WebElement getMenuPopup() {
            return getPopupFragment().getPopupElement();
        }

        protected MenuPopup getPopupFragment() {
            if (popupFragment == null) {
                popupFragment = Graphene.createPageFragment(MenuPopup.class,
                    WebElementUtils.findElementLazily(By.cssSelector(format(MENU_SELECTOR_TEMPLATE, getMenuId())), browser));
            }
            return popupFragment;
        }

        @Override
        protected WebElement getScriptElement() {
            return script;
        }
    }
}
