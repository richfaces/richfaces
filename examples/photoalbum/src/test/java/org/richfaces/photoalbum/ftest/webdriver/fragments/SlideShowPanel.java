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


import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.panel.TextualFragmentPart;
import org.richfaces.fragment.popupPanel.RichFacesPopupPanel;
import org.richfaces.fragment.tooltip.TextualRichFacesTooltip;
import org.richfaces.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel.Controls;
import org.richfaces.photoalbum.ftest.webdriver.fragments.SlideShowPanel.Body;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SlideShowPanel extends RichFacesPopupPanel<TextualFragmentPart, Controls, Body> {

    public void close() {
        getHeaderControlsContent().close();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void checkImagesInfoFromTooltip(String albumName, List<String> photoNames) {
        String actImgSrc;
        for (int i = 0; i < photoNames.size(); i++) {
            actImgSrc = getBodyContent().getImage().getAttribute("src");
            assertTrue(getBodyContent().getTooltipOnImage().show().getContent().getText().contains(photoNames.get(i)));
            getBodyContent().getTooltipOnImage().hide();
            if (i == (photoNames.size() - 1)) {
                advanced().waitUntilPopupIsNotVisible().withTimeout(5, TimeUnit.SECONDS).perform();
            } else {
                Graphene.waitAjax().withTimeout(5, TimeUnit.SECONDS).until().element(getBodyContent().getImage()).attribute("src").not().equalTo(actImgSrc);
            }
        }
    }

    public static class Body {

        @FindBy(tagName = "img")
        private WebElement image;
        @FindBy(tagName = "img")
        private TextualRichFacesTooltip tooltipOnImage;

        public WebElement getImage() {
            return image;
        }

        public TextualRichFacesTooltip getTooltipOnImage() {
            return tooltipOnImage;
        }
    }
}
