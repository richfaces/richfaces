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

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class NestedRepeatTest {
    private HtmlUnitEnvironment environment;

    @Before
    public void setUp() throws Exception {
        environment = new CustomizedHtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/component/faces-config.xml");
        environment.start();

        environment.getServer().getSession().setAttribute("dataBean", new DataBean());
    }

    @After
    public void tearDown() throws Exception {
        environment.release();
        environment = null;
    }

    @Test
    public void testRendering() throws Exception {
        HtmlPage page = environment.getPage("/NestedRepeatTest.jsf");

        for (int i = 0; i < 3; i++) {
            HtmlElement input = page.getElementById("form:outer:" + i + ":inner:0:input");
            input.type(Integer.toString(i));
        }

        HtmlElement ajax = page.getElementById("form:ajax");
        page = ajax.click();

        for (int i = 0; i < 3; i++) {
            HtmlElement input = page.getElementById("form:outer:" + i + ":inner:0:input");
            assertEquals(Integer.toString(i), input.getAttribute("value"));
        }
    }

    public static class DataBean implements Serializable {
        private List<DataItem> listDataItems = new LinkedList<DataItem>() {
            {
                add(new DataItem());
                add(new DataItem());
                add(new DataItem());
            }
        };

        public List<DataItem> getListDataItems() {
            return listDataItems;
        }

        public void setListDataItems(List<DataItem> listDataItems) {
            this.listDataItems = listDataItems;
        }
    }

    public static class DataItem implements Serializable {
        private List<Inner> list = new LinkedList<Inner>() {
            {
                add(new Inner());
            }
        };

        public List<Inner> getList() {
            return list;
        }

        public void setList(List<Inner> list) {
            this.list = list;
        }
    }

    public static class Inner implements Serializable {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
