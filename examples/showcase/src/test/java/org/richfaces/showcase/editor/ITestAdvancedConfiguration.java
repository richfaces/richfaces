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

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Keyboard;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.editor.page.AdvancedConfigurationPage;
import org.richfaces.showcase.editor.page.AdvancedConfigurationPage.Lang;
import org.richfaces.utils.focus.ElementIsFocused;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestAdvancedConfiguration extends AbstractWebDriverTest {

    @ArquillianResource
    private Keyboard keyboard;

    @Page
    private AdvancedConfigurationPage page;

    private String getNewPageButtonTitle() {
        return page.getNewPageButton().getAttribute("title");
    }

    @Test
    public void testEnglishLanguage() {
        page.switchToEditorLanguage(Lang.EN);
        assertEquals("The language was not changed to english!", AdvancedConfigurationPage.NEW_PAGE_ENG, getNewPageButtonTitle());
    }

    @Test
    public void testFrenchLanguage() {
        page.switchToEditorLanguage(Lang.FR);
        assertEquals("The language was not changed to french!", AdvancedConfigurationPage.NEW_PAGE_FR, getNewPageButtonTitle());
    }

    @Test
    public void testGermanLanguage() {
        page.switchToEditorLanguage(Lang.DE);
        assertEquals("The language was not changed to german!", AdvancedConfigurationPage.NEW_PAGE_DE, getNewPageButtonTitle());
    }

    @Test
    public void testUserFocusAutomaticallyOnEditor() {
        // workaround for Selenium bug https://code.google.com/p/selenium/issues/detail?id=7937
        // focus on some input on the page, so the Keyboard will not be focused in URL bar of the browser
        // this will also reload the editor, so the focus on editor will be re-applied
        page.switchToEditorLanguage(Lang.EN);

        // wait until the focus is applied
        waitForEditorIsFocused();

        final String expected = "Test String";
        // write some text
        keyboard.sendKeys(expected);
        // check it was written to editor, which had the focus
        assertEquals("The text should be writen to editor without explicit previous focusing!", expected, page.getEditor().getText());
    }

    private void waitForEditorIsFocused() {
        final WebElement editorFrameElement = page.getEditor().advanced().getRootElement().findElement(By.tagName("iframe"));
        webDriver.switchTo().frame(editorFrameElement);
        Graphene.waitAjax().until(new ElementIsFocused(null));
        webDriver.switchTo().defaultContent();
    }
}
