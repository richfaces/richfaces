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
package org.richfaces.tests.showcase.inplaceSelect;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.richfaces.tests.page.fragments.impl.input.inplace.EditingState.FinishEditingBy;
import org.richfaces.tests.page.fragments.impl.input.inplace.InplaceComponent.OpenBy;
import org.richfaces.tests.page.fragments.impl.input.inplace.select.RichFacesInplaceSelect;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.inplaceSelect.page.SimplePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ITInplaceSelect extends AbstractWebDriverTest {

    @Page
    private SimplePage page;

    /* *********************************************************************************************
     * Tests ********************************************************************* ************************
     */
    @Test
    public void testSimpleSelect() {
        for (int i = 1; i <= 5; i++) {
            checkSelect(page.simpleSelect, "Option " + i, OpenBy.CLICK);
        }
    }

    @Test
    public void testCustomizationSelect() {
        checkSelect(page.customSelect, "Alabama", OpenBy.DBLCLICK);
        checkSelect(page.customSelect, "Florida", OpenBy.DBLCLICK);
        checkSelect(page.customSelect, "California", OpenBy.DBLCLICK);
    }

    /* *********************************************************************************************************
     * Help methods **************************************************************
     * *******************************************
     */
    /**
     * Checks the select, when it is select which is activated by double click, then also there is need for click on
     * accept button.
     */
    private void checkSelect(RichFacesInplaceSelect select, String option, OpenBy by) {
        if (by.equals(OpenBy.DBLCLICK)) {
            select.editBy(by).changeToValue(option).confirm(FinishEditingBy.CONTROLS);
        } else {
            select.editBy(by).changeToValue(option).confirm();
        }
        assertEquals("The selected option is different as the select shows!", option, select.getLabelValue());
    }
}
