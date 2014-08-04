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

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.status.page.TestViewUsagePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestViewUsage extends TestUsage {

    @Page
    private TestViewUsagePage page;

    @Test
    public void testUserNameAndImagePresentionOfAjaxProgress() {
        page.userNameInput.sendKeys("a");
        assertProgressPictureAppearsOnAjaxRequest(page.progressImage);
    }

    @Test
    public void testAddressAndImagePresentionOfAjaxProgress() {
        page.addressInput.sendKeys("a");
        assertProgressPictureAppearsOnAjaxRequest(page.progressImage);
    }

    @Test
    public void testSumbitForUserDetailsAndImagePresentionOfAjaxProgress() {
        page.submitButton.click();
        assertProgressPictureAppearsOnAjaxRequest(page.progressImage);
    }

    @Test
    public void testSubmitForSearchPanelAndImagePresentionOfAjaxProgress() {
        page.searchButton.click();
        assertProgressPictureAppearsOnAjaxRequest(page.progressImage);
    }

}
