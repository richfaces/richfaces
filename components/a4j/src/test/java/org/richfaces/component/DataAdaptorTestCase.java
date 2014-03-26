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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.ListDataModel;

import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.IterationStateHolder;
import org.ajax4jsf.model.SequenceDataModel;
import org.jboss.test.faces.AbstractFacesTest;

/**
 * @author Nick Belaevski
 *
 */
public class DataAdaptorTestCase extends AbstractFacesTest {
    private static final String VAR_NAME = "item";

    private static class TestCallback {
        private int value;

        public void handle() {

        }

        public void reset() {
            value = 0;
        }

        public int getAndIncrement() {
            return value++;
        }

        public int get() {
            return value;
        }
    }

    private MockDataAdaptor mockDataAdaptor;
    private List<String> data;

    private ExtendedDataModel<String> createDataModel() {
        return new SequenceDataModel<String>(new ListDataModel<String>(new ArrayList<String>(data)));
    }

    private Object getVarValue() {
        return facesContext.getApplication().evaluateExpressionGet(facesContext, MessageFormat.format("#'{'{0}'}'", VAR_NAME),
            Object.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        data = Arrays.asList("a", "b", "c", "d");

        setupFacesRequest();

        mockDataAdaptor = new MockDataAdaptor();
        mockDataAdaptor.setDataModel(createDataModel());
        mockDataAdaptor.setVar(VAR_NAME);

        facesContext.getViewRoot().getChildren().add(mockDataAdaptor);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        data = null;
        mockDataAdaptor = null;
    }

    private void resetCallbacks(TestCallback... callbacks) {
        for (TestCallback callback : callbacks) {
            callback.reset();
            assertEquals(0, callback.get());
        }
    }

    private UIComponent createCallbackComponent(final TestCallback callback) throws Exception {
        return new HtmlOutputText() {
            private void notifyCallbacks() {
                callback.handle();
            }

            @Override
            public void processDecodes(FacesContext context) {
                super.processDecodes(context);
                notifyCallbacks();
            }

            @Override
            public void processValidators(FacesContext context) {
                super.processValidators(context);
                notifyCallbacks();
            }

            @Override
            public void processUpdates(FacesContext context) {
                super.processUpdates(context);
                notifyCallbacks();
            }
        };
    }

    public void testProcessChildren() throws Exception {

        TestCallback childCallback = new TestCallback() {
            @Override
            public void handle() {
                assertEquals(getVarValue(), data.get(getAndIncrement()));
            }
        };
        UIComponent child = createCallbackComponent(childCallback);
        child.setId("child");

        TestCallback facetCallback = new TestCallback() {
            @Override
            public void handle() {
                assertEquals(getVarValue(), data.get(getAndIncrement()));
            }
        };
        UIComponent facet = createCallbackComponent(facetCallback);
        child.getFacets().put("f", facet);
        facet.setId("facet");

        TestCallback immediateFacetCallback = new TestCallback() {
            @Override
            public void handle() {
                getAndIncrement();
                assertNull(getVarValue());
            }
        };
        UIComponent immediateFacet = createCallbackComponent(immediateFacetCallback);
        immediateFacet.setId("immediateFacet");

        mockDataAdaptor.getChildren().add(child);
        mockDataAdaptor.getFacets().put("facet", immediateFacet);

        mockDataAdaptor.processDecodes(facesContext);

        assertEquals(data.size(), facetCallback.get());
        assertEquals(data.size(), childCallback.get());
        assertEquals(1, immediateFacetCallback.get());

        resetCallbacks(childCallback, facetCallback, immediateFacetCallback);

        mockDataAdaptor.processValidators(facesContext);

        assertEquals(data.size(), facetCallback.get());
        assertEquals(data.size(), childCallback.get());
        assertEquals(1, immediateFacetCallback.get());

        resetCallbacks(childCallback, facetCallback, immediateFacetCallback);

        mockDataAdaptor.processUpdates(facesContext);

        assertEquals(data.size(), facetCallback.get());
        assertEquals(data.size(), childCallback.get());
        assertEquals(1, immediateFacetCallback.get());

        resetCallbacks(childCallback, facetCallback, immediateFacetCallback);
    }

    public void testSaveRestoreChildrenState() throws Exception {
        HtmlForm form = new HtmlForm();
        HtmlInputText input = new HtmlInputText();
        IterationStateHolderComponent stateHolder = new IterationStateHolderComponent();

        List<UIComponent> children = mockDataAdaptor.getChildren();
        children.add(form);
        form.getChildren().add(input);
        form.getFacets().put("facet", stateHolder);

        mockDataAdaptor.setRowKey(facesContext, Integer.valueOf(0));

        assertFalse(form.isSubmitted());
        assertNull(input.getSubmittedValue());
        assertNull(input.getLocalValue());
        assertTrue(input.isValid());
        assertFalse(input.isLocalValueSet());
        assertNull(stateHolder.getIterationState());

        form.setSubmitted(true);
        input.setSubmittedValue("user input");
        input.setValue("component value");
        input.setValid(false);
        input.setLocalValueSet(true);
        stateHolder.setIterationState("state");

        mockDataAdaptor.setRowKey(facesContext, Integer.valueOf(1));

        assertFalse(form.isSubmitted());
        assertNull(input.getSubmittedValue());
        assertNull(input.getLocalValue());
        assertTrue(input.isValid());
        assertFalse(input.isLocalValueSet());
        assertNull(stateHolder.getIterationState());

        input.setSubmittedValue("another input from user");
        input.setValue("123");
        assertTrue(input.isLocalValueSet());
        stateHolder.setIterationState("456");

        mockDataAdaptor.setRowKey(facesContext, Integer.valueOf(0));
        assertTrue(form.isSubmitted());
        assertEquals("user input", input.getSubmittedValue());
        assertEquals("component value", input.getLocalValue());
        assertFalse(input.isValid());
        assertTrue(input.isLocalValueSet());
        assertEquals("state", stateHolder.getIterationState());

        mockDataAdaptor.setRowKey(facesContext, Integer.valueOf(1));
        assertFalse(form.isSubmitted());
        assertEquals("another input from user", input.getSubmittedValue());
        assertEquals("123", input.getLocalValue());
        assertTrue(input.isValid());
        assertTrue(input.isLocalValueSet());
        assertEquals("456", stateHolder.getIterationState());

        mockDataAdaptor.setRowKey(facesContext, null);
        assertFalse(form.isSubmitted());
        assertNull(input.getSubmittedValue());
        assertNull(input.getLocalValue());
        assertTrue(input.isValid());
        assertFalse(input.isLocalValueSet());
        assertNull(stateHolder.getIterationState());
    }

    public void testSaveRestoreChildrenStateNestedDataAdaptors() throws Exception {
        MockDataAdaptor childAdaptor = new MockDataAdaptor();
        childAdaptor.setDataModel(createDataModel());

        HtmlInputText input = new HtmlInputText();

        mockDataAdaptor.getChildren().add(childAdaptor);
        childAdaptor.getChildren().add(input);

        Integer rowKey = Integer.valueOf(2);
        Integer childKey = Integer.valueOf(1);

        mockDataAdaptor.setRowKey(facesContext, rowKey);
        childAdaptor.setRowKey(facesContext, childKey);

        assertNull(input.getSubmittedValue());
        assertNull(input.getLocalValue());
        assertTrue(input.isValid());
        assertFalse(input.isLocalValueSet());

        input.setSubmittedValue("submittedValue");
        input.setValue("value");

        childAdaptor.setRowKey(facesContext, null);
        mockDataAdaptor.setRowKey(facesContext, Integer.valueOf(3));
        childAdaptor.setRowKey(facesContext, Integer.valueOf(0));

        assertNull(input.getSubmittedValue());
        assertNull(input.getLocalValue());
        assertFalse(input.isLocalValueSet());

        childAdaptor.setRowKey(facesContext, null);
        mockDataAdaptor.setRowKey(facesContext, rowKey);
        childAdaptor.setRowKey(facesContext, childKey);

        assertEquals("submittedValue", input.getSubmittedValue());
        assertEquals("value", input.getLocalValue());
        assertTrue(input.isValid());
        assertTrue(input.isLocalValueSet());
    }

    public void testEventsQueueing() throws Exception {
        HtmlInputText input = new HtmlInputText();

        final TestCallback testCallback = new TestCallback();
        input.addValueChangeListener(new ValueChangeListener() {
            public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
                testCallback.getAndIncrement();
                assertEquals(data.get(1), getVarValue());
            }
        });

        mockDataAdaptor.getChildren().add(input);
        mockDataAdaptor.setRowKey(facesContext, Integer.valueOf(1));
        assertEquals(data.get(1), getVarValue());

        new ValueChangeEvent(input, null, "testValue").queue();
        mockDataAdaptor.setRowKey(facesContext, null);

        facesContext.getViewRoot().broadcastEvents(facesContext, PhaseId.PROCESS_VALIDATIONS);
        assertEquals(1, testCallback.get());
    }

