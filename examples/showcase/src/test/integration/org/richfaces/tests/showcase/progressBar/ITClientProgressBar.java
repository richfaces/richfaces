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
package org.richfaces.tests.showcase.progressBar;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.junit.Test;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITClientProgressBar extends AbstractProgressBarTest {

    private static final int MAX_DEVIATION = runInPortalEnv ? 5000 : 2500;
    private static final int GRAPHICAL_DEVIATION = 5;
    private static final String STYLE_NAME = Attribute.STYLE.getAttributeName();
    private static final String START_WIDTH = "width: 0%;";
    private static final String END_WIDTH = "width: 100%;";

    @FindBy(jquery = "input[type='button']:eq(0)")
    private WebElement startButton;

    @FindBy(css = "div[class='rf-pb-prgs']")
    private WebElement progressBar;

    @Test
    public void testClientProgressBarIsRisingGraphically() {
        startButton.click();
        String width = START_WIDTH;
        int counter = 1;
        while (!width.equals(END_WIDTH)) {
            long currentTimeBeforeChange = System.currentTimeMillis();
            waitGui(webDriver).withTimeout(20, TimeUnit.SECONDS).until().element(progressBar).attribute(STYLE_NAME).not()
                .equalTo(width);
            width = progressBar.getAttribute(STYLE_NAME);

            long currentTimeAfterChange = System.currentTimeMillis();
            long duration = currentTimeAfterChange - currentTimeBeforeChange;
            // because the second update of the progress bar takes more, it is application specific
            if (counter != 2) {
                assertTrue("The graphical rising of progress bar should not take more than " + MAX_DEVIATION + ", and "
                    + "was " + duration, duration < MAX_DEVIATION);
            }
            getTheNumberFromValueAndSaveToList(width.split(" ")[1]);
            counter++;
        }
        checkTheDeviationInList(GRAPHICAL_DEVIATION);
    }
}
