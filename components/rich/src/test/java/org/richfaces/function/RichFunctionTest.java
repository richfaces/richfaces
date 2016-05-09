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
package org.richfaces.function;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.same;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.easymock.EasyMock;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.function.RichFunction.ComponentLocator;

/**
 * @author Nick Belaevski
 *
 */
public class RichFunctionTest {
    private static final class StubComponentLocator implements ComponentLocator {
        private UIComponent locatedComponent;

        StubComponentLocator(UIComponent locatedComponent) {
            super();
            this.locatedComponent = locatedComponent;
        }

        public UIComponent findComponent(FacesContext facesContext, UIComponent contextComponent, String id) {
            if (facesContext == null) {
                throw new NullPointerException("context");
            }

            if (contextComponent == null) {
                throw new NullPointerException("contextComponent");
            }

            if (EXISTING_TEST_ID.equals(id)) {
                return locatedComponent;
            } else if (NONEXISTING_TEST_ID.equals(id)) {
                return null;
            } else {
                fail(id);
                return null;
            }
        }
    }

    private static final String TEST_CLIENT_ID = "table:0:testId";
    private static final String TEST_JQUERY_SELECTOR = "#table\\\\:0\\\\:testId";
    private static final String EXISTING_TEST_ID = "testId";
    private static final String NONEXISTING_TEST_ID = "nonExistent";
    private MockFacesEnvironment environment;
    private ComponentLocator stubComponentLocator;
    private ComponentLocator mockComponentLocator;
    private FacesContext facesContext;
    private ExternalContext externalContext;
    private UIComponent currentComponent;
    private UIComponent locatedComponent;
    private UIViewRoot viewRoot;

    private <T extends UIComponent> T createMockComponent(Class<T> componentClass) {
        T component = environment.createMock(componentClass);
        expect(component.getAttributes()).andStubReturn(new HashMap<String, Object>());
        return component;
    }

    @Before
    public void setUp() throws Exception {
        environment = MockFacesEnvironment.createNiceEnvironment();

        facesContext = environment.getFacesContext();
        expect(facesContext.getAttributes()).andStubReturn(new HashMap<Object, Object>());

        externalContext = environment.createMock(ExternalContext.class);
        expect(facesContext.getExternalContext()).andStubReturn(externalContext);

        viewRoot = createMockComponent(UIViewRoot.class);
        expect(facesContext.getViewRoot()).andStubReturn(viewRoot);

        currentComponent = createMockComponent(UIComponent.class);

        locatedComponent = createMockComponent(UIComponent.class);
        expect(locatedComponent.getClientId(same(facesContext))).andStubReturn(TEST_CLIENT_ID);

        stubComponentLocator = new StubComponentLocator(locatedComponent);
        mockComponentLocator = environment.createMock(ComponentLocator.class);
        RichFunction.setComponentLocator(mockComponentLocator);
    }

    @After
    public void tearDown() throws Exception {
        facesContext = null;
        externalContext = null;
        currentComponent = null;
        locatedComponent = null;
        viewRoot = null;

        environment.verify();
        environment.release();
        environment = null;
    }

