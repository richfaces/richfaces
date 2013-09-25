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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Nick Belaevski
 *
 */
public class RepeatTest {
    private static final int TEST_DATA_SIZE = 40;

    public static class TestBean {
        private List<String> data = new ArrayList<String>();
        private int first;
        private int rows;
        private int switchedFirst;
        private int switchedRows;

        public List<String> getData() {
            return data;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public int getFirst() {
            return first;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public int getRows() {
            return rows;
        }

        public int getSwitchedFirst() {
            return switchedFirst;
        }

        public void setSwitchedFirst(int switchedFirst) {
            this.switchedFirst = switchedFirst;
        }

        public int getSwitchedRows() {
            return switchedRows;
        }

        public void setSwitchedRows(int switchedRows) {
            this.switchedRows = switchedRows;
        }

        public void switchFirstAndRows() {
            first = switchedFirst;
            rows = switchedRows;
        }
    }

    private TestBean testBean;
    private HtmlUnitEnvironment environment;

    private void setupTestBean() {
        testBean = new TestBean();

        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            testBean.getData().add(UUID.randomUUID().toString());
        }
    }

    @Before
    public void setUp() throws Exception {
        setupTestBean();

        environment = new CustomizedHtmlUnitEnvironment();

        environment.withResource("/WEB-INF/classes/faces-config.xml",
            getClass().getResource("/org/richfaces/component/RepeatTest.faces-config.xml"));

        environment.withResource("/test.xhtml", getClass().getResource("/org/richfaces/component/RepeatTest.xhtml"));

        environment.start();

        environment.getServer().getSession(true).setAttribute("testBean", testBean);
    }

    @After
    public void tearDown() throws Exception {
        testBean = null;

        environment.release();
        environment = null;
    }

    private List<HtmlElement> getRepeatContent(HtmlPage page) {
        List<?> xPathResult = page.getByXPath("//div[@id='repeatWrapper']/*");
        List<HtmlElement> result = new ArrayList<HtmlElement>();

        for (Object xPathResultObject : xPathResult) {
            result.add(HtmlElement.class.cast(xPathResultObject));
        }

        return result;
    }

    private void checkRendering(HtmlPage page, int first, int rows) {
        List<HtmlElement> content = getRepeatContent(page);
        List<String> testData = testBean.getData();
        assertEquals(rows, content.size());
        for (int i = 0; i < rows; i++) {
            HtmlElement element = content.get(i);

            int key = i + first;
            assertEquals(MessageFormat.format("form:repeat:{0}:child", key), element.getId());
            assertEquals(testData.get(key), element.<HtmlElement>getFirstByXPath("*[contains(@id, ':item')]").getTextContent()
                .trim());

            String iterationStatusString = MessageFormat.format("begin: {0}, end: {1}, index: {2}, rowCount: {3}", first, first
                + rows - 1, key, testData.size());

            assertEquals(iterationStatusString, element.<HtmlElement>getFirstByXPath("*[contains(@id, ':iterationStatus')]")
                .getTextContent().trim());
        }
    }

    @Test
    public void testRendering() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        checkRendering(page, 0, TEST_DATA_SIZE);
    }

    @Test
    public void testFirst() throws Exception {
        testBean.setFirst(5);
        HtmlPage page = environment.getPage("/test.jsf");
        checkRendering(page, 5, TEST_DATA_SIZE - 5);
    }

    @Test
    public void testRows() throws Exception {
        testBean.setRows(6);
        HtmlPage page = environment.getPage("/test.jsf");
        checkRendering(page, 0, 6);
    }

    @Test
    public void testSwitchFirstAndRows() throws Exception {
        testBean.setFirst(0);
        testBean.setRows(10);

        testBean.setSwitchedFirst(15);
        testBean.setSwitchedRows(7);

        HtmlPage page = environment.getPage("/test.jsf");
        checkRendering(page, 0, 10);

        page = (HtmlPage) page.getElementById("form:switchFirstAndRowsLink").click();

        assertEquals(15, testBean.getFirst());
        assertEquals(7, testBean.getRows());
    }
}
