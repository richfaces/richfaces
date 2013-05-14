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
package org.richfaces.tests.showcase.inplaceInput;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.tests.page.fragments.impl.input.inplace.InplaceComponent.OpenBy;
import org.richfaces.tests.page.fragments.impl.input.inplace.input.RichFacesInplaceInput;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.inplaceInput.page.SimplePage;

import category.NoPhantomJS;

/**
 * Test case for page faces/components/richInplaceInput/simple.xhtml.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision: 22196 $
 */
public class ITInplaceInput extends AbstractWebDriverTest {

    @Page
    private SimplePage page;

    /* ********************************************************************************
     * Tests********************************************************************************
     */
    @Test
    @Category(NoPhantomJS.class)
    public void testEnterSomethingToNameInput() {
        enterSomethingToInputAndCheck(page.name);
    }

    @Test
    @Category(NoPhantomJS.class)
    public void testEnterSomethingToEmail() {
        enterSomethingToInputAndCheck(page.email);
    }

    /* ***********************************************************************************
     * Help methods***********************************************************************************
     */
    private void enterSomethingToInputAndCheck(RichFacesInplaceInput input) {
        String expectedString = "Test string";
        input.editBy(OpenBy.CLICK).changeToValue(expectedString).confirm();

        String actualString = input.getLabelValue();

        assertEquals("The value in the input is not what have been typed there!", expectedString, actualString);
    }
}
