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
package org.richfaces.showcase.list;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.component.ListType;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.list.page.ListsPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestLists extends AbstractWebDriverTest {

    @Page
    private ListsPage page;

    /* ***********************************************************************************************
     * Tests***********************************************************************************************
     */
    @Test
    public void testDefinitionsList() {
        checkList(ListType.definitions);
    }

    @Test
    public void testOrderedList() {
        checkList(ListType.ordered);
    }

    @Test
    public void testUnorderedList() {
        checkList(ListType.unordered);
    }

    /* ********************************************************************************************************************
     * Help methods ******************************************************************************************************
     * **************
     */

    private void checkList(ListType type) {
        page.setType(type);
        String tag = page.getList().getRootElement().getTagName();
        ListType actual = null;
        if (tag.equals("dl")) {
            actual = ListType.definitions;
        } else if (tag.equals("ol")) {
            actual = ListType.ordered;
        } else if (tag.equals("ul")) {
            actual = ListType.unordered;
        }
        assertEquals("Unknown list type with root tag " + tag, type, actual);
    }
}
