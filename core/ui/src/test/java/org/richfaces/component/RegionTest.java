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

import java.util.Collection;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



/**
 * Test page pseudo-code:
 * <pre>
 * &lt;h:form id=&quot;form&quot;&gt;
 *    &lt;a4j:testLink id=&quot;link&quot; 
 *          binding=&quot;#{link}&quot; 
 *          execute=&quot;#{linkExecute}&quot; /&gt;
 *              
 *    &lt;a4j:region id=&quot;region&quot;&gt;
 *        &lt;a4j:testLink id=&quot;regionLink&quot; 
 *          binding=&quot;#{regionLink}&quot; 
 *          execute=&quot;#{regionLinkExecute}&quot; /&gt;
 *              
 *    &lt;/a4j:region&gt;
 * &lt;/h:form&gt;
 * </pre>
 * 
 * 
 * @author Nick Belaevski
 * 
 */
public class RegionTest {
        
    private FacesEnvironment environment;

    private FacesRequest request;

    private FacesContext facesContext;
    
    private UIComponent link;
    
    private String linkExecute;
    
    private UIComponent regionLink;
    
    private String regionLinkExecute;
    
    private String linkClientId;
    
    private String regionLinkClientId;

    private String regionClientId;
    
    @SuppressWarnings("serial")
    private static abstract class StringFieldValueExpression extends ValueExpression {

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
                linkExecute = (String) value;
            }
            
            @Override
            public Object getValue(ELContext context) {
                return linkExecute;
            }
        };
    }
    
    @SuppressWarnings("serial")
    private ValueExpression createNestedFirstLinkExecuteExpression() {
        return new StringFieldValueExpression() {
            
            @Override
            public void setValue(ELContext context, Object value) {
                regionLinkExecute = (String) value;
            }
            
            @Override
            public Object getValue(ELContext context) {
                return regionLinkExecute;
            }
        };
    }

    private void createView() {
        Application application = facesContext.getApplication();
        
        UIViewRoot viewRoot = facesContext.getViewRoot();

        UIComponent form = application.createComponent(UIForm.COMPONENT_TYPE);
        form.setId("form");
        viewRoot.getChildren().add(form);
        
        link = createTestLink();
        link.setId("link");
        link.setValueExpression("execute", createFirstLinkExecuteExpression());
        form.getChildren().add(link);
        linkClientId = link.getClientId(facesContext);

        UIComponent region = application.createComponent(AbstractRegion.COMPONENT_TYPE);
        region.setId("region");
        form.getChildren().add(region);
        regionClientId = region.getClientId(facesContext);
        
        regionLink = createTestLink();
        regionLink.setId("regionLink");
        regionLink.setValueExpression("execute", createNestedFirstLinkExecuteExpression());
        region.getChildren().add(regionLink);
        regionLinkClientId = regionLink.getClientId(facesContext);
    }

    private UIComponent createTestLink() {
        return facesContext.getApplication().createComponent(UICommandButton.COMPONENT_TYPE);
    }
    
    private void setActivatorComponentId(String clientId) {
        request.withParameter(AjaxRendererUtils.AJAX_COMPONENT_ID_PARAMETER, clientId);
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
        linkClientId = null;
        regionLinkClientId = null;
        
        link = null;
        regionLink = null;
        
        linkExecute = null;
        regionLinkExecute = null;
        
        facesContext = null;
        
        request.release();
        request = null;
        
        environment.release();
        environment = null;
    }
    
    @Test
    public void testDefaults() throws Exception {
        setActivatorComponentId(linkClientId);
        
        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(linkClientId, executeIds);
    }

    @Test
    public void testDefaultsInRegion() throws Exception {
        setActivatorComponentId(regionLinkClientId);
        
        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(regionClientId, executeIds);
    }

    @Test
    public void testExecuteThis() throws Exception {
        linkExecute = AjaxRendererUtils.THIS;
        setActivatorComponentId(linkClientId);
        
        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(linkClientId, executeIds);
    }

    @Test
    public void testExecuteThisInRegion() throws Exception {
        regionLinkExecute = AjaxRendererUtils.THIS;
        setActivatorComponentId(regionLinkClientId);
        
        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(regionLinkClientId, executeIds);
    }
    
    @Test
    public void testExecuteAll() throws Exception {
        linkExecute = AjaxRendererUtils.ALL;
        setActivatorComponentId(linkClientId);
        
        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(AjaxRendererUtils.ALL, executeIds);
    }
    
    @Test
    public void testExecuteAllInRegion() throws Exception {
        regionLinkExecute = AjaxRendererUtils.ALL;
        setActivatorComponentId(regionLinkClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(AjaxRendererUtils.ALL, executeIds);
    }
    
    @Test
    public void testExecuteRegion() throws Exception {
        linkExecute = AjaxContainer.META_CLIENT_ID;
        setActivatorComponentId(linkClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(linkClientId, executeIds);
    }
    
    @Test
    public void testExecuteRegionInRegion() throws Exception {
        regionLinkExecute = AjaxContainer.META_CLIENT_ID;
        setActivatorComponentId(regionLinkClientId);

        Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
        assertSingleElementCollection(regionClientId, executeIds);
    }
}
