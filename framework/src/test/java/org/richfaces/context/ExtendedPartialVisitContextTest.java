/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tested view structure:
 * <ul>
 * <li>a4j:table is AjaxTableComponentImpl</li>
 * </ul>
 *
 * <pre>
 * &lt;h:form id=&quot;myForm&quot;&gt;
 *   &lt;a4j:outputText id=&quot;outerOutput&quot; /&gt;
 *
 *   &lt;a4j:table id=&quot;table&quot; var=&quot;item&quot; value=&quot;['Item 0',... ,'Item 1']&quot;&gt;
 *       &lt;f:facet name=&quot;header&quot;&gt;
 *           &lt;h:outputText id=&quot;theHeader&quot; /&gt;
 *       &lt;/f:facet&gt;
 *
 *       &lt;h:column&gt;
 *          &lt;a4j:outputText id=&quot;nestedOutput&quot; value=&quot;#{item}&quot; /&gt;
 *
 *          &lt;h:outputText id=&quot;nestedText&quot; value=&quot;#{item}&quot; /&gt;
 *
 *          &lt;a4j:table id=&quot;nestedTable&quot; value=&quot;['Nested item 0',... ,'Nested item 2']&quot; var=&quot;nestedItem&quot;&gt;
 *              &lt;f:facet name=&quot;footer&quot;&gt;
 *                  &lt;h:outputText id=&quot;nestedTableFooter&quot; value=&quot;#{item}&quot; /&gt;
 *              &lt;/f:facet&gt;
 *
 *              &lt;h:column&gt;
 *                  &lt;h:outputText id=&quot;nestedTableText&quot; value=&quot;#{nestedItem}&quot; /&gt;
 *              &lt;/h:column&gt;
 *          &lt;/a4j:table&gt;
 *       &lt;/h:column&gt;
 *   &lt;/a4j:table&gt;
 * &lt;/h:form&gt;
 * </pre>
 *
 *
 * @author Nick Belaevski
 *
 */
public class ExtendedPartialVisitContextTest {
    private class TrackingVisitCallback implements VisitCallback {
        private List<String> visitedIds = new ArrayList<String>();

        public VisitResult visit(VisitContext context, UIComponent target) {
            if (context instanceof ExtendedVisitContext) {
                visitedIds.add(((ExtendedVisitContext) context).buildExtendedClientId(target));
            } else {
                visitedIds.add(target.getClientId(context.getFacesContext()));
            }

            return VisitResult.REJECT;
        }

        public List<String> getVisitedIds() {
            return visitedIds;
        }

        public void reset() {
            visitedIds.clear();
        }
    }

    private FacesEnvironment environment;
    private FacesRequest facesRequest;
    private FacesContext facesContext;
    private Application application;
    private UIViewRoot viewRoot;
    private UIForm form;
    private AjaxOutputComponentImpl outerOutput;
    private AjaxTableComponentImpl table;
    private UIOutput dataHeader;
    private AjaxOutputComponentImpl nestedOutput;
    private UIOutput nestedText;
    private List<String> tableData;
    private BaseExtendedVisitContext renderingContext;
    private TrackingVisitCallback trackingVisitCallback;
    private ArrayList<String> nestedTableData;
    private AjaxTableComponentImpl nestedTable;
    private UIOutput nestedTableText;
    private UIOutput nestedTableFooter;

    private static void assertEqualSets(Collection<?> expected, Collection<?> actual) {
        assertEquals(asComparableCollection(expected), asComparableCollection(actual));
    }

    private static <T> Collection<T> asComparableCollection(Collection<T> c) {
        if (c instanceof Set || c instanceof List) {
            return c;
        } else {
            if (c == VisitContext.ALL_IDS) {
                return c;
            }

            if (c != null) {
                return new HashSet<T>(c);
            } else {
                return null;
            }
        }
    }

    private static <T> Set<T> asSet(T... args) {
        Set<T> set = new HashSet<T>();
        for (T argItem : args) {
            set.add(argItem);
        }

        return set;
    }

    private void createVisitContext(boolean limitRender) {
        renderingContext = new RenderExtendedVisitContext(facesContext, Collections.<String>emptySet(),
            EnumSet.<VisitHint>of(VisitHint.SKIP_UNRENDERED), limitRender);
    }

