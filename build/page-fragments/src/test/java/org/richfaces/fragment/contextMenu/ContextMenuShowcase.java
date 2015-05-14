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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

public class ContextMenuShowcase {

    @FindBy
    private RichFacesContextMenu contextMenu;

    public void showcase_context_menu() {
        contextMenu.selectItem("Open");

        guardAjax(contextMenu).selectItem(6);

        contextMenu.selectItem(ChoicePickerHelper.byVisibleText().startsWith("Clo"));

        contextMenu.expandGroup("sub menu").expandGroup("sub sub menu").selectItem("sub sub menu item");
    }
}
