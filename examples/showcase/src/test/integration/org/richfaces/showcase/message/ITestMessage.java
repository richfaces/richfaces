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
package org.richfaces.showcase.message;

import category.Smoke;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.fragment.message.Message;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.message.page.MessagePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestMessage extends AbstractWebDriverTest {

    @Page
    private MessagePage page;

    @Test
    public void testCorrectValues() {
        page.fillCorrectValues();
        page.validate();
        page.getMessageForJob().advanced().waitUntilMessageIsNotVisible().perform();
        assertFalse("Unexpected message is present.", page.isAnyMessagePresent());
    }

    @Test
    public void testLessThanMinimum() {
        page.fillShorterValues();
        page.validate();
        page.getMessageForJob().advanced().waitUntilMessageIsVisible().perform();
        assertTrue("All message should be present.", page.areAllMessagesPresent());

        assertMessageDetail("Name", page.getMessageForName(), MessagePage.NAME_ERROR_LESS_THAN_MINIMUM);
        assertMessageDetail("Job", page.getMessageForJob(), MessagePage.JOB_ERROR_LESS_THAN_MINIMUM);
        assertMessageDetail("Address", page.getMessageForAddress(), MessagePage.ADDRESS_ERROR_LESS_THAN_MINIMUM);
        assertMessageDetail("Zip", page.getMessageForZip(), MessagePage.ZIP_ERROR_LESS_THAN_MINIMUM);
    }

    @Test
    @Category(Smoke.class)
    public void testEmptyInputs() {
        page.eraseAll();
        page.validate();
        page.getMessageForJob().advanced().waitUntilMessageIsVisible().perform();
        assertTrue("All message should be present.", page.areAllMessagesPresent());

        assertMessageDetail("Name", page.getMessageForName(), MessagePage.NAME_ERROR_VALUE_REQUIRED);
        assertMessageDetail("Job", page.getMessageForJob(), MessagePage.JOB_ERROR__VALUE_REQUIRED);
        assertMessageDetail("Address", page.getMessageForAddress(), MessagePage.ADDRESS_ERROR__VALUE_REQUIRED);
        assertMessageDetail("Zip", page.getMessageForZip(), MessagePage.ZIP_ERROR__VALUE_REQUIRED);

    }

    @Test
    public void testGreaterThanMaximum() {
        page.fillCorrectValues();
        page.fillLongerJob();
        page.validate();
        page.getMessageForJob().advanced().waitUntilMessageIsVisible().perform();
        assertTrue("The message for the Job input should be present.", page.getMessageForJob().advanced().isVisible());
        assertMessageDetail("Job", page.getMessageForJob(), MessagePage.JOB_ERROR_GREATER_THAN_MAXIMUM);

        for (Message message : new Message[] { page.getMessageForAddress(), page.getMessageForName(), page.getMessageForZip() }) {
            assertFalse("Unexpected message is present.", message.advanced().isVisible());
        }

        page.fillLongerZip();
        page.validate();
        page.getMessageForZip().advanced().waitUntilMessageIsVisible().perform();
        assertTrue("A message for the Job input should be present.", page.getMessageForJob().advanced().isVisible());
        assertMessageDetail("Job", page.getMessageForJob(), MessagePage.JOB_ERROR_GREATER_THAN_MAXIMUM);

        assertTrue("A message for the Zip input should be present.", page.getMessageForZip().advanced().isVisible());
        assertMessageDetail("Zip", page.getMessageForZip(), MessagePage.ZIP_ERROR_GREATER_THAN_MAXIMUM);

        for (Message message : new Message[] { page.getMessageForAddress(), page.getMessageForName() }) {
            assertFalse("Unexpected message is present.", message.advanced().isVisible());
        }
    }

    protected void assertMessageDetail(String fieldName, Message message, String expected) {
        assertTrue(
            "The message detail for '" + fieldName + "' should contain '" + expected + "', but it is '" + message.getDetail()
                + "'", message.getDetail().contains(expected));
    }

}
