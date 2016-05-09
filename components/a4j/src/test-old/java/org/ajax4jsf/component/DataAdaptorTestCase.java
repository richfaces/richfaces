/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.ajax4jsf.component;

import java.util.ArrayList;

import javax.faces.component.UIColumn;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.ajax4jsf.tests.MockComponentState;
import org.ajax4jsf.tests.MockDataModel;
import org.ajax4jsf.tests.MockUIInputRenderer;
import org.richfaces.model.DataComponentState;
import org.richfaces.model.ExtendedDataModel;

/**
 * @author shura
 *
 */
public class DataAdaptorTestCase extends AbstractAjax4JsfTestCase {
    private UIDataAdaptor adaptor;
    private UIInput child;
    private UIInput childChild;
    private UIInput childChildFacet;
    private int childChildFacetInvoked;
    private int childChildInvoked;
    private int childInvoked;
    UIData data;
    private UIInput facetChild;
    private int facetInvoked;

    /**
     * @param name
     */
    public DataAdaptorTestCase(String name) {
        super(name);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();

        // Create mock DataAdaptor and childs.
        adaptor = new MockDataAdaptor();
        child = new UIInput() {
            public void processDecodes(FacesContext context) {
                childInvoked++;
                super.processDecodes(context);
            }
        };
        childInvoked = 0;
        child.setId("child");
        adaptor.getChildren().add(child);
        facetChild = new UIInput() {
            public void processDecodes(FacesContext context) {
                facetInvoked++;
                super.processDecodes(context);
            }
        };
        facetInvoked = 0;
        facetChild.setId("facetChild");
        adaptor.getFacets().put("facet", facetChild);
        childChild = new UIInput() {
            public void processDecodes(FacesContext context) {
                childChildInvoked++;
                super.processDecodes(context);
            }
        };

        childChildInvoked = 0;
        childChild.setId("childChild");
        child.getChildren().add(childChild);
        childChildFacet = new UIInput() {
            public void processDecodes(FacesContext context) {
                childChildFacetInvoked++;
                super.processDecodes(context);
            }
        };

        childChildFacetInvoked = 0;
        childChildFacet.setId("childChildFacet");
        childChild.getFacets().put("facet", childChildFacet);
        data = new UIData();
        renderKit.addRenderer(child.getFamily(), child.getRendererType(), new MockUIInputRenderer());
        renderKit.addRenderer(adaptor.getFamily(), adaptor.getRendererType(), new MockUIInputRenderer());
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
        adaptor = null;
        child = null;
        childChild = null;
        childChildFacet = null;
        facetChild = null;
    }

    private void createTree() {
        facesContext.getViewRoot().getChildren().add(adaptor);
        adaptor.setId("adaptor");
    }

