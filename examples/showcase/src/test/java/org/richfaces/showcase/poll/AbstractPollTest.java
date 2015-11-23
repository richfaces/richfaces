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
package org.richfaces.showcase.poll;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertTrue;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class AbstractPollTest extends AbstractWebDriverTest {

    /**
     * Initialize GregorianCalendar with time which is give from dateRetriever
     * 
     * @param dateRetriever TextRetriever from which the calendar with specific time will be inicialized
     */
    public GregorianCalendar initializeCalendarFromDateRetriever(String date) {
        String[] serverDateParsed = date.split(":");
        String hours = serverDateParsed[1].substring(serverDateParsed[1].length() - 2, serverDateParsed[1].length());
        String minutes = serverDateParsed[2];
        String seconds = serverDateParsed[3].substring(0, 2);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(Time.valueOf(hours.trim() + ":" + minutes + ":" + seconds));
        return calendar;

    }

    /**
     * Computes deviation between two times, consider possibility of minute changing
     * 
     * @param calendarInitial the calendar with specific time which was before calendarAfterPool
     * @param calendarAfterServerAction the calendar with specific time which was after calendarInitial
     */
    public Integer computeDeviation(GregorianCalendar calendarInitial, GregorianCalendar calendarAfterServerAction) {
        int secondsInitial = calendarInitial.get(Calendar.SECOND);
        int secondsAfterServerAction = calendarAfterServerAction.get(Calendar.SECOND);
        // if there is more than one minute or one hour or deviation is return
        // error value -1
        if (secondsAfterServerAction == secondsInitial) {
            return -1;
        } else if ((calendarAfterServerAction.get(Calendar.MINUTE) - calendarInitial.get(Calendar.MINUTE)) >= 2) {
            return -1;
        } else if ((calendarAfterServerAction.get(Calendar.HOUR) - calendarInitial.get(Calendar.HOUR)) >= 2) {
            return -1;
        }
        int deviation = -1;
        if (secondsAfterServerAction < secondsInitial) {
            deviation = secondsAfterServerAction + (60 - secondsInitial);
        } else {
            deviation = secondsAfterServerAction - secondsInitial;
        }
        return deviation;
    }

    /**
     * Waits for particular server action, gets the deviation between two states(before particular server action and after),
     * checks that the deviation is not zero or bigger than one hour or one minute
     * 
     * @param dateRetriever retriever which points to the server date
     * @return deviation between two states of rendered server date(before particular server action and after)
     */
    public Integer waitForServerActionAndReturnDeviation(WebElement dateElement, String whatServerAction) {
        String date = dateElement.getText();
        GregorianCalendar calendarInitial = initializeCalendarFromDateRetriever(date);

        waitAjax(webDriver).withTimeout(30, TimeUnit.SECONDS).until().element(dateElement).text().not().equalTo(date);

        GregorianCalendar calendarAfterPush = initializeCalendarFromDateRetriever(dateElement.getText());
        assertTrue("The time after " + whatServerAction + "is before the initial time! You are returning to the past!",
            calendarAfterPush.after(calendarInitial));
        Integer deviation = computeDeviation(calendarInitial, calendarAfterPush);
        assertTrue("Deviaton: " + deviation + " between two " + whatServerAction + "s/es is either too "
            + "big (more than one minute/hour) or too small(zero)", deviation > 0);
        return deviation;
    }
}
