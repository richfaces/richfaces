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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.AbstractFacesTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.model.Arrangeable;
import org.richfaces.model.ArrangeableState;
import org.richfaces.model.DataVisitor;
import org.richfaces.model.ExtendedDataModel;
import org.richfaces.model.FilterField;
import org.richfaces.model.Range;
import org.richfaces.model.SortField;
import org.richfaces.model.SortMode;
import org.richfaces.model.SortOrder;

/**
 * @author Konstantin Mishin
 *
 */
public class UIDataTableTest extends AbstractFacesTest {
    private class MockArrangeableModel extends ExtendedDataModel<Object> implements Arrangeable {
        private ArrangeableState state;

        @Override
        public Object getRowKey() {
            return null;
        }

        @Override
        public void setRowKey(Object key) {
        }

        @Override
        public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        }

        @Override
        public int getRowCount() {
            return 0;
        }

        @Override
        public Object getRowData() {
            return null;
        }

        @Override
        public int getRowIndex() {
            return 0;
        }

        @Override
        public Object getWrappedData() {
            return null;
        }

        @Override
        public boolean isRowAvailable() {
            return false;
        }

        @Override
        public void setRowIndex(int rowIndex) {
        }

        @Override
        public void setWrappedData(Object data) {
        }

        public void arrange(FacesContext context, ArrangeableState state) {
            this.state = state;
        }

        public ArrangeableState getState() {
            return state;
        }
    }

    private UIDataTable table = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
        table = new UIDataTable();
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
     * Test method for {@link org.richfaces.component.UIDataTableBase#getRendersChildren()}.
     */
    @Test
    public void testGetRendersChildren() {
        Assert.assertTrue(table.getRendersChildren());
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#dataChildren()}.
     */
    @Test
    public void testDataChildren() {
        Assert.assertTrue(table.dataChildren() instanceof DataTableDataChildrenIterator);
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#fixedChildren()}.
     */
    @Test
    public void testFixedChildren() {
        Assert.assertTrue(table.fixedChildren() instanceof DataTableFixedChildrenIterator);
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#createExtendedDataModel()}.
     */
    @Test
    public void testCreateExtendedDataModel() {
        Assert.assertFalse(table.createExtendedDataModel() instanceof Arrangeable);
        List<Object> sortPriority = Arrays.<Object>asList("id2", "id0", "id1");
        List<UIComponent> children = table.getChildren();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        UIColumn column = new UIColumn();
        column.setRendered(false);
        children.add(column);
        for (int i = 0; i < sortPriority.size(); i++) {
            UIColumn child = new UIColumn();
            child.setId("id" + i);
            child.setValueExpression("filterExpression",
                expressionFactory.createValueExpression(elContext, "#{'id" + i + "'}", Object.class));
            child.setValueExpression("sortBy",
                expressionFactory.createValueExpression(elContext, "#{'id" + i + "'}", Object.class));
            child.setSortOrder(SortOrder.ascending);
            children.add(child);
        }
        Assert.assertTrue(table.createExtendedDataModel() instanceof Arrangeable);
        MockArrangeableModel model = new MockArrangeableModel();
        table.setValue(model);
        table.setSortPriority(sortPriority);
        Assert.assertSame(model, table.createExtendedDataModel());
        ArrangeableState state = model.getState();
        List<FilterField> filterFields = state.getFilterFields();
        for (int i = 0; i < sortPriority.size(); i++) {
            Assert.assertEquals("id" + i, filterFields.get(i).getFilterExpression().getValue(elContext));
        }
        List<SortField> sortFields = state.getSortFields();
        for (int i = 0; i < sortPriority.size(); i++) {
            Assert.assertEquals(sortPriority.get(i), sortFields.get(i).getSortBy().getValue(elContext));
        }
        Assert.assertEquals(facesContext.getViewRoot().getLocale(), state.getLocale());
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#columns()}.
     */
    @Test
    public void testColumns() {
        Assert.assertTrue(table.columns() instanceof DataTableColumnsIterator);
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#getHeader()}.
     */
    @Test
    public void testGetHeader() {
        UIOutput component = new UIOutput();
        table.getFacets().put("header", component);
        Assert.assertSame(component, table.getHeader());
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#getFooter()}.
     */
    @Test
    public void testGetFooter() {
        UIOutput component = new UIOutput();
        table.getFacets().put("footer", component);
        Assert.assertSame(component, table.getFooter());
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#getFilterVar()} and
     * {@link org.richfaces.component.UIDataTableBase#setFilterVar(java.lang.String)}.
     */
    @Test
    public void testFilterVar() {
        String string = "fv";
        table.setFilterVar(string);
        Assert.assertEquals(string, table.getFilterVar());
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#getSortPriority()} and
     * {@link org.richfaces.component.UIDataTableBase#setSortPriority(java.util.Collection)}.
     */
    @Test
    public void testSortPriority() {
        table.setSortPriority(Collections.emptyList());
        Assert.assertEquals(0, table.getSortPriority().size());
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#getSortMode()} and
     * {@link org.richfaces.component.UIDataTableBase#setSortMode(org.richfaces.model.SortMode)}.
     */
    @Test
    public void testSortMode() {
        table.setSortMode(SortMode.multi);
        Assert.assertEquals(SortMode.multi, table.getSortMode());
    }

    /**
     * Test method for {@link org.richfaces.component.UIDataTableBase#isColumnFacetPresent(java.lang.String)}.
     */
    @Test
    public void testIsColumnFacetPresent() {
        String facetName = "header";
        Assert.assertFalse(table.isColumnFacetPresent(facetName));
        UIColumn child = new UIColumn();
        List<UIComponent> children = table.getChildren();
        children.add(new UIColumn());
        children.add(child);
        Assert.assertFalse(table.isColumnFacetPresent(facetName));
        child.getFacets().put(facetName, new UIOutput());
        Assert.assertTrue(table.isColumnFacetPresent(facetName));
        child.setRendered(false);
        Assert.assertFalse(table.isColumnFacetPresent(facetName));
    }
}
