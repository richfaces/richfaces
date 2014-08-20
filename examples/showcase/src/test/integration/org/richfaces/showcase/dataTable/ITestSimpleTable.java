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
package org.richfaces.showcase.dataTable;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.dataTable.page.SimpleTablePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestSimpleTable extends AbstractWebDriverTest {

    @Page
    private SimpleTablePage page;

    @Test
    public void testInit() {
        assertEquals(page.getSanJoseHotels(), "$224.00");
        assertEquals(page.getSanJoseMeals(), "$65.02");
        assertEquals(page.getSanJoseSubtotals(), "$379.02");
        assertEquals(page.getSanJoseTransport(), "$90.00");

        assertEquals(page.getSeattleHotels(), "$218.00");
        assertEquals(page.getSeattleMeals(), "$131.25");
        assertEquals(page.getSeattleTransport(), "$72.00");
        assertEquals(page.getSeattleSubtotals(), "$421.25");

        assertEquals(page.getTotalsOfHotels(), "$442.00");
        assertEquals(page.getTotalsOfMeals(), "$196.27");
        assertEquals(page.getTotalsOfTransport(), "$162.00");
        assertEquals(page.getTotalsOfSubtotals(), "$800.27");
    }

}
