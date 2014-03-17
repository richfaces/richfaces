/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.component;

import javax.faces.context.FacesContext;

import org.richfaces.model.DataVisitResult;
import org.richfaces.model.DataVisitor;
import org.richfaces.model.SequenceRange;
import org.jboss.test.faces.AbstractFacesTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Konstantin Mishin
 *
 */
public class UIExtendedDataTableTest extends AbstractFacesTest {
    private UIExtendedDataTable table;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
        table = new UIExtendedDataTable();
        table.setValue(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        table = null;
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.richfaces.component.UIExtendedDataTable#visitDataChildren(javax .faces.component.visit.VisitContext, javax.faces.component.visit.VisitCallback, boolean)}
     * .
     */
    @Test
    public final void testVisitDataChildren() {
        // TODO fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.richfaces.component.UIExtendedDataTable#getActualFirst()}.
     */
    @Test
    public final void testGetActualFirst() {
        assertEquals(0, table.getActualFirst());
        table.setFirst(5);
        table.setClientFirst(3);
        assertEquals(8, table.getActualFirst());
    }

    /**
     * Test method for {@link org.richfaces.component.UIExtendedDataTable#getActualRows()}.
     */
    @Test
    public final void testGetActualRows() {
        assertEquals(0, table.getActualRows());
        table.setRows(5);
        assertEquals(5, table.getActualRows());
        table.setClientRows(3);
        assertEquals(3, table.getActualRows());
        table.setClientRows(8);
        assertEquals(5, table.getActualRows());
    }

    /**
     * Test method for {@link org.richfaces.component.UIExtendedDataTable#setFirst(int)}.
     */
    @Test
    public final void testSetFirst() {
        table.setClientFirst(3);
        assertEquals(3, table.getClientFirst());
        table.setFirst(3);
        assertEquals(0, table.getClientFirst());
    }

    /**
     * Test method for
     * {@link org.richfaces.component.UIExtendedDataTable#walk(javax.faces.context.FacesContext, org.richfaces.model.DataVisitor, org.richfaces.model.Range, java.lang.Object)}
     * .
     */
    @Test
    public final void testWalkFacesContextDataVisitorRangeObject() {
        final StringBuilder builder = new StringBuilder(5);
        table.walk(facesContext, new DataVisitor() {
            public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                table.setRowKey(rowKey);
                builder.append(table.getRowData());
                return DataVisitResult.CONTINUE;
            }
        }, new SequenceRange(2, 5), null);
        assertEquals("23456", builder.toString());
    }

    /**
     * Test method for {@link org.richfaces.component.UIExtendedDataTable#getClientFirst()} and
     * {@link org.richfaces.component.UIExtendedDataTable#setClientFirst(int)}.
     */
    @Test
    public final void testClientFirst() {
        assertEquals(0, table.getClientFirst());
        table.setClientFirst(3);
        assertEquals(3, table.getClientFirst());
    }

    /**
     * Test method for {@link org.richfaces.component.UIExtendedDataTable#getClientRows()} and
     * {@link org.richfaces.component.UIExtendedDataTable#setClientRows(int)}.
     */
    @Test
    public final void testClientRows() {
        assertEquals(0, table.getClientRows());
        table.setClientRows(3);
        assertEquals(3, table.getClientRows());
    }
}
