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
package org.richfaces.showcase.notify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.fragment.message.Message;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.notify.page.MessagesPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestNotifyMessages extends AbstractWebDriverTest {

    @Page
    private MessagesPage page;

    /* ***************************************************************************
     * Tests ********************************************************************* ******
     */
    @Test
    public void testAllInputsIncorrectValuesBlurActivation() {
        page.fillCorrectValues();
        page.validate();
        page.waitUntilThereIsNoNotify();

        //test one by one all fields and assert, always wait for previous notification to fade away
        page.fillShorterValueName();
        page.blur();
        assertErrorIsPresent("Name", "Name");
        page.waitUntilThereIsNoNotify();

        page.fillShorterValueJob();
        page.blur();
        assertErrorIsPresent("Job", "Job");
        page.waitUntilThereIsNoNotify();

        page.fillShorterValueZip();
        page.blur();
        assertErrorIsPresent("Zip", "Zip");
        page.waitUntilThereIsNoNotify();

        page.fillShorterValueAddress();
        page.blur();
        assertErrorIsPresent("Address", "Address");
        page.waitUntilThereIsNoNotify();
    }

    @Test
    public void testAllInputsIncorrectValuesSubmitActivation() {
        page.fillCorrectValues();
        page.validate();
        page.waitUntilThereIsNoNotify();

        page.fillShorterValues();
        page.blur();
        page.waitUntilThereIsNoNotify();
        page.validate();
        page.getNotify().advanced().waitUntilMessagesAreVisible().perform();

        int numberOfNotifyMessages = page.getNotify().size();
        assertEquals("There should be 4 notify messages!", 4, numberOfNotifyMessages);

        assertErrorIsPresent("Name", MessagesPage.NAME_ERROR_LESS_THAN_MINIMUM);
        assertErrorIsPresent("Job", MessagesPage.JOB_ERROR_LESS_THAN_MINIMUM);
        assertErrorIsPresent("Zip", MessagesPage.ZIP_ERROR_LESS_THAN_MINIMUM);
        assertErrorIsPresent("Address", MessagesPage.ADDRESS_ERROR_LESS_THAN_MINIMUM);

        page.waitUntilThereIsNoNotify();
    }

    /* ********************************************************************************************************************
     * Help methods **************************************************************
     * ******************************************************
     */
    protected void assertErrorIsPresent(String fieldName, String expected) {
        List<? extends Message> messages = page.getNotify().getItems(Message.MessageType.ERROR);
        for (Message message : messages) {
            if (message.getSummary().contains(expected)) {
                return;
            }
        }
        fail("There is no message with summary '" + expected + "' for field " + fieldName + ".");
    }

}
