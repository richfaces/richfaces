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
package org.richfaces.showcase.jquery;

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestJQuery extends AbstractWebDriverTest {

    private final int NUMBER_OF_IMGS = 9;
    private final String WIDTH_OF_IMG_BEFORE_HOVER = "120";
    private final String WIDTH_OF_IMG_AFTER_HOVER = "180";

    @ArquillianResource
    private Actions actions;

    @Test
    public void testImagesAreBecomingBiggerAfterHover() {
        for (int i = 0; i < NUMBER_OF_IMGS; i++) {
            WebElement image = webDriver.findElement(ByJQuery.selector("span[id$='gallery'] img:eq(" + i + ")"));
            assertEquals("The initial width is wrong", WIDTH_OF_IMG_BEFORE_HOVER, getWidthOfImage(image));

            actions.moveToElement(image).build().perform();
            waitModel().until("The width of image after hovering is wrong!").element(image).attribute("width")
                .equalTo(WIDTH_OF_IMG_AFTER_HOVER);
        }
    }

    /**
     * Gets the width of image element without the border
     * 
     * @param img the particular image which width will be returned
     * @return the width of image with width of image's borders
     */
    private String getWidthOfImage(WebElement element) {
        return element.getAttribute("width");
    }

}
