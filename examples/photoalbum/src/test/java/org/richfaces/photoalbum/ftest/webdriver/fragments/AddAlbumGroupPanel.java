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
package org.richfaces.photoalbum.ftest.webdriver.fragments;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.CheckboxInputComponentImpl;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.panel.TextualFragmentPart;
import org.richfaces.fragment.popupPanel.RichFacesPopupPanel;
import org.richfaces.photoalbum.ftest.webdriver.fragments.AddAlbumGroupPanel.Body;
import org.richfaces.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel.Controls;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AddAlbumGroupPanel extends RichFacesPopupPanel<TextualFragmentPart, Controls, Body> {

    public void addGroup(String groupName, boolean shared) {
        Body bodyContent = getBodyContent();
        bodyContent.getInput().clear().sendKeys(groupName);
        if (shared) {
            bodyContent.getShared().check();
        } else {
            bodyContent.getShared().uncheck();
        }
        Graphene.guardAjax(bodyContent.getSaveButton()).click();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void cancel() {
        getBodyContent().getCancelButton().click();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void check() {
        assertEquals("Name", getBodyContent().getNameText().getText());
        assertEquals("Shared", getBodyContent().getSharedText().getText());
        assertEquals("Add group", getHeaderContent().getText());
    }

    public void close() {
        getHeaderControlsContent().close();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public static class Body {

        @FindByJQuery("div:contains('Save') + input:visible")
        private WebElement saveButton;
        @FindByJQuery("div:contains('Cancel') + input:visible")
        private WebElement cancelButton;
        @FindByJQuery("td.name:eq(0)")
        private WebElement nameText;
        @FindByJQuery("td.name:eq(1)")
        private WebElement sharedText;
        @FindBy(css = "input[id$='shelfName']")
        private TextInputComponentImpl input;
        @FindBy(css = "input[id$='shelfShared']")
        private CheckboxInputComponentImpl shared;

        public WebElement getCancelButton() {
            return cancelButton;
        }

        public TextInputComponentImpl getInput() {
            return input;
        }

        public WebElement getNameText() {
            return nameText;
        }

        public WebElement getSaveButton() {
            return saveButton;
        }

        public CheckboxInputComponentImpl getShared() {
            return shared;
        }

        public WebElement getSharedText() {
            return sharedText;
        }
    }
}
