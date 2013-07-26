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
package org.richfaces.tests.showcase.repeat;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.repeat.page.RepeatPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITRepeat extends AbstractDataIterationWithStates {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    @Page
    private RepeatPage page;

    /* *****************************************************************************************************
     * Constants*****************************************************************************************************
     */

    private final StateWithCapitalAndTimeZone FIRST_STATE_FIRST_PAGE = new StateWithCapitalAndTimeZone("Alabama",
        "Montgomery", "GMT-6");

    private final StateWithCapitalAndTimeZone LAST_STATE_FIRST_PAGE = new StateWithCapitalAndTimeZone("Maryland",
        "Annapolis", "GMT-5");

    private final StateWithCapitalAndTimeZone FIRST_STATE_SECOND_PAGE = new StateWithCapitalAndTimeZone(
        "Massachusetts", "Boston", "GMT-5");

    private final StateWithCapitalAndTimeZone LAST_STATE_SECOND_PAGE = new StateWithCapitalAndTimeZone(
        "South Carolina", "Columbia", "GMT-5");

    private final StateWithCapitalAndTimeZone FIRST_STATE_THIRD_PAGE = new StateWithCapitalAndTimeZone("South Dakota",
        "Pierre", "GMT-6");

    private final StateWithCapitalAndTimeZone LAST_STATE_THIRD_PAGE = new StateWithCapitalAndTimeZone("Wyoming",
        "Cheyenne", "GMT-7");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testFirstStateFirstPage() {

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.firstStateHeader, page.firstStateBody);
        assertEquals("The first state on first page is not correct",actual, FIRST_STATE_FIRST_PAGE);
    }

    @Test
    public void testLastStateFirstPage() {

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.lastStateHeader, page.lastStateBody);
        assertEquals("The last state on first page is not correct", actual, LAST_STATE_FIRST_PAGE);
    }

    @Test
    public void testFirstStateSecondPage() {

        Graphene.guardAjax(page.anchorForSecondPage).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.firstStateHeader, page.firstStateBody);
        assertEquals("The first state on second page is not correct", actual, FIRST_STATE_SECOND_PAGE);
    }

    @Test
    public void testLastStateSecondPage() {

        Graphene.guardAjax(page.anchorForSecondPage).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.lastStateHeader, page.lastStateBody);
        assertEquals("The last state on second page is not correct", actual, LAST_STATE_SECOND_PAGE);
    }

    @Test
    public void testFirstStateThirdPage() {

        Graphene.guardAjax(page.anchorForThirdPage).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.firstStateHeader, page.firstStateBody);
        assertEquals("The first state on third page is not correct", actual, FIRST_STATE_THIRD_PAGE);
    }

    @Test
    public void testLastStateThirdPage() {

        Graphene.guardAjax(page.anchorForThirdPage).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.lastStateHeader, page.lastStateBody);
        assertEquals("The last state on third page is not correct", actual, LAST_STATE_THIRD_PAGE);
    }

    /* ********************************************************************************************************
     * Help methods *********************************************************************
     * ***********************************
     */

    /**
     * retrieves data about state from header and body elements of particular grid which is created via rich:repeat
     *
     * @param header
     *            there is name of State
     * @param body
     *            there is capital and timezone with particular format
     * @return
     */
    private StateWithCapitalAndTimeZone retrieveDataAboutState(WebElement header, WebElement body) {

        StateWithCapitalAndTimeZone state = new StateWithCapitalAndTimeZone();

        state.setState(header.getText());

        String capitalAndTimeZone = body.getText();

        // since capitalAndTimeZone is in format State Capital (particular capital) State TimeZone (particular time
        // zone)
        // for example State Capital Annapolis State TimeZone GMT-5
        String[] capitalAndTimeZoneSplitted = capitalAndTimeZone.split("[ \\n]");

        // capital and timezone will be always at these indices, since the samples I am testing do have capitals
        // and timezones which consist from one word only
        // NOTE it has to be changed when testing of other samples will come, samples which will have capitals or
        // timezones consisted from more than one word
        state.setCapital(capitalAndTimeZoneSplitted[2]);
        state.setTimeZone(capitalAndTimeZoneSplitted[5]);

        return state;
    }

}
