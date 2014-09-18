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
package org.richfaces.fragment.editor.toolbar;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesEditorToolbar implements EditorToolbar {

    @Drone
    private WebDriver browser;

    @Override
    public WebElement getButton(EditorButton whichButton) {
        String className = whichButton.getCSSClassName();
        return browser.findElement(By.className(className));
    }

    @Override
    public int numberOfToolbarItems() {
        List<WebElement> toolbarItems= browser.findElements(By.className("cke_toolbar"));
        return toolbarItems.size();
    }

    @Override
    public boolean isBasic() {
        if (numberOfToolbarItems() == 1) {
            return true;
        }else {
         return false;
        }
    }

    @Override
    public boolean isAdvanced() {
        if (numberOfToolbarItems() == 11) {
            return true;
        }else {
         return false;
        }
    }
}
