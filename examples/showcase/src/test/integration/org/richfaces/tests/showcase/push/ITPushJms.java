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
package org.richfaces.tests.showcase.push;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.poll.AbstractPollTest;

import category.Smoke;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITPushJms extends AbstractPollTest {

    @FindBy(jquery = "table tbody:visible:last")
    private WebElement serverDate;

    @Test
    public void testDeviationInServerDate() {
        checkDeviationInServerDate(20);
    }
    
    @Test
    @Category(Smoke.class)
    public void testDeviationInServerDateQuick() {
        checkDeviationInServerDate(1);
    }
    
    private void checkDeviationInServerDate(int howManyPushes) {
        List<Integer> deviations = new ArrayList<Integer>();
        String date = serverDate.getText();
        Graphene.waitAjax(webDriver).withTimeout(30, TimeUnit.SECONDS)
                .until()
                .element(serverDate)
                .text()
                .not()
                .equalTo(date);
        Integer deviation = null;
        for (int i = 0; i < howManyPushes; i++) {
            deviation = waitForServerActionAndReturnDeviation(serverDate, "push");
            deviations.add(deviation);
        }
        Collections.sort(deviations);
        assertEquals("Median of push deviations is wrong!", deviations.get(howManyPushes > 1 ? howManyPushes / 2 : 0).intValue(), 5);
    }

}
