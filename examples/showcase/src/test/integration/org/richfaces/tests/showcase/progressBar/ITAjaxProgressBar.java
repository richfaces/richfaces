/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.progressBar;


import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;

import category.Failing;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITAjaxProgressBar extends AbstractProgressBarTest {

    private final int MAX_DEVIATION = 3;

    @FindBy(jquery = "input[type='submit']:eq(0)")
    private WebElement startButton;

    @FindBy(css = "div[class='rf-pb-rmng']")
    private WebElement progressBar;

    @Test
    @Category(Failing.class)
    public void testProgressBarIsRisingByMax3() {
        startButton.click();
        waitAjax(webDriver).until()
                .element(progressBar)
                .text()
                .contains("%");
        while(progressBar.getText().contains("%")) {
            String value = progressBar.getText();
            waitAjax().until()
                    .element(progressBar)
                    .value()
                    .not()
                    .equalTo(value);
            getTheNumberFromValueAndSaveToList(value);
        }
        checkTheDeviationInList(MAX_DEVIATION);
    }
}
