/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.editor;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.editor.page.ToolbarAndSkinningPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestToolbarAndSkinning extends AbstractWebDriverTest {

    protected final int BASIC_ED_BUTTONS = 6;
    protected final int CUSTOM_ED_BUTTONS = 35;
    protected final int FULL_ED_BUTTONS = 60;

    @Page
    private ToolbarAndSkinningPage page;

    private void clickAndCheckButtonsSize(WebElement button, int size) {
        if (!isRadionButtonChecked(button)) {
            guardAjax(button).click();
        }
        assertEquals("The number of buttons is incorrect!", size, page.getButtonsOfEditor().size());
    }

    private boolean isRadionButtonChecked(WebElement radionButton) {
        String attribute = String.valueOf(radionButton.getAttribute("checked")).trim();
        return attribute.equals("checked") || attribute.equals("true");
    }

    @Test
    public void testNumberOfButtonsBasicEditor() {
        clickAndCheckButtonsSize(page.getBasicEditorCheckbox(), BASIC_ED_BUTTONS);
    }

    @Test
    public void testNumberOfButtonsCustomEditor() {
        clickAndCheckButtonsSize(page.getCustomEditorCheckbox(), CUSTOM_ED_BUTTONS);
    }

    @Test
    public void testNumberOfButtonsFullEditor() {
        clickAndCheckButtonsSize(page.getFullEditorCheckbox(), FULL_ED_BUTTONS);
    }
}