    public void testInvokeOnComponent() throws Exception {
        final HtmlInputText facet = new HtmlInputText();
        final HtmlInputText child = new HtmlInputText();

        mockDataAdaptor.getFacets().put("facet", facet);
        mockDataAdaptor.getChildren().add(child);

        mockDataAdaptor.setId("_data");
        facet.setId("_facet");
        child.setId("_child");

        boolean invocationResult;
        final TestCallback callback = new TestCallback();
        invocationResult = mockDataAdaptor.invokeOnComponent(facesContext, "_data", new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent target) {
                callback.getAndIncrement();
                assertEquals(mockDataAdaptor, target);
                assertEquals("_data", target.getClientId());
            }
        });

        assertTrue(invocationResult);
        assertEquals(1, callback.get());
        callback.reset();

        final char separatorChar = UINamingContainer.getSeparatorChar(facesContext);
        invocationResult = mockDataAdaptor.invokeOnComponent(facesContext, "_data" + separatorChar + "_facet",
            new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent target) {
                    callback.getAndIncrement();
                    assertEquals(facet, target);
                    assertEquals("_data" + separatorChar + "_facet", target.getClientId());
                }
            });

        assertTrue(invocationResult);
        assertEquals(1, callback.get());
        callback.reset();

        invocationResult = mockDataAdaptor.invokeOnComponent(facesContext, "_data" + separatorChar + "2" + separatorChar
            + "_child", new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent target) {
                callback.getAndIncrement();
                assertEquals(child, target);
                assertEquals(data.get(2), getVarValue());
                assertEquals("_data" + separatorChar + "2" + separatorChar + "_child", target.getClientId());
            }
        });

        assertTrue(invocationResult);
        assertEquals(1, callback.get());
        callback.reset();

        invocationResult = mockDataAdaptor.invokeOnComponent(facesContext, "_data" + separatorChar + "100" + separatorChar
            + "_child", new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent target) {
                fail();
            }
        });
        assertFalse(invocationResult);

        invocationResult = mockDataAdaptor.invokeOnComponent(facesContext, "_data" + separatorChar + "nonExistentComponent",
            new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent target) {
                    fail();
                }
            });
        assertFalse(invocationResult);
    }

    public void testVisitChildren() throws Exception {
        final HtmlInputText facet = new HtmlInputText();
        final HtmlInputText child = new HtmlInputText();

        mockDataAdaptor.getFacets().put("facet", facet);
        mockDataAdaptor.getChildren().add(child);

        mockDataAdaptor.setId("_data");
        facet.setId("_facet");
        child.setId("_child");

        VisitContext fullVisitContext = VisitContext.createVisitContext(facesContext);

        final char separatorChar = UINamingContainer.getSeparatorChar(facesContext);
        final Set<String> idsToVisit = new HashSet<String>();
        idsToVisit.add("_data" + separatorChar + "_facet");
        idsToVisit.add("_data" + separatorChar + "0" + separatorChar + "_child");
        idsToVisit.add("_data" + separatorChar + "2" + separatorChar + "_child");

        VisitContext partialVisitContext = VisitContext.createVisitContext(facesContext, idsToVisit,
            EnumSet.of(VisitHint.SKIP_UNRENDERED));

        final TestCallback callback = new TestCallback();
        mockDataAdaptor.visitTree(fullVisitContext, new VisitCallback() {
            public VisitResult visit(VisitContext context, UIComponent target) {
                callback.getAndIncrement();
                assertNotNull(target);

                return VisitResult.ACCEPT;
            }
        });

        assertEquals(1 /* adaptor itself */+ 1 /* facet */+ data.size(), callback.get());

        callback.reset();

        mockDataAdaptor.visitTree(partialVisitContext, new VisitCallback() {
            public VisitResult visit(VisitContext context, UIComponent target) {
                callback.getAndIncrement();
                assertNotNull(target);
                assertTrue(idsToVisit.contains(target.getClientId()));
                return VisitResult.ACCEPT;
            }
        });

        assertEquals(idsToVisit.size(), callback.get());

        callback.reset();

        mockDataAdaptor.visitTree(fullVisitContext, new VisitCallback() {
            public VisitResult visit(VisitContext context, UIComponent target) {
                callback.getAndIncrement();

                if (child.equals(target)
                    && child.getClientId().equals("_data" + separatorChar + "1" + separatorChar + "_child")) {
                    return VisitResult.COMPLETE;
                }

                return VisitResult.ACCEPT;
            }
        });

        assertEquals(1 /* data adaptor */+ 1 /* facet */+ 2 /* [0..1] children */, callback.get());
    }
}

class IterationStateHolderComponent extends UIComponentBase implements IterationStateHolder {
    private Object iterationState;

    @Override
    public String getFamily() {
        return "test.Component";
    }

    public Object getIterationState() {
        return iterationState;
    }

    public void setIterationState(Object state) {
        iterationState = state;
    }
}