    @Test
    public void testFunctionsInCurrentComponentContext() throws Exception {
        expect(mockComponentLocator.findComponent(same(facesContext), eq(currentComponent), EasyMock.<String>notNull()))
                .andStubDelegateTo(stubComponentLocator);

        environment.replay();

        currentComponent.pushComponentToEL(environment.getFacesContext(), null);

        assertEquals(TEST_CLIENT_ID, RichFunction.clientId(EXISTING_TEST_ID));
        assertEquals("RichFaces.component('" + TEST_CLIENT_ID + "')", RichFunction.component(EXISTING_TEST_ID));
        assertEquals("document.getElementById('" + TEST_CLIENT_ID + "')", RichFunction.element(EXISTING_TEST_ID));
        assertEquals(TEST_JQUERY_SELECTOR, RichFunction.jQuerySelector(EXISTING_TEST_ID));
        assertEquals("RichFaces.jQuery(document.getElementById('" + TEST_CLIENT_ID + "'))", RichFunction.jQuery(EXISTING_TEST_ID));
        assertEquals(locatedComponent, RichFunction.findComponent(EXISTING_TEST_ID));

        assertNull(RichFunction.clientId(NONEXISTING_TEST_ID));
        assertNull(RichFunction.component(NONEXISTING_TEST_ID));
        assertNull(RichFunction.element(NONEXISTING_TEST_ID));
        assertNull(RichFunction.jQuerySelector(NONEXISTING_TEST_ID));
        assertEquals("RichFaces.jQuery()", RichFunction.jQuery(NONEXISTING_TEST_ID));
        assertNull(RichFunction.findComponent(NONEXISTING_TEST_ID));

        assertNull(RichFunction.clientId(null));
        assertNull(RichFunction.component(null));
        assertNull(RichFunction.element(null));
        assertNull(RichFunction.jQuerySelector((String) null));
        assertEquals("RichFaces.jQuery()", RichFunction.jQuery(null));
        assertNull(RichFunction.findComponent(null));

        currentComponent.popComponentFromEL(environment.getFacesContext());
    }

    @Test
    public void testFunctionsInViewRootContext() throws Exception {
        expect(mockComponentLocator.findComponent(same(facesContext), eq(viewRoot), EasyMock.<String>notNull()))
                .andStubDelegateTo(stubComponentLocator);

        environment.replay();

        assertEquals(TEST_CLIENT_ID, RichFunction.clientId(EXISTING_TEST_ID));
        assertEquals("RichFaces.component('" + TEST_CLIENT_ID + "')", RichFunction.component(EXISTING_TEST_ID));
        assertEquals("document.getElementById('" + TEST_CLIENT_ID + "')", RichFunction.element(EXISTING_TEST_ID));
        assertEquals(TEST_JQUERY_SELECTOR, RichFunction.jQuerySelector(EXISTING_TEST_ID));
        assertEquals("RichFaces.jQuery(document.getElementById('" + TEST_CLIENT_ID + "'))", RichFunction.jQuery(EXISTING_TEST_ID));
        assertEquals(locatedComponent, RichFunction.findComponent(EXISTING_TEST_ID));

        assertNull(RichFunction.clientId(NONEXISTING_TEST_ID));
        assertNull(RichFunction.component(NONEXISTING_TEST_ID));
        assertNull(RichFunction.element(NONEXISTING_TEST_ID));
        assertNull(RichFunction.jQuerySelector(NONEXISTING_TEST_ID));
        assertEquals("RichFaces.jQuery()", RichFunction.jQuery(NONEXISTING_TEST_ID));
        assertNull(RichFunction.findComponent(NONEXISTING_TEST_ID));

        assertNull(RichFunction.clientId(null));
        assertNull(RichFunction.component(null));
        assertNull(RichFunction.element(null));
        assertNull(RichFunction.jQuerySelector((String) null));
        assertEquals("RichFaces.jQuery()", RichFunction.jQuery(null));
        assertNull(RichFunction.findComponent(null));
    }

    @Test
    public void testIsUserInRole() throws Exception {
        expect(externalContext.isUserInRole(eq("admin"))).andReturn(false);
        expect(externalContext.isUserInRole(eq("user"))).andReturn(true);

        expect(externalContext.isUserInRole(eq("manager"))).andReturn(false);
        expect(externalContext.isUserInRole(eq("guest"))).andReturn(false);
        expect(externalContext.isUserInRole(eq("supervisor"))).andReturn(true);

        expect(externalContext.isUserInRole(eq("auditor"))).andReturn(false);

        environment.replay();

        assertTrue(RichFunction.isUserInRole("admin, user, root"));

        Set<String> set = new LinkedHashSet<String>();
        set.add("manager");
        set.add("guest");
        set.add("supervisor");

        assertTrue(RichFunction.isUserInRole(set));

        assertFalse(RichFunction.isUserInRole("auditor"));

        assertFalse(RichFunction.isUserInRole(null));
    }
}
