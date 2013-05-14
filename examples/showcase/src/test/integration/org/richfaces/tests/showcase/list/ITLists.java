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
package org.richfaces.tests.showcase.list;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Assert;
import org.junit.Test;
import org.richfaces.tests.page.fragments.impl.list.simple.SimpleList.ListType;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.list.page.ListsPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITLists extends AbstractWebDriverTest {

    @Page
    private ListsPage page;

    /* ***********************************************************************************************
     * Tests***********************************************************************************************
     */

    @Test
    public void testOrderedList() {
        checkList(ListType.ORDERED);
    }

    @Test
    public void testUnorderedList() {
        checkList(ListType.UNORDERED);
    }

    @Test
    public void testDefinitionsList() {
        checkList(ListType.DEFINITIONS);
    }

    /* ********************************************************************************************************************
     * Help methods
     * ******************************************************************************************************
     * **************
     */

    private void checkList(ListType type) {
        page.setType(type);
        Assert.assertEquals(type, page.list.getType());
    }
}
