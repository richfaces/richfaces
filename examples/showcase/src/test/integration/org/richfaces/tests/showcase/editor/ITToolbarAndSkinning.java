/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************
 */
package org.richfaces.tests.showcase.editor;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.editor.page.ToolbarAndSkinningPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITToolbarAndSkinning extends AbstractWebDriverTest {

    protected final int BASIC_NUMBERS = 28; // there are four skins therefore 4 * 7 buttons
    protected final int FULL_NUMBERS = 83; // the same
    protected final int CUSTOM_NUMBERS = 56; // the same

    @Page
    private ToolbarAndSkinningPage page;

    @Test
    public void testNumberOfButtonsBasicEditor() {
        clickAndCheckButtonsSize(page.basicEditorCheckbox, BASIC_NUMBERS);
    }

    @Test
    public void testNumberOfButtonsFullEditor() {
        clickAndCheckButtonsSize(page.fullEditorCheckbox, FULL_NUMBERS);
    }

    @Test
    public void testNumberOfButtonsCustomEditor() {
        clickAndCheckButtonsSize(page.customEditorCheckbox, CUSTOM_NUMBERS);
    }

    private void clickAndCheckButtonsSize(WebElement button, int size) {
        guardAjax(button).click();
        assertEquals("The number of buttons is incorrect!", page.buttonsOfEditor.size(), size);
    }
}
