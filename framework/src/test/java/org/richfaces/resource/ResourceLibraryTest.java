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
package org.richfaces.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

/**
 * @author Nick Belaevski
 *
 */
public class ResourceLibraryTest {
    @ResourceDependency(library = "org.richfaces", name = "static.reslib")
    public static class StaticLibraryComponent extends UIComponentBase {
        @Override
        public String getFamily() {
            return null;
        }
    }

    @ResourceDependency(library = "org.richfaces", name = "dynamic.reslib")
    public static class DynamicLibraryComponent extends UIComponentBase {
        @Override
        public String getFamily() {
            return null;
        }
    }

    private String nextUri(Iterator<?> itr) {
        assertTrue(itr.hasNext());

        Element elt = (Element) itr.next();

        if (elt instanceof HtmlScript) {
            return ((HtmlScript) elt).getSrcAttribute();
        } else if (elt instanceof HtmlLink) {
            return ((HtmlLink) elt).getHrefAttribute();
        }

        throw new UnsupportedOperationException(elt.toString());
    }

    private HtmlUnitEnvironment environment;

    @Before
    public void setUp() throws Exception {
        environment = new CustomizedHtmlUnitEnvironment();
        environment.getServer().addInitParameter("org.richfaces.enableControlSkinning", "false");

        environment.withResource("/index.xhtml", "org/richfaces/resource/simple.xhtml");
        environment.start();
    }

    @After
    public void tearDown() throws Exception {
        environment.release();
        environment = null;
    }

    @Test
    public void testStaticLibrary() throws Exception {
        environment.getApplication().addComponent("testComponent", StaticLibraryComponent.class.getName());

        HtmlPage page = environment.getPage("/index.jsf");
        Iterator<?> itr = page.getByXPath("//head/link | //head/script").iterator();

        String uri;

        uri = nextUri(itr);
        assertTrue(uri.contains("jsf.js"));
        assertTrue(uri.contains("javax.faces"));

        uri = nextUri(itr);
        assertTrue(uri.contains("jquery.js"));

        uri = nextUri(itr);
        assertTrue(uri.contains("richfaces.js"));

        uri = nextUri(itr);
        assertTrue(uri.contains("skinning_both.ecss"));

        assertFalse(itr.hasNext());
    }

    @Test
    public void testDynamicLibrary() throws Exception {
        environment.getApplication().addComponent("testComponent", DynamicLibraryComponent.class.getName());

        HtmlPage page = environment.getPage("/index.jsf");
        Iterator<?> itr = page.getByXPath("//head/link | //head/script").iterator();

        String uri;

        uri = nextUri(itr);
        assertTrue(uri.contains("skinning_classes.ecss"));

        uri = nextUri(itr);
        assertTrue(uri.contains("jquery.js"));

        assertFalse(itr.hasNext());
    }
}
