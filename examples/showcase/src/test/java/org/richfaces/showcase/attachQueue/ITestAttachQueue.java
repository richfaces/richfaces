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
package org.richfaces.showcase.attachQueue;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Ignore;
import org.junit.Test;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.attachQueue.page.AttachQueuePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@Ignore("Will be rewritten once XHRHalter lands in Graphene 2.0.4")
public class ITestAttachQueue extends AbstractWebDriverTest {

    private static final int DELAY_IN_MILISECONDS = 2000;
    private static final int NO_DELAY = 0;

    @Page
    private AttachQueuePage page;

    @Test
    public void testInput() {
        for (int i = 0; i < 5; i++) {
            typeToTheInputAndCheckTheDelay();
        }
    }

    @Test
    public void testButton() {
        for (int i = 0; i < 5; i++) {
            clickOnTheButtonAndCheckTheDelay();
        }
    }

    /*
     * types a character to the input and check whether delay after which the ajax processing is visible is between
     * DELAY_IN_MILISECONDS and DELAY_IN_MILISECONDS + 1000
     */
    private void typeToTheInputAndCheckTheDelay() {
        long timeBeforePressingKey = System.currentTimeMillis();
        page.getInput().sendKeys("a");
        waitModel(webDriver).until().element(page.getAjaxRequestProcessing()).is().visible();
        long timeAfterAjaxRequestIsPresent = System.currentTimeMillis();
        Graphene.guardAjax(page.getSubmit()).click();
        waitGui(webDriver).until().element(page.getAjaxRequestProcessing()).is().visible();
        long actualDelay = timeAfterAjaxRequestIsPresent - timeBeforePressingKey;
        assertTrue("The delay should be between " + DELAY_IN_MILISECONDS + "ms and " + (DELAY_IN_MILISECONDS + 1000)
            + "ms but was:" + actualDelay, (actualDelay >= DELAY_IN_MILISECONDS)
            && (actualDelay <= DELAY_IN_MILISECONDS + 1000));
    }

    /*
     * clicks on the button and check whether delay after which the ajax processing is visible is NO_DELAY
     */
    private void clickOnTheButtonAndCheckTheDelay() {
        long timeBeforePressingKey = System.currentTimeMillis();
        Graphene.guardAjax(page.getSubmit()).click();
        waitGui(webDriver).until().element(page.getAjaxRequestProcessing()).is().visible();
        long actualDelay = System.currentTimeMillis() - timeBeforePressingKey;
        assertTrue("The delay should be between " + NO_DELAY + "ms and " + (NO_DELAY + 500) + "ms but was:!" + actualDelay,
            (actualDelay >= NO_DELAY) && (actualDelay <= NO_DELAY + 500));
    }
}
