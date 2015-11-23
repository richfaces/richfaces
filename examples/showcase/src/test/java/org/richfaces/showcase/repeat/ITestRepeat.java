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
package org.richfaces.showcase.repeat;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.repeat.page.RepeatPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestRepeat extends AbstractDataIterationWithStates {

    private final StateWithCapitalAndTimeZone FIRST_STATE_FIRST_PAGE = new StateWithCapitalAndTimeZone("Alabama", "Montgomery",
        "GMT-6");
    private final StateWithCapitalAndTimeZone FIRST_STATE_LAST_PAGE = new StateWithCapitalAndTimeZone("Virginia", "Richmond",
        "GMT-5");
    private final StateWithCapitalAndTimeZone FIRST_STATE_SECOND_PAGE = new StateWithCapitalAndTimeZone("Kansas", "Topeka",
        "GMT-6");
    private final StateWithCapitalAndTimeZone FIRST_STATE_THIRD_PAGE = new StateWithCapitalAndTimeZone("New Mexico", "Santa Fe",
        "GMT-7");
    private final StateWithCapitalAndTimeZone LAST_STATE_FIRST_PAGE = new StateWithCapitalAndTimeZone("Iowa", "Des Moines",
        "GMT-6");
    private final StateWithCapitalAndTimeZone LAST_STATE_LAST_PAGE = new StateWithCapitalAndTimeZone("Wyoming", "Cheyenne",
        "GMT-7");
    private final StateWithCapitalAndTimeZone LAST_STATE_SECOND_PAGE = new StateWithCapitalAndTimeZone("New Jersey", "Trenton",
        "GMT-5");
    private final StateWithCapitalAndTimeZone LAST_STATE_THIRD_PAGE = new StateWithCapitalAndTimeZone("Vermont", "Montpelier",
        "GMT-5");

    @Page
    private RepeatPage page;

    /**
     * retrieves data about state from header and body elements of particular grid which is created via rich:repeat
     *
     * @param header there is name of State
     * @param body there is capital and timezone with particular format
     * @return
     */
    private StateWithCapitalAndTimeZone retrieveDataAboutState(WebElement header, WebElement capital, WebElement timeZone) {
        StateWithCapitalAndTimeZone state = new StateWithCapitalAndTimeZone();

        state.setState(header.getText());
        state.setCapital(capital.getText());
        state.setTimeZone(timeZone.getText());

        return state;
    }

    @Test
    public void testFirstStateFirstPage() {
        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getFirstStateHeader(), page.getFirstStateCapital(),
            page.getFirstStateTimeZone());
        assertEquals("The first state on first page is not correct", FIRST_STATE_FIRST_PAGE, actual);
    }

    @Test
    public void testFirstStateLastPage() {
        Graphene.guardAjax(page.getAnchorForLastPage()).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getFirstStateHeader(), page.getFirstStateCapital(),
            page.getFirstStateTimeZone());
        assertEquals("The first state on last page is not correct", FIRST_STATE_LAST_PAGE, actual);
    }

    @Test
    public void testFirstStateSecondPage() {
        Graphene.guardAjax(page.getAnchorForSecondPage()).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getFirstStateHeader(), page.getFirstStateCapital(),
            page.getFirstStateTimeZone());
        assertEquals("The first state on second page is not correct", FIRST_STATE_SECOND_PAGE, actual);
    }

    @Test
    public void testFirstStateThirdPage() {
        Graphene.guardAjax(page.getAnchorForThirdPage()).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getFirstStateHeader(), page.getFirstStateCapital(),
            page.getFirstStateTimeZone());
        assertEquals("The first state on third page is not correct", FIRST_STATE_THIRD_PAGE, actual);
    }

    @Test
    public void testLastStateFirstPage() {
        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getLastStateHeader(), page.getLastStateCapital(),
            page.getLastStateTimeZone());
        assertEquals("The last state on first page is not correct", LAST_STATE_FIRST_PAGE, actual);
    }

    @Test
    public void testLastStateLastPage() {
        Graphene.guardAjax(page.getAnchorForLastPage()).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getLastStateHeader(), page.getLastStateCapital(),
            page.getLastStateTimeZone());
        assertEquals("The last state on last page is not correct", LAST_STATE_LAST_PAGE, actual);
    }

    @Test
    public void testLastStateSecondPage() {
        Graphene.guardAjax(page.getAnchorForSecondPage()).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getLastStateHeader(), page.getLastStateCapital(),
            page.getLastStateTimeZone());
        assertEquals("The last state on second page is not correct", LAST_STATE_SECOND_PAGE, actual);
    }

    @Test
    public void testLastStateThirdPage() {
        Graphene.guardAjax(page.getAnchorForThirdPage()).click();

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(page.getLastStateHeader(), page.getLastStateCapital(),
            page.getLastStateTimeZone());
        assertEquals("The last state on third page is not correct", LAST_STATE_THIRD_PAGE, actual);
    }
}
