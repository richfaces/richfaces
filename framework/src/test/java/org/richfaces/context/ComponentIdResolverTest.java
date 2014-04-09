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
package org.richfaces.context;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.renderkit.AjaxConstants;

/**
 * @author Nick Belaevski
 *
 */
public class ComponentIdResolverTest {
    static final String META_COMPONENT_ID = "testId";
    static final String META_COMPONENT_ID_SUBSTITUTION = "@substitutedTestId";
    private static final String META_CLIENT_ID = MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + META_COMPONENT_ID;
    private FacesEnvironment environment;
    private FacesRequest facesRequest;
    private FacesContext facesContext;
    private UIViewRoot viewRoot;

    @Before
    public void setUp() throws Exception {
        ComponentIdResolver.setMetaComponentSubstitutions(Collections.singletonMap(META_COMPONENT_ID,
            META_COMPONENT_ID_SUBSTITUTION));

        environment = FacesEnvironment.createEnvironment();

        environment.withResource("/test.xhtml", getClass().getResource("/org/richfaces/context/ComponentIdResolver.xhtml"));

        environment.withResource("/WEB-INF/faces-config.xml",
            getClass().getResource("/org/richfaces/context/ComponentIdResolver.config.xml"));

        environment.start();

        facesRequest = environment.createFacesRequest();
        facesRequest.start();

        facesContext = FacesContext.getCurrentInstance();
        viewRoot = facesContext.getViewRoot();

        facesContext.getExternalContext().getRequestMap().put("one", Arrays.asList(1));

        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(facesContext, viewRoot.getViewId());
        vdl.buildView(facesContext, viewRoot);
    }

    @After
    public void tearDown() throws Exception {
        viewRoot = null;
        facesContext = null;

        facesRequest.release();
        facesRequest = null;

        environment.release();
        environment = null;
    }

    private <T> Set<T> asSet(T... elements) {
        Set<T> set = new HashSet<T>();

        for (T element : elements) {
            set.add(element);
        }

        return set;
    }

    private ComponentIdResolver createComponentIdResolver() {
        return new ComponentIdResolver(facesContext);
    }

    private UIComponent evaluateComponentExpression(String expression) {
        return (UIComponent) facesContext.getApplication().evaluateExpressionGet(facesContext, expression, UIComponent.class);
    }

    @Test
    public void testFindBySimpleId() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId("input");
        resolver.addId("column");
        resolver.addId("header");
        resolver.resolve(viewRoot);

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("form:table:input", "form:table:header", "form:table:column"), resolvedIds);
    }

    @Test
    public void testFindBySimpleIdInContext() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId("input");
        resolver.addId("column");
        resolver.addId("header");

        resolver.resolve(evaluateComponentExpression("#{testBean.table}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("form:table:input", "form:table:header", "form:table:column"), resolvedIds);
    }

    @Test
    public void testFindByRowsId() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId("table:@rows(one):input");

        resolver.resolve(evaluateComponentExpression("#{testBean.table}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("form:table:1:input"), resolvedIds);
    }

    @Test
    public void testFindByMetaComponentId() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId("input@text");
        resolver.addId("table:@rows(one):header@head");
        resolver.addId("table:@rows(one):header@footer");

        resolver.resolve(viewRoot);

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("form:table:1:header@footer", "form:table:1:header@head", "form:table:input@text"), resolvedIds);
    }

    @Test
    public void testFindWithNoParentContainer() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId("form:table:@rows(one):column");

        resolver.resolve(evaluateComponentExpression("#{testBean.table}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("form:table:1:column"), resolvedIds);
    }

    @Test
    public void testFindNonExistent() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId("nonExistentId");
        resolver.addId("xForm:nonExistentId");
        resolver.addId(":ySubview:nonExistentId");

        resolver.resolve(viewRoot);

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("nonExistentId", "xForm:nonExistentId", "ySubview:nonExistentId"), resolvedIds);
    }

    @Test
    public void testFindNonExistentWithContext() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId("nonExistentId");
        resolver.addId("xForm:nonExistentId");
        resolver.addId(":ySubview:nonExistentId");

        resolver.resolve(evaluateComponentExpression("#{testBean.table}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("nonExistentId", "xForm:nonExistentId", "ySubview:nonExistentId"), resolvedIds);
    }

    @Test
    public void testFindViaMetadataResolverInContext() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId(META_CLIENT_ID);

        resolver.resolve(evaluateComponentExpression("#{testBean.outputInRegion}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("firstRegion"), resolvedIds);
    }

    @Test
    public void testFindViaMetadataResolverOutContext() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId(META_CLIENT_ID);

        resolver.resolve(evaluateComponentExpression("#{testBean.outputOutRegion}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet(META_COMPONENT_ID_SUBSTITUTION), resolvedIds);
    }

    @Test
    public void testAbsoluteIds() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();
        resolver.addId(":form:table:input");
        resolver.addId(":form:table:column@head");
        resolver.addId(":form:table:@rows(one):column");

        resolver.resolve(viewRoot);

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("form:table:input", "form:table:column@head", "form:table:1:column"), resolvedIds);
    }

    @Test
    public void testUnresolvedMetaComponentSubstitutionCompatibility() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();

        resolver.addId(META_CLIENT_ID);

        resolver.resolve(evaluateComponentExpression("#{testBean.linkInRegion}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet("firstRegion"), resolvedIds);
    }

    @Test
    public void testUnresolvedMetaComponentSubstitution() throws Exception {
        ComponentIdResolver resolver = createComponentIdResolver();

        resolver.addId(META_CLIENT_ID);

        resolver.resolve(evaluateComponentExpression("#{testBean.linkOutRegion}"));

        Set<String> resolvedIds = resolver.getResolvedIds();
        assertEquals(asSet(AjaxConstants.ALL), resolvedIds);
    }
}
