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
package org.richfaces.showcase.fileUpload;

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.fileUpload.page.FileUploadPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestFileUpload extends AbstractWebDriverTest {

    private final String MSG_NO_FILES = "No files currently uploaded";

    @Page
    private FileUploadPage page;

    @Test
    public void testThereAreAllRequiredElements() {
        waitModel().until().element(page.getAddButton()).is().present();
        assertTrue("The add button should be there!", page.getAddButton().isDisplayed());
        assertTrue("The upload area should be there!", page.getUploadArea().isDisplayed());
        assertTrue("The upload files info should be there!", page.getUploadFilesInfo().isDisplayed());
        assertTrue("The div with uplad files messages should be there!", page.getDivWithUploadFilesMessage().isDisplayed());
        assertEquals("The message that no files is currently uploaded should be there!", MSG_NO_FILES,
            page.getDivWithUploadFilesMessage().getText());

    }

}
