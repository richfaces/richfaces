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
package org.richfaces.javascript;

import com.google.common.collect.Iterables;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockController;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.jboss.test.faces.mock.Stub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.ServicesFactory;
import org.richfaces.component.UIScripts;
import org.richfaces.resource.ResourceKey;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(MockTestRunner.class)
public class JavaScriptServiceTest {
    static final ResourceKey FOO_RESOURCE = ResourceKey.create("foo", "org.rf");
    protected UIScripts scriptResource;
    protected JavaScriptServiceImpl serviceImpl;
    @Mock()
    @Environment({ Feature.APPLICATION, Feature.RENDER_KIT, Feature.EL_CONTEXT })
    protected MockFacesEnvironment environment;
    protected MockController controller;
    @Mock
    protected ValueExpression expression;
    @Stub
    protected ServicesFactory factory;

    @After
    public void tearDown() throws Exception {
        controller.release();
    }

    @Before
    public void setUpResource() {
        serviceImpl = new JavaScriptServiceImpl() {
            @Override
            UIScripts getOrCreateScriptResource(FacesContext facesContext) {
                return scriptResource;
            }
        };
        scriptResource = new UIScripts();
    }

    @After
    public void cleanUpResource() {
        scriptResource = null;
    }

    @Test
    public void testAddOrFindScript() {
        Object script = createScript("foo bar");
        Object script2 = serviceImpl.addScript(environment.getFacesContext(), script);
        Collection<Object> scripts = scriptResource.getScripts();
        assertEquals(1, scripts.size());
        assertSame(script, Iterables.getOnlyElement(scripts));
    }

    @Test
    public void testAddOrFindScript2() {
        Object script = createScript("foo bar");
        serviceImpl.addScript(environment.getFacesContext(), script);
        Object script2 = createScript("fooz baz bar");
        Object script3 = serviceImpl.addScript(environment.getFacesContext(), script2);
        Collection<Object> scripts = scriptResource.getScripts();
        assertEquals(2, scripts.size());
        assertSame(script2, script3);
    }

    @Test
    public void testAddOrFindScript3() {
        Object script = createScript("foo bar");
        serviceImpl.addScript(environment.getFacesContext(), script);
        Object script2 = createScript("foo bar");
        Object script3 = serviceImpl.addScript(environment.getFacesContext(), script2);
        Collection<Object> scripts = scriptResource.getScripts();
        assertEquals(1, scripts.size());
        assertSame(script, script3);
    }

    private Object createScript(String content) {
        return content;
    }
}
