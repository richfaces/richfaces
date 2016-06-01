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
package org.richfaces.showcase.focus;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.focus.page.FocusDelayedPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestFocusDelayed extends AbstractWebDriverTest {

    @Page
    private FocusDelayedPage page;

    @Test
    public void testDelayedFocusOnNameInput() {
        for (int i = 0; i < 3; i++) {
            page.showPopup();
            page.waitForFocusIsGiven(page.getNameInput().advanced().getInputElement());

            page.cancelPopup();
        }
    }

    @Test
    public void testDelayedFocusOnJobWhenItDoesNotPassValidation() {
        page.showPopup();

        page.getNameInput().sendKeys("RichFaces");
        page.getJobInput().sendKeys("aa");

        Graphene.guardAjax(page.getSaveButton()).click();
        page.waitForFocusIsGiven(page.getJobInput().advanced().getInputElement());

        page.cancelPopup();

        page.showPopup();
        page.waitForFocusIsGiven(page.getJobInput().advanced().getInputElement());
    }
}
