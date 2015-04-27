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
package org.richfaces.showcase.status;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.status.page.TestSimplePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestSimple extends TestUsage {

    @Page
    private TestSimplePage page;

    @Test
    public void testUserNameAndAjaxRequestProgress() {
        page.getUserNameInput().sendKeys("a");
        assertProgressPictureAppearsOnAjaxRequest(page.getProgressImage());
    }

    @Test
    public void testAddressAndAjaxRequestProgress() {
        page.getAddressInput().sendKeys("a");
        assertProgressPictureAppearsOnAjaxRequest(page.getProgressImage());
    }

    @Test
    public void testSubmitButtonAndAjaxRequestProgress() {
        page.getUserNameInput().sendKeys("a");
        page.getSubmitButton().click();
        assertProgressPictureAppearsOnAjaxRequest(page.getProgressImage());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("There should appear notification that user stored successfully!", "User a stored successfully",
            page.getOutput().getText());
    }
}
