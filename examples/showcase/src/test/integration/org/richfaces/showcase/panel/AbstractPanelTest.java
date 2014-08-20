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
package org.richfaces.showcase.panel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractPanelTest extends AbstractWebDriverTest {

    protected final String RICH_FACES_INFO =
            "RichFaces is a component library for JSF and an advanced framework for easily integrating AJAX capabilities into business applications."
        + "\n100+ AJAX enabled components in two libraries"
        + "\na4j: page centric AJAX controls"
        + "\nrich: self contained, ready to use components"
        + "\nWhole set of JSF benefits while working with AJAX"
        + "\nSkinnability mechanism"
        + "\nComponent Development Kit (CDK)"
        + "\nDynamic resources handling"
        + "\nTesting facilities for components, actions, listeners, and pages"
        + "\nBroad cross-browser support"
        + "\nLarge and active community";

    protected final String RICH_FACES_JSF_INFO = "We are working hard on RichFaces 4.0 which will have full JSF 2 integration. "
        + "That is not all though, here is a summary of updates and features:";

    protected final String HEADER = "div[id*='header']";
    protected final String BODY = "div[id*='body']";

    /**
     * Checks whether the particular part of panel is not empty and whether contains particular piece of text
     *
     * @param whichPart
     *            the string representation of the JQLocator for panel's body
     * @param expectedContent
     *            the content which should be in the particular part of output panel
     */
    protected void checkContentOfPanel(WebElement panel, String expectedContent) {
        assertFalse("The content of the panel should not be empty",panel.getText().isEmpty());
        assertTrue("The content is different",panel.getText().contains(expectedContent));
    }

    protected void checkContentOfPanel(String panelLocator, String expectedContent) {
        WebElement panel = webDriver.findElement(ByJQuery.selector(panelLocator));
        assertFalse("The content of the panel should not be empty",panel.getText().isEmpty());
        assertTrue("The content is different", panel.getText().contains(expectedContent));
    }

}
