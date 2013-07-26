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
package org.richfaces.tests.showcase.param;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.param.page.ClientParamPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITClientParam extends AbstractWebDriverTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    @Page
    private ClientParamPage page;

    @JavaScript
    private Screen screen;

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testShowScreenSizeAtInitialState() {

        String actualString = page.widthValueLocator.getText().trim();
        assertEquals("The value of width should be empty string!", actualString, "");

        actualString = page.heightValueLocator.getText().trim();
        assertEquals("The value of height should be empty string!", actualString, "");
    }

    @Test
    public void testShowScreenSizeAfterClickingOnButton() {

        Graphene.guardAjax(page.buttonShowScreenSize).click();

        long widthActual = Long.parseLong(page.widthValueLocator.getText().trim());
        long heightActual = Long.parseLong(page.heightValueLocator.getText().trim());

        long widthExpected = screen.getWidth();
        long heightExpected = screen.getHeight();

        assertEquals("The width returned from website can not be "
            + "different from width returned from this code", widthActual, widthExpected);
        assertEquals("The height returned from website can not be "
            + "different from height returned from this code", heightActual, heightExpected);
    }


    @JavaScript("window.screen")
    public interface Screen {
        Long getHeight();
        Long getWidth();
    }
}