    private void createDataTree() {
        data.setId("data");
        adaptor.setId("adaptor");

        ArrayList value = new ArrayList(2);

        value.add("first");
        value.add("second");
        data.setValue(value);
        data.setVar("var");

        UIColumn column = new UIColumn();

        data.getChildren().add(column);
        column.getChildren().add(adaptor);
        facesContext.getViewRoot().getChildren().add(data);
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#isRowAvailable()}.
     */
    public void testIsRowAvailable() {
        adaptor.setRowKey(new Integer(0));
        assertTrue(adaptor.isRowAvailable());
        adaptor.setRowKey(new Integer(MockDataModel.ROWS - 1));
        assertTrue(adaptor.isRowAvailable());
        adaptor.setRowKey(new Integer(MockDataModel.ROWS + 1));
        assertFalse(adaptor.isRowAvailable());
        adaptor.setRowKey(null);
        assertFalse(adaptor.isRowAvailable());
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#getRowKey()}.
     */
    public void testGetRowKey() {
        adaptor.setRowIndex(0);
        assertEquals(new Integer(0), adaptor.getRowKey());
        adaptor.setRowIndex(-1);
        assertNull(adaptor.getRowKey());
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#setRowKey(java.lang.Object)}.
     */
    public void testSetRowKey() {
        adaptor.setRowKey(new Integer(1));
        assertEquals(1, adaptor.getRowIndex());
        adaptor.setRowKey(null);
        assertEquals(-1, adaptor.getRowIndex());
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#resetDataModel()}.
     */
    public void testResetDataModel() {
        adaptor.setRowKey(new Integer(1));
        adaptor.resetDataModel();
        assertEquals(0, adaptor.getRowIndex());
    }

    public void testGetBaseClientId() {
        createDataTree();
        data.setRowIndex(-1);
        assertEquals(adaptor.getBaseClientId(facesContext), "data:adaptor");
        data.setRowIndex(0);
        assertEquals(adaptor.getBaseClientId(facesContext), "data:0:adaptor");
        data.setRowIndex(1);
        assertEquals(adaptor.getBaseClientId(facesContext), "data:1:adaptor");
        adaptor.setRowIndex(1);
        assertEquals(adaptor.getBaseClientId(facesContext), "data:1:adaptor");
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#setExtendedDataModel(org.ajax4jsf.ajax.repeat.ExtendedDataModel)}.
     */
    public void testSetIterableDataModel() {
        createDataTree();
        data.setRowIndex(0);

        MockDataModel mockDataModel0 = new MockDataModel();

        adaptor.setExtendedDataModel(mockDataModel0);
        data.setRowIndex(1);

        MockDataModel mockDataModel1 = new MockDataModel();

        adaptor.setExtendedDataModel(mockDataModel1);
        data.setRowIndex(0);
        assertSame(mockDataModel0, adaptor.getExtendedDataModel());
        data.setRowIndex(1);
        assertSame(mockDataModel1, adaptor.getExtendedDataModel());
    }

    public void testSetDataModel() {
        MockDataModel mockDataModel1 = new MockDataModel();

        adaptor.setExtendedDataModel(mockDataModel1);
        assertSame(mockDataModel1, adaptor.getExtendedDataModel());
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#getExtendedDataModel()}.
     */
    public void testGetIterableDataModel() {
        ExtendedDataModel dataModel = adaptor.getExtendedDataModel();

        assertTrue(dataModel instanceof MockDataModel);
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#getComponentState()}.
     */
    public void testGetComponentState() {
        createDataTree();
        data.setRowIndex(0);

        DataComponentState state0 = adaptor.getComponentState();

        assertTrue(state0 instanceof MockComponentState);
        data.setRowIndex(1);

        DataComponentState state1 = adaptor.getComponentState();

        data.setRowIndex(0);
        assertSame(state0, adaptor.getComponentState());
        data.setRowIndex(1);
        assertSame(state1, adaptor.getComponentState());
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#createComponentState()}.
     */
    public void testCreateComponentState() {
        DataComponentState state = adaptor.createComponentState();

        assertTrue(state instanceof MockComponentState);
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#iterate(javax.faces.context.FacesContext, org.ajax4jsf.ajax.repeat.RepeaterInvoker)}.
     */
    public void testProcess() {
        createTree();

        MockComponentState mockState = (MockComponentState) adaptor.getComponentState();

        mockState.setCount(4);
        adaptor.processDecodes(facesContext);
        assertEquals(childInvoked, 4);
        assertEquals(facetInvoked, 1);
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#iterate(javax.faces.context.FacesContext, org.ajax4jsf.ajax.repeat.RepeaterInvoker)}.
     */
    public void testProcessMore() {
        createTree();

        MockComponentState mockState = (MockComponentState) adaptor.getComponentState();

        mockState.setCount(Integer.MAX_VALUE);
        adaptor.processDecodes(facesContext);
        assertEquals(childInvoked, MockDataModel.ROWS);
        assertEquals(facetInvoked, 1);
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#restoreState(javax.faces.context.FacesContext, java.lang.Object)}.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void testRestoreStateFacesContextObject() throws Exception {
        createTree();

        MockComponentState mockState = (MockComponentState) adaptor.getComponentState();

        mockState.setCount(123);
        adaptor.encodeBegin(facesContext);

        UIViewRoot viewRoot = facesContext.getViewRoot();
        Object treeState = viewRoot.processSaveState(facesContext);
        UIViewRoot root = (UIViewRoot) viewRoot.getClass().newInstance();
        UIDataAdaptor restoredAdaptor = new MockDataAdaptor();

        root.getChildren().add(restoredAdaptor);
        root.processRestoreState(facesContext, treeState);
        mockState = (MockComponentState) restoredAdaptor.getComponentState();
        assertEquals(mockState.getCount(), 123);
    }

    /**
     * Test method for {@link org.ajax4jsf.component.UIDataAdaptor#saveState(javax.faces.context.FacesContext)}.
     */
    public void testSaveStateFacesContext() throws Exception {
        createDataTree();
        data.setRowIndex(0);

        MockComponentState mockState = (MockComponentState) adaptor.getComponentState();

        mockState.setCount(123);
        adaptor.encodeBegin(facesContext);
        data.setRowIndex(1);
        mockState = (MockComponentState) adaptor.getComponentState();
        mockState.setCount(321);
        adaptor.encodeBegin(facesContext);

        UIViewRoot viewRoot = facesContext.getViewRoot();
        Object treeState = viewRoot.processSaveState(facesContext);
        UIViewRoot root = (UIViewRoot) viewRoot.getClass().newInstance();
        UIData restoredData = new UIData();
        UIDataAdaptor restoredAdaptor = new MockDataAdaptor();

        root.getChildren().add(restoredData);

        UIColumn column = new UIColumn();

        restoredData.getChildren().add(column);
        column.getChildren().add(restoredAdaptor);
        root.processRestoreState(facesContext, treeState);
        restoredData.setRowIndex(0);
        mockState = (MockComponentState) restoredAdaptor.getComponentState();
        assertEquals(123, mockState.getCount());
        restoredData.setRowIndex(1);
        mockState = (MockComponentState) restoredAdaptor.getComponentState();
        assertEquals(321, mockState.getCount());
    }

    public void testSaveChildState() {
        createTree();
        child.setSubmittedValue("Submitted");
        child.setValid(true);
        child.setValue("Value");
        child.setLocalValueSet(true);
        adaptor.saveChildState(facesContext);
        child.setSubmittedValue("NonSubmitted");
        child.setValid(false);
        child.setValue(null);
        child.setLocalValueSet(false);
        adaptor.restoreChildState(facesContext);
        assertEquals(child.getSubmittedValue(), "Submitted");
        assertTrue(child.isValid());
        assertEquals(child.getValue(), "Value");
        assertTrue(child.isLocalValueSet());
    }

    public void testSaveChildChildState() {
        createTree();
        childChild.setSubmittedValue("Submitted");
        childChild.setValid(true);
        childChild.setValue("Value");
        childChild.setLocalValueSet(true);
        adaptor.saveChildState(facesContext);
        childChild.setSubmittedValue("NonSubmitted");
        childChild.setValid(false);
        childChild.setValue(null);
        childChild.setLocalValueSet(false);
        adaptor.restoreChildState(facesContext);
        assertEquals(childChild.getSubmittedValue(), "Submitted");
        assertTrue(childChild.isValid());
        assertEquals(childChild.getValue(), "Value");
        assertTrue(childChild.isLocalValueSet());
    }

    public void testSaveChildChildFacetState() {
        createTree();
        childChildFacet.setSubmittedValue("Submitted");
        childChildFacet.setValid(true);
        childChildFacet.setValue("Value");
        childChildFacet.setLocalValueSet(true);
        adaptor.saveChildState(facesContext);
        childChildFacet.setSubmittedValue("NonSubmitted");
        childChildFacet.setValid(false);
        childChildFacet.setValue(null);
        childChildFacet.setLocalValueSet(false);
        adaptor.restoreChildState(facesContext);
        assertEquals(childChildFacet.getSubmittedValue(), "Submitted");
        assertTrue(childChildFacet.isValid());
        assertEquals(childChildFacet.getValue(), "Value");
        assertTrue(childChildFacet.isLocalValueSet());
    }

    public void testSetValue() {
        ExtendedDataModel model1 = adaptor.getExtendedDataModel();

        adaptor.setValue("value");

        ExtendedDataModel model2 = adaptor.getExtendedDataModel();

        assertNotSame(model1, model2);

        Object value = adaptor.getValue();

        assertNotNull(value);
        assertEquals("value", value);
    }
}
