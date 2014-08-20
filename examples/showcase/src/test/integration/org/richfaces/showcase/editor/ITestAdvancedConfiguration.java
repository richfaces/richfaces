/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.editor;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.editor.page.AdvancedConfigurationPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Lukas Fryc and Juraj Huska</a>
 */
public class ITestAdvancedConfiguration extends AbstractWebDriverTest {

    @Page
    private AdvancedConfigurationPage page;

    @Test
    public void testEnglishLanguage() {

        guardAjax(page.getEnglishRadio()).click();

        String titleOfNewPageButton = page.getNewPageButton().getAttribute("title");
        assertEquals("The language was not changed to english!", AdvancedConfigurationPage.NEW_PAGE_ENG, titleOfNewPageButton);
    }

    @Test
    public void testFrenchLanguage() {
        guardAjax(page.getFrenchRadio()).click();

        String titleOfNewPageButton = page.getNewPageButton().getAttribute("title");
        assertEquals("The language was not changed to french!", AdvancedConfigurationPage.NEW_PAGE_FR, titleOfNewPageButton);
    }

    @Test
    public void testGermanLanguage() {
        guardAjax(page.getGermanRadio()).click();

        String titleOfNewPageButton = page.getNewPageButton().getAttribute("title");
        assertEquals("The language was not changed to german!", AdvancedConfigurationPage.NEW_PAGE_DE, titleOfNewPageButton);
    }

    @Test
    public void testUserFocusAutomaticallyOnEditor() {
        try {
            webDriver.switchTo().frame(0);
            WebElement activeArea = webDriver.findElement(By.tagName("body"));

            String expected = "Test String";
            activeArea.sendKeys(expected);
            assertTrue("The text should be writen to editor without explicit previos focusing!",
                activeArea.getText().contains(expected));
        } finally {
            webDriver.switchTo().defaultContent();
        }
    }
}