    private void createNestedTableData() {
        nestedTableData = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            nestedTableData.add(MessageFormat.format("Nested item {0}", i));
        }
    }

    private void createTableData() {
        tableData = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            tableData.add(MessageFormat.format("Item {0}", i));
        }
    }

    private ValueExpression createTableVarValueExpression() {
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();

        return expressionFactory.createValueExpression(elContext, "#{item}", String.class);
    }

    private ValueExpression createNestedTableVarValueExpression() {
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();

        return expressionFactory.createValueExpression(elContext, "#{nestedItem}", String.class);
    }

    private void createNestedText() {
        nestedText = (UIOutput) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nestedText.setId("nestedText");
        nestedText.setValueExpression("value", createTableVarValueExpression());

        table.getChildren().get(0).getChildren().add(nestedText);
    }

    private void createNestedOutput() {
        nestedOutput = new AjaxOutputComponentImpl();
        nestedOutput.setAjaxRendered(true);
        nestedOutput.setId("nestedOutput");
        nestedOutput.setValueExpression("value", createTableVarValueExpression());

        table.getChildren().get(0).getChildren().add(nestedOutput);
    }

    private void createOuterOutput() {
        outerOutput = new AjaxOutputComponentImpl();
        outerOutput.setAjaxRendered(true);
        outerOutput.setId("outerOutput");

        form.getChildren().add(outerOutput);
    }

    private void createTableHeader() {
        dataHeader = (UIOutput) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        dataHeader.setId("theHeader");

        table.getFacets().put("header", dataHeader);
    }

    private void createNestedTable() {
        nestedTable = new AjaxTableComponentImpl();
        nestedTable.setId("nestedTable");
        nestedTable.setVar("nestedItem");

        createNestedTableData();
        nestedTable.setValue(nestedTableData);

        nestedTable.getChildren().add(new UIColumn());

        table.getChildren().get(0).getChildren().add(nestedTable);

        createNestedTableText();
        createNestedTableFooter();
    }

    private void createNestedTableFooter() {
        nestedTableFooter = (UIOutput) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nestedTableFooter.setId("nestedTableFooter");
        nestedTableFooter.setValueExpression("value", createTableVarValueExpression());

        nestedTable.getFacets().put("footer", nestedTableFooter);
    }

    private void createNestedTableText() {
        nestedTableText = (UIOutput) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nestedTableText.setId("nestedTableText");
        nestedTableText.setValueExpression("value", createNestedTableVarValueExpression());

        nestedTable.getChildren().get(0).getChildren().add(nestedTableText);
    }

    private void createTable() {
        table = new AjaxTableComponentImpl();
        table.setId("table");
        table.setVar("item");

        createTableData();
        table.setValue(tableData);

        table.getChildren().add(new UIColumn());

        form.getChildren().add(table);
        createNestedOutput();
        createNestedText();
        createNestedTable();
        createTableHeader();
    }

    private void createForm() {
        form = (UIForm) application.createComponent(UIForm.COMPONENT_TYPE);
        form.setId("myForm");

        viewRoot.getChildren().add(form);

        createOuterOutput();
        createTable();
    }

    private void createView() {
        viewRoot = facesContext.getViewRoot();

        createForm();
    }

    @Before
    public void setUp() throws Exception {
        environment = FacesEnvironment.createEnvironment();
        environment.start();

        facesRequest = environment.createFacesRequest();
        facesRequest.start();

        facesContext = FacesContext.getCurrentInstance();
        application = facesContext.getApplication();

        createView();

        trackingVisitCallback = new TrackingVisitCallback();
    }

    @After
    public void tearDown() throws Exception {
        renderingContext = null;
        trackingVisitCallback = null;

        facesContext = null;
        application = null;

        table = null;
        dataHeader = null;
        form = null;
        nestedOutput = null;
        nestedTable = null;
        nestedTableData = null;
        nestedTableFooter = null;
        nestedTableText = null;
        nestedText = null;
        outerOutput = null;
        tableData = null;
        viewRoot = null;

        facesRequest.release();
        facesRequest = null;

        environment.release();
        environment = null;
    }

    @Test
    public void testCollectionProxy() throws Exception {
        createVisitContext(false);

        Iterator<String> iterator;

        Collection<String> idsToVisit = renderingContext.getIdsToVisit();
        assertTrue(idsToVisit.isEmpty());
        assertTrue(idsToVisit.size() == 0);

        iterator = idsToVisit.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());

        Set<String> idsToAdd = asSet("someIds", "thisIs:evenBetter", "myForm:table:0:nestedText");
        idsToVisit.addAll(idsToAdd);

        assertFalse(idsToVisit.isEmpty());
        assertTrue(idsToVisit.size() == 3);

        iterator = idsToVisit.iterator();

        while (iterator.hasNext()) {
            String nextId = iterator.next();

            assertTrue(idsToAdd.remove(nextId));

            iterator.remove();
        }

        assertTrue(idsToVisit.isEmpty());
        assertTrue(idsToVisit.size() == 0);
        assertTrue(idsToAdd.isEmpty());

        iterator = idsToVisit.iterator();

        try {
            iterator.remove();

            fail();
        } catch (IllegalStateException e) {
            // ignore
        }

        idsToVisit.add("testId");
        assertFalse(idsToVisit.isEmpty());

        iterator = idsToVisit.iterator();

        try {
            iterator.remove();

            fail();
        } catch (IllegalStateException e) {
            // ignore
        }
    }

    @Test
    public void testSubtreeIdsToForAjaxOutputs() throws Exception {
        createVisitContext(false);

        assertSame(VisitContext.ALL_IDS, renderingContext.getSubtreeIdsToVisit(form));
        assertEqualSets(asSet("table", "outerOutput"), renderingContext.getDirectSubtreeIdsToVisit(form));

        assertSame(VisitContext.ALL_IDS, renderingContext.getSubtreeIdsToVisit(table));
        assertEqualSets(asSet("nestedOutput"), renderingContext.getDirectSubtreeIdsToVisit(table));

        assertTrue(renderingContext.getSubtreeIdsToVisit(nestedTable).isEmpty());
        assertTrue(renderingContext.getDirectSubtreeIdsToVisit(nestedTable).isEmpty());
    }

    @Test
    public void testSubtreeIdsForEmptyIdsWithLimitRender() throws Exception {
        createVisitContext(true);

        assertTrue(renderingContext.getSubtreeIdsToVisit(form).isEmpty());
        assertTrue(renderingContext.getDirectSubtreeIdsToVisit(form).isEmpty());

        assertTrue(renderingContext.getSubtreeIdsToVisit(table).isEmpty());
        assertTrue(renderingContext.getDirectSubtreeIdsToVisit(table).isEmpty());

        assertTrue(renderingContext.getSubtreeIdsToVisit(nestedTable).isEmpty());
        assertTrue(renderingContext.getDirectSubtreeIdsToVisit(nestedTable).isEmpty());
    }

    @Test
    public void testSubtreeIdsForOuterOutputWithLimitRender() throws Exception {
        createVisitContext(true);

        renderingContext.getIdsToVisit().add("myForm:outerOutput");

        assertEqualSets(asSet("myForm:outerOutput"), renderingContext.getSubtreeIdsToVisit(form));
        assertEqualSets(asSet("outerOutput"), renderingContext.getDirectSubtreeIdsToVisit(form));

        assertTrue(renderingContext.getSubtreeIdsToVisit(table).isEmpty());
        assertTrue(renderingContext.getDirectSubtreeIdsToVisit(table).isEmpty());
    }

    @Test
    public void testSubtreeIdsForNestedComponentsWithLimitRender() throws Exception {
        createVisitContext(true);

        renderingContext.getIdsToVisit().add("myForm:outerOutput");
        renderingContext.getIdsToVisit().add("myForm:table:theHeader");
        renderingContext.getIdsToVisit().add("myForm:table:1:nestedOutput");
        renderingContext.getIdsToVisit().add("myForm:table:0:nestedText");
        renderingContext.getIdsToVisit().add("myForm:table:0:nestedTable:1:nestedTableText");
        renderingContext.getIdsToVisit().add("myForm:table:0:nestedTable:nestedFooter");

        Set<String> formClientIds = asSet("myForm:outerOutput", "myForm:table:0:nestedText", "myForm:table:1:nestedOutput",
            "myForm:table:theHeader", "myForm:table:0:nestedTable:1:nestedTableText", "myForm:table:0:nestedTable:nestedFooter");

        Set<String> formIds = asSet("table", "outerOutput");

        assertEqualSets(formClientIds, renderingContext.getSubtreeIdsToVisit(form));
        assertEqualSets(formIds, renderingContext.getDirectSubtreeIdsToVisit(form));

        Set<String> tableClientIds = asSet("myForm:table:0:nestedText", "myForm:table:1:nestedOutput",
            "myForm:table:theHeader", "myForm:table:0:nestedTable:1:nestedTableText", "myForm:table:0:nestedTable:nestedFooter");
        Set<String> tableIds = asSet("0", "1", "theHeader");

        assertEqualSets(tableClientIds, renderingContext.getSubtreeIdsToVisit(table));
        assertEqualSets(tableIds, renderingContext.getDirectSubtreeIdsToVisit(table));

        table.setRowIndex(0);

        Set<String> nestedTableClientIds = asSet("myForm:table:0:nestedTable:1:nestedTableText",
            "myForm:table:0:nestedTable:nestedFooter");

        Set<String> nestedTableIds = asSet("nestedFooter", "1");
        assertEqualSets(nestedTableClientIds, renderingContext.getSubtreeIdsToVisit(nestedTable));
        assertEqualSets(nestedTableIds, renderingContext.getDirectSubtreeIdsToVisit(nestedTable));

        table.setRowIndex(-1);
    }

    @Test
    public void testVisitCallbackForEmptyIds() throws Exception {
        createVisitContext(false);

        viewRoot.visitTree(renderingContext, trackingVisitCallback);

        assertEquals(Arrays.asList("myForm:outerOutput", "myForm:table:0:nestedOutput", "myForm:table:1:nestedOutput"),
            trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testVisitCallbackForEmptyIdsWithLimitRender() throws Exception {
        createVisitContext(true);

        viewRoot.visitTree(renderingContext, trackingVisitCallback);

        assertEquals(Arrays.asList(), trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testVisitCallbackForEmptyIdsForFalseAjaxRendered() throws Exception {
        outerOutput.setAjaxRendered(false);
        createVisitContext(false);

        viewRoot.visitTree(renderingContext, trackingVisitCallback);

        assertEquals(Arrays.asList("myForm:table:0:nestedOutput", "myForm:table:1:nestedOutput"),
            trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testVisitCallback() throws Exception {
        createVisitContext(false);

        renderingContext.getIdsToVisit().add("myForm:table:1:nestedTable:1:nestedTableText");
        renderingContext.getIdsToVisit().add("myForm:table:1:nestedTable:nestedTableFooter");

        viewRoot.visitTree(renderingContext, trackingVisitCallback);

        assertEquals(Arrays.asList("myForm:outerOutput", "myForm:table:0:nestedOutput", "myForm:table:1:nestedOutput",
            "myForm:table:1:nestedTable:nestedTableFooter", "myForm:table:1:nestedTable:1:nestedTableText"),
            trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testVisitMetaComponentsWithLimitRender() throws Exception {
        createVisitContext(true);
        renderingContext.getIdsToVisit().add("myForm:table:1:nestedTable@footer");

        boolean visitResult = viewRoot.visitTree(renderingContext, trackingVisitCallback);

        assertEquals(Arrays.asList("myForm:table:1:nestedTable@footer"), trackingVisitCallback.getVisitedIds());

        assertTrue(visitResult);
    }

    @Test
    public void testFormVisitContext() throws Exception {
        createVisitContext(false);

        Collection<String> formDirectIds = renderingContext.getDirectSubtreeIdsToVisit(form);
        assertNotSame(VisitContext.ALL_IDS, formDirectIds);
        ExtendedVisitContext formVisitContext = (ExtendedVisitContext) renderingContext.createNamingContainerVisitContext(form,
            formDirectIds);

        assertFalse(formVisitContext.getIdsToVisit().isEmpty());
        assertSame(VisitContext.ALL_IDS, formVisitContext.getSubtreeIdsToVisit(form));
        assertTrue(formVisitContext.getSubtreeIdsToVisit(table).isEmpty());

        Collection<String> directIds = formVisitContext.getDirectSubtreeIdsToVisit(form);
        assertEqualSets(asSet("outerOutput", "table"), directIds);
        assertTrue(formVisitContext.getDirectSubtreeIdsToVisit(table).isEmpty());

        boolean visitResult = form.visitTree(formVisitContext, trackingVisitCallback);
        assertTrue(visitResult);

        assertEquals(Arrays.asList("myForm:outerOutput", "myForm:table"), trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testFormVisitContextWithLimitRender() throws Exception {
        createVisitContext(true);

        renderingContext.getIdsToVisit().add("myForm:table:0:nestedText");

        Collection<String> formDirectIds = renderingContext.getDirectSubtreeIdsToVisit(form);
        assertNotSame(VisitContext.ALL_IDS, formDirectIds);
        ExtendedVisitContext formVisitContext = (ExtendedVisitContext) renderingContext.createNamingContainerVisitContext(form,
            formDirectIds);

        assertFalse(formVisitContext.getIdsToVisit().isEmpty());
        assertSame(VisitContext.ALL_IDS, formVisitContext.getSubtreeIdsToVisit(form));
        assertTrue(formVisitContext.getSubtreeIdsToVisit(table).isEmpty());

        Collection<String> directIds = formVisitContext.getDirectSubtreeIdsToVisit(form);
        assertEqualSets(asSet("table"), directIds);
        assertTrue(formVisitContext.getSubtreeIdsToVisit(table).isEmpty());

        boolean visitResult = form.visitTree(formVisitContext, trackingVisitCallback);
        assertTrue(visitResult);

        assertEquals(Arrays.asList("myForm:table"), trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testFormVisitContextNonexistentIdsWithLimitRender() throws Exception {
        createVisitContext(true);

        renderingContext.getIdsToVisit().add("myForm:nonExistentId");
        Collection<String> formDirectIds = renderingContext.getDirectSubtreeIdsToVisit(form);
        assertNotSame(VisitContext.ALL_IDS, formDirectIds);
        ExtendedVisitContext formVisitContext = (ExtendedVisitContext) renderingContext.createNamingContainerVisitContext(form,
            formDirectIds);

        assertFalse(formVisitContext.getIdsToVisit().isEmpty());
        assertSame(VisitContext.ALL_IDS, formVisitContext.getSubtreeIdsToVisit(form));
        assertTrue(formVisitContext.getSubtreeIdsToVisit(table).isEmpty());

        Collection<String> directIds = formVisitContext.getDirectSubtreeIdsToVisit(form);
        assertEqualSets(asSet("nonExistentId"), directIds);
        assertTrue(formVisitContext.getSubtreeIdsToVisit(table).isEmpty());

        boolean visitResult = form.visitTree(formVisitContext, trackingVisitCallback);
        assertFalse(visitResult);

        assertEquals(Arrays.asList(), trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testVisitMultiple() throws Exception {
        createVisitContext(true);

        String idFormat = "myForm:table:{0}:nestedTable:1:nestedTableText";

        renderingContext.getIdsToVisit().add(MessageFormat.format(idFormat, 0));
        renderingContext.getIdsToVisit().add(MessageFormat.format(idFormat, 1));
        boolean visitResult = viewRoot.visitTree(renderingContext, trackingVisitCallback);
        assertTrue(visitResult);

        assertEquals(
            Arrays.asList("myForm:table:0:nestedTable:1:nestedTableText", "myForm:table:1:nestedTable:1:nestedTableText"),
            trackingVisitCallback.getVisitedIds());
    }

    @Test
    public void testVisitMultipleWithPatternAndMetacomponent() throws Exception {
        createVisitContext(true);

        String idFormat = "myForm:table:{0}:nestedTable:{1}:nestedTableText";

        renderingContext.getIdsToVisit().add(MessageFormat.format(idFormat, 0, 0));
        renderingContext.getIdsToVisit().add(MessageFormat.format(idFormat, 0, 1));
        renderingContext.getIdsToVisit().add(MessageFormat.format(idFormat, 1, 0));
        renderingContext.getIdsToVisit().add(MessageFormat.format(idFormat, 1, 1));

        renderingContext.getIdsToVisit().add("myForm:table:0:nestedTable@footer");

        viewRoot.visitTree(renderingContext, trackingVisitCallback);

        assertEquals(Arrays.asList("myForm:table:0:nestedTable@footer", "myForm:table:0:nestedTable:0:nestedTableText",
            "myForm:table:0:nestedTable:1:nestedTableText",

            "myForm:table:1:nestedTable:0:nestedTableText", "myForm:table:1:nestedTable:1:nestedTableText"),
            trackingVisitCallback.getVisitedIds());
    }

    // TODO nick - add alike tests
    @Test
    public void testMultipleMetaComponentIds() throws Exception {
        createVisitContext(true);

        renderingContext.getIdsToVisit().add("myForm:table@header");
        renderingContext.getIdsToVisit().add("myForm:table@footer");

        assertTrue(renderingContext.getIdsToVisit().contains("myForm:table@header"));
        assertTrue(renderingContext.getIdsToVisit().contains("myForm:table@footer"));

        assertFalse(renderingContext.getIdsToVisit().contains("myForm"));
        assertFalse(renderingContext.getIdsToVisit().contains("table"));
        assertFalse(renderingContext.getIdsToVisit().contains("myForm:table"));
    }

    @Test
    public void testVisitForm() throws Exception {
        createVisitContext(true);

        renderingContext.getIdsToVisit().add("myForm");

        boolean result = facesContext.getViewRoot().visitTree(renderingContext, trackingVisitCallback);

        assertTrue(result);
        assertEquals(Arrays.asList("myForm"), trackingVisitCallback.getVisitedIds());
    }
}
