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
package org.richfaces.showcase.push;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestPushTopicsContext extends AbstractWebDriverTest {

    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w+)-(\\w+)-(\\w+)-(\\w+)-(\\w+)");

    @FindBy(css = "div[id$='uuid']")
    private WebElement uuidElement;

    @Test
    public void testUuidIsChangingInSomeIntervals() {
        final int checks = 10;
        final int halfChecks = checks / 2;
        String uuid = uuidElement.getText();
        checkTheUuid(uuid);
        List<Long> deviations = new ArrayList<Long>(checks);
        for (int i = 0; i < checks; i++) {
            Long deviation = checkDeviation();
            deviations.add(deviation);
        }
        Collections.sort(deviations);
        long median = checks % 2 == 0
            ? (deviations.get(halfChecks - 1) + deviations.get(halfChecks)) / 2
            : deviations.get(halfChecks);
        assertEquals("The median of " + checks + " measurements should be in range of (4600ms, 5400ms)", 5000, median, 400);
    }

    /**
     * Checks the deviation between two pushes, also checking that the uuid is changing
     * 
     * @param uuidRetriever retriever which points to the uuid text
     * @return the deviation between two pushes
     */
    private Long checkDeviation() {
        Long beforePush = System.currentTimeMillis();
        String uuidBefore = uuidElement.getText();
        Graphene.waitAjax(webDriver).withTimeout(10, TimeUnit.SECONDS).until().element(uuidElement).text().not()
            .equalTo(uuidBefore);
        Long afterPush = System.currentTimeMillis();
        checkTheUuid(uuidElement.getText());
        return afterPush - beforePush;
    }

    /**
     * Check very simply whether uuid is correct, it means whether it has 36 characters and that contains 4 hyphens
     */
    private void checkTheUuid(String uuid) {
        assertEquals("The length of uuid is wrong!", 36, uuid.length());
        assertTrue("Wrong uuid, there should be 4 hyphens", UUID_PATTERN.matcher(uuid).matches());
    }
}
