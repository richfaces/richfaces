/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;

import org.jboss.test.faces.AbstractFacesTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Konstantin Mishin
 *
 */
public class ArrangeableModelTest extends AbstractFacesTest {
    public class User {
        private String fname;
        private String lname;

        public User(String fname, String lname) {
            super();
            this.fname = fname;
            this.lname = lname;
        }

        public String getFname() {
            return fname;
        }

        public String getLname() {
            return lname;
        }
    }

    private static final int ROWS = 2;
    private static final int ROW_KEY = 4;
    private static final List<Integer> FILTERD_AND_SORTED_ROW_KEYS = Arrays.asList(5, 3, 2, 0);
    private User[] users = { new User("C", "A"), new User("a", "a"), new User("B", "B"), new User("B", "C"),
            new User("b", "b"), new User("A", "A") };
    private ExtendedDataModel<User> extendedDataModel;
    private ArrangeableModel arrangeableModel;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
        extendedDataModel = new SequenceDataModel<User>(new ArrayDataModel<User>(users));
        arrangeableModel = new ArrangeableModel(extendedDataModel, "var", "filterVar");
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        FilterField[] filterFields = {
                new FilterField(null, new Filter<User>() {
                    public boolean accept(User t) {
                        return t.getFname().indexOf('a') == -1;
                    }
                }, null),
                new FilterField(expressionFactory.createValueExpression(elContext, "#{var.lname != filterVar}", Object.class),
                    null, "b") };
        SortField[] sortFields = {
                new SortField(expressionFactory.createValueExpression(elContext, "#{var.fname}", Object.class), null,
                    SortOrder.ascending), new SortField(null, new Comparator<User>() {
                    public int compare(User o1, User o2) {
                        return o1.getLname().compareTo(o2.getLname());
                    }
                }, SortOrder.descending) };
        arrangeableModel.arrange(facesContext,
            new ArrangeableStateDefaultImpl(Arrays.asList(filterFields), Arrays.asList(sortFields), null));
    }

    @After
    public void tearDown() throws Exception {
        extendedDataModel = null;
        arrangeableModel = null;
        super.tearDown();
    }

    /**
     * Test method for {@link org.richfaces.model.ArrangeableModel#isRowAvailable()}.
     */
    @Test
    public void testIsRowAvailable() {
        arrangeableModel.setRowKey(null);
        Assert.assertFalse(extendedDataModel.isRowAvailable());
        arrangeableModel.setRowKey(ROW_KEY);
        Assert.assertTrue(extendedDataModel.isRowAvailable());
        arrangeableModel.setRowKey(extendedDataModel.getRowCount());
        Assert.assertFalse(extendedDataModel.isRowAvailable());
    }

    /**
     * Test method for {@link org.richfaces.model.ArrangeableModel#getRowCount()}.
     */
    @Test
    public void testGetRowCount() {
        Assert.assertEquals(ROW_KEY, arrangeableModel.getRowCount());
    }

    /**
     * Test method for {@link org.richfaces.model.ArrangeableModel#setRowIndex(int)} and
     * {@link org.richfaces.model.ArrangeableModel#getRowIndex()}.
     */
    @Test
    public void testRowIndex() {
        arrangeableModel.setRowIndex(-1);
        Assert.assertNull(extendedDataModel.getRowKey());
        Assert.assertEquals(arrangeableModel.getRowIndex(), -1);
        arrangeableModel.setRowIndex(arrangeableModel.getRowCount() - 1);
        Assert.assertEquals(0, extendedDataModel.getRowKey());
        Assert.assertEquals(arrangeableModel.getRowIndex(), arrangeableModel.getRowCount() - 1);
        arrangeableModel.setRowIndex(extendedDataModel.getRowCount());
        Assert.assertNull(extendedDataModel.getRowKey());
        Assert.assertEquals(arrangeableModel.getRowIndex(), -1);
    }

    /**
     * Test method for {@link org.richfaces.model.ArrangeableModel#setRowKey(java.lang.Object)} and
     * {@link org.richfaces.model.ArrangeableModel#getRowKey()}.
     */
    @Test
    public void testRowKey() {
        arrangeableModel.setRowKey(null);
        Assert.assertNull(extendedDataModel.getRowKey());
        arrangeableModel.setRowKey(ROW_KEY);
        Assert.assertEquals(ROW_KEY, extendedDataModel.getRowKey());
        Assert.assertEquals(ROW_KEY, arrangeableModel.getRowKey());
        arrangeableModel.setRowKey(extendedDataModel.getRowCount());
        Assert.assertEquals(extendedDataModel.getRowCount(), extendedDataModel.getRowKey());
    }

    /**
     * Test method for
     * {@link org.richfaces.model.ArrangeableModel#walk(javax.faces.context.FacesContext, DataVisitor, Range, java.lang.Object)}
     * .
     */
    @Test
    public void testWalk() {
        final List<Object> rowKeys = new ArrayList<Object>();
        DataVisitor visitor = new DataVisitor() {
            public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                arrangeableModel.setRowKey(rowKey);
                if (arrangeableModel.isRowAvailable()) {
                    rowKeys.add(rowKey);
                }
                return DataVisitResult.CONTINUE;
            }
        };
        arrangeableModel.walk(facesContext, visitor, new SequenceRange(0, -1), null);
        Assert.assertEquals(FILTERD_AND_SORTED_ROW_KEYS, rowKeys);
        rowKeys.clear();
        arrangeableModel.walk(facesContext, visitor, new SequenceRange(0, ROWS), null);
        Assert.assertEquals(FILTERD_AND_SORTED_ROW_KEYS.subList(0, ROWS), rowKeys);
    }

    /**
     * Test method for {@link org.richfaces.model.ArrangeableModel#addDataModelListener(javax.faces.model.DataModelListener)}
     * {@link org.richfaces.model.ArrangeableModel#removeDataModelListener(javax.faces.model.DataModelListener)} and
     * {@link org.richfaces.model.ArrangeableModel#getDataModelListeners()}.
     */
    @Test
    public void testDataModelListener() {
        Assert.assertEquals(0, extendedDataModel.getDataModelListeners().length);
        DataModelListener listener = new DataModelListener() {
            public void rowSelected(DataModelEvent event) {

            }
        };
        arrangeableModel.addDataModelListener(listener);
        Assert.assertSame(listener, extendedDataModel.getDataModelListeners()[0]);
        Assert.assertSame(listener, arrangeableModel.getDataModelListeners()[0]);
        arrangeableModel.removeDataModelListener(listener);
        Assert.assertEquals(0, extendedDataModel.getDataModelListeners().length);
    }

    /**
     * Test method for {@link org.richfaces.model.ArrangeableModel#getRowData()}.
     */
    @Test
    public void testGetRowData() {
        boolean caught = false;
        arrangeableModel.setRowKey(null);
        try {
            arrangeableModel.getRowData();
        } catch (IllegalArgumentException e) {
            caught = true;
        } finally {
            Assert.assertTrue(caught);
        }
        arrangeableModel.setRowKey(ROW_KEY);
        Assert.assertEquals(arrangeableModel.getRowData(), extendedDataModel.getRowData());
        caught = false;
        arrangeableModel.setRowKey(extendedDataModel.getRowCount());
        try {
            arrangeableModel.getRowData();
        } catch (IllegalArgumentException e) {
            caught = true;
        } finally {
            Assert.assertTrue(caught);
        }
    }

    /**
     * Test method for {@link org.richfaces.model.ArrangeableModel#setWrappedData(java.lang.Object)} and
     * {@link org.richfaces.model.ArrangeableModel#getWrappedData()}.
     */
    @Test
    public void testWrappedData() {
        Assert.assertSame(users, arrangeableModel.getWrappedData());
        Object[] objects = new Object[0];
        arrangeableModel.setWrappedData(objects);
        Assert.assertSame(objects, arrangeableModel.getWrappedData());
    }
}
