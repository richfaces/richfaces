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
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.inplaceSelect.InplaceSelect;
import org.richfaces.fragment.panel.TextualFragmentPart;
import org.richfaces.fragment.popupPanel.RichFacesPopupPanel;
import org.richfaces.photoalbum.ftest.webdriver.fragments.AddAlbumPanel.Body;
import org.richfaces.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel.Controls;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AddAlbumPanel extends RichFacesPopupPanel<TextualFragmentPart, Controls, Body> {

    public void addAlbum(String group, String albumName) {
        Body bodyContent = getBodyContent();
        bodyContent.getGroupSelect().select(group).confirm();
        bodyContent.getAlbumNameInput().clear().sendKeys(albumName);
        Graphene.guardAjax(bodyContent.getSaveButton()).click();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void cancel() {
        getBodyContent().getCancelButton().click();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void check() {
        assertEquals("Name", getBodyContent().getNameText().getText());
        assertEquals("Group", getBodyContent().getGroupNameText().getText());
        assertEquals("Add album", getHeaderContent().getText());
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
        private WebElement groupNameText;
        @FindByJQuery("td.name:eq(1)")
        private WebElement nameText;
        @FindBy(css = "input[id$='albumName']")
        private TextInputComponentImpl albumNameInput;
        @FindBy(css = ".rf-is")
        private RFInplaceSelect groupSelect;

        public TextInputComponentImpl getAlbumNameInput() {
            return albumNameInput;
        }

        public WebElement getCancelButton() {
            return cancelButton;
        }

        public WebElement getNameText() {
            return nameText;
        }

        public WebElement getSaveButton() {
            return saveButton;
        }

        public WebElement getGroupNameText() {
            return groupNameText;
        }

        public InplaceSelect getGroupSelect() {
            return groupSelect;
        }
    }
}
