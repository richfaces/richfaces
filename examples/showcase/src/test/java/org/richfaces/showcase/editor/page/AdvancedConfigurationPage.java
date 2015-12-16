/*
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
 */
package org.richfaces.showcase.editor.page;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.editor.RichFacesEditor;

public class AdvancedConfigurationPage {

    public static final String NEW_PAGE_DE = "Neue Seite";
    public static final String NEW_PAGE_ENG = "New Page";
    public static final String NEW_PAGE_FR = "Nouvelle page";

    @FindBy(className = "rf-ed")
    private RichFacesEditor editor;

    @FindByJQuery("input[type=radio]:eq(0)")
    private WebElement englishRadio;

    @FindByJQuery("input[type=radio]:eq(1)")
    private WebElement frenchRadio;

    @FindByJQuery("input[type=radio]:eq(2)")
    private WebElement germanRadio;

    @FindByJQuery(".cke_button__newpage")
    private WebElement newPageButton;

    public RichFacesEditor getEditor() {
        return editor;
    }

    public WebElement getNewPageButton() {
        return newPageButton;
    }

    private boolean isRadionButtonChecked(WebElement radionButton) {
        String attribute = String.valueOf(radionButton.getAttribute("checked")).trim();
        return attribute.equals("checked") || attribute.equals("true");
    }

    public void switchToEditorLanguage(Lang l) {
        WebElement button;
        switch (l) {
            case DE:
                button = germanRadio;
                break;
            case EN:
                button = englishRadio;
                break;
            case FR:
                button = frenchRadio;
                break;
            default:
                throw new UnsupportedOperationException("Unknown language " + l);
        }
        if (!isRadionButtonChecked(button)) {
            guardAjax(button).click();
        }
    }

    public static enum Lang {

        EN, FR, DE
    }
}
