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
package org.richfaces.tests.showcase.notify;

import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.tests.page.fragments.impl.message.Message;
import org.richfaces.tests.page.fragments.impl.notify.NotifyMessage;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.notify.page.MessagesPage;

import category.Failing;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITNotifyMessages extends AbstractWebDriverTest {

    @Page
    private MessagesPage page;

    /* ***************************************************************************
     * Tests ********************************************************************* ******
     */
    @Test
    @Category(Failing.class) //not stable, need to stabilize it
    public void testAllInputsIncorrectValuesBlurActivation() {
        checkNotifyMessages(false);
    }

    @Test
    public void testAllInputsIncorrectValuesSubmitActivation() {
        checkNotifyMessages(true);
    }

    /* ********************************************************************************************************************
     * Help methods **************************************************************
     * ******************************************************
     */

    protected void checkNotifyMessages(boolean submitActivation) {
        page.fillCorrectValues();
        page.validate();
        page.waitUntilThereIsNoNotify();

        page.fillShorterValues();
        page.blur();
        if (submitActivation) {
            page.waitUntilThereIsNoNotify();
            page.validate();
        }

        int numberOfNotifyMessages = page.getNotify().size();
        if (submitActivation) {
            Assert.assertEquals("There should be 4 notify messages!", numberOfNotifyMessages, 4);
        } else {
            Assert.assertTrue("There should be 4 notify messages!", numberOfNotifyMessages >= 4);
        }

        if (submitActivation) {
            assertErrorIsPresent("Name", MessagesPage.NAME_ERROR_LESS_THAN_MINIMUM);
            assertErrorIsPresent("Job", MessagesPage.JOB_ERROR_LESS_THAN_MINIMUM);
            assertErrorIsPresent("Zip", MessagesPage.ZIP_ERROR_LESS_THAN_MINIMUM);
            assertErrorIsPresent("Address", MessagesPage.ADDRESS_ERROR_LESS_THAN_MINIMUM);
        } else {
            assertErrorIsPresent("Name", "Name");
            assertErrorIsPresent("Job", "Job");
            assertErrorIsPresent("Zip", "Zip");
            assertErrorIsPresent("Address", "Address");
        }
    }

    protected void assertErrorIsPresent(String fieldName, String expected) {
        List<NotifyMessage> messages = page.getNotify().getAllMessagesOfType(Message.MessageType.ERROR);
        for (Message message : messages) {
            if (message.getSummary().contains(expected)) {
                return;
            }
        }
        Assert.fail("There is no message with summary '" + expected + "' for field " + fieldName + ".");
    }

}
