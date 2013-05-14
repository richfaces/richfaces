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
package org.richfaces.tests.showcase.dataTable;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.dataTable.page.SimpleTablePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITSimpleTable extends AbstractWebDriverTest {

    @Page
    private SimpleTablePage page;

    @Test
    public void testInit() {
        assertEquals("$224.00", page.getSanJoseHotels());
        assertEquals("$65.02", page.getSanJoseMeals());
        assertEquals("$379.02", page.getSanJoseSubtotals());
        assertEquals("$90.00", page.getSanJoseTransport());

        assertEquals("$218.00", page.getSeattleHotels());
        assertEquals("$131.25", page.getSeattleMeals());
        assertEquals("$72.00", page.getSeattleTransport());
        assertEquals("$421.25", page.getSeattleSubtotals());

        assertEquals("$442.00", page.getTotalsOfHotels());
        assertEquals("$196.27", page.getTotalsOfMeals());
        assertEquals("$162.00", page.getTotalsOfTransport());
        assertEquals("$800.27", page.getTotalsOfSubtotals());
    }

}
