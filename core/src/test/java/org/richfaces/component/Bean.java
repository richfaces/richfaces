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

package org.richfaces.component;

import java.util.Collections;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSLiteral;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;
import org.richfaces.application.ServiceTracker;

@RequestScoped
@ManagedBean(name = "test")
public class Bean {

    private static final class TestScript extends JSLiteral implements ResourceLibrary {
        public TestScript() {
            super(TEST_SCRIPT);
        }

        public Iterable<ResourceKey> getResources() {
            return Collections.singleton(TEST_RESOURCE);
        }
    }

    public static final String TEST_SCRIPT_NAME = "test_script";
    public static final String FOO_BAR = "foo.bar";
    public static final String FOO = "foo";
    public static final String FOO_VALUE = "fooValue";
    public static final String TEST_SCRIPT = "function " + FOO + "(id){alert(id);}";
    private static final ResourceKey TEST_RESOURCE = ResourceKey.create(TEST_SCRIPT_NAME + ".js", FOO_BAR);
    private static final ResourceLibrary SCRIPT = new TestScript();
    private String value = FOO_VALUE;

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String action() {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javaScriptService.addPageReadyScript(facesContext, SCRIPT);
        return null;
    }
}
