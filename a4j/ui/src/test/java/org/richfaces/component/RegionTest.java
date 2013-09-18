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
package org.richfaces.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.richfaces.ui.common.AjaxConstants.AJAX_COMPONENT_ID_PARAMETER;
import static org.richfaces.ui.common.AjaxConstants.ALL;
import static org.richfaces.ui.common.AjaxConstants.THIS;

import java.util.Collection;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.ui.ajax.region.AjaxContainer;

/**
 * Test page pseudo-code:
 *
 * <pre>
 * &lt;h:form id=&quot;form&quot;&gt;
 *    &lt;a4j:testCommandComponent id=&quot;testCommand&quot;
 *          binding=&quot;#{testCommand}&quot;
 *          execute=&quot;#{testCommandExecute}&quot; /&gt;
 *
 *    &lt;a4j:region id=&quot;region&quot;&gt;
 *        &lt;a4j:testCommandComponent id=&quot;testCommandRegion&quot;
 *          binding=&quot;#{testCommandRegion}&quot;
 *          execute=&quot;#{testCommandRegionExecute}&quot; /&gt;
 *
 *    &lt;/a4j:region&gt;
 * &lt;/h:form&gt;
 * </pre>
 *
 * TestCommandComponent is assumed to have execute=@this by default
 *
 * @author Nick Belaevski
 *
 */
public class RegionTest {
    private FacesEnvironment environment;
    private FacesRequest request;
    private FacesContext facesContext;
    private UIComponent testCommand;
    private String testCommandExecute;
    private UIComponent testCommandRegion;
    private String testCommandRegionExecute;
    private String testCommandClientId;
    private String testCommandRegionClientId;
    private String regionClientId;

    @SuppressWarnings("serial")
    private abstract static class StringFieldValueExpression extends ValueExpression {
        @Override
        public Class<?> getExpectedType() {
            return String.class;
        }

        @Override
        public Class<?> getType(ELContext context) {
            return String.class;
        }

        @Override
        public boolean isReadOnly(ELContext context) {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override
        public String getExpressionString() {
            return null;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean isLiteralText() {
            return false;
        }
    }

    @SuppressWarnings("serial")
    private ValueExpression createFirstLinkExecuteExpression() {
        return new StringFieldValueExpression() {
            @Override
            public void setValue(ELContext context, Object value) {
                testCommandExecute = (String) value;
            }

            @Override
            public Object getValue(ELContext context) {
                return testCommandExecute;
            }
        };
    }

    @SuppressWarnings("serial")
    private ValueExpression createNestedFirstLinkExecuteExpression() {
        return new StringFieldValueExpression() {
            @Override
            public void setValue(ELContext context, Object value) {
                testCommandRegionExecute = (String) value;
            }

            @Override
            public Object getValue(ELContext context) {
                return testCommandRegionExecute;
            }
        };
    }

    private void createView() {
        Application application = facesContext.getApplication();

        UIViewRoot viewRoot = facesContext.getViewRoot();

        UIComponent form = application.createComponent(UIForm.COMPONENT_TYPE);
        form.setId("form");
        viewRoot.getChildren().add(form);

        testCommand = createTestLink();
        testCommand.setId("testCommand");
        testCommand.setValueExpression("execute", createFirstLinkExecuteExpression());
        form.getChildren().add(testCommand);
        testCommandClientId = testCommand.getClientId(facesContext);

        UIComponent region = application.createComponent(AbstractRegion.COMPONENT_TYPE);
        region.setId("region");
        form.getChildren().add(region);
        regionClientId = region.getClientId(facesContext);

        testCommandRegion = createTestLink();
        testCommandRegion.setId("testCommandRegion");
        testCommandRegion.setValueExpression("execute", createNestedFirstLinkExecuteExpression());
        region.getChildren().add(testCommandRegion);
        testCommandRegionClientId = testCommandRegion.getClientId(facesContext);
    }

    private UIComponent createTestLink() {
        return facesContext.getApplication().createComponent(UICommand.COMPONENT_TYPE);
    }

    private void setActivatorComponentId(String clientId) {
        request.withParameter(AJAX_COMPONENT_ID_PARAMETER, clientId);
    }

    private <T> void assertSingleElementCollection(T expected, Collection<T> actual) {
        Iterator<T> iterator = actual.iterator();
        assertTrue(iterator.hasNext());

        assertEquals(expected, iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Before
    public void setUp() throws Exception {
        environment = FacesEnvironment.createEnvironment();
        environment.start();

        request = environment.createFacesRequest();
        request.start();

        facesContext = FacesContext.getCurrentInstance();
        createView();
    }

    @After
    public void tearDown() throws Exception {
        testCommandClientId = null;
        testCommandRegionClientId = null;

        testCommand = null;
        testCommandRegion = null;

        testCommandExecute = null;
        testCommandRegionExecute = null;

        facesContext = null;

        request.release();
        request = null;

        environment.release();
        environment = null;
    }

    @Test
    public void testDefaults() throws Exception {
        setActivatorComponentId(testCommandClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(testCommandClientId, executeIds);
    }

    @Test
    public void testDefaultsInRegion() throws Exception {
        setActivatorComponentId(testCommandRegionClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(regionClientId, executeIds);
    }

    @Test
    public void testExecuteThis() throws Exception {
        testCommandExecute = THIS;
        setActivatorComponentId(testCommandClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(testCommandClientId, executeIds);
    }

    @Test
    public void testExecuteThisInRegion() throws Exception {
        testCommandRegionExecute = THIS;
        setActivatorComponentId(testCommandRegionClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(testCommandRegionClientId, executeIds);
    }

    @Test
    public void testExecuteAll() throws Exception {
        testCommandExecute = ALL;
        setActivatorComponentId(testCommandClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(ALL, executeIds);
    }

    @Test
    public void testExecuteAllInRegion() throws Exception {
        testCommandRegionExecute = ALL;
        setActivatorComponentId(testCommandRegionClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(ALL, executeIds);
    }

    @Test
    public void testExecuteRegion() throws Exception {
        testCommandExecute = AjaxContainer.META_CLIENT_ID;
        setActivatorComponentId(testCommandClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(testCommandClientId, executeIds);
    }

    @Test
    public void testExecuteRegionInRegion() throws Exception {
        testCommandRegionExecute = AjaxContainer.META_CLIENT_ID;
        setActivatorComponentId(testCommandRegionClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(regionClientId, executeIds);
    }
}
