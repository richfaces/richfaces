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

import static org.easymock.EasyMock.expect;

import java.util.Comparator;

import javax.el.ValueExpression;

import junit.framework.Assert;

import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.model.Filter;
import org.richfaces.model.FilterField;
import org.richfaces.model.SortField;
import org.richfaces.model.SortOrder;

/**
 * @author Konstantin Mishin
 *
 */
public class UIColumnTest {
    private MockFacesEnvironment environment;
    private UIColumn column;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        environment = MockFacesEnvironment.createEnvironment();
        column = new UIColumn();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        environment.verify();
        environment.release();
        environment = null;
        column = null;
    }

    /**
     * {@link org.richfaces.component.UIColumn#getSortOrder()}.
     */
    @Test
    public final void testSortOrder() {
        Assert.assertNull(column.getSortOrder());
        column.setSortOrder(SortOrder.ascending);
        Assert.assertEquals(SortOrder.ascending, column.getSortOrder());
        environment.replay();
    }

    /**
     * Test method for {@link org.richfaces.component.UIColumn#setFilter(org.richfaces.model.Filter)} and
     * {@link org.richfaces.component.UIColumn#getFilter()}.
     */
    @Test
    public final void testFilter() {
        Assert.assertNull(column.getFilter());
        Filter<?> filter = new Filter<Object>() {
            public boolean accept(Object t) {
                return false;
            }
        };
        column.setFilter(filter);
        Assert.assertSame(filter, column.getFilter());
        environment.replay();
    }

    /**
     * Test method for {@link org.richfaces.component.UIColumn#setFilterValue(java.lang.Object)} and
     * {@link org.richfaces.component.UIColumn#getFilterValue()}.
     */
    @Test
    public final void testFilterValue() {
        Assert.assertNull(column.getFilterValue());
        Object object = new Object();
        column.setFilterValue(object);
        Assert.assertEquals(object, column.getFilterValue());
        environment.replay();
    }

    /**
     * Test method for {@link org.richfaces.component.UIColumn#setComparator(java.util.Comparator)} and
     * {@link org.richfaces.component.UIColumn#getComparator()}.
     */
    @Test
    public final void testComparator() {
        Assert.assertNull(column.getComparator());
        Comparator<?> comparator = new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        column.setComparator(comparator);
        Assert.assertSame(comparator, column.getComparator());
        environment.replay();
    }

    /**
     * Test method for {@link org.richfaces.component.UIColumn#getFilterField()}.
     */
    @Test
    public final void testGetFilterField() {
        Assert.assertNull(column.getFilterField());
        ValueExpression expression = environment.createMock(ValueExpression.class);
        expect(expression.isLiteralText()).andStubReturn(false);
        environment.replay();
        Filter<?> filter = new Filter<Object>() {
            public boolean accept(Object t) {
                return false;
            }
        };
        Object object = new Object();
        column.setValueExpression("filterExpression", expression);
        column.setFilter(filter);
        column.setFilterValue(object);
        FilterField filterField = column.getFilterField();
        Assert.assertEquals(expression, filterField.getFilterExpression());
        Assert.assertSame(filter, filterField.getFilter());
        Assert.assertEquals(object, filterField.getFilterValue());
    }

    /**
     * Test method for {@link org.richfaces.component.UIColumn#getSortField()}.
     */
    @Test
    public final void testGetSortField() {
        Assert.assertNull(column.getSortField());
        ValueExpression expression = environment.createMock(ValueExpression.class);
        expect(expression.isLiteralText()).andStubReturn(false);
        environment.replay();
        Comparator<?> comparator = new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        column.setSortOrder(SortOrder.ascending);
        column.setValueExpression("sortBy", expression);
        column.setComparator(comparator);
        SortField sortField = column.getSortField();
        Assert.assertEquals(SortOrder.ascending, sortField.getSortOrder());
        Assert.assertEquals(expression, sortField.getSortBy());
        Assert.assertSame(comparator, sortField.getComparator());
    }
}
